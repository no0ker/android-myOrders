package com.example.rush0714.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import NetUtils.Orders.Comment;
import NetUtils.Orders.Order;

public class ExpListAdapter extends BaseExpandableListAdapter {
    public static final String TAG = "ExpListAdapter";
    private Context mContext;
    private List<Order> orders;
    private static SimpleDateFormat ELEMENT_TIME_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    private static SimpleDateFormat GROUP_TIME_FORMAT = new SimpleDateFormat("HH:mm");

    public ExpListAdapter(Context context, List<Order> orders) {
        mContext = context;
        this.orders = orders;
    }

    @Override
    public int getGroupCount() {
        return orders.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (orders.get(groupPosition).getComments().isEmpty()) {
            return 0;
        }
        return orders.get(groupPosition).getComments().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return orders.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return orders.get(groupPosition).getComments().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Order currentOrder = orders.get(groupPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_group, null);
        }

        TextView groupCustomer = (TextView) convertView.findViewById(R.id.groupCustomer);
        groupCustomer.setText(currentOrder.getName());

        TextView groupComment = (TextView) convertView.findViewById(R.id.groupComment);
        groupComment.setText(currentOrder.getComment());

        if (isExpanded) {
            List<Comment> comments = currentOrder.getComments();
            if (!comments.isEmpty() || comments.size() > 0) {
                groupComment.setVisibility(View.GONE);
            }
        } else {
            groupComment.setVisibility(View.VISIBLE);
        }

        TextView groupText = (TextView) convertView.findViewById(R.id.groupTime);
        groupText.setText(GROUP_TIME_FORMAT.format(currentOrder.getTime()));
        groupText.setTextColor(Color.parseColor(currentOrder.getColor()));

        TextView groupTime = (TextView) convertView.findViewById(R.id.groupText);
        groupTime.setText(currentOrder.getAddress());
        groupTime.setTextColor(Color.parseColor(currentOrder.getColor()));

        if (currentOrder.getIsBenefit()) {
            convertView.findViewById(R.id.isBenefit).setVisibility(View.VISIBLE);
        } else {
            convertView.findViewById(R.id.isBenefit).setVisibility(View.GONE);
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_element, null);
        }

        Comment comment = orders.get(groupPosition).getComments().get(childPosition);
        int color = Color.parseColor(comment.getColor());
        ((TextView) convertView.findViewById(R.id.elementColorBox)).setBackgroundColor(color);

        TextView elementTime = (TextView) convertView.findViewById(R.id.elementTime);
        elementTime.setText(ELEMENT_TIME_FORMAT.format(comment.getDate()));

        TextView elementAuthor = (TextView) convertView.findViewById(R.id.elementAuthor);
        elementAuthor.setText(comment.getUser());

        TextView elementEvent = (TextView) convertView.findViewById(R.id.elementEvent);
        elementEvent.setText(comment.getEvent());

        TextView elementText = (TextView) convertView.findViewById(R.id.elementText);
        elementText.setText(comment.getMessage());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public boolean onGroupLongClick(int groupPosition) {
        return false;
    }
}