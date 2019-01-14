package com.maoyongxin.myapplication.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jky.baselibrary.util.common.TimeUtil;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.CommunityListInfo;

import java.util.List;

/**
 * Created by maoyongxin on 2017/12/7.
 */

public class NearbyCommunityListAdapter extends BaseAdapter {
    private List<CommunityListInfo.CommnunityList> commnunityLists;
    private Context context;
    private LayoutInflater inflater;

    public NearbyCommunityListAdapter(List<CommunityListInfo.CommnunityList> commnunityLists, Context context) {
        this.commnunityLists = commnunityLists;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return commnunityLists.size();
    }

    @Override
    public Object getItem(int position) {
        return commnunityLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_nearby_communitylist, null);
            holder.tv_communityName = (TextView) convertView.findViewById(R.id.tv_communityName);
            holder.tv_communityCreatTime = (TextView) convertView.findViewById(R.id.tv_communityCreatTime);
            holder.tv_communityNote = (TextView) convertView.findViewById(R.id.tv_communityNote);
            holder.tv_communityAddress = (TextView) convertView.findViewById(R.id.tv_communityAddress);
            holder.img_community = (ImageView) convertView.findViewById(R.id.img_community);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(commnunityLists.get(position).getCommunityImg()!=null&&!commnunityLists.get(position).getCommunityImg().equals("")){
            Glide.with(context).load(commnunityLists.get(position).getCommunityImg()).into(holder.img_community);
        }else{
            Glide.with(context).load(R.mipmap.community_icon_default).into(holder.img_community);
        }
        holder.tv_communityName.setText(commnunityLists.get(position).getCommunityName());
        holder.tv_communityCreatTime.setText(TimeUtil.getFormatYMDFromMillis(commnunityLists.get(position).getCreatTime(),"yyyy-MM-dd"));
        holder.tv_communityNote.setText(commnunityLists.get(position).getCommunityNote());
        holder.tv_communityAddress.setText(commnunityLists.get(position).getAddressName());
        return convertView;
    }

    class ViewHolder {
        ImageView img_community;
        TextView tv_communityName, tv_communityCreatTime,tv_communityNote,tv_communityAddress;
    }
}
