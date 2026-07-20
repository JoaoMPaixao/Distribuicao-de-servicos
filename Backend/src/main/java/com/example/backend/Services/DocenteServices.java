package com.example.backend.Services;

import com.example.backend.Dto.Departamentos.ListarDepartamentosDto;
import com.example.backend.Dto.Docentes.ListarDocentesUcDistribuicaoDto;
import com.example.backend.Repository.DistribuicaoRepository;
import com.example.backend.Repository.DocenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocenteServices {

    @Autowired
    private DocenteRepository docenteRepository;

    @Autowired
    private DistribuicaoRepository distribuicaoRepository;

    public List<ListarDocentesUcDistribuicaoDto> getDocentesUcDistribuicao (){

        return docenteRepository.findAll()
                .stream()
                .map(docente -> new ListarDocentesUcDistribuicaoDto(
                        docente.getId(),
                        docente.getNome(),
                        docente.getDepartamento().getNome()

                ))
                .toList();
    }

}
