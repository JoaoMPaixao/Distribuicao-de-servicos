package com.example.backend.Controllers;

import com.example.backend.Dto.Cursos.AlterarCursosDto;
import com.example.backend.Dto.Cursos.CriarCursosDto;
import com.example.backend.Dto.Cursos.ListarCursosDto;
import com.example.backend.Security.CustomUserDetails;
import com.example.backend.Services.CursoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/cursos")
public class CursoController {


    @Autowired
    private CursoServices cursoServices;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<ListarCursosDto>> listarCursos() {

        return ResponseEntity.ok(cursoServices.getCursos());

    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Void> criarCurso(Authentication authentication, @RequestBody CriarCursosDto dto) {
        try{
            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
            Integer utilizadorID = user.getId();

            cursoServices.CriarCurso(dto, utilizadorID);

            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }

    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Void> alterarCurso(@PathVariable Integer id, Authentication authentication, @RequestBody AlterarCursosDto dto) {

        try{
            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
            Integer utilizadorID = user.getId();

            cursoServices.AlterarCurso(dto, id,  utilizadorID);
            return ResponseEntity.status(HttpStatus.CREATED).build();

        }catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }

    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}/apagar")
    public ResponseEntity<Void> ApagarCurso(@PathVariable Integer id, Authentication authentication) {

        try {
            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
            Integer utilizadorID = user.getId();
            cursoServices.DeleteCurso(id, utilizadorID);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }


}
