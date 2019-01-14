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
import com.maoyongxin.myapplication.entity.shanghuiInfo;
import com.maoyongxin.myapplication.ui.groupchat.GroupChatDetailActivity;

import java.util.List;


/**
 * Created by maoyongxin on 2017/9/12.
 */

public class shanghuiAdapter extends BaseAdapter {


    private Context mContext;
    private List<shanghuiInfo> shanghui_info=null;
    public shanghuiAdapter(Context mContext, List<shanghuiInfo> shanghuiinfo) {
        this.mContext = mContext;
        this.shanghui_info=shanghuiinfo;
    }


    public int getCount() {
        return this.shanghui_info.size();
    }

    public Object getItem(int position) {
        return shanghui_info.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {


        ViewHolder viewHolder ;
        view = LayoutInflater.from(mContext).inflate(R.layout.item_remen_list_item, null);
        viewHolder = new ViewHolder();



        viewHolder.img_jingxuan = (ImageView) view.findViewById(R.id.img_jingxuan);
        viewHolder.name_shanghui = (TextView) view.findViewById(R.id.name_shanghui);
        viewHolder.num_shanghui = (TextView) view.findViewById(R.id.num_shanghui);
        viewHolder.headurl = (TextView) view.findViewById(R.id.headurl);
        viewHolder.memnum=(TextView)view.findViewById(R.id.memnum);
        viewHolder.topicNum=(TextView)view.findViewById(R.id.topicNum);
        viewHolder.groupTheme=(TextView)view.findViewById(R.id.groupTheme);
        view.setTag(viewHolder);




        if(shanghui_info.get(position)!=null){
            Glide.with(mContext).load(shanghui_info.get(position).getPic()).into(viewHolder.img_jingxuan);
            viewHolder.name_shanghui.setText(shanghui_info.get(position).getGroupName());
            viewHolder.num_shanghui.setText(shanghui_info.get(position).getGroupNum());
            viewHolder.headurl.setText(shanghui_info.get(position).getPic());
            viewHolder.memnum.setText(shanghui_info.get(position).getMemNum());
            viewHolder.topicNum.setText("话题总数："+shanghui_info.get(position).getTopicsNum());
            viewHolder.groupTheme.setText("商会主题："+shanghui_info.get(position).getGroupTheme());
        }else{
            Glide.with(mContext).load(R.mipmap.gb_mine_head).into(viewHolder.img_jingxuan);
            viewHolder.name_shanghui.setText("");
            viewHolder.num_shanghui.setText("");
            viewHolder.headurl.setText("");
            viewHolder.memnum.setText("");
            viewHolder.topicNum.setText("");
            viewHolder.groupTheme.setText("");

        }


        return view;

    }

    final static class ViewHolder {
        ImageView img_jingxuan;
        TextView  name_shanghui;
        TextView  num_shanghui;
        TextView  headurl;
        TextView  memnum;
        TextView  groupTheme;
        TextView  topicNum;
    }

}
