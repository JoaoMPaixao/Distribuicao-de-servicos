package com.example.backend.Services;


import com.example.backend.Dto.Departamentos.AlterarDepartamentosDto;
import com.example.backend.Dto.Departamentos.CriarDepartamentoDto;
import com.example.backend.Dto.Departamentos.ListarDepartamentosDto;
import com.example.backend.Entity.Departamento;
import com.example.backend.Repository.DepartamentoRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartamentoServices {

    // Logger a apontar para o ficheiro estabelecido no xml
    private static final Logger auditLogger = LoggerFactory.getLogger("auditoria");

    @Autowired
    private DepartamentoRepository departamentoRepository;

    public List<ListarDepartamentosDto> getDepartamentos (){

        return departamentoRepository.findAll()
                .stream()
                .map(departamento -> new ListarDepartamentosDto(
                        departamento.getId(),
                        departamento.getNome(),
                        departamento.getCoordenador()

                ))
                .toList();

    }

    @Transactional
    public void CriarDepartamento(CriarDepartamentoDto dto, Integer utilizador_id){

        Departamento departamento = new Departamento();
        departamento.setNome(dto.nome());
        departamento.setCoordenador(dto.coordenador());


        departamentoRepository.save(departamento);

        // LOG DE AUDITORIA: Criação
        auditLogger.info("Utilizador ID: {} CRIOU o Departamento ID: {} ('{}').",
                utilizador_id, departamento.getId(), departamento.getNome());

    }

    @Transactional
    public void AlterarCurso (AlterarDepartamentosDto dto, Integer departamento_id, Integer utilizador_id){

        Departamento departamento = departamentoRepository.findById(departamento_id).orElseThrow(() -> new RuntimeException("Departamento não encontrado"));

       departamento.setNome(dto.nome());
       departamento.setCoordenador(dto.coordenador());

        // LOG DE AUDITORIA: Alteração de Curso
        auditLogger.info("Utilizador ID: {} ALTEROU o Departamento  ID: {} para nome {} ; para coordenador {}.",
                utilizador_id, departamento_id, departamento.getNome(), dto.coordenador());

    }

}
