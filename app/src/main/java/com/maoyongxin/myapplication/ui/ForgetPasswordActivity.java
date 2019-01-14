package com.maoyongxin.myapplication.ui;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.request.ReqChangePsw;
import com.maoyongxin.myapplication.http.request.ReqSMS;
import com.maoyongxin.myapplication.http.response.ChangePswResponse;
import com.maoyongxin.myapplication.http.response.SMSResponse;
import com.maoyongxin.myapplication.myapp.AppActivity;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.server.utils.AMUtils;
import com.maoyongxin.myapplication.server.utils.NToast;
import com.maoyongxin.myapplication.server.utils.downtime.DownTimer;
import com.maoyongxin.myapplication.server.utils.downtime.DownTimerListener;
import com.maoyongxin.myapplication.server.widget.ClearWriteEditText;

import butterknife.BindView;


public class ForgetPasswordActivity extends AppActivity {
    @BindView(R.id.forget_phone)
    ClearWriteEditText forgetPhone;
    @BindView(R.id.forget_code)
    ClearWriteEditText forgetCode;
    @BindView(R.id.forget_getcode)
    Button forgetGetcode;
    @BindView(R.id.forget_password)
    ClearWriteEditText forgetPassword;
    @BindView(R.id.forget_password1)
    ClearWriteEditText forgetPassword1;
    @BindView(R.id.forget_button)
    Button forgetButton;
    private String phone;
    private String verCode;

    @Override
    protected int bindLayout() {
        return R.layout.activity_forget_password;
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        forgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!forgetPhone.getText().toString().equals("") || forgetPhone.getText() == null) {
                    if (!forgetCode.getText().toString().equals("") || forgetCode.getText() == null) {
                        if (!forgetPassword.getText().toString().equals("") || forgetPassword.getText() == null) {
                            if (!forgetPassword1.getText().toString().equals("") || forgetPassword1.getText() == null) {
                                if (!forgetPassword1.getText().toString().equals(forgetPassword.getText().toString())) {
                                    if (!forgetCode.getText().toString().equals(verCode)) {

                                    } else {
                                        showToastShort("验证码输入错误，请重试");
                                    }
                                } else {
                                    showToastShort("两次输入密码不一致");
                                }
                            } else {
                                showToastShort("请确认密码");
                            }
                        } else {
                            showToastShort("请输入密码");
                        }
                    } else {
                        showToastShort("请输入验证码");
                    }
                } else {
                    showToastShort("请输入电话号码");
                }
            }
        });
        forgetGetcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(forgetPhone.getText().toString().trim())) {
                    NToast.longToast(ForgetPasswordActivity.this, getString(R.string.phone_number_is_null));
                } else {
                    DownTimer downTimer = new DownTimer();
                    downTimer.setListener(new DownTimerListener() {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            forgetGetcode.setText("seconds:" + String.valueOf(millisUntilFinished / 1000));
                            forgetGetcode.setClickable(false);
                            forgetGetcode.setBackgroundDrawable(getResources().getDrawable(R.drawable.rs_select_btn_gray));
                        }

                        @Override
                        public void onFinish() {
                            forgetGetcode.setText(R.string.get_code);
                            forgetGetcode.setClickable(true);
                            forgetGetcode.setBackgroundDrawable(getResources().getDrawable(R.drawable.rs_select_btn_blue));
                        }
                    });
                    downTimer.startDown(60 * 1000);
                    getSms();
                }
            }
        });
        forgetPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 11) {
                    if (AMUtils.isMobile(s.toString().trim())) {
                        phone = forgetPhone.getText().toString().trim();
                    } else {
                        showToastLong(R.string.Illegal_phone_number);
                    }
                } else {
                    forgetGetcode.setClickable(false);
                    forgetGetcode.setBackgroundDrawable(getResources().getDrawable(R.drawable.rs_select_btn_gray));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        forgetCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 6) {
                    AMUtils.onInactive(ForgetPasswordActivity.this, forgetCode);
                    forgetButton.setClickable(true);
                    forgetButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.rs_select_btn_blue));
                } else {
                    forgetButton.setClickable(false);
                    forgetButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.rs_select_btn_gray));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

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
                    verCode = resp.obj.toString();
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

    private void changePsw() {
        ReqChangePsw.changePsw(this, getActivityTag(), AppApplication.getCurrentUserInfo().getUserId(), forgetPassword.getText().toString(), forgetCode.getText().toString(), new EntityCallBack<ChangePswResponse>() {
            @Override
            public Class<ChangePswResponse> getEntityClass() {
                return ChangePswResponse.class;
            }

            @Override
            public void onSuccess(ChangePswResponse resp) {
                showToastLong(resp.msg);
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
}
