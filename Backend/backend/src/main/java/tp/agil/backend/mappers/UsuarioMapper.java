package tp.agil.backend.mappers;

import org.springframework.security.crypto.password.PasswordEncoder;
import tp.agil.backend.dtos.UsuarioDTO;
import tp.agil.backend.dtos.UsuarioFormDTO;
import tp.agil.backend.entities.Usuario;

public class UsuarioMapper {

    public static Usuario usuarioFormDTOaUsuario(UsuarioFormDTO dto, PasswordEncoder passwordEncoder) {
        Usuario nuevo = new Usuario();
        nuevo.setNumeroDocumento(dto.getNumeroDocumento());
        nuevo.setApellido(dto.getApellido());
        nuevo.setNombre(dto.getNombre());
        nuevo.setEmail(dto.getEmail());
        nuevo.setRol(dto.getRol());
        nuevo.setContrasena(passwordEncoder.encode(dto.getContrasena()));
        return nuevo;
    }

    public static UsuarioDTO usuarioAusuarioDTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNumeroDocumento(usuario.getNumeroDocumento());
        dto.setApellido(usuario.getApellido());
        dto.setNombre(usuario.getNombre());
        dto.setEmail(usuario.getEmail());
        dto.setRol(usuario.getRol());
        return dto;
    }
}