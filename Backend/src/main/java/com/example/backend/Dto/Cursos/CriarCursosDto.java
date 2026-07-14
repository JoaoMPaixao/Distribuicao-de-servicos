package com.example.backend.Dto.Cursos;

import com.example.backend.Enums.EstadoCurso;

public record CriarCursosDto (

        String nome,
        String codigo_uc,
        Integer departamento_id,
        EstadoCurso estado

){
}
