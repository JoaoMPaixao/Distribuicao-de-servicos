package com.example.backend.Repository;

import com.example.backend.Entity.Utilizador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtilizadorRepository extends JpaRepository<Utilizador,Integer> {
}
