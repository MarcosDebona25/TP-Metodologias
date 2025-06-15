package tp.agil.backend.dtos;

import lombok.Getter;
import lombok.Setter;
import tp.agil.backend.entities.TipoDocumento;
import java.time.LocalDate;

/**
 * @author Marcos Debona
 */

@Getter
@Setter
public class TitularDTO {
    private TipoDocumento tipoDocumento;
    private Long numeroDocumento;
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private String direccion;
    private String grupoSanguineo;  // A, B, AB, O
    private boolean factorRh;       // true = positivo, false = negativo
    private boolean donanteOrganos;
}