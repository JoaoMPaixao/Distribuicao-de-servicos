package com.example.backend.Services;

import com.example.backend.Dto.Cursos.AlterarCursosDto;
import com.example.backend.Dto.Cursos.CriarCursosDto;
import com.example.backend.Dto.Cursos.ListarCursosDto;
import com.example.backend.Entity.Curso;
import com.example.backend.Entity.Departamento;
import com.example.backend.Enums.EstadoCurso;
import com.example.backend.Repository.*;
import com.example.backend.Utils.UtilsServices;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class CursoServices {

    // Logger a apontar para o ficheiro estabelecido no xml
    private static final Logger auditLogger = LoggerFactory.getLogger("auditoria");

    @Autowired
    private UcRepository ucRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private DistribuicaoRepository distribuicaoRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Autowired
    private UcTipoAulaRepository ucTipoAulaRepository;

    @Autowired
    private UcDistribuicaoRepository ucDistribuicaoRepository;

    @Autowired
    private UtilsServices utilsServices;

    public List<ListarCursosDto> getCursos (){

        return cursoRepository.findAll()
                .stream()
                .map(curso -> new ListarCursosDto(
                        curso.getId(),
                        curso.getNome(),
                        curso.getEstado(),
                        curso.getCodigo_uc(),
                        curso.getDepartamento().getId(),
                        curso.getDepartamento().getNome()
                ))
                .toList();

    }

    public List<ListarCursosDto> getCursosByDepartamento (Integer departamento_id){

        return cursoRepository.findByDepartamentoIdAndEstadoNot(departamento_id, EstadoCurso.INATIVO)
                .stream()
                .map(curso -> new ListarCursosDto(
                        curso.getId(),
                        curso.getNome(),
                        curso.getEstado(),
                        curso.getCodigo_uc(),
                        curso.getDepartamento().getId(),
                        curso.getDepartamento().getNome()
                ))
                .toList();

    }

    @Transactional
    public void CriarCurso(CriarCursosDto dto, Integer utilizador_id){

        Departamento departamento = departamentoRepository.findById(dto.departamento_id())
                .orElseThrow(() -> new RuntimeException("Departamento não encontrado"));

        Curso curso = new Curso();
        curso.setNome(dto.nome());
        curso.setCodigo_uc(dto.codigo_uc());
        curso.setEstado(dto.estado());
        curso.setDepartamento(departamento);

        cursoRepository.save(curso);

        // LOG DE AUDITORIA: Criação
        auditLogger.info("Utilizador ID: {} CRIOU o Curso ID: {} ('{}') no Departamento ID: {}.",
                utilizador_id, curso.getId(), curso.getNome(), curso.getDepartamento().getId());

    }

    @Transactional
    public void AlterarCurso (AlterarCursosDto dto, Integer curso_id, Integer utilizador_id){

        Curso curso = cursoRepository.findById(curso_id).orElseThrow(() -> new RuntimeException("Curso não encontrado"));

        Departamento departamento = departamentoRepository.findById(dto.departamento_id())
                .orElseThrow(() -> new RuntimeException("Departamento não encontrado"));

        curso.setNome(dto.nome());
        curso.setCodigo_uc(dto.codigo_uc());
        curso.setEstado(dto.estado());
        curso.setDepartamento(departamento);

        // LOG DE AUDITORIA: Alteração de Curso
        auditLogger.info("Utilizador ID: {} ALTEROU o Curso ID: {} para nome {} ; para codigo_uc {} ; para estado {} no Departamento ID: {}.",
                utilizador_id, curso_id, curso.getNome(), dto.codigo_uc(), dto.estado(), curso.getDepartamento().getId());

    }

    @Transactional
    public void DeleteCurso(Integer curso_id, Integer utilizador_id) {

        Curso curso = cursoRepository.findById(curso_id).orElseThrow(() -> new RuntimeException("Curso não encontrado"));

       cursoRepository.delete(curso);

        // LOG DE AUDITORIA: Delete
        auditLogger.info("Utilizador ID: {} APAGOU o Curso ID: {}.",
                utilizador_id, curso_id);

    }

}
