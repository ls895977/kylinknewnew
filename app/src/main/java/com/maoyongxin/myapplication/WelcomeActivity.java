package com.maoyongxin.myapplication;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.request.ReqRefreshToken;
import com.maoyongxin.myapplication.http.request.ReqUserServer;
import com.maoyongxin.myapplication.http.response.LoginResponse;
import com.maoyongxin.myapplication.http.response.RefreshTokenResponse;
import com.maoyongxin.myapplication.myapp.AppActivity;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.server.widget.ClearWriteEditText;

import com.maoyongxin.myapplication.tool.PollingUtils;
import com.maoyongxin.myapplication.tool.ToastUtil;
import com.maoyongxin.myapplication.ui.ActivityCollector;
import com.maoyongxin.myapplication.ui.LoginActivity;
import com.maoyongxin.myapplication.ui.MainActivity;
import com.maoyongxin.myapplication.ui.PackageUtils;

import com.maoyongxin.myapplication.ui.PollingService;
import com.maoyongxin.myapplication.ui.StreamUtils;
import com.maoyongxin.myapplication.ui.newBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import okhttp3.Call;

/**
 * Created by maoyongxin on 2017/11/27.
 */

public class WelcomeActivity extends AppActivity {
    @BindView(R.id.login_img)
    ImageView loginImg;
    private Context context;
    private Handler handler = new Handler();
    private boolean isFirstIn;
    private boolean isAutoLogin;
    private String phoneNum;
    private String pswNum;
    private String content = "";
    private ImageView mImg_Background;
    private ClearWriteEditText mPhoneEdit, mPasswordEdit;
    private ProgressDialog mProgressDialog;
    private static final int GOT_TOKEN = 1;
    private FrameLayout de_frm_backgroud;
    private static final int REQUEST_CODE = 0; // 请求码
    private String version1;
    int versionCode;
    int mainversion;
    int subversion;
    private static final int MY_PERMISSION_REQUEST_CODE = 10000;
    String path;
    String fileName;
    String publishVersionNum;
    private static final String TAG = "SplashActivity";
    private static final int WHAT_SHOW_UPDATE_DIALOG = 100;
    private static final int WHAT_SHOW_ERROR_TOAST = 101;
    private static final int REQUEST_CODE_INSTALL = 100;
    private TextView mTvVersion;
    private String mDownloadUrl;
    private static final String savePath = "/sdcard/updateAPK/"; //apk保存到SD卡的路径
    private static final String saveFileName = savePath + "app-release.apk"; //完整路径名
    private String permissionInfo;
    private final int SDK_PERMISSION_REQUEST = 127;
    private List<String> newFunction, upgrade;
    private String forceUpdate;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case WHAT_SHOW_UPDATE_DIALOG:
                    if (forceUpdate.equals("1")) {
                        doLogin();
                        //  showSafeUpdateDialog((String) msg.obj);
                    } else {
                        forceUpdateDialog((String) msg.obj);
                    }


                    break;
                case WHAT_SHOW_ERROR_TOAST:
                    // 提醒
                    Toast.makeText(WelcomeActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();


                    load2Home();    //现在不需要在首页的时候需要 ToDO
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected int bindLayout() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        return R.layout.activity_welcom;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }


    @Override
    protected void initData() {
        super.initData();

        SharedPreferences preferences = this.getSharedPreferences("first_open", MODE_PRIVATE);
        iniImg();
        isFirstIn = preferences.getBoolean("is_first_open", true);
        preferences = getSharedPreferences("first_open", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("is_first_open", false);
        editor.commit();

    }

    private void iniImg() {
        OkHttpUtils.post().url("http://st.3dgogo.com/index/get_photos/getAppLoginPhotos.html")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    final String imgurl;
                    imgurl = jsonObject.getJSONObject("info").getString("img");
                    if (!imgurl.equals("") && imgurl != null) {
                        try {
                            Glide.with(getActivity()).load(imgurl).into(loginImg);
                        } catch (Exception e) {
                            e.printStackTrace();

                            Glide.with(getActivity()).load(R.mipmap.welcome).into(loginImg);
                        }

                        //
                    } else {
                        Glide.with(getActivity()).load(R.mipmap.welcome).into(loginImg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

    }


    private void iniy() {


        mainversion = versionCode / 10;
        subversion = versionCode % 10;

        OkHttpUtils.post().url("https://st.3dgogo.com/index/versions_update/get_versions_update_info.html")
                .addParams("versionCode", mainversion + "")
                .addParams("subversionCode", subversion + "")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {


                doLogin();
                //  finish();


            }

            @Override
            public void onResponse(String response, int id) {
                //Log.e("更新", "onResponse: " + response);
                Gson gson = new Gson();
                newBean gengxinBean = gson.fromJson(response, newBean.class);
                newBean.HeadnewBean head = gengxinBean.getHead();
                boolean success = head.isSuccess();
                if (success == true) {

                    path = gengxinBean.getBody().getPath();
                    AppApplication.setmDownloadUrl(path);
                    fileName = gengxinBean.getBody().getFileName();
                    publishVersionNum = gengxinBean.getBody().getPublishVersionNum();
                    newFunction = gengxinBean.getBody().getNewFunction();
                    upgrade = gengxinBean.getBody().getMajorization();
                    forceUpdate = gengxinBean.getBody().getIsUpdated() + "";
                    int localVersionCode = PackageUtils
                            .getVersionCode(WelcomeActivity.this);

                    getPersimmions();//弹出   动态的权限


                } else {
                    Gson gson1 = new Gson();
                    newBean addBean = gson1.fromJson(response, newBean.class);
                    String msg = addBean.getHead().getMsg();
//


                    mHandler.postDelayed(new Runnable() {    //做个延时 的处理。

                        @Override
                        public void run() {
                            doLogin();

                        }
                    }, 1000);


                }
            }
        });

    }


    public String getVersionName() {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        version1 = packInfo.versionName;
        versionCode = packInfo.versionCode;
        return version1;
    }


    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();

            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.INTERNET);
            }
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.CAMERA);
            }
            /*
             * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
             */
            // 读写权限
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }
            // 读取路径状态权限
            if (addPermission(permissions, Manifest.permission.READ_PHONE_STATE)) {
                permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
            }

            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }
        }
        showUpdateDialog(newFunction, upgrade, forceUpdate);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission)) {
                return true;
            } else {
                permissionsList.add(permission);
                return false;
            }

        } else {
            return true;
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void showUpdateDialog(List<String> newFunction, List<String> upgrade, String forceUpdate) {
        content += "\n";
        content += "新增功能" + "\n";
        content += "\n";
        for (int i = 0; i < newFunction.size(); i++) {
            content += ((i + 1) + ": " + (newFunction.get(i) + "\n"));
        }
        content += "\n";
        content += "BUG修复" + "\n";
        content += "\n";
        for (int i = 0; i < upgrade.size(); i++) {
            content += ((i + 1) + ": " + (upgrade.get(i) + "\n"));
        }

        Message msg = Message.obtain();
        msg.what = WHAT_SHOW_UPDATE_DIALOG;
        msg.obj = content;
        AppApplication.setUpdatemsg(content);
        mHandler.sendMessage(msg);
    }

    private void showSafeUpdateDialog(String content) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);// 点击其他区域不可取消dialog
        // 设置title
        builder.setIcon(R.mipmap.app_icon);
        builder.setTitle("彼信商业社区");
        // 设置message,由服务器指定的


        builder.setMessage(content);

        builder.setNegativeButton("稍后再说", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (forceUpdate.equals("1")) {
                    doLogin();
                } else {
                    Toast.makeText(getActivity(), "新旧版本改动较大，烦请更新之后使用！谢谢", Toast.LENGTH_SHORT).show();
                }

            }
        });

        builder.setPositiveButton("立刻更新", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                downloadNewApk();
            }
        });

        // dialog显示,UI操作

        mHandler.postDelayed(new Runnable() {    //做个延时 的处理。

            @Override
            public void run() {
                if (!WelcomeActivity.this.isDestroyed()) {
                    builder.show();
                }
            }
        }, 2000);


    }

    private void forceUpdateDialog(String content) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);// 点击其他区域不可取消dialog
        // 设置title
        builder.setIcon(R.mipmap.app_icon);
        builder.setTitle("彼信商业社区");
        // 设置message,由服务器指定的


        builder.setMessage(content);


        builder.setPositiveButton("立刻更新", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                downloadNewApk();
            }
        });

        // dialog显示,UI操作

        mHandler.postDelayed(new Runnable() {    //做个延时 的处理。

            @Override
            public void run() {
                if (!WelcomeActivity.this.isDestroyed()) {
                    builder.show();
                }
            }
        }, 2000);


    }

    private void downloadNewApk() {
        // 弹出进度的dialog
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setProgressNumberFormat(""); //设置左下角不显示
        dialog.setCancelable(false);
        dialog.show();

        // 去网络下载
        new Thread(new DownloadApkTask(dialog)).start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ActivityCollector.addActivity(this);
        ButterKnife.bind(this);


    }

    private class DownloadApkTask implements Runnable {
        private ProgressDialog dialog;
        private String name;

        public DownloadApkTask(ProgressDialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public void run() {
            name = "community";
            String url = mDownloadUrl;// 怎来
            FileOutputStream fos = null;
            InputStream inputStream = null;
            try {
                // 1.去具体的网络接口去下载apk文件
                String sdpath = Environment.getExternalStorageDirectory() + "";
                String mSavePath = sdpath + name + "/" + "updatePath";
                mDownloadUrl = path;
                AppApplication.setmDownloadUrl(mDownloadUrl);

                HttpURLConnection conn = (HttpURLConnection) new URL(mDownloadUrl)
                        .openConnection();
                // 设置超时
                conn.setConnectTimeout(2 * 1000);
                conn.setReadTimeout(2 * 1000);
                // 新的apk文件流
                inputStream = conn.getInputStream();
                // 获得要下载文件的大小
                int contentLength = conn.getContentLength();
                dialog.setMax(contentLength);   //TODO  可以改
                // 指定输出的apk文件,sdcard下
                File file = new File(sdpath,
                        name);
                // 写到文件中
                fos = new FileOutputStream(file);
                int len = -1;
                byte[] buffer = new byte[1024 * 1];
                int progress = 0;
                // 反复的读写输入流
                while ((len = inputStream.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                    progress += len;
                    // 设置进度
                    dialog.setProgress(progress);

                }
                // 下载完成
                // dialog消失
                dialog.dismiss();
                // 提示安装
                installApk(file);
            } catch (Exception e) {

                Toast.makeText(WelcomeActivity.this, e.getMessage() + "", Toast.LENGTH_SHORT).show();
//                notifyError("Error:101");
            } finally {
                StreamUtils.closeIO(inputStream);
                StreamUtils.closeIO(fos);
            }
        }
    }

    private void notifyError(String content) {
        Message msg = Message.obtain();
        msg.what = WHAT_SHOW_ERROR_TOAST;
        msg.obj = content;
        mHandler.sendMessage(msg);
    }

    public static boolean isGrantExternalRW(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            activity.requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);
            return false;
        }
        return true;
    }


    private void installApk(File file) {


        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");

        startActivityForResult(intent, REQUEST_CODE_INSTALL);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_INSTALL) {
            // 响应的是安装的请求结果
            if (resultCode == Activity.RESULT_OK) {
                // 确定
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // 取消

                load2Home();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void load2Home() {

        mHandler.postDelayed(new Runnable() {    //做个延时 的处理。

            @Override
            public void run() {


                doLogin();

            }
        }, 2500);
    }


    @Override
    protected void initView() {

        super.initView();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//白底黑字+沉浸式导航栏
        context = this;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (isFirstIn) {
                    goToLogin();

                } else {
                    isAutoLogin = getSp(WelcomeActivity.this).getBoolean("isAuto", true);
                    if (isAutoLogin) {
                        phoneNum = getSp(WelcomeActivity.this).getString("num", "");
                        pswNum = getSp(WelcomeActivity.this).getString("psw", "");
                        if (phoneNum.equals("") || phoneNum == null || pswNum.equals("") || pswNum == null) {
                            isFirstIn = true;
                            goToLogin();
                        } else {
                            getVersionName();
                            iniy();


                        }

                    } else {
                        goToLogin();
                    }

                }
            }
        }, 10);


    }

    private void doLogin() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ReqUserServer.doLogin(getActivity(), getActivityTag(), phoneNum, pswNum, new EntityCallBack<LoginResponse>() {
                    @Override
                    public Class<LoginResponse> getEntityClass() {
                        return LoginResponse.class;
                    }

                    @Override
                    public void onSuccess(LoginResponse resp) {
                        if (resp.is200()) {
                            AppApplication.setLogNum(phoneNum);
                            AppApplication.setLogPsw(pswNum);
                            AppApplication.setMyPassword(pswNum);
                            AppApplication.setCurrentUserInfo(resp.obj);
                            connect(WelcomeActivity.this, resp.obj.getToken(), resp.obj.getUserId());
                            backdatatoserver();
                        } else {
                            ToastUtil toastUtil = new ToastUtil();
                            toastUtil.Short(getActivity(), "账号或密码有误").setToastColor(Color.WHITE, R.drawable.toast_radius).show();

                        }

                    }

                    @Override
                    public void onFailure(Throwable e) {

                        showToastShort("自动登录失败");//
                        goToLogin();

                    }

                    @Override
                    public void onCancelled(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onFinished() {

                    }
                });
            }
        }).start();

    }


    private void backdatatoserver() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.post().url("http://st.3dgogo.com/index/action_log/setActionLogApi.html")
                        .addParams("action_uid", AppApplication.getCurrentUserInfo().getUserId())
                        .addParams("action_user_name", AppApplication.getCurrentUserInfo().getUserName())
                        .addParams("action_type", "1")
                        .addParams("action_remarks", "APP内登陆")
                        .build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {

                    }
                });

            }
        }).start();
    }


    public void connect(final Context context, String token, final String userId) {
        RongIM.connect(token, new RongIMClient.ConnectCallback() {
            /**
             * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
             * 2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
             */
            @Override
            public void onTokenIncorrect() {
                ReqRefreshToken.refreshToken(WelcomeActivity.this, getActivityTag(), userId, new EntityCallBack<RefreshTokenResponse>() {
                    @Override
                    public Class<RefreshTokenResponse> getEntityClass() {
                        return RefreshTokenResponse.class;
                    }

                    @Override
                    public void onSuccess(RefreshTokenResponse resp) {
                        if (resp.is200()) {
                            connect(WelcomeActivity.this, resp.obj.getToken(), resp.obj.getUserId() + "");
                        } else {
                            //   NToast.shortToast(WelcomeActivity.this,resp.msg);
                        }
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        Toast.makeText(getActivity(), "自动登录失败", Toast.LENGTH_SHORT).show();
                        goToLogin();
                    }

                    @Override
                    public void onCancelled(Throwable e) {

                    }

                    @Override
                    public void onFinished() {

                    }
                });
            }

            /**
             * 连接融云成功
             *
             * @param userid 当前 token 对应的用户 id
             */
            @Override
            public void onSuccess(String userid) {
                if (AppApplication.getMyCurrentLocation() == null) {
                    showToastShort("获取位置信息失败，请检查定位权限或者重新登陆");
                } else {
                    mHandler.postDelayed(new Runnable() {    //做个延时 的处理。

                        @Override
                        public void run() {
                            if (!WelcomeActivity.this.isDestroyed()) {
                                //  builder.show();
                                ToastUtil toastUtil = new ToastUtil();
                                toastUtil.Short(getActivity(), "登陆成功").setToastColor(Color.WHITE, R.drawable.toast_radius).show();
                                Intent intent = new Intent(context, MainActivity.class);
                                intent.putExtra("userId", userId + "");
                                startActivity(intent);
                                overridePendingTransition(R.anim.anim_in, R.anim.anim_out);

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        PollingUtils.startPollingService(getActivity(), 5, PollingService.class, PollingService.ACTION);
                                    }
                                }).start();

                                finish();
                            }
                        }
                    }, 800);


                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                // Log.d("LoginActivity", "--onError--ErrorCode：" + errorCode);
            }
            /**
             * 连接融云失败
             *
             * @param errorCode 错误码，可到官网 查看错误码对应的注释
             */
        });
    }

    public SharedPreferences getSp(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userInfo", MODE_PRIVATE);
        return sharedPreferences;
    }

    private void goToLogin() {
        startActivity(new Intent(context, LoginActivity.class));
        overridePendingTransition(R.anim.scale_in, R.anim.fade_out);
        finish();
    }


}
