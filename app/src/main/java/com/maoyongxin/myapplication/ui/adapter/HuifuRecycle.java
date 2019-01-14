package com.maoyongxin.myapplication.ui.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jky.baselibrary.util.common.TimeUtil;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.huifuInfo;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.ui.StrangerDetailActivity;
import com.maoyongxin.myapplication.ui.groupchat.Comment_Detail;
import com.maoyongxin.myapplication.ui.groupchat.Dynamic_huifu;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.rockerhieu.emojicon.EmojiconTextView;
import okhttp3.Call;

/**
 * Created by Yusr on 2018-05-08.
 */
public class HuifuRecycle extends RecyclerView.Adapter<HuifuRecycle.MyHolder> {
    List<huifuInfo> list;
    Context context;
    LayoutInflater inflater;

    private String Api_Zan = "http://st.3dgogo.com/index/user_dynamic/set_user_praise_num_api.html";
    final String zan = "1";
    final String cai = "2";

    private String dynamicId,CommunityId;

    public HuifuRecycle(List<huifuInfo> list, Context context,String dynamicId,String CommunityId) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.dynamicId=dynamicId;
        this.CommunityId=CommunityId;

    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_huifu, parent, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyHolder myholder, int i) {
       final int position = i;

        MyHolder holder=myholder;

        Glide.with(context).load(list.get(position).getPic()).into(holder.img_jingxuan);
        holder.name_huifu.setText(list.get(position).getHuifu_user());
         holder.tv_huifu_time.setText(list.get(position).getHuifu_time());
        String str2 = new String(Base64.decode(list.get(position).getHuaticontent().getBytes(), Base64.DEFAULT));
        holder.tv_text.setText(str2);
        holder.huatiId.setText(list.get(position).getHuatiId());
        holder.huifuZan.setText(list.get(position).getHuifuZan() + "");
        holder.huifuCai.setText(list.get(position).getHuifuCai() + "");
        if(list.get(position).getParentname()!=null&&!list.get(position).getParentname().equals(""))
        {
            holder.tv_huifu_parent.setText("@"+list.get(position).getParentname());
        }
        else
        {
            holder.tv_huifu_parent.setText("");
        }

        if(list.get(position).getHuifuUserId().equals(AppApplication.getCurrentUserInfo().getUserId()))
        {
            holder.dele_huifu.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.dele_huifu.setVisibility(View.INVISIBLE);
        }

        holder.CaiNum = list.get(position).getHuifuCai();
        holder.ZanNum = list.get(position).getHuifuZan();
        holder.InfoType = list.get(position).getInfoType();


        holder.all_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Dynamic_huifu.class);
                intent.putExtra("DynamicId", dynamicId);
                intent.putExtra("parentId", dynamicId);
                intent.putExtra("parentUserId",list.get(position).getHuifuUserId());
                intent.putExtra("CommunityId",CommunityId);
                intent.putExtra("parentName", "@"+list.get(position).getHuifu_user());

                context.startActivity(intent);
            }
        });
        holder.name_huifu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(context, StrangerDetailActivity.class);
                in.putExtra("personId",list.get(position).getParentusrId());
                context.startActivity(in);
            }
        });
        holder.dele_huifu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("提示").setMessage("你确定要删除这条动态么？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteMyDynamic(list.get(position).getHuatiId(),position);
                            }
                        }).setNegativeButton("取消", null).show();

            }
        });


    }

    private void deleteMyDynamic(final String huatiId,final int position)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.post().url("http://st.3dgogo.com/index/user_dynamic/delete_dynamic_post.html")
                        .addParams("id",huatiId)
                        .build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {

                        Toast.makeText(context,"删除成功", Toast.LENGTH_SHORT).show();

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
    public  String timeStamp2Date(long time, String format) {
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(time));
    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    // 定义内部类继承ViewHolder
    class MyHolder extends RecyclerView.ViewHolder {

        ImageView img_jingxuan;
        TextView name_huifu;
        TextView tv_huifu_time;
        TextView huifu_content;
        TextView huatiId;
        TextView huifuZan;
        TextView huifuCai,dele_huifu;
        int CaiNum;
        int ZanNum;
        ImageButton ibZan;
        ImageButton ibBad;
        String InfoType;
        LinearLayout all_view;
        TextView tv_huifu_parent;
        EmojiconTextView tv_text;
        public MyHolder(View view) {
            super(view);

            img_jingxuan = (ImageView) view.findViewById(R.id.img_huifuHead);
            name_huifu = (TextView) view.findViewById(R.id.tv_huifu_user);
            tv_huifu_time = (TextView) view.findViewById(R.id.tv_huifu_time);
            huifu_content = (TextView) view.findViewById(R.id.tv_huifu_content);
            huifu_content.setVisibility(View.GONE);
            huatiId = (TextView) view.findViewById(R.id.huatiId);
            huifuZan = (TextView) view.findViewById(R.id.huifuZan);
            huifuCai = (TextView) view.findViewById(R.id.huifuCai);
            ibZan = (ImageButton) view.findViewById(R.id.ib_zan);
            ibBad = (ImageButton) view.findViewById(R.id.ib_bad);
            all_view=(LinearLayout)view.findViewById(R.id.all_view);
            tv_huifu_parent=(TextView)view.findViewById(R.id.tv_huifu_parent);
            dele_huifu=(TextView)view.findViewById(R.id.dele_huifu);
            tv_text = (EmojiconTextView) view.findViewById(R.id.tv_text);
            tv_text.setVisibility(View.VISIBLE);
        }

    }


}
