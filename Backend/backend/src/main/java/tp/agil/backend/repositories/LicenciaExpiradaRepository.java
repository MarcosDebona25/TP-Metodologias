package tp.agil.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tp.agil.backend.entities.LicenciaExpirada;
import tp.agil.backend.entities.Titular;

import java.util.List;

public interface LicenciaExpiradaRepository extends JpaRepository<LicenciaExpirada, Long> {
    List<LicenciaExpirada> findByTitular(Titular titular);
    List<LicenciaExpirada> findByTitular_NumeroDocumento(String titular_numeroDocumento);
    //CRUD implementado por JPA
}
