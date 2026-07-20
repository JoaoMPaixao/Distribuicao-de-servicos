package com.example.backend.Controllers;

import com.example.backend.Dto.Utilizadores.AlterarUtilizadoresDto;
import com.example.backend.Dto.Utilizadores.CriarUtilizadoresDto;
import com.example.backend.Dto.Utilizadores.ListarUtilizadoresDto;
import com.example.backend.Security.CustomUserDetails;
import com.example.backend.Services.UtilizadorServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/utilizadores")
public class UtilizadorController {

    @Autowired
    private UtilizadorServices utilizadorServices;

    @GetMapping
    public ResponseEntity<List<ListarUtilizadoresDto>> getUtilizadores(){

        return ResponseEntity.ok(utilizadorServices.getUtilizadores());

    }

    @PostMapping
    public ResponseEntity<Void> criarUtilizadores(Authentication authentication, @RequestBody CriarUtilizadoresDto dto) {
        try{
            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
            Integer utilizadorID = user.getId();

            utilizadorServices.CriarUtilizador(dto, utilizadorID);

            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> alterarUtilizador(@PathVariable Integer id, Authentication authentication, @RequestBody AlterarUtilizadoresDto dto) {

        try{
            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
            Integer utilizadorID = user.getId();

           utilizadorServices.AlterarUtilizador(dto, id, utilizadorID);
            return ResponseEntity.status(HttpStatus.CREATED).build();

        }catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }

    }

    @DeleteMapping("/{id}/apagar")
    public ResponseEntity<Void> ApagarUtilizador(@PathVariable Integer id, Authentication authentication) {

        try {
            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
            Integer utilizadorID = user.getId();
            utilizadorServices.DeleteUtilizador(id, utilizadorID);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
