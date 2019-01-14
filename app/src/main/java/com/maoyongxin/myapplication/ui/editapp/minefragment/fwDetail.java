package com.maoyongxin.myapplication.ui.editapp.minefragment;

import android.content.BroadcastReceiver;
import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jky.baselibrary.util.common.TimeUtil;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.FuwuList;
import com.maoyongxin.myapplication.entity.grideInfo;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.request.ReqCommunity;
import com.maoyongxin.myapplication.http.response.MyCommunityResponse;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.ui.AddressHomeCheckActivity;
import com.maoyongxin.myapplication.ui.ImageGetter;
import com.maoyongxin.myapplication.ui.TakePictureManager;
import com.maoyongxin.myapplication.ui.adapter.Fuwu_gride_adapter;
import com.maoyongxin.myapplication.ui.adapter.Yellow_pageAdapter;
import com.maoyongxin.myapplication.ui.adapter.shangjia_gride_adapter;
import com.maoyongxin.myapplication.ui.editapp.renmai.Yellow_page;
import com.maoyongxin.myapplication.ui.groupchat.Dynamic_huifu;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

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

public class fwDetail extends AppCompatActivity {
    private List<FuwuList> hangyelists = new ArrayList<>();

    final private int UPDATE=0;
    final private int UPDATFWDETAIL = 1;
    private Handler handler;

    private ListView  lv_LS_fenlei;
    private List<grideInfo> grideFuwulist=new ArrayList<>();

    private Yellow_pageAdapter mingpianAdaper;
    private GridView LS_xifen;
    private JSONArray  fwdata;
    private JSONObject classchoic;
    private ImageView img_show;
    private shangjia_gride_adapter fwgrideAdapter;
    private TakePictureManager PictureManager;
    private String tupianUrl ,companyname,communityId;
    private Drawable d;
    private TextView tv_create;
    private String userId,classify_id;
    private String uploadurl;// 获取并反馈给服务器的图片链接
    private int dafenlei;
    private String myCommunityId;
    private int listIndex;
    private Boolean updatesucess=false;
    private BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            listIndex = 0;

        }
    };
    private TextView et_intro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fw_detail);


        tv_create=(TextView)findViewById(R.id.tv_create);
        img_show=(ImageView)findViewById(R.id.img_show);
        lv_LS_fenlei=(ListView)findViewById(R.id.LS_fenlei);
        LS_xifen=(GridView)findViewById(R.id.LS_xifen);
        et_intro=(EditText)findViewById(R.id.et_intro);

      //  tupianUrl=getIntent().getStringExtra("headImg");

        companyname=getIntent().getStringExtra("companyName");

        userId=AppApplication.getCurrentUserInfo().getUserId();
        getFuwulist();

        tv_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(fwDetail.this);
                builder.setTitle("提示").setMessage("一家企业只能设置一个服务窗口，是否要创建？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                createserviceWindow();
                            }
                        }).setNegativeButton("取消", null).show();
            }
        });
        img_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActionSheetDialog();
            }
        });

        lv_LS_fenlei.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mingpianAdaper.setCurrentItem(i);
                mingpianAdaper.notifyDataSetChanged();
                updateGrideList(i);
                dafenlei=i;
            }
        });
        LS_xifen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                fwgrideAdapter.setCurrentItem(i);
                fwgrideAdapter.notifyDataSetChanged();
                getClassfyid(i);

            }
        });
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==UPDATE)
                {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mingpianAdaper = new Yellow_pageAdapter(fwDetail.this, hangyelists);
                            lv_LS_fenlei.setAdapter(mingpianAdaper);
                        }
                    },10);


                }
                else if (msg.what == UPDATFWDETAIL) {

                    fwgrideAdapter=new shangjia_gride_adapter(fwDetail.this,grideFuwulist);
                    LS_xifen.setAdapter(fwgrideAdapter);


                }
            }
        };

    }

    private void getClassfyid(int position)
    {
        try{
            JSONObject gridedate= fwdata.getJSONObject(dafenlei);
            JSONArray fuwulist=gridedate.getJSONArray("sublevel");
            classify_id=fuwulist.getJSONObject(position).getString("id");

        }catch (Exception e){
            e.printStackTrace();
        }

    }
    private void updateGrideList(int position)
    {
        try{
            JSONObject gridedate= fwdata.getJSONObject(position);
            JSONArray fuwulist=gridedate.getJSONArray("sublevel");
            grideFuwulist.clear();
            for(int i=0;i<fuwulist.length();i++)
            {
                grideInfo fwDetail =new grideInfo();
                fwDetail.setfuwuInfo(fuwulist.getJSONObject(i).getString("name"),fuwulist.getJSONObject(i).getString("img"),fuwulist.getJSONObject(i).getString("id"));
                grideFuwulist.add(fwDetail);
            }
            Message msg = new Message();
            msg.what = UPDATFWDETAIL;
            handler.sendMessage(msg);

        }catch (Exception e){
            e.printStackTrace();
        }

    }
    private void createserviceWindow(){
        threadPost();
        getMyOwnCommunity();
        if(updatesucess){
            OkHttpUtils.post().url("http://st.3dgogo.com/index/enterprise_info/create_enterprise_publicity_details_api.html")
                    .addParams("name", companyname)
                    .addParams("community_id", myCommunityId)
                    .addParams("classify_id", classify_id)
                    .addParams("publicity_img", uploadurl)
                    .addParams("last_operator", userId)
                    .addParams("intro", et_intro.getText().toString())
                    .build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {

                }

                @Override
                public void onResponse(String response, int id) {
                    try {


                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getInt("code") == 200) {
                                Toast.makeText(fwDetail.this,"恭喜，创建成功",Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(fwDetail.this,xiangqingye.class);
                                startActivity(intent);
                            } else if (jsonObject.getInt("code") == 500) {

                                Toast.makeText(fwDetail.this,jsonObject.getString("msg"),Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();


                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void getFuwulist() {
        final String XkOne = "http://st.3dgogo.com/index/enterprise_grouping/get_enterprise_grouping_list_api.html";


        final String usrId;

        usrId = AppApplication.getCurrentUserInfo().getUserId().toString();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();

                    Request request = new Request.Builder()
                            .url(XkOne).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseFuwuList(responseData);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }

    private void parseFuwuList(String fuwulist) {
        try {
            JSONObject jsonObject = new JSONObject(fuwulist);
            fwdata = jsonObject.getJSONArray("info");
            hangyelists.clear();
            for (int i = 0; i < fwdata.length(); i++) {
                FuwuList datas = new FuwuList();
                String name = fwdata.getJSONObject(i).getString("name");

                String id = fwdata.getJSONObject(i).getString("id");
                datas.setfuwuInfo(name, id);

                hangyelists.add(datas);
            }

            Message msg = new Message();
            msg.what = UPDATE;
            handler.sendMessage(msg);
        } catch (Exception e) {

        }


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

                Glide.with(fwDetail.this).load(outFile).error(R.mipmap.ic_launcher).into(img_show);
                d= Drawable.createFromPath(outFile.toString());
                // company_backgroud.setBackground(d);
                tupianUrl=outFile.toString();
            }

            //失败回调
            @Override
            public void failed(int errorCode, List<String> deniedPermissions) {
                Log.e("==w",deniedPermissions.toString());
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

                Glide.with(fwDetail.this).load(outFile).error(R.mipmap.ic_launcher).into(img_show);
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

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String tempResponse = response.body().string();//获取服务器返回的图片地址
                try{
                    uploadurl=new JSONObject(tempResponse).getString("url");
                    updatesucess=new JSONObject(tempResponse).getBoolean("code");
                }
                catch (Exception e){
                    e.printStackTrace();
                }




            }

        });

    }
    private void getMyOwnCommunity() {
        ReqCommunity.getMyCommunity(this, getActivityTag(), AppApplication.getCurrentUserInfo().getUserId(), new EntityCallBack<MyCommunityResponse>() {
            @Override
            public Class<MyCommunityResponse> getEntityClass() {
                return MyCommunityResponse.class;
            }

            @Override
            public void onSuccess(MyCommunityResponse resp) {
                if (resp.is200()) {

                    myCommunityId = resp.obj.getCommunityId() + "";

                } else {

                }
            }

            @Override
            public void onFailure(Throwable e) {
                //  lineMyCommunity.setVisibility(View.GONE);
                //   tvNoCommunity.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(Throwable e) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    public String getActivityTag() {
        return getClass().getSimpleName();
    }

}
