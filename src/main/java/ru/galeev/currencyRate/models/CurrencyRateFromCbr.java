package ru.galeev.currencyRate.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
@Builder
public class CurrencyRateFromCbr implements CurrencyRate {
    private String numCode, charCode, nominal, name, value;
}
