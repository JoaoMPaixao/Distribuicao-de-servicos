package com.example.backend.Repository;

import com.example.backend.Entity.UC;
import com.example.backend.Entity.UC_Distribuicao;
import com.example.backend.Enums.EstadoUc;
import com.example.backend.Enums.EstadoUcDistribuicao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UcDistribuicaoRepository extends JpaRepository<UC_Distribuicao,Integer> {

    List<UC_Distribuicao> findByUcTipoAula_Id(Integer Uc_tipo_aula_id);



}