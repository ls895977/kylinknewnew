package com.maoyongxin.myapplication.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.PoiInfoBean;

import java.util.List;

/**
 * Created by maoyongxin on 2017/12/4.
 */

public class PoiListAdapter extends BaseAdapter {
    private Context context;
    private List<PoiInfoBean> poiBeans;
    private LayoutInflater inflater;
    private OnLineClicklistener onLineClicklistener;

    public void setOnLineClicklistener(OnLineClicklistener lineClicklistener){
        this.onLineClicklistener=lineClicklistener;
    }
    private OnDetailClicklistener onDetailClicklistener;
    public void setOnDetailClicklistener(OnDetailClicklistener onDetailClicklistener){
        this.onDetailClicklistener=onDetailClicklistener;
    }

    public PoiListAdapter(Context context, List<PoiInfoBean> poiBeans) {
        this.context = context;
        this.poiBeans = poiBeans;
        this.inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return poiBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return poiBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            holder=new ViewHolder();
            convertView=inflater.inflate(R.layout.item_poi_list,null);
            holder.tv_poiName= (TextView) convertView.findViewById(R.id.tv_poiName);
            holder.tv_poiType= (TextView) convertView.findViewById(R.id.tv_poiType);
            holder.tv_poiDistance= (TextView) convertView.findViewById(R.id.tv_poiDistance);
            holder.tv_poiAddress= (TextView) convertView.findViewById(R.id.tv_poiAddress);
            holder.tv_detail= (TextView) convertView.findViewById(R.id.tv_detail);
            holder.line= (LinearLayout) convertView.findViewById(R.id.line);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        holder.tv_poiName.setText(poiBeans.get(position).getPoiName());
        holder.tv_poiType.setText(poiBeans.get(position).getPoiType());
        holder.tv_poiAddress.setText(poiBeans.get(position).getPoiAddress());
        holder.line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLineClicklistener.lineClick(position);
            }
        });
        holder.tv_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDetailClicklistener.goDetail(poiBeans.get(position).getPoiName(),poiBeans.get(position).getPoiAddress(),poiBeans.get(position).getLatitude(),poiBeans.get(position).getLngtitude(),poiBeans.get(position).getTargetId(),poiBeans.get(position).getPoinote());
            }
        });
        return convertView;
    }
    private class ViewHolder{
        TextView tv_poiName,tv_poiType,tv_poiDistance,tv_poiAddress,tv_detail;
        LinearLayout line;
    }
    public interface OnLineClicklistener{
        void lineClick(int position);
    }
    public interface OnDetailClicklistener{
        void goDetail(String poiName,String snippet,String latitude,String lngtitude,String targetid,int targettype);
    }
}
