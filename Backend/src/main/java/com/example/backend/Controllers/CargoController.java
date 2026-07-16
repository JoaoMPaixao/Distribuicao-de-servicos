package com.example.backend.Controllers;

import com.example.backend.Dto.Cargos.ListarCargosDto;
import com.example.backend.Dto.Departamentos.AlterarDepartamentosDto;
import com.example.backend.Dto.Departamentos.CriarDepartamentoDto;
import com.example.backend.Dto.Departamentos.ListarDepartamentosDto;
import com.example.backend.Security.CustomUserDetails;
import com.example.backend.Services.CargoServices;
import com.example.backend.Services.DepartamentoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/cargos")
public class CargoController {

    @Autowired
    private CargoServices cargoServices;

    @GetMapping
    public ResponseEntity<List<ListarCargosDto>> getCargos(){

        return ResponseEntity.ok(cargoServices.getCargos());

    }


}

