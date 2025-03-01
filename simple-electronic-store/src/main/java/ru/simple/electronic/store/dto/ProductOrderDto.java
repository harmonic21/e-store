package ru.simple.electronic.store.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class ProductOrderDto {

    private String id;
    private BigDecimal orderSum;
    private String status;

    public UUID getIdAsUuid() {
        return Optional.ofNullable(id).map(UUID::fromString).orElse(null);
    }
}
