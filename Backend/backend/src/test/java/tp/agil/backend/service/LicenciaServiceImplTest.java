package tp.agil.backend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tp.agil.backend.dtos.LicenciaEmitidaDTO;
import tp.agil.backend.dtos.LicenciaFormDTO;
import tp.agil.backend.entities.*;
import tp.agil.backend.mappers.LicenciaEmitidaMapper;
import tp.agil.backend.repositories.*;
import tp.agil.backend.services.LicenciaServiceImpl;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LicenciaServiceImplTest {

    @InjectMocks
    private LicenciaServiceImpl licenciaService;

    @Mock
    private TitularRepository titularRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private LicenciaActivaRepository licenciaActivaRepository;
    @Mock
    private LicenciaExpiradaRepository licenciaExpiradaRepository;
    @Mock
    private LicenciaEmitidaMapper licenciaEmitidaMapper;

    private LicenciaFormDTO buildForm(String documento, String clases) {
        LicenciaFormDTO form = new LicenciaFormDTO();
        form.setDocumentoTitular(documento);
        form.setClases(clases);
        form.setObservaciones("Observaciones de prueba");
        return form;
    }

    @Test
    void testEdadMenorA17ParaClaseB_lanzaExcepcion() {
        Titular titular = new Titular();
        titular.setNumeroDocumento("12345678");
        titular.setFechaNacimiento(LocalDate.now().minusYears(16));

        when(titularRepository.findByNumeroDocumento("12345678")).thenReturn(titular);

        LicenciaFormDTO form = buildForm("12345678", "B");

        Exception ex = assertThrows(IllegalArgumentException.class, () -> licenciaService.emitirLicencia(form));
        assertTrue(ex.getMessage().contains("al menos 17 años"));
    }

    @Test
    void testEdadMenorA21ParaClaseC_lanzaExcepcion() {
        Titular titular = new Titular();
        titular.setNumeroDocumento("12345678");
        titular.setFechaNacimiento(LocalDate.now().minusYears(20));

        when(titularRepository.findByNumeroDocumento("12345678")).thenReturn(titular);

        LicenciaFormDTO form = buildForm("12345678", "C");

        Exception ex = assertThrows(IllegalArgumentException.class, () -> licenciaService.emitirLicencia(form));
        assertTrue(ex.getMessage().contains("al menos 21 años"));
    }

    @Test
    void testClaseProfesionalSinLicenciaB_lanzaExcepcion() {
        Titular titular = new Titular();
        titular.setNumeroDocumento("12345678");
        titular.setFechaNacimiento(LocalDate.now().minusYears(30));

        when(titularRepository.findByNumeroDocumento("12345678")).thenReturn(titular);
        when(licenciaActivaRepository.findByTitular_NumeroDocumento("12345678")).thenReturn(null);
        when(licenciaExpiradaRepository.findByTitular_NumeroDocumento("12345678")).thenReturn(Collections.emptyList());

        LicenciaFormDTO form = buildForm("12345678", "D");

        Exception ex = assertThrows(IllegalArgumentException.class, () -> licenciaService.emitirLicencia(form));
        assertTrue(ex.getMessage().contains("licencia B"));
    }

    @Test
    void testClaseProfesionalSinAntiguedadLicenciaB_lanzaExcepcion() {
        Titular titular = new Titular();
        titular.setNumeroDocumento("12345678");
        titular.setFechaNacimiento(LocalDate.now().minusYears(30));

        LicenciaExpirada licB = new LicenciaExpirada();
        licB.setClases("B");
        licB.setFechaEmision(LocalDate.now().minusMonths(6));

        when(titularRepository.findByNumeroDocumento("12345678")).thenReturn(titular);
        when(licenciaActivaRepository.findByTitular_NumeroDocumento("12345678")).thenReturn(null);
        when(licenciaExpiradaRepository.findByTitular_NumeroDocumento("12345678")).thenReturn(List.of(licB));

        LicenciaFormDTO form = buildForm("12345678", "C");

        Exception ex = assertThrows(IllegalArgumentException.class, () -> licenciaService.emitirLicencia(form));
        assertTrue(ex.getMessage().contains("al menos 1 año de antigüedad"));
    }

    @Test
    void testClaseProfesionalMayorA65SinLicenciaPrevia_lanzaExcepcion() {
        Titular titular = new Titular();
        titular.setNumeroDocumento("12345678");
        titular.setFechaNacimiento(LocalDate.now().minusYears(66));

        when(titularRepository.findByNumeroDocumento("12345678")).thenReturn(titular);
        when(licenciaActivaRepository.findByTitular_NumeroDocumento("12345678")).thenReturn(null);
        when(licenciaExpiradaRepository.findByTitular_NumeroDocumento("12345678")).thenReturn(Collections.emptyList());

        LicenciaFormDTO form = buildForm("12345678", "D");

        Exception ex = assertThrows(IllegalArgumentException.class, () -> licenciaService.emitirLicencia(form));
        assertTrue(ex.getMessage().contains("Edad máxima para primera licencia profesional"));
    }

    @Test
    void testTitularNoExiste_lanzaExcepcion() {
        when(titularRepository.findByNumeroDocumento("12345678")).thenReturn(null);
        LicenciaFormDTO form = buildForm("12345678", "B");
        Exception ex = assertThrows(IllegalArgumentException.class, () -> licenciaService.emitirLicencia(form));
        assertTrue(ex.getMessage().contains("No existe titular"));
    }

    @Test
    void testEmitirLicenciaClaseB_ok() {
        Titular titular = new Titular();
        titular.setNumeroDocumento("12345678");
        titular.setFechaNacimiento(LocalDate.now().minusYears(30));

        Usuario usuario = new Usuario();
        usuario.setNumeroDocumento("11999888");

        when(titularRepository.findByNumeroDocumento("12345678")).thenReturn(titular);
        when(usuarioRepository.findByNumeroDocumento(any())).thenReturn(usuario);
        when(licenciaActivaRepository.findByTitular_NumeroDocumento("12345678")).thenReturn(null);
        when(licenciaExpiradaRepository.findByTitular_NumeroDocumento("12345678")).thenReturn(Collections.emptyList());
        when(licenciaEmitidaMapper.entityToDto(any())).thenReturn(new LicenciaEmitidaDTO());

        LicenciaFormDTO form = buildForm("12345678", "B");

        assertDoesNotThrow(() -> licenciaService.emitirLicencia(form));
    }

    @Test
    void testEmitirLicenciaClaseCConLicenciaB_ok() {
        Titular titular = new Titular();
        titular.setNumeroDocumento("12345678");
        titular.setFechaNacimiento(LocalDate.now().minusYears(30));

        Usuario usuario = new Usuario();
        usuario.setNumeroDocumento("11999888");

        LicenciaExpirada licB = new LicenciaExpirada();
        licB.setClases("B");
        licB.setFechaEmision(LocalDate.now().minusYears(2));

        when(titularRepository.findByNumeroDocumento("12345678")).thenReturn(titular);
        when(usuarioRepository.findByNumeroDocumento(any())).thenReturn(usuario);
        when(licenciaActivaRepository.findByTitular_NumeroDocumento("12345678")).thenReturn(null);
        when(licenciaExpiradaRepository.findByTitular_NumeroDocumento("12345678")).thenReturn(List.of(licB));
        when(licenciaEmitidaMapper.entityToDto(any())).thenReturn(new LicenciaEmitidaDTO());

        LicenciaFormDTO form = buildForm("12345678", "C");

        assertDoesNotThrow(() -> licenciaService.emitirLicencia(form));
    }


    @Test
    void testRenovarLicencia_conMotivoInvalido_lanzaExcepcion() {
        LicenciaFormDTO form = buildForm("12345678", "B");

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                licenciaService.renovarLicencia(form, "extravio")
        );
        assertTrue(ex.getMessage().contains("'vencimiento' o 'modificacion'"));
    }

    @Test
    void testRenovarLicencia_conMotivoModificacion_lanzaExcepcion_Sprint1() {
        LicenciaFormDTO form = buildForm("12345678", "B");

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                licenciaService.renovarLicencia(form, "modificacion")
        );
        assertTrue(ex.getMessage().contains("solo está permitida por vencimiento"));
    }

    @Test
    void testRenovarLicencia_titularNoExiste_lanzaExcepcion() {
        LicenciaFormDTO form = buildForm("12345678", "B");
        when(titularRepository.findByNumeroDocumento("12345678")).thenReturn(null);

        Exception ex = assertThrows(RuntimeException.class, () ->
                licenciaService.renovarLicencia(form, "vencimiento")
        );
        assertTrue(ex.getMessage().contains("No se encontró un titular"));
    }

    @Test
    void testRenovarLicencia_licenciaActivaNoExiste_lanzaExcepcion() {
        LicenciaFormDTO form = buildForm("12345678", "B");
        Titular titular = new Titular();
        titular.setNumeroDocumento("12345678");

        when(titularRepository.findByNumeroDocumento("12345678")).thenReturn(titular);

        Exception ex = assertThrows(RuntimeException.class, () ->
                licenciaService.renovarLicencia(form, "vencimiento")
        );
        assertTrue(ex.getMessage().contains("No hay licencia activa"));
    }

    @Test
    void testRenovarLicencia_licenciaNoVencida_lanzaExcepcion() {
        LicenciaFormDTO form = buildForm("12345678", "B");

        Titular titular = new Titular();
        titular.setNumeroDocumento("12345678");

        LicenciaActiva activa = new LicenciaActiva();
        activa.setFechaVencimiento(LocalDate.now().plusDays(5));

        titular.setLicenciaActiva(activa);

        when(titularRepository.findByNumeroDocumento("12345678")).thenReturn(titular);

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                licenciaService.renovarLicencia(form, "vencimiento")
        );
        assertTrue(ex.getMessage().contains("aún no está vencida"));
    }

    @Test
    void testRenovarLicencia_ok() {
        LicenciaFormDTO form = buildForm("12345678", "B");

        Titular titular = new Titular();
        titular.setNumeroDocumento("12345678");
        titular.setFechaNacimiento(LocalDate.now().minusYears(30));

        Usuario usuario = new Usuario();
        usuario.setNumeroDocumento("11999888");

        LicenciaActiva activa = new LicenciaActiva();
        activa.setFechaVencimiento(LocalDate.now().minusDays(1));
        activa.setUsuario(usuario);
        activa.setClases("B");
        activa.setFechaEmision(LocalDate.now().minusYears(5));
        activa.setObservaciones("Antigua");

        titular.setLicenciaActiva(activa);

        when(titularRepository.findByNumeroDocumento("12345678")).thenReturn(titular);
        when(usuarioRepository.findByNumeroDocumento(any())).thenReturn(usuario);
        when(licenciaEmitidaMapper.entityToDto(any())).thenReturn(new LicenciaEmitidaDTO());

        assertDoesNotThrow(() -> licenciaService.renovarLicencia(form, "vencimiento"));
    }

}
