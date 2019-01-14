package com.maoyongxin.myapplication.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.newsInfo;


import java.util.List;

/**
 * Created by yusr on 2018/5/7.
 */

public class news_adapter extends BaseAdapter {


    private Context mContext;
    private List<newsInfo> newsInfo=null;
    public news_adapter(Context mContext, List<newsInfo> newsInfo) {
        this.mContext = mContext;
        this.newsInfo=newsInfo;
    }
    public int getCount() {
        return this.newsInfo.size();
    }

    public Object getItem(int position) {
        return newsInfo.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {
        com.maoyongxin.myapplication.ui.adapter.news_adapter.ViewHolder viewHolder ;

        view = LayoutInflater.from(mContext).inflate(R.layout.item_news, null);

        viewHolder = new com.maoyongxin.myapplication.ui.adapter.news_adapter.ViewHolder();



        viewHolder.newsImag = (ImageView) view.findViewById(R.id.Img_news);
        viewHolder.newsTitle = (TextView) view.findViewById(R.id.Tv_newstitle);
        viewHolder.newsDate = (TextView) view.findViewById(R.id.Tv_newsDate);
        viewHolder.ImgUrl=(TextView)view.findViewById(R.id.imgurl);
        viewHolder.newsUrl=(TextView)view.findViewById(R.id.newsUrl);

        view.setTag(viewHolder);



        if(newsInfo.get(position)!=null){
            Glide.with(mContext).load(newsInfo.get(position).getNewsImg()).into(viewHolder.newsImag);
            viewHolder.newsTitle.setText(newsInfo.get(position).getNewsTitle());
            viewHolder.newsDate.setText(newsInfo.get(position).getNewsDate());
            viewHolder.ImgUrl.setText(newsInfo.get(position).getNewsImg());
            viewHolder.newsUrl.setText(newsInfo.get(position).getNewsurl());

        }else{

        }




        return view;
    }

    final static class ViewHolder {

        ImageView newsImag;
        TextView newsTitle;
        TextView  newsDate;
        TextView  ImgUrl;
        TextView  newsUrl;
    }
}
