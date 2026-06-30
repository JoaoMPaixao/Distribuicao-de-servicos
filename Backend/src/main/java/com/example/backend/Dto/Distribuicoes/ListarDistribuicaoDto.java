package com.example.backend.Dto.Distribuicoes;


import java.time.LocalDate;

public record ListarDistribuicaoDto(

        Integer id,
        String nome,
        String versao,
        String descricao,
        String ano_letivo,
        LocalDate data_criacao,
        String estado

) {
}
