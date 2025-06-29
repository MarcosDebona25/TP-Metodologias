package tp.agil.backend.services;

import org.springframework.stereotype.Service;
import tp.agil.backend.dtos.TitularClasesDTO;
import tp.agil.backend.dtos.TitularDTO;
import tp.agil.backend.entities.Titular;
import tp.agil.backend.exceptions.TitularExistenteException;
import tp.agil.backend.exceptions.TitularNoEncontradoException;
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
    public TitularDTO getTitularByNumeroDocumento(String numeroDocumento) {
        Titular titular = titularRepository.findByNumeroDocumento(numeroDocumento);
        if (titular == null) {
            throw new TitularNoEncontradoException("No se encontró un titular con el número de documento: " + numeroDocumento);
        }
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

    @Override
    public TitularClasesDTO getTitularConClases(String numeroDocumento) {
        Titular titular = titularRepository.findByNumeroDocumento(numeroDocumento);
        if (titular == null) {
            throw new TitularNoEncontradoException("No se encontró un titular con el número de documento: " + numeroDocumento);
        }
        TitularClasesDTO dto = new TitularClasesDTO();
        dto.setTitular(titularMapper.entityToDto(titular));
        dto.setClases(calcularClasesPosibles(titular));
        return dto;
    }

    private String calcularClasesPosibles(Titular titular) {
        int edad = titular.calcularEdad();
        boolean tieneB = false;
        boolean esMayor65 = edad > 65;
        java.time.LocalDate fechaEmisionB = null;

        if (titular.getLicenciaActiva() != null) {
            String clases = titular.getLicenciaActiva().getClases();
            if (clases != null && clases.contains("B")) {
                tieneB = true;
                fechaEmisionB = titular.getLicenciaActiva().getFechaEmision();
            }
        }

        if (titular.getLicenciasExpiradas() != null) {
            for (var l : titular.getLicenciasExpiradas()) {
                if (l.getClases() != null && l.getClases().contains("B")) {
                    tieneB = true;
                    if (fechaEmisionB == null || l.getFechaEmision().isBefore(fechaEmisionB)) {
                        fechaEmisionB = l.getFechaEmision();
                    }
                }
            }
        }

        StringBuilder clases = new StringBuilder();
        if (edad >= 17) clases.append("A B F G");
        boolean puedeProfesional = false;
        if (tieneB && fechaEmisionB != null && fechaEmisionB.isBefore(java.time.LocalDate.now().minusYears(1))) {
            puedeProfesional = true;
        }
        if (edad >= 21 && puedeProfesional && !esMayor65) clases.append(" C D E");

        return clases.toString().trim();
    }
}