package tp.agil.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import tp.agil.backend.dtos.JwtResponseDTO;
import tp.agil.backend.dtos.LoginRequestDTO;
import tp.agil.backend.entities.Usuario;
import tp.agil.backend.services.UsuarioDetailsService;
import tp.agil.backend.utils.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioDetailsService usuarioDetailsService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager,
                          UsuarioDetailsService usuarioDetailsService,
                          JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.usuarioDetailsService = usuarioDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getNumeroDocumento(),
                            loginRequest.getContrasena()
                    )
            );

            UserDetails userDetails = usuarioDetailsService.loadUserByUsername(loginRequest.getNumeroDocumento());
            Usuario usuario = (Usuario) userDetails;

            String token = jwtUtil.generateToken(userDetails);

            JwtResponseDTO response = new JwtResponseDTO();
            response.setNumeroDocumento(usuario.getNumeroDocumento());
            response.setRol(usuario.getRol());
            response.setToken(token);

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        // Con JWT stateless, el logout se maneja en el frontend eliminando el token
        return ResponseEntity.ok("Logout exitoso");
    }
}