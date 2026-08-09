package com.example.backend.Entity;


import com.example.backend.Enums.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name= "contratoDocente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContratoDocente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDate dataInicio;

    private LocalDate dataFim;

    private String descricao;

    @Enumerated(EnumType.STRING)
    private EstadoContrato estado;

    @Enumerated(EnumType.STRING)
    private CategoriaDocente categoria;

    @Enumerated(EnumType.STRING)
    private RelacaoJuridicaDocente relacaoJuridica;

    @Enumerated(EnumType.STRING)
    private DedicacaoDocente dedicacao;

    @ManyToOne
    @JoinColumn(name = "docente_id")
    private Docente docente;

}
