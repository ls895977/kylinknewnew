package com.maoyongxin.myapplication.ui.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.FollowListInfo;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.request.ReqAddFollow;
import com.maoyongxin.myapplication.http.response.BaseResp;
import com.maoyongxin.myapplication.http.response.FollowResponse;
import com.maoyongxin.myapplication.ui.StrangerDetailActivity;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by maoyongxin on 2017/12/7.
 */

public class FollowListAdapter extends BaseAdapter {
    private List<FollowListInfo.FollowList> followLists;
    private Context context;
    private LayoutInflater inflater;
    private OnRefreshListener onRefreshListener;

    public FollowListAdapter(List<FollowListInfo.FollowList> followLists, Context context) {
        this.followLists = followLists;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return followLists.size();
    }

    @Override
    public Object getItem(int position) {
        return followLists.get(position);
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
            convertView = inflater.inflate(R.layout.item_follow_list, null);
            holder.tv_follow_time = (TextView) convertView.findViewById(R.id.tv_follow_time);
            holder.img_follow_edit = (ImageView) convertView.findViewById(R.id.img_follow_edit);
            holder.img_follow_cancle = (ImageView) convertView.findViewById(R.id.img_follow_cancle);
            holder.tv_follow_name = (TextView) convertView.findViewById(R.id.tv_follow_name);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_follow_time.setText("关注时间：" + getStrTime(followLists.get(position).getFollowDate()));

        if (followLists.get(position).getNote().equals("") || followLists.get(position).getNote() == null) {
            holder.tv_follow_name.setText(followLists.get(position).getUserId());
        } else {
            holder.tv_follow_name.setText(followLists.get(position).getNote());
        }
        holder.img_follow_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog(followLists.get(position).getId() + "");
            }
        });
        holder.img_follow_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog(followLists.get(position).getId() + "");
            }
        });
        holder.tv_follow_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,StrangerDetailActivity.class);
                intent.putExtra("personId",followLists.get(position).getUserId());
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    private void showEditDialog(final String followId) {
        final EditText inputServer = new EditText(context);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("请输入备注").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                .setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ReqAddFollow.upDateFollow(context, "followAdapter", followId, inputServer.getText().toString(), new EntityCallBack<FollowResponse>() {
                    @Override
                    public Class<FollowResponse> getEntityClass() {
                        return FollowResponse.class;
                    }

                    @Override
                    public void onSuccess(FollowResponse resp) {
                        if (resp.is200()) {
                            onRefreshListener.refresh();
                            Toast.makeText(context, "修改备注成功", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Throwable e) {

                    }

                    @Override
                    public void onCancelled(Throwable e) {

                    }

                    @Override
                    public void onFinished() {

                    }
                });
            }
        }).show();
    }

    private void showDeleteDialog(final String followId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("提示").setMessage("你确定要取消关注么？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ReqAddFollow.deleteFollow(context, "followAdapter", followId, new EntityCallBack<BaseResp>() {
                    @Override
                    public Class<BaseResp> getEntityClass() {
                        return BaseResp.class;
                    }

                    @Override
                    public void onSuccess(BaseResp resp) {
                        if (resp.is200()) {
                            onRefreshListener.refresh();
                            Toast.makeText(context, "取消关注成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "修改备注失败，请稍后重试", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Throwable e) {

                    }

                    @Override
                    public void onCancelled(Throwable e) {

                    }

                    @Override
                    public void onFinished() {

                    }
                });
            }
        }).setNegativeButton("取消", null).show();
    }

    //时间戳转字符串
    public static String getStrTime(String timeStamp){
        String timeString = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        long  l = Long.valueOf(timeStamp);
        timeString = sdf.format(new Date(l));//单位秒
        return timeString;
    }

    class ViewHolder {
        ImageView img_follow_edit, img_follow_cancle;
        TextView tv_follow_name, tv_follow_time;
    }
    public void setOnRefreshListener(OnRefreshListener onRefreshListener){
        this.onRefreshListener=onRefreshListener;
    }
    public interface OnRefreshListener{
        void refresh();
    }
}
