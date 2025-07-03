package tp.agil.backend.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tp.agil.backend.dtos.UsuarioDTO;
import tp.agil.backend.dtos.UsuarioFormDTO;
import tp.agil.backend.entities.Usuario;
import tp.agil.backend.exceptions.UsuarioExistenteException;
import tp.agil.backend.exceptions.UsuarioNoEncontradoException;
import tp.agil.backend.mappers.UsuarioMapper;
import tp.agil.backend.repositories.UsuarioRepository;

@Service
public class UsuarioServiceImpl implements UsuarioService {
    private PasswordEncoder passwordEncoder;
    private UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(PasswordEncoder passwordEncoder, UsuarioRepository usuarioRepository) {
        this.passwordEncoder = passwordEncoder;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UsuarioDTO crearUsuario(UsuarioFormDTO usuarioFormDTO) {
        Usuario existente = usuarioRepository.findByNumeroDocumento(usuarioFormDTO.getNumeroDocumento());
        if (existente != null) throw new UsuarioExistenteException("Ya existe un usuario con el numero de documento proporcionado");

        Usuario guardado = usuarioRepository.save(UsuarioMapper.usuarioFormDTOaUsuario(usuarioFormDTO, passwordEncoder));
        return UsuarioMapper.usuarioAusuarioDTO(guardado);
    }

    @Override
    public UsuarioDTO actualizarUsuario(UsuarioFormDTO usuarioFormDTO) {
        Usuario usuario = usuarioRepository.findByNumeroDocumento(usuarioFormDTO.getNumeroDocumento());
        if (usuario == null) {
            throw new UsuarioNoEncontradoException("No se encontró un usuario con el número de documento: " + usuarioFormDTO.getNumeroDocumento());
        }

        usuario.setNombre(usuarioFormDTO.getNombre());
        usuario.setApellido(usuarioFormDTO.getApellido());
        usuario.setEmail(usuarioFormDTO.getEmail());
        usuario.setRol(usuarioFormDTO.getRol());
        // No modificar la contraseña

        Usuario actualizado = usuarioRepository.save(usuario);
        return UsuarioMapper.usuarioAusuarioDTO(actualizado);
    }

    @Override
    public UsuarioDTO getUsuarioByNumeroDocumento(String numeroDocumento) {
        Usuario usuario = usuarioRepository.findByNumeroDocumento(numeroDocumento);
        if (usuario == null) {
            throw new UsuarioNoEncontradoException("No se encontró un usuario con el número de documento: " + numeroDocumento);
        }
        return UsuarioMapper.usuarioAusuarioDTO(usuario);
    }
}
