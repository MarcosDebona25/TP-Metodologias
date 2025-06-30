package tp.agil.backend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tp.agil.backend.dtos.UsuarioDTO;
import tp.agil.backend.dtos.UsuarioFormDTO;
import tp.agil.backend.services.UsuarioService;

@RestController
@RequestMapping("api/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping()
    public ResponseEntity<UsuarioDTO> altaUsuario(@RequestBody UsuarioFormDTO usuarioFormDTO){
        UsuarioDTO usuarioCreado = usuarioService.crearUsuario(usuarioFormDTO);
        return new ResponseEntity<>(usuarioCreado, HttpStatus.OK);
    }

    @PutMapping("/{numeroDocumento}")
    public ResponseEntity<UsuarioDTO> modificarUsuario(@RequestBody UsuarioFormDTO usuarioFormDTO){
        UsuarioDTO usuarioModificado = usuarioService.actualizarUsuario(usuarioFormDTO);
        return new ResponseEntity<>(usuarioModificado, HttpStatus.OK);
    }
}