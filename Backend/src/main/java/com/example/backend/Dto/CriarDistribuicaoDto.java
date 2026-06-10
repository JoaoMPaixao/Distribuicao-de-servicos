package com.example.backend.Dto;

public record CriarDistribuicaoDto(

        String nome,
        String versao,
        String descricao,
        String ano_letivo,
        Integer depertamento_id,
        Integer utilizador_id


) {
}
