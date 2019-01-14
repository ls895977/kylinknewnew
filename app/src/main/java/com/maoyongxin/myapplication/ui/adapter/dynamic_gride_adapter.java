package com.maoyongxin.myapplication.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.hotDynamicInfo;
import com.maoyongxin.myapplication.ui.StrangerDetailActivity;

import java.util.List;

/**
 * Created by 鱼呈瑞 on 2018/3/24.
 */

public class dynamic_gride_adapter extends BaseAdapter{


    private Context mContext;
    private int currentItem = -1;
    private List<hotDynamicInfo> dynamicList;

    public dynamic_gride_adapter(Context mContext, List<hotDynamicInfo> dynamicList) {
        this.mContext = mContext;
        this.dynamicList=dynamicList;

    }




    public void setCurrentItem(int currentItem) {
        this.currentItem = currentItem;
    }

    public int getCount() {
        return this.dynamicList.size();
    }

    public Object getItem(int position) {
        return dynamicList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }


    public View getView(final int position, View view, ViewGroup arg2) {

        final dynamic_gride_adapter.ViewHolder viewHolder ;

        view = LayoutInflater.from(mContext).inflate(R.layout.item_hotdynamic, null);
        viewHolder = new dynamic_gride_adapter.ViewHolder();
        viewHolder.dynamictitle=(TextView)view.findViewById(R.id.dynamic_title);
        viewHolder.dynamic_pic=(ImageView)view.findViewById(R.id.endIcon);
        view.setTag(viewHolder);

        if(dynamicList.get(position)!=null){

            viewHolder.dynamictitle.setText(dynamicList.get(position).getDynamicContent());
            viewHolder.dynamicId=dynamicList.get(position).getDynamicId();
        }


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext,StrangerDetailActivity.class);
                intent.putExtra("personId",dynamicList.get(position).getUserId());
                mContext.startActivity(intent);
            }
        });

        return view;



    }

    final static class ViewHolder {

        TextView  dynamictitle;
        ImageView dynamic_pic;
        String dynamicId;

    }





}

