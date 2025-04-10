package ru.simple.electronic.store.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDto {
    @NotNull
    @NotEmpty
    private String username;
    @NotNull
    @NotEmpty
    @Size(min = 6)
    private String password;
}
