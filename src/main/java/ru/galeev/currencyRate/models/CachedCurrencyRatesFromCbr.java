package ru.galeev.currencyRate.models;

import lombok.Value;

import java.util.List;

@Value
public class CachedCurrencyRatesFromCbr {
    private List<CurrencyRateFromCbr> currencyRates;
}
