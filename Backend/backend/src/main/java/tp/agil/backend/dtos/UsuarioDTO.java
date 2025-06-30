package tp.agil.backend.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioDTO {
    private String numeroDocumento;
    private String apellido;
    private String nombre;
    private String email;
    private String rol;
}