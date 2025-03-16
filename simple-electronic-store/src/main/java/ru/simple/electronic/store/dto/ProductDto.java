package ru.simple.electronic.store.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class ProductDto {

    private UUID id;
    private String title;
    private BigDecimal price;
    private String description;
    private String image;
}
