package com.maoyongxin.myapplication.ui.adapter;

import android.content.Context;
import android.os.Looper;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.http.response.DynamicComment;
import com.maoyongxin.myapplication.myapp.AppApplication;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.rockerhieu.emojicon.EmojiconTextView;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by maoyongxin on 2017/9/12.
 */

public class huifuAdapter extends BaseAdapter {

    private String Api_Zan = "http://st.3dgogo.com/index/user_dynamic/set_user_praise_num_api.html";
    final String zan = "1";
    final String cai = "2";
    private Map<Integer, Boolean> zanmap = new HashMap<>();// 存放已被选中的CheckBox
    private Map<Integer, Boolean> Badmap = new HashMap<>();// 存放已被选中的CheckBox
    private List<DynamicComment> dynamicCommentList;
    private Context mContext;
    private List<com.maoyongxin.myapplication.entity.huifuInfo> huifuInfo = null;

    public huifuAdapter(Context mContext, List<com.maoyongxin.myapplication.entity.huifuInfo> huifuInfo) {
        this.mContext = mContext;
        this.huifuInfo = huifuInfo;

    }

//    public huifuAdapter(Context mContext, List<DynamicComment> dynamicCommentList) {
//        this.dynamicCommentList = dynamicCommentList;
//        this.mContext = mContext;
//    }

    public int getCount() {
        return this.huifuInfo.size();
    }

    public Object getItem(int position) {
        return huifuInfo.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {


        final ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_huifu, null);
            viewHolder = new ViewHolder();

            viewHolder.img_jingxuan = (ImageView) view.findViewById(R.id.img_huifuHead);
            viewHolder.name_huifu = (TextView) view.findViewById(R.id.tv_huifu_user);
            viewHolder.tv_huifu_time = (TextView) view.findViewById(R.id.tv_huifu_time);
            viewHolder.huifu_content = (TextView) view.findViewById(R.id.tv_huifu_content);
            viewHolder.huifu_content.setVisibility(View.GONE);
            viewHolder.huatiId = (TextView) view.findViewById(R.id.huatiId);
            viewHolder.huifuZan = (TextView) view.findViewById(R.id.huifuZan);
            viewHolder.huifuCai = (TextView) view.findViewById(R.id.huifuCai);
            viewHolder.ibZan = (ImageButton) view.findViewById(R.id.ib_zan);
            viewHolder.ibBad = (ImageButton) view.findViewById(R.id.ib_bad);
            viewHolder.userId = (TextView) view.findViewById(R.id.userid);
            viewHolder.tv_text = (EmojiconTextView) view.findViewById(R.id.tv_text);
            viewHolder.tv_text.setVisibility(View.VISIBLE);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        view.setTag(viewHolder);

        if (huifuInfo.get(position) != null) {
            Glide.with(mContext).load(huifuInfo.get(position).getPic()).into(viewHolder.img_jingxuan);
            viewHolder.name_huifu.setText(huifuInfo.get(position).getHuifu_user());
            viewHolder.tv_huifu_time.setText(huifuInfo.get(position).getHuifu_time());
            String content = huifuInfo.get(position).getHuaticontent();
            String str2 = new String(Base64.decode(content.getBytes(), Base64.DEFAULT));
            viewHolder.tv_text.setText(str2);
            viewHolder.huatiId.setText(huifuInfo.get(position).getHuatiId());
            viewHolder.huifuZan.setText(huifuInfo.get(position).getHuifuZan() + "");
            viewHolder.huifuCai.setText(huifuInfo.get(position).getHuifuCai() + "");
            viewHolder.userId.setText(huifuInfo.get(position).getHuifuUserId());

            viewHolder.CaiNum = huifuInfo.get(position).getHuifuCai();
            viewHolder.ZanNum = huifuInfo.get(position).getHuifuZan();
            viewHolder.InfoType = huifuInfo.get(position).getInfoType();
        } else {
            Glide.with(mContext).load(R.drawable.login_bg).into(viewHolder.img_jingxuan);
            viewHolder.name_huifu.setText("999");
            viewHolder.tv_huifu_time.setText("999");
            viewHolder.huifu_content.setText("彼信商业");
            viewHolder.huatiId.setText("999");
            viewHolder.huifuCai.setText("999");
            viewHolder.huifuZan.setText("999");


        }

        viewHolder.ibZan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dianzna(zan, Api_Zan, AppApplication.getCurrentUserInfo().getUserId() + "", viewHolder.InfoType, viewHolder);
                zanmap.put(position, true);
            }
        });

        viewHolder.ibBad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dianzna(cai, Api_Zan, AppApplication.getCurrentUserInfo().getUserId() + "", viewHolder.InfoType, viewHolder);
                Badmap.put(position, true);
            }
        });

        if (zanmap != null && zanmap.containsKey(position)) {
            viewHolder.ibZan.setImageResource(R.mipmap.gooded_big);
        } else {
            viewHolder.ibZan.setImageResource(R.mipmap.good_big);
        }

        if (Badmap != null && Badmap.containsKey(position)) {
            viewHolder.ibBad.setImageResource(R.mipmap.bebed_big);
        } else {
            viewHolder.ibBad.setImageResource(R.mipmap.bed_big);
        }


        return view;

    }

    final static class ViewHolder {
        ImageView img_jingxuan;
        TextView name_huifu;
        TextView tv_huifu_time;
        TextView huifu_content;
        TextView huatiId;
        TextView huifuZan;
        TextView huifuCai;
        int CaiNum;
        int ZanNum;
        ImageButton ibZan;
        ImageButton ibBad;
        String InfoType;
        TextView userId;
        EmojiconTextView tv_text;
    }

    private void dianzna(final String type, final String ApiUrl, final String userID, final String zanType, final ViewHolder holder) {
        if (type.equals(zan)) {
            holder.huifuZan.setText(holder.ZanNum + 1 + "");
            holder.ibZan.setImageResource(R.mipmap.gooded_big);
        } else if (type.equals(cai)) {
            holder.huifuCai.setText(holder.CaiNum + 1 + "");
            holder.ibBad.setImageResource(R.mipmap.bebed_big);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();


                try {

                    RequestBody requestBody = new FormBody.Builder()
                            .add("user_id", userID)
                            .add("zdm", zanType)
                            .add(zanType, holder.huatiId.getText().toString())
                            .add("type", type)
                            .build();

                    Request request = new Request.Builder()
                            .url(ApiUrl)
                            .post(requestBody)
                            .build();
                    try {
                        Call call = okHttpClient.newCall(request);
                        //判断请求是否成功
                        Looper.prepare();
                        try {
                            Response response = call.execute();

                            try {

                                JSONObject jsonObject = new JSONObject(response.body().string());

                                if (jsonObject.getInt("code") == 200) {


                                } else if (jsonObject.getInt("code") == 500) {


                                }
                            } catch (Exception e) {
                                e.printStackTrace();


                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Looper.loop();

                    } catch (Exception e) {
                        e.printStackTrace();

                    }


                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        }).start();

    }
}
