package ru.galeev.currencyRate.models;

import lombok.Value;

import java.util.List;

@Value
public class CachedCurrencyRatesFromMoex {
    private List<CurrencyRateFromMoex> currencyRates;
}
