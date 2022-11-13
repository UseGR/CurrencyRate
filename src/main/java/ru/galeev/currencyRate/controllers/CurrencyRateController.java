package ru.galeev.currencyRate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.galeev.currencyRate.dto.CurrencyRateFromCbrDto;
import ru.galeev.currencyRate.dto.CurrencyRateFromMoexDto;
import ru.galeev.currencyRate.services.CurrencyRateService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "${app.rest.api.prefix}/v1")
public class CurrencyRateController {
    private final CurrencyRateService currencyRateService;

    @GetMapping("/currencyRateFromCbr/{currency}")
    public CurrencyRateFromCbrDto getCurrencyRate(@PathVariable("currency") String currency,
                                                  @DateTimeFormat(pattern = "dd-MM-yyyy") @RequestParam("date") LocalDate date) {
        log.info("getCurrencyRate, currency:{}, date:{}", currency, date);
        CurrencyRateFromCbrDto rate = CurrencyRateFromCbrDto.fromCurrencyCbrRate(currencyRateService.getCurrencyRateFromCbr(currency, date));
        rate.setOnDate(date);
        log.info("rate: {}", rate);

        return rate;
    }

    @GetMapping("currencyRateFromMoex/{currency}")
    public List<CurrencyRateFromMoexDto> getCurrencyRate(@PathVariable("currency") String currency,
                                                   @DateTimeFormat(pattern = "dd-MM-yyyy") @RequestParam("momentStart") LocalDate momentStart,
                                                   @DateTimeFormat(pattern = "dd-MM-yyyy") @RequestParam("momentEnd") LocalDate momentEnd) {
        log.info("getCurrencyRate, currency:{}, momentStart:{}, momentEnd:{}", currency, momentStart, momentEnd);
        return currencyRateService.getCurrencyRateFromMoex(currency, momentStart, momentEnd).stream()
                .map(CurrencyRateFromMoexDto::fromCurrencyMoexRate)
                .peek(i -> i.setCharCode(currency))
                .collect(Collectors.toList());
    }
}
