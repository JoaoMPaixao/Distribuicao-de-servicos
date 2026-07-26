package com.example.backend.Repository;

import com.example.backend.Entity.ContratoDocente;
import com.example.backend.Entity.Curso;
import com.example.backend.Enums.EstadoCurso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContratoDocenteRepository extends JpaRepository<ContratoDocente,Integer> {


    List<ContratoDocente> findByDocente_Id(Integer docenteId);
}
