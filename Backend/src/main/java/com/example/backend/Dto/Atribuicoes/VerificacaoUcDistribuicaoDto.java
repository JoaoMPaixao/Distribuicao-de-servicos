package com.example.backend.Dto.Atribuicoes;

import java.util.List;

public record VerificacaoUcDistribuicaoDto(

        boolean valida,
        List<String> problemas,
        Integer ucsSemDocente,
        Integer ucsSemHoras


) {
}
