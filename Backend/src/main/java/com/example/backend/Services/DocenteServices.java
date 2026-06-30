package com.example.backend.Services;

import com.example.backend.Dto.Atribuicoes.CargaHorariaDocenteDto;
import com.example.backend.Dto.Atribuicoes.DepartamentoDocenteDto;
import com.example.backend.Dto.Atribuicoes.ListarDocentesDistribuicoesDto;
import com.example.backend.Enums.EstadoDocente;
import com.example.backend.Repository.DistribuicaoRepository;
import com.example.backend.Repository.DocenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class DocenteServices {

    @Autowired
    private DocenteRepository docenteRepository;

    @Autowired
    private DistribuicaoRepository distribuicaoRepository;

    public List<ListarDocentesDistribuicoesDto> getDocenteByDepartamento(Integer distribuicao_id, Integer departamento_id){

        // Validar se a distribuição pertence de facto a esse departamento
        boolean isValida = distribuicaoRepository.existsByIdAndDepartamentoId(distribuicao_id, departamento_id);

        if (!isValida) {
            // Lançar uma exceção clara ou retornar uma lista vazia
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Distribuição não pertence ao departamento indicado.");
        }

        return docenteRepository.findByEstadoNot( EstadoDocente.INATIVO)
                .stream()
                .map(docente -> new ListarDocentesDistribuicoesDto(
                        docente.getId(),
                        docente.getNome(),
                        new CargaHorariaDocenteDto(
                                docente.getTipo_docente().getId(),
                                docente.getTipo_docente().getCarga_horaria_max()
                        ),
                        new DepartamentoDocenteDto(
                                docente.getDepartamento().getId(),
                                docente.getDepartamento().getNome()
                        )

                ))
                .toList();

    }

}
