package com.example.demo.prometheus.bean;

import java.io.Serializable;
import java.util.List;

public class NotionalValue implements Serializable {
    private String name;
    private List<TradeBean> bids;
    private List<TradeBean> asks;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TradeBean> getBids() {
        return bids;
    }

    public void setBids(List<TradeBean> bids) {
        this.bids = bids;
    }

    public List<TradeBean> getAsks() {
        return asks;
    }

    public void setAsks(List<TradeBean> asks) {
        this.asks = asks;
    }
}
