package com.example.backend.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name= "uc_tipo_aula")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UC_Tipo_Aula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Float horas;

    @ManyToOne
    @JoinColumn(name = "uc_tipo_aula_id")
    private UC_Tipo_Aula uc_tipo_aula;

    @ManyToOne
    @JoinColumn(name = "uc_id")
    private UC uc;

}
