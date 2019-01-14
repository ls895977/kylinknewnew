package com.maoyongxin.myapplication.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.MyInterestingInfo;

import java.util.List;

/**
 * Created by maoyongxin on 2017/12/7.
 */

public class MyInterestingGridAdapter extends BaseAdapter {
    private List<MyInterestingInfo.UserInterestList>interestLists;
    private Context context;
    private LayoutInflater inflater;

    public MyInterestingGridAdapter(List<MyInterestingInfo.UserInterestList> interestLists, Context context) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            holder=new ViewHolder();
            convertView=inflater.inflate(R.layout.item_myinteresting_grid,null);
            holder.tv_myInteresting= (TextView) convertView.findViewById(R.id.tv_myInteresting);
            Drawable drawable = context.getResources().getDrawable(R.drawable.rc_cs_delete);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            holder.tv_myInteresting.setCompoundDrawables(null, null, drawable, null);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        holder.tv_myInteresting.setText(interestLists.get(position).getInterestName());
        holder.tv_myInteresting.setBackgroundResource(R.drawable.bg_radius_blue);
        return convertView;
    }
    class  ViewHolder{
        TextView tv_myInteresting;
    }
}
