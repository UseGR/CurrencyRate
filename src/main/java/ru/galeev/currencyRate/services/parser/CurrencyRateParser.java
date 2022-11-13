package ru.galeev.currencyRate.services.parser;

import ru.galeev.currencyRate.models.CurrencyRate;

import java.util.List;

public interface CurrencyRateParser {
    List<? extends CurrencyRate> parseRate(String ratesAsString);
}
