package com.example.rush0714.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.List;

import NetUtils.Listeners.ReciveOrdersOnClickListener;
import NetUtils.Orders.Order;

public class MainActivity extends Activity {
    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Button btnGetOrders = (Button) findViewById(R.id.buttonReciveOrders);
        btnGetOrders.setOnClickListener(
            new ReciveOrdersOnClickListener(this)
        );

        Button btnGetMap = (Button) findViewById(R.id.buttonMap);
        btnGetMap.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   Intent intent = new Intent(MainActivity.this, MapActivity.class);
                    for (Order iOrder : DataStorage.getOrders()) {
                        if (iOrder.getGeoPoint() == null) {
                            Toast.makeText(getApplicationContext(), getString(R.string.koord_later), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    startActivity(intent);
                }
            }
        );

        final ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                long packedPosition = expandableListView.getExpandableListPosition(i);
                int groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);

                Intent intent = new Intent(MainActivity.this, OrderActivity.class);
                intent.putExtra("order", groupPosition);
                startActivity(intent);
                return false;
            }
        });

        List<Order> orders = DataStorage.getOrders();
        if(orders != null){
            ExpandableListAdapter adapter = new ExpListAdapter(this.getApplicationContext(), orders);
            expandableListView.setAdapter(adapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
