package com.example.backend.Entity;


import  com.example.backend.Enums.EstadoUcDistribuicao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name= "uc_distribuicao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UC_Distribuicao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Float horas_atribuidas;

    @Enumerated(EnumType.STRING)
    private EstadoUcDistribuicao estado;

    private String turno;

    @ManyToOne
    @JoinColumn(name = "uc_distribuicao_pai_id")
    private UC_Distribuicao ucDistribuicaPai;

    @ManyToOne
    @JoinColumn(name = "distribuicao_id")
    private Distribuicao distribuicao;

    @ManyToOne
    @JoinColumn(name = "docente_id")
    private Docente docente;

    @ManyToOne(optional = true)
    @JoinColumn(name = "uc_tipo_aula_id", nullable = true)
    private UC_Tipo_Aula ucTipoAula;

}
