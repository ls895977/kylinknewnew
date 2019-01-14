package com.maoyongxin.myapplication.ui.qq;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.maoyongxin.myapplication.ui.LoginActivity;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import static cn.jiguang.share.android.api.AbsPlatform.getApplicationContext;
import static com.chad.library.adapter.base.listener.SimpleClickListener.TAG;

public class BaseUiListener implements IUiListener {
    Tencent mTencent;
    private Context context;
    BackQQData backQQData;

    public BaseUiListener(Tencent mTencent1, Context context1, BackQQData backQQData1) {
        this.mTencent = mTencent1;
        this.context = context1;
        this.backQQData = backQQData1;
    }

    private Gson gson;

    @Override
    public void onComplete(Object response) {
        Log.e(TAG, "response:" + response);
        gson = new Gson();
        JSONObject obj = (JSONObject) response;
        try {
            final String openID = obj.getString("openid");
            String accessToken = obj.getString("access_token");
            String expires = obj.getString("expires_in");
            mTencent.setOpenId(openID);
            mTencent.setAccessToken(accessToken, expires);
            QQToken qqToken = mTencent.getQQToken();
            UserInfo mUserInfo = new UserInfo(getApplicationContext(), qqToken);
            mUserInfo.getUserInfo(new IUiListener() {
                @Override
                public void onComplete(Object response) {
                    Toast.makeText(context, "登录成功", Toast.LENGTH_SHORT).show();
                    //是一个json串response.tostring，直接使用gson解析就好
                    Log.e(TAG, "登录成功" + response.toString());
                    //登录成功后进行Gson解析即可获得你需要的QQ头像和昵称
                    // Nickname  昵称
                    //Figureurl_qq_1 //头像
                    QQUserBean bean = gson.fromJson(response.toString(), QQUserBean.class);
                    backQQData.backQQData(bean, openID);
                }

                @Override
                public void onError(UiError uiError) {
                    Toast.makeText(context, "登录失败", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "登录失败" + uiError.toString());
                }

                @Override
                public void onCancel() {
                    Toast.makeText(context, "登录取消", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "登录取消");
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(UiError e) {
        Log.e("aa", "------onError--" + e.errorMessage);
    }

    @Override
    public void onCancel() {
        Log.e("aa", "------onCancel--");
    }

    public interface BackQQData {
        void backQQData(QQUserBean qqUserBean, String UserId);
    }
}