package com.example.rush0714.myapplication.NetUtils;

import java.util.List;
import java.util.Map;

public class CSParseResult {
    List<CSOrderService> csOrderServices;
    Map<String, String> mapStringString;

    public List<CSOrderService> getCsOrderServices() {
        return csOrderServices;
    }

    public void setCsOrderServices(List<CSOrderService> csOrderServices) {
        this.csOrderServices = csOrderServices;
    }

    public Map<String, String> getMapStringString() {
        return mapStringString;
    }

    public void setMapStringString(Map<String, String> mapStringString) {
        this.mapStringString = mapStringString;
    }
}
