package com.maoyongxin.myapplication.ui.huati;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.ArrayMap;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.myapp.AppActivity;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.server.utils.NToast;
import com.maoyongxin.myapplication.server.widget.ClearWriteEditText;
import com.maoyongxin.myapplication.ui.ImageGetter;
import com.maoyongxin.myapplication.ui.TakePictureManager;
import com.maoyongxin.myapplication.ui.widget.switchbutton.SwitchButton;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.rockerhieu.emojicon.EmojiconEditText;
import io.github.rockerhieu.emojicon.EmojiconGridFragment;
import io.github.rockerhieu.emojicon.EmojiconsFragment;
import io.github.rockerhieu.emojicon.emoji.Emojicon;
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

import static com.maoyongxin.myapplication.http.second.okhttp.OkHttpUtils.JSON;

public class Fabu extends AppActivity implements EmojiconsFragment.OnEmojiconBackspaceClickedListener, EmojiconGridFragment.OnEmojiconClickedListener {
    @BindView(R.id.textView)
    TextView textView;

    @BindView(R.id.ed_title)
    EditText edTitle;
    @BindView(R.id.dele_title)
    ImageView deleTitle;
    @BindView(R.id.im_showImg)
    ImageView imShowImg;
    @BindView(R.id.take_pic)
    ImageButton takePic;
    @BindView(R.id.strangeName)
    SwitchButton strangeName;
    private String usrid, serverImgUrl, showImg = "";
    private String usrname;
    private String groupid;
    private String groupName, headUrl = "";
    JSONObject fabuJson = new JSONObject();
    private TakePictureManager PictureManager;
    private Drawable d;


    private String param_groupName, param_groupId, param_usrId, param_title, param_content, param_imgUrl = "";

    @BindView(R.id.tv_fabu_address)
    TextView tvFabuAddress;

    @BindView(R.id.btfabu)
    Button btfabu;
    @BindView(R.id.edit_fabu_content)
    ClearWriteEditText editFabuContent;

    @BindView(R.id.editemoj)
    EmojiconEditText editEmojicon;

    @BindView(R.id.emoj_btn)
    RelativeLayout emoj_btn;

    @BindView(R.id.emoj)
    FrameLayout emoj;

    FrameLayout emojicons;

    @BindView(R.id.emoj_view)
    RelativeLayout emoj_view;

    @Override
    protected int bindLayout() {
        return (R.layout.activity_huati_base);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//白底黑字+沉浸式导航栏
        ButterKnife.bind(this);

        deleTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        edTitle.setText("");
                    }
                });
            }
        });

        final List<String> l = new ArrayList<>();
        btfabu.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {

                param_groupId = getIntent().getStringExtra("groupId");
                param_title = edTitle.getText() + "";
                String s = editEmojicon.getText().toString();
                String content = Base64.encodeToString(s.getBytes(), Base64.DEFAULT);
                param_content = content;

                String str2 = new String(Base64.decode(param_content.getBytes(), Base64.DEFAULT));
                Log.e("sssssssssssss",content+"==============="+editEmojicon.getText().toString()+"===="+str2);
//
                if (editEmojicon.getText().toString().equals("") || editEmojicon.getText() == null || edTitle.getText().toString().equals("") || edTitle.getText() == null) {
                    NToast.shortToast(getActivity(), "标题和内容不能空");
                } else {
                    showMessage("正在上传");
                    uploadMultiFile(headUrl);

                }

            }
        });
    }

    private String toStr(String str) {
        char[] strChar = str.toCharArray();
        String result = "";
        for (int i = 0; i < strChar.length; i++) {
            result += Integer.toBinaryString(strChar[i]);
        }
        return result;
    }

    @Override
    protected void initView() {
        editFabuContent.setVisibility(View.INVISIBLE);
        emoj_view.setVisibility(View.VISIBLE);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.emoj, EmojiconsFragment.newInstance(false))
                .commit();
        takePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActionSheetDialog();
            }
        });
        emoj_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emoj.getVisibility() == View.INVISIBLE) {
                    emoj.setVisibility(View.VISIBLE);
                    hideKeyboard();
                } else {
                    emoj.setVisibility(View.INVISIBLE);
                }
            }
        });
        param_usrId = AppApplication.getCurrentUserInfo().getUserId();
        tvFabuAddress.setText(AppApplication.getMyCurrentLocation().getCityName());


    }
    /**
     * 关闭软键盘
     */
    protected void hideKeyboard() {

        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && this.getCurrentFocus() != null) {
            if (this.getCurrentFocus().getWindowToken() != null) {

                imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
    private void showMessage(final String Text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), Text, Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    protected void initEvent() {
        //super.initEvent();
        strangeName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    param_usrId = 10071 + "";
                    Toast.makeText(getActivity(), "您将匿名发布本条话题", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "您将以用户名发布本条话题", Toast.LENGTH_SHORT).show();
                    param_usrId = AppApplication.getCurrentUserInfo().getUserId();
                }
            }
        });
    }

    private void upto_server() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getgrouplist();
            }
        }).start();
    }

    private void getgrouplist()//标准post线程
    {

        OkHttpClient okHttpClient = new OkHttpClient();
        try {

            RequestBody requestBody = new FormBody.Builder()
                    .add("uid", param_usrId)
                    .add("group_id", param_groupId)
                    .add("title", param_title)
                    .add("content", param_content)
                    .add("img", param_imgUrl)
                    .build();
            Request request = new Request.Builder()
                    .url("http://st.3dgogo.com/index/chatgroup_gambit/set_gambit")
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
                            Toast.makeText(getActivity(), "发布成功", Toast.LENGTH_SHORT).show();
                            finish();
                        } else if (jsonObject.getInt("code") == 500) {
                            Toast.makeText(getActivity(), "失败", Toast.LENGTH_SHORT).show();
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

                Glide.with(Fabu.this).load(outFile).error(R.mipmap.ic_launcher).into(imShowImg);
                d = Drawable.createFromPath(outFile.toString());
                // company_backgroud.setBackground(d);
                headUrl = outFile.toString();
            }

            //失败回调
            @Override
            public void failed(int errorCode, List<String> deniedPermissions) {
                //Log.e("==w", deniedPermissions.toString());
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

                Glide.with(Fabu.this).load(outFile).error(R.mipmap.ic_launcher).into(imShowImg);
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
            serverImgUrl = "";
            upto_server();
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
                    //Log.e("aa", "uploadMultiFile() e=" + e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String tempResponse = response.body().string();//获取服务器返回的图片地址

                    try {

                        serverImgUrl = new JSONObject(tempResponse).getString("url");
                        param_imgUrl = serverImgUrl;
                        if (new JSONObject(tempResponse).getBoolean("code")) {
                            upto_server();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //  gettupianurl(tempResponse);//反馈图片地址到服务器；

                }

            });
        }


    }

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        EmojiconsFragment.input(editEmojicon, emojicon);
    }

    @Override
    public void onEmojiconBackspaceClicked(View v) {
        EmojiconsFragment.backspace(editEmojicon);
    }
}