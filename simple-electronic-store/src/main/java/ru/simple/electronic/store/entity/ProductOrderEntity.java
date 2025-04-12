package ru.simple.electronic.store.entity;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
@Table(name = "product_order")
@NoArgsConstructor
public class ProductOrderEntity implements Persistable<UUID> {

    @Id
    @Column(value = "id")
    private UUID id;
    @Column(value = "order_sum")
    private BigDecimal orderSum = BigDecimal.ZERO;
    @Column(value = "status")
    private String status;
    @Column(value = "username")
    private String username;

    @Transient
    private boolean isNew = false;

    public ProductOrderEntity withId(UUID id) {
        this.id = id;
        this.isNew = true;
        return this;
    }

    public ProductOrderEntity withOrderSum(BigDecimal orderSum) {
        this.orderSum = orderSum;
        return this;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }
}
