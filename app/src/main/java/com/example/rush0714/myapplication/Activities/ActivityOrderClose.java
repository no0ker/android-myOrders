package com.example.rush0714.myapplication.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rush0714.myapplication.DataStorage;
import com.example.rush0714.myapplication.Helpers.AuthHelper;
import com.example.rush0714.myapplication.NetUtils.CSNetUtils;
import com.example.rush0714.myapplication.NetUtils.CSOrderService;
import com.example.rush0714.myapplication.NetUtils.CSRequest;
import com.example.rush0714.myapplication.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import NetUtils.Orders.Order;

public class ActivityOrderClose extends AppCompatActivity {
    private Order order;
    private String login;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_close);

        int orderNo = (int) getIntent().getExtras().getSerializable("order");
        order = DataStorage.getOrder(orderNo);

        TextView textView = (TextView) findViewById(R.id.AOrderCLoseOrderAddress);
        textView.setText(order.getAddress());

        Button addButton = (Button) findViewById(R.id.button_AOrderClose_add_id);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void getOrderServices() {
        try {
            Map<String, String> authData = AuthHelper.getAuthData(this);

            CSNetUtils<List<CSOrderService>> csNetUtils = new CSNetUtils<List<CSOrderService>>(authData.get("l"), authData.get("p")) {
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
            };

            csNetUtils.setCsRequest(new CSRequest() {
                @Override
                public void setVariables() {
                    method = Method.GET;
                    address = order.getLink();
                }
            });

            List<CSOrderService> orders = (List<CSOrderService>) csNetUtils.doRequest();

            Double a = 4.4;

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
        }
    }
}

