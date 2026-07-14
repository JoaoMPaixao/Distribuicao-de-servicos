package com.example.backend.Services;

import com.example.backend.Dto.Cursos.ListarCursosDto;
import com.example.backend.Dto.Departamentos.ListarDepartamentosDto;
import com.example.backend.Dto.Distribuicoes.ListarDistribuicaoDto;
import com.example.backend.Enums.EstadoDistribuicao;
import com.example.backend.Repository.CursoRepository;
import com.example.backend.Repository.DepartamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartamentoServices {

    @Autowired
    private DepartamentoRepository departamentoRepository;

    public List<ListarDepartamentosDto> getDepartamentos (){

        return departamentoRepository.findAll()
                .stream()
                .map(departamento -> new ListarDepartamentosDto(
                        departamento.getId(),
                        departamento.getNome()

                ))
                .toList();

    }

}
