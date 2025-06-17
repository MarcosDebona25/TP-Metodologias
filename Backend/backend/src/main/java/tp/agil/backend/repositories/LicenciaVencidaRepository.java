package tp.agil.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tp.agil.backend.entities.LicenciaVencida;
import tp.agil.backend.entities.Titular;

import java.util.List;

public interface LicenciaVencidaRepository extends JpaRepository<LicenciaVencida, Long> {
    List<LicenciaVencida> findByTitular(Titular titular);
    List<LicenciaVencida> findByTitular_NumeroDocumento(Long numeroDocumento);
    //CRUD implementado por JPA
}
