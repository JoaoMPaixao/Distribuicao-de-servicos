package com.example.backend.Repository;

import com.example.backend.Entity.ContratoDocente;
import com.example.backend.Entity.Curso;
import com.example.backend.Enums.EstadoCurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface ContratoDocenteRepository extends JpaRepository<ContratoDocente,Integer> {


    List<ContratoDocente> findByDocente_Id(Integer docenteId);

    void deleteAllByDocente_Id(Integer docenteId);

    @Query("SELECT c FROM ContratoDocente c WHERE c.docente.id IN :docenteIds AND c.estado = 'ATIVO' AND (c.dataFim IS NULL OR c.dataFim > CURRENT_DATE)")
    List<ContratoDocente> findContratosAtivos(@Param("docenteIds") Set<Integer> docenteIds);
}
