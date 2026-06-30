package com.example.backend.Services;

import com.example.backend.Repository.DistribuicaoRepository;
import com.example.backend.Repository.UcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UcServices {

    @Autowired
    private UcRepository  ucRepository;

    @Autowired
    private DistribuicaoRepository distribuicaoRepository;



}
