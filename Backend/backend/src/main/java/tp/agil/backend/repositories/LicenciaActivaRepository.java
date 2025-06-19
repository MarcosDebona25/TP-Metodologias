package tp.agil.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tp.agil.backend.entities.LicenciaActiva;
import tp.agil.backend.entities.Titular;
import java.time.LocalDate;
import java.util.List;


public interface LicenciaActivaRepository extends JpaRepository<LicenciaActiva, Long> {
    LicenciaActiva findByTitular(Titular titular);
    LicenciaActiva findByTitular_NumeroDocumento(String titular_numeroDocumento);
    List<LicenciaActiva> findByFechaVencimientoBetweenOrderByFechaVencimientoDesc(LocalDate desde, LocalDate hasta);

}