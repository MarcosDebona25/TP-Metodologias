package tp.agil.backend.controllers;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tp.agil.backend.dtos.*;
import tp.agil.backend.services.LicenciaService;
import tp.agil.backend.repositories.LicenciaActivaRepository;
import tp.agil.backend.repositories.TitularRepository;

import java.time.LocalDate;
import java.util.List;

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

    @GetMapping("/filtros")
    public ResponseEntity<List<LicenciaActivaDTO>> getLicenciasVigentesPorCriterios(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String apellido,
            @RequestParam(required = false) String grupoFactor,
            @RequestParam(required = false) Boolean esDonante
    ) {
        List<LicenciaActivaDTO> licencias = licenciaService.buscarLicenciasPorCriterios(nombre, apellido, grupoFactor, esDonante);
        return new ResponseEntity<>(licencias, HttpStatus.OK);
    }

    // POST /api/licencias
    @PostMapping()
    public ResponseEntity<LicenciaEmitidaDTO> emitirLicencia(@RequestBody LicenciaFormDTO licenciaform) {
        LicenciaEmitidaDTO licenciaCreada = licenciaService.emitirLicencia(licenciaform);
        return new ResponseEntity<>(licenciaCreada, HttpStatus.OK);
    }

    // GET /api/licencias/{numeroDocumento} - solo si es numérico
    @GetMapping("/{numeroDocumento:\\d+}")
    public ResponseEntity<LicenciaActivaDTO> buscarLicenciaActivaPorDni(@PathVariable String numeroDocumento) {
        LicenciaActivaDTO dto = licenciaService.buscarLicenciaActivaPorDni(numeroDocumento);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    // GET /api/licencias/comprobante/{numeroDocumento}
    @GetMapping("/comprobante/{numeroDocumento}")
    public ResponseEntity<ComprobanteDTO> devolverComprobanteLicenciaPorDni(@PathVariable String numeroDocumento) {
        ComprobanteDTO dto = licenciaService.devolverComprobanteLicenciaPorDni(numeroDocumento);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    // POST /api/licencias/renovar/{motivo}
    @PostMapping("/renovar/{motivo}")
    public ResponseEntity<LicenciaEmitidaDTO> renovarLicencia(@RequestBody LicenciaFormDTO licenciaFormDTO, @PathVariable String motivo) {
        LicenciaEmitidaDTO renovada = licenciaService.renovarLicencia(licenciaFormDTO, motivo);
        return new ResponseEntity<>(renovada, HttpStatus.OK);
    }

    // GET /api/licencias/vencidas?desde=...&hasta=...
    @GetMapping("/vencidas")
    public ResponseEntity<LicenciasVencidasDTO> obtenerLicenciasVencidasEntre(
            @RequestParam("desde") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam("hasta") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        LicenciasVencidasDTO lista = licenciaService.obtenerLicenciasVencidasEntre(desde, hasta);
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }


}
