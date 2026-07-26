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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;


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

            ucDistribuicao.setHoras_atribuidas(null);
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



                    return new ListarUcDistribuicoesDto(
                            ucDistribuicao.getId(),
                            ucDistribuicao.getTurno(),
                            ucDistribuicao.getHoras_atribuidas(),
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
                .sorted(Comparator.comparing(
                        ListarUcDistribuicoesDto::ucNome,
                        String.CASE_INSENSITIVE_ORDER
                ))
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
                    ucDistribuicao.getUcTipoAula().getHoras()

            );
        }
        else{
            // linha de junção — vai buscar o primeiro ucTipoAula da junção
            return ucJuncaoRepository.findByUcDistribuicao_Id(ucDistribuicao.getId())
                    .stream()
                    .findFirst()
                    .map(ucJuncao -> new DadosUcDistribuicaoDto(
                            ucJuncao.getUcTipoAula().getUc().getCurso().getId(),
                            ucJuncao.getUcTipoAula().getUc().getCurso().getNome(),
                            ucJuncao.getUcTipoAula().getUc().getDepartamento().getNome(),
                            ucJuncao.getUcTipoAula().getUc().getNome(),
                            ucJuncao.getUcTipoAula().getUc().getAno_curricular(),
                            ucJuncao.getUcTipoAula().getUc().getSemestre(),
                            ucJuncao.getUcTipoAula().getId(),
                            ucJuncao.getUcTipoAula().getTipo_aula().getNome(),
                            ucJuncao.getUcTipoAula().getHoras()
                    ))
                    .orElse(null);

        }

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
        String semestreUC = dados.ucSemestre();
        float horasAtribuidas = ucDistribuicao.getHoras_atribuidas() != null ? ucDistribuicao.getHoras_atribuidas() : 0f;

        if ("1".equalsIgnoreCase(semestreUC)) {
            horasSemestreS1 = horasAtribuidas;
        } else if ("2".equalsIgnoreCase(semestreUC)) {
            horasSemestreS2 = horasAtribuidas;
        }

        // 3. Busca todas as UCs atribuídas ao mesmo docente para calcular totais do docente
        List<UC_Distribuicao> ucDistribuicoes = ucDistribuicaoRepository.findByDocente_IdAndDistribuicao_Id(ucDistribuicao.getDocente().getId(), ucDistribuicao.getDistribuicao().getId());

        for (UC_Distribuicao UcD : ucDistribuicoes) {
            if (UcD.getHoras_atribuidas() != null) {
                horasSemestreMedia += UcD.getHoras_atribuidas();

                String sem = utilsServices.resolverSemestre(UcD);
                if ("1".equalsIgnoreCase(sem)) {
                    horasDocenteS1 += UcD.getHoras_atribuidas();
                } else if ("2".equalsIgnoreCase(sem)) {
                    horasDocenteS2 += UcD.getHoras_atribuidas();
                }
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


        UC_Tipo_Aula ucTipoAula = null;
        if (ucDistribuicaoPai.getUcTipoAula() != null) {
            ucTipoAula = ucTipoAulaRepository.findById(ucDistribuicaoPai.getUcTipoAula().getId())
                    .orElseThrow(() -> new RuntimeException("UC tipo aula não encontrada"));
        }

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
                ucDistribuicaoPai.getDocente() != null ? ucDistribuicaoPai.getDocente() : "N/A",
                ucDistribuicaoPai.getUcTipoAula() != null ? ucDistribuicaoPai.getUcTipoAula() : "N/A");

    }

    @Transactional
    public void CriarUcJuncao(CriarUcJuncaoDto dto, Integer distribuicao_id, Integer departamento_id, Integer utilizador_id) {

        // Verificar se existe a distribuição, docente e uc_tipo_aula
        Distribuicao distribuicao = utilsServices.getDistribuicaoComAcesso(distribuicao_id, departamento_id);

        List<UC_Tipo_Aula> ucsTipoAula = ucTipoAulaRepository.findAllById(dto.ucTipoAulaIds());

        if (ucsTipoAula.size() != dto.ucTipoAulaIds().size()) {
            throw new RuntimeException("Uma ou mais UCs não existem.");
        }

        String semestre = ucsTipoAula.get(0).getUc().getSemestre();

        boolean mesmoSemestre = ucsTipoAula.stream()
                .allMatch(uc -> uc.getUc().getSemestre().equals(semestre));

        if (!mesmoSemestre) {
            throw new RuntimeException("Só é possível criar junções entre UCs do mesmo semestre.");
        }

        //é criado uma nova ucdistribuicao e as junçoes
        UC_Distribuicao ucDistribuicao = new UC_Distribuicao();
        ucDistribuicao.setTurno(1);
        ucDistribuicao.setDistribuicao(distribuicao);
        ucDistribuicao.setEstado(EstadoUcDistribuicao.ATIVO);

        ucDistribuicaoRepository.save(ucDistribuicao);
        System.out.println(dto.ucTipoAulaIds());
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

        ucDistribuicaoRepository.deleteById(ucDistribuicao_id);

        List<UC_Juncao> juncoes = ucJuncaoRepository.findByUcDistribuicao_Id(distribuicao_id);

        List<UC_Distribuicao> ucDistribuicoes = new ArrayList<>();

        for(UC_Juncao ucJuncao : juncoes){

            UC_Distribuicao ucDistribuicao = new UC_Distribuicao();

            ucDistribuicao.setHoras_atribuidas(null);
            ucDistribuicao.setTurno(1);
            ucDistribuicao.setUcTipoAula(ucJuncao.getUcTipoAula());
            ucDistribuicao.setDistribuicao(distribuicao);
            ucDistribuicao.setEstado(EstadoUcDistribuicao.ATIVO);

            ucDistribuicoes.add(ucDistribuicao);

            ucJuncaoRepository.deleteById(ucJuncao.getId());
        }

        ucDistribuicaoRepository.saveAll(ucDistribuicoes);

    }

    @Transactional
    public void DeleteUcDistribuicao(Integer distribuicao_id, Integer ucDistribuicao_id, Integer departamento_id, Integer utilizador_id) {

        //verifica se a distribuicao e o utilizador têm o mesmo departamento
        utilsServices.validarAcessoDistribuicao(distribuicao_id, departamento_id);

        // valida se a ucDistribuicao existe antes de tentar apagar
        if (!ucDistribuicaoRepository.existsById(ucDistribuicao_id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Atribuição não encontrada");
        }


        System.out.println(ucDistribuicao_id);
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

        // 3. Validação dos Limites (Min e Max)
        if (dto.horasAtribuidas() < 0 || dto.horasAtribuidas() > uc_distribuicao.getUcTipoAula().getHoras()) {
            throw new IllegalArgumentException(
                    String.format("Número de horas inválido (%.1f). Deve estar entre 0 e %.1f.", dto.horasAtribuidas(), uc_distribuicao.getUcTipoAula().getHoras())
            );
        }

        uc_distribuicao.setHoras_atribuidas(dto.horasAtribuidas());

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


}
