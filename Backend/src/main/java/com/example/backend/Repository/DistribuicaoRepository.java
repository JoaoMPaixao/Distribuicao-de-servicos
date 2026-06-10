package com.example.backend.Repository;

import com.example.backend.Entity.Distribuicao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface DistribuicaoRepository extends JpaRepository<Distribuicao,Integer> {

    Set<Distribuicao> findDistribuicaoBydepartamento_id(Integer departamento_id);

}
