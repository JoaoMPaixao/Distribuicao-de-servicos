package com.example.backend.Services;

import com.example.backend.Dto.Atribuicoes.AlterarUcDistribuicaoDto;
import com.example.backend.Dto.Atribuicoes.CriarUcDistribuicoesDto;
import com.example.backend.Dto.Atribuicoes.CriarUcJuncaoDto;
import com.example.backend.Dto.Distribuicoes.CriarDistribuicaoDto;
import com.example.backend.Entity.*;
import com.example.backend.Enums.EstadoDistribuicao;
import com.example.backend.Enums.EstadoUcDistribuicao;
import com.example.backend.Repository.*;
import com.example.backend.Utils.UtilsServices;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Service
public class UcDistribuicaoServices {

    //  logger definido no logback-spring.xml
    private static final Logger auditLogger = LoggerFactory.getLogger("auditoria");

    @Autowired
    private UcDistribuicaoRepository ucDistribuicaoRepository;

    @Autowired
    private DistribuicaoRepository distribuicaoRepository;

    @Autowired
    private DocenteRepository docenteRepository;

    @Autowired
    private UcTipoAulaRepository  ucTipoAulaRepository;

    @Autowired
    private UcJuncaoRepository ucJuncaoRepository;

    @Autowired
    private UtilsServices utilsServices;


    @Transactional
    public void CriarUcDistribuicao(CriarUcDistribuicoesDto dto, Integer distribuicao_id, Integer departamento_id, Integer utilizador_id) {

        // Verificar se existe a distribuição, docente e uc_tipo_aula
        Distribuicao distribuicao = utilsServices.getDistribuicaoComAcesso(distribuicao_id, departamento_id);

        Docente docente = null;
        if(dto.docenteId() != null){
            docente = docenteRepository.findById(dto.docenteId())
                    .orElseThrow(() -> new RuntimeException("Docente não encontrado"));
        }


        UC_Tipo_Aula ucTipoAula = null;
        if (dto.Uc_tipo_aula_id() != null) {
            ucTipoAula = ucTipoAulaRepository.findById(dto.Uc_tipo_aula_id())
                    .orElseThrow(() -> new RuntimeException("UC tipo aula não encontrada"));
        }



        UC_Distribuicao ucDistribuicao = new UC_Distribuicao();
        ucDistribuicao.setTurno(dto.turno());
        ucDistribuicao.setHoras_atribuidas(dto.horasAtribuidas());
        ucDistribuicao.setDocente(docente);
        ucDistribuicao.setDistribuicao(distribuicao);
        ucDistribuicao.setUcTipoAula(ucTipoAula);
        ucDistribuicao.setEstado(EstadoUcDistribuicao.ATIVO);


        ucDistribuicaoRepository.save(ucDistribuicao);

        // LOG DE AUDITORIA: Criação
        auditLogger.info("Utilizador ID: {} CRIOU UC_Distribuicao ID: {} (Turno: {}, Horas: {}) na Distribuição ID: {} (Departamento ID: {}). Docente ID: {}, UCTipoAula ID: {}.",
                utilizador_id, ucDistribuicao.getId(), dto.turno(), dto.horasAtribuidas(), distribuicao_id, departamento_id,
                dto.docenteId() != null ? dto.docenteId() : "N/A",
                dto.Uc_tipo_aula_id() != null ? dto.Uc_tipo_aula_id() : "N/A");

    }

    @Transactional
    public void CriarUcJuncao(CriarUcJuncaoDto dto, Integer distribuicao_id, Integer departamento_id, Integer utilizador_id) {

        UC_Tipo_Aula ucTipoAula = ucTipoAulaRepository.findById(dto.Uc_tipo_aula_id())
                .orElseThrow(() -> new RuntimeException("UC tipo aula não encontrada"));

        UC_Distribuicao ucDistribuicao = ucDistribuicaoRepository.findById(dto.UcDistribuicao_id())
                .orElseThrow(() -> new RuntimeException("UC tipo aula não encontrada"));

        //verifica se a distribuicao e o utilizador têm o mesmo departamento
        utilsServices.validarAcessoDistribuicao(distribuicao_id, departamento_id);


        UC_Juncao ucJuncao = new UC_Juncao();
        ucJuncao.setUcDistribuicao(ucDistribuicao);
        ucJuncao.setUcTipoAula(ucTipoAula);

        ucJuncaoRepository.save(ucJuncao);

        // LOG DE AUDITORIA: Junção
        auditLogger.info("Utilizador ID: {} CRIOU UC_Juncao ID: {} ligando UC_Distribuicao ID: {} à UC_Tipo_Aula ID: {} na Distribuição ID: {} (Departamento ID: {}).",
                utilizador_id, ucJuncao.getId(), dto.UcDistribuicao_id(), dto.Uc_tipo_aula_id(), distribuicao_id, departamento_id);

    }

    @Transactional
    public void DeleteUcDistribuicao(Integer distribuicao_id, Integer ucDistribuicao_id, Integer departamento_id, Integer utilizador_id) {

        //verifica se a distribuicao e o utilizador têm o mesmo departamento
        utilsServices.validarAcessoDistribuicao(distribuicao_id, departamento_id);

        // valida se a ucDistribuicao existe antes de tentar apagar
        if (!ucDistribuicaoRepository.existsById(ucDistribuicao_id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Atribuição não encontrada");
        }


        System.out.println(ucDistribuicao_id);
        ucDistribuicaoRepository.deleteById(ucDistribuicao_id);

        // LOG DE AUDITORIA: Remoção
        auditLogger.info("Utilizador ID: {} APAGOU UC_Distribuicao ID: {} da Distribuição ID: {} (Departamento ID: {}).",
                utilizador_id, ucDistribuicao_id, distribuicao_id, departamento_id);

    }

    @Transactional
    public void AlterarUcDistribuicao(AlterarUcDistribuicaoDto dto, Integer distribuicao_id, Integer ucDistribuicao_id, Integer departamento_id, Integer utilizador_id) {

        //verifica se a distribuicao e o utilizador têm o mesmo departamento
        utilsServices.validarAcessoDistribuicao(distribuicao_id, departamento_id);

        UC_Distribuicao uc_distribuicao = ucDistribuicaoRepository.findById(ucDistribuicao_id)
                .orElseThrow(() -> new EntityNotFoundException("UC Distribuicao não encontrada"));

        Docente docente = null;
        if(dto.docenteID() != null){
            docente = docenteRepository.findById(dto.docenteID())
                    .orElseThrow(() -> new RuntimeException("Docente não encontrado"));
        }


        uc_distribuicao.setDocente(docente);
        uc_distribuicao.setHoras_atribuidas(dto.horasAtribuidas());

        // LOG DE AUDITORIA: Alteração
        auditLogger.info("Utilizador ID: {} ALTEROU UC_Distribuicao ID: {} na Distribuição ID: {} (Departamento ID: {}). Docente: -> {}. Horas: -> {}.",
                utilizador_id, ucDistribuicao_id, distribuicao_id, departamento_id,
                 (dto.docenteID() != null ? dto.docenteID() : "N/A"), dto.horasAtribuidas());

    }
}
