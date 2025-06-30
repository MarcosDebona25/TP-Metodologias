package tp.agil.backend.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import tp.agil.backend.entities.Usuario;
import tp.agil.backend.exceptions.UsuarioNoEncontradoException;
import tp.agil.backend.repositories.UsuarioRepository;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username){ //el username será el numeroDocumento
        Usuario usuario = usuarioRepository.findByNumeroDocumento(username);
        if (usuario == null) throw new UsuarioNoEncontradoException("Usuario no encontrado con documento " + username);
        return usuario;
    }
}
