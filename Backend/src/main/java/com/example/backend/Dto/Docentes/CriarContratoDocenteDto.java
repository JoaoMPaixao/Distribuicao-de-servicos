package com.example.backend.Dto.Docentes;

import com.example.backend.Enums.CategoriaDocente;
import com.example.backend.Enums.DedicacaoDocente;
import com.example.backend.Enums.EstadoContrato;
import com.example.backend.Enums.RelacaoJuridicaDocente;

import java.time.LocalDate;
import java.util.Date;

public record CriarContratoDocenteDto(

        String contratoDescricao,
        LocalDate contratoDataInicio,
        LocalDate contratoDataFim,
        CategoriaDocente contratoCategoria,
        RelacaoJuridicaDocente contratoRelacaoJuridica,
        DedicacaoDocente contratoDedicacao,
        Integer docenteId

) {
}
