package ru.simple.electronic.store.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class ProductDto {

    private String id;
    private String title;
    private BigDecimal price;
    private String description;
    private String image;
}
