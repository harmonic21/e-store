package ru.simple.store.authorization.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "user")
public class UserEntity {

    @Id
    @Column(name = "id")
    private UUID id;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "roles")
    private String[] roles;
}
