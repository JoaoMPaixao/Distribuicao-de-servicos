package com.example.backend.Dto.Atribuicoes;

import java.util.List;

public record ListarTipo_Uc_DistribuicoesDto (

        Integer id,
        Float horas,
        Integer tipo_aula_id,
        String tipo_aula_nome,
        List<ListarUcDistribuicoesDto>ucDistribuicao

) {
}
