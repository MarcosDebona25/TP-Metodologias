package tp.agil.backend.dtos;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class LicenciaFormDTO {
        private Long titularID; // id del titular de la licencia
        private Long usuarioId; // id del usuario administrativo que lleva a cabo el tramite
        private List<String> listaLicencias;
        private String observaciones;
}