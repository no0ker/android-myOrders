package NetUtils.Listeners;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.rush0714.myapplication.DataStorage;
import com.example.rush0714.myapplication.ExpListAdapter;
import com.example.rush0714.myapplication.MainActivity;
import com.example.rush0714.myapplication.R;

import java.util.List;
import java.util.Map;

import NetUtils.AsyncTask.AsyncTaskWithProgressBar;
import NetUtils.AsyncTask.ProgressPoint;
import NetUtils.DataSite.DataSiteHelperV2;
import NetUtils.Maps.GeoCoderHelper;
import NetUtils.Orders.Order;

public class ReciveOrdersOnClickListener {
    public static final String TAG = ReciveOrdersOnClickListener.class.toString();
    private String login;
    private String password;
    private ProgressBar progressBar;
    private AsyncTaskWithProgressBar<Void, ProgressPoint, Void> asyncTaskWithProgressBar;

    Activity parentActivity;

    public ReciveOrdersOnClickListener(Activity parentActivity) {
        this.parentActivity = parentActivity;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(parentActivity);
        Map<String, ?> preferences = sharedPreferences.getAll();
        this.login = (String) preferences.get(parentActivity.getString(R.string.login_key));
        this.password = (String) preferences.get(parentActivity.getString(R.string.pass_key));
        this.progressBar = (ProgressBar) parentActivity.findViewById(R.id.progressBar);
    }

    public void onClick() {

        if (login == null || password == null) {
            Toast.makeText(parentActivity.getApplicationContext(), R.string.no_auth_data, Toast.LENGTH_SHORT).show();
            return;
        }

        asyncTaskWithProgressBar = new AsyncTaskWithProgressBar<Void, ProgressPoint, Void>(parentActivity, R.id.progressText, R.id.progressBar) {
            private String message;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                ((MainActivity) parentActivity).setIsOrdersLoad(true);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    publishProgress(new ProgressPoint(parentActivity.getString(R.string.progress_begin_load), 0));
                    new DataSiteHelperV2().getOrders(login, password);
                    publishProgress(new ProgressPoint(parentActivity.getString(R.string.progress_end_load), 20));
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

                    AsyncTaskWithProgressBar<Void, ProgressPoint, Void> asyncTaskWithProgressBar = new AsyncTaskWithProgressBar<Void, ProgressPoint, Void>(parentActivity, R.id.progressText, R.id.progressBar) {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            try {
                                List<Order> orders = DataStorage.getOrders();
                                Integer incrementProgress = (80 / orders.size()) - 1;
                                Integer increment = 0;
                                Integer baseIncrement = 20;
                                for (Order iOrder : DataStorage.getOrders()) {
                                    publishProgress(
                                        new ProgressPoint(
                                            parentActivity.getString(R.string.progress_load_comments) + " " + iOrder.getAddress(),
                                            baseIncrement + increment * incrementProgress)
                                    );
                                    new DataSiteHelperV2().setComments(iOrder, login, password);
                                    iOrder.setGeoPoint(new GeoCoderHelper().getGeoPoint(iOrder.getAddress()));
                                    ++increment;
                                }
                                publishProgress(
                                    new ProgressPoint(parentActivity.getString(R.string.progress_end), 100)
                                );
                                ((MainActivity) parentActivity).setIsOrdersLoad(false);
                            } catch (Exception e) {
                                Log.d(TAG, Log.getStackTraceString(e));
                            }
                            return null;
                        }
                    };
                    this.childTasks.add(asyncTaskWithProgressBar);
                    asyncTaskWithProgressBar.execute();
                }
                if (message == null) {
                    if (DataStorage.getOrders().size() == 0) {
                        message = parentActivity.getString(R.string.no_orders);
                    } else {
                        message = parentActivity.getString(R.string.progress_end_load);
                    }
                }
                Toast.makeText(parentActivity.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        };
        asyncTaskWithProgressBar.execute();
    }

    public void setParentActivity(Activity parentActivity) {
        this.parentActivity = parentActivity;
        asyncTaskWithProgressBar.setActivity(parentActivity);
    }
}