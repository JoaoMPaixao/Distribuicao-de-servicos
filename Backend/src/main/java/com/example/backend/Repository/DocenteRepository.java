package com.example.backend.Repository;

import com.example.backend.Entity.Distribuicao;
import com.example.backend.Entity.Docente;
import com.example.backend.Enums.EstadoDistribuicao;
import com.example.backend.Enums.EstadoDocente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface DocenteRepository extends JpaRepository<Docente,Integer> {

    List<Docente> findByDepartamento_IdAndEstadoNot(Integer departamento_id, EstadoDocente estado);

}
