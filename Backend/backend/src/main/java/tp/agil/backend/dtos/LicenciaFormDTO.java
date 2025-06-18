package tp.agil.backend.dtos;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class LicenciaFormDTO {
        private String documentoTitular; // dni del titular de la licencia
        private String documentoUsuario; // dni del usuario administrativo que lleva a cabo el tramite
        private String clases;
        private String observaciones;
}