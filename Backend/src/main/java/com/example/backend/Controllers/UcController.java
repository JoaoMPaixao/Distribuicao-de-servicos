package com.example.backend.Controllers;

import com.example.backend.Dto.Ucs.AlterarUcsDto;
import com.example.backend.Dto.Ucs.CriarUcsDto;
import com.example.backend.Dto.Ucs.ListarUcsDto;
import com.example.backend.Security.CustomUserDetails;
import com.example.backend.Services.UcServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/ucs")
public class UcController {

    @Autowired
    private UcServices ucServices;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<ListarUcsDto>> listarUcs() {

        return ResponseEntity.ok(ucServices.getUcs());

    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Void> criarUc(Authentication authentication, @RequestBody CriarUcsDto dto) {
        try{
            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
            Integer utilizadorID = user.getId();

            ucServices.CriarUcs(dto, utilizadorID);

            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }

    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Void> alterarUc(@PathVariable Integer id, Authentication authentication, @RequestBody AlterarUcsDto dto) {

        try{
            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
            Integer utilizadorID = user.getId();

            ucServices.AlterarUcs(dto, id,  utilizadorID);
            return ResponseEntity.status(HttpStatus.CREATED).build();

        }catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }

    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}/apagar")
    public ResponseEntity<Void> ApagarUc(@PathVariable Integer id, Authentication authentication) {

        try {
            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
            Integer utilizadorID = user.getId();
            ucServices.DeleteUc(id,  utilizadorID);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
