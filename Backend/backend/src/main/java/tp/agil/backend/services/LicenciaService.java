package tp.agil.backend.services;

import tp.agil.backend.dtos.*;

import java.time.LocalDate;
import java.util.List;

public interface LicenciaService {
    LicenciaEmitidaDTO emitirLicencia(LicenciaFormDTO licenciaFormDTO);
    LicenciaActivaDTO buscarLicenciaActivaPorDni(String numeroDocumento);
    ComprobanteDTO devolverComprobanteLicenciaPorDni(String numeroDocumento);
    LicenciaEmitidaDTO renovarLicencia(LicenciaFormDTO licenciaFormDTO, String motivo);

    LicenciasVencidasDTO obtenerLicenciasVencidasEntre(LocalDate desde, LocalDate hasta);
}