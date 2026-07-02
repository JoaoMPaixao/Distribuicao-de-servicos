package com.example.backend.Controllers;

import com.example.backend.Dto.Atribuicoes.CriarUcDistribuicoesDto;
import com.example.backend.Dto.Atribuicoes.CriarUcJuncaoDto;
import com.example.backend.Dto.Atribuicoes.ListarCursoDistribuicoesDto;
import com.example.backend.Security.CustomUserDetails;
import com.example.backend.Services.CursoServices;
import com.example.backend.Services.UcDistribuicaoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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



        @GetMapping()
        public ResponseEntity<List<ListarCursoDistribuicoesDto>> getUcsByDistribuicao(@PathVariable Integer id, Authentication authentication) {
            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
            Integer departamentoId = user.getDepartamentoId();

            return ResponseEntity.ok(cursoServices.getUcByDepartamento(id, departamentoId));
        }

        @PostMapping()
        public ResponseEntity<Void> CriarUcDistribuicao(@PathVariable Integer id, @RequestBody CriarUcDistribuicoesDto dto, Authentication authentication) {

            try{
                CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
                Integer departamentoId = user.getDepartamentoId();
                ucDistribuicaoServices.CriarUcDistribuicao(dto, id, departamentoId);
                return ResponseEntity.status(HttpStatus.CREATED).build();
            }catch(NumberFormatException e) {
                return ResponseEntity.badRequest().build();
            }
        }

        @PostMapping("/juncao")
        public ResponseEntity<Void> CriarUcJuncao(@PathVariable Integer id, @RequestBody CriarUcJuncaoDto dto, Authentication authentication) {

            try{
                CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
                Integer departamentoId = user.getDepartamentoId();
                ucDistribuicaoServices.CriarUcJuncao(dto, id, departamentoId);
                return ResponseEntity.status(HttpStatus.CREATED).build();
            }catch(NumberFormatException e) {
                return ResponseEntity.badRequest().build();
            }


        }

        @DeleteMapping("/delete/{UcDistribuicao_id}")
        public ResponseEntity<Void> deleteUcDistribuicao(@PathVariable Integer id, @PathVariable Integer UcDistribuicao_id, Authentication authentication) {

            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
            Integer departamentoId = user.getDepartamentoId();
            ucDistribuicaoServices.DeleteUcDistribuicao(id, UcDistribuicao_id, departamentoId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        }




}
