package com.example.rush0714.myapplication.NetUtils;

import java.util.Map;

public abstract class CSParser<T,V> {
    public abstract Map<T,V> parse(String stringIn);
}
