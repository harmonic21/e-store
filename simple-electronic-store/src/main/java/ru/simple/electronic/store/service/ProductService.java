package ru.simple.electronic.store.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.simple.electronic.store.dto.FiltrationDto;
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

    public Mono<List<ProductDto>> findAll(FiltrationDto filtrationDto) {
        Sort sort = applySort(filtrationDto);
        Mono<List<ProductDto>> productList;
        if (StringUtils.isNotBlank(filtrationDto.getKeyWord())) {
            productList = productRepository.findByTitleLikeOrDescriptionLike(
                            LIKE_QUERY_TEMPLATE.formatted(filtrationDto.getKeyWord()),
                            LIKE_QUERY_TEMPLATE.formatted(filtrationDto.getKeyWord()),
                            sort
                    )
                    .skip(filtrationDto.getPageNum().longValue() * filtrationDto.getPageSize())
                    .take(filtrationDto.getPageSize())
                    .map(productMapper::mapToProductDto)
                    .collectList();
        } else {
            productList = productRepository.findAll(sort)
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

    private String imageToBase64(MultipartFile file) {
        try {
            var encodedImage = new String(Base64.getEncoder().encode(file.getBytes()), StandardCharsets.UTF_8);
            return encodedImage.isBlank() ? encodedImage : BASE_64_IMAGE_TEMPLATE.formatted(encodedImage);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private Sort applySort(FiltrationDto filtrationDto) {
        List<Sort.Order> orders = new ArrayList<>();

        if (BooleanUtils.isTrue(filtrationDto.isPriceSortAsc())) {
            orders.add(Sort.Order.asc("price"));
        }
        if (BooleanUtils.isTrue(filtrationDto.isAbcSortAsc())) {
            orders.add(Sort.Order.asc("title"));
        }
        if (BooleanUtils.isTrue(filtrationDto.isPriceSortDesc())) {
            orders.add(Sort.Order.desc("price"));
        }
        if (BooleanUtils.isTrue(filtrationDto.isAbcSortDesc())) {
            orders.add(Sort.Order.desc("title"));
        }
        return Sort.by(orders);
    }
}
