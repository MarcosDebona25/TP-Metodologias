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
public class Licencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long numero;

    private String observaciones;

    @OneToOne
    @JoinColumn(name = "titular_numeroDocumento")
    private Titular titular;

    @ManyToOne
    @JoinColumn(name = "usuario_numeroDocumento")
    private Usuario usuario;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "licencia_numero")
    private List<Clase> clases;
}