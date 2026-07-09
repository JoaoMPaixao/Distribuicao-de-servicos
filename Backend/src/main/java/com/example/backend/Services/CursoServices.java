package com.example.backend.Services;

import com.example.backend.Dto.Atribuicoes.ListarCursoDistribuicoesDto;
import com.example.backend.Dto.Atribuicoes.ListarTipo_Uc_DistribuicoesDto;
import com.example.backend.Dto.Atribuicoes.ListarUcDistribuicoesDto;
import com.example.backend.Dto.Atribuicoes.ListarUcDto;
import com.example.backend.Dto.Cursos.ListarCursosDto;
import com.example.backend.Dto.Distribuicoes.ListarDistribuicaoDto;
import com.example.backend.Enums.EstadoCurso;
import com.example.backend.Enums.EstadoDistribuicao;
import com.example.backend.Enums.EstadoUc;
import com.example.backend.Repository.*;
import com.example.backend.Utils.UtilsServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CursoServices {

    @Autowired
    private UcRepository ucRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private DistribuicaoRepository distribuicaoRepository;

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
                        curso.getDepartamento().getId()
                ))
                .toList();

    }
}
