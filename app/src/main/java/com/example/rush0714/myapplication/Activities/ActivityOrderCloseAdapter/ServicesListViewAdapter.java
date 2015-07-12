package com.example.rush0714.myapplication.Activities.ActivityOrderCloseAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.rush0714.myapplication.Activities.ActivityOrderClose;
import com.example.rush0714.myapplication.NetUtils.CSOrderService;
import com.example.rush0714.myapplication.R;

import java.util.List;
import java.util.Map;

public class ServicesListViewAdapter extends ArrayAdapter<CSOrderService> {
    private Activity parentActivity;
    private List<CSOrderService> csOrderServicesSelected;
    private Map<Integer, Integer> csOrderServicesCounts;
    private LayoutInflater lInflater;

    String test;

    public ServicesListViewAdapter(
        Context context,
        Activity activity,
        List<CSOrderService> csOrderServicesSelected,
        Map<Integer, Integer> csOrderServicesCounts) {

        super(context, R.layout.list_order_close_services_editable);
        this.parentActivity = activity;
        this.csOrderServicesSelected = csOrderServicesSelected;
        this.csOrderServicesCounts = csOrderServicesCounts;
        lInflater = (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View cView = convertView;
        if (cView == null) {
            cView = lInflater.inflate(R.layout.list_order_close_services_editable, parent, false);
        }
        CSOrderService csOrderService = csOrderServicesSelected.get(position);

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
        buttonInr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ActivityOrderClose) parentActivity).increment(position);
            }
        });

        Button buttonDecr = (Button) cView.findViewById(R.id.buttonDecr);
        buttonDecr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ActivityOrderClose) parentActivity).decrement(position);
            }
        });

        Button buttonDelete = (Button) cView.findViewById(R.id.buttonDelete);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ActivityOrderClose) parentActivity).remove(position);
            }
        });

        return cView;
    }

    @Override
    public int getCount() {
        return csOrderServicesSelected.size();
    }

}


