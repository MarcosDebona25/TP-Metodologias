package tp.agil.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tp.agil.backend.dtos.LoginRequestDTO;
import tp.agil.backend.dtos.JwtResponseDTO;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    public AuthController() {
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        // Lógica de autenticación y generación de JWT pendiente de implementación
        return ResponseEntity.status(501).build(); // 501 Not Implemented
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        // eliminando el token del almacenamiento local
        return ResponseEntity.status(501).build(); // 501 Not Implemented
    }
}