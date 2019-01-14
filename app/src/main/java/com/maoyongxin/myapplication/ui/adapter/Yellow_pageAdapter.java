package com.maoyongxin.myapplication.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.FuwuList;
import com.maoyongxin.myapplication.entity.shanghuiInfo;
import com.maoyongxin.myapplication.entity.yellowPage_info;

import java.util.List;

/**
 * Created by 鱼呈瑞 on 2018/3/24.
 */

public class Yellow_pageAdapter  extends BaseAdapter{


    private Context mContext;

    private List<FuwuList> yellowPage_info=null;
    public Yellow_pageAdapter(Context mContext, List<FuwuList> yellowPage_info) {
        this.mContext = mContext;
        this.yellowPage_info=yellowPage_info;
    }
    private int currentItem = -1;

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


        final com.maoyongxin.myapplication.ui.adapter.Yellow_pageAdapter.ViewHolder viewHolder ;

        view = LayoutInflater.from(mContext).inflate(R.layout.item_yellowpage, null);
        viewHolder = new com.maoyongxin.myapplication.ui.adapter.Yellow_pageAdapter.ViewHolder();
        viewHolder.select_ln=(LinearLayout)view.findViewById(R.id.select_ln);
        viewHolder.companeName = (TextView) view.findViewById(R.id.companyName);
        viewHolder.companeType = (TextView) view.findViewById(R.id.companyType);

        view.setTag(viewHolder);



        if(yellowPage_info.get(position)!=null){

            viewHolder.companeName.setText(yellowPage_info.get(position).getFuwuName());
            viewHolder.companeType.setText(yellowPage_info.get(position).getFuwuId());


        }else{

            viewHolder.companeName.setText("");
            viewHolder.companeType.setText("");

        }

        if(currentItem==position)
        {
            viewHolder.companeName.setSelected(true);
            viewHolder.select_ln.setVisibility(View.VISIBLE);
            view.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }
        else{
            viewHolder.companeName.setSelected(false);
            viewHolder.select_ln.setVisibility(View.INVISIBLE);
            view.setBackgroundColor(mContext.getResources().getColor(R.color.white_somoke));
        }



        return view;

    }

    final static class ViewHolder {

        TextView  companeName;
        TextView  companeType;
        LinearLayout select_ln;
    }

}

