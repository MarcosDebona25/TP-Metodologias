package tp.agil.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tp.agil.backend.entities.EstadoLicencia;
import tp.agil.backend.entities.Licencia;
import tp.agil.backend.entities.Titular;

import java.util.List;

/**
 * @author Marcos Debona
 */

public interface LicenciaRepository extends JpaRepository<Licencia, Long> {
    List<Licencia> findByTitular(Titular titular);
    List<Licencia> findByEstadoLicencia(EstadoLicencia estadoLicencia); // VENCIDA = EXPIRADA
}