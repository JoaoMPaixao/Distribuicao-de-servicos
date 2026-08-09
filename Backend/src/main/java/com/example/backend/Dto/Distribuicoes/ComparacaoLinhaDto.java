package com.example.backend.Dto.Distribuicoes;

public record ComparacaoLinhaDto(

        String ucNome,
        String ucTipoNome,
        String turno,
        // distribuição 1
        String docenteNome1,
        Float horasAtribuidas1,
        // distribuição 2
        String docenteNome2,
        Float horasAtribuidas2,
        // diferença
        boolean docenteIgual,
        boolean horasIguais,
        boolean temDiferenca

) {
}
