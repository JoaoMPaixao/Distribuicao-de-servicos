package com.example.backend.Services;

import com.example.backend.Dto.ListarDistribuicaoDto;
import com.example.backend.Entity.Distribuicao;
import com.example.backend.Repository.DistribuicaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistribuicaoServices {

    @Autowired
    private DistribuicaoRepository distribuicaoRepository;

    public List<ListarDistribuicaoDto> getDistribuicaoByDepartamentoId(int id){
        return
     distribuicaoRepository.findDistribuicaoBydepartamento_id(id);
    }

}
