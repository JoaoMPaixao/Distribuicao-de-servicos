package com.example.backend.Dto.Distribuicoes;

import java.util.List;

public record ComparacaoDistribuicoesDto(

        String nomeDistribuicao1,
        String nomeDistribuicao2,
        List<ComparacaoLinhaDto> linhas,
        int totalDiferencas
) {
}
