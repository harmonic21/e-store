package ru.simple.electronic.store.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.simple.electronic.store.dto.ProductDto;
import ru.simple.electronic.store.mapper.ProductMapper;
import ru.simple.electronic.store.repository.ProductRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private static final String BASE_64_IMAGE_TEMPLATE = "data:image/jpeg;base64,%s";

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public List<ProductDto> findAll() {
        return productRepository.findAll().stream()
                .map(productMapper::mapToProductDto)
                .toList();
    }

    @Transactional
    public void addProduct(ProductDto product, MultipartFile image) {
        product.setImage(imageToBase64(image));
        productRepository.save(productMapper.mapToProductEntity(product));
    }

    private String imageToBase64(MultipartFile file) {
        try {
            var encodedImage = new String(Base64.getEncoder().encode(file.getBytes()), StandardCharsets.UTF_8);
            return encodedImage.isBlank() ? encodedImage : BASE_64_IMAGE_TEMPLATE.formatted(encodedImage);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
