package tp.agil.backend.services;

import tp.agil.backend.dtos.TitularDTO;
import tp.agil.backend.entities.Titular;

public interface TitularService {
    boolean validarDatosTitular(Titular titular);
    TitularDTO getTitularId(Long numeroDocumento);
}