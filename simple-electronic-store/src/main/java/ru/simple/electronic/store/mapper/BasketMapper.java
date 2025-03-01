package ru.simple.electronic.store.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.simple.electronic.store.dto.BasketDto;
import ru.simple.electronic.store.entity.BasketEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BasketMapper {

    @Mapping(target = "productId", source = "product.id")
    BasketDto mapToDto(BasketEntity entity);
}
