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

public class Fuwu_gride_adapter extends BaseAdapter{


    private Context mContext;
    private int currentItem = -1;
    private List<grideInfo> yellowPage_info=null;
    private tuijian_adapter tuijianAdapter;
    private List<tuijianInfo> tuijianList=new ArrayList<>();
    private List<tuijianInfo> tuijianBanner=new ArrayList<>();
    private  List<String> res = new ArrayList<>();
    private  List<String> titles = new ArrayList<>();

    public Fuwu_gride_adapter(Context mContext, List<grideInfo> yellowPage_info,List<tuijianInfo> tuijianList,List<tuijianInfo> tuijianBanner,List<String> res,List<String> titles) {
        this.mContext = mContext;
        this.yellowPage_info=yellowPage_info;
        this.tuijianList=tuijianList;
        this.res=res;
        this.titles=titles;
        this.tuijianBanner=tuijianBanner;
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

        final Fuwu_gride_adapter.ViewHolder viewHolder ;

        view = LayoutInflater.from(mContext).inflate(R.layout.item_gride_layout, null);
        viewHolder = new Fuwu_gride_adapter.ViewHolder();



        viewHolder.fuwuname=(TextView)view.findViewById(R.id.fuwuname);
        viewHolder.funaImg = (ImageView) view.findViewById(R.id.img_fuwu);
        viewHolder.fuwuId=(TextView)view.findViewById(R.id.gridId);
        viewHolder.tuijianList=(RecyclerView)view.findViewById(R.id.tuijianList);
        viewHolder.tuijianHead=(LinearLayout)view.findViewById(R.id.tuijianHead);
        viewHolder.banner_tuijian=(BannerLayout)view.findViewById(R.id.banne_tuijian);
        viewHolder.card_fuwu=(CardView)view.findViewById(R.id.card_fuwu);
        viewHolder.tuijianList.setAdapter(tuijianAdapter);
        view.setTag(viewHolder);



        if(yellowPage_info.get(position)!=null){

            viewHolder.fuwuname.setText(yellowPage_info.get(position).getFuwuName());
            viewHolder.fuwuId.setText(yellowPage_info.get(position).getFuwuId().toString());
            Glide.with(mContext).load(yellowPage_info.get(position).getFuwuImg()).into(viewHolder.funaImg);

            if(position==0)
            {

                viewHolder.tuijianHead.setVisibility(VISIBLE);


                viewHolder.tuijianList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                viewHolder.tuijianList.setItemAnimator(null);
                viewHolder.tuijianList.setHasFixedSize(true);
                viewHolder.tuijianList.setNestedScrollingEnabled(false);

                tuijianAdapter=new tuijian_adapter(mContext,tuijianList);

                viewHolder.banner_tuijian.setViewUrls(res,titles);

                viewHolder.banner_tuijian.setOnBannerItemClickListener(new BannerLayout.OnBannerItemClickListener() {
                    @Override
                    public void onItemClick(int position_tuijian ) {
                        Intent intent = new Intent(mContext, tuijian_web.class);
                        intent.putExtra("tuijianurl",tuijianBanner.get(position_tuijian).getTuijianurl().toString());
                        intent.putExtra("shareimg", tuijianBanner.get(position_tuijian).getShare_img().toString());
                        mContext.startActivity(intent);
                    }
                });





            }

        }
        else{
            viewHolder.tuijianHead.setVisibility(GONE);


        }

        if(currentItem==position)
        {
            viewHolder.fuwuname.setSelected(true);

        }
        else{
            viewHolder.fuwuname.setSelected(false);

        }

        viewHolder.card_fuwu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, shangjiaList.class);
                intent.putExtra("fenleiId", yellowPage_info.get(position).getFuwuId().toString());
                mContext.startActivity(intent);
            }
        });

        return view;



    }

    final static class ViewHolder {

        TextView  fuwuname;
        ImageView funaImg;
        TextView  fuwuId;
        RecyclerView tuijianList;
        LinearLayout tuijianHead;
        BannerLayout banner_tuijian;
        CardView card_fuwu;
    }





}

