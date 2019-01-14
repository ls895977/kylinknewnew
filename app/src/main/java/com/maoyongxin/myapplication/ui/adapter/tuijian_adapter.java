package com.maoyongxin.myapplication.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.tuijianInfo;
import com.maoyongxin.myapplication.ui.tuijian_web;

import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Yusr on 2018-05-08.
 */
public class tuijian_adapter extends RecyclerView.Adapter<tuijian_adapter.MyHolder> {
    List<tuijianInfo> list;
    Context context;
    LayoutInflater inflater;


    public tuijian_adapter(Context context, List<tuijianInfo> list) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_tuijian, parent, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyHolder myholder, int i) {
          final int position = i;

        final MyHolder holder=myholder;

        holder.tuijian_title.setText(list.get(position).getTuijian_title());
        holder.tuijian_subtitle.setText(list.get(position).getTuijian_subtitle());
        holder.tuijianurl=list.get(position).getTuijianurl().toString();
        holder.shareimg=list.get(position).getShare_img().toString();
        String headurl=list.get(i).getTuijian_img().toString();

        if(!headurl.equals(""))
        {
            holder.tuijian_img.setVisibility(VISIBLE);
            Glide.with(context).load(headurl).into(holder.tuijian_img);
        }
        else {
            holder.tuijian_img.setVisibility(GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent=new Intent(context, tuijian_web.class);
            intent.putExtra("tuijianurl",holder.tuijianurl);
            intent.putExtra("shareimg",holder.shareimg);
            intent.putExtra("targetUserId",list.get(position).getHostId());
            intent.putExtra("company_name","");
            intent.putExtra("communityId","");
            context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // 定义内部类继承ViewHolder
    class MyHolder extends RecyclerView.ViewHolder {

        TextView tuijian_title,tuijian_subtitle;
        String tuijianurl,shareimg;
        ImageView tuijian_img;
        public MyHolder(View view) {
            super(view);
            tuijian_title = (TextView) view.findViewById(R.id.tuijian_title);
            tuijian_subtitle = (TextView) view.findViewById(R.id.tuijian_subtitle);
            tuijian_img=(ImageView)view.findViewById(R.id.tuijian_img);
        }

    }

}
