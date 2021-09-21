package com.example.demo.prometheus.bean;

public class NotionalValuePrice {
    private String symbol;
    private Double bidPrice;
    private Double askPrice;

    public NotionalValuePrice() {
    }

    public NotionalValuePrice(String symbol, Double bidPrice, Double askPrice) {
        this.symbol = symbol;
        this.bidPrice = bidPrice;
        this.askPrice = askPrice;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Double getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(Double bidPrice) {
        this.bidPrice = bidPrice;
    }

    public Double getAskPrice() {
        return askPrice;
    }

    public void setAskPrice(Double askPrice) {
        this.askPrice = askPrice;
    }
}
