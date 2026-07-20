package com.example.backend.Controllers;

import com.example.backend.Dto.Departamentos.ListarDepartamentosDto;
import com.example.backend.Dto.Docentes.ListarDocentesUcDistribuicaoDto;
import com.example.backend.Services.DocenteServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/docentes")
public class DocenteController {

    @Autowired
    private DocenteServices docenteServices;

    @GetMapping("/distribuicoes")
    public ResponseEntity<List<ListarDocentesUcDistribuicaoDto>> getDocentesDistribuicao(){

        return ResponseEntity.ok(docenteServices.getDocentesUcDistribuicao());
    }
}
