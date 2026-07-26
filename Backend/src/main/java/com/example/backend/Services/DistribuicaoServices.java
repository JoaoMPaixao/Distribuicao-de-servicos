package com.example.backend.Services;

import com.example.backend.Dto.Distribuicoes.AlterarEstadoDistribuicaoDto;
import com.example.backend.Dto.Distribuicoes.CriarDistribuicaoDto;
import com.example.backend.Dto.Distribuicoes.ListarDistribuicaoDto;
import com.example.backend.Entity.*;
import com.example.backend.Enums.EstadoDistribuicao;
import com.example.backend.Repository.*;
import com.example.backend.Utils.UtilsServices;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class DistribuicaoServices {

    // Logger a apontar para o ficheiro estabelecido no xml
    private static final Logger auditLogger = LoggerFactory.getLogger("auditoria");

    @Autowired
    private DistribuicaoRepository distribuicaoRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Autowired
    private UtilizadorRepository utilizadorRepository;

    @Autowired
    private UcDistribuicaoRepository ucDistribuicaoRepository;

    @Autowired
    private UcDistribuicaoServices ucDistribuicaoServices;

    @Autowired
    private UcJuncaoRepository  ucJuncaoRepository;

    @Autowired
    private UtilsServices utilsServices;

    public ListarDistribuicaoDto getDistribuicaoById(Integer id) {


        return distribuicaoRepository.findByIdAndEstadoNot(id,EstadoDistribuicao.INATIVO)
                .map(distribuicao -> new ListarDistribuicaoDto(
                        distribuicao.getId(),
                        distribuicao.getNome(),
                        distribuicao.getVersao(),
                        distribuicao.getDescricao(),
                        distribuicao.getAno_letivo(),
                        distribuicao.getData_criacao(),
                        distribuicao.getEstado().name()
                )).orElseThrow(() -> new RuntimeException("Distribuição não encontrada "));

    }

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

        ucDistribuicaoServices.GerarUcDistribuicoes(distribuicao.getId(),departamento_id);

        // LOG DE AUDITORIA: Criação
        auditLogger.info("Utilizador ID: {} CRIOU a Distribuição ID: {} ('{}') no Departamento ID: {}.",
                utilizador_id, distribuicao.getId(), distribuicao.getNome(), departamento_id);

    }

    @Transactional
    public void DesativarDistribuicao(Integer distribuicao_id, Integer departamento_id, Integer utilizador_id) {

        //verifica se a distribuicao e o utilizador têm o mesmo departamento e retorna a distribuicao
        Distribuicao distribuicao = utilsServices.getDistribuicaoComAcesso(distribuicao_id, departamento_id);

        distribuicao.setEstado(EstadoDistribuicao.INATIVO);

        // LOG DE AUDITORIA: Desativação
        auditLogger.info("Utilizador ID: {} APAGOU a Distribuição ID: {} no Departamento ID: {}.",
                utilizador_id, distribuicao_id, departamento_id);

    }

    @Transactional
    public void AlterarEstadoDistribuicao(AlterarEstadoDistribuicaoDto dto, Integer distribuicao_id, Integer departamento_id, Integer utilizador_id) {

        //verifica se a distribuicao e o utilizador têm o mesmo departamento e retorna a distribuicao
        Distribuicao distribuicao = utilsServices.getDistribuicaoComAcesso(distribuicao_id, departamento_id);
        EstadoDistribuicao estadoAnterior = distribuicao.getEstado();
        distribuicao.setEstado(dto.estado());

        // LOG DE AUDITORIA: Alteração de Estado
        auditLogger.info("Utilizador ID: {} ALTEROU o Estado da Distribuição ID: {} de {} para {} no Departamento ID: {}.",
                utilizador_id, distribuicao_id, estadoAnterior, dto.estado(), departamento_id);

    }

    @Transactional
    public void ClonarDistribuicao(Integer distribuicao_id, Integer departamento_id, Integer utilizador_id) {

        //verifica se a distribuicao e o utilizador têm o mesmo departamento e retorna a distribuicao
        Distribuicao distribuicao = utilsServices.getDistribuicaoComAcesso(distribuicao_id, departamento_id);

        Utilizador utilizador = utilizadorRepository.findById(utilizador_id)
                .orElseThrow(() -> new RuntimeException("Utilizador não encontrado"));

        Distribuicao distribuicaoClone = new Distribuicao();
        distribuicaoClone.setNome(distribuicao.getNome() + " (Clone)");
        distribuicaoClone.setVersao(distribuicao.getVersao());
        distribuicaoClone.setDescricao(distribuicao.getDescricao());
        distribuicaoClone.setAno_letivo(distribuicao.getAno_letivo());

        distribuicaoClone.setDepartamento(distribuicao.getDepartamento());
        distribuicaoClone.setUtilizador(utilizador);


        distribuicaoClone.setData_criacao(LocalDate.now());
        distribuicaoClone.setEstado(EstadoDistribuicao.ABERTO);

        distribuicaoRepository.save(distribuicaoClone);

        List<UC_Distribuicao> ucDistribuicoes = ucDistribuicaoRepository.findByDistribuicao_Id(distribuicao_id);

        //array para guardar todas as atribuicoes clonadas para depois dar saveall
        List<UC_Distribuicao> clones = new ArrayList<>();

        //array para guardar todas as atribuicoes clonadas para depois dar saveall
        List<UC_Juncao> Juncaoclones = new ArrayList<>();

        for(UC_Distribuicao ucDistribuicao : ucDistribuicoes){

            UC_Distribuicao ucDistribuicaoClone = new UC_Distribuicao();

            ucDistribuicaoClone.setDistribuicao(distribuicaoClone);
            ucDistribuicaoClone.setHoras_atribuidas(ucDistribuicao.getHoras_atribuidas());
            ucDistribuicaoClone.setEstado(ucDistribuicao.getEstado());
            ucDistribuicaoClone.setTurno(ucDistribuicao.getTurno());
            ucDistribuicaoClone.setDocente(ucDistribuicao.getDocente());

            List<UC_Juncao> ucJuncoes = ucJuncaoRepository.findByUcDistribuicao_Id(ucDistribuicao.getId());

            //se uc tipo aula na ucDistribuicao nao for null significa que nao temos juncao
            if(ucDistribuicao.getUcTipoAula() != null){
                ucDistribuicaoClone.setUcTipoAula(ucDistribuicao.getUcTipoAula());
            }
            else{

                for(UC_Juncao ucJuncao : ucJuncoes){

                    UC_Juncao ucJuncaoClone = new UC_Juncao();

                    ucJuncaoClone.setUcDistribuicao(ucDistribuicaoClone);
                    ucJuncaoClone.setUcTipoAula(ucJuncao.getUcTipoAula());

                    Juncaoclones.add(ucJuncaoClone);

                }

            }

            clones.add(ucDistribuicaoClone);
        }
        ucDistribuicaoRepository.saveAll(clones);
        ucJuncaoRepository.saveAll(Juncaoclones);

        // LOG DE AUDITORIA: Clonagem
        auditLogger.info("Utilizador ID: {} CLONOU a Distribuição ID: {} para a nova Distribuição ID: {}.",
                utilizador_id, distribuicao_id, distribuicaoClone.getId());

    }

}


