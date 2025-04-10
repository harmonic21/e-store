package ru.simple.electronic.store.service;

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

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .log()
                .map(
                        user -> User.builder()
                                .username(user.getUsername())
                                .password(user.getPassword())
                                .roles(user.getRoles())
                                .build()
                ).log();
    }

    public Mono<UserEntity> saveNewUser(UserDto userDto) {
        UserEntity user = new UserEntity().withId(UUID.randomUUID());
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRoles(new String[]{"USER"});
        return userRepository.save(user);
    }
}
