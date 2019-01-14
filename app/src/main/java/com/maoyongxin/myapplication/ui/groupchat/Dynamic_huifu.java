package com.maoyongxin.myapplication.ui.groupchat;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.myapp.AppActivity;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.rockerhieu.emojicon.EmojiconEditText;
import io.github.rockerhieu.emojicon.EmojiconGridFragment;
import io.github.rockerhieu.emojicon.EmojiconsFragment;
import io.github.rockerhieu.emojicon.emoji.Emojicon;
import okhttp3.Call;

public class Dynamic_huifu extends AppActivity implements EmojiconsFragment.OnEmojiconBackspaceClickedListener, EmojiconGridFragment.OnEmojiconClickedListener{

    @BindView(R.id.Ib_Dynamic)
    ImageButton IbDynamic;
    @BindView(R.id.ed_Dynamic_detail)
    EditText edDynamicDetail;
    @BindView(R.id.tv_parent)
    TextView tvParent;

    @BindView(R.id.ed_huifu_d)
    EmojiconEditText ed_huifu_d;


    @BindView(R.id.moj_view)
    FrameLayout moj_view;

    @BindView(R.id.moj_btn)
    RelativeLayout moj_btn;
    @Override
    protected int bindLayout() {
        return (R.layout.activity_dynamic_huifu);
    }

    private String dynamicId, uid, uName, panrentId, parentUserId, toUser, personId, CommunityId, parentName, parentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        IbDynamic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setIbQuerenhuifu();
            }
        });
        moj_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(moj_view.getVisibility()==View.GONE){
                    moj_view.setVisibility(View.VISIBLE);
                }else {
                    moj_view.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void initData() {


        parentUserId = getIntent().getStringExtra("parentUserId");
        dynamicId = getIntent().getStringExtra("DynamicId");
        CommunityId = getIntent().getStringExtra("CommunityId");
        parentId = getIntent().getStringExtra("parentId");
        uid = AppApplication.getCurrentUserInfo().getUserId() + "";
        uName = AppApplication.getCurrentUserInfo().getUserName() + "";

    }

    @Override
    protected void initView() {

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//白底黑字+沉浸式导航栏
        tvParent.setText(getIntent().getStringExtra("parentName"));
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.moj_view, EmojiconsFragment.newInstance(false))
                .commit();
    }

    private void setIbQuerenhuifu() {//标准接口请求
        String s = ed_huifu_d.getText().toString();
        String strBase64 = Base64.encodeToString(s.getBytes(), Base64.DEFAULT);
        OkHttpUtils.post().url("http://st.3dgogo.com/index/user_dynamic/set_user_dynamic_post.html")
                .addParams("postContent", strBase64)//内容
                .addParams("dynamicId", dynamicId)//动态id
                .addParams("userId", uid)//回复者id
                .addParams("parentId", parentId)
                .addParams("parentUserId", parentUserId)
                .addParams("communityId", CommunityId)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                try {

                    Toast.makeText(Dynamic_huifu.this, "回复成功", Toast.LENGTH_SHORT).show();
                    finish();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(Dynamic_huifu.this, "回复失败", Toast.LENGTH_SHORT).show();
                }


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
}
