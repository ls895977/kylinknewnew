package com.maoyongxin.myapplication.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.server.utils.NToast;
import com.maoyongxin.myapplication.ui.mingpianInfo;
public class mingpianAdaper extends ArrayAdapter<mingpianInfo> {

    private int rescourchid;

    @Override
    public int getCount() {
        return super.getCount();
    }

    public mingpianAdaper(Context content, int textViewResourceid, List<mingpianInfo> objects)
    {
        super(content,textViewResourceid,objects);
        rescourchid=textViewResourceid;


    }



    @NonNull


    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final mingpianInfo mp=getItem(position);
        View view;
        ViewHolder viewHolder;

        view= LayoutInflater.from(getContext()).inflate(rescourchid,parent,false);
        viewHolder = new ViewHolder();

        viewHolder.companyname = (TextView) view.findViewById(R.id.companyName);
        viewHolder.legalPersonName=(TextView)view.findViewById(R.id.bossName);
        viewHolder.zhiwei=(TextView)view.findViewById(R.id.zhiwei);
        viewHolder.TelNumber=(TextView)view.findViewById(R.id.tel_number);
        viewHolder.email=(TextView)view.findViewById(R.id.emaill);
        viewHolder.webAdress=(TextView)view.findViewById(R.id.webadress);
        viewHolder.businessScope=(TextView) view.findViewById(R.id.businessScope);
        viewHolder.bt_deleteMingpian=(Button) view.findViewById(R.id.bt_deleteMingpian);

        view.setTag(convertView);

        viewHolder.companyname.setText(mp.getcompanyName());
        viewHolder.legalPersonName.setText(mp.getlegalPersonName());
        viewHolder. zhiwei.setText(mp.getzhiwei());
        viewHolder.TelNumber.setText(mp.getTelNumber());
        viewHolder.email.setText(mp.getemail());
        viewHolder.webAdress.setText(mp.webAdress());
        viewHolder.businessScope.setText(mp.getZhuying());



        return view;
    }
    class ViewHolder{

        TextView companyname;
        TextView legalPersonName;
        TextView zhiwei;
        TextView TelNumber;
        TextView email;
        TextView webAdress;
        TextView businessScope;
        Button bt_deleteMingpian;



    }



}

