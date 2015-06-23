package com.example.rush0714.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CallListAdapter extends BaseAdapter {
    private Context context;
    private List<String> phoneNumbers;
    private LayoutInflater lInflater;

    public CallListAdapter(Context context, List<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
        this.context = context;
        lInflater = (LayoutInflater) context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return phoneNumbers.size();
    }

    @Override
    public Object getItem(int i) {
        return phoneNumbers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View cView = view;
        if (cView == null) {
            cView = lInflater.inflate(R.layout.list_call_list_item, viewGroup, false);
        }
        final TextView textView = (TextView) cView.findViewById(R.id.phoneNumber);
        textView.setText(phoneNumbers.get(i));
        cView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String callNumber = (String) textView.getText();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse("tel:" + callNumber));
                context.startActivity(intent);
            }
        });
        return cView;
    }

}
