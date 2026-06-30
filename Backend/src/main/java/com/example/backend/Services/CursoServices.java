package com.example.backend.Services;

import com.example.backend.Dto.Atribuicoes.ListarCursoDistribuicoesDto;
import com.example.backend.Dto.Atribuicoes.ListarTipo_Uc_DistribuicoesDto;
import com.example.backend.Dto.Atribuicoes.ListarUcDistribuicoesDto;
import com.example.backend.Dto.Atribuicoes.ListarUcDto;
import com.example.backend.Enums.EstadoCurso;
import com.example.backend.Enums.EstadoUc;
import com.example.backend.Repository.*;
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

    public List<ListarCursoDistribuicoesDto> getUcByDepartamento(Integer distribuicao_id, Integer departamento_id){

        // Validar se a distribuição pertence de facto a esse departamento
        boolean isValida = distribuicaoRepository.existsByIdAndDepartamentoId(distribuicao_id, departamento_id);

        if (!isValida) {
            // Lançar uma exceção clara ou retornar uma lista vazia
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Distribuição não pertence ao departamento indicado.");
        }

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
}
