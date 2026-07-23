package com.example.backend.Controllers;

import com.example.backend.Dto.Distribuicoes.AlterarEstadoDistribuicaoDto;
import com.example.backend.Dto.Distribuicoes.CriarDistribuicaoDto;
import com.example.backend.Dto.Distribuicoes.ListarDistribuicaoDto;
import com.example.backend.Dto.Docentes.ListarDocentesUcDistribuicaoDto;
import com.example.backend.Security.CustomUserDetails;
import com.example.backend.Services.DistribuicaoServices;
import com.example.backend.Services.DocenteServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/distribuicoes")
public class DistribuicaoController {


    @Autowired
    private DistribuicaoServices distribuicaoServices;

    @Autowired
    private DocenteServices docenteServices;

    @PreAuthorize("hasAnyRole('COORDENADOR', 'DIRETOR_COORDENADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<List<ListarDistribuicaoDto>> getDistribuicoesById(@PathVariable Integer id) {
        return ResponseEntity.ok(distribuicaoServices.getDistribuicoesById(id));
    }

    @PreAuthorize("hasAnyRole('COORDENADOR', 'DIRETOR_COORDENADOR')")
    @GetMapping
    public ResponseEntity<List<ListarDistribuicaoDto>> getDistribuicoesByDepartamento(Authentication authentication) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        Integer departamentoId = user.getDepartamentoId();

        return ResponseEntity.ok(distribuicaoServices.getDistribuicoesByDepartamentoId(departamentoId));
    }

    @PreAuthorize("hasAnyRole('COORDENADOR', 'DIRETOR_COORDENADOR')")
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

    @PreAuthorize("hasAnyRole('COORDENADOR', 'DIRETOR_COORDENADOR')")
    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> DesativarDistribuicao(@PathVariable Integer id, Authentication authentication) {

        try {
            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

            Integer departamentoId = user.getDepartamentoId();
            Integer utilizadorID = user.getId();
            distribuicaoServices.DesativarDistribuicao(id, departamentoId,  utilizadorID);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAnyRole('DIRETOR_COORDENADOR')")
    @PatchMapping("/{id}/alterar")
    public ResponseEntity<Void> AlterarEstadoDistribuicao(@PathVariable Integer id, @RequestBody AlterarEstadoDistribuicaoDto dto, Authentication authentication) {

        try {
            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
            Integer departamentoId = user.getDepartamentoId();
            Integer utilizadorID = user.getId();
            distribuicaoServices.AlterarEstadoDistribuicao(dto, id, departamentoId, utilizadorID);
            return  ResponseEntity.status(HttpStatus.CREATED).build();
        }catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }

    }

    @PreAuthorize("hasAnyRole('COORDENADOR', 'DIRETOR_COORDENADOR')")
    @PostMapping("/{id}/clonar")
    public ResponseEntity<Void> ClonarDistribuicao(@PathVariable Integer id, Authentication authentication) {


        try {
            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
            Integer departamentoId = user.getDepartamentoId();
            Integer utilizadorID = user.getId();
            distribuicaoServices.ClonarDistribuicao(id, departamentoId,  utilizadorID);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/docentes")
    public ResponseEntity<List<ListarDocentesUcDistribuicaoDto>> getDocentesDistribuicao(){

        return ResponseEntity.ok(docenteServices.getDocentesUcDistribuicao());
    }

}
