package com.example.backend.Dto.Cursos;

import com.example.backend.Enums.EstadoCurso;

public record AlterarCursosDto (

        String nome,
        EstadoCurso estado,
        String codigo_uc,
        Integer departamento_id


) {
}
