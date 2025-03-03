package ru.simple.electronic.store.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.simple.electronic.store.dto.ProductOrderDto;
import ru.simple.electronic.store.entity.ProductOrderEntity;
import ru.simple.electronic.store.mapper.ProductOrderMapper;
import ru.simple.electronic.store.repository.ProductOrderRepository;

@Service
@RequiredArgsConstructor
public class ProductOrderService {

    private static final ProductOrderDto EMPTY_ORDER = new ProductOrderDto();

    private final ProductOrderRepository productOrderRepository;
    private final ProductOrderMapper productOrderMapper;

    @Transactional
    public ProductOrderDto findCurrentOrderOrCreateNew() {
        return productOrderRepository.findProductOrderByStatus("NEW")
                .map(productOrderMapper::mapToDto)
                .orElseGet(this::createNewOrder);
    }

    private ProductOrderDto createNewOrder() {
        ProductOrderEntity orderEntity = new ProductOrderEntity();
        orderEntity.setStatus("NEW");
        return productOrderMapper.mapToDto(productOrderRepository.save(orderEntity));
    }
}
