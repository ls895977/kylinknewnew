package com.maoyongxin.myapplication.ui.editapp.minefragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.ui.ImageGetter;
import com.maoyongxin.myapplication.ui.News_web;
import com.maoyongxin.myapplication.ui.TakePictureManager;
import com.maoyongxin.myapplication.ui.micro_web;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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

import static android.view.View.VISIBLE;

public class xiangqingye extends AppCompatActivity {
    private String shangjiaInfo,myCommunityId;
    private JSONObject shInfo;
    private String headImg,showImg,cpnName,cpnAdress,detail,id;
    private TextView companyAdress,tc_button;
    private EditText hangye_detail,compnayName;
    private Handler handler;
    final private int UPDATE=0;
    final private int REDEFINE=1;
    final private int  UPDATESUCCESS=2;
    private ImageView mainImg;
    private String Intro;
    private CardView toDetail;
    ImageButton redefine,img_redefine;
    private Boolean companyted=true;
    private TakePictureManager PictureManager;
    private Drawable d;
    private String headUrl = "";
    private String serverImgUrl="";
    private Boolean updateSuccess=false,sendedData=false;
    private String webapi="http://st.3dgogo.com/index/enterprise_publicity/get_community_id_details_url_api.html?community_id=";
    private String shangjiaId,classfyid;
    private ImageView img_userhead;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiangqingye);
        img_userhead=(ImageView)findViewById(R.id.img_userhead);
        mainImg=(ImageView)findViewById(R.id.showImg);
        compnayName=(EditText)findViewById(R.id.company_name);
        redefine=(ImageButton)findViewById(R.id.redefine);
        companyAdress=(TextView)findViewById(R.id.company_adress);
        hangye_detail=(EditText)findViewById(R.id.hangye_detail);
        img_redefine=(ImageButton)findViewById(R.id.imb_redefine);
        tc_button=(TextView)findViewById(R.id.tc_button);
        shangjiaInfo=getIntent().getStringExtra("shangjiaInfo");
        myCommunityId=getIntent().getStringExtra("myCommunityId");

        shangjiaId=getIntent().getStringExtra("shangjiaId");
        classfyid=getIntent().getStringExtra("classfyid");

        getShangjiaInfo(shangjiaInfo);
        toDetail=(CardView)findViewById(R.id.toDetail);
        toDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(companyted){
                    Intent intent=new Intent(xiangqingye.this,micro_web.class);
                    intent.putExtra("weburl",webapi+myCommunityId);
                    intent.putExtra("targetUserId",shangjiaId);
                    intent.putExtra("communityId",myCommunityId);
                    startActivity(intent);
                }

                else {
                    threaddatatoserver();
                      }

            }
        });
        redefine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message msg=new Message();
                msg.what=REDEFINE;
                handler.sendMessage(msg);
            }
        });
        handler=new Handler(){

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==UPDATE)
                {
                    compnayName.setText(cpnName+"");
                    hangye_detail.setText(Intro+"");

                    Glide.with(xiangqingye.this).load("http://st.3dgogo.com/"+showImg).into(mainImg);
                    Glide.with(xiangqingye.this).load(AppApplication.getCurrentUserInfo().getHeadImg()).into(img_userhead);

                }
                else if(msg.what==REDEFINE)
                {
                    compnayName.setBackgroundColor(getResources().getColor(R.color.text_hint));
                    hangye_detail.setBackgroundColor(getResources().getColor(R.color.text_hint));
                    toDetail.setCardBackgroundColor(getResources().getColor(R.color.blue_tiny));
                    img_redefine.setVisibility(VISIBLE);
                    companyted=false;
                    tc_button.setText("保存");

                }
                else if(msg.what==UPDATESUCCESS)
                {
                    compnayName.setBackgroundColor(getResources().getColor(R.color.tranprent));
                    hangye_detail.setBackgroundColor(getResources().getColor(R.color.tranprent));
                    toDetail.setCardBackgroundColor(getResources().getColor(R.color.rc_draft_color));
                    img_redefine.setVisibility(View.INVISIBLE);
                    companyted=true;
                    tc_button.setText("保存");

                }
            }
        };

        img_redefine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActionSheetDialog();
            }
        });







    }

    private void ActionSheetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)

                .setTitle("选择图片：")
                //设置两个item
                .setItems(new String[]{"相机", "图库"}, new android.content.DialogInterface.OnClickListener() {

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
    private void new_take()
    {
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

                Glide.with(xiangqingye.this).load(outFile).error(R.mipmap.ic_launcher).into(mainImg);
                d= Drawable.createFromPath(outFile.toString());
                // company_backgroud.setBackground(d);
                headUrl=outFile.toString();
            }

            //失败回调
            @Override
            public void failed(int errorCode, List<String> deniedPermissions) {
                //Log.e("==w",deniedPermissions.toString());
            }
        });
    }
    private void album() {
        PictureManager = new TakePictureManager(this);

        PictureManager.startTakeWayByAlbum();
        PictureManager.setComptressor(false);
        PictureManager.setTakePictureCallBackListener(new TakePictureManager.takePictureCallBackListener() {
            @Override
            public void successful(boolean isTailor, File outFile, Uri filePath) {

                Glide.with(xiangqingye.this).load(outFile).error(R.mipmap.ic_launcher).into(mainImg);
                d=Drawable.createFromPath(outFile.toString());
                // company_backgroud.setBackground(d);
                headUrl=outFile.toString();

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
        if( img_url.equals("")||img_url==null)
        {
            serverImgUrl=showImg;
            updateUserInfo();
        }
        else
        {
            String url = "http://st.3dgogo.com/order_tracking/uploads/photos.html";
            String originalPath =img_url;//图片路径

            ImageGetter.doCompressBySize(originalPath, originalPath);
            File file = new File(originalPath);
            final okhttp3.OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
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
                    String tempResponse = response.body().string();//获取服务器返回的图片地址

                    try{

                        serverImgUrl=new JSONObject(tempResponse).getString("url");
                        if(new JSONObject(tempResponse).getBoolean("code"))
                        {
                            updateUserInfo();
                        }

                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    //  gettupianurl(tempResponse);//反馈图片地址到服务器；


                }

            });
        }



    }


    private void getShangjiaInfo(String Info)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                    shInfo=new JSONObject(shangjiaInfo);
                    cpnName=shInfo.getJSONObject("info").getString("name");
                    Intro=shInfo.getJSONObject("info").getString("intro");
                    headImg=shInfo.getJSONObject("info").getString("head_portrait");
                    detail=shInfo.getJSONObject("info").getString("details");
                    showImg=shInfo.getJSONObject("info").getString("publicity_img");
                    id=shInfo.getJSONObject("info").getString("id");


                    Message msg=new Message();
                    msg.what=UPDATE;
                    handler.sendMessage(msg);

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        }).start();

    }

    private void threaddatatoserver() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                uploadMultiFile(headUrl);

            }
        }).start();

    }
    private void updateUserInfo(){


        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();
                try {

                    RequestBody requestBody = new FormBody.Builder()
                            .add("id", id)
                            .add("name", compnayName.getText().toString())
                            .add("community_id", myCommunityId)
                            .add("publicity_img", serverImgUrl)
                            .add("last_operator", AppApplication.getCurrentUserInfo().getUserId())
                            .build();
                    Request request = new Request.Builder()
                            .url("http://st.3dgogo.com/index/enterprise_info/update_enterprise_publicity_details_api.html")
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

                                    Message msg=new Message();
                                    msg.what=UPDATESUCCESS;
                                    handler.sendMessage(msg);

                                    Toast.makeText(xiangqingye.this, "恭喜上传成功", Toast.LENGTH_SHORT).show();
                                } else if (jsonObject.getInt("code") == 500) {

                                    Toast.makeText(xiangqingye.this, "抱歉，修改失败！请再次提交", Toast.LENGTH_SHORT).show();
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
