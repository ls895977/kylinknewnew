package com.maoyongxin.myapplication.ui;

import android.Manifest;
import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.request.ReqRefreshToken;
import com.maoyongxin.myapplication.http.request.ReqUserServer;
import com.maoyongxin.myapplication.http.response.LoginResponse;
import com.maoyongxin.myapplication.http.response.RefreshTokenResponse;
import com.maoyongxin.myapplication.myapp.AppActivity;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.server.utils.AMUtils;
import com.maoyongxin.myapplication.server.utils.NToast;
import com.maoyongxin.myapplication.server.widget.ClearWriteEditText;
import com.maoyongxin.myapplication.tool.PermissionsChecker;
import com.maoyongxin.myapplication.ui.bean.EventBusCarrier;
import com.maoyongxin.myapplication.ui.bean.RegisterSFBean;
import com.maoyongxin.myapplication.ui.qq.BaseUiListener;
import com.maoyongxin.myapplication.ui.qq.QQUserBean;
import com.tencent.connect.common.Constants;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.tauth.Tencent;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import okhttp3.Call;

import static cn.jiguang.share.android.api.AbsPlatform.getApplicationContext;
import static com.chad.library.adapter.base.listener.SimpleClickListener.TAG;
import static com.maoyongxin.myapplication.R.id.de_login_register;

public class LoginActivity extends AppActivity implements View.OnClickListener, BaseUiListener.BackQQData {
    private ImageView mImg_Background;
    private ClearWriteEditText mPhoneEdit, mPasswordEdit;
    private String phoneNum;
    private String pswNum;
    private static final int GOT_TOKEN = 1;
    private android.os.Handler handler = new android.os.Handler();
    private ProgressDialog mProgressDialog;
    private FrameLayout de_frm_backgroud;
    private boolean isAutoLogin;
    private boolean isFirstIn;
    private static final int REQUEST_CODE = 0; // 请求码
    private NbButton nbButtonLogin;
    private RelativeLayout rlContent;
    private Animator animator;
    private ImageView igheadview;
    private TextView mRegister;
    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };
    private PermissionsChecker mPermissionsChecker; // 权限检测器
    private int xc;
    private int yc;

    @Override
    protected int bindLayout() {
        EventBus.getDefault().register(this);  //事件的注册
        return R.layout.activity_login_new;
    }

    @Override
    protected void initView() {
        super.initView();
        ActivityCollector.addActivity(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        SharedPreferences preferences = this.getSharedPreferences("first_open", MODE_PRIVATE);
        isFirstIn = preferences.getBoolean("is_first_open", true);
        if (isFirstIn) {
            preferences = getSharedPreferences("first_open", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("is_first_open", false);
            editor.commit();
        } else {
            isAutoLogin = getSp(LoginActivity.this).getBoolean("isAuto", true);
        }
        mPhoneEdit = (ClearWriteEditText) findViewById(R.id.de_login_phone);
        mPasswordEdit = (ClearWriteEditText) findViewById(R.id.de_login_password);
        findViewById(R.id.de_login_wx).setOnClickListener(this);
        findViewById(R.id.de_login_qq).setOnClickListener(this);
        igheadview = (ImageView) findViewById(R.id.igheadview);
        nbButtonLogin = (NbButton) findViewById(R.id.de_login_sign);
        rlContent = (RelativeLayout) findViewById(R.id.rl_content);
        mRegister = (TextView) findViewById(de_login_register);
        TextView forgetPassword = (TextView) findViewById(R.id.de_login_forgot);
        de_frm_backgroud = (FrameLayout) findViewById(R.id.de_frm_backgroud);
        rlContent.getBackground().setAlpha(0);
        rlContent.setOnClickListener(this);

        setBackHeight(1);//参数代表背景图宽度占屏幕的几分之一，如2代表设置为屏幕宽度1/2

        forgetPassword.setOnClickListener(this);
        nbButtonLogin.setOnClickListener(this);
        mRegister.setOnClickListener(this);
        mImg_Background = (ImageView) findViewById(R.id.de_img_backgroud);
        mProgressDialog = new ProgressDialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog);
        mProgressDialog.setMessage("登录中");
        mProgressDialog.setCancelable(false);
        mPermissionsChecker = new PermissionsChecker(this);
        if (isAutoLogin) {
            phoneNum = getSp(LoginActivity.this).getString("num", "");
            pswNum = getSp(LoginActivity.this).getString("psw", "");
            mPhoneEdit.setText(phoneNum + "");
            mPasswordEdit.setText(pswNum + "");
            doLogin();
        }
    }

    private void setBackHeight(int bei) {
        WindowManager wm = this.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) de_frm_backgroud.getLayoutParams();
        lp.height = (height / bei);
        lp.width = width;
        de_frm_backgroud.setLayoutParams(lp);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mProgressDialog != null && !this.isFinishing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        }
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
    }

    @Override
    protected void initEvent() {
        super.initEvent();


        mPhoneEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 11) {
                    if (!AMUtils.isMobile(mPhoneEdit.getText().toString())) {
                        showToastShort("手机号输入不合法，请重新输入");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    @Override
    protected void initData() {
        super.initData();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.translate_anim);
                mImg_Background.startAnimation(animation);
            }
        }, 1);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == GOT_TOKEN) {

                }
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.de_login_sign:
            case R.id.rl_content:
                phoneNum = mPhoneEdit.getText().toString();
                pswNum = mPasswordEdit.getText().toString();
                if (phoneNum != null && !phoneNum.equals("")) {
                    if (pswNum != null && !pswNum.equals("")) {
                        doLogin();
                    } else {
                        mPasswordEdit.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.shake));
                        showToastShort("请输入密码");
                    }
                } else {
                    mPhoneEdit.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.shake));
                    showToastShort("请输入账号");
                }
                break;
            case R.id.de_login_register:
                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                // startActivityForResult(new Intent(this, RegisterActivity.class), AppApplication.ISREGISTER_OK);
                Pair<View, String> shanghuiHead = new Pair<View, String>(mRegister, "imghead");
                Pair<View, String> bt_reg = new Pair<View, String>(mRegister, "bt_reg");
                ActivityOptionsCompat optionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity());
                startActivity(intent, optionsCompat.toBundle());
                break;
            case R.id.de_login_forgot:
                startActivity(new Intent(this, ForgetPasswordActivity.class));
                break;
            case R.id.de_login_wx://微信登录
                loginWX();
                break;
            case R.id.de_login_qq://qq登录
                loginQQ();
                break;
        }
    }

    /**
     * 融云登录
     */
    private void doLogin() {
        nbButtonLogin.setClickable(false);
        Log.e("aa", phoneNum + "---------" + pswNum);
        ReqUserServer.doLogin(this, getActivityTag(), phoneNum, pswNum, new EntityCallBack<LoginResponse>() {
            @Override
            public Class<LoginResponse> getEntityClass() {
                return LoginResponse.class;
            }

            @Override
            public void onSuccess(LoginResponse resp) {
                Log.e("aa", phoneNum + "----onSuccess-----" + pswNum);
                if (resp.is200()) {

                    AppApplication.setLogNum(phoneNum);
                    AppApplication.setLogPsw(pswNum);
                    AppApplication.setMyPassword(pswNum);
                    AppApplication.setCurrentUserInfo(resp.obj);
                    connect(LoginActivity.this, resp.obj.getToken(), resp.obj.getUserId());
                    backdatatoserver();
                } else {
                    showToastShort(resp.msg);
                }
                mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Throwable e) {
                //  mProgressDialog.dismiss();
                nbButtonLogin.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.shake));
                showToastShort("请确认账号或密码");
                nbButtonLogin.setClickable(true);
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
        Log.e("aa", token + "----connect-----" + userId);

        RongIM.connect(token, new RongIMClient.ConnectCallback() {
            /**
             * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
             * 2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
             */
            @Override
            public void onTokenIncorrect() {
                ReqRefreshToken.refreshToken(LoginActivity.this, getActivityTag(), userId, new EntityCallBack<RefreshTokenResponse>() {
                    @Override
                    public Class<RefreshTokenResponse> getEntityClass() {
                        return RefreshTokenResponse.class;
                    }

                    @Override
                    public void onSuccess(RefreshTokenResponse resp) {
                        if (resp.is200()) {
                            connect(LoginActivity.this, resp.obj.getToken(), resp.obj.getUserId() + "");
                        } else {
                            NToast.shortToast(LoginActivity.this, resp.msg);
                        }
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onCancelled(Throwable e) {
                        e.printStackTrace();
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
                Log.e("aa", "--对应的用户--onSuccess-----" + userId);
                if (AppApplication.getMyCurrentLocation() == null) {
                    showToastShort("获取位置信息失败，请检查定位权限或者重新登陆");
                } else {
                    showToastShort("登录成功");
                    nbButtonLogin.startAnim();
                    int xc = (nbButtonLogin.getLeft() + nbButtonLogin.getRight()) / 2;
                    int yc = (nbButtonLogin.getTop() + nbButtonLogin.getBottom()) / 2;
                    animator = ViewAnimationUtils.createCircularReveal(nbButtonLogin, xc, yc, 0, 1111);
                    animator.setDuration(300);
                    animator.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    intent.putExtra("userId", userId + "");
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.scale_in, R.anim.anim_out);
                                    //CircularAnimUtil.startActivity(getActivity(),intent,nbButtonLogin,R.color.blue_tiny,1000 );

                                    finish();
                                }
                            }, 500);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {

                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                    animator.start();
                    rlContent.getBackground().setAlpha(0);
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

    private void backdatatoserver() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.post().url("http://st.3dgogo.com/index/action_log/setActionLogApi.html")
                        .addParams("action_uid", phoneNum)
                        .addParams("action_user_name", AppApplication.getCurrentUserInfo().getUserName())
                        .addParams("action_type", "1")
                        .addParams("action_remarks", "APP内登陆")
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

    /**
     * QQ 登录
     */
    Tencent mTencent;

    public void loginQQ() {
        mTencent = Tencent.createInstance("101539710", getApplicationContext());
        if (!mTencent.isSessionValid()) {
            mTencent.login(this, "all", new BaseUiListener(mTencent, getApplicationContext(), this));
        }
    }

    /**
     * 微信 登录
     */
    public void loginWX() {
//先判断是否安装微信APP,按照微信的说法，目前移动应用上微信登录只提供原生的登录方式，需要用户安装微信客户端才能配合使用。
        if (!isWeixinAvilible(this)) {
            Toast.makeText(getActivity(), "您还未安装微信客户端", Toast.LENGTH_SHORT).show();
            return;
        }
        //微信登录
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "diandi_wx_login";
        //像微信发送请求
        AppApplication.mWxApi.sendReq(req);

    }

    /**
     * 判
     * 断是否绑定oping
     *
     * @param wx_openid
     * @param qq_openid
     */
    private String Name = "";
    Gson gson = new Gson();

    public void getOpenidApi(final String wx_openid, final String qq_openid,final String userICon) {
        OkHttpUtils.post().url("http://st.3dgogo.com/index/user/getOpenidApi/")
                .addParams("wx_openid", wx_openid)
                .addParams("qq_openid", qq_openid)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                if (response.contains("null")) {
                    bindOpenApi(wx_openid, qq_openid,userICon);
                } else if (response.contains("200")) {
                    RegisterSFBean bean = gson.fromJson(response, RegisterSFBean.class);
                    phoneNum = bean.getInfo().getUserId();
                    pswNum = bean.getInfo().getPassword();
                    Log.e("aa", "--phoneNum--------" + phoneNum + "-----pswNum-------" + pswNum);
                    doLogin();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("aa", requestCode + "--------------" + resultCode);
        Tencent.onActivityResultData(requestCode, resultCode, data, new BaseUiListener(mTencent, getApplicationContext(), this));
        if (requestCode == Constants.REQUEST_API) {
            if (resultCode == Constants.REQUEST_LOGIN) {
                Tencent.handleResultData(data, new BaseUiListener(mTencent, getApplicationContext(), this));
            }
        }
    }

    /**
     * 返回QQ回调参数
     *
     * @param qqUserBean
     */
    @Override
    public void backQQData(QQUserBean qqUserBean, String UserId) {
        getOpenidApi("", UserId,qqUserBean.getFigureurl_2());
    }

    // 微信成功返回参数
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(EventBusCarrier carrier) {
        String openid = (String) carrier.getOpenid();
        String USericon=(String )carrier.getUserBean().getHeadimgurl();
        getOpenidApi(openid, "",USericon);
        Log.e("aa", "-------微信成功返回参数-" + carrier.getUserBean().getNickname());
    }

    /**
     * 判断 用户是否安装微信客户端
     */
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this); //解除注册
        super.onDestroy();
    }

    public void bindOpenApi(final String wx_openid, final String qq_openid,final String userIcon) {
        OkHttpUtils.post().url("http://st.3dgogo.com/index/user/bindingOpenidApi.html")
                .addParams("wx_openid", wx_openid)
                .addParams("qq_openid", qq_openid)
                .addParams("userName", Name)
                .addParams("headImg",userIcon)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                Log.e("aa", "---------" + response);
                if (response.contains("null")) {

                } else if (response.contains("200")) {
                    RegisterSFBean bean = gson.fromJson(response, RegisterSFBean.class);
                    phoneNum = bean.getInfo().getUserId();
                    pswNum = bean.getInfo().getPassword();
                    mPhoneEdit.setText(phoneNum + "");
                    mPasswordEdit.setText(pswNum + "");
                    showToastLong("第三方软件登陆中");
                    doLogin();
                }
            }
        });
    }
}
