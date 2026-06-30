package com.example.backend.Repository;

import com.example.backend.Entity.Curso;
import com.example.backend.Entity.UC;
import com.example.backend.Enums.EstadoCurso;
import com.example.backend.Enums.EstadoUc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CursoRepository extends JpaRepository<Curso,Integer> {

    List<Curso> findByDepartamento_IdAndEstadoNot(Integer departamento_id, EstadoCurso estado);

}
