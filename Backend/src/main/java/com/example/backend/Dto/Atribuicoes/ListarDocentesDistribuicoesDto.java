package com.example.backend.Dto.Atribuicoes;

public record ListarDocentesDistribuicoesDto(

        Integer id,
        String nome,
        CargaHorariaDocenteDto carga_horaria_max,
        DepartamentoDocenteDto departamento

) {

}
