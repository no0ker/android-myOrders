package com.example.rush0714.myapplication.Activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rush0714.myapplication.DataStorage;
import com.example.rush0714.myapplication.Helpers.AuthHelper;
import com.example.rush0714.myapplication.NetUtils.CSNetUtils;
import com.example.rush0714.myapplication.NetUtils.CSOrderService;
import com.example.rush0714.myapplication.NetUtils.CSParser;
import com.example.rush0714.myapplication.NetUtils.CSRequest;
import com.example.rush0714.myapplication.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import NetUtils.Orders.Order;

public class ActivityOrderClose extends AppCompatActivity {
    private Order order;
    private List<CSOrderService> orderServices;
    private List<CSOrderService> orderServicesSelected;
    private AsyncTask<Void, Void, List<CSOrderService>> asyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_close);

        int orderNo = (int) getIntent().getExtras().getSerializable("order");
        order = DataStorage.getOrder(orderNo);

        TextView textView = (TextView) findViewById(R.id.AOrderCLoseOrderAddress);
        textView.setText(order.getAddress());

        ((Button) findViewById(R.id.button_AOrderClose_add_id)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    orderServices = asyncTask.get(1, TimeUnit.SECONDS);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), R.string.orders_is_load, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (orderServices == null || orderServices.isEmpty()) {
                    Toast.makeText(getApplicationContext(), R.string.orders_is_load, Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder adb = new AlertDialog.Builder(ActivityOrderClose.this);
                    adb.setTitle(getString(R.string.button_AOrderClose_service_orders));
                    List<String> services = new LinkedList<String>();
                    for (CSOrderService i : orderServices) {
                        services.add(i.getName());
                    }

                    adb.setSingleChoiceItems(
                        new ArrayAdapter<String>(
                            getApplicationContext(), R.layout.list_order_close_services, services),
                        0, null);

                    adb.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ListView lv = ((AlertDialog) dialogInterface).getListView();
                            Integer chekedItem = lv.getCheckedItemPosition();

                            Toast.makeText(getApplicationContext(), " " + chekedItem, Toast.LENGTH_SHORT).show();
                        }
                    });
                    Dialog dialog = adb.create();
                    dialog.show();
                }
            }
        });

        getOrderServices();
    }

    private void getOrderServices() {
        try {
            Map<String, String> authData = AuthHelper.getAuthData(this);

            CSNetUtils<List<CSOrderService>> csNetUtils =
                new CSNetUtils<List<CSOrderService>>(authData.get("l"), authData.get("p"));

            csNetUtils.setCsRequest(new CSRequest() {
                @Override
                public void setVariables() {
                    method = Method.GET;
                    address = order.getLink();
                }
            });

            csNetUtils.setCsParser(new CSParser<List<CSOrderService>>() {
                @Override
                public List<CSOrderService> parse(String stringIn) {
                    Document doc = Jsoup.parse(stringIn);
                    Element element = doc.getElementsByClass("jobs").first();
                    Elements elements = element.getElementsByTag("option");
                    List<CSOrderService> result = new LinkedList<>();
                    Integer i = 0;
                    for (Element iElement : elements) {
                        String name = iElement.text();
                        Integer value = Integer.parseInt(iElement.attr("value"));
                        Integer price = Integer.parseInt(iElement.attr("price"));
                        result.add(new CSOrderService(name, value, price));
                    }
                    return result;
                }
            });

            asyncTask = csNetUtils.makeTask();
            asyncTask.execute();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
            Log.d("a", Log.getStackTraceString(e));
        }
    }

    public void setOrderServices(List<CSOrderService> orderServices) {
        this.orderServices = orderServices;
    }
}

