package com.example.backend.Utils;

import com.example.backend.Dto.Atribuicoes.DadosUcDistribuicaoDto;
import com.example.backend.Entity.ContratoDocente;
import com.example.backend.Entity.Distribuicao;
import com.example.backend.Entity.Docente;
import com.example.backend.Entity.UC_Distribuicao;
import com.example.backend.Enums.DedicacaoDocente;
import com.example.backend.Enums.EstadoContrato;
import com.example.backend.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UtilsServices {

    @Autowired
    private DistribuicaoRepository distribuicaoRepository;

    @Autowired
    private ContratoDocenteRepository contratoDocenteRepository;

    @Autowired
    private UcJuncaoRepository  ucJuncaoRepository;

    @Autowired
    private UcDistribuicaoRepository ucDistribuicaoRepository;
    @Autowired
    private DocenteRepository docenteRepository;

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

        LocalDate hoje = LocalDate.now();

        return contratos.stream()

                .filter(c -> c.getEstado() == EstadoContrato.ATIVO)
                .filter(c -> c.getDataFim() == null || c.getDataFim().isAfter(hoje))
                .max(Comparator.comparing(ContratoDocente::getDataInicio))
                .orElse(null);
    }

    public Integer resolverSemestre(UC_Distribuicao ucDistribuicao) {

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

    public boolean temDuplicados(UC_Distribuicao ucDistribuicao, DadosUcDistribuicaoDto dto) {
        if (ucDistribuicao == null || ucDistribuicao.getDistribuicao() == null) {
            return false;
        }

        Integer distribuicaoId = ucDistribuicao.getDistribuicao().getId();


        if (ucDistribuicao.getUcTipoAula() != null) {
            long total = ucDistribuicaoRepository.countByDistribuicao_IdAndUcTipoAula_Id(
                    distribuicaoId,
                    ucDistribuicao.getUcTipoAula().getId()
            );
            return total > 1;
        }
        else{

            long total = ucJuncaoRepository.countByUcTipoAula_IdAndUcDistribuicao_Distribuicao_Id(dto.ucTipoAulaId() ,ucDistribuicao.getDistribuicao().getId());
            return total > 1;
        }


    }

    public void atualizarHorasTurnos(Integer ucDistribucaoId, float novasHoras) {

        // Procura todos o filho do ucDistribucaoId atual
        UC_Distribuicao ucDistribuicao = ucDistribuicaoRepository.findByUcDistribuicaPai_Id(ucDistribucaoId);

        if (ucDistribuicao == null) {
            return;
        }

        ucDistribuicao.setHoras_atribuidas(novasHoras);

        // Chamada recursiva: vai procurar os filhos deste filho
        atualizarHorasTurnos(ucDistribuicao.getId(), novasHoras);

    }

    public float getDocenteHoras(Integer docente_id, Integer distribuicao_id) {

        List<UC_Distribuicao> ucDistribuicoes = ucDistribuicaoRepository.findByDocente_IdAndDistribuicao_Id(docente_id, distribuicao_id);

        ContratoDocente contrato = getContratoAtual(docente_id);

        float horasTotais = 0;

        for(UC_Distribuicao ucDistribuicao : ucDistribuicoes){

            horasTotais+=ucDistribuicao.getHoras_atribuidas();

        }

        float horasSemestre = 16;

        if(contrato != null && contrato.getDedicacao().equals(DedicacaoDocente.TEMPO_PARCIAL)){
            horasSemestre=20;
        }


        //numero de horas totais do semestre 1 e 2 / horas por semestre * 2 pois estamos a fazer ano total
        return Math.round(horasTotais/(horasSemestre*2) * 10f)/10f;

    }

   public String resolverNomeUc(UC_Distribuicao uc) {
        if (uc.getUcTipoAula() != null) {
            return uc.getUcTipoAula().getUc().getNome() + " — " + uc.getUcTipoAula().getTipo_aula().getNome();
        }
        return ucJuncaoRepository.findByUcDistribuicao_Id(uc.getId())
                .stream().findFirst()
                .map(j -> j.getUcTipoAula().getUc().getNome() + " (junção)")
                .orElse("UC desconhecida");
    }


}
