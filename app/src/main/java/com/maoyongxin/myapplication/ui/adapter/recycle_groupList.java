package com.maoyongxin.myapplication.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.GroupListInfo;
import com.maoyongxin.myapplication.entity.HuatiInfo;
import com.maoyongxin.myapplication.server.widget.SelectableRoundedImageView;
import com.maoyongxin.myapplication.ui.groupchat.huatiDetail;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Yusr on 2018-05-08.
 */
public class recycle_groupList extends RecyclerView.Adapter<recycle_groupList.MyHolder> {
    List<GroupListInfo.GroupList> list;
    Context context;
    LayoutInflater inflater;
    List<String> headList;

    public recycle_groupList(List<String> headList,List<GroupListInfo.GroupList> list, Context context) {
        this.list = list;
        this.headList=headList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.group_item_new, parent, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyHolder myholder, int i) {
          int position = i;



        myholder.tvTitle.setText(list.get(position).getGroupName());


        Glide.with(context).load(headList.get(i)).into(myholder.mImageView);







    }

    @Override
    public int getItemCount() {

        return list.size();
    }


    // 定义内部类继承ViewHolder
    class MyHolder extends RecyclerView.ViewHolder {

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
        TextView groupId;


        public MyHolder(View view) {
            super(view);

            tvTitle = (TextView) view.findViewById(R.id.groupname);
            mImageView = (SelectableRoundedImageView) view.findViewById(R.id.groupuri);
            groupId = (TextView) view.findViewById(R.id.group_id);

        }

    }







}
