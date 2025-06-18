package tp.agil.backend.dtos;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Setter
@Getter
public class LicenciaDTO {

    //Documento del titular
    private String documentoTitular;
    //Documento del usuario administrativo
    private String documentoUsuario;

    private String observaciones;

    //string separado por espacios: clases = "A B C D E F G"
    private String clases;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaEmision;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaVencimiento;

}