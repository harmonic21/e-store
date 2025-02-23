package ru.simple.electronic.store.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.simple.electronic.store.dto.ProductDto;
import ru.simple.electronic.store.entity.ProductEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {

    ProductDto mapToProductDto(ProductEntity source);

    ProductEntity mapToProductEntity(ProductDto source);
}
