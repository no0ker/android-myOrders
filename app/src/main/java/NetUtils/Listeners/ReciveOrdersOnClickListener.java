package NetUtils.Listeners;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.rush0714.myapplication.DataStorage;
import com.example.rush0714.myapplication.ExpListAdapter;
import com.example.rush0714.myapplication.R;

import java.util.List;
import java.util.Map;

import NetUtils.DataSite.DataSiteHelperV2;
import NetUtils.Maps.GeoCoderHelper;
import NetUtils.Orders.Order;

public class ReciveOrdersOnClickListener {
    public static final String TAG = ReciveOrdersOnClickListener.class.toString();
    private String login;
    private String password;

    Activity parentActivity;

    public ReciveOrdersOnClickListener(Activity parentActivity) {
        this.parentActivity = parentActivity;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(parentActivity);
        Map<String, ?> preferences = sharedPreferences.getAll();
        this.login = (String) preferences.get(parentActivity.getString(R.string.login_key));
        this.password = (String) preferences.get(parentActivity.getString(R.string.pass_key));
    }

    public void onClick() {

        if (login == null || password == null) {
            Toast.makeText(parentActivity.getApplicationContext(), R.string.no_auth_data, Toast.LENGTH_SHORT).show();
            return;
        }

        new AsyncTask<Void, Void, Void>() {
            private String message;

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    new DataSiteHelperV2().getOrders(login, password);
                } catch (Exception e) {
                    message = e.getMessage();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                List<Order> orders = DataStorage.getOrders();
                if (!orders.isEmpty()) {
                    ExpandableListAdapter adapter = new ExpListAdapter(parentActivity.getApplicationContext(), orders);
                    final ExpandableListView expandableListView = (ExpandableListView) parentActivity.findViewById(R.id.expandableListView);
                    expandableListView.setAdapter(adapter);


                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            try {
                                for (final Order iOrder : DataStorage.getOrders()) {
                                    new DataSiteHelperV2().setComments(iOrder, login, password);
                                    iOrder.setGeoPoint(new GeoCoderHelper().getGeoPoint(iOrder.getAddress()));
                                }
                            } catch (Exception e) {
                                Log.d(TAG, Log.getStackTraceString(e));
                            }
                            return null;
                        }
                    }.execute();

                }
                if (message == null) {
                    if (DataStorage.getOrders().size() == 0) {
                        message = parentActivity.getString(R.string.no_orders);
                    } else {
                        message = parentActivity.getString(R.string.orders_done);
                    }
                }
                Toast.makeText(parentActivity.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }
}