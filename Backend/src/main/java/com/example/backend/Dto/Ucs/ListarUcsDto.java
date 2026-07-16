package com.example.backend.Dto.Ucs;

import com.example.backend.Dto.Atribuicoes.ListarUcDto;
import com.example.backend.Dto.UcTipoAula.ListarUcTipoAulaDto;
import com.example.backend.Enums.EstadoUc;

import java.util.List;

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
        String departamento_nome,
        Integer curso_id,
        String curso_nome,
        List<ListarUcTipoAulaDto> ucTipoAula

) {
}
