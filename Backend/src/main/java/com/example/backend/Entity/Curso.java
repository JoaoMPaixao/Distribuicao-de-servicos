package com.example.backend.Entity;


import com.example.backend.Enums.EstadoCurso;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name= "curso")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;

    @Enumerated(EnumType.STRING)
    private EstadoCurso estado;

    private String codigo_uc;

    @ManyToOne
    @JoinColumn(name = "departamento_id")
    private Departamento departamento;

}
