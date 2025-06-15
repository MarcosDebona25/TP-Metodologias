package tp.agil.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tp.agil.backend.entities.Titular;

public interface TitularRepository extends JpaRepository<Titular, Long> {
    Titular findByNumeroDocumento(Long numeroDocumento);
}
