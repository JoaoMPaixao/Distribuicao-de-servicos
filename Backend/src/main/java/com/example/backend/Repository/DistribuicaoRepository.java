package com.example.backend.Repository;

import com.example.backend.Entity.Distribuicao;
import com.example.backend.Enums.EstadoDistribuicao;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface DistribuicaoRepository extends JpaRepository<Distribuicao,Integer> {

    List<Distribuicao> findByDepartamento_IdAndEstadoNot(Integer departamento_id, EstadoDistribuicao estado);


    boolean existsByIdAndDepartamentoId(Integer distribuicao_id, Integer departamento_id);

    Optional<Distribuicao> findByIdAndEstadoNot(Integer id,EstadoDistribuicao estadoDistribuicao);
}