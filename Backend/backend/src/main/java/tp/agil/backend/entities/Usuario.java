package tp.agil.backend.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String numeroDocumento;

    private String nombre;
    private String apellido;
    private String email;

    @OneToMany(mappedBy = "usuario")
    private List<Licencia> licenciasTramitadas;
}