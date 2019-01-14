package com.maoyongxin.myapplication.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.fuwuInfo;
import com.maoyongxin.myapplication.ui.News_web;

import java.util.List;

/**
 * Created by 鱼呈瑞 on 2018/3/24.
 */

public class shangjiaList_adapter extends BaseAdapter{


        private Context mContext;
        private int currentItem = -1;
        private List<fuwuInfo> yellowPage_info=null;
        public shangjiaList_adapter(Context mContext, List<fuwuInfo> yellowPage_info) {
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


            final shangjiaList_adapter.ViewHolder viewHolder ;

            view = LayoutInflater.from(mContext).inflate(R.layout.item_fuwu_shangjia, null);
            viewHolder = new shangjiaList_adapter.ViewHolder();

            viewHolder.fuwuname=(TextView)view.findViewById(R.id.fwsName);
            viewHolder.fuwuImg = (ImageView) view.findViewById(R.id.fwshead);
            viewHolder.adress=(TextView)view.findViewById(R.id.fwsadress);
            viewHolder.shagnjiaIntro=(TextView)view.findViewById(R.id.fwsIntro);

            view.setTag(viewHolder);



            if(yellowPage_info.get(position)!=null){

                viewHolder.fuwuname.setText(yellowPage_info.get(position).getFwsName());
                viewHolder.shagnjiaIntro.setText(yellowPage_info.get(position).getFwsIntro().toString());
                viewHolder.adress.setText(yellowPage_info.get(position).getAdress().toString());
                viewHolder.fwsDeatails=(yellowPage_info.get(position).getFwsDetail().toString());
                Glide.with(mContext).load(yellowPage_info.get(position).getFwsHEadIma()).into(viewHolder.fuwuImg);
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
                    Intent intent=new Intent(mContext,News_web.class);
                    intent.putExtra("weburl",viewHolder.fwsDeatails);
                    mContext.startActivity(intent);
                }
            });
            return view;

        }

        final static class ViewHolder {
            ImageView fuwuImg;
            TextView  fuwuname;
            TextView  shagnjiaIntro;
            TextView   adress;
            String fwsDeatails;
        }

    }

