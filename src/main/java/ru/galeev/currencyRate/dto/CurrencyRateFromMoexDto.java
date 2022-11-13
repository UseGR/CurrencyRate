package ru.galeev.currencyRate.dto;

import lombok.Data;
import ru.galeev.currencyRate.models.CurrencyRateFromMoex;

import java.time.format.DateTimeFormatter;

@Data
public class CurrencyRateFromMoexDto {
    private String charCode, value;
    private String onDate;

    public static CurrencyRateFromMoexDto fromCurrencyMoexRate(CurrencyRateFromMoex currencyRate) {
        CurrencyRateFromMoexDto currencyRateDto = new CurrencyRateFromMoexDto();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        currencyRateDto.setValue(currencyRate.getValue());
        currencyRateDto.setOnDate(dtf.format(currencyRate.getMomentStart()));
        return currencyRateDto;
    }
}
