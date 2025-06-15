package tp.agil.backend.services;

import org.springframework.stereotype.Service;
import tp.agil.backend.dtos.TitularDTO;
import tp.agil.backend.entities.Titular;
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
    public boolean validarDatosTitular(Titular titular) {
        if (titular.getNumeroDocumento() == null || titular.getTipoDocumento() == null) {
            return false;
        }
        if (titular.getNombre() == null || titular.getApellido() == null || titular.getFechaNacimiento() == null) {
            return false;
        }
        return true;
    }

    @Override
    public TitularDTO getTitularId(Long numeroDocumento) {
        TitularDTO titularObtenido = titularMapper.entityToDto(titularRepository.findByNumeroDocumento(numeroDocumento));
        return titularObtenido;
    }
}