package com.example.backend.Services;

import com.example.backend.Dto.Atribuicoes.CalculosUcDistribuicaoDto;
import com.example.backend.Dto.Atribuicoes.DadosUcDistribuicaoDto;
import com.example.backend.Dto.Atribuicoes.ListarUcDistribuicoesDto;
import com.example.backend.Dto.Cursos.AlterarCursosDto;
import com.example.backend.Dto.Cursos.CriarCursosDto;
import com.example.backend.Dto.Departamentos.ListarDepartamentosDto;
import com.example.backend.Dto.Docentes.*;
import com.example.backend.Entity.ContratoDocente;
import com.example.backend.Entity.Curso;
import com.example.backend.Entity.Departamento;
import com.example.backend.Entity.Docente;
import com.example.backend.Enums.EstadoContrato;
import com.example.backend.Enums.EstadoDocente;
import com.example.backend.Repository.ContratoDocenteRepository;
import com.example.backend.Repository.DepartamentoRepository;
import com.example.backend.Repository.DistribuicaoRepository;
import com.example.backend.Repository.DocenteRepository;
import com.example.backend.Utils.UtilsServices;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DocenteServices {

    // Logger a apontar para o ficheiro estabelecido no xml
    private static final Logger auditLogger = LoggerFactory.getLogger("auditoria");

    @Autowired
    private DocenteRepository docenteRepository;

    @Autowired
    private UtilsServices utilsServices;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Autowired
    private ContratoDocenteRepository contratoDocenteRepository;

    public List<ListarDocentesUcDistribuicaoDto> getDocentesUcDistribuicao (Integer distribuicao_id){

        return docenteRepository.findByEstadoNot(EstadoDocente.INATIVO)
                .stream()
                .map(docente -> new ListarDocentesUcDistribuicaoDto(
                        docente.getId(),
                        docente.getNome(),
                        docente.getDepartamento().getNome(),
                        utilsServices.getDocenteHoras(docente.getId(), distribuicao_id)

                ))
                .toList();
    }

    public List<ListarDocentesDto> getDocentes (){

        return docenteRepository.findAll()
                .stream()
                .map(docente -> {

                   ContratoDocente contrato = utilsServices.getContratoAtual(docente.getId());

                    return new ListarDocentesDto(
                            docente.getId(),
                            docente.getNome(),
                            docente.getEmail(),
                            docente.getEstado(),
                            docente.getDepartamento() != null ? docente.getDepartamento().getId() : null,
                            docente.getDepartamento() != null ? docente.getDepartamento().getNome() : null,
                            contrato != null ? contrato.getId() : null,
                            contrato != null ? contrato.getDescricao() : null,
                            contrato != null ? contrato.getEstado() : null,
                            contrato != null ? contrato.getDataInicio() : null,
                            contrato != null ? contrato.getDataFim() : null,
                            contrato != null ? contrato.getCategoria() : null,
                            contrato != null ? contrato.getRelacaoJuridica() : null,
                            contrato != null ? contrato.getDedicacao() : null
                    );
                })
                .toList();

    }

    @Transactional
    public void CriarDocente(CriarDocenteDto dto, Integer utilizador_id){

        Departamento departamento = departamentoRepository.findById(dto.departamentoId())
                .orElseThrow(() -> new RuntimeException("Departamento não encontrado"));

       Docente docente = new Docente();

       docente.setNome(dto.nome());
       docente.setEstado(dto.estado());
       docente.setDepartamento(departamento);
       docente.setEmail(dto.email());

        docenteRepository.save(docente);

        // LOG DE AUDITORIA: Criação
        auditLogger.info("Utilizador ID: {} CRIOU o Docente ID: {} ('{}') no Departamento ID: {}.",
                utilizador_id, docente.getId(), docente.getNome(), docente.getDepartamento().getId());

    }

    @Transactional
    public void AlterarDocente (AlterarDocenteDto dto, Integer docente_id, Integer utilizador_id){

        Docente docente = docenteRepository.findById(docente_id).orElseThrow(() -> new RuntimeException("Docente não encontrado"));

        Departamento departamento = departamentoRepository.findById(dto.departamentoId())
                .orElseThrow(() -> new RuntimeException("Departamento não encontrado"));

        docente.setNome(dto.nome());
        docente.setEstado(dto.estado());
        docente.setDepartamento(departamento);
        docente.setEmail(dto.email());

        // LOG DE AUDITORIA: Alteração de Curso
        auditLogger.info("Utilizador ID: {} ALTEROU o Docente ID: {} para nome {} ; para email {} ; para estado {} no Departamento ID: {}.",
                utilizador_id, docente_id, docente.getNome(), docente.getEmail(), dto.estado(), docente.getDepartamento().getId());

    }

    @Transactional
    public void DeleteDocente(Integer docente_id, Integer utilizador_id) {

        Docente docente = docenteRepository.findById(docente_id).orElseThrow(() -> new RuntimeException("Docente não encontrado"));

        contratoDocenteRepository.deleteAllByDocente_Id(docente_id);

        docenteRepository.delete(docente);

        // LOG DE AUDITORIA: Delete
        auditLogger.info("Utilizador ID: {} APAGOU o Docente ID: {}.",
                utilizador_id, docente_id);

    }

    @Transactional
    public void CriarContrato(CriarContratoDocenteDto dto, Integer utilizador_id){

        Docente docente = docenteRepository.findById(dto.docenteId()).orElseThrow(() -> new RuntimeException("Docente não encontrado"));

        ContratoDocente contratoDocenteAntigo = utilsServices.getContratoAtual(docente.getId());

        if(contratoDocenteAntigo != null){

            contratoDocenteAntigo.setDataFim(LocalDate.now());
            contratoDocenteAntigo.setEstado(EstadoContrato.INATIVO);

        }


        ContratoDocente contratoDocente = new ContratoDocente();

        contratoDocente.setCategoria(dto.contratoCategoria());
        contratoDocente.setDescricao(dto.contratoDescricao());
        contratoDocente.setEstado(EstadoContrato.ATIVO);
        contratoDocente.setDataInicio(dto.contratoDataInicio());
        contratoDocente.setDataFim(dto.contratoDataFim());
        contratoDocente.setDocente(docente);
        contratoDocente.setRelacaoJuridica(dto.contratoRelacaoJuridica());
        contratoDocente.setDedicacao(dto.contratoDedicacao());

        contratoDocenteRepository.save(contratoDocente);

        // LOG DE AUDITORIA: Criação
        auditLogger.info("Utilizador ID: {} CRIOU o ContratoDocente ID: {} para o Docente ID: {}.",
                utilizador_id, contratoDocente.getId(), contratoDocente.getDocente().getId());

    }

    @Transactional
    public void AlterarContratoDocente (CriarContratoDocenteDto dto, Integer contrato_id, Integer utilizador_id){

        Docente docente = docenteRepository.findById(dto.docenteId()).orElseThrow(() -> new RuntimeException("Docente não encontrado"));

        ContratoDocente contratoDocente = contratoDocenteRepository.findById(contrato_id)
                .orElseThrow(() -> new RuntimeException("Contrato não encontrado"));

        contratoDocente.setCategoria(dto.contratoCategoria());
        contratoDocente.setDescricao(dto.contratoDescricao());
        contratoDocente.setEstado(EstadoContrato.ATIVO);
        contratoDocente.setDataInicio(LocalDate.now());
        contratoDocente.setDataFim(dto.contratoDataFim());
        contratoDocente.setDocente(docente);
        contratoDocente.setRelacaoJuridica(dto.contratoRelacaoJuridica());
        contratoDocente.setDedicacao(dto.contratoDedicacao());


        // LOG DE AUDITORIA: Alteração de Curso
        auditLogger.info("Utilizador ID: {} ALTEROU o ContratoDocente ID: {} do Docente ID: {}.",
                utilizador_id, contrato_id, docente.getId());

    }


}
