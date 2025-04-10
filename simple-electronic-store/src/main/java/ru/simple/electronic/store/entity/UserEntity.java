package ru.simple.electronic.store.entity;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Entity
@Table(name = "e_store_user")
@NoArgsConstructor
public class UserEntity implements Persistable<UUID> {

    @Id
    @Column(value = "id")
    private UUID id;
    @Column(value = "username")
    private String username;
    @Column(value = "password")
    private String password;
    @Column(value = "roles")
    private String[] roles;

    @Transient
    private boolean isNew = false;

    public UserEntity withId(UUID id) {
        this.id = id;
        this.isNew = true;
        return this;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }
}
