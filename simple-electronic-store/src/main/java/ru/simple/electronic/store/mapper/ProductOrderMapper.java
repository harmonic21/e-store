package ru.simple.electronic.store.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.simple.electronic.store.dto.ProductOrderDto;
import ru.simple.electronic.store.entity.ProductOrderEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductOrderMapper {

    ProductOrderDto mapToDto(ProductOrderEntity entity);
}
