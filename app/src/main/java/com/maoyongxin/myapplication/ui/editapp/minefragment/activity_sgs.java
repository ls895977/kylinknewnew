package com.maoyongxin.myapplication.ui.editapp.minefragment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.myapp.AppActivity;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class activity_sgs extends AppCompatActivity {

    EditText et_content;
    Button bt_sendmsg;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sgs);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//白底黑字+沉浸式导航栏


        et_content=(EditText)findViewById(R.id.et_content);
        bt_sendmsg=(Button)findViewById(R.id.bt_Sendmessage);

        bt_sendmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendmsgtoserver();
            }
        });
    }





    private void sendmsgtoserver() {
        OkHttpUtils.post().url("http://st.3dgogo.com/index/couple_back/setCoupleBackApi.html")
                .addParams("uid", AppApplication.getCurrentUserInfo().getUserId())
                .addParams("content", et_content.getText().toString())
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(activity_sgs.this,"老铁，谢谢你的反馈",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                Toast.makeText(activity_sgs.this,"老铁，谢谢你的反馈",Toast.LENGTH_SHORT).show();
            }
        });


    }
}
