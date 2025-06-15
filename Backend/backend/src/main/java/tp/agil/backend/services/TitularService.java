package tp.agil.backend.services;

import tp.agil.backend.dtos.TitularDTO;

public interface TitularService {
    TitularDTO getTitularById(Long numeroDocumento);
}