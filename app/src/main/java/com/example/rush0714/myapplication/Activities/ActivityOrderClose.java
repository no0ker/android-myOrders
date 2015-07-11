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
import com.example.rush0714.myapplication.NetUtils.CSParser;
import com.example.rush0714.myapplication.NetUtils.CSRequest;
import com.example.rush0714.myapplication.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

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
        final Map<String, String> authData = AuthHelper.getAuthData(this);

        Button addButton = (Button) findViewById(R.id.button_AOrderClose_add_id);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    CSNetUtils csNetUtils = new CSNetUtils(authData.get("l"), authData.get("p"));
                    csNetUtils.setCsParser(new CSParser<String, CSOrderService>(){
                        @Override
                        public Map<String, CSOrderService> parse(String stringIn) {
                            Document doc = (Document) Jsoup.parse(stringIn);

                            return null;
                        }
                    });
                    csNetUtils.setCsRequest(new CSRequest() {
                        @Override
                        public void setVariables() {
                            method = Method.GET;
                            address = order.getLink();
                        }
                    });
                    csNetUtils.doRequest();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), getString(R.string.error), Toast.LENGTH_SHORT);
                }
            }
        });
    }
}
