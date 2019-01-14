package com.maoyongxin.myapplication.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.ui.bean.EventBusCarrier;
import com.maoyongxin.myapplication.ui.editapp.findfragment.zhihu.bean.ZhiHuGridBean;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;

import okhttp3.Call;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final int RETURN_MSG_TYPE_LOGIN = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //如果没回调onResp，八成是这句没有写
        AppApplication.mWxApi.handleIntent(getIntent(), this);
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    //app发送消息给微信，处理返回消息的回调
    @Override
    public void onResp(BaseResp resp) {
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                break;
            case BaseResp.ErrCode.ERR_OK:
                switch (resp.getType()) {
                    case RETURN_MSG_TYPE_LOGIN:
                        //拿到了微信返回的code,立马再去请求access_token
                        String code = ((SendAuth.Resp) resp).code;
                        //就在这个地方，用网络库什么的或者自己封的网络api，发请求去咯，注意是get请求
                        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                                "appid=" + AppApplication.WEIXIN_APP_ID +
                                "&secret=b67d75c11e4255772717a89b175c0647" +
                                "&code=" + code +
                                "&grant_type=authorization_code";
                        postAccess_token(url);
                        break;
                }
                break;
        }
    }

    Gson gson = new Gson();

    public void postAccess_token(String url) {
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(String response, int id) {
                AccessTokenBean bean = gson.fromJson(response, AccessTokenBean.class);
                String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + bean.getAccess_token() + "&openid=" + bean.getOpenid();
                postUserBean(url, bean.getOpenid());
            }
        });
    }

    public void postUserBean(String url, final String openid) {
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(String response, int id) {
                WXUserBean userBean = gson.fromJson(response, WXUserBean.class);
                EventBusCarrier eventBusCarrier = new EventBusCarrier();
                eventBusCarrier.setOpenid(openid);
                eventBusCarrier.setUserBean(userBean);
                EventBus.getDefault().post(eventBusCarrier); //普通事件发布
                finish();
            }
        });
    }
}