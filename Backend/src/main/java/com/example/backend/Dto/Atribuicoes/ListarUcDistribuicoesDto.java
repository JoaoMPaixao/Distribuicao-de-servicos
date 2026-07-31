package com.example.backend.Dto.Atribuicoes;

public record ListarUcDistribuicoesDto(

        Integer id,
        Integer turno,
        float horasAtribuidas,
        Integer ucDistribuicaoPaiId,
        boolean temFilhos,
        boolean temDuplicado,
        Integer docenteId,
        String docenteNome,

        Integer cursoId,
        String cursoNome,
        String ucDepartamentoNome,
        String ucNome,
        Integer ucAno,
        Integer ucSemestre,
        Integer ucTipoAulaId,
        String ucTipoNome,
        float ucTipoHoras,
        Boolean temJuncao,
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
