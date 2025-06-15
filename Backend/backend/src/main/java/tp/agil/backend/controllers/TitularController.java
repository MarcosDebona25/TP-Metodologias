package tp.agil.backend.controllers;

import org.springframework.web.bind.annotation.*;
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
    public TitularDTO getTitularById(@PathVariable Long numeroDocumento) {
        return titularService.getTitularById(numeroDocumento);
    }
}