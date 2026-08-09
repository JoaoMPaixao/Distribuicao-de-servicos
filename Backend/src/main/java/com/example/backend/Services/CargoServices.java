package com.example.backend.Services;

import com.example.backend.Dto.Cargos.ListarCargosDto;
import com.example.backend.Repository.CargoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CargoServices {

    @Autowired
    private CargoRepository cargoRepository;

    public List<ListarCargosDto> getCargos (){

        return cargoRepository.findAll()
                .stream()
                .map(cargo -> new ListarCargosDto(
                        cargo.getId(),
                        cargo.getTipo()

                ))
                .toList();

    }
}
