package com.example.backend.Repository;

import com.example.backend.Entity.Docente;
import com.example.backend.Entity.UC;
import com.example.backend.Enums.EstadoDocente;
import com.example.backend.Enums.EstadoUc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UcRepository extends JpaRepository<UC,Integer> {

    List<UC> findByDepartamento_IdAndEstadoNot(Integer departamento_id, EstadoUc estado);

    List<UC> findByCurso_IdAndEstadoNot(Integer cursoId, EstadoUc estado);

}