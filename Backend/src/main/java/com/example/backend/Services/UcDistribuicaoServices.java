package com.example.backend.Services;

import com.example.backend.Dto.Atribuicoes.*;
import com.example.backend.Dto.Departamentos.ListarDepartamentosDto;
import com.example.backend.Entity.*;
import com.example.backend.Enums.EstadoCurso;
import com.example.backend.Enums.EstadoUc;
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

import java.util.ArrayList;
import java.util.List;


@Service
public class UcDistribuicaoServices {

    //  logger definido no logback-spring.xml
    private static final Logger auditLogger = LoggerFactory.getLogger("auditoria");

    @Autowired
    private UcDistribuicaoRepository ucDistribuicaoRepository;

    @Autowired
    private DocenteRepository docenteRepository;

    @Autowired
    private UcTipoAulaRepository  ucTipoAulaRepository;

    @Autowired
    private UcJuncaoRepository ucJuncaoRepository;

    @Autowired
    private UtilsServices utilsServices;

    @Transactional
    public void GerarUcDistribuicoes(Integer distribuicao_id, Integer departamento_id){

        // Verificar se existe a distribuição, docente e uc_tipo_aula
        Distribuicao distribuicao = utilsServices.getDistribuicaoComAcesso(distribuicao_id, departamento_id);

        List<UC_Tipo_Aula> uc_tipo_aulas = ucTipoAulaRepository.findByUc_Curso_DepartamentoIdAndUc_EstadoNotAndUc_Curso_EstadoNot(departamento_id, EstadoUc.INATIVO, EstadoCurso.INATIVO);

        List<UC_Distribuicao> ucDistribuicoes = new ArrayList<>();

        for (UC_Tipo_Aula uc_tipo_aula : uc_tipo_aulas) {

            UC_Distribuicao ucDistribuicao = new UC_Distribuicao();

            ucDistribuicao.setHoras_atribuidas(uc_tipo_aula.getHoras());
            ucDistribuicao.setUcTipoAula(uc_tipo_aula);
            ucDistribuicao.setDistribuicao(distribuicao);
            ucDistribuicao.setEstado(EstadoUcDistribuicao.ATIVO);

            ucDistribuicoes.add(ucDistribuicao);

        }

        ucDistribuicaoRepository.saveAll(ucDistribuicoes);

    }

    @Transactional
    public List<ListarUcDistribuicoesDto> getUcDistribuicoes(){

        //Vai buscar os campos das UcDistribuicoes e ainda informacoes pertinentes sobre a uc e curso
        return ucDistribuicaoRepository.findAll()
                .stream()
                .map(ucDistribuicao -> new ListarUcDistribuicoesDto(
                        ucDistribuicao.getId(),
                        ucDistribuicao.getTurno(),
                        ucDistribuicao.getHoras_atribuidas(),
                        ucDistribuicao.getDocente().getId(),
                        ucDistribuicao.getDocente().getNome(),
                        new UcCursoDto(
                                ucDistribuicao.getUcTipoAula().getUc().getCurso().getNome(),
                                ucDistribuicao.getUcTipoAula().getUc().getCurso().getDepartamento().getNome(),
                                ucDistribuicao.getUcTipoAula().getUc().getNome(),
                                ucDistribuicao.getUcTipoAula().getUc().getAno_curricular(),
                                ucDistribuicao.getUcTipoAula().getUc().getSemestre(),
                                ucDistribuicao.getUcTipoAula().getTipo_aula().getNome()

                        )

                ))
                .toList();


    }


    @Transactional
    public void CriarUcDistribuicao(Integer ucDistribuicao_id, Integer distribuicao_id, Integer departamento_id, Integer utilizador_id) {

        // Verificar se existe a distribuição, docente e uc_tipo_aula
        Distribuicao distribuicao = utilsServices.getDistribuicaoComAcesso(distribuicao_id, departamento_id);

        UC_Distribuicao ucDistribuicaoPai = ucDistribuicaoRepository.findById(ucDistribuicao_id)
                .orElseThrow(() -> new RuntimeException("UcDistribuicao não encontrada"));

        Docente docente = null;
        if(ucDistribuicaoPai.getDocente() != null){
            docente = docenteRepository.findById(ucDistribuicaoPai.getDocente().getId())
                    .orElseThrow(() -> new RuntimeException("Docente não encontrado"));
        }


        UC_Tipo_Aula ucTipoAula = null;
        if (ucDistribuicaoPai.getUcTipoAula() != null) {
            ucTipoAula = ucTipoAulaRepository.findById(ucDistribuicaoPai.getUcTipoAula().getId())
                    .orElseThrow(() -> new RuntimeException("UC tipo aula não encontrada"));
        }

        UC_Distribuicao ucDistribuicao = new UC_Distribuicao();
        ucDistribuicao.setTurno("");
        ucDistribuicao.setHoras_atribuidas(ucDistribuicaoPai.getHoras_atribuidas());
        ucDistribuicao.setDocente(docente);
        ucDistribuicao.setUcTipoAula(ucTipoAula);
        ucDistribuicao.setDistribuicao(distribuicao);
        ucDistribuicao.setEstado(EstadoUcDistribuicao.ATIVO);
        ucDistribuicao.setUcDistribuicaPai(ucDistribuicaoPai);

        ucDistribuicaoRepository.save(ucDistribuicao);

        // LOG DE AUDITORIA: Criação
        auditLogger.info("Utilizador ID: {} CRIOU UC_Distribuicao ID {} Turno da UC_DistribuicaoPai: {} (Turno: {}, Horas: {}) na Distribuição ID: {} (Departamento ID: {}). Docente ID: {}, UCTipoAula ID: {}.",
                utilizador_id, ucDistribuicao.getId(), ucDistribuicao.getUcDistribuicaPai().getId(), ucDistribuicao.getTurno(), ucDistribuicao.getHoras_atribuidas(), distribuicao_id, departamento_id,
                ucDistribuicaoPai.getDocente() != null ? ucDistribuicaoPai.getDocente() : "N/A",
                ucDistribuicaoPai.getUcTipoAula() != null ? ucDistribuicaoPai.getUcTipoAula() : "N/A");

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
