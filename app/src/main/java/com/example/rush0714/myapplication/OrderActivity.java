package com.example.rush0714.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

import NetUtils.Orders.OrderHelper;

public class OrderActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        int orderNo = (int) getIntent().getExtras().getSerializable("order");
        List<String> phoneNumbers= OrderHelper.parsePhoneNumbers(orderNo);
        ListView listView = (ListView) findViewById(R.id.callList);
        listView.setAdapter(new CallListAdapter(getApplicationContext(), phoneNumbers));
    }
}
