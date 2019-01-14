package com.maoyongxin.myapplication.ui;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.request.ReqDealRequest;
import com.maoyongxin.myapplication.http.response.BaseResp;
import com.maoyongxin.myapplication.myapp.AppActivity;
import com.maoyongxin.myapplication.myapp.AppApplication;

import butterknife.BindView;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.InformationNotificationMessage;
import io.rong.message.TextMessage;

public class RecieverFriendsPushActivity extends AppActivity {

    @BindView(R.id.tv_request_title)
    TextView tvRequestTitle;
    @BindView(R.id.tv_request_data)
    TextView tvRequestData;
    @BindView(R.id.btn_yes)
    Button btnYes;
    @BindView(R.id.btn_no)
    Button btnNo;
    @BindView(R.id.activity_reciever_friends_push)
    LinearLayout activityRecieverFriendsPush;
    private String requestId;
    private String state;
    @Override
    protected int bindLayout() {
        return R.layout.activity_reciever_friends_push;
    }

    @Override
    protected void initData() {
        super.initData();
        requestId=getIntent().getStringExtra("requestId");
    }

    @Override
    protected void initView() {
        super.initView();
        tvRequestTitle.setText(getIntent().getStringExtra("userId")+"请求添加您为好友");
        tvRequestData.setText(getIntent().getStringExtra("data"));

    }

    @Override
    protected void initEvent() {
        super.initEvent();

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state=2+"";
                dealRequest(state);
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state=3+"";
                dealRequest(state);
            }
        });
    }

    private void dealRequest(final String state) {
        ReqDealRequest.dealRequest(RecieverFriendsPushActivity.this, getActivityTag(), AppApplication.getCurrentUserInfo().getUserId(), requestId, state, new EntityCallBack<BaseResp>() {
            @Override
            public Class<BaseResp> getEntityClass() {
                return BaseResp.class;
            }

            @Override
            public void onSuccess(BaseResp resp) {
                if(resp.is200()){
                    if(state.equals("2")){
                        showToastShort("成功添加对方为好友，你们现在可以聊天啦");
                    }else{
                        showToastShort("您已拒绝");
                    }
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
}
