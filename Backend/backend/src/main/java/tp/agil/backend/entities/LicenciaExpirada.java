package tp.agil.backend.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString
public class LicenciaExpirada extends Licencia {

    @ManyToOne
    @JoinColumn(name = "titular_id")
    private Titular titular;

}