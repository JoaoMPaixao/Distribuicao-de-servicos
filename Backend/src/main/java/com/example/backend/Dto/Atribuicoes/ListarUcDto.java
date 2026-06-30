package com.example.backend.Dto.Atribuicoes;

import java.util.List;

public record ListarUcDto(

        Integer id,
        String nome,
        String semestre,
        String ano_curricular,
        Float horas,
        List<ListarTipo_Uc_DistribuicoesDto> tipo_aula

) {
}
