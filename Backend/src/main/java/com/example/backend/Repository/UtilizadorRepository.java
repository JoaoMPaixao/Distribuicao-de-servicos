package com.example.backend.Repository;

import com.example.backend.Entity.Utilizador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtilizadorRepository extends JpaRepository<Utilizador,Integer> {

        Optional<Utilizador> findByEmail(String email);

}
