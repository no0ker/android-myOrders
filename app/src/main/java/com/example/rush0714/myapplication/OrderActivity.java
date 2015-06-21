package com.example.rush0714.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import NetUtils.Orders.Order;
import NetUtils.Orders.OrderHelper;

public class OrderActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        int orderNo = (int) getIntent().getExtras().getSerializable("order");
        List<String> phoneNumbers = OrderHelper.parsePhoneNumbers(orderNo);

        TextView customerName = (TextView) findViewById(R.id.nameCustomer);
        Order order = DataStorage.getOrder(orderNo);
        if(order != null){
            customerName.setText(order.getName());
        }

        ListView listView = (ListView) findViewById(R.id.callList);
        listView.setAdapter(new CallListAdapter(getApplicationContext(), phoneNumbers));
    }
}
