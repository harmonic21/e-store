package ru.simple.electronic.store.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import ru.simple.electronic.store.entity.ProductEntity;

@Configuration
public class RedisConfiguration {

    @Bean
    public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory(@Value("${redis.hostname}") String hostname,
                                                                         @Value("${redis.port}") int port) {
        return new LettuceConnectionFactory(hostname, port);
    }

    @Bean
    public ReactiveRedisTemplate<String, ProductEntity> reactiveRedisTemplate(ReactiveRedisConnectionFactory reactiveRedisConnectionFactory) {
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<ProductEntity> valueSerializer = new Jackson2JsonRedisSerializer<>(ProductEntity.class);
        RedisSerializationContext.RedisSerializationContextBuilder<String, ProductEntity> builder = RedisSerializationContext.newSerializationContext(keySerializer);
        RedisSerializationContext<String, ProductEntity> context = builder.value(valueSerializer).build();
        return new ReactiveRedisTemplate<>(reactiveRedisConnectionFactory, context);
    }
}
