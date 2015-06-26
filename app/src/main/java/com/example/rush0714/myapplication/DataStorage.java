package com.example.rush0714.myapplication;

import java.net.CookieManager;
import java.net.CookieStore;
import java.util.ArrayList;
import java.util.List;

import NetUtils.Orders.Order;

public class DataStorage {
    private static List<Order> orders = new ArrayList<Order>();
    private static CookieStore cookieStore = new CookieManager().getCookieStore();

    public static CookieStore getCookieStore() {
        return cookieStore;
    }

    public static void setCookieStore(CookieStore cookieStore) {
        DataStorage.cookieStore = cookieStore;
    }

    public static Order getOrder(Integer no){
        if(orders.size() <= no){
            return null;
        }
        return orders.get(no);
    }

    public static List<Order> getOrders() {
        return orders;
    }

    public static void setOrders(List<Order> orders) {
        DataStorage.orders = orders;
    }

}
