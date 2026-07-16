package com.example.backend.Dto.Utilizadores;

import com.example.backend.Enums.EstadoUtilizador;

public record ListarUtilizadoresDto(

        Integer id,
        String nome,
        String email,
        String password,
        EstadoUtilizador estado,
        Integer cargo_id,
        String cargo_tipo,
        Integer departamento_id,
        String departamento_nome


) {
}
