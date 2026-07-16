package com.example.backend.Controllers;

import com.example.backend.Dto.Departamentos.AlterarDepartamentosDto;
import com.example.backend.Dto.Departamentos.CriarDepartamentoDto;
import com.example.backend.Dto.Departamentos.ListarDepartamentosDto;
import com.example.backend.Security.CustomUserDetails;
import com.example.backend.Services.DepartamentoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<Void> criarDepartamento(Authentication authentication, @RequestBody CriarDepartamentoDto dto) {
        try{
            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
            Integer utilizadorID = user.getId();

            departamentoServices.CriarDepartamento(dto, utilizadorID);

            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> alterarDepartamento(@PathVariable Integer id, Authentication authentication, @RequestBody AlterarDepartamentosDto dto) {

        try{
            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
            Integer utilizadorID = user.getId();

            departamentoServices.AlterarCurso(dto, id, utilizadorID);
            return ResponseEntity.status(HttpStatus.CREATED).build();

        }catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }

    }
    
}
