package com.maoyongxin.myapplication.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jky.baselibrary.widget.TitleBar;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.myapp.AppActivity;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
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

public class group_headEidt extends AppActivity {
    @BindView(R.id.TitleBar_group)
    TitleBar TitleBarGroup;
    @BindView(R.id.groupPic)
    ImageView groupPic;
    @BindView(R.id.add_pics)
    ImageView addPics;
    @BindView(R.id.btn_save)
    TextView btnSave;
    @BindView(R.id.pg_update)
    ProgressBar pgUpdate;
    private Drawable d;
    private String headUrl = "";
    private TakePictureManager PictureManager;
    private String serverImgUrl = "", showImg, groupId;
    final private int UPDATE = 0;
    final private int REDEFINE = 1;
    final private int UPDATESUCCESS = 2;
    private Handler handler;



    @Override
    protected int bindLayout() {
        return R.layout.activity_group_head_eidt;
    }

    @Override
    protected void initData() {
        groupId = getIntent().getStringExtra("groupId");
        showImg = getIntent().getStringExtra("showImg");
    }

    @Override
    protected void initView() {


        Glide.with(getActivity()).load(showImg).into(groupPic);

    }

    @Override
    protected void initEvent() {

        addPics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ActionSheetDialog();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message msg = new Message();
                msg.what = REDEFINE;
                handler.sendMessage(msg);

                uploadMultiFile(headUrl);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        //setContentView(R.layout.activity_group_head_eidt);
        ButterKnife.bind(this);
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==REDEFINE)
                {
                    pgUpdate.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(),"开始上传头像",Toast.LENGTH_SHORT).show();
                }
                if(msg.what==UPDATESUCCESS)
                {
                    pgUpdate.setVisibility(View.GONE);
                    Toast.makeText(getActivity(),"头像上传成功",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        };
    }

    private void ActionSheetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)

                .setTitle("选择图片：")
                //设置两个item
                .setItems(new String[]{"相机", "图库"}, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:

                                new_take();
                                break;
                            case 1:
                                album();

                                break;
                            default:
                                break;
                        }

                    }
                });
        builder.create().show();
    }

    private void new_take() {
        PictureManager = new TakePictureManager(this);
        //开启裁剪 比例 1:3 宽高 350 350  (默认不裁剪)
        //PictureManager.setTailor(1, 1, 350, 350);
        //拍照方式

        PictureManager.startTakeWayByCarema();
        //回调
        PictureManager.setTakePictureCallBackListener(new TakePictureManager.takePictureCallBackListener() {
            //成功拿到图片,isTailor 是否裁剪？ ,outFile 拿到的文件 ,filePath拿到的URl
            @Override
            public void successful(boolean isTailor, File outFile, Uri filePath) {

                Glide.with(group_headEidt.this).load(outFile).error(R.mipmap.ic_launcher).into(groupPic);
                d = Drawable.createFromPath(outFile.toString());
                // company_backgroud.setBackground(d);
                headUrl = outFile.toString();
            }

            //失败回调
            @Override
            public void failed(int errorCode, List<String> deniedPermissions) {
                Log.e("==w", deniedPermissions.toString());
            }
        });
    }

    private void album() {
        PictureManager = new TakePictureManager(this);
        //  PictureManager.setTailor(1, 1, 350, 350);
        PictureManager.startTakeWayByAlbum();
        PictureManager.setComptressor(false);
        PictureManager.setTakePictureCallBackListener(new TakePictureManager.takePictureCallBackListener() {
            @Override
            public void successful(boolean isTailor, File outFile, Uri filePath) {

                Glide.with(group_headEidt.this).load(outFile).error(R.mipmap.ic_launcher).into(groupPic);
                d = Drawable.createFromPath(outFile.toString());
                // company_backgroud.setBackground(d);
                headUrl = outFile.toString();

            }

            @Override
            public void failed(int errorCode, List<String> deniedPermissions) {

            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PictureManager.attachToActivityForResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PictureManager.onRequestPermissionsResult(requestCode, permissions, grantResults);

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

    private void uploadMultiFile(String img_url) {
        if (img_url.equals("") || img_url == null) {
            serverImgUrl = showImg;
            updateUserInfo();
        } else {
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
                    Log.e("aa", "uploadMultiFile() e=" + e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String tempResponse = response.body().string();//获取服务器返回的图片地址

                    try {

                        serverImgUrl = new JSONObject(tempResponse).getString("url");
                        if (new JSONObject(tempResponse).getBoolean("code")) {
                            updateUserInfo();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

            });
        }


    }

    private void updateUserInfo() {


        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();
                try {

                    RequestBody requestBody = new FormBody.Builder()
                            .add("groupId", groupId)
                            .add("image", serverImgUrl)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://st.3dgogo.com/index/chatgroup/update_chatgroup_info_api.html")
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

                                    Message msg = new Message();
                                    msg.what = UPDATESUCCESS;
                                    handler.sendMessage(msg);

                                    Toast.makeText(group_headEidt.this, "恭喜上传成功", Toast.LENGTH_SHORT).show();
                                } else if (jsonObject.getInt("code") == 500) {

                                    Toast.makeText(group_headEidt.this, "抱歉，修改失败！请再次提交", Toast.LENGTH_SHORT).show();
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

}
