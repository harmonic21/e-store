package ru.simple.electronic.store.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
@Table(name = "product_order")
@NoArgsConstructor
public class ProductOrderEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;
    @Column(name = "order_sum")
    private BigDecimal orderSum = BigDecimal.ZERO;
    @Column(name = "status")
    private String status;
}
