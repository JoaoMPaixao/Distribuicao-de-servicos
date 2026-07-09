package com.example.backend.Dto.Ucs;

import com.example.backend.Enums.EstadoUc;

public record ListarUcsDto (

        Integer id,
        String nome,
        String codigo_uc,
        String semestre,
        String ano_curricular,
        Float ects,
        Float horas,
        EstadoUc estado,
        Integer departamento_id,
        Integer curso_id

) {
}
