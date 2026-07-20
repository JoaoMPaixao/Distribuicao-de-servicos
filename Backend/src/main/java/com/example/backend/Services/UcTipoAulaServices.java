package com.example.backend.Services;

import com.example.backend.Dto.UcTipoAula.AlterarUcTipoAulaDto;
import com.example.backend.Dto.UcTipoAula.CriarUcTipoAulaDto;
import com.example.backend.Dto.UcTipoAula.ListarUcTipoAulaDto;
import com.example.backend.Entity.*;
import com.example.backend.Repository.TipoAulaRepository;
import com.example.backend.Repository.UcRepository;
import com.example.backend.Repository.UcTipoAulaRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UcTipoAulaServices {

    // Logger a apontar para o ficheiro estabelecido no xml
    private static final Logger auditLogger = LoggerFactory.getLogger("auditoria");

    @Autowired
    private UcTipoAulaRepository ucTipoAulaRepository;

    @Autowired
    private UcRepository ucRepository;

    @Autowired
    private TipoAulaRepository tipoAulaRepository;

    public List<ListarUcTipoAulaDto> getUcTipoAulas (){

        return ucTipoAulaRepository.findAll()
                .stream()
                .map(ucTipoAula -> new ListarUcTipoAulaDto(
                        ucTipoAula.getId(),
                        ucTipoAula.getHoras(),
                        ucTipoAula.getUc().getId(),
                        ucTipoAula.getUc().getNome(),
                        ucTipoAula.getTipo_aula().getId(),
                        ucTipoAula.getTipo_aula().getNome()

                ))
                .toList();

    }

    public List<ListarUcTipoAulaDto> getUcTipoAulasByUc (Integer id){

        return ucTipoAulaRepository.findByUc_Id(id)
                .stream()
                .map(ucTipoAula -> new ListarUcTipoAulaDto(
                        ucTipoAula.getId(),
                        ucTipoAula.getHoras(),
                        ucTipoAula.getUc().getId(),
                        ucTipoAula.getUc().getNome(),
                        ucTipoAula.getTipo_aula().getId(),
                        ucTipoAula.getTipo_aula().getNome()

                ))
                .toList();

    }

    @Transactional
    public void CriarUcTipoAulas(CriarUcTipoAulaDto dto, Integer utilizador_id){

        UC uc = ucRepository.findById(dto.uc_id())
                .orElseThrow(() -> new RuntimeException("Uc não encontrado"));

        Tipo_Aula tipoAula= tipoAulaRepository.findById(dto.tipo_aula_id())
                .orElseThrow(() -> new RuntimeException("Tipo aula não encontrado"));

        UC_Tipo_Aula uctipoAula = new UC_Tipo_Aula();

        uctipoAula.setHoras(dto.horas());
        uctipoAula.setUc(uc);
        uctipoAula.setTipo_aula(tipoAula);

        ucTipoAulaRepository.save(uctipoAula);

        // LOG DE AUDITORIA: Criação
        auditLogger.info("Utilizador ID: {} CRIOU o UcTipoAula ID: {} no Uc ID: {}.",
                utilizador_id, uctipoAula.getId(), uc.getId());

    }

    @Transactional
    public void AlterarUcTipoAulas (AlterarUcTipoAulaDto dto, Integer ucTipoAula_id, Integer utilizador_id){

        UC uc = ucRepository.findById(dto.uc_id())
                .orElseThrow(() -> new RuntimeException("Uc não encontrado"));

        Tipo_Aula tipoAula= tipoAulaRepository.findById(dto.tipo_aula_id())
                .orElseThrow(() -> new RuntimeException("Tipo aula não encontrado"));

        UC_Tipo_Aula uctipoAula = ucTipoAulaRepository.findById(ucTipoAula_id).orElseThrow(() -> new RuntimeException("Uc Tipo aula não encontrado"));

        uctipoAula.setHoras(dto.horas());
        uctipoAula.setUc(uc);
        uctipoAula.setTipo_aula(tipoAula);

        // LOG DE AUDITORIA: Alteração de Curso
        auditLogger.info("Utilizador ID: {} ALTEROU o UcTipoAula ID: {} para horas {} ; para TipoAula ID: {} ; para Uc ID: {}.",
                utilizador_id, ucTipoAula_id, uctipoAula.getHoras(), dto.tipo_aula_id(), dto.uc_id());

    }

    @Transactional
    public void DeleteUcTipoAulas(Integer ucTipoAula_id, Integer utilizador_id) {


        UC_Tipo_Aula uctipoAula = ucTipoAulaRepository.findById(ucTipoAula_id).orElseThrow(() -> new RuntimeException("Uc Tipo aula não encontrado"));

        ucTipoAulaRepository.delete(uctipoAula);

        // LOG DE AUDITORIA: Delete
        auditLogger.info("Utilizador ID: {} APAGOU a UcTipoAula ID: {}.",
                utilizador_id, ucTipoAula_id);

    }

}
