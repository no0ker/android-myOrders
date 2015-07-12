package com.example.rush0714.myapplication.Activities.ActivityOrderCloseAdapter;

import android.app.Activity;
import android.view.View;

import com.example.rush0714.myapplication.Activities.ActivityOrderClose;

public class ButtonDeleteListener implements View.OnClickListener {
    private Activity activity;
    private Integer position;

    public ButtonDeleteListener(Integer position, Activity activity) {
        this.activity = activity;
        this.position = position;
    }

    @Override
    public void onClick(View view) {
        ((ActivityOrderClose)activity).delete(position);
    }
}
