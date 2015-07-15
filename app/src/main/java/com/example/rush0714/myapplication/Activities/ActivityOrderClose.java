package com.example.rush0714.myapplication.Activities;

import android.app.Activity;
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

import com.example.rush0714.myapplication.Activities.ActivityOrderCloseAdapter.ServicesListViewAdapter;
import com.example.rush0714.myapplication.DataStorage;
import com.example.rush0714.myapplication.Helpers.AuthHelper;
import com.example.rush0714.myapplication.NetUtils.CSNetUtils;
import com.example.rush0714.myapplication.NetUtils.CSOrderService;
import com.example.rush0714.myapplication.NetUtils.CSParseResult;
import com.example.rush0714.myapplication.NetUtils.CSParser;
import com.example.rush0714.myapplication.NetUtils.CSPostExecute;
import com.example.rush0714.myapplication.NetUtils.CSRequest;
import com.example.rush0714.myapplication.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import NetUtils.Orders.Order;
import NetUtils.Resources.Resources;

public class ActivityOrderClose extends AppCompatActivity {
    private Order order;
    private List<CSOrderService> orderServices;
    private List<CSOrderService> orderServicesSelected = new ArrayList<CSOrderService>();
    private Map<Integer, Integer> orderServicesCounts = new ConcurrentHashMap<Integer, Integer>();
    private ServicesListViewAdapter servicesListViewAdapter;
    private Map<String, String> myActsParams;
    private List<Integer> myActs;
    private AsyncTask<Void, Void, CSParseResult> asyncTaskServices;
    private AsyncTask<Void, Void, List<Integer>> asyncTaskActs;
    private TextView sumCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_close);
        sumCount = (TextView) findViewById(R.id.AOrderClose_countSum);
        servicesListViewAdapter = new ServicesListViewAdapter(
            getApplicationContext(),
            ActivityOrderClose.this,
            orderServicesSelected,
            orderServicesCounts);
        ListView listView = (ListView) findViewById(R.id.list_AOrderClose_services);
        listView.setAdapter(servicesListViewAdapter);

        int orderNo = (int) getIntent().getExtras().getSerializable("order");
        order = DataStorage.getOrder(orderNo);

        TextView textView = (TextView) findViewById(R.id.AOrderCLoseOrderAddress);
        textView.setText(order.getAddress());
        getOrderServices();
        setButtonListener();
        setButtonListener2();
    }


    public void refresh() {
        Integer sum = 0;
        for (int i = 0; i < orderServicesSelected.size(); i++) {
            if (orderServicesCounts.containsKey(i)) {
                sum += orderServicesSelected.get(i).getPrice() * orderServicesCounts.get(i);
            } else {
                sum += orderServicesSelected.get(i).getPrice();
            }
        }
        sumCount.setText("" + sum.toString());
        servicesListViewAdapter.notifyDataSetChanged();
    }


    public void increment(int position) {
        if (orderServicesCounts.containsKey(position)) {
            int i = orderServicesCounts.get(position);
            ++i;
            orderServicesCounts.put(position, i);
        } else {
            orderServicesCounts.put(position, 2);
        }
        refresh();
    }

    public void decrement(int position) {
        if (orderServicesCounts.containsKey(position)) {
            int i = orderServicesCounts.get(position);
            --i;
            if (i < 0) {
                i = 0;
            }
            orderServicesCounts.put(position, i);
        } else {
            orderServicesCounts.put(position, 2);
        }
        servicesListViewAdapter.notifyDataSetChanged();
        refresh();
    }

    public void remove(int position) {
        if (orderServicesCounts.containsKey(position)) {
            orderServicesCounts.remove(position);
        }
        orderServicesSelected.remove(position);
        refresh();
    }

    public void setButtonListener() {
        ((Button) findViewById(R.id.button_AOrderClose_add_id)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    orderServices = asyncTaskServices.get(1, TimeUnit.SECONDS).getCsOrderServices();
                    myActsParams = asyncTaskServices.get(1, TimeUnit.SECONDS).getMapStringString();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), R.string.orders_is_load, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (orderServices == null || orderServices.isEmpty()) {
                    Toast.makeText(getApplicationContext(), R.string.orders_is_load, Toast.LENGTH_SHORT).show();
                } else {
                    if(myActs == null || myActs.isEmpty()){
                        getMyActs();
                    }
                    AlertDialog.Builder adb = new AlertDialog.Builder(ActivityOrderClose.this);
                    adb.setTitle(getString(R.string.button_AOrderClose_add_text));
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

                            CSOrderService serviceSelected = orderServices.get(chekedItem);
                            Boolean stillExists = false;

                            for (int j = 0; j < orderServicesSelected.size(); j++) {
                                CSOrderService si = orderServicesSelected.get(j);
                                if (si.getValue().equals(serviceSelected.getValue())) {
                                    if (orderServicesCounts.containsKey(j)) {
                                        int tmp = orderServicesCounts.get(j);
                                        orderServicesCounts.put(j, ++tmp);
                                    } else {
                                        orderServicesCounts.put(j, 2);
                                    }
                                    stillExists = true;
                                    break;
                                }
                            }
                            if (!stillExists) {
                                orderServicesSelected.add(orderServices.get(chekedItem));
                            }
                            refresh();
                        }
                    });
                    Dialog dialog = adb.create();
                    dialog.show();
                }
            }
        });

        ((Button) findViewById(R.id.button_AOrderClose_send_id)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myActsParams == null || myActsParams.isEmpty()) {
                    Toast.makeText(getApplicationContext(), R.string.button_AOrderClose_first_added_services, Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    myActs = asyncTaskActs.get(1, TimeUnit.SECONDS);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), R.string.orders_is_load, Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    public void setButtonListener2() {
        Button button = (Button) findViewById(R.id.button_AOrderClose_add_act);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void getMyActs() {
        try {
            Map<String, String> authData = AuthHelper.getAuthData(this);
            CSNetUtils<List<Integer>> csNetUtils =
                new CSNetUtils<List<Integer>>(authData.get("l"), authData.get("p"), this);

            csNetUtils.setCsRequest(
                new CSRequest() {
                    @Override
                    public void setVariables() {
                        method = Method.POST;
                        address = Resources.URL_ACTS;
                        postParams = myActsParams;
                        referer = order.getLink();
                    }
                }
            );

            csNetUtils.setCsParser(new CSParser<List<Integer>>() {
                @Override
                public List<Integer> parse(String stringIn) {
                    if(stringIn != null){
                        Document doc = Jsoup.parse(stringIn);
                    }
                    return null;
                }
            });
            asyncTaskActs = csNetUtils.makeTask();
            asyncTaskActs.execute();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
            e.printStackTrace();
        }
    }

    private void getOrderServices() {
        try {
            Map<String, String> authData = AuthHelper.getAuthData(this);

            CSNetUtils<CSParseResult> csNetUtils =
                new CSNetUtils<CSParseResult>(authData.get("l"), authData.get("p"), this);

            csNetUtils.setCsRequest(new CSRequest() {
                @Override
                public void setVariables() {
                    method = Method.GET;
                    address = order.getLink();
                }
            });

            csNetUtils.setCsParser(new CSParser<CSParseResult>() {
                @Override
                public CSParseResult parse(String stringIn) {
                    Document doc = Jsoup.parse(stringIn);
                    Element element = doc.getElementsByClass("jobs").first();
                    Elements elements = element.getElementsByTag("option");
                    List<CSOrderService> resultList = new LinkedList<>();
                    Integer i = 0;
                    for (Element iElement : elements) {
                        String name = iElement.text();
                        Integer value = Integer.parseInt(iElement.attr("value"));
                        Integer price = Integer.parseInt(iElement.attr("price"));
                        resultList.add(new CSOrderService(name, value, price));
                    }

                    Map<String, String> resultMap = new HashMap<String, String>();
                    Elements scripts = doc.getElementsByTag("script");
                    for (Element si : scripts) {
                        List<Node> texts = si.childNodes();
                        if (texts.size() > 0) {
                            Pattern p = Pattern.compile("AppItem.CheckedActs");
                            Matcher m = p.matcher(texts.get(0).toString());
                            if (m.find()) {
                                p = Pattern.compile("AppItem.CheckedActs\\(\\'(\\d+)\\',\\'(\\d+)\\',\\'(\\d+)\\'\\);");
                                m = p.matcher(texts.get(0).toString());
                                if (m.find()) {
                                    resultMap.put("id", m.group(1));
                                    resultMap.put("ida", m.group(2));
                                    resultMap.put("city", m.group(3));
                                    break;
                                }
                            }
                        }
                    }
                    CSParseResult csParseResult = new CSParseResult();
                    csParseResult.setCsOrderServices(resultList);
                    csParseResult.setMapStringString(resultMap);
                    return csParseResult;
                }
            });

            csNetUtils.setCsPostExecute(new CSPostExecute() {
                @Override
                protected void run(Activity activity) {
                    Toast.makeText(activity.getApplicationContext(), "ecttt!!!!!", Toast.LENGTH_SHORT).show();
                }
            });

            asyncTaskServices = csNetUtils.makeTask();
            asyncTaskServices.execute();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
            Log.d("a", Log.getStackTraceString(e));
        }
    }
}

