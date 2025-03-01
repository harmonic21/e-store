package ru.simple.electronic.store.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.simple.electronic.store.dto.ProductOrderDto;
import ru.simple.electronic.store.mapper.ProductOrderMapper;
import ru.simple.electronic.store.repository.ProductOrderRepository;

@Service
@RequiredArgsConstructor
public class ProductOrderService {

    private static final ProductOrderDto EMPTY_ORDER = new ProductOrderDto();

    private final ProductOrderRepository productOrderRepository;
    private final ProductOrderMapper productOrderMapper;

    public ProductOrderDto findCurrentOrder() {
        return productOrderRepository.findProductOrderByStatus("NEW")
                .map(productOrderMapper::mapToDto)
                .orElse(EMPTY_ORDER);
    }
}
