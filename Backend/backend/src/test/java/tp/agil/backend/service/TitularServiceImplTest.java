package tp.agil.backend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tp.agil.backend.dtos.TitularDTO;
import tp.agil.backend.dtos.TitularClasesDTO;
import tp.agil.backend.entities.LicenciaExpirada;
import tp.agil.backend.entities.Titular;
import tp.agil.backend.exceptions.TitularExistenteException;
import tp.agil.backend.exceptions.TitularNoEncontradoException;
import tp.agil.backend.mappers.TitularMapper;
import tp.agil.backend.repositories.TitularRepository;
import tp.agil.backend.services.TitularServiceImpl;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TitularServiceImplTest {

    @InjectMocks
    private TitularServiceImpl titularService;

    @Mock
    private TitularRepository titularRepository;

    @Mock
    private TitularMapper titularMapper;

    private TitularDTO sampleDTO;
    private Titular sampleEntity;

    @BeforeEach
    void setup() {
        sampleDTO = new TitularDTO();
        sampleDTO.setNumeroDocumento("12345678");
        sampleDTO.setNombre("Juan");
        sampleDTO.setApellido("Pérez");
        sampleDTO.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        sampleDTO.setDomicilio("Calle Falsa 123");
        sampleDTO.setGrupoFactor("0+");
        sampleDTO.setDonanteOrganos(true);

        sampleEntity = new Titular();
        sampleEntity.setNumeroDocumento("12345678");
        sampleEntity.setNombre("Juan");
        sampleEntity.setApellido("Pérez");
        sampleEntity.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        sampleEntity.setDomicilio("Calle Falsa 123");
        sampleEntity.setGrupoFactor("0+");
        sampleEntity.setDonanteOrganos(true);
    }

    @Test
    void testCrearTitular_ok() {
        when(titularRepository.findByNumeroDocumento("12345678")).thenReturn(null);
        when(titularMapper.dtoToEntity(sampleDTO)).thenReturn(sampleEntity);
        when(titularRepository.save(sampleEntity)).thenReturn(sampleEntity);
        when(titularMapper.entityToDto(sampleEntity)).thenReturn(sampleDTO);

        TitularDTO result = titularService.crearTitular(sampleDTO);

        assertEquals("12345678", result.getNumeroDocumento());
        verify(titularRepository).save(sampleEntity);
    }

    @Test
    void testCrearTitular_duplicado_lanzaExcepcion() {
        when(titularRepository.findByNumeroDocumento("12345678")).thenReturn(sampleEntity);

        assertThrows(TitularExistenteException.class, () -> titularService.crearTitular(sampleDTO));
        verify(titularRepository, never()).save(any());
    }

    @Test
    void testGetTitularById_ok() {
        when(titularRepository.findByNumeroDocumento("12345678")).thenReturn(sampleEntity);
        when(titularMapper.entityToDto(sampleEntity)).thenReturn(sampleDTO);

        TitularDTO result = titularService.getTitularById("12345678");

        assertEquals("12345678", result.getNumeroDocumento());
    }

    @Test
    void testGetTitularById_noExiste_lanzaExcepcion() {
        when(titularRepository.findByNumeroDocumento("99999999")).thenReturn(null);

        assertThrows(TitularNoEncontradoException.class, () -> titularService.getTitularById("99999999"));
    }

    @Test
    void testGetTitularConClases_menorA17_retornaSoloA_B_F_G() {
        Titular menor = new Titular();
        menor.setNumeroDocumento("11111111");
        menor.setFechaNacimiento(LocalDate.now().minusYears(17).minusDays(1));

        when(titularRepository.findByNumeroDocumento("11111111")).thenReturn(menor);
        when(titularMapper.entityToDto(menor)).thenReturn(new TitularDTO());

        TitularClasesDTO dto = titularService.getTitularConClases("11111111");
        assertEquals("A B F G", dto.getClases());
    }

    @Test
    void testGetTitularConClases_profesionalConAntiguedad_retornaTodas() {
        Titular titular = new Titular();
        titular.setNumeroDocumento("22222222");
        titular.setFechaNacimiento(LocalDate.now().minusYears(30));

        LicenciaExpirada exp = new LicenciaExpirada();
        exp.setClases("B");
        exp.setFechaEmision(LocalDate.now().minusYears(2));
        titular.setLicenciasExpiradas(List.of(exp));

        when(titularRepository.findByNumeroDocumento("22222222")).thenReturn(titular);
        when(titularMapper.entityToDto(titular)).thenReturn(new TitularDTO());

        TitularClasesDTO dto = titularService.getTitularConClases("22222222");
        assertTrue(dto.getClases().contains("C"));
        assertTrue(dto.getClases().contains("D"));
        assertTrue(dto.getClases().contains("E"));
    }

    @Test
    void testGetTitularConClases_menorA17_noRetornaClases() {
        Titular menor16 = new Titular();
        menor16.setNumeroDocumento("22222222");
        menor16.setFechaNacimiento(LocalDate.now().minusYears(16));

        when(titularRepository.findByNumeroDocumento("22222222")).thenReturn(menor16);
        when(titularMapper.entityToDto(menor16)).thenReturn(new TitularDTO());

        TitularClasesDTO dto = titularService.getTitularConClases("22222222");

        assertEquals("", dto.getClases());
    }

    @Test
    void testGetTitularConClases_mayor65_noIncluyeProfesionales() {
        Titular titular = new Titular();
        titular.setNumeroDocumento("33333333");
        titular.setFechaNacimiento(LocalDate.now().minusYears(70));

        LicenciaExpirada exp = new LicenciaExpirada();
        exp.setClases("B");
        exp.setFechaEmision(LocalDate.now().minusYears(10));
        titular.setLicenciasExpiradas(List.of(exp));

        when(titularRepository.findByNumeroDocumento("33333333")).thenReturn(titular);
        when(titularMapper.entityToDto(titular)).thenReturn(new TitularDTO());

        TitularClasesDTO dto = titularService.getTitularConClases("33333333");
        assertFalse(dto.getClases().contains("C"));
        assertFalse(dto.getClases().contains("D"));
        assertFalse(dto.getClases().contains("E"));
    }
}