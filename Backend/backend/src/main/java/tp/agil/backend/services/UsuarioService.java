package tp.agil.backend.services;

import tp.agil.backend.dtos.UsuarioDTO;
import tp.agil.backend.dtos.UsuarioFormDTO;

public interface UsuarioService {
    UsuarioDTO crearUsuario(UsuarioFormDTO usuarioFormDTO);
    UsuarioDTO actualizarUsuario(UsuarioFormDTO usuarioFormDTO);
    UsuarioDTO getUsuarioByNumeroDocumento(String numeroDocumento);
}
