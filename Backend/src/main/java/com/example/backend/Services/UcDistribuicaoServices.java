package com.example.backend.Services;

import com.example.backend.Dto.Atribuicoes.*;
import com.example.backend.Dto.Departamentos.ListarDepartamentosDto;
import com.example.backend.Entity.*;
import com.example.backend.Enums.*;
import com.example.backend.Repository.*;
import com.example.backend.Utils.UtilsServices;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UcDistribuicaoServices {

    //  logger definido no logback-spring.xml
    private static final Logger auditLogger = LoggerFactory.getLogger("auditoria");

    @Autowired
    private UcDistribuicaoRepository ucDistribuicaoRepository;

    @Autowired
    private DocenteRepository docenteRepository;

    @Autowired
    private UcTipoAulaRepository  ucTipoAulaRepository;

    @Autowired
    private UcJuncaoRepository ucJuncaoRepository;

    @Autowired
    private UtilsServices utilsServices;

    @Transactional
    public void GerarUcDistribuicoes(Integer distribuicao_id, Integer departamento_id){

        // Verificar se existe a distribuição, docente e uc_tipo_aula
        Distribuicao distribuicao = utilsServices.getDistribuicaoComAcesso(distribuicao_id, departamento_id);

        List<UC_Tipo_Aula> uc_tipo_aulas = ucTipoAulaRepository.findByUc_Curso_DepartamentoIdAndUc_EstadoNotAndUc_Curso_EstadoNot(departamento_id, EstadoUc.INATIVO, EstadoCurso.INATIVO);

        List<UC_Distribuicao> ucDistribuicoes = new ArrayList<>();

        for (UC_Tipo_Aula uc_tipo_aula : uc_tipo_aulas) {

            UC_Distribuicao ucDistribuicao = new UC_Distribuicao();

            ucDistribuicao.setHoras_atribuidas(uc_tipo_aula.getHoras());
            ucDistribuicao.setTurno(1);
            ucDistribuicao.setUcTipoAula(uc_tipo_aula);
            ucDistribuicao.setDistribuicao(distribuicao);
            ucDistribuicao.setEstado(EstadoUcDistribuicao.ATIVO);

            ucDistribuicoes.add(ucDistribuicao);

        }

        ucDistribuicaoRepository.saveAll(ucDistribuicoes);

    }

    @Transactional
    public List<ListarUcDistribuicoesDto> getUcDistribuicoes(Integer distribuicao_id) {

        return ucDistribuicaoRepository.findByDistribuicao_Id(distribuicao_id)
                .stream()
                .map(ucDistribuicao -> {


                    DadosUcDistribuicaoDto dados = mapearUcDistribuicoes(ucDistribuicao);

                    CalculosUcDistribuicaoDto calculos = getUcDistribuicoesCalculos(ucDistribuicao,dados);



                    List<UC_Distribuicao> ucDistribuicoes = ucDistribuicaoRepository.findAllByUcDistribuicaPai_Id(ucDistribuicao.getId());

                    boolean temFilhos = !ucDistribuicoes.isEmpty();

                    boolean temDuplicado = utilsServices.temDuplicados(ucDistribuicao, dados);

                    return new ListarUcDistribuicoesDto(
                            ucDistribuicao.getId(),
                            ucDistribuicao.getTurno(),
                            ucDistribuicao.getHoras_atribuidas(),
                            Optional.ofNullable(ucDistribuicao.getUcDistribuicaPai()).map(UC_Distribuicao::getId).orElse(null),
                            temFilhos,
                            temDuplicado,
                            Optional.ofNullable(ucDistribuicao.getDocente()).map(Docente::getId).orElse(null),
                            Optional.ofNullable(ucDistribuicao.getDocente()).map(Docente::getNome).orElse(null),
                            dados.cursoId(),
                            dados.cursoNome(),
                            dados.ucDepartamentoNome(),
                            dados.ucNome(),
                            dados.ucAno(),
                            dados.ucSemestre(),
                            dados.ucTipoAulaId(),
                            dados.ucTipoNome(),
                            dados.ucTipoHoras(),
                            dados.temJuncao(),
                            calculos.nSemanas(),
                            calculos.horasSemestreS1(),
                            calculos.horasSemestreS2(),
                            calculos.horasSemestreMedia(),
                            calculos.horasSemanaS1(),
                            calculos.horasSemanaS2(),
                            calculos.horasDocenteS1(),
                            calculos.horasDocenteS2(),
                            calculos.horasDocenteMedia()
                    );
                })
                .sorted(Comparator
                        // 1. Curso
                        .comparing(ListarUcDistribuicoesDto::cursoNome, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER))
                        // 2. Ano
                        .thenComparing(ListarUcDistribuicoesDto::ucAno, Comparator.nullsLast(Comparator.naturalOrder()))
                        // 3. Semestre
                        .thenComparing(ListarUcDistribuicoesDto::ucSemestre, Comparator.nullsLast(Comparator.naturalOrder()))
                        // 4. UC
                        .thenComparing(ListarUcDistribuicoesDto::ucNome, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER))
                        // 5. Tipo de Aula
                        .thenComparing(ListarUcDistribuicoesDto::ucTipoNome, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER))
                )
                .toList();
    }

    private DadosUcDistribuicaoDto mapearUcDistribuicoes(UC_Distribuicao ucDistribuicao) {

        // linha normal
        if (ucDistribuicao.getUcTipoAula() != null) {

            return new DadosUcDistribuicaoDto(
                    ucDistribuicao.getUcTipoAula().getUc().getCurso().getId(),
                    ucDistribuicao.getUcTipoAula().getUc().getCurso().getNome(),
                    ucDistribuicao.getUcTipoAula().getUc().getDepartamento().getNome(),
                    ucDistribuicao.getUcTipoAula().getUc().getNome(),
                    ucDistribuicao.getUcTipoAula().getUc().getAno_curricular(),
                    ucDistribuicao.getUcTipoAula().getUc().getSemestre(),
                    ucDistribuicao.getUcTipoAula().getId(),
                    ucDistribuicao.getUcTipoAula().getTipo_aula().getNome(),
                    ucDistribuicao.getUcTipoAula().getHoras(),
                    false

            );
        }

        // linha de junção
        List<UC_Juncao> juncoes = ucJuncaoRepository.findByUcDistribuicao_Id(ucDistribuicao.getId());

        if (juncoes.isEmpty()) return null;

        var primeira = juncoes.get(0);

        String cursosDisplay = juncoes.stream()
                .map(j -> j.getUcTipoAula().getUc().getCurso().getNome())
                .distinct()
                .collect(Collectors.joining(" / "));

        String ucsDisplay = juncoes.stream()
                .map(j -> j.getUcTipoAula().getUc().getNome())
                .distinct()
                .collect(Collectors.joining(" / "));

        String departamentosDisplay = juncoes.stream()
                .map(j -> j.getUcTipoAula().getUc().getDepartamento().getNome())
                .distinct()
                .collect(Collectors.joining(" / "));


        String tipoAulaDisplay = juncoes.stream()
                .map(j -> j.getUcTipoAula().getTipo_aula().getNome())
                .distinct()
                .collect(Collectors.joining(" / "));

        return new DadosUcDistribuicaoDto(
                primeira.getUcTipoAula().getUc().getCurso().getId(),
                cursosDisplay,
                departamentosDisplay,
                ucsDisplay,
                primeira.getUcTipoAula().getUc().getAno_curricular(),
                primeira.getUcTipoAula().getUc().getSemestre(),
                primeira.getUcTipoAula().getId(),
                tipoAulaDisplay,
                primeira.getUcTipoAula().getHoras(),
                true

        );
    }

    private CalculosUcDistribuicaoDto getUcDistribuicoesCalculos(UC_Distribuicao ucDistribuicao, DadosUcDistribuicaoDto dados) {

        Integer nSemanas = 16;
        float horasSemestreS1 = 0;
        float horasSemestreS2 = 0;
        float horasSemestreMedia = 0;

        float horasDocenteS1 = 0;
        float horasDocenteS2 = 0;

        // Se não houver docente atribuído a esta UC, retorna valores zerados padrão
        if (ucDistribuicao.getDocente() == null) {
            return new CalculosUcDistribuicaoDto(nSemanas, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);
        }

        // 1. Busca o contrato do docente para definir nSemanas
        ContratoDocente contratoDocente = utilsServices.getContratoAtual(ucDistribuicao.getDocente().getId());
        if (contratoDocente != null && contratoDocente.getDedicacao() == DedicacaoDocente.TEMPO_PARCIAL) {
            nSemanas = 20;
        }

        // 2. Define as horas do semestre para esta UC específica
        Integer semestreUC = dados.ucSemestre();
        float horasAtribuidas =  ucDistribuicao.getHoras_atribuidas();

        if(ucDistribuicao.getUcDistribuicaPai() == null){
            if (semestreUC == 1) {
                horasSemestreS1 = horasAtribuidas;
            } else if (semestreUC == 2) {
                horasSemestreS2 = horasAtribuidas;
            }
        }


        // 3. Busca todas as UCs atribuídas ao mesmo docente para calcular totais do docente
        List<UC_Distribuicao> ucDistribuicoes = ucDistribuicaoRepository.findByDocente_IdAndDistribuicao_Id(ucDistribuicao.getDocente().getId(), ucDistribuicao.getDistribuicao().getId());

        for (UC_Distribuicao UcD : ucDistribuicoes) {

            if(UcD.getUcDistribuicaPai() == null) {
                horasSemestreMedia += UcD.getHoras_atribuidas();
            }
            Integer sem = utilsServices.resolverSemestre(UcD);
            if (sem == 1) {
                horasDocenteS1 += UcD.getHoras_atribuidas();
            } else if (sem == 2) {
                horasDocenteS2 += UcD.getHoras_atribuidas();
            }

        }

        // Cálculos finais
        horasSemestreMedia = horasSemestreMedia / 2;
        float horasSemanaS1 = horasSemestreS1 / nSemanas;
        float horasSemanaS2 = horasSemestreS2 / nSemanas;
              horasDocenteS1 = horasDocenteS1 / nSemanas;
              horasDocenteS2 = horasDocenteS2 / nSemanas;
        float horasDocenteMedia = (horasDocenteS1 + horasDocenteS2) / 2;

        return new CalculosUcDistribuicaoDto(
                nSemanas,
                horasSemestreS1 = Math.round(horasSemestreS1 * 10f)/10f,
                horasSemestreS2 = Math.round(horasSemestreS2 * 10f)/10f,
                horasSemestreMedia = Math.round(horasSemestreMedia * 10f)/10f,
                horasSemanaS1 = Math.round(horasSemanaS1 * 10f)/10f,
                horasSemanaS2 = Math.round(horasSemanaS2 * 10f)/10f,
                horasDocenteS1 = Math.round(horasDocenteS1 * 10f)/10f,
                horasDocenteS2 = Math.round(horasDocenteS2 * 10f)/10f,
                horasDocenteMedia = Math.round(horasDocenteMedia * 10f)/10f
        );
    }

    @Transactional
    public void CriarUcDistribuicao(Integer ucDistribuicao_id, Integer distribuicao_id, Integer departamento_id, Integer utilizador_id) {

        // Verificar se existe a distribuição, docente e uc_tipo_aula
        Distribuicao distribuicao = utilsServices.getDistribuicaoComAcesso(distribuicao_id, departamento_id);

        UC_Distribuicao ucDistribuicaoPai = ucDistribuicaoRepository.findById(ucDistribuicao_id)
                .orElseThrow(() -> new RuntimeException("UcDistribuicao não encontrada"));

        Docente docente = null;
        if(ucDistribuicaoPai.getDocente() != null){
            docente = docenteRepository.findById(ucDistribuicaoPai.getDocente().getId())
                    .orElseThrow(() -> new RuntimeException("Docente não encontrado"));
        }


        if(ucDistribuicaoPai.getUcTipoAula() != null){

            UC_Tipo_Aula ucTipoAula = ucTipoAulaRepository.findById(ucDistribuicaoPai.getUcTipoAula().getId())
                    .orElseThrow(() -> new RuntimeException("UC tipo aula não encontrada"));

            UC_Distribuicao ucDistribuicao = new UC_Distribuicao();
            ucDistribuicao.setTurno(ucDistribuicaoPai.getTurno());
            ucDistribuicao.setHoras_atribuidas(0);
            ucDistribuicao.setDocente(docente);
            ucDistribuicao.setUcTipoAula(ucTipoAula);
            ucDistribuicao.setDistribuicao(distribuicao);
            ucDistribuicao.setEstado(EstadoUcDistribuicao.ATIVO);

            ucDistribuicaoRepository.save(ucDistribuicao);

            // LOG DE AUDITORIA: Criação
            auditLogger.info("Utilizador ID: {} CRIOU UC_Distribuicao ID {} Copia da UC_DistribuicaoPai: {} (Turno: {}, Horas: {}) na Distribuição ID: {} (Departamento ID: {}). Docente ID: {}, UCTipoAula ID: {}.",
                    utilizador_id, ucDistribuicao.getId(), ucDistribuicaoPai.getId(), ucDistribuicao.getTurno(), ucDistribuicao.getHoras_atribuidas(), distribuicao_id, departamento_id,
                    ucDistribuicaoPai.getDocente() != null ? ucDistribuicaoPai.getDocente().getId() : "N/A",
                    ucDistribuicaoPai.getUcTipoAula() != null ? ucDistribuicaoPai.getUcTipoAula().getId() : "N/A");


        }
        else{

            List<UC_Juncao> juncoes = ucJuncaoRepository.findByUcDistribuicao_Id(ucDistribuicao_id);

            UC_Distribuicao ucDistribuicao = new UC_Distribuicao();
            ucDistribuicao.setTurno(ucDistribuicaoPai.getTurno());
            ucDistribuicao.setHoras_atribuidas(0);
            ucDistribuicao.setDocente(docente);
            ucDistribuicao.setUcTipoAula(null);
            ucDistribuicao.setDistribuicao(distribuicao);
            ucDistribuicao.setEstado(EstadoUcDistribuicao.ATIVO);

            ucDistribuicaoRepository.save(ucDistribuicao);

            List<UC_Juncao> novasJuncoes = new ArrayList<>();

            for(UC_Juncao ucJuncao : juncoes){

                UC_Juncao novaucJuncao = new UC_Juncao();

                novaucJuncao.setUcDistribuicao(ucDistribuicao);
                novaucJuncao.setUcTipoAula(ucJuncao.getUcTipoAula());

                novasJuncoes.add(novaucJuncao);

            }

            ucJuncaoRepository.saveAll(novasJuncoes);

            // LOG DE AUDITORIA: Criação
            auditLogger.info("Utilizador ID: {} CRIOU UC_Distribuicao ID {} Copia da UC_DistribuicaoPai: {} (Turno: {}, Horas: {}) na Distribuição ID: {} (Departamento ID: {}). Docente ID: {}, UCTipoAula ID: {}.",
                    utilizador_id, ucDistribuicao.getId(), ucDistribuicaoPai.getId(), ucDistribuicao.getTurno(), ucDistribuicao.getHoras_atribuidas(), distribuicao_id, departamento_id,
                    ucDistribuicaoPai.getDocente() != null ? ucDistribuicaoPai.getDocente().getId() : "N/A",
                    novasJuncoes.get(1).getUcTipoAula() != null ? novasJuncoes.get(1).getUcTipoAula().getId() : "N/A");


        }

    }

    @Transactional
    public void CriarUcDistribuicaoTurno(Integer ucDistribuicao_id, Integer distribuicao_id, Integer departamento_id, Integer utilizador_id) {

        // Verificar se existe a distribuição, docente e uc_tipo_aula
        Distribuicao distribuicao = utilsServices.getDistribuicaoComAcesso(distribuicao_id, departamento_id);

       List<UC_Distribuicao> ucDistribuicoes = ucDistribuicaoRepository.findAllByUcDistribuicaPai_Id(ucDistribuicao_id);

       if(!ucDistribuicoes.isEmpty()){
           throw new IllegalStateException("Esta UC Distribuição já possui um turno registado.");
       }

        UC_Distribuicao ucDistribuicaoPai = ucDistribuicaoRepository.findById(ucDistribuicao_id)
                .orElseThrow(() -> new RuntimeException("UcDistribuicao não encontrada"));

        Docente docente = null;
        if(ucDistribuicaoPai.getDocente() != null){
            docente = docenteRepository.findById(ucDistribuicaoPai.getDocente().getId())
                    .orElseThrow(() -> new RuntimeException("Docente não encontrado"));
        }


        if(ucDistribuicaoPai.getUcTipoAula() != null){

           UC_Tipo_Aula ucTipoAula = ucTipoAulaRepository.findById(ucDistribuicaoPai.getUcTipoAula().getId())
                    .orElseThrow(() -> new RuntimeException("UC tipo aula não encontrada"));

            UC_Distribuicao ucDistribuicao = new UC_Distribuicao();
            ucDistribuicao.setTurno(ucDistribuicaoPai.getTurno() + 1);
            ucDistribuicao.setHoras_atribuidas(ucDistribuicaoPai.getHoras_atribuidas());
            ucDistribuicao.setDocente(docente);
            ucDistribuicao.setUcTipoAula(ucTipoAula);
            ucDistribuicao.setDistribuicao(distribuicao);
            ucDistribuicao.setEstado(EstadoUcDistribuicao.ATIVO);
            ucDistribuicao.setUcDistribuicaPai(ucDistribuicaoPai);

            ucDistribuicaoRepository.save(ucDistribuicao);

            // LOG DE AUDITORIA: Criação
            auditLogger.info("Utilizador ID: {} CRIOU UC_Distribuicao ID {} Turno da UC_DistribuicaoPai: {} (Turno: {}, Horas: {}) na Distribuição ID: {} (Departamento ID: {}). Docente ID: {}, UCTipoAula ID: {}.",
                    utilizador_id, ucDistribuicao.getId(), ucDistribuicao.getUcDistribuicaPai().getId(), ucDistribuicao.getTurno(), ucDistribuicao.getHoras_atribuidas(), distribuicao_id, departamento_id,
                    ucDistribuicaoPai.getDocente() != null ? ucDistribuicaoPai.getDocente().getId() : "N/A",
                    ucDistribuicaoPai.getUcTipoAula() != null ? ucDistribuicaoPai.getUcTipoAula().getId() : "N/A");


        }
        else{

            List<UC_Juncao> juncoes = ucJuncaoRepository.findByUcDistribuicao_Id(ucDistribuicaoPai.getId());

            UC_Distribuicao ucDistribuicao = new UC_Distribuicao();
            ucDistribuicao.setTurno(ucDistribuicaoPai.getTurno() + 1);
            ucDistribuicao.setHoras_atribuidas(ucDistribuicaoPai.getHoras_atribuidas());
            ucDistribuicao.setDocente(docente);
            ucDistribuicao.setUcTipoAula(null);
            ucDistribuicao.setDistribuicao(distribuicao);
            ucDistribuicao.setEstado(EstadoUcDistribuicao.ATIVO);
            ucDistribuicao.setUcDistribuicaPai(ucDistribuicaoPai);

            ucDistribuicaoRepository.save(ucDistribuicao);

            List<UC_Juncao> novasJuncoes = new ArrayList<>();

            for(UC_Juncao ucJuncao : juncoes){

                UC_Juncao novaucJuncao = new UC_Juncao();

                novaucJuncao.setUcDistribuicao(ucDistribuicao);
                novaucJuncao.setUcTipoAula(ucJuncao.getUcTipoAula());

                novasJuncoes.add(novaucJuncao);

            }

            ucJuncaoRepository.saveAll(novasJuncoes);

            // LOG DE AUDITORIA: Criação
            auditLogger.info("Utilizador ID: {} CRIOU UC_Distribuicao ID {} Turno da UC_DistribuicaoPai: {} (Turno: {}, Horas: {}) na Distribuição ID: {} (Departamento ID: {}). Docente ID: {}, UCTipoAula ID: {}.",
                    utilizador_id, ucDistribuicao.getId(), ucDistribuicao.getUcDistribuicaPai().getId(), ucDistribuicao.getTurno(), ucDistribuicao.getHoras_atribuidas(), distribuicao_id, departamento_id,
                    ucDistribuicaoPai.getDocente() != null ? ucDistribuicaoPai.getDocente().getId() : "N/A",
                    novasJuncoes.get(1).getUcTipoAula() != null ? novasJuncoes.get(1).getUcTipoAula().getId() : "N/A");

        }

    }

    @Transactional
    public void CriarUcJuncao(CriarUcJuncaoDto dto, Integer distribuicao_id, Integer departamento_id, Integer utilizador_id) {

        // Verificar se existe a distribuição, docente e uc_tipo_aula
        Distribuicao distribuicao = utilsServices.getDistribuicaoComAcesso(distribuicao_id, departamento_id);

        List<UC_Tipo_Aula> ucsTipoAula = ucTipoAulaRepository.findAllById(dto.ucTipoAulaIds());

        if (ucsTipoAula.size() != dto.ucTipoAulaIds().size()) {
            throw new RuntimeException("Uma ou mais UCs não existem.");
        }

        Integer semestre = ucsTipoAula.get(0).getUc().getSemestre();

        boolean mesmoSemestre = ucsTipoAula.stream()
                .allMatch(uc -> uc.getUc().getSemestre().equals(semestre));

        if (!mesmoSemestre) {
            throw new IllegalArgumentException(
                    "Só é possível criar junções entre UCs do mesmo semestre."
            );
        }

        //é criado uma nova ucdistribuicao e as junçoes
        UC_Distribuicao ucDistribuicao = new UC_Distribuicao();
        ucDistribuicao.setTurno(1);
        ucDistribuicao.setDistribuicao(distribuicao);
        ucDistribuicao.setEstado(EstadoUcDistribuicao.ATIVO);

        ucDistribuicaoRepository.save(ucDistribuicao);

        List<UC_Juncao> juncoesParaSalvar = new ArrayList<>();

        // associa os ucTipoAulas à junção
        for (UC_Tipo_Aula ucTipoAula : ucsTipoAula) {

            //apagamos as ucdistribuicoes atuais que tenham estas uc para depois ter a juncao
            ucDistribuicaoRepository.deleteByUcTipoAula_IdAndDistribuicao_Id(ucTipoAula.getId(), distribuicao_id);

            UC_Juncao ucJuncao = new UC_Juncao();
            ucJuncao.setUcDistribuicao(ucDistribuicao);
            ucJuncao.setUcTipoAula(ucTipoAula);

            juncoesParaSalvar.add(ucJuncao);

            // LOG DE AUDITORIA: Junção
            auditLogger.info("Utilizador ID: {} CRIOU UC_Juncao ID : {} ligando UC_Distribuicao ID: {} à UC_Tipo_Aula ID: {} na Distribuição ID: {} (Departamento ID: {}).",
                    utilizador_id, ucJuncao.getId(), ucDistribuicao.getId(), ucTipoAula.getId(), distribuicao_id, departamento_id);
        }

        ucJuncaoRepository.saveAll(juncoesParaSalvar);

    }

    @Transactional
    public void DeleteUcJuncao(Integer distribuicao_id, Integer ucDistribuicao_id, Integer departamento_id, Integer utilizador_id){

        //verifica se a distribuicao e o utilizador têm o mesmo departamento
        Distribuicao distribuicao= utilsServices.getDistribuicaoComAcesso(distribuicao_id, departamento_id);

        List<UC_Juncao> juncoes = ucJuncaoRepository.findByUcDistribuicao_Id(ucDistribuicao_id);

        List<UC_Distribuicao> ucDistribuicoes = new ArrayList<>();

        for(UC_Juncao ucJuncao : juncoes){

            UC_Distribuicao ucDistribuicao = new UC_Distribuicao();

            ucDistribuicao.setHoras_atribuidas(0);
            ucDistribuicao.setTurno(1);
            ucDistribuicao.setUcTipoAula(ucJuncao.getUcTipoAula());
            ucDistribuicao.setDistribuicao(distribuicao);
            ucDistribuicao.setEstado(EstadoUcDistribuicao.ATIVO);

            ucDistribuicoes.add(ucDistribuicao);

            ucJuncaoRepository.deleteById(ucJuncao.getId());
            // LOG DE AUDITORIA: Apagar Junção
            auditLogger.info("Utilizador ID: {} APAGOU UC_Juncao ID : {} ligando UC_Distribuicao ID: {} à UC_Tipo_Aula ID: {} na Distribuição ID: {} (Departamento ID: {}).",
                    utilizador_id, ucJuncao.getId(), ucDistribuicao.getId(), ucJuncao.getUcTipoAula().getId(), distribuicao_id, departamento_id);
        }

        ucDistribuicaoRepository.deleteById(ucDistribuicao_id);

        ucDistribuicaoRepository.saveAll(ucDistribuicoes);

    }

    @Transactional
    public void DeleteUcDistribuicao(Integer distribuicao_id, Integer ucDistribuicao_id, Integer departamento_id, Integer utilizador_id) {

        //verifica se a distribuicao e o utilizador têm o mesmo departamento
        utilsServices.validarAcessoDistribuicao(distribuicao_id, departamento_id);

        UC_Distribuicao uc_distribuicao = ucDistribuicaoRepository.findById(ucDistribuicao_id)
                .orElseThrow(() -> new RuntimeException("Uc Distribuicao não encontrado"));

        if(uc_distribuicao.getUcTipoAula() == null){

            List<UC_Juncao> juncoes = ucJuncaoRepository.findByUcDistribuicao_Id(ucDistribuicao_id);

            for(UC_Juncao ucJuncao : juncoes){

                ucJuncaoRepository.deleteById(ucJuncao.getId());

            }

        }

        ucDistribuicaoRepository.deleteById(ucDistribuicao_id);

        // LOG DE AUDITORIA: Remoção
        auditLogger.info("Utilizador ID: {} APAGOU UC_Distribuicao ID: {} da Distribuição ID: {} (Departamento ID: {}).",
                utilizador_id, ucDistribuicao_id, distribuicao_id, departamento_id);

    }

    @Transactional
    public void AlterarHoras(AlterarHorasUcDistribuicaoDto dto, Integer distribuicao_id, Integer ucDistribuicao_id, Integer departamento_id, Integer utilizador_id) {

        //verifica se a distribuicao e o utilizador têm o mesmo departamento
        utilsServices.validarAcessoDistribuicao(distribuicao_id, departamento_id);

        UC_Distribuicao uc_distribuicao = ucDistribuicaoRepository.findById(ucDistribuicao_id)
                .orElseThrow(() -> new EntityNotFoundException("UC Distribuicao não encontrada"));

        //verificação caso seja turno nao poder alterar horas
        if(uc_distribuicao.getUcDistribuicaPai() != null) return;


        if(uc_distribuicao.getUcTipoAula() == null){

            UC_Juncao juncao = ucJuncaoRepository.findFirstByUcDistribuicao_Id(ucDistribuicao_id);

            // apenas as ucDistribuicoes sem turnos podem alterar horas por isso o 1 hardcoded
            List<UC_Distribuicao> ucDistribuicoes = ucDistribuicaoRepository.findAllByUcTipoAulaIsNullAndTurnoAndDistribuicao_Id(1, distribuicao_id);

            float horasTotais=0;

            for(UC_Distribuicao ucDistribuicao : ucDistribuicoes){

                UC_Juncao juncao2 = ucJuncaoRepository.findFirstByUcDistribuicao_Id(ucDistribuicao.getId());

                if (Objects.equals(juncao.getUcTipoAula().getId(), juncao2.getUcTipoAula().getId()) && !ucDistribuicao.getId().equals(uc_distribuicao.getId())) {
                    horasTotais += ucDistribuicao.getHoras_atribuidas();
                }
            }
            float novoHorasTotais = horasTotais + dto.horasAtribuidas();

            // 3. Validação dos Limites (Min e Max)
            if (dto.horasAtribuidas() < 0 || novoHorasTotais > juncao.getUcTipoAula().getHoras()) {
                throw new IllegalArgumentException(
                        String.format("Número de horas inválido (%.1f). Deve estar entre 0 e %.1f.", novoHorasTotais, juncao.getUcTipoAula().getHoras())
                );
            }

        }else{

            // apenas as ucDistribuicoes sem turnos podem alterar horas por isso o 1 hardcoded
            List<UC_Distribuicao> ucDistribuicoes = ucDistribuicaoRepository.findAllByUcTipoAula_IdAndTurnoAndDistribuicao_Id(uc_distribuicao.getUcTipoAula().getId(), 1, distribuicao_id);

            float horasTotais=0;

            for(UC_Distribuicao ucDistribuicao : ucDistribuicoes){
                if (uc_distribuicao.getId() == null || !ucDistribuicao.getId().equals(uc_distribuicao.getId())) {
                    horasTotais += ucDistribuicao.getHoras_atribuidas();
                }
            }

            float novoHorasTotais = horasTotais + dto.horasAtribuidas();
            // 3. Validação dos Limites (Min e Max)
            if (dto.horasAtribuidas() < 0 || novoHorasTotais > uc_distribuicao.getUcTipoAula().getHoras()) {
                throw new IllegalArgumentException(
                        String.format("Número totais de horas da uc inválido (%.1f). Deve estar entre 0 e %.1f.", novoHorasTotais, uc_distribuicao.getUcTipoAula().getHoras())
                );
            }
        }

        uc_distribuicao.setHoras_atribuidas(dto.horasAtribuidas());

        utilsServices.atualizarHorasTurnos(uc_distribuicao.getId(), dto.horasAtribuidas());

        // LOG DE AUDITORIA: Alteração
        auditLogger.info("Utilizador ID: {} ALTEROU UC_Distribuicao ID: {} na Distribuição ID: {} (Departamento ID: {}). Horas: -> {}.",
                utilizador_id, ucDistribuicao_id, distribuicao_id, departamento_id, dto.horasAtribuidas());
    }

    @Transactional
    public void atribuirDocente(AtribuirDocenteDto dto, Integer distribuicao_id, Integer ucDistribuicao_id, Integer departamento_id, Integer utilizador_id) {

        //verifica se a distribuicao e o utilizador têm o mesmo departamento
        utilsServices.validarAcessoDistribuicao(distribuicao_id, departamento_id);

        UC_Distribuicao uc_distribuicao = ucDistribuicaoRepository.findById(ucDistribuicao_id)
                .orElseThrow(() -> new EntityNotFoundException("UC Distribuicao não encontrada"));

        Docente docente = docenteRepository.findById(dto.docenteId())
                .orElseThrow(() -> new EntityNotFoundException("Docente não encontrado"));

        uc_distribuicao.setDocente(docente);


        // LOG DE AUDITORIA: Alteração
        auditLogger.info("Utilizador ID: {} ALTEROU UC_Distribuicao ID: {} na Distribuição ID: {} (Departamento ID: {}). Docente: -> {}.",
                utilizador_id, ucDistribuicao_id, distribuicao_id, departamento_id, dto.docenteId());

    }

    @Transactional
    public void retirarDocente(Integer distribuicao_id, Integer ucDistribuicao_id, Integer departamento_id, Integer utilizador_id) {

        //verifica se a distribuicao e o utilizador têm o mesmo departamento
        utilsServices.validarAcessoDistribuicao(distribuicao_id, departamento_id);

        UC_Distribuicao uc_distribuicao = ucDistribuicaoRepository.findById(ucDistribuicao_id)
                .orElseThrow(() -> new EntityNotFoundException("UC Distribuicao não encontrada"));

        uc_distribuicao.setDocente(null);

        // LOG DE AUDITORIA: Alteração
        auditLogger.info("Utilizador ID: {} ALTEROU UC_Distribuicao ID: {} na Distribuição ID: {} (Departamento ID: {}). Docente: -> null.",
                utilizador_id, ucDistribuicao_id, distribuicao_id, departamento_id);

    }

    public VerificacaoUcDistribuicaoDto verificarDistribuicao(Integer distribuicao_id, Integer departamento_id){

        //verifica se a distribuicao e o utilizador têm o mesmo departamento
        utilsServices.validarAcessoDistribuicao(distribuicao_id, departamento_id);

        List<String> problemas = new ArrayList<>();

        List<UC_Distribuicao> ucDistribuicoesSemDocente = ucDistribuicaoRepository.findByDistribuicao_IdAndDocenteIsNull(distribuicao_id);

        List<UC_Distribuicao> ucDistribuicoes = ucDistribuicaoRepository.findByDistribuicao_IdAndUcDistribuicaPaiIsNull(distribuicao_id);

        int ucsSemDocente = 0;
        int ucsSemHoras = 0;

        for (UC_Distribuicao uc : ucDistribuicoesSemDocente) {
            String nome = utilsServices.resolverNomeUc(uc);

            if (uc.getDocente() == null) {
                ucsSemDocente++;
                problemas.add("Sem docente: " + nome + " (Turno " + uc.getTurno() + ")");
            }

        }

        for(UC_Distribuicao uc : ucDistribuicoes){

            if(uc.getUcTipoAula()!=null){
                // apenas as ucDistribuicoes sem turnos podem alterar horas por isso o 1 hardcoded
                List<UC_Distribuicao> ucD = ucDistribuicaoRepository.findAllByUcTipoAula_IdAndTurnoAndDistribuicao_Id(uc.getUcTipoAula().getId(), 1, distribuicao_id);

                float horasTotais=0;

                for(UC_Distribuicao ucDistribuicao : ucD){
                    horasTotais += ucDistribuicao.getHoras_atribuidas();
                }

                String nome = utilsServices.resolverNomeUc(uc);
                if (horasTotais < uc.getUcTipoAula().getHoras()) {
                    ucsSemHoras++;
                    problemas.add("Horas insuficientes atribuídas: " + nome + " (Turno " + uc.getTurno() + ")");
                }
            }
            else{
                //essencialmente temos o primeiro array que passa por todas as ucDistribuicoes onde apanhamos a primeira junção e em seguida comparamos a todas as primerias junções dos ouitros ucDistribuicoes
                UC_Juncao juncao = ucJuncaoRepository.findFirstByUcDistribuicao_Id(uc.getId());

                // apenas as ucDistribuicoes sem turnos podem alterar horas por isso o 1 hardcoded
                List<UC_Distribuicao> ucDistribuicoes2 = ucDistribuicaoRepository.findAllByUcTipoAulaIsNullAndTurnoAndDistribuicao_Id(1, distribuicao_id);

                float horasTotais=0;

                for(UC_Distribuicao ucDistribuicao : ucDistribuicoes2){

                    UC_Juncao juncao2 = ucJuncaoRepository.findFirstByUcDistribuicao_Id(ucDistribuicao.getId());

                    if (Objects.equals(juncao.getUcTipoAula().getId(), juncao2.getUcTipoAula().getId())) {
                        horasTotais += ucDistribuicao.getHoras_atribuidas();
                    }
                }

                String nome = utilsServices.resolverNomeUc(uc);
                if (horasTotais < juncao.getUcTipoAula().getHoras()) {
                    ucsSemHoras++;
                    problemas.add("Horas insuficientes atribuídas: " + nome + " (Turno " + uc.getTurno() + ")");
                }
            }



        }

        return new VerificacaoUcDistribuicaoDto(
                problemas.isEmpty(),
                problemas,
                ucsSemDocente,
                ucsSemHoras
        );
    }

    /*@Transactional
    public List<ListarUcDistribuicoesDto> getUcDistribuicoes2(Integer distribuicao_id) {

        // 1. carrega tudo de uma vez
        List<UC_Distribuicao> todas = ucDistribuicaoRepository.findByDistribuicao_Id(distribuicao_id);

        List<Integer> ids = todas.stream().map(UC_Distribuicao::getId).toList();

        // juncoes agrupadas por ucDistribuicaoId
        Map<Integer, List<UC_Juncao>> juncoesPorUcDist = ucJuncaoRepository
                .findByUcDistribuicao_IdIn(ids)
                .stream()
                .collect(Collectors.groupingBy(j -> j.getUcDistribuicao().getId()));

        // filhos agrupados por paiId
        Map<Integer, List<UC_Distribuicao>> filhosPorPai = ucDistribuicaoRepository
                .findAllByUcDistribuicaPai_IdIn(ids)
                .stream()
                .collect(Collectors.groupingBy(f -> f.getUcDistribuicaPai().getId()));

        // contratos ativos dos docentes — só os que têm docente
        Set<Integer> docenteIds = todas.stream()
                .filter(uc -> uc.getDocente() != null)
                .map(uc -> uc.getDocente().getId())
                .collect(Collectors.toSet());

        Map<Integer, ContratoDocente> contratosPorDocente = docenteIds.isEmpty()
                ? Map.of()
                : contratoDocenteRepository.findContratosAtivos(docenteIds)
                .stream()
                .collect(Collectors.toMap(
                        c -> c.getDocente().getId(),
                        c -> c,
                        (a, b) -> a // em caso de duplicado, fica o mais recente
                ));

        // todas as ucs do docente na distribuição — para calculos de horas docente
        Map<Integer, List<UC_Distribuicao>> ucsPorDocente = todas.stream()
                .filter(uc -> uc.getDocente() != null)
                .collect(Collectors.groupingBy(uc -> uc.getDocente().getId()));

        // 2. mapeia sem queries adicionais
        return todas.stream()
                .map(uc -> {
                    List<UC_Juncao> juncoes = juncoesPorUcDist.getOrDefault(uc.getId(), List.of());
                    List<UC_Distribuicao> filhos = filhosPorPai.getOrDefault(uc.getId(), List.of());
                    ContratoDocente contrato = uc.getDocente() != null
                            ? contratosPorDocente.get(uc.getDocente().getId())
                            : null;
                    List<UC_Distribuicao> ucsDocente = uc.getDocente() != null
                            ? ucsPorDocente.getOrDefault(uc.getDocente().getId(), List.of())
                            : List.of();

                    DadosUcDistribuicaoDto dados = mapearUcDistribuicoes2(uc, juncoes);
                    CalculosUcDistribuicaoDto calculos = getUcDistribuicoesCalculos(uc, dados, contrato, ucsDocente);
                    boolean temFilhos = !filhos.isEmpty();
                    boolean temDuplicado = utilsServices.temDuplicados(uc, dados);

                    return new ListarUcDistribuicoesDto(
                            uc.getId(),
                            uc.getTurno(),
                            uc.getHoras_atribuidas(),
                            Optional.ofNullable(uc.getUcDistribuicaPai()).map(UC_Distribuicao::getId).orElse(null),
                            temFilhos,
                            temDuplicado,
                            Optional.ofNullable(uc.getDocente()).map(Docente::getId).orElse(null),
                            Optional.ofNullable(uc.getDocente()).map(Docente::getNome).orElse(null),
                            dados.cursoId(),
                            dados.cursoNome(),
                            dados.ucDepartamentoNome(),
                            dados.ucNome(),
                            dados.ucAno(),
                            dados.ucSemestre(),
                            dados.ucTipoAulaId(),
                            dados.ucTipoNome(),
                            dados.ucTipoHoras(),
                            dados.temJuncao(),
                            calculos.nSemanas(),
                            calculos.horasSemestreS1(),
                            calculos.horasSemestreS2(),
                            calculos.horasSemestreMedia(),
                            calculos.horasSemanaS1(),
                            calculos.horasSemanaS2(),
                            calculos.horasDocenteS1(),
                            calculos.horasDocenteS2(),
                            calculos.horasDocenteMedia()
                    );
                })
                .sorted(Comparator
                        .comparing(ListarUcDistribuicoesDto::cursoNome, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER))
                        .thenComparing(ListarUcDistribuicoesDto::ucAno, Comparator.nullsLast(Comparator.naturalOrder()))
                        .thenComparing(ListarUcDistribuicoesDto::ucSemestre, Comparator.nullsLast(Comparator.naturalOrder()))
                        .thenComparing(ListarUcDistribuicoesDto::ucNome, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER))
                        .thenComparing(ListarUcDistribuicoesDto::ucTipoNome, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER))
                )
                .toList();
    }

    // mapearUcDistribuicoes recebe as juncoes já carregadas
    private DadosUcDistribuicaoDto mapearUcDistribuicoes2(UC_Distribuicao uc, List<UC_Juncao> juncoes) {
        if (uc.getUcTipoAula() != null) {
            return new DadosUcDistribuicaoDto(
                    uc.getUcTipoAula().getUc().getCurso().getId(),
                    uc.getUcTipoAula().getUc().getCurso().getNome(),
                    uc.getUcTipoAula().getUc().getDepartamento().getNome(),
                    uc.getUcTipoAula().getUc().getNome(),
                    uc.getUcTipoAula().getUc().getAno_curricular(),
                    uc.getUcTipoAula().getUc().getSemestre(),
                    uc.getUcTipoAula().getId(),
                    uc.getUcTipoAula().getTipo_aula().getNome(),
                    uc.getUcTipoAula().getHoras(),
                    false
            );
        }

        if (juncoes.isEmpty()) return null;

        var primeira = juncoes.get(0);
        String cursosDisplay = juncoes.stream().map(j -> j.getUcTipoAula().getUc().getCurso().getNome()).distinct().collect(Collectors.joining(" / "));
        String ucsDisplay = juncoes.stream().map(j -> j.getUcTipoAula().getUc().getNome()).distinct().collect(Collectors.joining(" / "));
        String departamentosDisplay = juncoes.stream().map(j -> j.getUcTipoAula().getUc().getDepartamento().getNome()).distinct().collect(Collectors.joining(" / "));
        String tipoAulaDisplay = juncoes.stream().map(j -> j.getUcTipoAula().getTipo_aula().getNome()).distinct().collect(Collectors.joining(" / "));

        return new DadosUcDistribuicaoDto(
                primeira.getUcTipoAula().getUc().getCurso().getId(),
                cursosDisplay, departamentosDisplay, ucsDisplay,
                primeira.getUcTipoAula().getUc().getAno_curricular(),
                primeira.getUcTipoAula().getUc().getSemestre(),
                primeira.getUcTipoAula().getId(),
                tipoAulaDisplay,
                primeira.getUcTipoAula().getHoras(),
                true
        );
    }

    // calculos recebe contrato e ucsDocente já carregados
    private CalculosUcDistribuicaoDto getUcDistribuicoesCalculos(
            UC_Distribuicao uc,
            DadosUcDistribuicaoDto dados,
            ContratoDocente contrato,
            List<UC_Distribuicao> ucsDocente) {

        Integer nSemanas = 16;
        float horasSemestreS1 = 0, horasSemestreS2 = 0, horasDocenteS1 = 0, horasDocenteS2 = 0;

        if (uc.getDocente() == null) {
            return new CalculosUcDistribuicaoDto(nSemanas, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);
        }

        if (contrato != null && contrato.getDedicacao() == DedicacaoDocente.TEMPO_PARCIAL) {
            nSemanas = 20;
        }

        Integer semestreUC = dados != null ? dados.ucSemestre() : null;
        float horasAtribuidas = uc.getHoras_atribuidas();

        if (uc.getUcDistribuicaPai() == null) {
            if (Integer.valueOf(1).equals(semestreUC)) horasSemestreS1 = horasAtribuidas;
            else if (Integer.valueOf(2).equals(semestreUC)) horasSemestreS2 = horasAtribuidas;
        }

        // usa ucsDocente já carregadas — sem query adicional
        for (UC_Distribuicao ucD : ucsDocente) {

            Integer sem = utilsServices.resolverSemestre(ucD);
            if (Integer.valueOf(1).equals(sem)) horasDocenteS1 += ucD.getHoras_atribuidas();
            else if (Integer.valueOf(2).equals(sem)) horasDocenteS2 += ucD.getHoras_atribuidas();
        }

        float horasSemestreMedia = (horasSemestreS1 + horasSemestreS2) / 2;
        float horasSemanaS1 = horasSemestreS1 / nSemanas;
        float horasSemanaS2 = horasSemestreS2 / nSemanas;
        horasDocenteS1 = horasDocenteS1 / nSemanas;
        horasDocenteS2 = horasDocenteS2 / nSemanas;
        float horasDocenteMedia = (horasDocenteS1 + horasDocenteS2) / 2;

        return new CalculosUcDistribuicaoDto(
                nSemanas,
                Math.round(horasSemestreS1 * 10f) / 10f,
                Math.round(horasSemestreS2 * 10f) / 10f,
                Math.round(horasSemestreMedia * 10f) / 10f,
                Math.round(horasSemanaS1 * 10f) / 10f,
                Math.round(horasSemanaS2 * 10f) / 10f,
                Math.round(horasDocenteS1 * 10f) / 10f,
                Math.round(horasDocenteS2 * 10f) / 10f,
                Math.round(horasDocenteMedia * 10f) / 10f
        );
    }*/


}
