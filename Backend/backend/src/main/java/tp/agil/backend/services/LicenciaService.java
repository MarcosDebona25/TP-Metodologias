package tp.agil.backend.services;

import tp.agil.backend.entities.Licencia;
import tp.agil.backend.entities.Titular;
import tp.agil.backend.entities.TipoClase;

public interface LicenciaService {
    Licencia crearLicencia(Titular titular, TipoClase tipoClase);
    boolean verificarRequisitosPorClase(Titular titular, TipoClase tipoClase);
    Long generarNumeroLicencia();
}