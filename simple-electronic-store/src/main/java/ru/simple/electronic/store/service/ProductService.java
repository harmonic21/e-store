package ru.simple.electronic.store.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
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
    private static final String LIKE_QUERY_TEMPLATE = "%%%s%%";

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    public Mono<List<ProductDto>> findAll(Integer pageNum,
                                    Integer pageSize,
                                    String keyWord,
                                    Boolean priceSortAsc,
                                    Boolean abcSortAsc,
                                    Boolean priceSortDesc,
                                    Boolean abcSortDesc) {
        Sort sort = applySort(priceSortAsc, abcSortAsc, priceSortDesc, abcSortDesc);
        Mono<List<ProductDto>> productList;
        if (StringUtils.isNotBlank(keyWord)) {
            productList = productRepository.findByTitleLikeOrDescriptionLike(
                            LIKE_QUERY_TEMPLATE.formatted(keyWord),
                            LIKE_QUERY_TEMPLATE.formatted(keyWord),
                            sort
                    )
                    .skip(pageNum.longValue() * pageSize)
                    .take(pageSize)
                    .map(productMapper::mapToProductDto)
                    .collectList();
        } else {
            productList = productRepository.findAll(sort)
                    .skip(pageNum.longValue() * pageSize)
                    .take(pageSize)
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
