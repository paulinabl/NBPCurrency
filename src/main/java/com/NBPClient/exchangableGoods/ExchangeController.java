package com.NBPClient.exchangableGoods;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/")
public class ExchangeController {

    private final ExchangeService exchangeService;

    @GetMapping("gold-price/average")
    public ResponseEntity<String> getAverageGoldPrice(){
        String average = exchangeService.prepareAndFetch(ExchangeableGoodsTypes.GOLD, 14, "");
        String outputMsg = String.format("Średnia cena złota z ostatnich 14 dni to: %s PLN", average);
        return ResponseEntity.ok(outputMsg);
    }

    @GetMapping("exchange-rates/{currencyCode}")
    public ResponseEntity<String> getAverageGoldPrice(@PathVariable String currencyCode){
        String average = exchangeService.prepareAndFetch(ExchangeableGoodsTypes.CURRENCY, 5, currencyCode);
        return ResponseEntity.ok(average);
    }

}
