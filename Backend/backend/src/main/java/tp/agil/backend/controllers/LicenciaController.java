package tp.agil.backend.controllers;

import lombok.Getter;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tp.agil.backend.dtos.LicenciaDTO;
import tp.agil.backend.dtos.LicenciaFormDTO;
import tp.agil.backend.services.LicenciaService;

@RestController
@RequestMapping("/api/licencias")
public class LicenciaController {
    private final LicenciaService licenciaService;

    public LicenciaController(LicenciaService licenciaService) {
        this.licenciaService = licenciaService;
    }

    @PostMapping()
    public ResponseEntity<LicenciaDTO> emitirLicencia(@RequestBody LicenciaFormDTO licenciaform) {
        LicenciaDTO licenciaCreada = licenciaService.emitirLicencia(licenciaform);
        return new ResponseEntity<>(licenciaCreada, HttpStatus.OK);
    }

    @GetMapping("/{numeroDocumento}")
    public ResponseEntity<LicenciaDTO> buscarLicenciaActivaPorNumeroDocumentoTitular(@PathVariable Long numeroDocumento){
        // CONSULTAS LAS LICENCIAS ACTIVDAS POR DNI DEL TITULAR
        // JSON DE RESPUESTA: {
        //dniTitular: string
        //nombreTitular: string
        //apellidoTItular: string
        //clases: String de las calses separadas por un espacio
        //domicilioTitular: string
        //fechaEmisionLicencia: LocalDate ("AAAA-mm-DD")
        //fechaVencimientoLicencia: LocalDate ("AAAA-mm-DD")
        //grupoFactor: string
        //observacionesLicencia: string
    }

    @GetMapping("/comprobante/{numeroDocumento}")
    public ResponseEntity<ComrpobanteDTO> devolverDetallesComprobanteLicenciaActivaPorNumeroDocumentoTitular(@PathVariable Long numeroDocumento){
        // nombreTitular
        // apellidoTitular
        // clases: String de las calses separadas por un espacio
        // costos de emision: ...
        // costos administrativos: ....
    }
}







