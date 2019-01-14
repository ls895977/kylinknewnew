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
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.FuwuList;
import com.maoyongxin.myapplication.entity.grideInfo;
import com.maoyongxin.myapplication.entity.yellowPage_info;
import com.maoyongxin.myapplication.myapp.AppActivity;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.server.widget.SelectableRoundedImageView;
import com.maoyongxin.myapplication.ui.CreateGroupActivity;
import com.maoyongxin.myapplication.ui.ImageGetter;
import com.maoyongxin.myapplication.ui.TakePictureManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

public class CompanyshowDefine extends AppCompatActivity {
    private ImageButton changepic;
    private TakePictureManager PictureManager;
    private SelectableRoundedImageView mainpic;
    private RelativeLayout company_backgroud;
    private String tupianUrl = "";
    private String uploadurl = "";//获得并需要返回给服务器的地址
    private Drawable d;
    private Button EditCompany;
    private TextView next_step;
    private EditText fuwushangName;
    private List<yellowPage_info> commonFunList = new ArrayList<>();
    private List<String> fuwuList = new ArrayList<>();
    private List<grideInfo> grideFuwulist=new ArrayList<>();
    private TextView tv_create;
    private String companyName,comid;
    private Handler handler;
    final private int UPDATE=0;
    private Boolean updateSuccess=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_companyshow_define);
        AppApplication.getInstance();
        next_step=(TextView) findViewById(R.id.next_step);
        fuwushangName=(EditText)findViewById(R.id.fuwushangName);
        mainpic=(SelectableRoundedImageView)findViewById(R.id.mainpic);

        companyName  =getIntent().getStringExtra("companyName");


        getFuwulist(companyName);

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what==UPDATE)
                {
                    fuwushangName.setText(companyName);
                }
            }
        };

        mainpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActionSheetDialog();
            }
        });
        next_step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                threadPost();
                if(updateSuccess){
                    Intent intent=new Intent(CompanyshowDefine.this,fwDetail.class);
                    intent.putExtra("companyName",fuwushangName.getText().toString());
                    intent.putExtra("headImg",uploadurl);
                    Toast.makeText(CompanyshowDefine.this,uploadurl,Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
                else{
                    Toast.makeText(CompanyshowDefine.this,"网络不太好，请再试一次",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void getFuwulist(final String Contex) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg=new Message();
                msg.what=UPDATE;
                handler.sendMessage(msg);
            }
        }).start();

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

                Glide.with(CompanyshowDefine.this).load(outFile).error(R.mipmap.ic_launcher).into(mainpic);
                d=Drawable.createFromPath(outFile.toString());
                // company_backgroud.setBackground(d);
                tupianUrl=outFile.toString();
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
        //  PictureManager.setTailor(1, 1, 350, 350);
        PictureManager.startTakeWayByAlbum();
        PictureManager.setComptressor(false);
        PictureManager.setTakePictureCallBackListener(new TakePictureManager.takePictureCallBackListener() {
            @Override
            public void successful(boolean isTailor, File outFile, Uri filePath) {

                Glide.with(CompanyshowDefine.this).load(outFile).error(R.mipmap.ic_launcher).into(mainpic);
                d=Drawable.createFromPath(outFile.toString());
                // company_backgroud.setBackground(d);
                tupianUrl=outFile.toString();

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

                    uploadurl=new JSONObject(tempResponse).getString("url");
                    updateSuccess=new JSONObject(tempResponse).getBoolean("code");

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                //  gettupianurl(tempResponse);//反馈图片地址到服务器；


            }

        });

    }


    private void  gettupianurl(String companyName) {


        try {

            JSONObject jsonObject = new JSONObject(companyName);

            uploadurl = jsonObject.getString("url").toString();
            //   (uploadurl);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    private void postDatetoserver(String url) {
        final JSONObject order = new JSONObject();
        OkHttpClient okHttpClient = new OkHttpClient();
        Looper.prepare();
        try {

            RequestBody requestBody = new FormBody.Builder()
                    .add("userid","")
                    .add("image", url)
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
        Looper.myQueue();



    }
}
