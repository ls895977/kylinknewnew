package com.maoyongxin.myapplication.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bumptech.glide.Glide;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.PictureEntity;
import com.maoyongxin.myapplication.myapp.AppConfig;
import com.maoyongxin.myapplication.server.widget.SelectableRoundedImageView;

import java.util.List;


/**
 * Created by maoyongxin on 2017/9/12.
 */

public class GridNetPicAdapter extends BaseAdapter {
    private List<PictureEntity> list = null;
    private Context mContext;

    public GridNetPicAdapter(Context mContext, List<PictureEntity> list) {
        this.mContext = mContext;
        this.list = list;
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
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_follow_project_grid_pic, null);
            viewHolder.img_project_follow = (SelectableRoundedImageView) view.findViewById(R.id.img_project_follow);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if(list.get(position).getImgUrl()!=null){
            Glide.with(mContext).load( AppConfig.sRootUrl+"/noticecontroller/getNoticeImg/"+list.get(position).getImgUrl()).into(viewHolder.img_project_follow);
        }else if(list.get(position).getFilePath()!=null){
            Glide.with(mContext).load(list.get(position).getFilePath()).into(viewHolder.img_project_follow);
        }else{
            Glide.with(mContext).load(list.get(position).getImgResource()).into(viewHolder.img_project_follow);
        }
//        Glide.with(mContext).load( "http://qnimg.gzjky.com/"+list.get(position)).into(viewHolder.img_project_follow);
//        viewHolder.img_project_follow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(mContext, ShowPictureDialogActivity.class);
//                intent.putExtra("resource", list.get(position));
//                mContext.startActivity(intent);
//            }
//        });
        return view;

    }

    final static class ViewHolder {
        SelectableRoundedImageView img_project_follow;
    }
}
