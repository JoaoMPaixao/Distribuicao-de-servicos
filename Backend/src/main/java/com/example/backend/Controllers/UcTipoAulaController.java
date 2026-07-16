package com.example.backend.Controllers;

import com.example.backend.Dto.UcTipoAula.AlterarUcTipoAulaDto;
import com.example.backend.Dto.UcTipoAula.CriarUcTipoAulaDto;
import com.example.backend.Dto.UcTipoAula.ListarUcTipoAulaDto;
import com.example.backend.Security.CustomUserDetails;
import com.example.backend.Services.UcTipoAulaServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/uctipoaulas")
public class UcTipoAulaController {

    @Autowired
    private UcTipoAulaServices ucTipoAulaServices;

    @GetMapping
    public ResponseEntity<List<ListarUcTipoAulaDto>> getUcTipoAulasByUc(@RequestParam int uc_id) {

        return ResponseEntity.ok(ucTipoAulaServices.getUcTipoAulasByUc(uc_id));

    }

    @PostMapping
    public ResponseEntity<Void> criarUcTipoAulas(Authentication authentication, @RequestBody CriarUcTipoAulaDto dto) {
        try{
            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
            Integer utilizadorID = user.getId();

            ucTipoAulaServices.CriarUcTipoAulas(dto, utilizadorID);

            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> alterarUcTipoAulas(@PathVariable Integer id, Authentication authentication, @RequestBody AlterarUcTipoAulaDto dto) {

        try{
            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
            Integer utilizadorID = user.getId();

            ucTipoAulaServices.AlterarUcTipoAulas(dto, id, utilizadorID);
            return ResponseEntity.status(HttpStatus.CREATED).build();

        }catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }

    }

}

