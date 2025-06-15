package tp.agil.backend.services;

import tp.agil.backend.dtos.LicenciaFormDTO;
import tp.agil.backend.dtos.LicenciaDTO;

public interface LicenciaService {
    LicenciaDTO emitirLicencia(LicenciaFormDTO licenciaFormDTO);
}