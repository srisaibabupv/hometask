package com.example.demo.prometheus.bean;

public class TradeBean {
    private Double price;
    private Double quantity;
    private Double notionalValue;

    public TradeBean() {
    }

    public TradeBean(Double price, Double quantity) {
        this.price = price;
        this.quantity = quantity;
        this.notionalValue = price * quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getNotionalValue() {
        return notionalValue;
    }

    public void setNotionalValue(Double notionalValue) {
        this.notionalValue = notionalValue;
    }
}
