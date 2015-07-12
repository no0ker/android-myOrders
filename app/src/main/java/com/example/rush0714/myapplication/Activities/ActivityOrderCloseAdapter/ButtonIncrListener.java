package com.example.rush0714.myapplication.Activities.ActivityOrderCloseAdapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.example.rush0714.myapplication.Activities.ActivityOrderClose;
import com.example.rush0714.myapplication.R;

import java.util.Map;

public class ButtonIncrListener implements View.OnClickListener {
    private View parentView;
    private Map<Integer, Integer> csOrderServicesCounts;
    private Integer position;
    private Activity activity;
    protected Integer count;

    public ButtonIncrListener(View parentView, Map<Integer, Integer> csOrderServicesCounts, Integer position, Activity activity) {
        this.parentView = parentView;
        this.csOrderServicesCounts = csOrderServicesCounts;
        this.position = position;
        this.activity = activity;
    }

    @Override
    public void onClick(View view) {
        TextView priceView = (TextView) parentView.findViewById(R.id.price);
        TextView priceEnd = (TextView) parentView.findViewById(R.id.priceEnd);
        TextView countView = (TextView) parentView.findViewById(R.id.count);

        Integer price = Integer.parseInt((String)priceView.getText());
        Integer count = Integer.parseInt((String) countView.getText());

        ++count;

        Integer priceEndInt = count * price;

        countView.setText("" + count.toString());
        priceEnd.setText("" + priceEndInt.toString());

        csOrderServicesCounts.put(position, count);
        ((ActivityOrderClose)activity).refresh();
    }
}
