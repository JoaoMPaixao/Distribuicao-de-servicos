package com.example.backend.Dto.Atribuicoes;

public record ListarUcDistribuicoesDto(

        Integer id,
        Integer turno,
        Float horasAtribuidas,
        Integer docenteId,
        String docenteNome,

        Integer cursoId,
        String cursoNome,
        String ucDepartamentoNome,
        String ucNome,
        String ucAno,
        String ucSemestre,
        Integer ucTipoAulaId,
        String ucTipoNome,
        float ucTipoHoras,
        Integer nSemanas,

        // Horas/Semestre UC
        float horasSemestreS1,
        float horasSemestreS2,
        float horasSemestreMedia,

        // Horas/Semana UC
        float horasSemanaS1,
        float horasSemanaS2,

        // Horas Totais/Semana Docente
        float horasDocenteS1,
        float horasDocenteS2,
        float horasDocenteMedia


) {
}
