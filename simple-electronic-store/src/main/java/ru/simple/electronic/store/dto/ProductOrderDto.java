package ru.simple.electronic.store.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class ProductOrderDto {

    private UUID id;
    private BigDecimal orderSum;
    private String status;
    private List<BasketDto> orderItems;

    public BigDecimal getCurrentSum() {
        return CollectionUtils.emptyIfNull(orderItems).stream()
                .map(basket -> basket.getProductInfo().getPrice().multiply(BigDecimal.valueOf(basket.getProductCount())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }
}
