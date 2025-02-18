package ru.simple.electronic.store.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ProductDto {

    private String id;
    private String title;
    private BigDecimal price;
    private String description;
    private String image;
}
