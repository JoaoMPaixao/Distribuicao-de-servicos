package com.example.backend.Utils;

import com.example.backend.Entity.ContratoDocente;
import com.example.backend.Entity.Distribuicao;
import com.example.backend.Entity.Docente;
import com.example.backend.Entity.UC_Distribuicao;
import com.example.backend.Enums.EstadoContrato;
import com.example.backend.Repository.ContratoDocenteRepository;
import com.example.backend.Repository.DistribuicaoRepository;
import com.example.backend.Repository.UcJuncaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
public class UtilsServices {

    @Autowired
    private DistribuicaoRepository distribuicaoRepository;

    @Autowired
    private ContratoDocenteRepository contratoDocenteRepository;

    @Autowired
    private UcJuncaoRepository  ucJuncaoRepository;

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

    // auxiliar genérico para média de dois valores nullable
    public Double media(Number a, Number b) {
        if (a != null && b != null) return (a.doubleValue() + b.doubleValue()) / 2.0;
        if (a != null) return a.doubleValue();
        if (b != null) return b.doubleValue();
        return null;
    }

    public ContratoDocente getContratoAtual(Integer docente_id) {

        List<ContratoDocente> contratos = contratoDocenteRepository.findByDocente_Id(docente_id);

        if (contratos == null || contratos.isEmpty()) {
            return null;
        }

        Date hoje = new Date();

        return contratos.stream()

                .filter(c -> c.getEstado() == EstadoContrato.ATIVO)
                .filter(c -> c.getDataFim() == null || c.getDataFim().after(hoje))
                .max(Comparator.comparing(ContratoDocente::getDataInicio))
                .orElse(null);
    }

    public String resolverSemestre(UC_Distribuicao ucDistribuicao) {

        // linha normal
        if (ucDistribuicao.getUcTipoAula() != null) {
            return ucDistribuicao.getUcTipoAula().getUc().getSemestre();
        }

        // linha de junção — vai buscar o semestre do primeiro ucTipoAula da junção
        return ucJuncaoRepository.findByUcDistribuicao_Id(ucDistribuicao.getId())
                .stream()
                .findFirst()
                .map(j -> j.getUcTipoAula().getUc().getSemestre())
                .orElse(null);
    }


}
