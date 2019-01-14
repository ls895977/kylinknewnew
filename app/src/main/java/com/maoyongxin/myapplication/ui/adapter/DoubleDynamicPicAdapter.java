package com.maoyongxin.myapplication.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.PictureEntity;
import com.maoyongxin.myapplication.server.widget.SelectableRoundedImageView;
import com.maoyongxin.myapplication.ui.ShowPictureDialogActivity;
import com.maoyongxin.myapplication.ui.VideoActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by maoyongxin on 2017/9/12.
 */

public class DoubleDynamicPicAdapter extends BaseAdapter {
    private List<PictureEntity> list = null;
    private Context mContext;
    private ArrayList<String> imgurl=null;
    public DoubleDynamicPicAdapter(Context mContext, List<PictureEntity> list, ArrayList<String> imgurl) {
        this.mContext = mContext;
        this.list = list;
        this.imgurl=imgurl;

    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<PictureEntity> list) {
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_follow_project_double_pic, null);
            viewHolder.img_project_follow = (SelectableRoundedImageView) view.findViewById(R.id.img_project_follow);
            viewHolder.icPlay = (ImageView) view.findViewById(R.id.im_play);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if (list.get(position).getImgUrl() != null) {
            Glide.with(mContext).load(list.get(position).getImgUrl())
                    .error(R.drawable.rc_image_error)
                    .into(viewHolder.img_project_follow);


        } else if (list.get(position).getFilePath() != null) {
            Glide.with(mContext).load(list.get(position).getFilePath()).into(viewHolder.img_project_follow);
        } else {
            Glide.with(mContext).load(list.get(position).getImgResource()).into(viewHolder.img_project_follow);
        }




        if (list.get(position).getType() == 1) {
            viewHolder.icPlay.setVisibility(View.GONE);
        } else {
            viewHolder.icPlay.setVisibility(View.VISIBLE);
        }

        viewHolder.img_project_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ShowPictureDialogActivity.class);
                if (list.get(position).getType() == 1) {
                    intent.putExtra("type", 1);
                    intent.putExtra("resource", list.get(position).getImgUrl());
                    intent.putStringArrayListExtra("imagList",imgurl);

                } else {
                    intent.setClass(mContext, VideoActivity.class);
                    intent.putExtra("type", 2);
                    intent.putExtra("resource", list.get(position).getVideoUrl());
                    intent.putExtra("thumb", list.get(position).getImgUrl());
                    intent.putExtra("title", list.get(position).getTitle());
                }
                mContext.startActivity(intent);
            }
        });
        return view;

    }

    final static class ViewHolder {
        SelectableRoundedImageView img_project_follow;
        private ImageView icPlay;

    }
}
