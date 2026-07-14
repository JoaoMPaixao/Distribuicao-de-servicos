package com.example.backend.Controllers;

import com.example.backend.Dto.Departamentos.ListarDepartamentosDto;
import com.example.backend.Services.DepartamentoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/departamentos")
public class DepartamentoController {

    @Autowired
    private DepartamentoServices departamentoServices;

    @GetMapping
    public ResponseEntity<List<ListarDepartamentosDto>> getDepartamentos(){

        return ResponseEntity.ok(departamentoServices.getDepartamentos());

    }
    
}
