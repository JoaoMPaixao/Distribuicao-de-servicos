package com.example.backend.Repository;

import com.example.backend.Dto.Ucs.ListarUcsDto;
import com.example.backend.Entity.Docente;
import com.example.backend.Entity.UC;
import com.example.backend.Enums.EstadoCurso;
import com.example.backend.Enums.EstadoDocente;
import com.example.backend.Enums.EstadoUc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface UcRepository extends JpaRepository<UC,Integer> {

}