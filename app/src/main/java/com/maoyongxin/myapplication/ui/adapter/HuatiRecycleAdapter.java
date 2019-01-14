package com.maoyongxin.myapplication.ui.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.HuatiInfo;
import com.maoyongxin.myapplication.entity.HuatiInfo1;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.server.widget.SelectableRoundedImageView;
import com.maoyongxin.myapplication.ui.fragment.ShareDialogFragment;
import com.maoyongxin.myapplication.ui.groupchat.huatiDetail_new;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.List;

import cn.jiguang.share.android.api.JShareInterface;
import cn.jiguang.share.android.api.PlatActionListener;
import cn.jiguang.share.android.api.Platform;
import cn.jiguang.share.android.api.ShareParams;
import cn.jiguang.share.wechat.Wechat;
import cn.jiguang.share.wechat.WechatMoments;
import okhttp3.Call;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class HuatiRecycleAdapter extends RecyclerView.Adapter<HuatiRecycleAdapter.MyHolder> implements ShareDialogFragment.Listener {
    List<HuatiInfo1.InfoBean.DataBean> list;
    Context context;
    LayoutInflater inflater;
    String groupName, Groupid,groupimg;
    public HuatiRecycleAdapter(List<HuatiInfo1.InfoBean.DataBean> list, Context context, String groupName1, String Groupid1,String groupimg1) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        groupName = groupName1;
        Groupid = Groupid1;
        groupimg=groupimg1;
    }

    @Override
    public HuatiRecycleAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_group_huati, parent, false);
        return new HuatiRecycleAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final HuatiRecycleAdapter.MyHolder myholder, final int position) {
        final HuatiRecycleAdapter.MyHolder holder = myholder;
        final HuatiInfo1.InfoBean.DataBean itme = list.get(position);
        holder.ll_top.setVisibility(GONE);
        holder.tv_title.setText(itme.getTitle());
        holder.tv_time.setText(itme.getCreate_time());
        holder.tv_uname.setText(itme.getUserName());

        holder.tv_msgNum.setText(itme.getPost_num());
        holder.numZan.setText(itme.getPraise_num());

        holder.groupId.setText(itme.getId());
        holder.HolderId.setText(list.get(position).getUid());
        holder.tv_groupName.setText("#" + groupName);
        final String contentImg = itme.getImg();
        if (!contentImg.equals("")) {
            holder.content_img.setVisibility(VISIBLE);
            Glide.with(context).load(itme.getImg()).into(holder.content_img);
        } else {
            holder.content_img.setVisibility(GONE);
        }
        Glide.with(context).load(itme.getHeadImg()).into(holder.img_userHead);
        if (itme.getIs_top().equals("0")) {
            holder.ll_content.setVisibility(VISIBLE);
            holder.ll_top.setVisibility(GONE);
        } else {
            holder.ll_content.setVisibility(GONE);
            holder.ll_top.setVisibility(VISIBLE);
            holder.tv_toptext.setText(itme.getTitle());
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, huatiDetail_new.class);
                intent.putExtra("huatiTitle", itme.getTitle());
                intent.putExtra("huatiContent", itme.getContent());
                intent.putExtra("postNum", itme.getPost_num()+"");
                intent.putExtra("zanNum", itme.getPraise_num()+"");
                intent.putExtra("gambit_id", itme.getId());
                intent.putExtra("groupNum", Groupid);
                intent.putExtra("GroupId", Groupid);
                intent.putExtra("parentUserId", itme.getUid());
                intent.putExtra("img_userHead",itme.getHeadImg());
                intent.putExtra("holdername", itme.getUserName());
                intent.putExtra("contentImg", contentImg);
                intent.putExtra("gppic",groupimg);
                context.startActivity(intent);
            }
        });
        if (itme.getUid().equals(AppApplication.getCurrentUserInfo().getUserId())) {
            holder.img_delete.setVisibility(VISIBLE);
        } else {
            holder.img_delete.setVisibility(GONE);
        }
        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("提示").setMessage("你确定要删除这条动态么？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteMyDynamic(itme.getId(), position);
                            }
                        }).setNegativeButton("取消", null).show();
            }
        });
        holder.img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("分享").setIcon(R.mipmap.wechat_moment)
                        .setPositiveButton("朋友圈", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(context, "正在准备分享...", Toast.LENGTH_SHORT).show();
                                shareWeChatMoments(holder);
                            }
                        }).setNegativeButton("微信好友", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Toast.makeText(context, "正在准备分享...", Toast.LENGTH_SHORT).show();
                        shareWeChat(holder);
                    }
                }).show();
            }
        });
    }


    private void deleteMyDynamic(final String huatiId, final int position) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.post().url("http://st.3dgogo.com/index/chatgroup_gambit/delete_chatgroup_gambit")
                        .addParams("id", huatiId)
                        .build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {

                        Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();

                        removeData(position);
                    }
                });

            }
        }).start();

    }

    public void removeData(int position) {
        list.remove(position);
        //删除动画
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    // 定义内部类继承ViewHolder
    class MyHolder extends RecyclerView.ViewHolder {
        TextView tv_title, tv_uname, tv_msgNum, tv_time, groupId, HolderId, tv_groupName, numZan;
        SelectableRoundedImageView img_userHead;
        ImageView content_img, img_share;
        LinearLayout ll_zan, all_view, ll_share;
        ImageView img_delete;
        Bitmap sharebitmap;
        LinearLayout ll_top;
        LinearLayout ll_content;
        TextView tv_toptext;

        public MyHolder(View view) {
            super(view);
            tv_title = (TextView) view.findViewById(R.id.tv_huati_content);
            tv_uname = (TextView) view.findViewById(R.id.tv_huati_user);
            tv_msgNum = (TextView) view.findViewById(R.id.tv_pinglun_num);
            tv_time = (TextView) view.findViewById(R.id.tv_huati_time);
            img_userHead = (SelectableRoundedImageView) view.findViewById(R.id.img_huatiuser_head);
            groupId = (TextView) view.findViewById(R.id.huatiId);
            HolderId = (TextView) view.findViewById(R.id.holderID);
            content_img = (ImageView) view.findViewById(R.id.content_img);
            tv_groupName = (TextView) view.findViewById(R.id.tv_groupName);
            numZan = (TextView) view.findViewById(R.id.numZan);
            ll_zan = (LinearLayout) view.findViewById(R.id.ll_zan);
            img_delete = (ImageView) view.findViewById(R.id.img_delete);
            img_share = (ImageView) view.findViewById(R.id.img_share);
            all_view = (LinearLayout) view.findViewById(R.id.all_view);
            ll_share = (LinearLayout) view.findViewById(R.id.ll_share);
            ll_top = (LinearLayout) view.findViewById(R.id.ll_top_text);
            ll_content = (LinearLayout) view.findViewById(R.id.ll_content);
            tv_toptext = (TextView) view.findViewById(R.id.top_text);

        }
    }
    public interface OnDynamicDeletedListener {
        void delete();
    }
    @Override
    public void onSharePlatformClicked(int position) {
        Toast.makeText(context, "正在准备分享...", Toast.LENGTH_SHORT).show();
    }

    private void shareWeChat(HuatiRecycleAdapter.MyHolder holder) {
        try {
            JShareInterface.share(Wechat.Name, generateParams(holder), new PlatActionListener() {
                @Override
                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                    Log.d("---", "onComplete:" + i);
                }
                @Override
                public void onError(Platform platform, int i, int i1, Throwable throwable) {
                    Log.e("----", "platform:" + i + "____" + platform.getName() + throwable.getMessage());
                }

                @Override
                public void onCancel(Platform platform, int i) {
                    Log.e("---", "onCancel:" + i);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void shareWeChatMoments(HuatiRecycleAdapter.MyHolder holder) {
        JShareInterface.share(WechatMoments.Name, generateParams(holder), null);
    }
    private ShareParams generateParams(HuatiRecycleAdapter.MyHolder holder) {
        ShareParams shareParams = new ShareParams();
        shareParams.setShareType(Platform.SHARE_IMAGE);
        holder.sharebitmap = convertViewToBitmap(holder);
        shareParams.setImageData(holder.sharebitmap);
        return shareParams;
    }
    private Bitmap convertViewToBitmap(HuatiRecycleAdapter.MyHolder viewHolder) {
        viewHolder.all_view.setDrawingCacheEnabled(true);
        viewHolder.ll_share.setVisibility(VISIBLE);
        viewHolder.all_view.buildDrawingCache();  //启用DrawingCache并创建位图
        Bitmap bitmap = Bitmap.createBitmap(viewHolder.all_view.getDrawingCache()); //创建一个DrawingCache的拷贝，因为DrawingCache得到的位图在禁用后会被回收
        viewHolder.ll_share.setVisibility(GONE);
        viewHolder.all_view.setDrawingCacheEnabled(false);
        return bitmap;
    }
}
