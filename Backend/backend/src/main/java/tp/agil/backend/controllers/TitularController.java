package tp.agil.backend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tp.agil.backend.dtos.TitularClasesDTO;
import tp.agil.backend.dtos.TitularDTO;
import tp.agil.backend.services.TitularService;

@RestController
@RequestMapping("/api/titulares")
public class TitularController {

    private final TitularService titularService;

    public TitularController(TitularService titularService) {
        this.titularService = titularService;
    }

    @GetMapping("/id/{numeroDocumento}")
    public TitularClasesDTO getTitularByNumeroDocumento(@PathVariable String numeroDocumento) {
        return titularService.getTitularConClases(numeroDocumento);
    }

    @PostMapping()
    public ResponseEntity<TitularDTO> altaTitular(@RequestBody TitularDTO titularDTO) {
        TitularDTO titularCreado = titularService.crearTitular(titularDTO);
        return new ResponseEntity<>(titularCreado, HttpStatus.OK);
    }

    @PutMapping("/{numeroDocumento}")
    public ResponseEntity<TitularDTO> modificarTitular(@PathVariable String numeroDocumento, @RequestBody TitularDTO titularDTO) {
        TitularDTO titularActualizado = titularService.modificarTitular(titularDTO);
        return new ResponseEntity<>(titularActualizado, HttpStatus.OK);
    }

}