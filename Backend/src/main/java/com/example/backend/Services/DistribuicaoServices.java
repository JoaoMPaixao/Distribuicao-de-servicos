package com.example.backend.Services;

import com.example.backend.Dto.Distribuicoes.*;
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
import java.util.*;
import java.util.stream.Collectors;

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

        Map<Integer, UC_Distribuicao> mapaClones = new HashMap<>();

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
            ucDistribuicaoClone.setUcDistribuicaPai(null);

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

        for (UC_Distribuicao ucDistribuicaoOriginal : ucDistribuicoes) {

            // Se a UC original tinha um pai atribuído...
            if (ucDistribuicaoOriginal.getUcDistribuicaPai() != null) {

                Integer idPaiOriginal = ucDistribuicaoOriginal.getUcDistribuicaPai().getId();

                // Procura o clone que corresponde ao pai original
                UC_Distribuicao novoPaiClone = mapaClones.get(idPaiOriginal);

                // Procura o clone atual correspondente
                UC_Distribuicao novoFilhoClone = mapaClones.get(ucDistribuicaoOriginal.getId());

                if (novoFilhoClone != null && novoPaiClone != null) {
                    novoFilhoClone.setUcDistribuicaPai(novoPaiClone);
                }
            }
        }


        ucDistribuicaoRepository.saveAll(clones);
        ucJuncaoRepository.saveAll(Juncaoclones);

        // LOG DE AUDITORIA: Clonagem
        auditLogger.info("Utilizador ID: {} CLONOU a Distribuição ID: {} para a nova Distribuição ID: {}.",
                utilizador_id, distribuicao_id, distribuicaoClone.getId());

    }

    public ComparacaoDistribuicoesDto CompararDistribuicoes(Integer dist1Id, Integer dist2Id, Integer departamentoId) {

        Distribuicao dist1 = utilsServices.getDistribuicaoComAcesso(dist1Id, departamentoId);
        Distribuicao dist2 = utilsServices.getDistribuicaoComAcesso(dist2Id, departamentoId);

        List<UC_Distribuicao> linhas1 = ucDistribuicaoRepository.findByDistribuicao_Id(dist1Id);
        List<UC_Distribuicao> linhas2 = ucDistribuicaoRepository.findByDistribuicao_Id(dist2Id);

        // indexa as linhas da distribuição 2 por ucTipoAulaId + turno para comparação rápida
        Map<String, UC_Distribuicao> indexDist2 = linhas2.stream()
                .filter(uc -> uc.getUcTipoAula() != null)
                .collect(Collectors.toMap(
                        uc -> uc.getUcTipoAula().getId() + "_" + uc.getTurno(),
                        uc -> uc,
                        (a, b) -> a // em caso de duplicado, fica o primeiro
                ));

        List<ComparacaoLinhaDto> linhasComparacao = linhas1.stream()
                .filter(uc -> uc.getUcTipoAula() != null) // ignora junções por agora
                .map(uc1 -> {
                    String chave = uc1.getUcTipoAula().getId() + "_" + uc1.getTurno();
                    UC_Distribuicao uc2 = indexDist2.get(chave);

                    String docente1 = uc1.getDocente() != null ? uc1.getDocente().getNome() : null;
                    String docente2 = uc2 != null && uc2.getDocente() != null ? uc2.getDocente().getNome() : null;
                    float horas1 = uc1.getHoras_atribuidas();
                    float horas2 = uc2.getHoras_atribuidas();

                    boolean docenteIgual = Objects.equals(docente1, docente2);
                    boolean horasIguais = Objects.equals(horas1, horas2);

                    return new ComparacaoLinhaDto(
                            uc1.getUcTipoAula().getUc().getNome(),
                            uc1.getUcTipoAula().getTipo_aula().getNome(),
                            String.valueOf(uc1.getTurno()),
                            docente1, horas1,
                            docente2, horas2,
                            docenteIgual, horasIguais,
                            !docenteIgual || !horasIguais
                    );
                })
                .sorted(Comparator.comparing(ComparacaoLinhaDto::ucNome, String.CASE_INSENSITIVE_ORDER))
                .toList();

        long totalDiferencas = linhasComparacao.stream().filter(ComparacaoLinhaDto::temDiferenca).count();

        return new ComparacaoDistribuicoesDto(
                dist1.getNome(), dist2.getNome(),
                linhasComparacao, (int) totalDiferencas
        );
    }

}


