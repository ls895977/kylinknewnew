package com.maoyongxin.myapplication.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jky.baselibrary.widget.TitleBar;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.request.ReqCommunity;
import com.maoyongxin.myapplication.http.response.BaseResp;
import com.maoyongxin.myapplication.http.response.CommunityUsersResponse;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.myapp.AppTitleBarActivity;
import com.maoyongxin.myapplication.server.widget.SelectableRoundedImageView;
import com.maoyongxin.myapplication.ui.adapter.CommunityPersonListAdapter;
import com.maoyongxin.myapplication.ui.editapp.minefragment.CompanyshowDefine;
import com.maoyongxin.myapplication.ui.editapp.minefragment.fwDetail;
import com.maoyongxin.myapplication.ui.editapp.minefragment.xiangqingye;
import com.maoyongxin.myapplication.ui.widget.MorePopWindow;
import com.maoyongxin.myapplication.ui.widget.TeamPopWindow;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CommunityDetailActivity extends AppTitleBarActivity {
    @BindView(R.id.TitleBar_CMD)
    TitleBar TitleBarCMD;
    @BindView(R.id.img_DetailCommunityIcon)
    SelectableRoundedImageView imgDetailCommunityIcon;
    @BindView(R.id.tv_CommunityName)
    TextView tvCommunityName;
    @BindView(R.id.tv_communityCreatTime)
    TextView tvCommunityCreatTime;
    @BindView(R.id.tv_communityAddress)
    TextView tvCommunityAddress;
    @BindView(R.id.tv_communityNote)
    TextView tvCommunityNote;
    @BindView(R.id.lv_communityPersonList)
    ListView lvCommunityPersonList;
    @BindView(R.id.btn_becomeManager)
    Button btnBecomeManager;
    @BindView(R.id.btn_doOutCommunity)
    Button btnDoOutCommunity;
    @BindView(R.id.btn_EditCommunityMsg)
    Button btnEditCommunityMsg;
    @BindView(R.id.btn_EditSHow)
    Button btnEditSHow;
    @BindView(R.id.img_control)
    TextView imgControl;

    private ProgressDialog progressDialog;
    private String myCommunityIcon;
    private String myCommunityNote;
    private String myCommunityId;
    private String myCommunityName;
    private String myCommunityAddress;
    private String myCommunityCreatTime;
    private CommunityPersonListAdapter adapter;
    private boolean isSuperManager = false;
    private boolean isManager = false;
    private String personId;
    private String personHead;
    private String personusrename;
    private String personTel;
    private String personSex;
    private Boolean hasadvertise = false;
    private String shangjiaInfo;

    @Override
    protected int bindLayout() {
        return R.layout.activity_community_detail;
    }

    @Override
    protected void initData() {
        super.initData();
        myCommunityId = getIntent().getStringExtra("myCommunityId");
        myCommunityIcon = getIntent().getStringExtra("myCommunityIcon");
        myCommunityName = getIntent().getStringExtra("myCommunityName");
        myCommunityNote = getIntent().getStringExtra("myCommunityNote");
        myCommunityAddress = getIntent().getStringExtra("myCommunityAddress");
        myCommunityCreatTime = getIntent().getStringExtra("myCommunityCreatTime");
        Hasadvertise();
    }

    private void Hasadvertise() {
        final String XkOne = "http://st.3dgogo.com/index/enterprise_info/get_enterprise_publicity_details_api.html";
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();
                try {

                    RequestBody requestBody = new FormBody.Builder()
                            .add("community_id", myCommunityId)
                            .build();

                    Request request = new Request.Builder()
                            .url(XkOne)
                            .post(requestBody)
                            .build();
                    try {
                        Call call = okHttpClient.newCall(request);
                        //判断请求是否成功
                        Looper.prepare();
                        try {
                            Response response = call.execute();

                            try {
                                shangjiaInfo = response.body().string();
                                JSONObject jsonObject = new JSONObject(shangjiaInfo);

                                if (jsonObject.getInt("code") == 200) {
                                    Toast.makeText(CommunityDetailActivity.this, "去bisonchat.com编辑您的服务号，让客户更好的找到你", Toast.LENGTH_SHORT).show();
                                    hasadvertise = true;
                                } else if (jsonObject.getInt("code") == 500) {
                                    hasadvertise = false;
                                    //Toast.makeText(CommunityDetailActivity.this, "您的微官服务号，还没有设置界面请前往官网设置", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(CommunityDetailActivity.this, "去bisonchat.com编辑您的服务号，让客户更好的找到你", Toast.LENGTH_SHORT).show();

                              //      openMicroweb();



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

    private void openMicroweb()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {


                OkHttpUtils.post().url("http://st.3dgogo.com/index/enterprise_info/create_enterprise_publicity_details_api.html")
                        .addParams("name", myCommunityName)
                        .addParams("community_id", myCommunityId)
                        .addParams("classify_id", "1")
                        .addParams("publicity_img", "")
                        .addParams("last_operator", "")
                        .addParams("intro","")
                        .build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {


                            try {

                                JSONObject jsonObject = new JSONObject(response);

                                if (jsonObject.getInt("code") == 200) {
                                     Toast.makeText(getActivity(),"已经为您开通服务号，请前往官网 www.bisonchat.com编辑",Toast.LENGTH_LONG).show();
                                  //  Intent intent=new Intent(fwDetail.this,xiangqingye.class);
                                   // startActivity(intent);
                                } else if (jsonObject.getInt("code") == 500) {


                                }
                            } catch (Exception e) {
                                e.printStackTrace();


                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });






            }
        }).start();
    }
    @Override
    protected void handlerPassMsg(int target, int target1, Object obj) {

    }

    @Override
    protected void initView() {
        super.initView();
        progressDialog = new ProgressDialog(this, android.R.style.Theme_DeviceDefault_Dialog);
        // progressDialog.setMessage("处理中，请稍候");
        progressDialog.setCancelable(false);
        if (myCommunityIcon.equals("")) {
            Glide.with(CommunityDetailActivity.this).load(R.drawable.jingying).into(imgDetailCommunityIcon);
        } else {
            Glide.with(CommunityDetailActivity.this).load(myCommunityIcon).into(imgDetailCommunityIcon);
        }
        tvCommunityAddress.setText(myCommunityAddress);
        // tvCommunityCreatTime.setText(myCommunityCreatTime);
        tvCommunityCreatTime.setText(myCommunityId);
        tvCommunityName.setText(myCommunityName);
        tvCommunityNote.setText(myCommunityNote);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

    }

    @Override
    protected void doBusiness() {
        super.doBusiness();
        getMyCommunityPerson();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMyCommunityPerson();
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        btnEditSHow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!hasadvertise) {
                   // Intent intent = new Intent(getActivity(), CompanyshowDefine.class);
                    Intent intent = new Intent(getActivity(), fwDetail.class);
                    intent.putExtra("companyName", myCommunityName);

                    startActivity(intent);
                } else if (hasadvertise) {
                    Intent intent = new Intent(getActivity(), xiangqingye.class);
                    intent.putExtra("shangjiaInfo", shangjiaInfo);

                    startActivity(intent);
                }

            }
        });
        TitleBarCMD.setOnTitleBarClickListener(new TitleBar.OnClickListener() {
            @Override
            public boolean onClick(int function) {
                if (function == TitleBar.FUNCTION_RIGHT_TEXT) {
                    Intent intent = new Intent(CommunityDetailActivity.this, CommunityMessageActivity.class);
                    intent.putExtra("communityId", myCommunityId);
                    if (isSuperManager) {
                        intent.putExtra("type", "superManager");
                        startActivity(intent);
                    } else {
                        if (isManager) {
                            intent.putExtra("type", "manager");
                            startActivity(intent);
                        } else {
                            showToastShort("对不起，只有管理员才有权限查看请求信息");
                        }
                    }

                }
                return false;
            }
        });
        btnEditCommunityMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CommunityDetailActivity.this, CommunityEditActivity.class);
                startActivity(intent);
            }
        });
        btnDoOutCommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder buider = new AlertDialog.Builder(CommunityDetailActivity.this);
                buider.setTitle("提示").setMessage("你确定退出当前社区么？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ReqCommunity.deleteCommunity(CommunityDetailActivity.this, getActivityTag(), AppApplication.getCurrentUserInfo().getUserId(), myCommunityId, new EntityCallBack<BaseResp>() {
                            @Override
                            public Class<BaseResp> getEntityClass() {
                                return BaseResp.class;
                            }

                            @Override
                            public void onSuccess(BaseResp resp) {
                                showToastShort(resp.msg);
                                if (resp.is200()) {
                                    finish();
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
        });
        btnBecomeManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText inputServer = new EditText(CommunityDetailActivity.this);
                AlertDialog.Builder builder = new AlertDialog.Builder(CommunityDetailActivity.this);
                builder.setTitle("提示").setMessage("成为管理员需要审核，请输入请求内容").setView(inputServer)
                        .setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ReqCommunity.becomeManager(CommunityDetailActivity.this, getActivityTag(), AppApplication.getCurrentUserInfo().getUserId(), myCommunityId, inputServer.getText().toString(), new EntityCallBack<BaseResp>() {
                            @Override
                            public Class<BaseResp> getEntityClass() {
                                return BaseResp.class;
                            }

                            @Override
                            public void onSuccess(BaseResp resp) {
                                showToastShort(resp.msg);
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


        imgControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TeamPopWindow teamPopWindow = new TeamPopWindow(CommunityDetailActivity.this,myCommunityId,isSuperManager,isManager,hasadvertise,myCommunityName,shangjiaInfo);
                teamPopWindow.showPopupWindow(imgControl);
            }
        });
    }

    private void getMyCommunityPerson() {
        progressDialog.show();
        ReqCommunity.getMyCommunityPersons(this, getActivityTag(), AppApplication.getCurrentUserInfo().getUserId(), myCommunityId, new EntityCallBack<CommunityUsersResponse>() {
            @Override
            public Class<CommunityUsersResponse> getEntityClass() {
                return CommunityUsersResponse.class;
            }

            @Override
            public void onSuccess(final CommunityUsersResponse resp) {
                progressDialog.dismiss();
                if (resp.is200()) {
                    if (resp.obj.getSuperManagerUserId().equals(AppApplication.getCurrentUserInfo().getUserId())) {//超管
                        isSuperManager = true;
                        btnEditCommunityMsg.setVisibility(View.VISIBLE);
                        btnDoOutCommunity.setVisibility(View.GONE);
                        btnBecomeManager.setVisibility(View.GONE);
                    } else {
                        if (resp.obj.getManagerUserId().size() != 0) {
                            for (int i = 0; i < resp.obj.getManagerUserId().size(); i++) {
                                if (resp.obj.getManagerUserId().get(i).equals(AppApplication.getCurrentUserInfo().getUserId())) {
                                    btnEditCommunityMsg.setVisibility(View.GONE);
                                    btnDoOutCommunity.setVisibility(View.VISIBLE);
                                    btnBecomeManager.setVisibility(View.GONE);
                                    isManager = true;
                                    break;
                                } else {
                                    btnEditCommunityMsg.setVisibility(View.GONE);
                                    btnDoOutCommunity.setVisibility(View.VISIBLE);
                                    btnBecomeManager.setVisibility(View.VISIBLE);
                                }
                            }
                        } else {
                            isSuperManager = false;
                            isManager = false;
                            btnDoOutCommunity.setVisibility(View.VISIBLE);
                            btnBecomeManager.setVisibility(View.VISIBLE);
                            btnEditCommunityMsg.setVisibility(View.GONE);
                        }
                    }
                    adapter = new CommunityPersonListAdapter(myCommunityId, resp.obj.getSuperManagerUserId(), resp.obj.getManagerUserId(), resp.obj.getUserList(), CommunityDetailActivity.this, false);
                    lvCommunityPersonList.setAdapter(adapter);
                    adapter.setOnRefreshListener(new CommunityPersonListAdapter.OnRefreshListener() {
                        @Override
                        public void refresh() {
                            getMyCommunityPerson();
                        }
                    });
                    lvCommunityPersonList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(CommunityDetailActivity.this, comunitymateDetailActivity.class);
                            personId = resp.obj.getUserList().get(position).getUserId();
                            personHead = resp.obj.getUserList().get(position).getHeadImg();
                            personusrename = resp.obj.getUserList().get(position).getUserName();
                            personTel = resp.obj.getUserList().get(position).getUserPhone();
                            personSex = resp.obj.getUserList().get(position).getSex() + "";
                            intent.putExtra("personId", personId);
                            intent.putExtra("personHead", personHead);
                            intent.putExtra("personusrename", personusrename);
                            intent.putExtra("personTel", personTel);
                            intent.putExtra("personSex", personSex);
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Throwable e) {
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(Throwable e) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);


    }
}
