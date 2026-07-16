package com.example.backend.Controllers;

import com.example.backend.Dto.TipoAula.ListarTipoAulaDto;
import com.example.backend.Dto.UcTipoAula.ListarUcTipoAulaDto;
import com.example.backend.Services.TipoAulaServices;
import com.example.backend.Services.UcTipoAulaServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/tipoaulas")
public class TipoAulaController {

    @Autowired
    private TipoAulaServices tipoAulaServices;

    @GetMapping
    public ResponseEntity<List<ListarTipoAulaDto>> getTipoAulas() {

        return  ResponseEntity.ok(tipoAulaServices.getTipoAulas());

    }

}
