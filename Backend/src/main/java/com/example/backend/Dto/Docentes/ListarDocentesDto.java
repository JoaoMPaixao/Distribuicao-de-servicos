package com.example.backend.Dto.Docentes;

import com.example.backend.Enums.*;

import java.time.LocalDate;

public record ListarDocentesDto(

        Integer id,
        String nome,
        String email,
        EstadoDocente estado,
        Integer departamentoId,
        String departamentoNome,
        Integer contratoId,
        String contratoDescricao,
        EstadoContrato contratoEstado,
        LocalDate contratoDataInicio,
        LocalDate contratoDataFim,
        CategoriaDocente contratoCategoria,
        RelacaoJuridicaDocente contratoRelacaoJuridica,
        DedicacaoDocente contratoDedicacao

) {



}
