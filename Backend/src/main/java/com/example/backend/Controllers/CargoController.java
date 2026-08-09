package com.example.backend.Controllers;

import com.example.backend.Dto.Cargos.ListarCargosDto;
import com.example.backend.Services.CargoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/cargos")
public class CargoController {

    @Autowired
    private CargoServices cargoServices;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<ListarCargosDto>> getCargos(){

        return ResponseEntity.ok(cargoServices.getCargos());

    }


}

