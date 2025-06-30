package tp.agil.backend.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {
    private String numeroDocumento;
    private String contrasena;
}