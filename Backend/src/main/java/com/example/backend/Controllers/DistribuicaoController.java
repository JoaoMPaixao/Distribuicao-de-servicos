package com.example.backend.Controllers;

import com.example.backend.Dto.CriarDistribuicaoDto;
import com.example.backend.Dto.ListarDistribuicaoDto;
import com.example.backend.Security.CustomUserDetails;
import com.example.backend.Services.DistribuicaoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/distribuicoes")
public class DistribuicaoController {


    @Autowired
    private DistribuicaoServices distribuicaoServices;

    @GetMapping
    public ResponseEntity<List<ListarDistribuicaoDto>> getDistribuicoes(Authentication authentication) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        Integer departamentoId = user.getDepartamentoId();

        return ResponseEntity.ok(distribuicaoServices.getDistribuicoesByDepartamentoId(departamentoId));
    }


    @PostMapping
    public ResponseEntity<Void> postDistribuicao(@RequestBody CriarDistribuicaoDto distribuicao, Authentication authentication) {

        try{

            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
            Integer departamentoId = user.getDepartamentoId();
            Integer utilizadorID = user.getId();
            distribuicaoServices.CriarDistribuicao(distribuicao, departamentoId, utilizadorID);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }

    }

    @PutMapping("/{id}/desativar")
    public ResponseEntity<Void> DesativarDistribuicao(@PathVariable Integer id, Authentication authentication) {

        try {
            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

            Integer utilizadorID = user.getId();
            distribuicaoServices.DesativarDistribuicao(id, utilizadorID);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
