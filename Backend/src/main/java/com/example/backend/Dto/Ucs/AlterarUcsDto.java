package com.example.backend.Dto.Ucs;

import com.example.backend.Enums.EstadoUc;

public record AlterarUcsDto (

        String nome,
        String codigo_uc,
        Integer semestre,
        Integer ano_curricular,
        Float ects,
        float horas,
        EstadoUc estado,
        Integer departamento_id,
        Integer curso_id


) {
}
