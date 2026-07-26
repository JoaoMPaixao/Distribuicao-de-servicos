package com.example.backend.Dto.Atribuicoes;

public record CalculosUcDistribuicaoDto(

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
