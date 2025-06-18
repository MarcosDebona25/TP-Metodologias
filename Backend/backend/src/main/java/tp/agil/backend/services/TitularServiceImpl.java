package tp.agil.backend.services;

import org.springframework.stereotype.Service;
import tp.agil.backend.dtos.TitularDTO;
import tp.agil.backend.entities.Titular;
import tp.agil.backend.exceptions.TitularExistenteException;
import tp.agil.backend.mappers.TitularMapper;
import tp.agil.backend.repositories.TitularRepository;

@Service
public class TitularServiceImpl implements TitularService {

    private final TitularRepository titularRepository;
    private final TitularMapper titularMapper;

    public TitularServiceImpl(TitularRepository titularRepository, TitularMapper titularMapper) {
        this.titularRepository = titularRepository;
        this.titularMapper = titularMapper;
    }

    @Override
    public TitularDTO getTitularById(String numeroDocumento) {
        Titular titular = titularRepository.findByNumeroDocumento(numeroDocumento);
        return titularMapper.entityToDto(titular);
    }

    @Override
    public TitularDTO crearTitular(TitularDTO titularDTO) {
        Titular existente = titularRepository.findByNumeroDocumento(titularDTO.getNumeroDocumento());
        if (existente != null) {
            throw new TitularExistenteException("Ya existe un titular con el número de documento proporcionado.");
        }
        Titular titularGuardado = titularRepository.save(titularMapper.dtoToEntity(titularDTO));
        return titularMapper.entityToDto(titularGuardado);
    }
}