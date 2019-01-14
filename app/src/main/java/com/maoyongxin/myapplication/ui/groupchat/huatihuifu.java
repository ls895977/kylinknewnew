package com.maoyongxin.myapplication.ui.groupchat;

import android.app.Activity;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.myapp.AppActivity;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.ui.EventMessage;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.rockerhieu.emojicon.EmojiconEditText;
import io.github.rockerhieu.emojicon.EmojiconGridFragment;
import io.github.rockerhieu.emojicon.EmojiconsFragment;
import io.github.rockerhieu.emojicon.emoji.Emojicon;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class huatihuifu extends AppActivity implements EmojiconsFragment.OnEmojiconBackspaceClickedListener, EmojiconGridFragment.OnEmojiconClickedListener {

    @BindView(R.id.ed_huifu_detail)
    EditText edHuifuDetail;
    @BindView(R.id.Ib_querenhuifu)
    ImageButton IbQuerenhuifu;

    @BindView(R.id.moj_btn)
    RelativeLayout moj_btn;

    @BindView(R.id.moj_view)
    FrameLayout moj_view;

    @BindView(R.id.ed_huifu_d)
    EmojiconEditText ed_huifu_d;

    @Override
    protected int bindLayout() {
        return (R.layout.activity_huatihuifu);
    }

    private String userid;
    private String gamit_id;
    private String huifuUser;
    private String groupId;
    private String parentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//白底黑字+沉浸式导航栏
        userid = AppApplication.getCurrentUserInfo().getUserId();
        IbQuerenhuifu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edHuifuDetail.getText().equals("") || edHuifuDetail.getText() == null) {
                    Toast.makeText(huatihuifu.this, "回复不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    setIbQuerenhuifu();
                }
            }
        });

    }

    @Override
    protected void initData() {

//        ed_huifu_d.setVisibility(View.VISIBLE);
//        edHuifuDetail.setVisibility(View.GONE);

        gamit_id = getIntent().getStringExtra("gambit_id");
        huifuUser = getIntent().getStringExtra("username");
        groupId = getIntent().getStringExtra("groupId");
        parentUserId = getIntent().getStringExtra("parentUserId");

    }

    @Override
    protected void initView() {

        edHuifuDetail.setText(huifuUser + "");
        moj_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (moj_view.getVisibility() == View.GONE) {
                    moj_view.setVisibility(View.VISIBLE);
                } else {
                    moj_view.setVisibility(View.GONE);
                }
            }
        });
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.moj_view, EmojiconsFragment.newInstance(false))
                .commit();
    }

    private void setIbQuerenhuifu() {
        String s = ed_huifu_d.getText().toString();
        String str = Base64.encodeToString(s.getBytes(), Base64.DEFAULT);
        OkHttpUtils.post().url("http://st.3dgogo.com/index/chatgroup_gambit/set_respond")
                .addParams("gambit_id", gamit_id)
                .addParams("group_id", groupId)
                .addParams("uid", userid)
                .addParams("parent_id", parentUserId)
                .addParams("parentUserId", parentUserId)
                .addParams("content", str)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(String response, int id) {
                Toast.makeText(huatihuifu.this, "回复成功", Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post(new EventMessage<String>(6, ""));
                finish();
            }
        });
    }

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        EmojiconsFragment.input(ed_huifu_d, emojicon);
    }

    @Override
    public void onEmojiconBackspaceClicked(View v) {
        EmojiconsFragment.backspace(ed_huifu_d);
    }
/*
    private void setIbQuerenhuifu() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();
                try {

                    RequestBody requestBody = new FormBody.Builder()
                            .add("gambit_id", gamit_id)
                            .add("group_id", groupId)
                            .add("uid", userid)
                            .add("parent_id","10070")
                            .add("content", edHuifuDetail.getText().toString())
                            .build();

                    Request request = new Request.Builder()
                            .url("http://st.3dgogo.com/index/chatgroup_gambit/set_respond")
                            .post(requestBody)
                            .build();


                    try {
                        Call call = okHttpClient.newCall(request);
                        //判断请求是否成功
                        Looper.prepare();
                        try {
                            Response response = call.execute();

                            try {

                                JSONObject jsonObject = new JSONObject(response.body().string());

                                if (jsonObject.getInt("code") == 200) {
                                    Toast.makeText(huatihuifu.this,"回复成功",Toast.LENGTH_SHORT).show();
                                    finish();
                                } else if (jsonObject.getInt("code") == 500) {

                                    Toast.makeText(huatihuifu.this,"回复失败",Toast.LENGTH_SHORT).show();
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
    */
}
