package com.example.backend.Repository;

import com.example.backend.Entity.UC;
import com.example.backend.Entity.UC_Distribuicao;
import com.example.backend.Entity.UC_Tipo_Aula;
import com.example.backend.Enums.EstadoUc;
import com.example.backend.Enums.EstadoUcDistribuicao;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UcDistribuicaoRepository extends JpaRepository<UC_Distribuicao,Integer> {

    List<UC_Distribuicao> findByUcTipoAula_Id(Integer Uc_tipo_aula_id);

    List<UC_Distribuicao> findByDistribuicao_Id(Integer distribuicao_id);

    List<UC_Distribuicao> findByDocente_IdAndDistribuicao_Id(Integer id, Integer id1);

    void deleteByUcTipoAula_IdAndDistribuicao_Id(Integer ucTipoAulaId, Integer distribuicaoId);

    List<UC_Distribuicao> findAllByUcDistribuicaPai_Id(Integer ucDistribuicaPaiId);

    List<UC_Distribuicao> findAllByUcTipoAula_Id(Integer id);

    long countByDistribuicao_IdAndUcTipoAula_Id(Integer id, Integer id1);

    List<UC_Distribuicao> findAllByUcTipoAula_IdAndTurnoAndDistribuicao_Id(Integer id, int i, Integer distribuicaoId);

    UC_Distribuicao findByUcDistribuicaPai_Id(Integer ucDistribucaoId);

    List<UC_Distribuicao> findByDistribuicao_IdAndDocenteIsNull(Integer distribuicaoId);

    List<UC_Distribuicao> findByDistribuicao_IdAndUcDistribuicaPaiIsNull(Integer distribuicaoId);

    List<UC_Distribuicao> findAllByUcTipoAulaIsNullAndTurnoAndDistribuicao_Id(Integer i, Integer distribuicaoId);

    List<UC_Distribuicao> findAllByUcDistribuicaPai_IdIn(List<Integer> ids);
}