package NetUtils.AsyncTask;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

public class AsyncTaskWithProgressBar<T, V extends ProgressPoint, W> extends AsyncTask<T, V, W> {
    private Activity activity;
    private TextView textView;
    private ProgressBar progressBar;
    private int textViewId;
    private int progressBarId;
    protected List<AsyncTaskWithProgressBar<Void, ProgressPoint, Void>> childTasks = new LinkedList<>();

    public AsyncTaskWithProgressBar(Activity activity, int textViewId, int progressBarId) {
        this.activity = activity;
        this.textViewId = textViewId;
        this.progressBarId = progressBarId;
        synchronizeViews();
    }

    @Override
    protected W doInBackground(T... ts) {
        return null;
    }

    @Override
    protected void onProgressUpdate(V... values) {
        super.onProgressUpdate(values);
        if (values.length >= 1 && values[0] != null) {
            setProgress(values[0]);
        }
    }

    protected void setProgress(ProgressPoint progressPoint) {
        Integer progress = progressPoint.getProgress();
        if (progress < 100) {
            textView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        } else if (progress == 100) {
            textView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }
        textView.setText(progressPoint.getMessage());
        progressBar.setProgress(progress);
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
        synchronizeViews();
        for(AsyncTaskWithProgressBar i : childTasks){
            i.setActivity(activity);
        }
    }

    protected void synchronizeViews(){
        this.textView = (TextView) activity.findViewById(textViewId);
        this.progressBar = (ProgressBar) activity.findViewById(progressBarId);
    }
}
