package com.example.backend.Dto.Atribuicoes;

import java.util.List;

public record ListarCursoDistribuicoesDto(

        Integer id,
        String nome,
        List<ListarUcDto> uc

) {
}
