package com.example.backend.Dto.Ucs;

import com.example.backend.Enums.EstadoUc;

public record CriarUcsDto (

        String nome,
        String codigo_uc,
        String semestre,
        String ano_curricular,
        Float ects,
        Float horas,
        Integer departamento_id,
        Integer curso_id



) {
}
