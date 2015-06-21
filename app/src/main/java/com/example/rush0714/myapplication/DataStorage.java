package com.example.rush0714.myapplication;

import java.net.CookieManager;
import java.net.CookieStore;
import java.util.ArrayList;
import java.util.List;

import NetUtils.Orders.Order;

public class DataStorage {
    private static String login;
    private static String password;
    private static List<Order> orders = new ArrayList<Order>();
    private static CookieStore cookieStore = new CookieManager().getCookieStore();

    public static CookieStore getCookieStore() {
        return cookieStore;
    }

    public static void setCookieStore(CookieStore cookieStore) {
        DataStorage.cookieStore = cookieStore;
    }

    public static List<Order> getOrders() {
        return orders;
    }

    public static void setOrders(List<Order> orders) {
        DataStorage.orders = orders;
    }

    public static String getLogin() {
        return login;
    }

    public static void setLogin(String login) {
        DataStorage.login = login;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        DataStorage.password = password;
    }
}
