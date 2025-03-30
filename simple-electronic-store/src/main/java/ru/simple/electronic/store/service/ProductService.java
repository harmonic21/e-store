package ru.simple.electronic.store.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.simple.electronic.store.dto.FiltrationDto;
import ru.simple.electronic.store.dto.ProductDto;
import ru.simple.electronic.store.mapper.ProductMapper;
import ru.simple.electronic.store.repository.ProductRepository;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductRedisCacheService productRedisCacheService;

    public Mono<List<ProductDto>> findAll(FiltrationDto filtrationDto) {
        Mono<List<ProductDto>> productList;
        if (StringUtils.isNotBlank(filtrationDto.getKeyWord())) {
            productList = productRedisCacheService.getAll(filtrationDto)
                    .filter(product -> product.getTitle().contains(filtrationDto.getKeyWord()) || product.getDescription().contains(filtrationDto.getKeyWord()))
                    .skip(filtrationDto.getPageNum().longValue() * filtrationDto.getPageSize())
                    .take(filtrationDto.getPageSize())
                    .map(productMapper::mapToProductDto)
                    .collectList();
        } else {
            productList = productRedisCacheService.getAll(filtrationDto)
                    .skip(filtrationDto.getPageNum().longValue() * filtrationDto.getPageSize())
                    .take(filtrationDto.getPageSize())
                    .map(productMapper::mapToProductDto)
                    .collectList();
        }
        return productList;
    }

    @Transactional
    public Mono<Void> saveNewProduct(Flux<ProductDto> products) {
        var productEntities = products.map(productMapper::mapToProductEntity)
                .map(productEntity -> productEntity.withId(UUID.randomUUID()));
        return productRepository.saveAll(productEntities).then();
    }

    public Mono<ProductDto> findById(UUID uuid) {
        return productRepository.findById(uuid).map(productMapper::mapToProductDto);
    }
}
