package com.maoyongxin.myapplication.tool;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.myapp.AppActivity;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.ui.News_web;
import com.maoyongxin.myapplication.ui.comunitymateDetailActivity;
import com.maoyongxin.myapplication.ui.micro_web;
import com.maoyongxin.myapplication.ui.mingpian;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.maoyongxin.myapplication.http.second.okhttp.OkHttpUtils.JSON;
import static com.maoyongxin.myapplication.tool.NaviActivity.isPhoneNumber;

public class VipNaviActivity extends AppActivity {


    @BindView(R.id.tv_poiName)
    TextView tvPoiName;
    @BindView(R.id.tv_poiType)
    TextView tvPoiType;
    @BindView(R.id.tv_poiDistance)
    TextView tvPoiDistance;
    @BindView(R.id.tv_poiAddress)
    TextView tvPoiAddress;
    @BindView(R.id.line)
    LinearLayout line;
    @BindView(R.id.tv_detail)
    TextView tvDetail;
    @BindView(R.id.line_navi)
    LinearLayout lineNavi;
    @BindView(R.id.bossName)
    TextView bossName;
    @BindView(R.id.zhiwei)
    TextView zhiwei;
    @BindView(R.id.companyName)
    TextView companyName;
    @BindView(R.id.companyadress)
    TextView companyadress;
    @BindView(R.id.tel_number)
    TextView telNumber;
    @BindView(R.id.emaill)
    TextView emaill;
    @BindView(R.id.webadress)
    TextView webadress;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.content_ScrollView)
    LinearLayout contentScrollView;
    @BindView(R.id.toshoucangjia)
    TextView toshoucangjia;
    @BindView(R.id.lishijilu)
    TextView lishijilu;
    @BindView(R.id.bt_shoucang)
    Button btShoucang;
    @BindView(R.id.bt_zhuanfa)
    Button btZhuanfa;
    @BindView(R.id.bt_Sendmessage)
    Button btSendmessage;
    @BindView(R.id.bt_newCall)
    Button btNewCall;

    private String communityId;
    private String fuwuhao_name;
    private String intro;
    private String intronote;
    private String contact_num;
    private String webapi="http://st.3dgogo.com/index/enterprise_publicity/get_community_id_details_url_api.html?community_id=";
    private String targetUserId;
    private Boolean parsed=true;

    private  String userId;
    private String collcted="0";
    private String call="0";
    private String msgsend="0";

    private String cpnName;
    private String cpnNum;

    private String myHeadPath;
    private String nickName;
    private String phoneNumber;
    private String personSex;
    private String  personId;
    private String business;
    private String api_slide="http://st.3dgogo.com/index/user/set_get_enterprise_num.html";
    private Boolean  vipstatu;
    private  String yewu;
    private String bossname;
    private  String tel;
    private  String email;
    private  String webAdress;
    private  String creditCode="";
    @Override
    protected int bindLayout() {
        return (R.layout.activity_vip_navi);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        userId= AppApplication.getCurrentUserInfo().getUserId().toString();
        companyName.setText(getIntent().getStringExtra("poiName"));
        companyadress.setText(getIntent().getStringExtra("poiAddress"));
        communityId=getIntent().getStringExtra("targetid");
        getcommunityInfo();




    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(getActivity(),"返回键退出",Toast.LENGTH_SHORT);
        recordaction(userId,companyName.getText().toString(),telNumber.getText().toString(),collcted,msgsend,call);
        finish();

    }
    private void recordaction(final String uid,final String enterprise_name, final String  phone ,final String is_collect,final String msg,final String call)
    {

//get自身服务器数据
        final String XkOne = "http://st.3dgogo.com/index/enterprise_info/create_msg_call_info";


        final String XkThere = ".html";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(XkOne +"/uid/"+uid+"/enterprise_name/"+enterprise_name+"/is_collect/"+is_collect+"/msg/"+msg+"/call/"+call+"/phone/"+phone+ XkThere).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();

                } catch (Exception e) {
                    //showMessagefail("解析失败");
                }
            }
        }).start();
    }
    @Override
    protected void initEvent() {
        //super.initEvent();
        toshoucangjia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), mingpian.class);

                startActivity(intent);
                overridePendingTransition(R.anim.slide_left_in,R.anim.slide_right_out);
            }
        });
        btSendmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    msgsend="1";
                    sendSMS( bossName.getText()+"经理，您好！我是刚才和您通话的那位，很高兴认识您，这是我的电话请惠存", "smsto:" + telNumber.getText());



            }
        });//判断号码格式并发送短信

        webadress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),News_web.class);
                intent.putExtra("weburl",webapi+communityId);
                intent.putExtra("communityId",communityId);
                intent.putExtra("company_name",webadress.getText());
                intent.putExtra("targetUserId",targetUserId);
                startActivity(intent);
            }
        });

        telNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + telNumber.getText()));
                startActivity(intent);
                call="1";
                //点击电话号码直接拨号

            }
        });
        btNewCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    int i= ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE);
                    if(i != PackageManager.PERMISSION_GRANTED)
                    {
                        showDialogTipUserRequestPermission("拨打电话");
                    }
                    else{

                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + telNumber.getText()));
                        call="1";
                        startActivity(intent);
                    }
                }

                // 显示给用户的解释






                //点击电话按钮直接拨号
            }
        });
        btShoucang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(parsed){

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            collcted="1";
                            collecet_new();

                        }
                    }).start();

                }
                else{
                    Toast.makeText(getActivity(), "请解析数据先", Toast.LENGTH_SHORT).show();
                }

            }
        });

        lishijilu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), comunitymateDetailActivity.class);
                nickName = AppApplication.getCurrentUserInfo().getUserName();
                personSex=AppApplication.getCurrentUserInfo().getSex()+"";
                myHeadPath = AppApplication.getCurrentUserInfo().getHeadImg();
                phoneNumber=AppApplication.getCurrentUserInfo().getUserPhone();
                personId=AppApplication.getCurrentUserInfo().getUserId();
                intent.putExtra("personId",personId);
                intent.putExtra("personHead",myHeadPath);
                intent.putExtra("personusrename",nickName);
                intent.putExtra("personTel",phoneNumber);
                intent.putExtra("personSex",personSex);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_in,R.anim.slide_left_out);
            }
        });






    }

    private void getcommunityInfo()
    {
        OkHttpUtils.get().url("http://st.3dgogo.com/index/enterprise_info/get_enterprise_publicity_details_api.html")
                .addParams("community_id",communityId)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {


            }

            @Override
            public void onResponse(String response, int id) {
               try{
                   JSONObject data=new JSONObject(response);
                   intro=data.getJSONObject("info").getString("name");
                   intronote=data.getJSONObject("info").getString("intro");
                   contact_num=data.getJSONObject("info").getString("contact_num");
                   targetUserId=data.getJSONObject("info").getString("last_operator");


                   webadress.setText(getIntent().getStringExtra("poiName"));
                   emaill.setText(intro);
                   telNumber.setText(contact_num);
                   TextView yewuText = new TextView(getActivity());
                   yewuText.setText(intronote);
                   contentScrollView.addView(yewuText);

                   communityId=getIntent().getStringExtra("targetid");
                   parsed=true;


               }catch (Exception e)
               {
                   e.printStackTrace();
               }


            }
        });
    }


    private void showDialogTipUserRequestPermission(String type) {

        new AlertDialog.Builder(this)
                .setTitle(type+"权限不可用")
                .setMessage("通过“彼信”直接"+type+"，能让您更加便捷的联系客户；\n否则，您将无法正常使用“麒麟”，请在后台赋予相应权限")
                .setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      //  startRequestPermission();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setCancelable(false).show();
    }
    public void collecet_new()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.post().url("http://st.3dgogo.com/index/enterprise_info/create_collect_info")
                        .addParams("uid",AppApplication.getCurrentUserInfo().getUserId())
                        .addParams("name",getIntent().getStringExtra("poiAddress"))
                        .addParams("legalPersonName",bossName.getText().toString())
                        .addParams("phoneNumber",telNumber.getText().toString())
                        .addParams("email", webadress.getText().toString())
                        .addParams("websiteList", webadress.getText().toString())
                        .addParams("businessScope",intronote)
                        .build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Toast.makeText(getActivity(), "收藏成功", Toast.LENGTH_SHORT).show();
                    }
                });



            }
        }).start();
    }
    public void shoucang_mingpian(JSONObject data_into) {
//post数据到服务器

        OkHttpClient okHttpClient = new OkHttpClient();

        try {
            String content = String.valueOf(data_into);
            RequestBody requestBody = RequestBody.create(JSON, content);
            Request request = new Request.Builder()
                    .url("http://st.3dgogo.com/index/enterprise_info/create_collect_info")
                    .post(requestBody)
                    .build();

            //发送请求获取响应
            try {
                Response newresponse = okHttpClient.newCall(request).execute();
                //判断请求是否成功
                if (newresponse.isSuccessful()) {

                    Toast.makeText(getActivity(), "收藏成功", Toast.LENGTH_SHORT).show();
                     btShoucang.setClickable(false);
                }
                else
                {
                   // showMessagefail("上传失败1");
                    Toast.makeText(getActivity(),newresponse.body().string(),Toast.LENGTH_SHORT );
                    Log.v("收藏名片",newresponse.body().string());
                }
            } catch (IOException e) {
                e.printStackTrace();

            }
        } catch (Exception e) {
            e.printStackTrace();




        }

    }
    private JSONObject packDate()
    {
        String uid= (AppApplication.getCurrentUserInfo().getUserId());
        String name=getIntent().getStringExtra("poiAddress");
        String legalPersonName= bossName.getText().toString();
        String phoneNumber=     telNumber.getText().toString();
        String email=          webadress.getText().toString();
        String websiteList=    webadress.getText().toString();
        String businessScope= intronote;
        String createUid=uid;

        JSONObject data=new JSONObject();
        try{
            data.put("uid",uid);
            data.put("name",name);
            data.put("legalPersonName",legalPersonName);
            data.put("phoneNumber",phoneNumber);
            data.put("email",email);
            data.put("websiteList",websiteList);
            data.put("businessScope",businessScope);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return data;
    }


    private void sendSMS(String smsBody, String person)

    {

        Uri smsToUri = Uri.parse(person);

        Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);

        intent.putExtra("sms_body", smsBody);

        startActivity(intent);

    }
}
