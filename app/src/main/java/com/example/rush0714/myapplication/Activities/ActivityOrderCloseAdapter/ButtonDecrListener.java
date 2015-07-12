package com.example.rush0714.myapplication.Activities.ActivityOrderCloseAdapter;

    import android.view.View;
    import android.widget.TextView;

    import com.example.rush0714.myapplication.R;

    import java.util.Map;

public class ButtonDecrListener implements View.OnClickListener {
    private View parentView;
    private Map<Integer, Integer> csOrderServicesCounts;
    private Integer position;
    protected Integer count;

    public ButtonDecrListener(View parentView, Map<Integer, Integer> csOrderServicesCounts, Integer position) {
        this.parentView = parentView;
        this.csOrderServicesCounts = csOrderServicesCounts;
        this.position = position;
    }

    @Override
    public void onClick(View view) {
        TextView priceView = (TextView) parentView.findViewById(R.id.price);
        TextView priceEnd = (TextView) parentView.findViewById(R.id.priceEnd);
        TextView countView = (TextView) parentView.findViewById(R.id.count);

        Integer price = Integer.parseInt((String)priceView.getText());
        Integer count = Integer.parseInt((String) countView.getText());

        --count;
        if(count < 0){
            count = 0;
        }

        Integer priceEndInt = count * price;

        countView.setText("" + count.toString());
        priceEnd.setText("" + priceEndInt.toString());

        csOrderServicesCounts.put(position, count);
    }
}
