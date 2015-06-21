package NetUtils.Listeners;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.rush0714.myapplication.DataStorage;
import com.example.rush0714.myapplication.ExpListAdapter;
import com.example.rush0714.myapplication.R;

import java.util.List;
import java.util.Map;

import NetUtils.DataSite.DataSiteHelperV2;
import NetUtils.Orders.Order;

public class ReciveOrdersOnClickListener implements View.OnClickListener {
    private SharedPreferences sharedPreferences;
    public static final String TAG = ReciveOrdersOnClickListener.class.toString();
    Activity parentActivity;

    public ReciveOrdersOnClickListener(Activity parentActivity) {
        this.parentActivity = parentActivity;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(parentActivity);
        ;
    }

    @Override
    public void onClick(View view) {

        Map<String, ?> preferences = sharedPreferences.getAll();

        String login = (String) preferences.get(parentActivity.getString(R.string.login_key));
        String pass = (String) preferences.get(parentActivity.getString(R.string.pass_key));

        if (login == null || pass == null) {
            Toast.makeText(parentActivity.getApplicationContext(), R.string.no_auth_data, Toast.LENGTH_SHORT).show();
            return;
        }

        DataStorage.setLogin(login);
        DataStorage.setPassword(pass);

        new AsyncTask<Void, Void, Void>() {
            private String message;

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    new DataSiteHelperV2().getOrders();
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

                    for (Order iOrder : DataStorage.getOrders()) {
                        new AsyncTask<Order, Void, Void>() {
                            @Override
                            protected Void doInBackground(Order... order) {
                                try {
                                    new DataSiteHelperV2().setComments(order[0]);
                                } catch (Exception e) {
                                    Log.d(TAG, Log.getStackTraceString(e));
                                }
                                return null;
                            }
                        }.execute(iOrder);
                    }
                }
                if (message != null) {
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