package com.maoyongxin.myapplication.ui.adapter;

import android.content.Context;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.GroupListInfo;
import com.maoyongxin.myapplication.entity.mygroupList;
import com.maoyongxin.myapplication.server.widget.SelectableRoundedImageView;
import com.maoyongxin.myapplication.ui.GroupDetailActivity;
import com.maoyongxin.myapplication.ui.GroupListActivity;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import io.rong.imkit.RongIM;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by maoyongxin on 2018/1/10.
 */

public class GroupAdapter extends BaseAdapter {
    private Context context;
    private List<mygroupList> list;
    private LayoutInflater inflater;
    private List<String> headlist;
    public GroupAdapter(List<mygroupList> list,Context context) {
        this.context = context;
        this.headlist=headlist;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder Holder;
        if (convertView == null) {
            Holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.group_item_new, null);
            Holder.tvTitle = (TextView) convertView.findViewById(R.id.groupname);
            Holder.mImageView = (SelectableRoundedImageView) convertView.findViewById(R.id.groupuri);
            Holder.groupId=(TextView)convertView.findViewById(R.id.group_id);

            convertView.setTag(Holder);
        } else {
            Holder = (ViewHolder) convertView.getTag();
        }

        Holder.tvTitle.setText(list.get(position).getGroupName());
        Holder.groupId.setText("商会号："+list.get(position).getGroupId());
        Glide.with(context).load(list.get(position).getImage()).into(Holder.mImageView);


        return convertView;
    }

    class ViewHolder {
        /**
         * 昵称
         */
        TextView tvTitle;
        /**
         * 头像
         */
        SelectableRoundedImageView mImageView;
        /**
         * userId
         */
        TextView  groupId;
    }



}
