package com.example.rush0714.myapplication.Activities.ActivityOrderCloseAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.rush0714.myapplication.NetUtils.CSOrderService;
import com.example.rush0714.myapplication.R;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServicesListViewAdapter extends ArrayAdapter<CSOrderService> {
    Activity parentActivity;
    int resource;
    List<CSOrderService> csOrderServices;
    Map<Integer, Integer> csOrderServicesCounts;
    private LayoutInflater lInflater;

    String test;

    public ServicesListViewAdapter(
        Activity activity,
        int resource,
        List<CSOrderService> objects,
        Map<Integer, Integer> csOrderServicesCounts) {

        super(activity.getApplicationContext(), resource, objects);
        this.parentActivity = activity;
        this.resource = resource;
        this.csOrderServices = objects;
        if (csOrderServicesCounts == null) {
            this.csOrderServicesCounts = new ConcurrentHashMap<Integer, Integer>();
        } else {
            this.csOrderServicesCounts = csOrderServicesCounts;
        }
        lInflater = (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View cView = convertView;
        if (cView == null) {
            cView = lInflater.inflate(resource, parent, false);
        }
        final CSOrderService csOrderService = csOrderServices.get(position);

        TextView price = (TextView) cView.findViewById(R.id.price);
        TextView priceEnd = (TextView) cView.findViewById(R.id.priceEnd);
        TextView name = (TextView) cView.findViewById(R.id.name);
        TextView count = (TextView) cView.findViewById(R.id.count);

        price.setText(csOrderService.getPrice().toString());
        name.setText(csOrderService.getName());

        if (csOrderServicesCounts.containsKey(position)) {
            Integer countInt = csOrderServicesCounts.get(position);
            count.setText("" + countInt.toString());
            Integer priceEndInt = csOrderService.getPrice() * countInt;
            priceEnd.setText("" + priceEndInt.toString());
        } else {
            count.setText("1");
            priceEnd.setText(csOrderService.getPrice().toString());
        }

        Button buttonInr = (Button) cView.findViewById(R.id.buttonIncr);
        buttonInr.setOnClickListener(new ButtonIncrListener(cView, csOrderServicesCounts, position, parentActivity));

        Button buttonDecr = (Button) cView.findViewById(R.id.buttonDecr);
        buttonDecr.setOnClickListener(new ButtonDecrListener(cView, csOrderServicesCounts, position, parentActivity));

        Button buttonDelete = (Button) cView.findViewById(R.id.buttonDelete);
        buttonDelete.setOnClickListener(new ButtonDeleteListener(position, parentActivity));

        return cView;
    }

    @Override
    public int getCount() {
        return csOrderServices.size();
    }

    public Map<Integer, Integer> getCsOrderServicesCounts() {
        return csOrderServicesCounts;
    }

    public List<CSOrderService> getCsOrderServices() {
        return csOrderServices;
    }
}


