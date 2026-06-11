package com.example.backend.Controllers;

import com.example.backend.Dto.CriarDistribuicaoDto;
import com.example.backend.Dto.ListarDistribuicaoDto;
import com.example.backend.Services.DistribuicaoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/departamento/{id}")
public class DistribuicaoController {


    @Autowired
    private DistribuicaoServices distribuicaoServices;

    @GetMapping("/distribuicoes")
    public ResponseEntity<List<ListarDistribuicaoDto>> getDistribuicao (@PathVariable Integer id) {

        try{

            List<ListarDistribuicaoDto> distribuicoes = distribuicaoServices.getDistribuicoesByDepartamentoId(id);

            return ResponseEntity.ok(distribuicoes);

        }catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }

    }

    @PostMapping("/distribuicoes")
    public ResponseEntity<Void> postDistribuicao(@RequestBody CriarDistribuicaoDto distribuicao) {

        try{
            distribuicaoServices.CriarDistribuicao(distribuicao);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }

    }
}
