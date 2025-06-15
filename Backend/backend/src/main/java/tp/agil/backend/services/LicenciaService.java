package tp.agil.backend.services;

import tp.agil.backend.dtos.LicenciaDTO;
import tp.agil.backend.dtos.TitularDTO;
import tp.agil.backend.entities.Licencia;
import tp.agil.backend.entities.Titular;
import tp.agil.backend.entities.TipoClase;

public interface LicenciaService {
    LicenciaDTO crearLicencia(LicenciaDTO licenciaDTO);
    boolean verificarRequisitosPorClase(Titular titular, TipoClase tipoClase);
    Long generarNumeroLicencia();
}