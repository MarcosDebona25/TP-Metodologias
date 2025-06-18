package tp.agil.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tp.agil.backend.entities.LicenciaActiva;
import tp.agil.backend.entities.Titular;

public interface LicenciaActivaRepository extends JpaRepository<LicenciaActiva, Long> {
    LicenciaActiva findByTitular(Titular titular);
    LicenciaActiva findByTitular_NumeroDocumento(Long numeroDocumento);
    //CRUD ya implementado por JPA
}