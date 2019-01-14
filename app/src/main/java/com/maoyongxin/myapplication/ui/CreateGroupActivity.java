package com.maoyongxin.myapplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jky.baselibrary.widget.TitleBar;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.request.ReqGroup;
import com.maoyongxin.myapplication.http.response.GroupResponse;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.myapp.AppTitleBarActivity;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CreateGroupActivity extends AppTitleBarActivity {

    @BindView(R.id.tvGrouptype)
    TextView tvGrouptype;
    private TakePictureManager takePictureManager;
    private String tupianUrl = "";
    private String uploadurl = "";
    private String serverURl = "";
    private List<String> name_lis = new ArrayList<String>();

    private List<String> id_list = new ArrayList<String>();
    @BindView(R.id.TitleBar_like)
    TitleBar TitleBarLike;
    @BindView(R.id.edit_groupName)
    EditText editGroupName;
    @BindView(R.id.edit_group_content)
    EditText editGroupContent;
    @BindView(R.id.btn_create_group)
    Button btnCreateGroup;
    @BindView(R.id.activity_create_group)
    LinearLayout activityCreateGroup;
    @BindView(R.id.group_img)
    ImageView groupImg;


    private int UPDATE = 1;


    private String groupType;

    private String groupname;
    private String grouptyname;


    @Override
    protected void initData() {
        super.initData();
        tupianUrl = getIntent().getStringExtra("groupImg");
        groupname = getIntent().getStringExtra("groupname");
        groupType = getIntent().getStringExtra("groupClassifyId");
        grouptyname=getIntent().getStringExtra("groupClassiname");

    }

    @Override
    protected void initView() {
        super.initView();

        Glide.with(getActivity()).load(tupianUrl).into(groupImg);
        editGroupName.setText(groupname);
        tvGrouptype.setText(grouptyname);
    }

    @Override
    protected void handlerPassMsg(int target, int target1, Object obj) {

    }

    @Override
    protected void doBusiness() {
        super.doBusiness();


        btnCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editGroupContent.getText().toString() != null && !editGroupContent.getText().toString().equals("")) {
                    if (editGroupName.getText().toString() != null && !editGroupName.getText().toString().equals("")) {
                        createGroup();
                    } else {
                        showToastShort("请输入群名称");
                    }
                } else {
                    showToastShort("请输入群备注");
                }
            }
        });
    }

    private void createGroup() {
        ReqGroup.CreateGroup(this, getActivityTag(), AppApplication.getCurrentUserInfo().getUserId(), editGroupName.getText().toString(), editGroupContent.getText().toString(), new EntityCallBack<GroupResponse>() {
            @Override
            public Class<GroupResponse> getEntityClass() {
                return GroupResponse.class;
            }

            @Override
            public void onSuccess(GroupResponse resp) {
                if (resp.is200()) {
                    showToastShort(resp.msg);
                    threadPost();

                } else {
                    showToastShort(resp.msg);
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
    private void threadPost() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                uploadMultiFile(tupianUrl);
            }
        }).start();
    }

    private void uploadMultiFile(String img_url) {
        String url = "http://st.3dgogo.com/order_tracking/uploads/photos.html";
        String originalPath = img_url;//图片路径


        ImageGetter.doCompressBySize(originalPath, originalPath);
        File file = new File(originalPath);
        final OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
        OkHttpClient okHttpClient = httpBuilder
                //设置超时
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();
        Map<String, String> maps = new ArrayMap<>();
        maps.put("val", "order_tracking_photos");//参数
        okHttpClient.newCall(getFileRequest(url, file, maps)).enqueue(new Callback() {


            @Override
            public void onFailure(Call call, IOException e) {
                //Log.e("aa", "uploadMultiFile() e=" + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String tempResponse = response.body().string();
                gettupianurl(tempResponse);


            }

        });

    }

    private void gettupianurl(String companyName) {


        try {

            JSONObject jsonObject = new JSONObject(companyName);

            uploadurl = jsonObject.getString("url").toString();
            postDatetoserver( editGroupName.getText().toString(),uploadurl,groupType,editGroupContent.getText().toString());

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    private void postDatetoserver(String gpName,String url,String gpType,String gpNote) {
        final JSONObject order = new JSONObject();
        OkHttpClient okHttpClient = new OkHttpClient();

        try {

            RequestBody requestBody = new FormBody.Builder()
                    .add("group_name",gpName)
                    .add("image", url)
                    .add("groupClassifyId",gpType)
                    .add("groupNote",gpNote)
                    .build();

            Request request = new Request.Builder()
                    .url("http://st.3dgogo.com/index/chatgroup_gambit/set_chatgroup_image.html")
                    .post(requestBody)
                    .build();
            try {
                Call call = okHttpClient.newCall(request);//判断请求是否成功
                try {
                    Response response = call.execute();
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        if (jsonObject.getInt("code") == 200) {
                            finish();

                        } else if (jsonObject.getInt("code") == 500) {


                        }
                    } catch (Exception e) {
                        e.printStackTrace();


                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } catch (Exception e) {
                e.printStackTrace();
                //    showMessagefail("登陆失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }




    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        takePictureManager.attachToActivityForResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        takePictureManager.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    public static Request getFileRequest(String url, File file, Map<String, String> maps) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (maps == null) {
            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"file\";filename=\"file.jpg\""), RequestBody.create(MediaType.parse("image/*"), file)
            ).build();
        } else {
            for (String key : maps.keySet()) {
                builder.addFormDataPart(key, maps.get(key));
            }
            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"file\";filename=\"file.jpg\""), RequestBody.create(MediaType.parse("image/*"), file)
            );
        }
        RequestBody body = builder.build();
        return new Request.Builder().url(url).post(body).build();

    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_create_group;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);


    }


    private String parse_Value(JSONObject data, String value) {
        String com_value = "";
        if (data.has(value)) {
            try {
                com_value = data.getString(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return com_value;
    }


}
