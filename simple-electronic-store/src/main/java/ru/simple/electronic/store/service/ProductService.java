package ru.simple.electronic.store.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.simple.electronic.store.dto.ProductDto;
import ru.simple.electronic.store.mapper.ProductMapper;
import ru.simple.electronic.store.repository.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public List<ProductDto> findAll() {
        return productRepository.findAll().stream()
                .map(productMapper::mapToProductDto)
                .toList();
    }
}
