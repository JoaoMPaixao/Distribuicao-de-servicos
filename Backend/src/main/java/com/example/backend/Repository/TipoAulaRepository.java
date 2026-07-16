package com.example.backend.Repository;

import com.example.backend.Entity.Tipo_Aula;

import com.example.backend.Enums.EstadoUc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TipoAulaRepository extends JpaRepository<Tipo_Aula,Integer> {


}
