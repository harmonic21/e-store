package ru.simple.store.payment.service;

import org.springframework.stereotype.Service;

@Service
public class BalanceService {

    private static Double CURRENT_BALANCE = 2541.12;

    public Double getCurrentBalance() {
        return CURRENT_BALANCE;
    }

    public boolean withdraw(Double amount) {
        CURRENT_BALANCE -= amount;
        return true;
    }
}
