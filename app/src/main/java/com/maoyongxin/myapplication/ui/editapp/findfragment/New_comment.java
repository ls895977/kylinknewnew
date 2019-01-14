package com.maoyongxin.myapplication.ui.editapp.findfragment;

import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.new_comment;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.ui.adapter.comment_recycle_groupList;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class New_comment extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<new_comment> huifuInfoList = new ArrayList<>();
    private Handler handler;
    private int updateone=1;
    private comment_recycle_groupList commentAdapter;
    private int     nextpage=1;
    private int  maxpage=1;
    private int  current=0;
    private SwipeRefreshLayout mRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_comment);
        mRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.layout_swipe_refresh);

        recyclerView=(RecyclerView)findViewById(R.id.common_list);

        LinearLayoutManager  mLinearLayoutManager=new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.setItemAnimator(null);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);


        Getrepeat();
        commentAdapter=new comment_recycle_groupList( huifuInfoList,New_comment.this);
        recyclerView.setAdapter(commentAdapter);
        handler=new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what==updateone)
                {
                    commentAdapter=new comment_recycle_groupList( huifuInfoList,New_comment.this);
                    recyclerView.setAdapter(commentAdapter);

                }
            }
        };

        recyclerView.addOnScrollListener(new EndLessOnScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                if(maxpage>=nextpage)
                {
                    Getrepeat();
                }
                else
                {
                    Toast.makeText(New_comment.this,"没有数据了",Toast.LENGTH_SHORT).show();
                }

            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void Getrepeat()
    {

        String  api_adress="http://st.3dgogo.com/index/msg_alert/getMsgAlertListApi.html";
        OkHttpUtils.get().url(api_adress)
                .addParams("uid1", AppApplication.getCurrentUserInfo().getUserId()+"")
                .addParams("page",nextpage+"")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {

                try{
                    JSONObject data=new JSONObject(response);
                    JSONArray commList = data.getJSONObject("info").getJSONArray("data");
                    nextpage=data.getJSONObject("info").getInt("current_page")+1;
                    maxpage=data.getJSONObject("info").getInt("last_page");


                    if(data.getInt("code")==200)
                    {
                        for(int i=0;i<commList.length();i++)
                        {
                            JSONObject dates=commList.getJSONObject(i);
                            new_comment newComment=new new_comment();

                            String commentID=parse_Value(dates,"target_parent_id");
                            String commentUserName=parse_Value(dates,"userName");
                            String commentUserHead=parse_Value(dates,"headImg");
                            String commentTime=parse_Value(dates,"atime");
                            String commentContent=parse_Value(dates,"content");
                            String commenttype=parse_Value(dates,"type");
                            String commentUserId=parse_Value(dates,"uid2");
                            String Communityname=parse_Value(dates,"communityName");
                            String CommunityId=parse_Value(dates,"community_id");
                            String group_id=parse_Value(dates,"group_id");
                            String parentId=parse_Value(dates,"uid1");
                            String isread=parse_Value(dates,"is_read");
                            newComment.sethuifuInfo(commentID,commentUserName,commentUserHead,commentTime,commentContent,commenttype,commentUserId,Communityname,CommunityId,group_id,parentId,isread);
                            huifuInfoList.add(newComment);

                        }
                        Message msg=new Message();
                        msg.what=updateone;
                        handler.sendMessage(msg);

                    }

                    else if(data.getInt("code")==500)
                    {

                    }




                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });
    }
    private String parse_Value(JSONObject data, String value) {
        String com_value = "";
        if (data.has(value)) {
            try {
                com_value = data.getString(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return com_value;
    }
}
