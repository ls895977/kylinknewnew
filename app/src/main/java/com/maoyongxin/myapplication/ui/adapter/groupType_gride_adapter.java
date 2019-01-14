package com.maoyongxin.myapplication.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.maoyongxin.myapplication.entity.tuijianInfo;
import com.maoyongxin.myapplication.server.widget.SelectableRoundedImageView;
import com.maoyongxin.myapplication.ui.editapp.renmai.shangjiaList;
import com.maoyongxin.myapplication.ui.news.activity.BannerLayout;
import com.maoyongxin.myapplication.ui.tuijian_web;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by 鱼呈瑞 on 2018/3/24.
 */

public class groupType_gride_adapter extends BaseAdapter{
    private Context mContext;
    private int currentItem ;
    private List<grideInfo> yellowPage_info=new ArrayList<>();

    public groupType_gride_adapter(Context mContext, List<grideInfo> yellowPage_info) {
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

        final groupType_gride_adapter.ViewHolder viewHolder ;

        view = LayoutInflater.from(mContext).inflate(R.layout.item_grouptype_gride_layout, null);

        viewHolder = new groupType_gride_adapter.ViewHolder();
        viewHolder.fuwuname=(TextView)view.findViewById(R.id.fuwuname);
        viewHolder.funaImg = (SelectableRoundedImageView) view.findViewById(R.id.img_fuwu);
        viewHolder.fuwuId=(TextView)view.findViewById(R.id.gridId);

        view.setTag(viewHolder);



        if(yellowPage_info.get(position)!=null){

            viewHolder.fuwuname.setText(yellowPage_info.get(position).getFuwuName());
            viewHolder.fuwuId.setText(yellowPage_info.get(position).getFuwuId().toString());
            Glide.with(mContext).load("http://st.3dgogo.com/"+yellowPage_info.get(position).getFuwuImg()).into(viewHolder.funaImg);



        }
        if(currentItem==position)
        {
            viewHolder.fuwuname.setSelected(true);
            viewHolder.fuwuname.setTextColor(mContext.getResources().getColor(R.color.blue_tiny));
        }
        else{
            viewHolder.fuwuname.setSelected(false);
            viewHolder.fuwuname.setTextColor(mContext.getResources().getColor(R.color.text_tab));

        }




        return view;



    }

    final static class ViewHolder {

        TextView  fuwuname;
        SelectableRoundedImageView funaImg;
        TextView  fuwuId;
    }
}

