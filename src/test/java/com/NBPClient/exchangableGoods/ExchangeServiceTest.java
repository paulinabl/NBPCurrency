package com.NBPClient.exchangableGoods;

import com.NBPClient.Properties;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
class ExchangeServiceTest {

    @Mock private Properties properties;
    RestTemplate restTemplate = new RestTemplateBuilder().build();
    private ExchangeService sut;

    @BeforeEach
    void setUp() {
        sut = new ExchangeService(restTemplate,properties);
    }

    @Test
    public void shouldReturnProductsFromWeb(){
        String productName = "cenyzlota";
        String topCount = "14";
        String url = String.format("http://api.nbp.pl/api/%s/last/%s/?format=json", productName, topCount);
        List<Product> products = sut.getProducts(url);
        assertThat(products.size()).isEqualTo(Integer.parseInt(topCount));
    }

    @Test
    public void shouldReturnCurrenciesFromWeb(){
        String currencyTo = "USD";
        int days = 5;
        String url = String.format("http://api.nbp.pl/api/exchangerates/rates/a/%s/last/%d/?format=json", currencyTo, days);
        CurrencyExchange exchange = sut.getExchanges(url);
        assertThat(exchange).isNotNull();
        assertThat(exchange.getRates()).isNotNull();
        assertThat(exchange.getRates().size()).isEqualTo(days);
    }
}