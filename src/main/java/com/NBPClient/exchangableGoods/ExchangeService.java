package com.NBPClient.exchangableGoods;

import com.NBPClient.NotFoundException;
import com.NBPClient.Properties;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ExchangeService {

    private final RestTemplate restTemplate;
    private final Properties properties;

    public List<Product> getProducts(String url) {
        Product[] products = restTemplate.getForObject(url, Product[].class);
        return Arrays.stream(Optional.ofNullable(products).orElseThrow(() -> new NotFoundException("Product Not found"))).collect(Collectors.toList());
    }

    public String prepareAndFetch(ExchangeableGoodsTypes exchangeableGood, int days, String currencyTo) {
        switch (exchangeableGood) {
            case GOLD:
                String goldUrl = String.format(properties.getGoldUrl(), "cenyzlota", days);
                return calculategetProducts(getProducts(goldUrl));
            case CURRENCY:
                String currencyUrl = String.format(properties.getCurrencyUrl(), currencyTo, days);
                return getCurrenciesForSpecificDays(currencyUrl, days, currencyTo);
        }
        return null;
    }

    private String getCurrenciesForSpecificDays(String currencyUrl, int days, String currencyTo) {
        CurrencyExchange currency = getExchanges(currencyUrl);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("exchange ratio for %d days<br>", days));
        stringBuilder.append(String.format("1 PLN to %s :<br>",currencyTo));
        for (Rate rate : currency.getRates()) {
            String inversedRatio = inverseRate(rate.getMid());
            stringBuilder.append(inversedRatio).append("<br>");
        }
        return stringBuilder.toString();
    }

    public CurrencyExchange getExchanges(String currencyUrl) {
        return restTemplate.getForObject(currencyUrl, CurrencyExchange.class);
    }

    private String inverseRate(Double mid) {
        return BigDecimal.valueOf(1/mid).setScale(3,RoundingMode.CEILING).toString();
    }

    private String calculategetProducts(List<Product> products) {
        int productsQty = products.size();
        double avg = products.stream()
                .map(Product::getCena)
                .reduce(0.0, Double::sum) / productsQty;
        return BigDecimal.valueOf(avg).setScale(2, RoundingMode.CEILING).toString();
    }

}
