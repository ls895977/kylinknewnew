package com.maoyongxin.myapplication.ui.adapter;

import android.content.Context;
import android.content.Intent;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.grideInfo;

import com.maoyongxin.myapplication.ui.groupchat.GroupChatDetailNewActivity;


import java.util.List;



/**
 * Created by 鱼呈瑞 on 2018/3/24.
 */

public class group_gride_adapter extends BaseAdapter{


    private Context mContext;
    private int currentItem = -1;
    private List<grideInfo> yellowPage_info=null;


    public group_gride_adapter(Context mContext, List<grideInfo> yellowPage_info) {
        this.mContext = mContext;
        this.yellowPage_info=yellowPage_info;

    }




    public void setCurrentItem(int currentItem) {
        this.currentItem = currentItem;
    }

    public int getCount() {
        return this.yellowPage_info.size();
    }

    public Object getItem(int position) {
        return yellowPage_info.get(position);
    }

    public long getItemId(int position) {
        return position;
    }


    public View getView(final int position, View view, ViewGroup arg2) {

        final group_gride_adapter.ViewHolder viewHolder ;

        view = LayoutInflater.from(mContext).inflate(R.layout.item_gride_group_layout, null);


        if (view == null || view.getTag() == null) {
            viewHolder = new group_gride_adapter.ViewHolder();
            viewHolder.fuwuname=(TextView)view.findViewById(R.id.fuwuname);
            viewHolder.funaImg = (ImageView) view.findViewById(R.id.img_fuwu);
            viewHolder.fuwuId=(TextView)view.findViewById(R.id.gridId);
            view.setTag(viewHolder);
        }
        else{
            viewHolder=(group_gride_adapter.ViewHolder)view.getTag();
        }








        if(yellowPage_info.get(position)!=null){

            viewHolder.fuwuname.setText(yellowPage_info.get(position).getFuwuName());
            viewHolder.fuwuId.setText(yellowPage_info.get(position).getFuwuId().toString());
            Glide.with(mContext).load(yellowPage_info.get(position).getFuwuImg()).into(viewHolder.funaImg);



        }
        else{



        }

        if(currentItem==position)
        {
            viewHolder.fuwuname.setSelected(true);

        }
        else{
            viewHolder.fuwuname.setSelected(false);

        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext,GroupChatDetailNewActivity.class);
                intent.putExtra("picUrl",yellowPage_info.get(position).getFuwuImg().toString());
                intent.putExtra("groupName",yellowPage_info.get(position).getFuwuName().toString());
                intent.putExtra("groupNum",yellowPage_info.get(position).getFuwuId().toString());
                mContext.startActivity(intent);
            }
        });

        return view;



    }

      class ViewHolder {

        TextView  fuwuname;
        ImageView funaImg;
        TextView  fuwuId;

    }





}

