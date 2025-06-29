package tp.agil.backend.services;

import tp.agil.backend.dtos.TitularClasesDTO;
import tp.agil.backend.dtos.TitularDTO;

public interface TitularService {
    TitularDTO getTitularByNumeroDocumento(String numeroDocumento);
    TitularDTO crearTitular(TitularDTO titularDTO);
    TitularClasesDTO getTitularConClases(String numeroDocumento);
    TitularDTO modificarTitular(TitularDTO titularDTO);
}