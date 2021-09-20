package com.example.demo.prometheus.bean;

import java.io.Serializable;

public class PriceSpreadBean implements Serializable {
    private String symbol;
    private String bidPrice;
    private String askPrice;
    private String priceChange;

    public PriceSpreadBean() {
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(String bidPrice) {
        this.bidPrice = bidPrice;
    }

    public String getAskPrice() {
        return askPrice;
    }

    @Override
    public String toString() {
        return "PriceSpreadBean{" +
                "symbol='" + symbol + '\'' +
                "priceChange='" + priceChange + '\'' +
                '}';
    }

    public String getPriceChange() {
        return priceChange;
    }

    public void setPriceChange(String priceChange) {
        this.priceChange = priceChange;
    }

    public void setAskPrice(String askPrice) {
        this.askPrice = askPrice;
    }
}
