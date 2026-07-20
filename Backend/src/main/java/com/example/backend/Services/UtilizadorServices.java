package com.example.backend.Services;

import com.example.backend.Dto.Utilizadores.AlterarUtilizadoresDto;
import com.example.backend.Dto.Utilizadores.CriarUtilizadoresDto;
import com.example.backend.Dto.Utilizadores.ListarUtilizadoresDto;
import com.example.backend.Entity.Cargo;
import com.example.backend.Entity.Departamento;
import com.example.backend.Entity.Distribuicao;
import com.example.backend.Entity.Utilizador;
import com.example.backend.Enums.EstadoDistribuicao;
import com.example.backend.Repository.CargoRepository;
import com.example.backend.Repository.DepartamentoRepository;
import com.example.backend.Repository.UtilizadorRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtilizadorServices {

    @Autowired
    private UtilizadorRepository utilizadorRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Autowired
    private CargoRepository cargoRepository;

    // Logger a apontar para o ficheiro estabelecido no xml
    private static final Logger auditLogger = LoggerFactory.getLogger("auditoria");


    public List<ListarUtilizadoresDto> getUtilizadores (){

        return utilizadorRepository.findAll()
                .stream()
                .map(utilizador -> new ListarUtilizadoresDto(
                        utilizador.getId(),
                        utilizador.getNome(),
                        utilizador.getEmail(),
                        utilizador.getPassword(),
                        utilizador.getEstado(),
                        utilizador.getCargo().getId(),
                        utilizador.getCargo().getTipo(),
                        utilizador.getDepartamento().getId(),
                        utilizador.getDepartamento().getNome()

                ))
                .toList();

    }

    @Transactional
    public void CriarUtilizador(CriarUtilizadoresDto dto, Integer utilizador_id){

        Departamento departamento = departamentoRepository.findById(dto.departamento_id())
                .orElseThrow(() -> new RuntimeException("Departamento não encontrado"));

        Cargo cargo = cargoRepository.findById(dto.cargo_id())
                .orElseThrow(() -> new RuntimeException("Cargo não encontrado"));

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode(dto.password());

        Utilizador utilizador = new Utilizador();
        utilizador.setNome(dto.nome());
        utilizador.setEmail(dto.email());
        utilizador.setPassword(encode);
        utilizador.setEstado(dto.estado());
        utilizador.setCargo(cargo);
        utilizador.setDepartamento(departamento);


        utilizadorRepository.save(utilizador);

        // LOG DE AUDITORIA: Criação
        auditLogger.info("Utilizador ID: {} CRIOU o Utilizador ID: {} ('{}').",
                utilizador_id, utilizador.getId(), utilizador.getNome());

    }

    @Transactional
    public void AlterarUtilizador (AlterarUtilizadoresDto dto, Integer utilizador_id, Integer utilizadorAuth_id){

        Departamento departamento = departamentoRepository.findById(dto.departamento_id())
                .orElseThrow(() -> new RuntimeException("Departamento não encontrado"));

        Cargo cargo = cargoRepository.findById(dto.cargo_id())
                .orElseThrow(() -> new RuntimeException("Cargo não encontrado"));

        Utilizador utilizador = utilizadorRepository.findById(utilizador_id).orElseThrow(() -> new RuntimeException("Utilizador não encontrado"));

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode(dto.password());

        utilizador.setNome(dto.nome());
        utilizador.setEmail(dto.email());
        utilizador.setPassword(encode);
        utilizador.setEstado(dto.estado());
        utilizador.setCargo(cargo);
        utilizador.setDepartamento(departamento);

        // LOG DE AUDITORIA: Alteração de Curso
        auditLogger.info("Utilizador ID: {} ALTEROU o Utilizador  ID: {} para nome {} ; para email {} ; para estado {} ; para cargo {} ; para departamento {}.",
                utilizadorAuth_id, utilizador_id, utilizador.getNome(), dto.email(), dto.estado(), dto.cargo_id(), dto.departamento_id());

    }

    @Transactional
    public void DeleteUtilizador(Integer utilizador_id, Integer utilizadorAuth_id) {

        //verifica se a distribuicao e o utilizador têm o mesmo departamento e retorna a distribuicao
        Utilizador utilizador = utilizadorRepository.findById(utilizador_id).orElseThrow(() -> new RuntimeException("Utilizador não encontrado"));

        utilizadorRepository.delete(utilizador);

        // LOG DE AUDITORIA: Delete
        auditLogger.info("Utilizador ID: {} APAGOU o Utilizador ID: {}.",
                utilizadorAuth_id, utilizador_id);

    }

}
