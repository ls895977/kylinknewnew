package com.maoyongxin.myapplication.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.InterestingInfo;

import java.util.List;

/**
 * Created by maoyongxin on 2017/12/7.
 */

public class InterestingGridAdapter extends BaseAdapter {
    private List<InterestingInfo.InterestList> interestLists;
    private Context context;
    private LayoutInflater inflater;

    public InterestingGridAdapter(List<InterestingInfo.InterestList> interestLists, Context context) {
        this.interestLists = interestLists;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return interestLists.size();
    }

    @Override
    public Object getItem(int position) {
        return interestLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_interesting_list, null);
            holder.tv_myInteresting = (TextView) convertView.findViewById(R.id.tv_myInteresting);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_myInteresting.setText(interestLists.get(position).getInterestName());
        holder.tv_myInteresting.setTextColor(context.getResources().getColor(R.color.text_deco));
        if (interestLists.get(position).getLevel() == 1) {
            holder.tv_myInteresting.setBackground(context.getResources().getDrawable(R.drawable.item_interest1_bg));
        } else if (interestLists.get(position).getLevel() == 2) {
            holder.tv_myInteresting.setBackground(context.getResources().getDrawable(R.drawable.item_interest2_bg));
        } else {
            holder.tv_myInteresting.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
        final ViewHolder finalHolder = holder;
        return convertView;
    }

    class ViewHolder {
        TextView tv_myInteresting;
    }

}
