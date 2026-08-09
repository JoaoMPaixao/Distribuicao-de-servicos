package com.example.backend.Controllers;

import com.example.backend.Dto.Docentes.*;
import com.example.backend.Security.CustomUserDetails;
import com.example.backend.Services.DocenteServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/docentes")
public class DocenteController {

    @Autowired
    private DocenteServices docenteServices;


    @GetMapping
    public ResponseEntity<List<ListarDocentesDto>> getDocentes() {

        return ResponseEntity.ok(docenteServices.getDocentes());
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Void> criarDocente(Authentication authentication, @RequestBody CriarDocenteDto dto) {
        try{
            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
            Integer utilizadorID = user.getId();

            docenteServices.CriarDocente(dto, utilizadorID);

            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }

    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Void> alterarDocente(@PathVariable Integer id, Authentication authentication, @RequestBody AlterarDocenteDto dto) {

        try{
            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
            Integer utilizadorID = user.getId();

            docenteServices.AlterarDocente(dto, id, utilizadorID);
            return ResponseEntity.status(HttpStatus.CREATED).build();

        }catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }

    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}/apagar")
    public ResponseEntity<Void> ApagarDocente(@PathVariable Integer id, Authentication authentication) {

        try {
            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
            Integer utilizadorID = user.getId();
           docenteServices.DeleteDocente(id, utilizadorID);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/contrato")
    public ResponseEntity<Void> criarContratoDocente(Authentication authentication, @RequestBody CriarContratoDocenteDto dto) {
        try{
            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
            Integer utilizadorID = user.getId();

            docenteServices.CriarContrato(dto, utilizadorID);

            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }

    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/contrato/{id}")
    public ResponseEntity<Void> alterarContratoDocente(@PathVariable Integer id, Authentication authentication, @RequestBody CriarContratoDocenteDto dto) {

        try{
            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
            Integer utilizadorID = user.getId();

           docenteServices.AlterarContratoDocente(dto, id, utilizadorID);
            return ResponseEntity.status(HttpStatus.CREATED).build();

        }catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }

    }

}
