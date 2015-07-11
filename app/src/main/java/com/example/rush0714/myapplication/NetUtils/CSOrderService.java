package com.example.rush0714.myapplication.NetUtils;

public class CSOrderService {
    String name;
    Integer value;
    Integer price;

    public CSOrderService(String name, Integer price, Integer value) {
        this.name = name;
        this.price = price;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
