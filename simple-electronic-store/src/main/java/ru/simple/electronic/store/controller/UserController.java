package ru.simple.electronic.store.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;
import ru.simple.electronic.store.dto.UserDto;
import ru.simple.electronic.store.service.UserService;

@Controller
@Validated
@RequestMapping(value = "/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/registration")
    public Mono<Rendering> getRegistrationForm() {
        return Mono.defer(
                        () -> Mono.just(Rendering.view("registration")
                                .modelAttribute("user", new UserDto()))
                )
                .map(Rendering.Builder::build);
    }

    @PostMapping("/registration")
    public Mono<Rendering> registerNewUser(@ModelAttribute(name = "user") @Valid UserDto userDto) {
        return userService.saveNewUser(userDto, "USER")
                .then(Mono.just(Rendering.redirectTo("/").build()));
    }
}
