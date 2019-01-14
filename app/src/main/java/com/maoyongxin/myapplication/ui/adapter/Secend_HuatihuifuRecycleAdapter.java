package com.maoyongxin.myapplication.ui.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
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
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.server.widget.SelectableRoundedImageView;
import com.maoyongxin.myapplication.ui.Secent_HuifuList;
import com.maoyongxin.myapplication.ui.bean.HuatihuifuRecyclePinlun;
import com.maoyongxin.myapplication.ui.fragment.ShareDialogFragment;
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
import io.github.rockerhieu.emojicon.EmojiconTextView;
import okhttp3.Call;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class Secend_HuatihuifuRecycleAdapter extends RecyclerView.Adapter<Secend_HuatihuifuRecycleAdapter.MyHolder> implements ShareDialogFragment.Listener {
    List<HuatihuifuRecyclePinlun.InfoBean.DataBeanX> list;
    Context context;
    LayoutInflater inflater;
    private Secend_HuatihuifuRecycleAdapter.OnDynamicDeletedListener onDynamicDeletedListener;
    private Handler handler;
    private Secend_HuatihuifuRecycleAdapter.LongClickListener listener;
    private String parentUserId;

    public interface LongClickListener {
        void onItemLongClick(int position);
    }

    public Secend_HuatihuifuRecycleAdapter(String parentUserId1, List<HuatihuifuRecyclePinlun.InfoBean.DataBeanX> list, Context context, Secend_HuatihuifuRecycleAdapter.LongClickListener listener) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        handler = new Handler();
        this.parentUserId = parentUserId1;
        this.listener = listener;
    }

    @Override
    public Secend_HuatihuifuRecycleAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_secend_huati_huifu, parent, false);
        return new Secend_HuatihuifuRecycleAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final Secend_HuatihuifuRecycleAdapter.MyHolder holder, int i) {
        final int position = i;
        holder.ll_response.setVisibility(GONE);
        //  holder.ll_top.setVisibility(GONE);
        try {
            holder.tv_title.setText(new String(Base64.decode(list.get(position).getContent().getBytes(), Base64.DEFAULT)));
        } catch (IllegalArgumentException exception) {
            holder.tv_title.setText(list.get(position).getContent());
        }

        holder.tv_time.setText(list.get(position).getCreate_time());
        holder.tv_uname.setText(list.get(position).getUserName());
//        holder.tv_msgNum.setText(list.get(position).getHuatiPinglun());
        holder.numZan.setText(list.get(position).getPraise_num() + "");
        holder.groupId.setText(list.get(position).getId());
        holder.HolderId.setText(list.get(position).getUid());
//        holder.tv_groupName.setText("#" + list.get(position).getGroupName());
        if (list.get(position).getRow().getTotal() > 0) {
            holder.ll_response.setVisibility(VISIBLE);
//            holder.first_response.setText(list.get(position).getFirstPerson());
            if (list.get(position).getRow().getTotal() == 1) {
//                holder.first_content.setText(new String(Base64.decode(list.get(position).getlaset_response().getBytes(), Base64.DEFAULT)));
                holder.tv_res_count.setVisibility(GONE);
            } else {
//                holder.first_content.setText(new String(Base64.decode(list.get(position).getlaset_response().getBytes(), Base64.DEFAULT)));
                holder.tv_res_count.setVisibility(VISIBLE);
                holder.tv_res_count.setText("等" + list.get(position).getRow().getTotal() + "条评论");
            }
        } else {
            holder.ll_response.setVisibility(GONE);
        }
//        if (!contentImg.equals("")) {
//            holder.content_img.setVisibility(VISIBLE);
//            Glide.with(context).load(list.get(position).getContentImg()).into(holder.content_img);
//        } else {
        holder.content_img.setVisibility(GONE);
//        }
        String headurl = list.get(i).getHeadImg();
        Glide.with(context).load(headurl).into(holder.img_userHead);
//        if (list.get(position).getTopparam().equals("0")) {
        holder.ll_content.setVisibility(VISIBLE);
        holder.ll_top.setVisibility(GONE);
//        } else {
//            holder.ll_content.setVisibility(GONE);
//            holder.ll_top.setVisibility(VISIBLE);
//            holder.tv_toptext.setText(list.get(position).getHuatiName());
        if (list.get(position).getUid().equals(parentUserId))

        {
            holder.tv_holder_icon.setVisibility(VISIBLE);
        } else

        {
            holder.tv_holder_icon.setVisibility(GONE);
        }
        holder.ll_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemLongClick(holder.getAdapterPosition());
            }
        });
        if (list.get(position).getUid().equals(AppApplication.getCurrentUserInfo().getUserId())) {
            holder.img_delete.setVisibility(VISIBLE);

        } else {
            holder.img_delete.setVisibility(GONE);
        }
        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("提示").setMessage("你确定要删除这条评论吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteMyDynamic(list.get(position).getId(), position);
                            }
                        }).setNegativeButton("取消", null).show();
            }
        });
        holder.ll_response.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Secent_HuifuList.class);
                intent.putExtra("holdername", list.get(position).getUserName());
                intent.putExtra("holderHeadimg", list.get(position).getHeadImg());
                intent.putExtra("holdercontent", list.get(position).getContent());
                intent.putExtra("num_zan", list.get(position).getPraise_num() + "");
                intent.putExtra("num_pin", list.get(position).getTread_num() + "");
                intent.putExtra("parent_time", list.get(position).getCreate_time());
                intent.putExtra("content_img", "");
                intent.putExtra("gambit_id", list.get(position).getGambit_id());
                intent.putExtra("GroupId", list.get(position).getGroup_id() + "");
                intent.putExtra("parentUserId", list.get(position).getUid());
                context.startActivity(intent);
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

    public void deleteMyDynamic(final String huatiId, final int position) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.post().url("http://st.3dgogo.com/index/chatgroup_gambit/delete_chatgroup_respond")
                        .addParams("id", huatiId)
                        .build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
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
        TextView tv_title, tv_uname, tv_msgNum, tv_time, groupId, HolderId, tv_groupName, numZan, tv_res_count;
        SelectableRoundedImageView img_userHead;
        String headImgUrl, Groupid;
        ImageView content_img, img_share;
        LinearLayout ll_zan, all_view, ll_share;
        TextView img_delete;
        Bitmap sharebitmap;
        LinearLayout ll_top;
        LinearLayout ll_content;
        TextView tv_toptext, first_response;
        TextView tv_responsetwo;
        String responsedata;
        EmojiconTextView first_content;
        LinearLayout ll_response;
        TextView tv_holder_icon;

        public MyHolder(View view) {
            super(view);
            tv_holder_icon = (TextView) view.findViewById(R.id.tv_holder_icon);
            tv_res_count = (TextView) view.findViewById(R.id.tv_res_count);
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
            img_delete = (TextView) view.findViewById(R.id.img_delete);
            img_share = (ImageView) view.findViewById(R.id.img_share);
            all_view = (LinearLayout) view.findViewById(R.id.all_view);
            ll_share = (LinearLayout) view.findViewById(R.id.ll_share);
            ll_top = (LinearLayout) view.findViewById(R.id.ll_top_text);
            ll_content = (LinearLayout) view.findViewById(R.id.ll_content);
            tv_toptext = (TextView) view.findViewById(R.id.top_text);
            first_content = (EmojiconTextView) view.findViewById(R.id.tv_responsethere);
            first_response = (TextView) view.findViewById(R.id.tv_responseone);
            tv_responsetwo = (TextView) view.findViewById(R.id.tv_responsetwo);
            ll_response = (LinearLayout) view.findViewById(R.id.ll_response);


        }
    }

    public interface OnDynamicDeletedListener {
        void delete();

    }

    @Override
    public void onSharePlatformClicked(int position) {
        Toast.makeText(context, "正在准备分享...", Toast.LENGTH_SHORT).show();

    }

    private void shareWeChat(Secend_HuatihuifuRecycleAdapter.MyHolder holder) {
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


    private void shareWeChatMoments(Secend_HuatihuifuRecycleAdapter.MyHolder holder) {
        JShareInterface.share(WechatMoments.Name, generateParams(holder), null);
    }


    private ShareParams generateParams(Secend_HuatihuifuRecycleAdapter.MyHolder holder) {
        ShareParams shareParams = new ShareParams();

        shareParams.setShareType(Platform.SHARE_IMAGE);
        holder.sharebitmap = convertViewToBitmap(holder);
        shareParams.setImageData(holder.sharebitmap);


        return shareParams;
    }

    private Bitmap convertViewToBitmap(Secend_HuatihuifuRecycleAdapter.MyHolder viewHolder) {

        viewHolder.all_view.setDrawingCacheEnabled(true);
        viewHolder.ll_share.setVisibility(VISIBLE);
        viewHolder.all_view.buildDrawingCache();  //启用DrawingCache并创建位图
        Bitmap bitmap = Bitmap.createBitmap(viewHolder.all_view.getDrawingCache()); //创建一个DrawingCache的拷贝，因为DrawingCache得到的位图在禁用后会被回收
        viewHolder.ll_share.setVisibility(GONE);
        viewHolder.all_view.setDrawingCacheEnabled(false);

        return bitmap;
    }
}
