package tp.agil.backend.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Marcos Debona
 */

@Getter
@Setter
@EqualsAndHashCode
@RequiredArgsConstructor
@ToString
@Entity
public class Titular {
    @Id
    private Long numeroDocumento;

    @Enumerated(EnumType.STRING)
    private TipoDocumento tipoDocumento;

    private String nombre;
    private String apellido;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;

    private String direccion;

    private String grupoSanguineo;  // A, B, AB, O
    private boolean factorRh;       // true = positivo, false = negativo

    private boolean donanteOrganos;

    @OneToMany(mappedBy = "titular")
    private List<Licencia> listaLicencias;

    private String obtenerSangre(){
        return this.getGrupoSanguineo() + (this.isFactorRh()? "+":"-");
    }
}