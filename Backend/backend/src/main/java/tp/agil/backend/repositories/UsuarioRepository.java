package tp.agil.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tp.agil.backend.entities.Usuario;

/**
 * @author Marcos Debona
 */

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
