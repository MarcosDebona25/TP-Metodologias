package tp.agil.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tp.agil.backend.entities.Titular;
import tp.agil.backend.repositories.TitularRepository;

@Service
public class TitularServiceImpl implements TitularService {

    private final TitularRepository titularRepository;

    public TitularServiceImpl(TitularRepository titularRepository) {
        this.titularRepository = titularRepository;
    }

    @Override
    public boolean validarDatosTitular(Titular titular) {
        if (titular.getNumeroDocumento() == null || titular.getTipoDocumento() == null) {
            return false;
        }
        if (titular.getNombre() == null || titular.getApellido() == null || titular.getFechaNacimiento() == null) {
            return false;
        }
        return true;
    }
}