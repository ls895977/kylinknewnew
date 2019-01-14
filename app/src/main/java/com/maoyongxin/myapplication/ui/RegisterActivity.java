package com.maoyongxin.myapplication.ui;

import android.animation.Animator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.request.ReqRefreshToken;
import com.maoyongxin.myapplication.http.request.ReqRegister;
import com.maoyongxin.myapplication.http.request.ReqSMS;
import com.maoyongxin.myapplication.http.request.ReqUserServer;
import com.maoyongxin.myapplication.http.response.LoginResponse;
import com.maoyongxin.myapplication.http.response.RefreshTokenResponse;
import com.maoyongxin.myapplication.http.response.RegisterResponse;
import com.maoyongxin.myapplication.http.response.SMSResponse;
import com.maoyongxin.myapplication.myapp.AppActivity;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.server.utils.NToast;
import com.maoyongxin.myapplication.server.widget.ClearWriteEditText;
import com.maoyongxin.myapplication.ui.bean.RegisterSFBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

public class RegisterActivity extends AppActivity {

    @BindView(R.id.reg_username)
    ClearWriteEditText regUsername;
    @BindView(R.id.reg_phone)
    ClearWriteEditText regPhone;
    @BindView(R.id.reg_password)
    ClearWriteEditText regPassword;
    @BindView(R.id.reg_button)
    Button regButton;
    @BindView(R.id.reg_login)
    TextView regLogin;

    @BindView(R.id.rge_code)
    ClearWriteEditText rgecode;

    @BindView(R.id.tv_getcode)
    TextView tvgetcode;
    @BindView(R.id.again_password)
    ClearWriteEditText againpassword;

    private String userName;
    private String phone;
    private String passWord;
    private String yz_code;
    private String smsCode;
    private String againpsw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ActivityCollector.addActivity(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//白底黑字+沉浸式导航栏
        ButterKnife.bind(this);
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_register;
    }

    String registerStatus = "";

    @Override
    protected void initView() {
        super.initView();
        if (getIntent().getStringExtra("title") != null) {
            registerStatus = getIntent().getStringExtra("title");
            wx_openid = getIntent().getStringExtra("wx_openid");
            qq_openid = getIntent().getStringExtra("qq_openid");
            Name = getIntent().getStringExtra("Name");
        } else {
            registerStatus = "";
        }

    }

    @Override
    protected void initEvent() {
        super.initEvent();
        regLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = regUsername.getText().toString();
                phone = regPhone.getText().toString();
                passWord = regPassword.getText().toString();
                yz_code = rgecode.getText().toString();
                againpsw = againpassword.getText().toString();
                if (userName != null && !userName.equals("")) {
                    if (phone != null && !phone.equals("")) {
                        if (passWord != null && !passWord.equals("")) {
                            if (yz_code.equals(smsCode)) {
                                if (againpsw.equals(passWord)) {
                                    if (registerStatus.equals("registerStatus")) {
                                        verifyUserPhoneApi(userName, yz_code);
                                    } else {
                                        doRegisternew();
//                                        doRegister();
                                    }
                                } else {
                                    showToastShort("两次密码不一致");
                                }
                            } else {
                                showToastShort("请输入正确验证码");
                            }
                        } else {
                            showToastShort("请输入密码");
                        }
                    } else {
                        showToastShort("请输入电话");
                    }
                } else {
                    showToastShort("请输入昵称");
                }
            }
        });


        tvgetcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = regPhone.getText().toString();
                if (phone.length() == 11) {

                    getmsg();
                    tvgetcode.setTextColor(getResources().getColor(R.color.def_sub_3_gray_99));
                    tvgetcode.setClickable(false);
                } else {
                    showToastShort("请输入有效手机号码");
                }

            }
        });


    }

    public static boolean isPhoneNumber(String phoneNo) {
        if (TextUtils.isEmpty(phoneNo)) {
            return false;
        }

        if (phoneNo.length() == 11) {
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

        return false;
    }

    private void getmsg() {
        OkHttpUtils.post().url("http://st.3dgogo.com/index/send_sms/sendSmsApp.html")
                .addParams("phone", phone)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    Log.e("aa","---------"+response);
                    JSONObject data = new JSONObject(response);
                    smsCode = data.getString("smsCode");
                    String msg = data.getString("msg");
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void getSms() {
        ReqSMS.getSmS(this, getActivityTag(), phone, new EntityCallBack<SMSResponse>() {
            @Override
            public Class<SMSResponse> getEntityClass() {
                return SMSResponse.class;
            }

            @Override
            public void onSuccess(final SMSResponse resp) {
                if (resp.is200()) {
                    final EditText inputServer = new EditText(RegisterActivity.this);
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setTitle("请输入收到的验证码").setView(inputServer)
                            .setNegativeButton("取消", null);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //     doRegisternew(inputServer.getText().toString());

                        }
                    }).show();
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

    private void doRegisternew() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.post().url("http://st.3dgogo.com/index/registered/andUserApi.html")
                        .addParams("userName", userName)
                        .addParams("userPhone", phone)
                        .addParams("note", "1")
                        .addParams("password", passWord)
                        .build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject data = new JSONObject(response);
                            String msg = data.getString("msg");
                            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

                            int code = data.getInt("code");
                            if (code == 200) {
                                Intent intent = new Intent(getActivity(), regesite_headimg.class);
                                intent.putExtra("userId", data.getString("userId"));
                                intent.putExtra("nickname", userName);
                                intent.putExtra("userpsswd", regPassword.getText().toString());
                                backdatatoserver(data.getString("userId"), userName, phone);
                                startActivity(intent);
                            } else {

                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }).start();

    }

    private void backdatatoserver(final String uid, final String uname, final String telenum) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.post().url("http://st.3dgogo.com/index/action_log/setActionLogApi.html")
                        .addParams("action_uid", uid)
                        .addParams("action_user_name", uname)
                        .addParams("action_type", "2")
                        .addParams("action_ip", telenum)
                        .addParams("action_remarks", "新用户注册")
                        .build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {

                    }
                });

            }
        }).start();
    }

    private void doRegister() {
        ReqRegister.doRegister(this, getActivityTag(), userName, phone, passWord, new EntityCallBack<RegisterResponse>() {
            @Override
            public Class<RegisterResponse> getEntityClass() {
                return RegisterResponse.class;
            }

            @Override
            public void onSuccess(final RegisterResponse resp) {
                if (resp.is200()) {
                    showToastShort(resp.msg);
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setTitle("提示：账号是唯一标识，登陆可使用账号或者手机")
                            .setMessage("请记住您的账号(建议截图保存)：" + resp.obj.getUserId())
                            .setPositiveButton("我记住了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(getActivity(), regesite_headimg.class);
                                    intent.putExtra("userId", resp.obj.getUserId());
                                    intent.putExtra("userpsswd", regPassword.getText().toString());
                                    startActivity(intent);
                                }
                            }).show();
                }
            }

            @Override
            public void onFailure(Throwable e) {
                e.printStackTrace();
                showToastShort("注册失败");
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
    protected void doBusiness() {
        super.doBusiness();
    }

    /**
     * 验证手机号是否存在。存在返回账号
     *
     * @param userPhone
     * @param smsCode
     */
    public void verifyUserPhoneApi(String userPhone, String smsCode) {
        OkHttpUtils.post().url("http://st.3dgogo.com/index/user/verifyUserPhoneApi/")
                .addParams("userPhone", userPhone)
                .addParams("smsCode", smsCode)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                Log.e("aa", "---------" + response);
                if (response.contains("200")) {
                    if (response.contains("null")) {
                        bindingOpenidApi();
                    } else {//有账号
                        RegisterSFBean bean = gosn.fromJson(response, RegisterSFBean.class);
                        doLogin(bean.getInfo().getUserId(), bean.getInfo().getPassword());
                    }
                } else if (response.contains("500")) {
                }
            }
        });
    }

    /**
     * 验证手机号是否存在。存在返回账号
     *
     * @param wx_openid
     * @param qq_openid
     */
    private String wx_openid = "", qq_openid = "", Name = "";
    Gson gosn = new Gson();

    public void bindingOpenidApi() {
        OkHttpUtils.post().url("http://st.3dgogo.com/index/user/bindingOpenidApi/")
                .addParams("wx_openid", wx_openid)
                .addParams("qq_openid", qq_openid)
                .addParams("userName", Name)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                Log.e("aa", "---------" + response);
                if (response.contains("200")) {
                    RegisterSFBean bean = gosn.fromJson(response, RegisterSFBean.class);
                    doLogin(bean.getInfo().getUserId(), bean.getInfo().getPassword());
                } else if (response.contains("500")) {
                }
            }
        });
    }

    /**
     * 融云登录
     */
    private void doLogin(final String userId, final String userpsswd) {
        ReqUserServer.doLogin(this, getActivityTag(), userId, userpsswd, new EntityCallBack<LoginResponse>() {
            @Override
            public Class<LoginResponse> getEntityClass() {
                return LoginResponse.class;
            }

            @Override
            public void onSuccess(LoginResponse resp) {
                if (resp.is200()) {
                    AppApplication.setLogNum(userId);
                    AppApplication.setLogPsw(userpsswd);
                    AppApplication.setMyPassword(userpsswd);
                    AppApplication.setCurrentUserInfo(resp.obj);
                    connect(RegisterActivity.this, resp.obj.getToken(), resp.obj.getUserId());
                } else {
                    showToastShort(resp.msg);
                }
            }

            @Override
            public void onFailure(Throwable e) {
            }

            @Override
            public void onCancelled(Throwable e) {
                // mProgressDialog.dismiss();
            }

            @Override
            public void onFinished() {
                // mProgressDialog.dismiss();
            }
        });
    }

    /**
     * <p>连接服务器，在整个应用程序全局，只需要调用一次，需在 {@linkiminit(Context)} 之后调用。</p>
     * <p>如果调用此接口遇到连接失败，SDK 会自动启动重连机制进行最多10次重连，分别是1, 2, 4, 8, 16, 32, 64, 128, 256, 512秒后。
     * 在这之后如果仍没有连接成功，还会在当检测到设备网络状态变化时再次进行重连。</p>
     *
     * @param token 从服务端获取的用户身份令牌（Token）。
     * @return RongIM  客户端核心类的实例。
     */
    public void connect(final Context context, String token, final String userId) {
        RongIM.connect(token, new RongIMClient.ConnectCallback() {
            /**
             * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
             * 2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
             */
            @Override
            public void onTokenIncorrect() {
                ReqRefreshToken.refreshToken(RegisterActivity.this, getActivityTag(), userId, new EntityCallBack<RefreshTokenResponse>() {
                    @Override
                    public Class<RefreshTokenResponse> getEntityClass() {

                        return RefreshTokenResponse.class;
                    }

                    @Override
                    public void onSuccess(RefreshTokenResponse resp) {
                        if (resp.is200()) {
                            connect(RegisterActivity.this, resp.obj.getToken(), resp.obj.getUserId() + "");
                        } else {
                            NToast.shortToast(RegisterActivity.this, resp.msg);
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

            /**
             * 连接融云成功
             * @param userid 当前 token 对应的用户 id
             */
            @Override
            public void onSuccess(String userid) {
                if (AppApplication.getMyCurrentLocation() == null) {
                    showToastShort("获取位置信息失败，请检查定位权限或者重新登陆");
                } else {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("userId", userId + "");
                    startActivity(intent);
                }
            }

            /**
             * 连接融云失败
             * @param errorCode 错误码，可到官网 查看错误码对应的注释
             */
            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.d("LoginActivity", "--onError--ErrorCode：" + errorCode);
            }
        });
    }

}
