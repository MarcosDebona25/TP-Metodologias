package tp.agil.backend.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@RequiredArgsConstructor
@ToString
@Entity
public class LicenciaActiva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long numero;

    private String observaciones;

    private List<TipoClase> listaClases;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaEmision;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaVencimiento;

    @OneToOne
    @JoinColumn(name = "titular_numeroDocumento")
    private Titular titular;

    @ManyToOne
    @JoinColumn(name = "usuario_numeroDocumento")
    private Usuario usuario;
}

