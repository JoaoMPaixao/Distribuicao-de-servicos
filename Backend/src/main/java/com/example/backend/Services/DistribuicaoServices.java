package com.example.backend.Services;

import com.example.backend.Dto.CriarDistribuicaoDto;
import com.example.backend.Dto.ListarDistribuicaoDto;
import com.example.backend.Entity.Departamento;
import com.example.backend.Entity.Distribuicao;
import com.example.backend.Entity.Utilizador;
import com.example.backend.Enums.EstadoDistribuicao;
import com.example.backend.Repository.DepartamentoRepository;
import com.example.backend.Repository.DistribuicaoRepository;
import com.example.backend.Repository.UtilizadorRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
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

        return distribuicaoRepository.findByDepartamento_Id(departamento_id)
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
    public void CriarDistribuicao(CriarDistribuicaoDto dto){


        Departamento departamento = departamentoRepository.findById(dto.departamento_id())
                .orElseThrow(() -> new RuntimeException("Departamento não encontrado"));

        Utilizador utilizador = utilizadorRepository.findById(dto.utilizador_id())
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

}
