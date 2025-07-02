package tp.agil.backend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import tp.agil.backend.dtos.LicenciaActivaDTO;
import tp.agil.backend.dtos.LicenciaEmitidaDTO;
import tp.agil.backend.dtos.LicenciaFormDTO;
import tp.agil.backend.entities.*;
import tp.agil.backend.exceptions.TitularNoEncontradoException;
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
    @Mock
    private tp.agil.backend.mappers.LicenciaExpiradaMapper licenciaExpiradaMapper;
    @Mock
    private tp.agil.backend.mappers.LicenciaActivaMapper licenciaActivaMapper;

    private LicenciaFormDTO buildForm(String documento, String clases) {
        LicenciaFormDTO form = new LicenciaFormDTO();
        form.setDocumentoTitular(documento);
        form.setClases(clases);
        form.setObservaciones("Observaciones de prueba");
        return form;
    }

    // =========================
    // === TESTS EMITIR LICENCIA
    // =========================

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
        Exception ex = assertThrows(TitularNoEncontradoException.class, () -> licenciaService.emitirLicencia(form));
        assertTrue(ex.getMessage().contains("No se encontró un titular"));
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

    // =========================
    // === TESTS RENOVAR LICENCIA
    // =========================

    @Test
    void testRenovarLicencia_conMotivoInvalido_lanzaExcepcion() {
        LicenciaFormDTO form = buildForm("12345678", "B");

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                licenciaService.renovarLicencia(form, "extravio")
        );
        assertTrue(ex.getMessage().contains("'vencimiento' o 'modificacion'"));
    }

// TEST DEL SPRINT 1, AHORA YA SE IMPLEMENTÓ LA RENOVACIÓN POR MODIFICACIÓN
//    @Test
//    void testRenovarLicencia_conMotivoModificacion_lanzaExcepcion_Sprint1() {
//        LicenciaFormDTO form = buildForm("12345678", "B");
//
//        Exception ex = assertThrows(IllegalArgumentException.class, () ->
//                licenciaService.renovarLicencia(form, "modificacion")
//        );
//        assertTrue(ex.getMessage().contains("solo está permitida por vencimiento"));
//    }

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

    // ==========================================
    // === TESTS DE CÁLCULO DE VIGENCIA Y VENCIMIENTO
    // ==========================================

    private Titular buildTitular(LocalDate nacimiento) {
        Titular t = new Titular();
        t.setNumeroDocumento("99999999");
        t.setFechaNacimiento(nacimiento);
        return t;
    }

    private LocalDate invocarCalcularFechaVencimiento(Titular titular) throws Exception {
        Method m = LicenciaServiceImpl.class.getDeclaredMethod("calcularFechaVencimiento", Titular.class);
        m.setAccessible(true);
        return (LocalDate) m.invoke(licenciaService, titular);
    }

    @Test
    void testVigenciaMenor21_sinLicencia() throws Exception {
        LocalDate hoy = LocalDate.now();
        LocalDate nacimiento = hoy.minusYears(20);
        Titular t = buildTitular(nacimiento);

        when(licenciaActivaRepository.findByTitular_NumeroDocumento(any())).thenReturn(null);
        when(licenciaExpiradaRepository.findByTitular_NumeroDocumento(any())).thenReturn(Collections.emptyList());

        LocalDate vencimiento = invocarCalcularFechaVencimiento(t);

        assertEquals(nacimiento.getMonth(), vencimiento.getMonth());
        assertEquals(nacimiento.getDayOfMonth(), vencimiento.getDayOfMonth());
        assertEquals(1, vencimiento.getYear() - hoy.getYear());
        assertTrue(vencimiento.isAfter(hoy));
    }

    @Test
    void testVigenciaMenor21_conLicenciaPrevia() throws Exception {
        LocalDate hoy = LocalDate.now();
        LocalDate nacimiento = hoy.minusYears(19);
        Titular t = buildTitular(nacimiento);

        // Solo este stub es necesario si el metodo solo consulta la licencia activa
        when(licenciaActivaRepository.findByTitular_NumeroDocumento(any())).thenReturn(mock(LicenciaActiva.class));
        // Elimina el siguiente si no se usa:
        //when(licenciaExpiradaRepository.findByTitular_NumeroDocumento(any())).thenReturn(Collections.emptyList());

        LocalDate vencimiento = invocarCalcularFechaVencimiento(t);

        assertEquals(nacimiento.getMonth(), vencimiento.getMonth());
        assertEquals(nacimiento.getDayOfMonth(), vencimiento.getDayOfMonth());
        assertEquals(3, vencimiento.getYear() - hoy.getYear());
        assertTrue(vencimiento.isAfter(hoy));
    }

    @Test
    void testVigencia21a46() throws Exception {
        LocalDate hoy = LocalDate.now();
        LocalDate nacimiento = hoy.minusYears(30);
        Titular t = buildTitular(nacimiento);

        when(licenciaActivaRepository.findByTitular_NumeroDocumento(any())).thenReturn(null);
        when(licenciaExpiradaRepository.findByTitular_NumeroDocumento(any())).thenReturn(Collections.emptyList());

        LocalDate vencimiento = invocarCalcularFechaVencimiento(t);

        assertEquals(nacimiento.getMonth(), vencimiento.getMonth());
        assertEquals(nacimiento.getDayOfMonth(), vencimiento.getDayOfMonth());
        assertEquals(5, vencimiento.getYear() - hoy.getYear());
        assertTrue(vencimiento.isAfter(hoy));
    }

    @Test
    void testVigencia47a60() throws Exception {
        LocalDate hoy = LocalDate.now();
        LocalDate nacimiento = hoy.minusYears(50);
        Titular t = buildTitular(nacimiento);

        when(licenciaActivaRepository.findByTitular_NumeroDocumento(any())).thenReturn(null);
        when(licenciaExpiradaRepository.findByTitular_NumeroDocumento(any())).thenReturn(Collections.emptyList());

        LocalDate vencimiento = invocarCalcularFechaVencimiento(t);

        assertEquals(nacimiento.getMonth(), vencimiento.getMonth());
        assertEquals(nacimiento.getDayOfMonth(), vencimiento.getDayOfMonth());
        assertEquals(4, vencimiento.getYear() - hoy.getYear());
        assertTrue(vencimiento.isAfter(hoy));
    }

    @Test
    void testVigencia61a70() throws Exception {
        LocalDate hoy = LocalDate.now();
        LocalDate nacimiento = hoy.minusYears(65);
        Titular t = buildTitular(nacimiento);

        when(licenciaActivaRepository.findByTitular_NumeroDocumento(any())).thenReturn(null);
        when(licenciaExpiradaRepository.findByTitular_NumeroDocumento(any())).thenReturn(Collections.emptyList());

        LocalDate vencimiento = invocarCalcularFechaVencimiento(t);

        assertEquals(nacimiento.getMonth(), vencimiento.getMonth());
        assertEquals(nacimiento.getDayOfMonth(), vencimiento.getDayOfMonth());
        assertEquals(3, vencimiento.getYear() - hoy.getYear());
        assertTrue(vencimiento.isAfter(hoy));
    }

    @Test
    void testVigenciaMayor70() throws Exception {
        LocalDate hoy = LocalDate.now();
        LocalDate nacimiento = hoy.minusYears(75);
        Titular t = buildTitular(nacimiento);

        when(licenciaActivaRepository.findByTitular_NumeroDocumento(any())).thenReturn(null);
        when(licenciaExpiradaRepository.findByTitular_NumeroDocumento(any())).thenReturn(Collections.emptyList());

        LocalDate vencimiento = invocarCalcularFechaVencimiento(t);

        assertEquals(nacimiento.getMonth(), vencimiento.getMonth());
        assertEquals(nacimiento.getDayOfMonth(), vencimiento.getDayOfMonth());
        assertEquals(1, vencimiento.getYear() - hoy.getYear());
        assertTrue(vencimiento.isAfter(hoy));
    }

    @Test
    void testCambioDeRangoEnCumple() throws Exception {
        LocalDate hoy = LocalDate.now();
        // Cumple hoy, pasa de 46 a 47 (vigencia debe ser 4 años)
        LocalDate nacimiento = hoy.minusYears(47);
        Titular t = buildTitular(nacimiento);

        when(licenciaActivaRepository.findByTitular_NumeroDocumento(any())).thenReturn(null);
        when(licenciaExpiradaRepository.findByTitular_NumeroDocumento(any())).thenReturn(Collections.emptyList());

        LocalDate vencimiento = invocarCalcularFechaVencimiento(t);

        assertEquals(nacimiento.getMonth(), vencimiento.getMonth());
        assertEquals(nacimiento.getDayOfMonth(), vencimiento.getDayOfMonth());
        assertEquals(4, vencimiento.getYear() - hoy.getYear());
        assertTrue(vencimiento.isAfter(hoy));
    }

    @Test
    void testFechaInicioEsHoy() throws Exception {
        LocalDate hoy = LocalDate.now();
        LocalDate nacimiento = hoy.minusYears(30);
        Titular t = buildTitular(nacimiento);

        when(licenciaActivaRepository.findByTitular_NumeroDocumento(any())).thenReturn(null);
        when(licenciaExpiradaRepository.findByTitular_NumeroDocumento(any())).thenReturn(Collections.emptyList());

        LocalDate vencimiento = invocarCalcularFechaVencimiento(t);

        // La fecha de inicio de vigencia siempre es hoy
        assertTrue(!vencimiento.isBefore(hoy));
    }

    // === TESTS DE CÁLCULO DE COSTO DE LICENCIA ===

    private double calcularCosto(String clases, Titular titular) throws Exception {
        Method m = LicenciaServiceImpl.class.getDeclaredMethod("calcularCostoEmision", String.class, Titular.class);
        m.setAccessible(true);
        return (double) m.invoke(licenciaService, clases, titular);
    }

    private double costoPorClaseYVigencia(String clase, int vigencia) throws Exception {
        Method m = LicenciaServiceImpl.class.getDeclaredMethod("costoPorClaseYVigencia", String.class, int.class);
        m.setAccessible(true);
        return (double) m.invoke(licenciaService, clase, vigencia);
    }

    @Test
    void testCostosPorClaseYVigencia() throws Exception {
        // Simula un titular de 30 años (vigencia 5)
        Titular titular = new Titular();
        titular.setFechaNacimiento(LocalDate.now().minusYears(30));
        // Tabla de precios esperada
        assertEquals(40.0, costoPorClaseYVigencia("A", 5));
        assertEquals(30.0, costoPorClaseYVigencia("B", 4));
        assertEquals(25.0, costoPorClaseYVigencia("G", 3));
        assertEquals(20.0, costoPorClaseYVigencia("F", 1));
        assertEquals(47.0, costoPorClaseYVigencia("C", 5));
        assertEquals(35.0, costoPorClaseYVigencia("C", 4));
        assertEquals(30.0, costoPorClaseYVigencia("C", 3));
        assertEquals(23.0, costoPorClaseYVigencia("C", 1));
        assertEquals(50.0, costoPorClaseYVigencia("D", 5));
        assertEquals(40.0, costoPorClaseYVigencia("D", 4));
        assertEquals(35.0, costoPorClaseYVigencia("D", 3));
        assertEquals(28.0, costoPorClaseYVigencia("D", 1));
        assertEquals(59.0, costoPorClaseYVigencia("E", 5));
        assertEquals(44.0, costoPorClaseYVigencia("E", 4));
        assertEquals(39.0, costoPorClaseYVigencia("E", 3));
        assertEquals(29.0, costoPorClaseYVigencia("E", 1));
    }

    @Test
    void testCostosInvalidosLanzanExcepcion() throws Exception {
        assertThrows(Exception.class, () -> costoPorClaseYVigencia("Z", 5));
        assertThrows(Exception.class, () -> costoPorClaseYVigencia("A", 2));
        assertThrows(Exception.class, () -> costoPorClaseYVigencia("C", 2));
    }

    @Test
    void testCostoEmisionMultiplesClases() throws Exception {
        Titular titular = new Titular();
        titular.setFechaNacimiento(LocalDate.now().minusYears(30)); // vigencia 5
        double costo = calcularCosto("B C", titular);
        assertEquals(40.0 + 47.0, costo);
    }

    // =========================
    // === TESTS OBTENCIÓN DE LICENCIAS EXPIRADAS
    // =========================

    @Test
    void testObtenerLicenciasExpiradas_filtradoYOrden() {
        LocalDate hoy = LocalDate.now();
        LicenciaExpirada lic1 = new LicenciaExpirada();
        lic1.setFechaVencimiento(hoy.minusDays(2));
        Titular t1 = new Titular();
        t1.setNumeroDocumento("1");
        lic1.setTitular(t1);

        LicenciaExpirada lic2 = new LicenciaExpirada();
        lic2.setFechaVencimiento(hoy.minusDays(1));
        Titular t2 = new Titular();
        t2.setNumeroDocumento("2");
        lic2.setTitular(t2);

        LicenciaExpirada lic3 = new LicenciaExpirada();
        lic3.setFechaVencimiento(hoy.minusDays(3));
        Titular t3 = new Titular();
        t3.setNumeroDocumento("3");
        lic3.setTitular(t3);

        when(licenciaExpiradaRepository.findByFechaVencimientoBetweenOrderByFechaVencimientoDesc(
                hoy.minusDays(5), hoy)).thenReturn(List.of(lic2, lic1, lic3));
        when(licenciaExpiradaMapper.entityToDto(any())).thenAnswer(inv -> {
            LicenciaExpirada l = inv.getArgument(0);
            var dto = new tp.agil.backend.dtos.LicenciaExpiradaDTO();
            dto.setDocumentoTitular(l.getTitular().getNumeroDocumento());
            dto.setFechaVencimientoLicencia(l.getFechaVencimiento());
            return dto;
        });
        when(licenciaActivaRepository.findByFechaVencimientoBetweenOrderByFechaVencimientoDesc(any(), any()))
                .thenReturn(Collections.emptyList());

        var result = licenciaService.obtenerLicenciasVencidasEntre(hoy.minusDays(5), hoy);

        assertEquals(3, result.getExpiradas().size());
        assertEquals("2", result.getExpiradas().get(0).getDocumentoTitular()); // más reciente
        assertEquals("1", result.getExpiradas().get(1).getDocumentoTitular());
        assertEquals("3", result.getExpiradas().get(2).getDocumentoTitular()); // más antigua
    }

    @Test
    void testObtenerLicenciasExpiradas_sinResultados() {
        LocalDate hoy = LocalDate.now();
        when(licenciaExpiradaRepository.findByFechaVencimientoBetweenOrderByFechaVencimientoDesc(
                hoy.minusDays(10), hoy.minusDays(5))).thenReturn(Collections.emptyList());
        when(licenciaActivaRepository.findByFechaVencimientoBetweenOrderByFechaVencimientoDesc(any(), any()))
                .thenReturn(Collections.emptyList());

        var result = licenciaService.obtenerLicenciasVencidasEntre(hoy.minusDays(10), hoy.minusDays(5));
        assertNotNull(result.getExpiradas());
        assertTrue(result.getExpiradas().isEmpty());
    }

    @Test
    void testObtenerLicenciasExpiradas_conActivasVencidasYExpiradas() {
        LocalDate hoy = LocalDate.now();
        // Expirada
        LicenciaExpirada exp1 = new LicenciaExpirada();
        exp1.setFechaVencimiento(hoy.minusDays(2));
        Titular t1 = new Titular();
        t1.setNumeroDocumento("1");
        exp1.setTitular(t1);
        // Activa vencida
        LicenciaActiva act1 = new LicenciaActiva();
        act1.setFechaVencimiento(hoy.minusDays(1));
        Titular t2 = new Titular();
        t2.setNumeroDocumento("10");
        act1.setTitular(t2);

        when(licenciaExpiradaRepository.findByFechaVencimientoBetweenOrderByFechaVencimientoDesc(hoy.minusDays(5), hoy))
                .thenReturn(List.of(exp1));
        when(licenciaActivaRepository.findByFechaVencimientoBetweenOrderByFechaVencimientoDesc(hoy.minusDays(5), hoy))
                .thenReturn(List.of(act1));
        when(licenciaExpiradaMapper.entityToDto(any())).thenAnswer(inv -> {
            LicenciaExpirada l = inv.getArgument(0);
            var dto = new tp.agil.backend.dtos.LicenciaExpiradaDTO();
            dto.setDocumentoTitular(l.getTitular().getNumeroDocumento());
            dto.setFechaVencimientoLicencia(l.getFechaVencimiento());
            return dto;
        });
        when(licenciaActivaMapper.entityToDto(any())).thenAnswer(inv -> {
            LicenciaActiva l = inv.getArgument(0);
            var dto = new tp.agil.backend.dtos.LicenciaActivaDTO();
            dto.setDocumentoTitular(l.getTitular().getNumeroDocumento());
            dto.setFechaVencimientoLicencia(l.getFechaVencimiento());
            return dto;
        });

        var result = licenciaService.obtenerLicenciasVencidasEntre(hoy.minusDays(5), hoy);

        assertEquals(1, result.getExpiradas().size());
        assertEquals("1", result.getExpiradas().get(0).getDocumentoTitular());
        assertEquals(1, result.getActivasVencidas().size());
        assertEquals("10", result.getActivasVencidas().get(0).getDocumentoTitular());
    }

    @Test
    void testObtenerLicenciasExpiradas_fronterasDeFechas() {
        LocalDate desde = LocalDate.of(2024, 1, 1);
        LocalDate hasta = LocalDate.of(2024, 1, 31);

        LicenciaExpirada lic = new LicenciaExpirada();
        lic.setFechaVencimiento(desde);
        Titular t = new Titular();
        t.setNumeroDocumento("5");
        lic.setTitular(t);

        when(licenciaExpiradaRepository.findByFechaVencimientoBetweenOrderByFechaVencimientoDesc(desde, hasta))
                .thenReturn(List.of(lic));
        when(licenciaExpiradaMapper.entityToDto(any())).thenAnswer(inv -> {
            LicenciaExpirada l = inv.getArgument(0);
            var dto = new tp.agil.backend.dtos.LicenciaExpiradaDTO();
            dto.setDocumentoTitular(l.getTitular().getNumeroDocumento());
            dto.setFechaVencimientoLicencia(l.getFechaVencimiento());
            return dto;
        });
        when(licenciaActivaRepository.findByFechaVencimientoBetweenOrderByFechaVencimientoDesc(desde, hasta))
                .thenReturn(Collections.emptyList());

        var result = licenciaService.obtenerLicenciasVencidasEntre(desde, hasta);

        assertEquals(1, result.getExpiradas().size());
        assertEquals(desde, result.getExpiradas().get(0).getFechaVencimientoLicencia());
    }

    @Test
    void testObtenerLicenciasExpiradas_mapeoCompletoDatos() {
        LocalDate hoy = LocalDate.now();
        LicenciaExpirada lic = new LicenciaExpirada();
        lic.setFechaVencimiento(hoy.minusDays(1));
        Titular titular = new Titular();
        titular.setNumeroDocumento("123");
        titular.setNombre("Ana");
        titular.setApellido("García");
        lic.setTitular(titular);

        when(licenciaExpiradaRepository.findByFechaVencimientoBetweenOrderByFechaVencimientoDesc(any(), any()))
                .thenReturn(List.of(lic));
        when(licenciaExpiradaMapper.entityToDto(any())).thenAnswer(inv -> {
            LicenciaExpirada l = inv.getArgument(0);
            var dto = new tp.agil.backend.dtos.LicenciaExpiradaDTO();
            dto.setDocumentoTitular(l.getTitular().getNumeroDocumento());
            dto.setNombreTitular(l.getTitular().getNombre());
            dto.setApellidoTitular(l.getTitular().getApellido());
            return dto;
        });
        when(licenciaActivaRepository.findByFechaVencimientoBetweenOrderByFechaVencimientoDesc(any(), any()))
                .thenReturn(Collections.emptyList());

        var result = licenciaService.obtenerLicenciasVencidasEntre(hoy.minusDays(5), hoy);

        assertEquals("123", result.getExpiradas().get(0).getDocumentoTitular());
        assertEquals("Ana", result.getExpiradas().get(0).getNombreTitular());
        assertEquals("García", result.getExpiradas().get(0).getApellidoTitular());
    }

    // ========================= TEST LICENCIAS POR CRITERIOS

    @Test
    void testBuscarLicenciasPorCriterios_soloNombre() {
        LicenciaActiva lic = new LicenciaActiva();
        Titular titular = new Titular();
        titular.setNombre("Juan");
        lic.setTitular(titular);

        when(licenciaActivaRepository.findAll(org.mockito.ArgumentMatchers.<Specification<LicenciaActiva>>any()))
                .thenReturn(List.of(lic));
        when(licenciaActivaMapper.entityToDto(any())).thenAnswer(inv -> {
            LicenciaActiva l = inv.getArgument(0);
            LicenciaActivaDTO dto = new LicenciaActivaDTO();
            dto.setNombreTitular(l.getTitular().getNombre());
            return dto;
        });

        var result = licenciaService.buscarLicenciasPorCriterios("Juan", null, null, null);

        assertEquals(1, result.size());
        assertEquals("Juan", result.get(0).getNombreTitular());
    }

    @Test
    void testBuscarLicenciasPorCriterios_nombreYApellido() {
        LicenciaActiva lic = new LicenciaActiva();
        Titular titular = new Titular();
        titular.setNombre("Ana");
        titular.setApellido("García");
        lic.setTitular(titular);

        when(licenciaActivaRepository.findAll(org.mockito.ArgumentMatchers.<Specification<LicenciaActiva>>any()))
                .thenReturn(List.of(lic));
        when(licenciaActivaMapper.entityToDto(any())).thenAnswer(inv -> {
            LicenciaActiva l = inv.getArgument(0);
            LicenciaActivaDTO dto = new LicenciaActivaDTO();
            dto.setNombreTitular(l.getTitular().getNombre());
            dto.setApellidoTitular(l.getTitular().getApellido());
            return dto;
        });

        var result = licenciaService.buscarLicenciasPorCriterios("Ana", "García", null, null);

        assertEquals(1, result.size());
        assertEquals("Ana", result.get(0).getNombreTitular());
        assertEquals("García", result.get(0).getApellidoTitular());
    }

    @Test
    void testBuscarLicenciasPorCriterios_grupoFactorYDonante() {
        LicenciaActiva lic = new LicenciaActiva();
        Titular titular = new Titular();
        titular.setGrupoFactor("A+");
        titular.setDonanteOrganos(true);
        lic.setTitular(titular);

        when(licenciaActivaRepository.findAll(org.mockito.ArgumentMatchers.<Specification<LicenciaActiva>>any()))
                .thenReturn(List.of(lic));
        when(licenciaActivaMapper.entityToDto(any())).thenAnswer(inv -> {
            LicenciaActiva l = inv.getArgument(0);
            LicenciaActivaDTO dto = new LicenciaActivaDTO();
            dto.setGrupoFactor(l.getTitular().getGrupoFactor());
            dto.setDonanteOrganos(l.getTitular().isDonanteOrganos() ? "Si" : "No");
            return dto;
        });

        var result = licenciaService.buscarLicenciasPorCriterios(null, null, "A+", true);

        assertEquals(1, result.size());
        assertEquals("A+", result.get(0).getGrupoFactor());
        assertEquals("Si", result.get(0).getDonanteOrganos());
    }

    @Test
    void testBuscarLicenciasPorCriterios_sinResultados() {
        when(licenciaActivaRepository.findAll(org.mockito.ArgumentMatchers.<Specification<LicenciaActiva>>any()))
                .thenReturn(Collections.emptyList());

        var result = licenciaService.buscarLicenciasPorCriterios("NoExiste", null, null, null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testBuscarLicenciasPorCriterios_todosNulos() {
        LicenciaActiva lic = new LicenciaActiva();
        Titular titular = new Titular();
        titular.setNombre("Pedro");
        lic.setTitular(titular);

        when(licenciaActivaRepository.findAll(org.mockito.ArgumentMatchers.<Specification<LicenciaActiva>>any()))
                .thenReturn(List.of(lic));
        when(licenciaActivaMapper.entityToDto(any())).thenAnswer(inv -> {
            LicenciaActiva l = inv.getArgument(0);
            LicenciaActivaDTO dto = new LicenciaActivaDTO();
            dto.setNombreTitular(l.getTitular().getNombre());
            return dto;
        });

        var result = licenciaService.buscarLicenciasPorCriterios(null, null, null, null);

        assertEquals(1, result.size());
        assertEquals("Pedro", result.get(0).getNombreTitular());
    }
}