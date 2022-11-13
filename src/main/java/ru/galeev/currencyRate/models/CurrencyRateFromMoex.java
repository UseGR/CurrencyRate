package ru.galeev.currencyRate.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
@Builder
public class CurrencyRateFromMoex implements CurrencyRate {
    private String value;
    private LocalDateTime momentStart;
    private LocalDateTime momentEnd;
    private String charCode;
}
