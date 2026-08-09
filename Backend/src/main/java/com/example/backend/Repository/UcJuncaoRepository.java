package com.example.backend.Repository;

import com.example.backend.Entity.UC_Distribuicao;
import com.example.backend.Entity.UC_Juncao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UcJuncaoRepository extends JpaRepository<UC_Juncao,Integer> {

    List<UC_Juncao> findByUcDistribuicao_Id(Integer id);

    UC_Juncao findFirstByUcDistribuicao_Id(Integer id);

    long countByUcTipoAula_IdAndUcDistribuicao_Distribuicao_Id(Integer integer, Integer id);

    List<UC_Juncao> findByUcDistribuicao_IdIn(List<Integer> ids);
}
