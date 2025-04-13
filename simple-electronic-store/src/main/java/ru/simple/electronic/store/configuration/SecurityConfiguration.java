package ru.simple.electronic.store.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) throws Exception {
        http
                .authorizeExchange(auth -> auth
                        .pathMatchers("/product/upload").hasRole("ADMIN")
                        .pathMatchers("/", "/products").permitAll()
                        .pathMatchers("/product/{id}").permitAll()
                        .pathMatchers("/user/registration").permitAll()
                        .anyExchange().hasAnyRole("USER", "ADMIN")
                )
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .formLogin(Customizer.withDefaults())
                .anonymous(ServerHttpSecurity.AnonymousSpec::disable);
        return http.build();
    }

    @Bean
    public ReactiveOAuth2AuthorizedClientManager auth2AuthorizedClientManager(ReactiveClientRegistrationRepository clientRegistrationRepository,
                                                                              ReactiveOAuth2AuthorizedClientService authorizedClientService) {
        AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager manager =
                new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientService);

        manager.setAuthorizedClientProvider(ReactiveOAuth2AuthorizedClientProviderBuilder.builder()
                .clientCredentials()
                .refreshToken()
                .build()
        );

        return manager;
    }
}
