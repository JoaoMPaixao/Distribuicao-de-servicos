package com.example.backend.Services;

import com.example.backend.Dto.Ucs.AlterarUcsDto;
import com.example.backend.Dto.Ucs.CriarUcsDto;
import com.example.backend.Dto.Ucs.ListarUcsDto;
import com.example.backend.Entity.*;
import com.example.backend.Enums.EstadoCurso;
import com.example.backend.Enums.EstadoUc;
import com.example.backend.Repository.CursoRepository;
import com.example.backend.Repository.DepartamentoRepository;
import com.example.backend.Repository.UcRepository;
import com.example.backend.Repository.UcTipoAulaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UcServices {

    // Logger a apontar para o ficheiro estabelecido no xml
    private static final Logger auditLogger = LoggerFactory.getLogger("auditoria");

    @Autowired
    private UcRepository  ucRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private UcTipoAulaRepository  ucTipoAulaRepository;

    @Autowired
    private UcTipoAulaServices ucTipoAulaServices;

    public List<ListarUcsDto> getUcs (){

        return ucRepository.findAll()
                .stream()
                .map(uc -> new ListarUcsDto(
                        uc.getId(),
                        uc.getNome(),
                        uc.getCodigo_uc(),
                        uc.getSemestre(),
                        uc.getAno_curricular(),
                        uc.getEtcs(),
                        uc.getHoras(),
                        uc.getEstado(),
                        uc.getDepartamento().getId(),
                        uc.getDepartamento().getNome(),
                        uc.getCurso().getId(),
                        uc.getCurso().getNome(),
                        ucTipoAulaServices.getUcTipoAulas()

                ))
                .toList();


    }

    @Transactional
    public void CriarUcs(CriarUcsDto dto, Integer utilizador_id){

        Departamento departamento = departamentoRepository.findById(dto.departamento_id())
                .orElseThrow(() -> new RuntimeException("Departamento não encontrado"));

        Curso curso = cursoRepository.findById(dto.curso_id()) .orElseThrow(() -> new RuntimeException("Curso não encontrado"));

        UC uc = new UC();

        uc.setNome(dto.nome());
        uc.setCodigo_uc(dto.codigo_uc());
        uc.setSemestre(dto.semestre());
        uc.setAno_curricular(dto.ano_curricular());
        uc.setEtcs(dto.ects());
        uc.setHoras(dto.horas());
        uc.setEstado(EstadoUc.ATIVO);
        uc.setDepartamento(departamento);
        uc.setCurso(curso);

        ucRepository.save(uc);

        // LOG DE AUDITORIA: Criação
        auditLogger.info("Utilizador ID: {} CRIOU a Uc ID: {} ('{}') no Departamento ID: {}.",
                utilizador_id, uc.getId(), uc.getNome(), uc.getDepartamento().getId());

    }

    @Transactional
    public void AlterarUcs(AlterarUcsDto dto, Integer Uc_id, Integer utilizador_id){

        Departamento departamento = departamentoRepository.findById(dto.departamento_id())
                .orElseThrow(() -> new RuntimeException("Departamento não encontrado"));

        Curso curso = cursoRepository.findById(dto.curso_id()).orElseThrow(() -> new RuntimeException("Curso não encontrado"));

        UC uc = ucRepository.findById(Uc_id) .orElseThrow(() -> new RuntimeException("Uc não encontrado"));

        uc.setNome(dto.nome());
        uc.setCodigo_uc(dto.codigo_uc());
        uc.setSemestre(dto.semestre());
        uc.setAno_curricular(dto.ano_curricular());
        uc.setEtcs(dto.ects());
        uc.setHoras(dto.horas());
        uc.setEstado(dto.estado());
        uc.setDepartamento(departamento);
        uc.setCurso(curso);

        // LOG DE AUDITORIA: Alteração de Curso
        auditLogger.info("Utilizador ID: {} ALTEROU a Uc ID: {} para nome {} ; para codigo_uc {} ; para estado {} no Departamento ID: {}.",
                utilizador_id, Uc_id, uc.getNome(), dto.codigo_uc(), dto.estado(), uc.getDepartamento().getId());

    }

    @Transactional
    public void DeleteUc(Integer uc_id, Integer utilizador_id) {

        //verifica se a distribuicao e o utilizador têm o mesmo departamento e retorna a distribuicao
        UC uc = ucRepository.findById(uc_id).orElseThrow(() -> new RuntimeException("Uc não encontrado"));

        ucTipoAulaRepository.deleteAllByUc_Id(uc_id);

        ucRepository.delete(uc);

        // LOG DE AUDITORIA: Delete
        auditLogger.info("Utilizador ID: {} APAGOU a Uc ID: {}.",
                utilizador_id, uc_id);

    }

}
