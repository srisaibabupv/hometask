package com.example.demo.prometheus.bean;

import java.io.Serializable;

public class NotionalBean implements Serializable {
    String value;

    public NotionalBean() {
    }

    public NotionalBean(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
