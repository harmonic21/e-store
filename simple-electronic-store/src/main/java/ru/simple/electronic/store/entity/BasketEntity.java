package ru.simple.electronic.store.entity;

import jakarta.persistence.Entity;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Entity
@Table(name = "basket")
public class BasketEntity implements Persistable<UUID> {

    @Id
    @Column(value = "id")
    private UUID id;
    @Column(value = "product_count")
    private Long productCount = 0L;
    @Column(value = "id_product")
    private UUID productId;
    @Column(value = "order_id")
    private UUID orderId;

    @Transient
    private boolean isNew = false;

    public BasketEntity withId(UUID id) {
        this.id = id;
        this.isNew = true;
        return this;
    }

    public BasketEntity withProductId(UUID id) {
        this.productId = id;
        return this;
    }

    public BasketEntity withOrderId(UUID id) {
        this.orderId = id;
        return this;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }
}
