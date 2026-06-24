package com.example.backend.Services;

import com.example.backend.Dto.ListarDistribuicaoDto;
import com.example.backend.Dto.ListarDocentesDto;
import com.example.backend.Dto.TipoDocenteDto;
import com.example.backend.Entity.Docente;
import com.example.backend.Enums.EstadoDistribuicao;
import com.example.backend.Enums.EstadoDocente;
import com.example.backend.Repository.DistribuicaoRepository;
import com.example.backend.Repository.DocenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Service
public class DocenteServices {

    @Autowired
    private DocenteRepository docenteRepository;

    @Autowired
    private DistribuicaoRepository distribuicaoRepository;

    public List<ListarDocentesDto> getDocenteByDistribuicao(Integer distribuicao_id, Integer departamento_id){

        // Validar se a distribuição pertence de facto a esse departamento
        boolean isValida = distribuicaoRepository.existsByIdAndDepartamentoId(distribuicao_id, departamento_id);

        if (!isValida) {
            // Lançar uma exceção clara ou retornar uma lista vazia
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Distribuição não pertence ao departamento indicado.");
        }

        return docenteRepository.findByDepartamento_IdAndEstadoNot(departamento_id, EstadoDocente.INATIVO)
                .stream()
                .map(docente -> new ListarDocentesDto(
                        docente.getId(),
                        docente.getNome(),
                        new TipoDocenteDto(
                                docente.getTipo_docente().getId(),
                                docente.getTipo_docente().getTipo()
                        )
                ))
                .toList();

    }

}
