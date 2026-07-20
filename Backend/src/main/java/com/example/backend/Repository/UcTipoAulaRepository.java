package com.example.backend.Repository;

import com.example.backend.Entity.Tipo_Aula;
import com.example.backend.Entity.UC_Tipo_Aula;
import com.example.backend.Enums.EstadoCurso;
import com.example.backend.Enums.EstadoUc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface UcTipoAulaRepository extends JpaRepository<UC_Tipo_Aula,Integer> {



    List<UC_Tipo_Aula> findByUc_IdAndUc_EstadoNot(Integer UCId, EstadoUc estado);

    List<UC_Tipo_Aula> findByUc_Id(Integer UCId);

    List<UC_Tipo_Aula> findByUc_Curso_DepartamentoIdAndUc_EstadoNotAndUc_Curso_EstadoNot(Integer ucCursoDepartamentoId, EstadoUc ucEstado, EstadoCurso ucCursoEstado);

    void deleteAllByUc_Id(Integer ucId);
}
