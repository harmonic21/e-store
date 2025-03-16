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
@Table(name = "product")
@NoArgsConstructor
public class ProductEntity implements Persistable<UUID> {

    @Id
    @Column(value = "id")
    private UUID id;
    @Column(value = "title")
    private String title;
    @Column(value = "price")
    private BigDecimal price;
    @Column(value = "description")
    private String description;
    @Column(value = "image")
    private String image;

    @Transient
    private boolean isNew = false;

    public ProductEntity withId(UUID id) {
        this.id = id;
        this.isNew = true;
        return this;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }
}
