package com.example.backend.Dto.Docentes;

import com.example.backend.Enums.EstadoDocente;

public record AlterarDocenteDto(

        String nome,
        String email,
        EstadoDocente estado,
        Integer departamentoId

) {
}
