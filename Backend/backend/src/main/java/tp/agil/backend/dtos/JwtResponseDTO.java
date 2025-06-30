package tp.agil.backend.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponseDTO {
    private String numeroDocumento;
    private String rol;
    private String token;
}