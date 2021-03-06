package com.example.rush0714.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.rush0714.myapplication.Activities.ActivityOrderClose;

import java.net.CookieManager;
import java.util.Arrays;
import java.util.List;

import NetUtils.Listeners.ReciveOrdersOnClickListener;
import NetUtils.Orders.Order;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    private static boolean isOrdersLoad = false;
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
                final int groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);

                AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
                adb.setTitle(getString(R.string.oper_title));
                String operations[] = {getString(R.string.oper_call), getString(R.string.oper_close)};
//                adb.setSingleChoiceItems(operations, -1, null);

                adb.setSingleChoiceItems(
                    new ArrayAdapter<String>(
                        getApplicationContext(), R.layout.list_order_close_services, Arrays.asList(operations)),
                    0, null);


                adb.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ListView lv = ((AlertDialog) dialogInterface).getListView();
                        Integer chekedItem = lv.getCheckedItemPosition();

                        Intent intent = null;
                        switch (chekedItem) {
                            case 0: {
                                intent = new Intent(MainActivity.this, OrderActivity.class);
                                break;
                            }
                            case 1: {
                                intent = new Intent(MainActivity.this, ActivityOrderClose.class);
                                break;
                            }
                        }
                        if(intent != null){
                            intent.putExtra("order", groupPosition);
                            startActivity(intent);
                        }
                    }
                });
                Dialog dialog = adb.create();
                dialog.show();
                return false;
            }
        });

        List<Order> orders = DataStorage.getOrders();

        reciveOrdersOnClickListener = (ReciveOrdersOnClickListener) getLastCustomNonConfigurationInstance();

        if (reciveOrdersOnClickListener == null) {
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
                CookieManager cookieManager = new CookieManager();
                DataStorage.setCookieStore(cookieManager.getCookieStore());
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
                        break;
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
