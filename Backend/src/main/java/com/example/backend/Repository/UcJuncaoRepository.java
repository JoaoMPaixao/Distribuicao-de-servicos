package com.example.backend.Repository;

import com.example.backend.Entity.UC_Distribuicao;
import com.example.backend.Entity.UC_Juncao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UcJuncaoRepository extends JpaRepository<UC_Juncao,Integer> {
}
