package ru.simple.electronic.store.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.simple.electronic.store.dto.UserDto;
import ru.simple.electronic.store.entity.UserEntity;
import ru.simple.electronic.store.repository.UserRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements ReactiveUserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void addAdmin() {
        userRepository.findByUsername("admin")
                .switchIfEmpty(saveNewUser(new UserDto().setUsername("admin").setPassword("admin"), "ADMIN"))
                .subscribe();
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(
                        user -> User.builder()
                                .username(user.getUsername())
                                .password(user.getPassword())
                                .roles(user.getRoles())
                                .build()
                );
    }

    public Mono<UserEntity> saveNewUser(UserDto userDto, String... roles) {
        UserEntity user = new UserEntity().withId(UUID.randomUUID());
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRoles(roles);
        return userRepository.save(user);
    }
}
