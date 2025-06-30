package tp.agil.backend.dtos;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class LicenciasVencidasDTO {
    private List<LicenciaExpiradaDTO> expiradas;
    private List<LicenciaActivaDTO> activasVencidas;
}