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
    @OneToOne
    @JoinColumn(name = "id_product", referencedColumnName = "id")
    private ProductEntity product;
    @Column(name = "product_count")
    private Long productCount;

}
