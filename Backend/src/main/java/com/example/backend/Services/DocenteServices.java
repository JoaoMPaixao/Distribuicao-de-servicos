package com.example.backend.Services;

import com.example.backend.Dto.Atribuicoes.CargaHorariaDocenteDto;
import com.example.backend.Dto.Atribuicoes.DepartamentoDocenteDto;
import com.example.backend.Dto.Atribuicoes.ListarDocentesDistribuicoesDto;
import com.example.backend.Enums.EstadoDocente;
import com.example.backend.Repository.DistribuicaoRepository;
import com.example.backend.Repository.DocenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class DocenteServices {

    @Autowired
    private DocenteRepository docenteRepository;

    @Autowired
    private DistribuicaoRepository distribuicaoRepository;



}
