package tp.agil.backend.mappers;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import tp.agil.backend.dtos.TitularDTO;
import tp.agil.backend.entities.Titular;

/**
 * @author Marcos Debona
 */

@Mapper(componentModel = "spring")
public interface TitularMapper {
    TitularDTO entityToDto(Titular titular);
    Titular dtoToEntity(TitularDTO titularDTO);
}