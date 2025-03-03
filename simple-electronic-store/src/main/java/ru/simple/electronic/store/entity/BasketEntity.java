package ru.simple.electronic.store.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "basket")
public class BasketEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;
    @Column(name = "product_count")
    private Long productCount = 0L;
    @ManyToOne
    @JoinColumn(name = "id_product", referencedColumnName = "id")
    private ProductEntity product;
    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private ProductOrderEntity order;

}
