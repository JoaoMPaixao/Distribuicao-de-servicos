package com.example.backend.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name= "uc_juncao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UC_Juncao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "uc_tipo_aula_id")
    private UC_Tipo_Aula ucTipoAula;

    @ManyToOne
    @JoinColumn(name = "uc_distribuicao_id")
    private UC_Distribuicao ucDistribuicao;

}
