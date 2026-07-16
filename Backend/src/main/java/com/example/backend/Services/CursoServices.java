package com.example.backend.Services;

import com.example.backend.Dto.Atribuicoes.ListarCursoDistribuicoesDto;
import com.example.backend.Dto.Atribuicoes.ListarTipo_Uc_DistribuicoesDto;
import com.example.backend.Dto.Atribuicoes.ListarUcDistribuicoesDto;
import com.example.backend.Dto.Atribuicoes.ListarUcDto;
import com.example.backend.Dto.Cursos.AlterarCursosDto;
import com.example.backend.Dto.Cursos.CriarCursosDto;
import com.example.backend.Dto.Cursos.ListarCursosDto;
import com.example.backend.Entity.Curso;
import com.example.backend.Entity.Departamento;
import com.example.backend.Enums.EstadoCurso;
import com.example.backend.Enums.EstadoUc;
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

    public List<ListarCursoDistribuicoesDto> getUcByDepartamento(Integer distribuicao_id, Integer departamento_id){

        //verificar se a distribuicao e utilizador têm o mesmo departamento
       utilsServices.validarAcessoDistribuicao(distribuicao_id, departamento_id);

        //adicionar logica para que quando o uc_tipo_aula_id na UcDistribuicao seja null, é porque temos uma juncao e entao temos de ir buscar à tabela ucJuncao
        return cursoRepository.findByDepartamento_IdAndEstadoNot(departamento_id, EstadoCurso.INATIVO)
                .stream()
                .map(curso -> new ListarCursoDistribuicoesDto(
                        curso.getId(),
                        curso.getNome(),
                        ucRepository.findByCurso_IdAndEstadoNot(curso.getId(), EstadoUc.INATIVO)
                                .stream()
                                .map(uc -> new ListarUcDto(
                                        uc.getId(),
                                        uc.getNome(),
                                        uc.getSemestre(),
                                        uc.getAno_curricular(),
                                        uc.getHoras(),
                                        ucTipoAulaRepository.findByUc_IdAndUc_EstadoNot(uc.getId(), EstadoUc.INATIVO)
                                                .stream()
                                                .map(uc_tipo_aula -> new ListarTipo_Uc_DistribuicoesDto(

                                                       uc_tipo_aula.getId(),
                                                        uc_tipo_aula.getHoras(),
                                                        uc_tipo_aula.getTipo_aula().getId(),
                                                        uc_tipo_aula.getTipo_aula().getNome(),
                                                        ucDistribuicaoRepository.findByUcTipoAula_Id(uc_tipo_aula.getId())
                                                                .stream()
                                                                .map(ucDistribuicao -> new ListarUcDistribuicoesDto(

                                                                        ucDistribuicao.getId(),
                                                                        ucDistribuicao.getTurno(),
                                                                        ucDistribuicao.getHoras_atribuidas(),
                                                                        ucDistribuicao.getDocente().getId(),
                                                                        ucDistribuicao.getDocente().getNome()

                                                                ))
                                                                .toList()

                                                ))
                                                .toList()

                                ))
                                .toList()

                ))
                .toList();

    }


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
}
