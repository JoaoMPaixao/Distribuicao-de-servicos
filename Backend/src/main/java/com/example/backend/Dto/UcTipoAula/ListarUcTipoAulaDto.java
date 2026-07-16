package com.example.backend.Dto.UcTipoAula;

public record ListarUcTipoAulaDto (

        Integer id,
        Float horas,
        Integer uc_id,
        String uc_nome,
        Integer tipo_aula_id,
        String tipo_aula_nome


){
}
