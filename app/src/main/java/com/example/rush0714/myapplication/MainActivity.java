package com.example.rush0714.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.List;

import NetUtils.Listeners.ReciveOrdersOnClickListener;
import NetUtils.Orders.Order;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    private boolean isOrdersLoad = false;
    private ReciveOrdersOnClickListener reciveOrdersOnClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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

        reciveOrdersOnClickListener = (ReciveOrdersOnClickListener) getLastCustomNonConfigurationInstance();

        if(reciveOrdersOnClickListener == null){
            reciveOrdersOnClickListener = new ReciveOrdersOnClickListener(this);
        } else {
            reciveOrdersOnClickListener.setParentActivity(this);
        }

        if (orders != null && !orders.isEmpty()) {
            ExpandableListAdapter adapter = new ExpListAdapter(this.getApplicationContext(), orders);
            expandableListView.setAdapter(adapter);
        } else if (!isOrdersLoad) {
            reciveOrdersOnClickListener.onClick();
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
        switch (id) {
            case R.id.menu_settings: {
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.menu_orders: {
                if (isOrdersLoad) {
                    Toast.makeText(getApplicationContext(), R.string.orders_is_load, Toast.LENGTH_SHORT).show();
                } else {
                    reciveOrdersOnClickListener.onClick();
                }
                break;
            }
            case R.id.menu_map: {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                boolean ready = true;
                for (Order iOrder : DataStorage.getOrders()) {
                    if (iOrder.getGeoPoint() == null || isOrdersLoad) {
                        Toast.makeText(getApplicationContext(), getString(R.string.orders_is_load), Toast.LENGTH_SHORT).show();
                        ready = false;
                    }
                }
                if (ready) {
                    startActivity(intent);
                }
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void setIsOrdersLoad(boolean isOrdersLoad) {
        this.isOrdersLoad = isOrdersLoad;
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return reciveOrdersOnClickListener;
    }
}
