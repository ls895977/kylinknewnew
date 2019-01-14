package com.maoyongxin.myapplication.ui;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.maoyongxin.myapplication.R;

import com.maoyongxin.myapplication.entity.grideInfo;

import com.maoyongxin.myapplication.ui.adapter.groupType_gride_adapter;

import org.json.JSONArray;
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
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.maoyongxin.myapplication.server.utils.NToast.showToast;


public class CreateGroupSteopOne extends AppCompatActivity {

    @BindView(R.id.groupimg)
    ImageView groupImg;
    @BindView(R.id.editgroupName)
    EditText editGroupName;

    @BindView(R.id.newxtStep)
    Button newxtStep;
    @BindView(R.id.groupTypegride)
    GridView groupTypegride;

    private TakePictureManager takePictureManager;
    private String tupianUrl = "";
    private String uploadurl = "";
    private String groupName, groupId,groupimg;
    private int UPDATE = 1;
    private Handler handler;
    private List<grideInfo> hangyelists = new ArrayList<>();

    private String grouptypeId="";
    private groupType_gride_adapter fwgrideAdapter;
    private String grouptyname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group_steop_one);
        ButterKnife.bind(this);
        getgroupttypeList();

        groupImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActionSheetDialog();
            }
        });
        newxtStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tupianUrl.equals(""))
                {
                  Toast.makeText(CreateGroupSteopOne.this,"请选个图片先",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(grouptypeId.equals(""))
                    {
                        Toast.makeText(CreateGroupSteopOne.this,"请选择商会分类",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        if(editGroupName.getText().equals("")||editGroupName.getText()==null)
                        {
                            Toast.makeText(CreateGroupSteopOne.this,"名字莫搞忘了",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Intent intent = new Intent(CreateGroupSteopOne.this, CreateGroupActivity.class);
                            intent.putExtra("groupImg", tupianUrl);
                            intent.putExtra("groupClassifyId", grouptypeId);
                            intent.putExtra("groupClassiname", grouptyname);
                            intent.putExtra("groupname",editGroupName.getText().toString());
                            startActivity(intent);

                        }

                    }
                }

            }
        });


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                if (msg.what == UPDATE) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                   fwgrideAdapter=new groupType_gride_adapter(CreateGroupSteopOne.this,hangyelists);
                     groupTypegride.setAdapter(fwgrideAdapter);
                        }
                    },10);
                }
            }
        };

        groupTypegride.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final TextView tv_id = (TextView) view.findViewById(R.id.gridId);
                final TextView tv_name=(TextView)view.findViewById(R.id.fuwuname);
                fwgrideAdapter.setCurrentItem(i);
                //通知ListView改变状态
                fwgrideAdapter.notifyDataSetChanged();
                grouptypeId=tv_id.getText()+"";
                grouptyname=tv_name.getText().toString();
            }
        });
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
              //  Log.e("aa", "uploadMultiFile() e=" + e);
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
            // postDatetoserver(uploadurl);

        } catch (Exception e) {
            e.printStackTrace();
        }


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
                                //  take_photo();
                                new_take();
                                break;
                            case 1:
                                album();
                                // pickPicFromPic();
                                break;
                            default:
                                break;
                        }

                    }
                });
        builder.create().show();
    }

    private void new_take() {
        takePictureManager = new TakePictureManager(this);
        //开启裁剪 比例 1:3 宽高 350 350  (默认不裁剪)
        takePictureManager.setTailor(3, 2, 600, 400);
        //拍照方式
        takePictureManager.startTakeWayByCarema();
        //回调
        takePictureManager.setTakePictureCallBackListener(new TakePictureManager.takePictureCallBackListener() {
            //成功拿到图片,isTailor 是否裁剪？ ,outFile 拿到的文件 ,filePath拿到的URl
            @Override
            public void successful(boolean isTailor, File outFile, Uri filePath) {

                Glide.with(CreateGroupSteopOne.this).load(outFile).error(R.mipmap.ic_launcher).into(groupImg);
                tupianUrl = outFile.toString();


            }

            //失败回调
            @Override
            public void failed(int errorCode, List<String> deniedPermissions) {
                //Log.e("==w", deniedPermissions.toString());
            }
        });
    }

    private void album() {
        takePictureManager = new TakePictureManager(this);
        takePictureManager.setTailor(3, 2, 600, 400);
        takePictureManager.startTakeWayByAlbum();
        takePictureManager.setTakePictureCallBackListener(new TakePictureManager.takePictureCallBackListener() {
            @Override
            public void successful(boolean isTailor, File outFile, Uri filePath) {

                Glide.with(CreateGroupSteopOne.this).load(outFile).error(R.mipmap.ic_launcher).into(groupImg);
                tupianUrl = outFile.toString();

            }

            @Override
            public void failed(int errorCode, List<String> deniedPermissions) {

            }

        });
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

    private void getgroupttypeList() {
        final String grouptypeList = "http://st.3dgogo.com/index/chatgroup_gambit/get_chatgroup_classify";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(grouptypeList).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();

                    try {

                        JSONObject jsonObject = new JSONObject(responseData);

                        JSONArray data = jsonObject.getJSONArray("info");


                        for (int i = 0; i < data.length(); i++) {


                            JSONObject newList = data.getJSONObject(i);
                            grideInfo datas = new grideInfo();
                            groupName = parse_Value(newList, "group_classify_name");
                            groupId = parse_Value(newList, "group_classify_id");
                            groupimg=parse_Value(newList, "image");
                            datas.setfuwuInfo(groupName, groupimg,groupId);
                            hangyelists.add(datas);



                        }

                        Message msg = new Message();
                        msg.what = UPDATE;
                        handler.sendMessage(msg);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    //  showMessagefail("解析失败");
                }
            }
        }).start();
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
