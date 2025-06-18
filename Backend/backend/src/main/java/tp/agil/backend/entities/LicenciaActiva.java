package tp.agil.backend.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString
public class LicenciaActiva extends Licencia {

    @OneToOne
    @JoinColumn(name = "titular_numeroDocumento")
    private Titular titular;

}