package ru.galeev.currencyRate.dto;

import lombok.Data;
import ru.galeev.currencyRate.models.CurrencyRateFromCbr;

import java.time.LocalDate;

@Data
public class CurrencyRateFromCbrDto {
    private String charCode, value;
    private LocalDate onDate;

    public static CurrencyRateFromCbrDto fromCurrencyCbrRate(CurrencyRateFromCbr currencyRate) {
        CurrencyRateFromCbrDto currencyRateDto = new CurrencyRateFromCbrDto();
        currencyRateDto.setValue(currencyRate.getValue());
        currencyRateDto.setCharCode(currencyRate.getCharCode());
        return currencyRateDto;
    }
}
