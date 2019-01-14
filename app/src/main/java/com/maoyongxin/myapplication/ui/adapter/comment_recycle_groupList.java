package com.maoyongxin.myapplication.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.GroupListInfo;
import com.maoyongxin.myapplication.entity.new_comment;
import com.maoyongxin.myapplication.server.widget.SelectableRoundedImageView;
import com.maoyongxin.myapplication.ui.groupchat.Comment_Detail;
import com.maoyongxin.myapplication.ui.groupchat.huatiDetail;
import com.maoyongxin.myapplication.ui.groupchat.huatiDetailByid;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import io.github.rockerhieu.emojicon.EmojiconTextView;
import okhttp3.Call;

/**
 * Created by Yusr on 2018-05-08.
 */
public class comment_recycle_groupList extends RecyclerView.Adapter<comment_recycle_groupList.MyHolder> {

    Context context;
    LayoutInflater inflater;
    private List<new_comment> list = null;
    private final int TYPE_ITEM = 1;
    // 脚布局
    private final int TYPE_FOOTER = 2;

    private int loadState = 2;
    // 正在加载
    public final int LOADING = 1;
    // 加载完成
    public final int LOADING_COMPLETE = 2;
    // 加载到底
    public final int LOADING_END = 3;


    public comment_recycle_groupList(List<new_comment> list, Context context) {

        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {



            View view = inflater.inflate(R.layout.item_huifu, parent, false);

            return new MyHolder(view);


    }
    @Override
    public int getItemViewType(int position) {

        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }


    @Override
    public void onBindViewHolder(final MyHolder viewHolder, final int i) {
          final int position = i;

        if(list.get(position).getIs_read().equals("0"))
        {
            viewHolder.huifu_content.setTextColor(context.getResources().getColor(R.color.text_normal));
        }
        if(list.get(position).getIs_read().equals("1"))
        {
            viewHolder.huifu_content.setTextColor(context.getResources().getColor(R.color.text_deco));
        }

        Glide.with(context).load(list.get(position).getCommentUserHead()).into(viewHolder.img_jingxuan);
        viewHolder.name_huifu.setText(list.get(position).getCommentUserName());
        viewHolder.tv_huifu_time.setText(list.get(position).getConmmentTime());

        viewHolder.huifu_content.setText(new String (Base64.decode(list.get(position).getCommentContent().getBytes(), Base64.DEFAULT)));

        viewHolder.huatiId.setText(list.get(position).getCommentId());
        viewHolder.userId.setText(list.get(position).getCommentUserId()) ;
        viewHolder.InfoType.setText(list.get(position).getCommentType());

        viewHolder.all_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (list.get(position).getCommentType())
                {
                    case "1":
                        Intent intent=new Intent(context,Comment_Detail.class);
                        intent.putExtra("dynamicId",list.get(position).getCommentId());
                        intent.putExtra("communityName",list.get(position).getCommunityName());
                        intent.putExtra("community_id",list.get(position).getCommunityId());
                        intent.putExtra("parentUserId",list.get(position).getParentuserId());
                        recode_read(list.get(position).getCommentId(),"1");
                        context.startActivity(intent);
                        break;

                    case "2":
                        Intent intent2=new Intent(context,huatiDetailByid.class);

                        intent2.putExtra("gambit_id",list.get(position).getCommentId());//话题id
                        intent2.putExtra("groupId",list.get(position).getGroup_id());
                        intent2.putExtra("parentUserId",list.get(position).getParentuserId());

                        recode_read(list.get(position).getCommentId(),"2");
                        context.startActivity(intent2);
                        break;


                }

            }
        });



    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    private void  recode_read( final String targetId,final String type)
        {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    OkHttpUtils.post().url("http://st.3dgogo.com/index/msg_alert/setMsgAlertIsReadApi.html")
                            .addParams("target_parent_id",targetId)
                            .addParams("type",type)
                            .build().execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {

                        }
                    });


                }
            }).start();


        }

    // 定义内部类继承ViewHolder
    class MyHolder extends RecyclerView.ViewHolder {
        SelectableRoundedImageView img_jingxuan;
        TextView name_huifu;
        TextView tv_huifu_time;
        EmojiconTextView huifu_content;
        TextView huatiId;
        TextView InfoType;
        TextView userId;
        LinearLayout all_view;


        public MyHolder(View view) {
            super(view);
             img_jingxuan = (SelectableRoundedImageView) view.findViewById(R.id.img_huifuHead);
             name_huifu = (TextView) view.findViewById(R.id.tv_huifu_user);
             tv_huifu_time = (TextView) view.findViewById(R.id.tv_huifu_time);
             huifu_content = (EmojiconTextView) view.findViewById(R.id.tv_huifu_content);
             huatiId = (TextView) view.findViewById(R.id.huatiId);
             userId=(TextView)view.findViewById(R.id.userid);
             InfoType=(TextView)view.findViewById(R.id.InfoType);
             all_view=(LinearLayout)view.findViewById(R.id.all_view);
        }

    }







}
