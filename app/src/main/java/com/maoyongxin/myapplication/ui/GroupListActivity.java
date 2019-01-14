package com.maoyongxin.myapplication.ui;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.GroupListInfo;
import com.maoyongxin.myapplication.entity.mygroupList;
import com.maoyongxin.myapplication.entity.shanghuiInfo;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.request.ReqGroup;
import com.maoyongxin.myapplication.http.response.BaseResp;
import com.maoyongxin.myapplication.http.response.GroupListResponse;
import com.maoyongxin.myapplication.http.response.GroupResponse;
import com.maoyongxin.myapplication.myapp.AppActivity;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.server.utils.NToast;
import com.maoyongxin.myapplication.ui.adapter.GroupAdapter;
import com.maoyongxin.myapplication.ui.adapter.recycle_groupList;
import com.maoyongxin.myapplication.ui.editapp.findfragment.AllGroupList;
import com.maoyongxin.myapplication.ui.groupchat.GroupChatDetailNewActivity;
import com.maoyongxin.myapplication.ui.widget.NestedListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import io.rong.imkit.RongIM;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GroupListActivity extends AppActivity {
    @BindView(R.id.group_search)
    EditText groupSearch;
    @BindView(R.id.show_no_group)
    TextView showNoGroup;
    @BindView(R.id.group_listview)
    ListView groupListview;
    @BindView(R.id.foot_group_size)
    TextView footGroupSize;
    @BindView(R.id.tv_searchGroupName)
    TextView tvSearchGroupName;
    @BindView(R.id.tv_searchGroupNote)
    TextView tvSearchGroupNote;
    @BindView(R.id.btn_askJoinGroup)
    Button btnAskJoinGroup;
    @BindView(R.id.line_searchResult)
    LinearLayout lineSearchResult;
    @BindView(R.id.creatGroup)
    TextView creatGroup;
    private CardView sh_card1,sh_card2,sh_card3;
    private ImageView sh_img1,sh_img2,sh_img3;
    private TextView sh_name1,sh_name2,sh_name3;
    private TextView sh_num1,sh_num2,sh_num3,tv_showMore;
    private List<shanghuiInfo> shanghuiList=new ArrayList<>();
    private String groupName,groupId,groupImg,membernum,integralNum,holderId,groupNote;
    private String  Headurl;
    private GroupAdapter adapter;
    private List<mygroupList> mList=new ArrayList<>();
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    private String Hostid;
    private String GroupId;
    private Handler handler;
    private int UPTADE=1;
    private int UPDATEONE=2;
    private String ApiPop = "http://st.3dgogo.com/index/chatgroup_gambit/get_chat_group_hottest?page=";
    private int pagepopNext = 1;

    @Override
    protected int bindLayout() {
        return R.layout.activity_group_list;
    }

    @Override
    protected void doBusiness() {
        super.doBusiness();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(receiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getgrouplist();

        IntentFilter filter = new IntentFilter();
        filter.addAction("finish");
        this.registerReceiver(receiver, filter);
    }

    @Override
    protected void initData() {
        super.initData();

    }

    @Override
    protected void initView() {

        lineSearchResult.setVisibility(View.GONE);

        View view=View.inflate(getApplicationContext(),R.layout.hangye_mine,null);
        tv_showMore=(TextView)view.findViewById(R.id.tv_showMore);
        tv_showMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), AllGroupList.class);
                startActivity(intent);
            }
        });
        sh_card1=(CardView)view.findViewById(R.id.shanghui_card1);
        sh_card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), GroupChatDetailNewActivity.class);
                intent.putExtra("groupName", sh_name1.getText().toString());
                intent.putExtra("peopleNum", sh_num1.getText().toString());
                intent.putExtra("groupNum",shanghuiList.get(0).getGroupNum().toString());
                intent.putExtra("picUrl", shanghuiList.get(0).getPic());
                intent.putExtra("hostId",shanghuiList.get(0).getHolderId());

                Pair<View, String> shanghuiHead = new Pair<View, String>(sh_img1,"qtx");
                Pair<View,String> shanghuiName = new Pair<View, String>(sh_name1,"shanghuiName");
                Pair<View,String> shanghuiNum = new Pair<View, String>(sh_num1,"shanghuiNum");
                ActivityOptionsCompat optionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), shanghuiHead,shanghuiName,shanghuiNum);
                startActivity(intent,optionsCompat.toBundle());



            }
        });
        sh_card2=(CardView)view.findViewById(R.id.shanghui_card2);
        sh_card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent intent = new Intent(getActivity(), GroupChatHuatiDetial.class);
                Intent intent = new Intent(getActivity(), GroupChatDetailNewActivity.class);
                intent.putExtra("groupName", sh_name2.getText().toString());
                intent.putExtra("peopleNum", sh_num2.getText().toString());
                intent.putExtra("groupNum",shanghuiList.get(1).getGroupNum().toString());
                intent.putExtra("picUrl", shanghuiList.get(1).getPic());
                intent.putExtra("hostId",shanghuiList.get(1).getHolderId());

                Pair<View, String> shanghuiHead = new Pair<View, String>(sh_img2,"qtx");
                Pair<View,String> shanghuiName = new Pair<View, String>(sh_name2,"shanghuiName");
                Pair<View,String> shanghuiNum = new Pair<View, String>(sh_num2,"shanghuiNum");
                ActivityOptionsCompat optionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), shanghuiHead,shanghuiName,shanghuiNum);
                startActivity(intent,optionsCompat.toBundle());


            }
        });

        sh_card3=(CardView)view.findViewById(R.id.shanghui_card3);
        sh_card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Intent intent = new Intent(getActivity(), GroupChatDetailActivity.class);

                Intent intent = new Intent(getActivity(), GroupChatDetailNewActivity.class);


                intent.putExtra("groupName", sh_name3.getText().toString());
                intent.putExtra("peopleNum", sh_num3.getText().toString());
                intent.putExtra("groupNum",shanghuiList.get(2).getGroupNum().toString());
                intent.putExtra("picUrl", shanghuiList.get(2).getPic());
                intent.putExtra("hostId",shanghuiList.get(2).getHolderId());

                Pair<View, String> shanghuiHead = new Pair<View, String>(sh_img3,"qtx");
                Pair<View,String> shanghuiName = new Pair<View, String>(sh_name3,"shanghuiName");
                Pair<View,String> shanghuiNum = new Pair<View, String>(sh_num3,"shanghuiNum");
                ActivityOptionsCompat optionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), shanghuiHead,shanghuiName,shanghuiNum);


                startActivity(intent,optionsCompat.toBundle());


            }
        });

        sh_img1=(ImageView) view.findViewById(R.id.shanghui_img1);
        sh_img2=(ImageView) view.findViewById(R.id.shanghui_img2);
        sh_img3=(ImageView) view.findViewById(R.id.shanghui_img3);

        sh_name1=(TextView) view.findViewById(R.id.shanghui_name1);
        sh_name2=(TextView) view.findViewById(R.id.shanghui_name2);
        sh_name3=(TextView) view.findViewById(R.id.shanghui_name3);

        sh_num1=(TextView) view.findViewById(R.id.shanghui_num1);
        sh_num2=(TextView) view.findViewById(R.id.shanghui_num2);
        sh_num3=(TextView) view.findViewById(R.id.shanghui_num3);
        groupListview.addHeaderView(view);
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        groupSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                lineSearchResult.setVisibility(View.GONE);
                getGroupInfo(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        creatGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CreateGroupSteopOne.class));
            }
        });
    }


    private void getGroupInfo(String datainfos) {
        ReqGroup.getGroupInfo(this, getActivityTag(), datainfos, new EntityCallBack<GroupResponse>() {
            @Override
            public Class<GroupResponse> getEntityClass() {
                return GroupResponse.class;
            }

            @Override
            public void onSuccess(final GroupResponse resp) {
                if (resp.is200()) {
                    lineSearchResult.setVisibility(View.VISIBLE);
                    tvSearchGroupName.setText("群名：" + resp.obj.getGroupName());
                    tvSearchGroupNote.setText("群备注：" + resp.obj.getGroupNote());
                    GroupId=resp.obj.getGroupId()+"";
                    Hostid=resp.obj.getGroupHostId()+"";

                    btnAskJoinGroup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final EditText inputServer = new EditText(GroupListActivity.this);
                            AlertDialog.Builder builder = new AlertDialog.Builder(GroupListActivity.this);
                            builder.setTitle("请输入请求信息").setView(inputServer)
                                    .setNegativeButton("取消", null);
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {



                                    ReqGroup.joinGroup(GroupListActivity.this, getActivityTag(), AppApplication.getCurrentUserInfo().getUserId(), resp.obj.getGroupId() + "", inputServer.getText().toString(), new EntityCallBack<BaseResp>() {
                                        @Override
                                        public Class<BaseResp> getEntityClass() {
                                            return BaseResp.class;
                                        }

                                        @Override
                                        public void onSuccess(BaseResp resp) {
                                            showToastShort(resp.msg);
                                            String userId=AppApplication.getCurrentUserInfo().getUserId()+"";
                                            String HostID=Hostid;
                                            AppApplication.getCurrentUserInfo().setUserId(HostID);
                                            dealMessage(Hostid ,"3",userId+"",GroupId+"");
                                            AppApplication.getCurrentUserInfo().setUserId(userId);
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
                            });
                            builder.show();
                        }
                    });
                } else {
                    lineSearchResult.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Throwable e) {
                lineSearchResult.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(Throwable e) {
                lineSearchResult.setVisibility(View.GONE);
            }

            @Override
            public void onFinished() {

            }
        });
    }
    private void getgrouplist()//标准post线程
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();
                try {

                    RequestBody requestBody = new FormBody.Builder()
                            .add("uid", AppApplication.getCurrentUserInfo().getUserId())
                            .build();
                    Request request = new Request.Builder()
                            .url("http://st.3dgogo.com/index/chatgroup/get_user_concerned_chatgroup_api.html")

                            .post(requestBody)
                            .build();
                    try {
                        Call call = okHttpClient.newCall(request);
                        //判断请求是否成功

                        try {
                            Response response = call.execute();

                            try {

                                JSONObject jsonObject = new JSONObject(response.body().string());
                                if (jsonObject.getInt("code") == 200) {
                                    JSONArray data=jsonObject.getJSONArray("info");
                                    mList.clear();
                                    for(int i=0;i<data.length();i++)
                                    {

                                        JSONObject info=data.getJSONObject(i);
                                        mygroupList myroupInfo=new mygroupList();

                                        myroupInfo.setGroupId(info.getString("groupId"));
                                        myroupInfo.setGroupName(info.getString("groupName"));
                                        myroupInfo.setImage(info.getString("image"));

                                        mList.add(myroupInfo);


                                    }

                                } else if (jsonObject.getInt("code") == 500) {


                                }
                                Message message=new Message();
                                message.what=UPTADE;
                                handler.sendMessage(message);
                            } catch (Exception e) {
                                e.printStackTrace();


                            }
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();

                    }


                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        }).start();
        getPoplis(ApiPop,pagepopNext);
    }

    private void dealMessage(String HostId,String state, String joinuserId,String groupId) {
        ReqGroup.dealGroupMessages(getActivity(), "adapter", HostId, groupId, joinuserId, state, new EntityCallBack<BaseResp>() {
            @Override
            public Class<BaseResp> getEntityClass() {
                return BaseResp.class;
            }

            @Override
            public void onSuccess(BaseResp resp) {
                if (resp.is200()) {
                    Toast.makeText(getActivity(),"加入群成功",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable e) {
                Toast.makeText(getActivity(),"加入失败",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(Throwable e) {
                Toast.makeText(getActivity(),"取消加入",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinished() {

            }
        });

    }
    private void getPoplis(String api_url, int page) {
        final String api = api_url + page + "";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(api).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();

                    try {

                        JSONObject jsonObject = new JSONObject(responseData);

                        JSONArray data = jsonObject.getJSONObject("info").getJSONArray("data");
                        shanghuiList.clear();
                        for (int i = 0; i < 3; i++) {

                            shanghuiInfo newshanghuiInfo = new shanghuiInfo();
                            JSONObject newList = data.getJSONObject(i);
                            groupName = parse_Value(newList, "groupName");
                            groupId = parse_Value(newList, "groupId");
                            groupImg = parse_Value(newList, "image");
                            membernum = parse_Value(newList, "memberNum");
                            integralNum = parse_Value(newList, "integralNum");
                            holderId=parse_Value(newList,"groupHostId");
                            groupNote=parse_Value(newList,"groupNote");
                            newshanghuiInfo.setshanghuiInfo(groupImg, groupName, groupId, membernum, integralNum, groupName,holderId,groupNote);

                            shanghuiList.add(newshanghuiInfo);

                          //  pagepopNext = jsonObject.getJSONObject("info").getInt("next_page");
                        }

                        Message msg = new Message();
                        msg.what = UPDATEONE;
                        handler.sendMessage(msg);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    //  showMessagefail("解析失败");
                }
            }
        }).start();
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

    private void updateHeadview(){

        sh_name1.setText(shanghuiList.get(0).getGroupName().toString());
        sh_name2.setText(shanghuiList.get(1).getGroupName().toString());
        sh_name3.setText(shanghuiList.get(2).getGroupName().toString());

        sh_num1.setText(shanghuiList.get(0).getMemNum().toString()+"k会员");
        sh_num2.setText(shanghuiList.get(1).getMemNum().toString()+"k会员");
        sh_num3.setText(shanghuiList.get(2).getMemNum().toString()+"k会员");

        Glide.with(getActivity()).load(shanghuiList.get(0).getPic()).into(sh_img1);
        Glide.with(getActivity()).load(shanghuiList.get(1).getPic()).into(sh_img2);
        Glide.with(getActivity()).load(shanghuiList.get(2).getPic()).into(sh_img3);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//白底黑字+沉浸式导航栏
        handler=new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what==UPTADE)
                {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            adapter=new GroupAdapter(mList,getActivity());
                            groupListview.setAdapter(adapter);

                            groupListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    mygroupList bean=(mygroupList)adapter.getItem(i-1);
                                    RongIM.getInstance().startGroupChat(GroupListActivity.this, bean.getGroupId() + "", bean.getGroupName());
                                }
                            });
                        }
                    },10);

                }
                else if (msg.what==UPDATEONE)
                {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            updateHeadview();
                        }
                    },10);

                }
            }
        };

    }




}
