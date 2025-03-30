package ru.simple.electronic.store.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.simple.store.payment.service.ApiClient;
import ru.simple.store.payment.service.api.BalanceApi;
import ru.simple.store.payment.service.api.PaymentApi;

@Configuration
public class ClientConfig {

    @Bean
    public ApiClient paymentClient(@Value("${client.payment-service.url}") String paymentUrl) {
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(paymentUrl);
        return apiClient;
    }

    @Bean
    public BalanceApi balanceApi(ApiClient paymentClient) {
        return new BalanceApi(paymentClient);
    }

    @Bean
    public PaymentApi paymentApi(ApiClient paymentClient) {
        return new PaymentApi(paymentClient);
    }
}
