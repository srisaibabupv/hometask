package com.example.demo.prometheus.bean;

import java.io.Serializable;

public class SymbolNotionalValue implements Serializable {
    Long lastUpdateId;
    NotionalBean[][] bids;

    public Long getLastUpdateId() {
        return lastUpdateId;
    }

    public void setLastUpdateId(Long lastUpdateId) {
        this.lastUpdateId = lastUpdateId;
    }

    public NotionalBean[][] getBids() {
        return bids;
    }

    public void setBids(NotionalBean[][] bids) {
        this.bids = bids;
    }
}
