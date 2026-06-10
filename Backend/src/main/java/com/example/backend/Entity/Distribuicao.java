package com.example.backend.Entity;


import com.example.backend.Enums.EstadoDistribuicao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Entity
@Table(name= "distribuicao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Distribuicao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;

    private String ano_letivo;

    private String versao;

    private String descricao;

    @Enumerated(EnumType.STRING)
    private EstadoDistribuicao estado = EstadoDistribuicao.ABERTO;

    private Date data_criacao;

    private Date data_fechar;

    @ManyToOne
    @JoinColumn(name = "departamento_id")
    private Departamento departamento;

    @ManyToOne
    @JoinColumn(name = "utilizador_id")
    private Utilizador utilizador;

}
