package ru.simple.electronic.store.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.simple.electronic.store.dto.ProductOrderDto;
import ru.simple.electronic.store.entity.ProductOrderEntity;
import ru.simple.electronic.store.mapper.ProductOrderMapper;
import ru.simple.electronic.store.repository.ProductOrderRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductOrderService {

    private final ProductOrderRepository productOrderRepository;
    private final ProductOrderMapper productOrderMapper;

    @Transactional
    public ProductOrderDto findCurrentOrderOrCreateNew() {
        return productOrderRepository.findProductOrderByStatus("NEW").blockOptional()
                .map(productOrderMapper::mapToDto)
                .orElseGet(this::createNewOrder);
    }

    public List<ProductOrderDto> findAllOrdersInStatusDone() {
        return productOrderRepository.findAllProductOrderByStatus("DONE").collectList().block().stream()
                .map(productOrderMapper::mapToDto)
                .toList();
    }

    @Transactional
    public void placeAnOrder() {
        productOrderRepository.findProductOrderByStatus("NEW").blockOptional()
                .ifPresent(order -> {
                    var totalOrderSum = CollectionUtils.emptyIfNull(order.getOrderItems()).stream()
                            .map(basket -> basket.getProduct().getPrice().multiply(BigDecimal.valueOf(basket.getProductCount())))
                            .reduce(BigDecimal::add)
                            .orElse(BigDecimal.ZERO);
                    order.setOrderSum(totalOrderSum);
                    order.setStatus("DONE");
                    productOrderRepository.save(order);
                });
    }

    public ProductOrderDto getDetailInfoById(UUID id) {
        return productOrderRepository.findById(id).map(productOrderMapper::mapToDto).blockOptional().orElse(null);
    }

    private ProductOrderDto createNewOrder() {
        ProductOrderEntity orderEntity = new ProductOrderEntity();
        orderEntity.setStatus("NEW");
        return productOrderMapper.mapToDto(productOrderRepository.save(orderEntity).block());
    }
}
