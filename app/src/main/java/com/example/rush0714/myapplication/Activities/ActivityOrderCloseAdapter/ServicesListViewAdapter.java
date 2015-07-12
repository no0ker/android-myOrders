package com.example.rush0714.myapplication.Activities.ActivityOrderCloseAdapter;

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
    Context context;
    int resource;
    List<CSOrderService> csOrderServices;
    Map<Integer,Integer> csOrderServicesCouns = new ConcurrentHashMap<Integer, Integer>();
    private LayoutInflater lInflater;

    String test;

    public ServicesListViewAdapter(Context context, int resource, List<CSOrderService> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.csOrderServices = objects;
        lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        price.setText(csOrderService.getPrice().toString());
        priceEnd.setText(csOrderService.getPrice().toString());
        name.setText(csOrderService.getName());

        Button buttonInr = (Button) cView.findViewById(R.id.buttonIncr);
        buttonInr.setOnClickListener(new ButtonIncrListener(cView, csOrderServicesCouns, position));

        Button buttonDecr = (Button) cView.findViewById(R.id.buttonDecr);
        buttonDecr.setOnClickListener(new ButtonDecrListener(cView, csOrderServicesCouns, position));

        return cView;
    }

    @Override
    public int getCount() {
        return csOrderServices.size();
    }

    public String getText(){
        return "  " + test;
    }
}


