package tp.agil.backend.service;

import org.junit.jupiter.api.BeforeEach;
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

import java.lang.reflect.Method;
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

    // ---------------------- TESTS EXISTENTES ----------------------

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

    // ---------------------- TESTS CALCULO FECHA VENCIMIENTO ----------------------

    /**
     * Utilidad para invocar el método privado calcularFechaVencimiento usando reflexión.
     */
    private LocalDate calcularFechaVencimientoPrivado(Titular titular) throws Exception {
        Method m = LicenciaServiceImpl.class.getDeclaredMethod("calcularFechaVencimiento", Titular.class);
        m.setAccessible(true);
        return (LocalDate) m.invoke(licenciaService, titular);
    }

    @Test
    void testVigenciaMenor21SinLicenciaPrevia() throws Exception {
        Titular t = new Titular();
        t.setFechaNacimiento(LocalDate.now().minusYears(18));
        t.setNumeroDocumento("111");
        when(licenciaActivaRepository.findByTitular_NumeroDocumento("111")).thenReturn(null);
        when(licenciaExpiradaRepository.findByTitular_NumeroDocumento("111")).thenReturn(Collections.emptyList());

        LocalDate vencimiento = calcularFechaVencimientoPrivado(t);
        // Vigencia 1 año, debe vencer en el próximo cumpleaños + 1 año
        LocalDate proximoCumple = t.getFechaNacimiento().withYear(LocalDate.now().getYear());
        if (!proximoCumple.isAfter(LocalDate.now())) proximoCumple = proximoCumple.plusYears(1);
        assertEquals(proximoCumple.plusYears(1), vencimiento);
    }

    @Test
    void testVigenciaMenor21ConLicenciaPrevia() throws Exception {
        Titular t = new Titular();
        t.setFechaNacimiento(LocalDate.now().minusYears(20));
        t.setNumeroDocumento("222");
        when(licenciaActivaRepository.findByTitular_NumeroDocumento("222")).thenReturn(new LicenciaActiva());

        LocalDate vencimiento = calcularFechaVencimientoPrivado(t);
        // Vigencia 3 años, debe vencer en el próximo cumpleaños + 3 años
        LocalDate proximoCumple = t.getFechaNacimiento().withYear(LocalDate.now().getYear());
        if (!proximoCumple.isAfter(LocalDate.now())) proximoCumple = proximoCumple.plusYears(1);
        assertEquals(proximoCumple.plusYears(3), vencimiento);
    }

    @Test
    void testVigencia21a46() throws Exception {
        Titular t = new Titular();
        t.setFechaNacimiento(LocalDate.now().minusYears(30));
        t.setNumeroDocumento("333");
        when(licenciaActivaRepository.findByTitular_NumeroDocumento("333")).thenReturn(null);
        when(licenciaExpiradaRepository.findByTitular_NumeroDocumento("333")).thenReturn(Collections.emptyList());

        LocalDate vencimiento = calcularFechaVencimientoPrivado(t);
        // Vigencia 5 años
        LocalDate proximoCumple = t.getFechaNacimiento().withYear(LocalDate.now().getYear());
        if (!proximoCumple.isAfter(LocalDate.now())) proximoCumple = proximoCumple.plusYears(1);
        assertEquals(proximoCumple.plusYears(5), vencimiento);
    }

    @Test
    void testVigencia47a60() throws Exception {
        Titular t = new Titular();
        t.setFechaNacimiento(LocalDate.now().minusYears(50));
        t.setNumeroDocumento("444");
        when(licenciaActivaRepository.findByTitular_NumeroDocumento("444")).thenReturn(null);
        when(licenciaExpiradaRepository.findByTitular_NumeroDocumento("444")).thenReturn(Collections.emptyList());

        LocalDate vencimiento = calcularFechaVencimientoPrivado(t);
        // Vigencia 4 años
        LocalDate proximoCumple = t.getFechaNacimiento().withYear(LocalDate.now().getYear());
        if (!proximoCumple.isAfter(LocalDate.now())) proximoCumple = proximoCumple.plusYears(1);
        assertEquals(proximoCumple.plusYears(4), vencimiento);
    }

    @Test
    void testVigencia61a70() throws Exception {
        Titular t = new Titular();
        t.setFechaNacimiento(LocalDate.now().minusYears(65));
        t.setNumeroDocumento("555");
        when(licenciaActivaRepository.findByTitular_NumeroDocumento("555")).thenReturn(null);
        when(licenciaExpiradaRepository.findByTitular_NumeroDocumento("555")).thenReturn(Collections.emptyList());

        LocalDate vencimiento = calcularFechaVencimientoPrivado(t);
        // Vigencia 3 años
        LocalDate proximoCumple = t.getFechaNacimiento().withYear(LocalDate.now().getYear());
        if (!proximoCumple.isAfter(LocalDate.now())) proximoCumple = proximoCumple.plusYears(1);
        assertEquals(proximoCumple.plusYears(3), vencimiento);
    }

    @Test
    void testVigenciaMayor70() throws Exception {
        Titular t = new Titular();
        t.setFechaNacimiento(LocalDate.now().minusYears(75));
        t.setNumeroDocumento("666");
        when(licenciaActivaRepository.findByTitular_NumeroDocumento("666")).thenReturn(null);
        when(licenciaExpiradaRepository.findByTitular_NumeroDocumento("666")).thenReturn(Collections.emptyList());

        LocalDate vencimiento = calcularFechaVencimientoPrivado(t);
        // Vigencia 1 año
        LocalDate proximoCumple = t.getFechaNacimiento().withYear(LocalDate.now().getYear());
        if (!proximoCumple.isAfter(LocalDate.now())) proximoCumple = proximoCumple.plusYears(1);
        assertEquals(proximoCumple.plusYears(1), vencimiento);
    }

    @Test
    void testLimiteCambioDeRangoEdad() throws Exception {
        Titular t = new Titular();
        t.setNumeroDocumento("777");
        // Edad 46, debe ser vigencia 5
        t.setFechaNacimiento(LocalDate.now().minusYears(46));
        when(licenciaActivaRepository.findByTitular_NumeroDocumento("777")).thenReturn(null);
        when(licenciaExpiradaRepository.findByTitular_NumeroDocumento("777")).thenReturn(Collections.emptyList());
        LocalDate vencimiento = calcularFechaVencimientoPrivado(t);
        LocalDate proximoCumple = t.getFechaNacimiento().withYear(LocalDate.now().getYear());
        if (!proximoCumple.isAfter(LocalDate.now())) proximoCumple = proximoCumple.plusYears(1);
        assertEquals(proximoCumple.plusYears(5), vencimiento);

        // Edad 47, debe ser vigencia 4
        t.setFechaNacimiento(LocalDate.now().minusYears(47));
        vencimiento = calcularFechaVencimientoPrivado(t);
        proximoCumple = t.getFechaNacimiento().withYear(LocalDate.now().getYear());
        if (!proximoCumple.isAfter(LocalDate.now())) proximoCumple = proximoCumple.plusYears(1);
        assertEquals(proximoCumple.plusYears(4), vencimiento);
    }

    @Test
    void testFechaInicioEsHoy() throws Exception {
        Titular t = new Titular();
        t.setFechaNacimiento(LocalDate.now().minusYears(30));
        t.setNumeroDocumento("888");
        when(licenciaActivaRepository.findByTitular_NumeroDocumento("888")).thenReturn(null);
        when(licenciaExpiradaRepository.findByTitular_NumeroDocumento("888")).thenReturn(Collections.emptyList());

        // El metodo privado no retorna la fecha de inicio, pero se puede verificar que la fecha de vencimiento
        // siempre es mayor a hoy y que el cálculo parte de la fecha actual.
        LocalDate vencimiento = calcularFechaVencimientoPrivado(t);
        assertTrue(vencimiento.isAfter(LocalDate.now()));
    }
}