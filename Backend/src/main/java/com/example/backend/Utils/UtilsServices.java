package com.example.backend.Utils;

import com.example.backend.Entity.Distribuicao;
import com.example.backend.Repository.DistribuicaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UtilsServices {

    @Autowired
    private DistribuicaoRepository distribuicaoRepository;

    //serviço para verificar se a distribuicao e o utilizador estao no mesmo departamento
    public void validarAcessoDistribuicao(Integer distribuicaoId, Integer departamentoId) {


        // primeiro verifica se existe → 404 se não existir
        if (!distribuicaoRepository.existsById(distribuicaoId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Distribuição não encontrada");
        }

        // só depois verifica se pertence ao departamento → 403 se não pertencer
        if (!distribuicaoRepository.existsByIdAndDepartamentoId(distribuicaoId, departamentoId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Sem acesso a esta distribuição");
        }


    }

    //serviço para verificar se a distribuicao e o utilizador estao no mesmo departamento e que retorna a distribuicao
    public Distribuicao getDistribuicaoComAcesso(Integer distribuicaoId, Integer departamentoId) {

        Distribuicao distribuicao = distribuicaoRepository.findById(distribuicaoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Distribuição não encontrada"
                ));

        if (!distribuicao.getDepartamento().getId().equals(departamentoId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Sem acesso a esta distribuição"
            );
        }

        return distribuicao;
    }
}
