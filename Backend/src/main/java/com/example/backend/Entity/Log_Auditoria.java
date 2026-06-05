package com.example.backend.Entity;


import com.example.backend.Enums.LogAcao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Entity
@Table(name= "log_auditoria")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Log_Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private LogAcao acao;

    private Date data;

    private String descricao;

    @ManyToOne
    @JoinColumn(name = "utilizador_id")
    private Utilizador utilizador;




}
