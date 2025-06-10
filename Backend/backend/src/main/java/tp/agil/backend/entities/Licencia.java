package tp.agil.backend.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

/**
 * @author Marcos Debona
 */

@Getter
@Setter
@EqualsAndHashCode
@RequiredArgsConstructor
@ToString
@Entity
public class Licencia {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long numero;

    @Enumerated(EnumType.STRING)
    private TipoClase tipoClase;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaEmision;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaVencimiento;

    @Enumerated(EnumType.STRING)
    private EstadoLicencia estadoLicencia;

    private String observaciones;

    @ManyToOne
    @JoinColumn(name = "titular_numeroDocumento")
    private Titular titular;

    @ManyToOne
    @JoinColumn(name = "usuario_numeroDocumento")
    private Usuario usuario;
}