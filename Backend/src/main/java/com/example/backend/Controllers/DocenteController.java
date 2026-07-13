package com.example.backend.Controllers;

import com.example.backend.Dto.Atribuicoes.ListarDocentesDistribuicoesDto;
import com.example.backend.Security.CustomUserDetails;
import com.example.backend.Services.DocenteServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class DocenteController {

    @Autowired
    private DocenteServices docenteServices;


}
