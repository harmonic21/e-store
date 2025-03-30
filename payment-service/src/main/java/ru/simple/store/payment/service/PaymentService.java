package ru.simple.store.payment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final BalanceService balanceService;

    public boolean placeAnOrder(Double orderAmount) {
        return orderAmount <= balanceService.getCurrentBalance() && balanceService.withdraw(orderAmount);
    }
}
