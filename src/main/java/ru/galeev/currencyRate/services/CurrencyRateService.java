package ru.galeev.currencyRate.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ehcache.Cache;
import org.springframework.stereotype.Service;
import ru.galeev.currencyRate.config.CbrConfig;
import ru.galeev.currencyRate.config.MoexConfig;
import ru.galeev.currencyRate.models.CachedCurrencyRatesFromCbr;
import ru.galeev.currencyRate.models.CachedCurrencyRatesFromMoex;
import ru.galeev.currencyRate.models.CurrencyRateFromCbr;
import ru.galeev.currencyRate.models.CurrencyRateFromMoex;
import ru.galeev.currencyRate.services.parser.CbrCurrencyRateParserXml;
import ru.galeev.currencyRate.services.parser.MoexCurrencyRateParserXml;
import ru.galeev.currencyRate.services.requester.Requester;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CurrencyRateService {
    private static final String DATE_FORMAT_CBR = "dd/MM/yyyy";
    private static final DateTimeFormatter DATE_FORMATTER_CBR = DateTimeFormatter.ofPattern(DATE_FORMAT_CBR);

    private static final String DATE_FORMAT_MOEX = "yyyy-MM-dd";
    private static final DateTimeFormatter DATE_FORMATTER_MOEX = DateTimeFormatter.ofPattern(DATE_FORMAT_MOEX);

    private final Requester requester;
    private final CbrCurrencyRateParserXml cbrCurrencyRateParserXml;
    private final MoexCurrencyRateParserXml moexCurrencyRateParserXml;
    private final CbrConfig cbrConfig;
    private final MoexConfig moexConfig;
    private final Cache<LocalDate, CachedCurrencyRatesFromCbr> currencyRateFromCbrCache;
    private final Cache<LocalDate, CachedCurrencyRatesFromMoex> currencyRateFromMoexCache;


    public CurrencyRateFromCbr getCurrencyRateFromCbr(String currency, LocalDate date) {
        log.info("getCurrencyRate. currency:{}, date:{}", currency, date);
        List<CurrencyRateFromCbr> rates;

        CachedCurrencyRatesFromCbr cachedCurrencyRates =  currencyRateFromCbrCache.get(date);
        if (cachedCurrencyRates == null) {
            String urlWithParams = String.format("%s?date_req=%s", cbrConfig.getUrl(), DATE_FORMATTER_CBR.format(date));
            String ratesAsXml = requester.getRatesAsXml(urlWithParams);
            rates = cbrCurrencyRateParserXml.parseRate(ratesAsXml);
            currencyRateFromCbrCache.put(date, new CachedCurrencyRatesFromCbr(rates));
        } else {
            rates = cachedCurrencyRates.getCurrencyRates();
        }

        return rates.stream().filter(rate -> currency.equals(rate.getCharCode()))
                .findFirst()
                .orElseThrow(() -> new CurrencyRateNotFoundException("Currency Rate not found. Currency:" + currency + ", date:" + date));
    }

    public List<CurrencyRateFromMoex> getCurrencyRateFromMoex(String currency, LocalDate momentStart, LocalDate momentEnd) {
        log.info("getCurrencyRate. currency:{}, momentStart:{}, momentEnd:{}", currency, momentStart, momentEnd);
        List<CurrencyRateFromMoex> rates;

        CachedCurrencyRatesFromMoex cachedCurrencyRates =  currencyRateFromMoexCache.get(momentStart);
        if (cachedCurrencyRates == null) {
            String urlWithParams = String.format("%s?language=en&currency=%s_RUB&moment_start=%s&moment_end=%s", moexConfig.getUrl(), currency, DATE_FORMATTER_MOEX.format(momentStart), DATE_FORMATTER_MOEX.format(momentEnd));
            String ratesAsXml = requester.getRatesAsXml(urlWithParams);
            rates = moexCurrencyRateParserXml.parseRate(ratesAsXml);
            currencyRateFromMoexCache.put(momentStart, new CachedCurrencyRatesFromMoex(rates));
        } else {
            rates = cachedCurrencyRates.getCurrencyRates();
        }

        return rates;
    }
}
