package tp.agil.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tp.agil.backend.entities.Usuario;
import tp.agil.backend.repositories.UsuarioRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public Usuario crearUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario obtenerUsuarioPorDocumento(Long numeroDocumento) {
        return usuarioRepository.findById(numeroDocumento)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con documento: " + numeroDocumento));
    }

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario actualizarUsuario(Long numeroDocumento, Usuario usuarioActualizado) {
        Usuario usuarioExistente = obtenerUsuarioPorDocumento(numeroDocumento);
        usuarioExistente.setNombre(usuarioActualizado.getNombre());
        usuarioExistente.setApellido(usuarioActualizado.getApellido());
        usuarioExistente.setEmail(usuarioActualizado.getEmail());
        return usuarioRepository.save(usuarioExistente);
    }

    @Override
    public void eliminarUsuario(Long numeroDocumento) {
        Usuario usuarioExistente = obtenerUsuarioPorDocumento(numeroDocumento);
        usuarioRepository.delete(usuarioExistente);
    }
}