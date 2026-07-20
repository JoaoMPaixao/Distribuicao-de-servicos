package com.example.backend.Dto.Atribuicoes;

public record ListarUcDistribuicoesDto(

        Integer id,
        String turno,
        Float horasAtribuidas,
        Integer docenteId,
        String docenteNome,
       UcCursoDto ucCursoDto


) {
}
