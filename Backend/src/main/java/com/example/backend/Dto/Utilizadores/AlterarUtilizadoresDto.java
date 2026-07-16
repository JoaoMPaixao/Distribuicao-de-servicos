package com.example.backend.Dto.Utilizadores;

import com.example.backend.Enums.EstadoUtilizador;

public record AlterarUtilizadoresDto(

        String nome,
        String email,
        String password,
        EstadoUtilizador estado,
        Integer cargo_id,
        Integer departamento_id

) {
}
