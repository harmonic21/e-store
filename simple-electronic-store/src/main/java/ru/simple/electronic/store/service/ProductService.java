package ru.simple.electronic.store.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.simple.electronic.store.dto.ProductDto;
import ru.simple.electronic.store.mapper.ProductMapper;
import ru.simple.electronic.store.repository.ProductRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private static final String BASE_64_IMAGE_TEMPLATE = "data:image/jpeg;base64,%s";

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public List<ProductDto> findAll(Integer pageNum,
                                    Integer pageSize,
                                    String keyWord,
                                    Boolean priceSortAsc,
                                    Boolean abcSortAsc,
                                    Boolean priceSortDesc,
                                    Boolean abcSortDesc) {
        Sort sort = applySort(priceSortAsc, abcSortAsc, priceSortDesc, abcSortDesc);
        List<ProductDto> productList;
        if (Objects.nonNull(keyWord)) {
            productList = productRepository.findAllAndFilter("%" + keyWord +"%", PageRequest.of(pageNum, pageSize, sort)).stream()
                    .map(productMapper::mapToProductDto)
                    .toList();
        } else {
            productList = productRepository.findAll((PageRequest.of(pageNum, pageSize, sort))).stream()
                    .map(productMapper::mapToProductDto)
                    .toList();
        }
        return productList;
    }

    @Transactional
    public void addProduct(ProductDto product, MultipartFile image) {
        product.setImage(imageToBase64(image));
        productRepository.save(productMapper.mapToProductEntity(product));
    }

    @Transactional
    public void saveNewProduct(List<ProductDto> products) {
        var productEntities = products.stream().map(productMapper::mapToProductEntity).toList();
        productRepository.saveAll(productEntities);
    }

    public ProductDto findById(UUID uuid) {
        return productRepository.findById(uuid).map(productMapper::mapToProductDto).orElse(null);
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

    private Sort applySort(Boolean priceSortAsc,
                           Boolean abcSortAsc,
                           Boolean priceSortDesc,
                           Boolean abcSortDesc) {
        List<Sort.Order> orders = new ArrayList<>();

        if (Objects.nonNull(priceSortAsc)) {
            orders.add(Sort.Order.asc("price"));
        }
        if (Objects.nonNull(abcSortAsc)) {
            orders.add(Sort.Order.asc("title"));
        }
        if (Objects.nonNull(priceSortDesc)) {
            orders.add(Sort.Order.desc("price"));
        }
        if (Objects.nonNull(abcSortDesc)) {
            orders.add(Sort.Order.desc("title"));
        }
        return Sort.by(orders);
    }
}
