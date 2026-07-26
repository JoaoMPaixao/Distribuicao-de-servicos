package com.example.backend.Controllers;

import com.example.backend.Dto.Atribuicoes.*;
import com.example.backend.Security.CustomUserDetails;
import com.example.backend.Services.CursoServices;
import com.example.backend.Services.UcDistribuicaoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/distribuicoes/{id}")
public class UcDistribuicaoController {

        @Autowired
        private CursoServices cursoServices;

        @Autowired
        private UcDistribuicaoServices ucDistribuicaoServices;


        @PreAuthorize("hasAnyRole('COORDENADOR', 'DIRETOR_COORDENADOR')")
        @GetMapping("/atribuicoes")
        public ResponseEntity<List<ListarUcDistribuicoesDto>> getUcsByDistribuicao(@PathVariable Integer id) {

            return ResponseEntity.ok(ucDistribuicaoServices.getUcDistribuicoes(id));
        }

        @PreAuthorize("hasAnyRole('COORDENADOR', 'DIRETOR_COORDENADOR')")
        @PostMapping("/atribuicao/{udid}")
        public ResponseEntity<Void> CriarUcDistribuicaoTurno(@PathVariable Integer id, @PathVariable Integer udid, Authentication authentication) {

            try{
                CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
                Integer departamentoId = user.getDepartamentoId();
                Integer utilizadorID = user.getId();
                ucDistribuicaoServices.CriarUcDistribuicao(udid, id, departamentoId, utilizadorID);
                return ResponseEntity.status(HttpStatus.CREATED).build();
            }catch(NumberFormatException e) {
                return ResponseEntity.badRequest().build();
            }
        }

        @PreAuthorize("hasAnyRole('COORDENADOR', 'DIRETOR_COORDENADOR')")
        @PostMapping("/juncao")
        public ResponseEntity<Void> CriarUcJuncao(@PathVariable Integer id, @RequestBody CriarUcJuncaoDto dto, Authentication authentication) {

            try{
                CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
                Integer departamentoId = user.getDepartamentoId();
                Integer utilizadorID = user.getId();
                ucDistribuicaoServices.CriarUcJuncao(dto, id, departamentoId, utilizadorID);
                return ResponseEntity.status(HttpStatus.CREATED).build();
            }catch(NumberFormatException e) {
                return ResponseEntity.badRequest().build();
            }


        }

        @PreAuthorize("hasAnyRole('COORDENADOR', 'DIRETOR_COORDENADOR')")
        @DeleteMapping("/delete/{UcDistribuicao_id}")
        public ResponseEntity<Void> deleteUcDistribuicao(@PathVariable Integer id, @PathVariable Integer UcDistribuicao_id, Authentication authentication) {

            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
            Integer departamentoId = user.getDepartamentoId();
            Integer utilizadorID = user.getId();
            ucDistribuicaoServices.DeleteUcDistribuicao(id, UcDistribuicao_id, departamentoId, utilizadorID);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        }

    @PreAuthorize("hasAnyRole('COORDENADOR', 'DIRETOR_COORDENADOR')")
    @PatchMapping("/horas/{UcDistribuicao_id}")
    public ResponseEntity<Void> alterarHoras(@PathVariable Integer id, @PathVariable Integer UcDistribuicao_id, @RequestBody AlterarHorasUcDistribuicaoDto dto, Authentication authentication) {

        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        Integer departamentoId = user.getDepartamentoId();
        Integer utilizadorID = user.getId();
        ucDistribuicaoServices.AlterarHoras(dto, id, UcDistribuicao_id, departamentoId, utilizadorID );
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasAnyRole('COORDENADOR', 'DIRETOR_COORDENADOR')")
    @PatchMapping("/docente/{UcDistribuicao_id}/atribuir")
    public ResponseEntity<Void> atribuirDocente(@PathVariable Integer id, @PathVariable Integer UcDistribuicao_id, @RequestBody AtribuirDocenteDto dto, Authentication authentication) {

        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        Integer departamentoId = user.getDepartamentoId();
        Integer utilizadorID = user.getId();
        ucDistribuicaoServices.atribuirDocente(dto, id, UcDistribuicao_id, departamentoId, utilizadorID );
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasAnyRole('COORDENADOR', 'DIRETOR_COORDENADOR')")
    @PatchMapping("/docente/{UcDistribuicao_id}/retirar")
    public ResponseEntity<Void> retirarDocente(@PathVariable Integer id, @PathVariable Integer UcDistribuicao_id,Authentication authentication) {

        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        Integer departamentoId = user.getDepartamentoId();
        Integer utilizadorID = user.getId();
        ucDistribuicaoServices.retirarDocente(id, UcDistribuicao_id, departamentoId, utilizadorID );
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }






}
