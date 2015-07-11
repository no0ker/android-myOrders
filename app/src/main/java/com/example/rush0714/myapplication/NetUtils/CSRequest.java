package com.example.rush0714.myapplication.NetUtils;

public abstract class CSRequest {
    public String address;
    public Method method;
    public static enum Method {GET, POST};
    public abstract void setVariables();

    public String getAddress() {
        return address;
    }

    public Method getMethod() {
        return method;
    }
}


