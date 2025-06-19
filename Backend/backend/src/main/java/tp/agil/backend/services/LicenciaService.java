package tp.agil.backend.services;

import tp.agil.backend.dtos.ComprobanteDTO;
import tp.agil.backend.dtos.LicenciaActivaDTO;
import tp.agil.backend.dtos.LicenciaEmitidaDTO;
import tp.agil.backend.dtos.LicenciaFormDTO;

public interface LicenciaService {
    LicenciaEmitidaDTO emitirLicencia(LicenciaFormDTO licenciaFormDTO);
    LicenciaActivaDTO buscarLicenciaActivaPorDni(String numeroDocumento);
    ComprobanteDTO devolverComprobanteLicenciaPorDni(String numeroDocumento);
    LicenciaEmitidaDTO renovarLicencia(LicenciaFormDTO licenciaFormDTO, String motivo);
}