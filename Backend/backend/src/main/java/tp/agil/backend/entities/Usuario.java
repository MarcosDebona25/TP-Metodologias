package tp.agil.backend.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@RequiredArgsConstructor
@ToString
@Entity
public class Usuario {
    @Id
    private Long numeroDocumento;

    private String nombre;
    private String apellido;
    private String email;

    @OneToMany(mappedBy = "usuario")
    private List<Licencia> licenciasTramitadas;
}