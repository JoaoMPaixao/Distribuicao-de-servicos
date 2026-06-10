package com.example.backend.Dto;

import java.sql.Date;

public record ListarDistribuicaoDto(

        Integer id,
        String nome,
        String versao,
        String descricao,
        String ano_letivo,
        Date data_criacao,
        String estado

) {
}
