package com.maoyongxin.myapplication.tool;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.UserInfoService;
import com.maoyongxin.myapplication.myapp.AppActivity;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.ui.AddressHomeCheckActivity;
import com.maoyongxin.myapplication.ui.MyPayActivity;
import com.maoyongxin.myapplication.ui.ShowWebUrlActivity;
import com.maoyongxin.myapplication.ui.comunitymateDetailActivity;
import com.maoyongxin.myapplication.ui.mingpian;

import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.maoyongxin.myapplication.http.second.okhttp.OkHttpUtils.JSON;
import static com.maoyongxin.myapplication.http.second.okhttp.OkHttpUtils.getInstance;

public class NaviActivity extends AppActivity {
    private TextView tv_poiName, tv_poiType, tv_poiAddress;
    private LinearLayout line_navi;
    private ProgressDialog mProgressDialog;
    private android.os.Handler handler = new android.os.Handler();
    private Button   bt_newCall, bt_Sendmessage, bt_zhuanfa, bt_shoucang;
    private TextView lishijilu,textView, tv_companyName, tv_bossName, tv_tel_number, tv_emaill, tv_webAdress, tv_zhiwei,tv_creditCode,tv_toshoucangjia;
    private Boolean parsed=false;
    private String business;
    private LinearLayout scrollview;
    private int MY_PERMISSIONS_REQUEST_READ_CONTACTS;
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
    private TextView companyadress;
    private String api_slide="http://st.3dgogo.com/index/user/set_get_enterprise_num.html";
    private Boolean  vipstatu;
    private  String yewu;
    private String bossname;
    private  String tel;
    private  String email;
    private  String webAdress;
    private  String creditCode="";
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean hasSoftKeys(WindowManager windowManager) {
        Display d = windowManager.getDefaultDisplay();
        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        d.getRealMetrics(realDisplayMetrics);
        int realHeight = realDisplayMetrics.heightPixels;
        int realWidth = realDisplayMetrics.widthPixels;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);
        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;
        return (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
    }
    @Override
    protected int bindLayout() {
        return R.layout.activity_navi;
    }

    @Override
    protected void initData() {
        vipstatu=getIntent().getBooleanExtra("vipstatu",false);

    }

    @Override
    protected void initView() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//白底黑字+沉浸式导航栏
        line_navi = (LinearLayout) findViewById(R.id.line_navi);
        tv_poiName = (TextView) findViewById(R.id.tv_poiName);
        tv_poiType = (TextView) findViewById(R.id.tv_poiType);
        tv_poiAddress = (TextView) findViewById(R.id.tv_poiAddress);
        companyadress=(TextView)findViewById(R.id.companyadress);
        lishijilu=(TextView)findViewById(R.id.lishijilu);
        scrollview=(LinearLayout) findViewById(R.id.content_ScrollView);
        bt_newCall = (Button) findViewById(R.id.bt_newCall);
        bt_Sendmessage = (Button) findViewById(R.id.bt_Sendmessage);
        bt_zhuanfa = (Button) findViewById(R.id.bt_zhuanfa);
        bt_shoucang = (Button) findViewById(R.id.bt_shoucang);

        textView = (TextView) findViewById(R.id.textView);
        tv_companyName = (TextView) findViewById(R.id.companyName);
        tv_bossName = (TextView) findViewById(R.id.bossName);
        tv_tel_number = (TextView) findViewById(R.id.tel_number);
        tv_emaill = (TextView) findViewById(R.id.emaill);
        tv_webAdress = (TextView) findViewById(R.id.webadress);
        tv_zhiwei = (TextView) findViewById(R.id.zhiwei);
        tv_poiName.setText(getIntent().getStringExtra("poiAddress"));
        cpnName=tv_poiName.getText().toString();

        tv_poiType.setText("企业");
        tv_companyName.setText(getIntent().getStringExtra("poiAddress"));
        companyadress.setText(getIntent().getStringExtra("poiName"));
        tv_poiAddress.setText(getIntent().getStringExtra("poiName"));
        // tv_creditCode=(TextView)findViewById(R.id.creditCode);
        tv_toshoucangjia=(TextView)findViewById(R.id.toshoucangjia);



        if(vipstatu)
        {
            get_data_xk(getIntent().getStringExtra("poiAddress"));
        }
        else{
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
            builder.setTitle("您今日查询已达上限，您可以提升VIP等级或加入团队来提升每日查询上限")

                    .setNegativeButton("加入团队", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent=new Intent(getActivity(), AddressHomeCheckActivity.class);
                            startActivityThenFinish(intent);
                        }
                    })
                    .setPositiveButton("提升vip", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent=new Intent(getActivity(), MyPayActivity.class);
                            startActivityThenFinish(intent);
                        }
                    }).show();
        }

        userId=AppApplication.getCurrentUserInfo().getUserId().toString();
    }



    @Override
    protected void initEvent() {
        super.initEvent();
        line_navi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressDialog = new ProgressDialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog);
                mProgressDialog.setMessage("导航初始化中，请稍候");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doNavi();
                    }
                }, 1000);
            }
        });


        tv_tel_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + tv_tel_number.getText()));
                startActivity(intent);
                call="1";
                //点击电话号码直接拨号

            }
        });
        bt_newCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    int i= ContextCompat.checkSelfPermission(NaviActivity.this,Manifest.permission.CALL_PHONE);
                    if(i !=PackageManager.PERMISSION_GRANTED)
                    {
                        showDialogTipUserRequestPermission("拨打电话");
                    }
                    else{

                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + tv_tel_number.getText()));
                        call="1";
                        startActivity(intent);
                    }
                }

                // 显示给用户的解释






                //点击电话按钮直接拨号
            }
        });
        tv_toshoucangjia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), mingpian.class);

                startActivity(intent);
                overridePendingTransition(R.anim.slide_left_in,R.anim.slide_right_out);
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

        bt_Sendmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isPhoneNumber(tel)) {

                    sendSMS( tv_bossName.getText()+"经理，您好！我是刚才和您通话的那位，很高兴认识您，这是我的电话请惠存", "smsto:" + tv_tel_number.getText());
                    msgsend="1";
                } else {
                    showToastShort("不是有效的电话号码");
                }
                //  }
                // }


            }
        });//判断号码格式并发送短信

        bt_zhuanfa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });//转发到好友的收藏夹

        bt_shoucang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(parsed){

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            shoucang_mingpian(packDate());
                            collcted="1";
                        }
                    }).start();

                }
                else{
                    Toast.makeText(NaviActivity.this, "请解析数据先", Toast.LENGTH_SHORT).show();
                }

            }
        });
//收藏到自己的收藏夹
        tv_webAdress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(getActivity(), ShowWebUrlActivity.class);
                intent.putExtra("url", tv_webAdress.getText());
                startActivity(intent);
                //点击网址访问官网

            }
        });

    }

    private void doNavi() {
        Poi start = new Poi(getIntent().getStringExtra("myAddress"), new LatLng(Double.parseDouble(getIntent().getStringExtra("myLat")), Double.parseDouble(getIntent().getStringExtra("myLng"))), "");
        Poi end = new Poi(getIntent().getStringExtra("poiName"), new LatLng(Double.parseDouble(getIntent().getStringExtra("poiLat")), Double.parseDouble(getIntent().getStringExtra("poiLng"))), "");
        AmapNaviPage.getInstance().showRouteActivity(NaviActivity.this, new AmapNaviParams(start, null, end, AmapNaviType.DRIVER), new INaviInfoCallback() {
            @Override
            public void onInitNaviFailure() {

            }

            @Override
            public void onGetNavigationText(String s) {
            }

            @Override
            public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

            }

            @Override
            public void onArriveDestination(boolean b) {

            }

            @Override
            public void onStartNavi(int i) {
                finish();
            }

            @Override
            public void onCalculateRouteSuccess(int[] ints) {

            }

            @Override
            public void onCalculateRouteFailure(int i) {

            }

            @Override
            public void onStopSpeaking() {

            }
        });
        finish();
    }


    private void get_data_xk(final String companyName) {
//get自身服务器数据
        final String XkOne = "http://st.3dgogo.com/index/enterprise_info/enterprise_info/name/";//https://way.jd.com/jindidata/detail_info?name=
        final String XkThere = ".html";//"&appkey=35be7935b960fc78b04ae16ae654193a";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(XkOne + companyName + XkThere).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJsonXK(responseData);

                } catch (Exception e) {
                    // showMessagefail("解析失败");
                }
            }
        }).start();
    }

    private void parseJsonXK(String companyName) {
//post自身服务器数据
        int Result_code = 0;

        try {

            JSONObject jsonObject = new JSONObject(companyName);
            Result_code = jsonObject.getInt("code");
            if (Result_code == 10000) {

                JSONObject data = jsonObject.getJSONObject("result").getJSONObject("result").getJSONObject("baseInfo");

                yewu = parse_Value(data, "businessScope");
                business=yewu;
                bossname = parse_Value(data, "legalPersonName");
                tel = parse_Value(data, "phoneNumber");
                cpnNum=tel;
                email = parse_Value(data, "email");
                webAdress = parse_Value(data, "websiteList");
                parsed=true;
                showMessage(yewu, bossname, tel, email, webAdress,creditCode,Result_code);
                slidenum();

            } else if (Result_code == 500) {
                showMessagefail("查询数据失败，正在深度查询");

                get_data_jd(getIntent().getStringExtra("poiAddress"));

            }
        } catch (Exception e) {
            // showMessagefail("查询数据失败，不扣除查询点");

        }


    }


    private void get_data_jd(final String companyName) {
        final String One = "https://way.jd.com/jindidata/detail_info?name=";
        final String There = "&appkey=35be7935b960fc78b04ae16ae654193a";
//标准get获取数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(One + companyName + There).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJson_JD(responseData);
                    parsed=true;
                    slidenum();
                } catch (Exception e) {
                    showMessagefail("深度解析失败，试一下其他的吧（不扣除数据点）");
                }
            }
        }).start();
    }


    private void parseJson_JD(String Jsondata) {
        //get京东数据
        String yewu;
        String bossname;
        String tel;
        String email;
        String webAdress;
        String creditCode;
        try {

            JSONObject jsonObject = new JSONObject(Jsondata);
            JSONObject data = jsonObject.getJSONObject("result").getJSONObject("result").getJSONObject("baseInfo");
            JSONObject data_complie = jsonObject;

            yewu = parse_Value(data, "businessScope");
            bossname = parse_Value(data, "legalPersonName");
            tel = parse_Value(data, "phoneNumber");
            cpnNum=tel;
            email = parse_Value(data, "email");
            webAdress = parseWEB_Value(data, "websiteList");
            creditCode=parse_Value(data, "creditCode");

            showMessage(yewu, bossname, tel, email, webAdress,creditCode, 200);
            updata_to_server(data_complie);


        } catch (Exception e) {
            showToastLong("数据解析失败");
        }

    }

    private String parse_Value(JSONObject data, String value) {
        String com_value = "暂无数据";
        if (data.has(value)) {
            try {
                com_value = data.getString(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return com_value;
    }

    private String parseWEB_Value(JSONObject data, String value) {
        String com_value = "暂无数据";
        if (data.has(value)) {
            try {
                com_value = data.getJSONArray(value).getString(0);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return com_value;
    }

    private void showMessagefail(final String yewu) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(NaviActivity.this,yewu,Toast.LENGTH_SHORT).show();


            }
        });

    }

    private void showMessage(final String yewu, final String bossname, final String tel, final String email, final String webadress,final String creditCode, final int Result_code) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView yewuText = new TextView(NaviActivity.this);
                final UserInfoService resp = new UserInfoService();
                tv_companyName.setText(getIntent().getStringExtra("poiAddress"));

                yewuText.setTextColor(Color.BLACK);
                yewuText.setText(yewu);

                scrollview.addView(yewuText);
                tv_bossName.setText(bossname);

                tv_webAdress.append(cleanUrl(webadress).toString());
                tv_tel_number.append(tel);
                tv_zhiwei.setText("总经理");
                tv_emaill.append(email);

                //   tv_creditCode.setText(creditCode);


            }
        });

    }

    private void updata_to_server(final JSONObject data) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                postJson_new(data);

            }
        }).start();


    }

    private void postJson_new(JSONObject data_all) {
//post获取数据并解析
        OkHttpClient okHttpClient = new OkHttpClient();

        try {
            String content = String.valueOf(data_all);
            RequestBody requestBody = RequestBody.create(JSON, content);
            Request request = new Request.Builder()
                    .url("http://st.3dgogo.com/index/enterprise_info/create_enterprise_info")
                    .post(requestBody)
                    .build();

            //发送请求获取响应
            try {
                Response response = okHttpClient.newCall(request).execute();
                //判断请求是否成功
                if (response.isSuccessful()) {


                    showMessagefail("深度解析完成");

                }
            } catch (IOException e) {
                e.printStackTrace();
                showMessagefail("上传失败");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            showMessagefail("上传失败");
        }


    }

    private void sendSMS(String smsBody, String person)

    {

        Uri smsToUri = Uri.parse(person);

        Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);

        intent.putExtra("sms_body", smsBody);

        startActivity(intent);

    }

    public static boolean isPhoneNumber(String phoneNo) {
        if (TextUtils.isEmpty(phoneNo)) {
            return false;
        }

        if (phoneNo.length() == 11){
            for (int i = 0; i < 11; i++) {
                if (!PhoneNumberUtils.isISODigit(phoneNo.charAt(i))) {
                    return false;
                }
            }
            Pattern p = Pattern.compile("^((13[^4,\\D])" + "|(134[^9,\\D])" +
                    "|(14[5,7])" +
                    "|(15[^4,\\D])" +
                    "|(17[3,6-8])" +
                    "|(18[0-9]))\\d{8}$");
            Matcher m = p.matcher(phoneNo);
            return m.matches();
        }
        {

        }
        return false;
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

                    showMessagefail("收藏成功");
                    bt_shoucang.setClickable(false);
                }
                else
                {
                    showMessagefail("上传失败1");
                    showMessagefail(newresponse.body().string());
                    Log.v("收藏名片",newresponse.body().string());
                }
            } catch (IOException e) {
                e.printStackTrace();
                showMessagefail("上传失败2");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showMessagefail("上传失败3");
            showMessagefail(data_into.toString());


        }

    }
    private JSONObject packDate()
    {
        String uid= (AppApplication.getCurrentUserInfo().getUserId());
        String name=getIntent().getStringExtra("poiAddress");
        String legalPersonName= tv_bossName.getText().toString();
        //  String creditCode=      tv_creditCode.getText().toString();
        String phoneNumber=     tv_tel_number.getText().toString();
        String email=           tv_emaill.getText().toString();
        String websiteList=     tv_webAdress.getText().toString();
        String businessScope= business;
        String createUid=uid;

        JSONObject data=new JSONObject();
        try{
            data.put("uid",uid);
            data.put("name",name);
            data.put("legalPersonName",legalPersonName);
            //  data.put("creditCode",creditCode);
            data.put("phoneNumber",phoneNumber);
            data.put("email",email);
            data.put("websiteList",websiteList);
            data.put("businessScope",business);
            data.put("createUid",uid);
        }
        catch (Exception e){e.printStackTrace();}
        return data;
    }
    private void showDialogTipUserRequestPermission(String type) {

        new AlertDialog.Builder(this)
                .setTitle(type+"权限不可用")
                .setMessage("通过“彼信”直接"+type+"，能让您更加便捷的联系客户；\n否则，您将无法正常使用“麒麟”，请在后台赋予相应权限")
                .setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startRequestPermission();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setCancelable(false).show();
    }
    // 开始提交请求权限

    private void startRequestPermission() {
        // ActivityCompat.requestPermissions(this, java.security.acl.Permission, 321);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(getActivity(),"返回键退出",Toast.LENGTH_SHORT);
        recordaction(userId,cpnName,cpnNum,collcted,msgsend,call);
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
                    showMessagefail("解析失败");
                }
            }
        }).start();
    }

    private  String cleanUrl(String webUrl){
        String rep ="\\";
        final String url=  webUrl.replace("[","").replace("]","");

        final String ImgUrl=url.replace(rep,"");
        return webUrl;
    }


    private void slidenum() {

        OkHttpClient okHttpClient = new OkHttpClient();


        try {

            RequestBody requestBody = new FormBody.Builder()
                    .add("uid", AppApplication.getCurrentUserInfo().getUserId())
                    .build();

            Request request = new Request.Builder()
                    .url(api_slide)
                    .post(requestBody)
                    .build();


            try {
                Call call = okHttpClient.newCall(request);
                //判断请求是否成功
                try {
                    Response response = call.execute();

                    try {

                        JSONObject jsonObject = new JSONObject(response.body().string());

                        if (jsonObject.getInt("code")== 200) {

                            showMessagefail("数据解析成功");
                            // Toast.makeText(NaviActivity.this,"查询成功扣除一个查询点",Toast.LENGTH_SHORT).show();


                        }
                        else if (jsonObject.getInt("code") == 500) {
                            //    showMessagefail("扣除失败，查一下接口");
                            Toast.makeText(NaviActivity.this,"数据解析失败，不扣除查询点数",Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        //  showMessagefail("深度解析失败3");


                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } catch (Exception e) {
                e.printStackTrace();
                showMessagefail("登陆失败");
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
            showMessagefail("登陆失败");
        }
    }
}
