package tp.agil.backend.mappers;

import org.mapstruct.Mapper;
import tp.agil.backend.dtos.TitularDTO;
import tp.agil.backend.entities.Titular;

@Mapper(componentModel = "spring")
public interface TitularMapper {
    TitularDTO entityToDto(Titular titular);
    Titular dtoToEntity(TitularDTO titularDTO);
}