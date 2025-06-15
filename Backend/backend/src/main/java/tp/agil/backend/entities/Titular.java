package tp.agil.backend.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@RequiredArgsConstructor
@ToString
@Entity
public class Titular {
    @Id
    private Long numeroDocumento;

    private String nombre;
    private String apellido;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;
    private String direccion;
    private String grupoSanguineo; // A, B, AB, O
    private boolean factorRh;      // true = positivo, false = negativo
    private boolean donanteOrganos;

    @OneToOne(mappedBy = "titular", cascade = CascadeType.ALL)
    private Licencia licencia;

    public int calcularEdad() {
        LocalDate hoy = LocalDate.now();
        int edad = hoy.getYear() - this.fechaNacimiento.getYear();
        if (hoy.getMonthValue() < this.fechaNacimiento.getMonthValue() ||
                (hoy.getMonthValue() == this.fechaNacimiento.getMonthValue() && hoy.getDayOfMonth() < this.fechaNacimiento.getDayOfMonth())) {
            edad--;
        }
        return edad;
    }
}