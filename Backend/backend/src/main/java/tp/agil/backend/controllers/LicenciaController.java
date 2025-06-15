package tp.agil.backend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tp.agil.backend.dtos.LicenciaDTO;
import tp.agil.backend.services.LicenciaService;

/**
 * @author Marcos Debona
 */

@RestController
@RequestMapping("/api")
public class LicenciaController {
    private final LicenciaService licenciaService;

    public LicenciaController(LicenciaService licenciaService) {
        this.licenciaService = licenciaService;
    }

    @PostMapping("/licencias")
    public ResponseEntity<LicenciaDTO> emitirLicencia (@RequestBody LicenciaDTO licenciaDTO){
        LicenciaDTO licenciaCreada = licenciaService.crearLicencia(licenciaDTO);
        return new ResponseEntity<>(licenciaCreada, HttpStatus.OK);
    }
}







