package com.example.backend.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name= "tipo_docente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tipo_Docente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String tipo;

    private Integer carga_horaria_max;

    private String descricao;

}
