package ru.simple.electronic.store.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import reactor.util.function.Tuple2;
import ru.simple.electronic.store.dto.BasketDto;
import ru.simple.electronic.store.dto.ProductDto;
import ru.simple.electronic.store.entity.BasketEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BasketMapper {

//    @Mapping(target = "productInfo", source = "product")
    BasketDto mapToDto(BasketEntity entity);
    @Mapping(target = ".", source = "source.t1")
    @Mapping(target = "productInfo", source = "source.t2")
    BasketDto mapToDto(Tuple2<BasketEntity, ProductDto> source);
}
