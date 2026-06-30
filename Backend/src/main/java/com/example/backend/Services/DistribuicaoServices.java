package com.example.backend.Services;

import com.example.backend.Dto.Distribuicoes.CriarDistribuicaoDto;
import com.example.backend.Dto.Distribuicoes.ListarDistribuicaoDto;
import com.example.backend.Entity.Departamento;
import com.example.backend.Entity.Distribuicao;
import com.example.backend.Entity.Utilizador;
import com.example.backend.Enums.EstadoDistribuicao;
import com.example.backend.Repository.DepartamentoRepository;
import com.example.backend.Repository.DistribuicaoRepository;
import com.example.backend.Repository.UtilizadorRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DistribuicaoServices {

    @Autowired
    private DistribuicaoRepository distribuicaoRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Autowired
    private UtilizadorRepository utilizadorRepository;

    public List<ListarDistribuicaoDto> getDistribuicoesByDepartamentoId(Integer departamento_id) {


        return distribuicaoRepository.findByDepartamento_IdAndEstadoNot(departamento_id, EstadoDistribuicao.INATIVO)
                .stream()
                .map(distribuicao -> new ListarDistribuicaoDto(
                        distribuicao.getId(),
                        distribuicao.getNome(),
                        distribuicao.getVersao(),
                        distribuicao.getDescricao(),
                        distribuicao.getAno_letivo(),
                        distribuicao.getData_criacao(),
                        distribuicao.getEstado().name()
                ))
                .toList();
    }

    @Transactional
    public void CriarDistribuicao(CriarDistribuicaoDto dto, Integer departamento_id, Integer utilizador_id) {


        Departamento departamento = departamentoRepository.findById(departamento_id)
                .orElseThrow(() -> new RuntimeException("Departamento não encontrado"));

        Utilizador utilizador = utilizadorRepository.findById(utilizador_id)
                .orElseThrow(() -> new RuntimeException("Utilizador não encontrado"));


        Distribuicao distribuicao = new Distribuicao();
        distribuicao.setNome(dto.nome());
        distribuicao.setVersao(dto.versao());
        distribuicao.setDescricao(dto.descricao());
        distribuicao.setAno_letivo(dto.ano_letivo());

        distribuicao.setDepartamento(departamento);
        distribuicao.setUtilizador(utilizador);


        distribuicao.setData_criacao(LocalDate.now());
        distribuicao.setEstado(EstadoDistribuicao.ABERTO);


        distribuicaoRepository.save(distribuicao);

    }

    @Transactional
    public void DesativarDistribuicao(Integer distribuicao_id, Integer utilizador_id) {

        Distribuicao distribuicao = distribuicaoRepository.findById(distribuicao_id).orElseThrow(() -> new EntityNotFoundException("Distribuicao não encontrada"));

        if(distribuicao.getUtilizador().getId().equals(utilizador_id)) {

            distribuicao.setEstado(EstadoDistribuicao.INATIVO);
        }
    }

}
