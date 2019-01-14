package com.maoyongxin.myapplication.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.request.ReqGetVipMoney;
import com.maoyongxin.myapplication.http.request.ReqPay;
import com.maoyongxin.myapplication.http.request.ReqUserServer;
import com.maoyongxin.myapplication.http.response.LoginResponse;
import com.maoyongxin.myapplication.http.response.StringObjResponse;
import com.maoyongxin.myapplication.http.response.VipMoneyListResponse;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.myapp.AppTitleBarActivity;
import com.maoyongxin.myapplication.tool.PayResult;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyPayActivity extends AppTitleBarActivity {
    @BindView(R.id.tv_1Mmoney)
    TextView tv1Mmoney;
    @BindView(R.id.btn_1MPay)
    Button btn1MPay;
    @BindView(R.id.tv_3Mmoney)
    TextView tv3Mmoney;
    @BindView(R.id.btn_3MPay)
    Button btn3MPay;
    @BindView(R.id.tv_Ymoney)
    TextView tvYmoney;
    @BindView(R.id.btn_YPay)
    Button btnYPay;
    String orderInfo;
    private static final int SDK_PAY_FLAG = 1000;
    @BindView(R.id.userName)
    TextView userName;
    @BindView(R.id.userId)
    TextView userId;
    @BindView(R.id.vipNum)
    TextView vipNum;
    private String vip1MId;
    private String vip3MId;
    private String vipYId;
    Runnable payRunnable = new Runnable() {

        @Override
        public void run() {
            PayTask alipay = new PayTask(MyPayActivity.this);
            Map<String, String> result = alipay.payV2(orderInfo, true);
            alipay.payV2(orderInfo, true);
            Message msg = new Message();
            msg.what = SDK_PAY_FLAG;
            msg.obj = result;
            mHandler.sendMessage(msg);
        }
    };
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == SDK_PAY_FLAG) {

                PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                /**
                 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                 */
                String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                String resultStatus = payResult.getResultStatus();
                // 判断resultStatus 为9000则代表支付成功
                if (TextUtils.equals(resultStatus, "9000")) {
                    // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                    Toast.makeText(MyPayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    upLoadUserInfo();
                } else {
                    // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                    Toast.makeText(MyPayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    protected int bindLayout() {
        return R.layout.activity_vip_pay;
    }

    @Override
    protected void handlerPassMsg(int target, int target1, Object obj) {

    }

    private void upLoadUserInfo() {
        ReqUserServer.doLogin(this, getActivityTag(), AppApplication.getLogNum(), AppApplication.getLogPsw(), new EntityCallBack<LoginResponse>() {
            @Override
            public Class<LoginResponse> getEntityClass() {
                return LoginResponse.class;
            }

            @Override
            public void onSuccess(LoginResponse resp) {
                if (resp.is200()) {
                    AppApplication.setCurrentUserInfo(resp.obj);
                }
            }

            @Override
            public void onFailure(Throwable e) {
                showToastShort("登录失败");
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
    protected void initView() {
        super.initView();

        userName.setText(AppApplication.getCurrentUserInfo().getUserName());
        userId.setText("会员编号:"+AppApplication.getCurrentUserInfo().getUserId());
        vipNum.setText("会员到期日:"+AppApplication.getCurrentUserInfo().getVipNum());
        getVipMoney();
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        btn1MPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doPay(vip1MId);
            }
        });
        btn3MPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doPay(vip3MId);
            }
        });
        btnYPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doPay(vipYId);
            }
        });
    }

    private void getVipMoney() {
        ReqGetVipMoney.getMoney(this, getActivityTag(), new EntityCallBack<VipMoneyListResponse>() {
            @Override
            public Class<VipMoneyListResponse> getEntityClass() {
                return VipMoneyListResponse.class;
            }

            @Override
            public void onSuccess(VipMoneyListResponse resp) {
                if (resp.is200()) {
                    for (int i = 0; i < resp.obj.size(); i++) {
                        if (resp.obj.get(i).getType() == 1 && resp.obj.get(i).getVipTime() == 1) {//1个月
                            tv1Mmoney.setText(resp.obj.get(i).getVipMoney() + "元");
                            vip1MId = resp.obj.get(i).getId() + "";
                        } else if (resp.obj.get(i).getType() == 1 && resp.obj.get(i).getVipTime() == 3) {//3个月
                            tv3Mmoney.setText(resp.obj.get(i).getVipMoney() + "元");
                            vip3MId = resp.obj.get(i).getId() + "";
                        } else if (resp.obj.get(i).getType() == 2 && resp.obj.get(i).getVipTime() == 1) {//年费
                            tvYmoney.setText(resp.obj.get(i).getVipMoney() + "元");
                            vipYId = resp.obj.get(i).getId() + "";
                        }
                    }
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

    private void doPay(String vipId) {
        ReqPay.getAliMessage(this, getActivityTag(), AppApplication.getCurrentUserInfo().getUserId(), vipId, new EntityCallBack<StringObjResponse>() {
            @Override
            public Class<StringObjResponse> getEntityClass() {
                return StringObjResponse.class;
            }

            @Override
            public void onSuccess(StringObjResponse resp) {
                if (resp.is200()) {
                    orderInfo = resp.obj;
                    // 必须异步调用
                    Thread payThread = new Thread(payRunnable);
                    payThread.start();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//白底黑字+沉浸式导航栏

        ButterKnife.bind(this);
    }
}
