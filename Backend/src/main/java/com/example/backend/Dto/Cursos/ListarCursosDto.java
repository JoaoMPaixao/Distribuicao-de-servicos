package com.example.backend.Dto.Cursos;

import com.example.backend.Enums.EstadoCurso;

public record ListarCursosDto(

        Integer id,
        String nome,
        EstadoCurso estado,
        String codigo_uc,
        String departamento_nome


) {
}
