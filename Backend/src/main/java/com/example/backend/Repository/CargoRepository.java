package com.example.backend.Repository;

import com.example.backend.Entity.Cargo;
import com.example.backend.Entity.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CargoRepository extends JpaRepository<Cargo,Integer> {
}
