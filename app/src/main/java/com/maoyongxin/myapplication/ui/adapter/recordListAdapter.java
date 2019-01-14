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
import com.maoyongxin.myapplication.entity.recordInfo;
import com.maoyongxin.myapplication.entity.shanghuiInfo;

import java.util.List;


/**
 * Created by maoyongxin on 2017/9/12.
 */

public class recordListAdapter extends BaseAdapter {


    private Context mContext;
    private List<recordInfo> recordInfos=null;
    public recordListAdapter(Context mContext, List<recordInfo> recordInfos) {
        this.mContext = mContext;
        this.recordInfos=recordInfos;
 }


    public int getCount() {
        return this.recordInfos.size();
    }

    public Object getItem(int position) {
        return recordInfos.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {


            ViewHolder viewHolder ;
            view = LayoutInflater.from(mContext).inflate(R.layout.item_renmai_record, null);
            viewHolder = new ViewHolder();



            viewHolder.cpnNameRcd = (TextView) view.findViewById(R.id.cpnNameRcd);
            viewHolder.teleCpn = (TextView) view.findViewById(R.id.cpnTele);
            viewHolder.rcdTime = (TextView) view.findViewById(R.id.rcdTime);

            viewHolder.teleInfo = (ImageView) view.findViewById(R.id.teleInfo);
            viewHolder.smgInfo = (ImageView) view.findViewById(R.id.smgInfo);
            viewHolder.scInfo = (ImageView) view.findViewById(R.id.scInfo);


            view.setTag(viewHolder);

      //     viewHolder = (ViewHolder) view.getTag();


        if(recordInfos.get(position)!=null){

            viewHolder.cpnNameRcd.setText(recordInfos.get(position).getcompanyName());
            viewHolder.teleCpn.setText(recordInfos.get(position).getcompanyTele());

            viewHolder.rcdTime.setText(recordInfos.get(position).getrecordTime());




            if(recordInfos.get(position).getteleinfo().equals("0"))
            {
                viewHolder.teleInfo.setImageResource(R.mipmap.icon_ctele2);
            }
            else
            {
                viewHolder.teleInfo.setImageResource(R.mipmap.icon_ctele);
            }

            if(recordInfos.get(position).getsmginfo().equals("0"))
            {
                viewHolder.smgInfo.setImageResource(R.mipmap.icon_cmsg2);
            }
            else
            {
                viewHolder.smgInfo.setImageResource(R.mipmap.icon_cmsg);
            }
            if(recordInfos.get(position).getscinfo().equals("0"))
            {
                viewHolder.scInfo.setImageResource(R.mipmap.icon_ilike2);
            }
            else
            {
                viewHolder.scInfo.setImageResource(R.mipmap.icon_clike);
            }





        }else{

        }


        return view;

    }

    final static class ViewHolder {

        TextView  cpnNameRcd;
        TextView  teleCpn;
        TextView  rcdTime;

        ImageView  teleInfo;
        ImageView  smgInfo;
        ImageView  scInfo;

    }

}
