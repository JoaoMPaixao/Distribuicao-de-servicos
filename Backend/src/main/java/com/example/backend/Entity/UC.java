package com.example.backend.Entity;


import com.example.backend.Enums.EstadoUc;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name= "uc")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UC {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;

    private String codigo_uc;

    private Integer semestre;

    private Integer ano_curricular;

    private Float etcs;

    private float horas;

    @Enumerated(EnumType.STRING)
    private EstadoUc estado;

    @ManyToOne
    @JoinColumn(name = "departamento_id")
    private Departamento departamento;

    @ManyToOne
    @JoinColumn(name = "curso_id")
    private Curso curso;




}
