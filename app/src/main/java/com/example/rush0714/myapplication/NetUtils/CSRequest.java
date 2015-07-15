package com.example.rush0714.myapplication.NetUtils;

import java.util.Map;

public abstract class CSRequest {
    public String address;
    public Method method;
    public static enum Method {GET, POST};
    public String referer;
    public Map<String, String> postParams;

    public abstract void setVariables();

    public String getAddress() {
        return address;
    }

    public Method getMethod() {
        return method;
    }


}


