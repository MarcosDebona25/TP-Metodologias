package tp.agil.backend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tp.agil.backend.dtos.ComprobanteDTO;
import tp.agil.backend.dtos.LicenciaActivaDTO;
import tp.agil.backend.dtos.LicenciaDTO;
import tp.agil.backend.dtos.LicenciaFormDTO;
import tp.agil.backend.services.LicenciaService;
import tp.agil.backend.entities.LicenciaActiva;
import tp.agil.backend.entities.Titular;
import tp.agil.backend.repositories.LicenciaActivaRepository;
import tp.agil.backend.repositories.TitularRepository;


@RestController
@RequestMapping("/api/licencias")
public class LicenciaController {

    private final LicenciaService licenciaService;
    private final LicenciaActivaRepository licenciaActivaRepository;
    private final TitularRepository titularRepository;

    public LicenciaController(LicenciaService licenciaService, LicenciaActivaRepository licenciaActivaRepository, TitularRepository titularRepository) {
        this.licenciaService = licenciaService;
        this.licenciaActivaRepository = licenciaActivaRepository;
        this.titularRepository = titularRepository;
    }

    @PostMapping()
    public ResponseEntity<LicenciaDTO> emitirLicencia(@RequestBody LicenciaFormDTO licenciaform) {
        LicenciaDTO licenciaCreada = licenciaService.emitirLicencia(licenciaform);
        return new ResponseEntity<>(licenciaCreada, HttpStatus.OK);
    }

    @GetMapping("/{numeroDocumento}")
    public ResponseEntity<LicenciaActivaDTO> buscarLicenciaActivaPorDni(@PathVariable String numeroDocumento) {
        LicenciaActivaDTO dto = licenciaService.buscarLicenciaActivaPorDni(numeroDocumento);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/comprobante/{numeroDocumento}")
    public ResponseEntity<ComprobanteDTO> devolverComprobanteLicenciaPorDni(@PathVariable String numeroDocumento) {
        ComprobanteDTO dto = licenciaService.devolverComprobanteLicenciaPorDni(numeroDocumento);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}