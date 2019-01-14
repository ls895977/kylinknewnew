package com.maoyongxin.myapplication.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.PictureEntity;
import com.maoyongxin.myapplication.entity.allGroupinfo;
import com.maoyongxin.myapplication.server.widget.SelectableRoundedImageView;
import com.maoyongxin.myapplication.ui.ShowPictureDialogActivity;
import com.maoyongxin.myapplication.ui.VideoActivity;
import com.maoyongxin.myapplication.ui.groupchat.GroupChatDetailNewActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by maoyongxin on 2017/9/12.
 */

public class GridView_allgrouplist_adapter extends BaseAdapter {
    private List<allGroupinfo> list = null;
    private Context mContext;
    private ArrayList<String> imgurl=null;
    public GridView_allgrouplist_adapter(Context mContext, List<allGroupinfo> list) {
        this.mContext = mContext;
        this.list = list;


    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<allGroupinfo> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {
         final   ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();

            view = LayoutInflater.from(mContext).inflate(R.layout.item_grid_allgrouplist, null);

            viewHolder.img_project_follow = (SelectableRoundedImageView) view.findViewById(R.id.img_project_follow);
            viewHolder.bt_follow = (Button) view.findViewById(R.id.bt_follow);
            viewHolder.groupName=(TextView)view.findViewById(R.id.groupName);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }


            Glide.with(mContext).load(list.get(position).getImage()).into(viewHolder.img_project_follow);
            viewHolder.groupName.setText(list.get(position).getGroupName());


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent=new Intent(mContext, GroupChatDetailNewActivity.class);

                    intent.putExtra("groupName", list.get(position).getGroupName());
                    intent.putExtra("peopleNum", "");
                    intent.putExtra("groupNote",list.get(position).getGroupNote());
                    intent.putExtra("groupNum", list.get(position).getGroupId());
                    intent.putExtra("picUrl", list.get(position).getImage().toString());
                    intent.putExtra("hostId","");

                    mContext.startActivity(intent);

                }
            });


        return view;

    }

    final static class ViewHolder {
        SelectableRoundedImageView img_project_follow;
        TextView groupName;
        Button bt_follow;
    }
}
