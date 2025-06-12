package tp.agil.backend.services;

import org.springframework.stereotype.Service;
import tp.agil.backend.dtos.LicenciaDTO;
import tp.agil.backend.dtos.TitularDTO;
import tp.agil.backend.entities.*;
import tp.agil.backend.mappers.LicenciaMapper;
import tp.agil.backend.mappers.TitularMapper;
import tp.agil.backend.repositories.LicenciaRepository;
import tp.agil.backend.repositories.TitularRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class LicenciaServiceImpl implements LicenciaService {

    private final LicenciaRepository licenciaRepository;
    private final TitularRepository titularRepository;
    private final TitularMapper titularMapper;
    private final LicenciaMapper licenciaMapper;

    public LicenciaServiceImpl(LicenciaRepository licenciaRepository, TitularRepository titularRepository, TitularMapper titularMapper, LicenciaMapper licenciaMapper) {
        this.licenciaRepository = licenciaRepository;
        this.titularRepository = titularRepository;
        this.titularMapper = titularMapper;
        this.licenciaMapper = licenciaMapper;
    }

    @Override
    public LicenciaDTO crearLicencia(TitularDTO titular, String tipoClase1) {
        TipoClase tipoClase = TipoClase.valueOf(tipoClase1);

        if (!validarDatosTitular(titularMapper.dtoToEntity(titular))) {
            throw new IllegalArgumentException("Datos del titular inválidos.");
        }
        if (!verificarRequisitosPorClase(titularMapper.dtoToEntity(titular), tipoClase)) {
            throw new IllegalArgumentException("El titular no cumple los requisitos para la clase de licencia.");
        }

        Licencia nuevaLicencia = new Licencia();
        nuevaLicencia.setNumero(generarNumeroLicencia());
        nuevaLicencia.setTipoClase(tipoClase);
        nuevaLicencia.setFechaEmision(LocalDate.now());
        nuevaLicencia.setFechaVencimiento(LocalDate.now().plusYears(5));
        nuevaLicencia.setEstadoLicencia(EstadoLicencia.VIGENTE);
        nuevaLicencia.setTitular(titularMapper.dtoToEntity(titular));

        titularRepository.save(titularMapper.dtoToEntity(titular));
        licenciaRepository.save(nuevaLicencia);

        return licenciaMapper.entityToDto(nuevaLicencia);
    }

    @Override
    public boolean verificarRequisitosPorClase(Titular titular, TipoClase tipoClase) {
        int edad = titular.calcularEdad();
        if (tipoClase == TipoClase.A || tipoClase == TipoClase.B) {
            return edad >= 17;
        } else if (tipoClase == TipoClase.C || tipoClase == TipoClase.D || tipoClase == TipoClase.E) {
            if (edad < 21 || edad > 65) {
                return false;
            }
            List<Licencia> licencias = licenciaRepository.findByTitular(titular);
            return licencias.stream().anyMatch(licencia ->
                    licencia.getTipoClase() == TipoClase.B &&
                            licencia.getFechaEmision().isBefore(LocalDate.now().minusYears(1)) &&
                            licencia.getEstadoLicencia() == EstadoLicencia.VIGENTE
            );
        }
        return true;
    }

    @Override
    public Long generarNumeroLicencia() {
        return licenciaRepository.findAll().stream()
                .mapToLong(Licencia::getNumero)
                .max()
                .orElse(0L) + 1;
    }

    private boolean validarDatosTitular(Titular titular) {
        return titular.getNumeroDocumento() != null &&
                titular.getTipoDocumento() != null &&
                titular.getNombre() != null &&
                titular.getApellido() != null &&
                titular.getFechaNacimiento() != null;
    }
}