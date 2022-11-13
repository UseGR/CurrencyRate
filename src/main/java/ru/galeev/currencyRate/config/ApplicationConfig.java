package ru.galeev.currencyRate.config;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.galeev.currencyRate.models.CachedCurrencyRatesFromCbr;
import ru.galeev.currencyRate.models.CachedCurrencyRatesFromMoex;

import java.time.LocalDate;

@Configuration
@EnableConfigurationProperties({CbrConfig.class, MoexConfig.class})
public class ApplicationConfig {
    private final CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build(true);

    @Bean
    public Cache<LocalDate, CachedCurrencyRatesFromCbr> currencyRateFromCbrCache(@Value("${app.cache.size}") int cacheSize) {
        return cacheManager.createCache("CurrencyCbrRate-Cache",
                CacheConfigurationBuilder.newCacheConfigurationBuilder(LocalDate.class, CachedCurrencyRatesFromCbr.class,
                        ResourcePoolsBuilder.heap(cacheSize))
                        .build());
    }

    @Bean
    public Cache<LocalDate, CachedCurrencyRatesFromMoex> currencyRateFromMoexCache(@Value("${app.cache.size}") int cacheSize) {
        return cacheManager.createCache("CurrencyMoexRate-Cache",
                CacheConfigurationBuilder.newCacheConfigurationBuilder(LocalDate.class, CachedCurrencyRatesFromMoex.class,
                                ResourcePoolsBuilder.heap(cacheSize))
                        .build());
    }
}
