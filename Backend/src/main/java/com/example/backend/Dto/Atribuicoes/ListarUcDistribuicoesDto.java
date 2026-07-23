package com.example.backend.Dto.Atribuicoes;

public record ListarUcDistribuicoesDto(

        Integer id,
        String turno,
        Float horasAtribuidas,
        Integer docenteId,
        String docenteNome,
        String cursoNome,
        String cursoDepartamentoNome,
        String ucNome,
        String ucAno,
        String ucSemestre,
        String ucTipoNome


) {
}
