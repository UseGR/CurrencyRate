package ru.galeev.currencyRate.services;

public class CurrencyRateNotFoundException extends RuntimeException{
    public CurrencyRateNotFoundException(String message) {
        super(message);
    }
}
