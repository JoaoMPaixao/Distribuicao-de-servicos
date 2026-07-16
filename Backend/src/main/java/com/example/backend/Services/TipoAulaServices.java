package com.example.backend.Services;

import com.example.backend.Dto.TipoAula.ListarTipoAulaDto;
import com.example.backend.Dto.UcTipoAula.ListarUcTipoAulaDto;
import com.example.backend.Repository.TipoAulaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoAulaServices {

    @Autowired
    private TipoAulaRepository tipoAulaRepository;

    public List<ListarTipoAulaDto> getTipoAulas (){

        return tipoAulaRepository.findAll()
                .stream()
                .map(tipoAula -> new ListarTipoAulaDto(
                        tipoAula.getId(),
                        tipoAula.getNome(),
                        tipoAula.getDescricao()
                ))
                .toList();
    }
}
