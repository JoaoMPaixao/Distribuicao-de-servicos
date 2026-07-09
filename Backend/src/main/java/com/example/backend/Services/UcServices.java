package com.example.backend.Services;

import com.example.backend.Dto.Cursos.ListarCursosDto;
import com.example.backend.Dto.Ucs.ListarUcsDto;
import com.example.backend.Repository.DistribuicaoRepository;
import com.example.backend.Repository.UcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UcServices {

    @Autowired
    private UcRepository  ucRepository;

    @Autowired
    private DistribuicaoRepository distribuicaoRepository;

    public List<ListarUcsDto> getUcs (){

        return ucRepository.findAll()
                .stream()
                .map(uc -> new ListarUcsDto(
                        uc.getId(),
                        uc.getNome(),
                        uc.getCodigo_uc(),
                        uc.getSemestre(),
                        uc.getAno_curricular(),
                        uc.getEtcs(),
                        uc.getHoras(),
                        uc.getEstado(),
                        uc.getDepartamento().getId(),
                        uc.getCurso().getId()

                ))
                .toList();


    }

}
