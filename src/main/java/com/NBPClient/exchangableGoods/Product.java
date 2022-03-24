package com.NBPClient.exchangableGoods;

import com.fasterxml.jackson.annotation.*;

@JsonPropertyOrder({
        "data",
        "cena"
})
public class Product{


    private String data;

    private Double cena;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Double getCena() {
        return cena;
    }

    public void setCena(Double cena) {
        this.cena = cena;
    }
}