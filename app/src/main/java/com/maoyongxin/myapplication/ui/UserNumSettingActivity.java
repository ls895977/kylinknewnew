package com.maoyongxin.myapplication.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.WelcomeActivity;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.myapp.AppTitleBarActivity;
import com.maoyongxin.myapplication.tool.CompressPhotoUtils;
import com.maoyongxin.myapplication.tool.SpUtils;
import com.maoyongxin.myapplication.ui.widget.switchbutton.SwitchButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.constraint.solver.widgets.ConstraintWidget.GONE;

public class UserNumSettingActivity extends AppTitleBarActivity {


    @BindView(R.id.tv_versionNum)
    TextView tvVersionNum;
    @BindView(R.id.btn_doLogout)
    Button btnDoLogout;
    @BindView(R.id.sw_isauto)
    SwitchButton swIsauto;
    @BindView(R.id.updatecheck)
    TextView updatecheck;

    private static final int WHAT_SHOW_UPDATE_DIALOG = 100;
    private static final int WHAT_SHOW_ERROR_TOAST = 101;
    private static final int REQUEST_CODE_INSTALL = 100;

    @Override
    protected int bindLayout() {
        return R.layout.activity_user_num_setting;
    }

    @Override
    protected void initData() {
        // super.initData();
    }


    @Override
    protected void initEvent() {
        super.initEvent();

        updatecheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSafeUpdateDialog(AppApplication.getUpdatemsg());
            }
        });


        if (getSp(this).getBoolean("isAotu", true)) {
            swIsauto.setChecked(true);
        } else {
            swIsauto.setChecked(false);
        }
        btnDoLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SpUtils.putBoolean(UserNumSettingActivity.this,"isFirst",true);
                CompressPhotoUtils.deleteFolderFile("mnt/sdcard/situ/", true);//清空图片操作缓存
                SharedPreferences preferences = UserNumSettingActivity.this.getSharedPreferences("first_open", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("is_first_open", true);
                editor.commit();
                makeSpNoAuto(UserNumSettingActivity.this);
                final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        swIsauto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (swIsauto.isChecked()) {
                    makeSpAuto(UserNumSettingActivity.this);
                } else {
                    makeSpNoAuto(UserNumSettingActivity.this);
                }
            }
        });
    }

    @Override
    protected void handlerPassMsg(int target, int target1, Object obj) {

    }

    @Override
    protected void initView() {
        super.initView();
        if(AppApplication.getmDownloadUrl()!=null)
        {

            updatecheck.setText("可更新");

        }
        else{
            updatecheck.setText("");
        }
        tvVersionNum.setText(getAppVersionName(this));
    }

    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            //Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
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


            }
        });

        builder.setPositiveButton("立刻更新", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                downloadNewApk();
            }
        });

        // dialog显示,UI操作
        builder.show();
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
    private class DownloadApkTask implements Runnable {
        private ProgressDialog dialog;
        private String name;

        public DownloadApkTask(ProgressDialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public void run() {
            name = "community";

            FileOutputStream fos = null;
            InputStream inputStream = null;
            try {
                // 1.去具体的网络接口去下载apk文件
                String sdpath = Environment.getExternalStorageDirectory() + "";
                String mSavePath = sdpath + name + "/" + "updatePath";

                HttpURLConnection conn = (HttpURLConnection) new URL(AppApplication.getmDownloadUrl())
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

                dialog.dismiss();
                // 提示安装
                installApk(file);
            } catch (Exception e) {
                e.printStackTrace();
                // Toast.makeText(getActivity(), e.getMessage() + "", Toast.LENGTH_SHORT).show();
                notifyError("Error:101");
                dialog.dismiss();
            }

            finally {
                StreamUtils.closeIO(inputStream);
                StreamUtils.closeIO(fos);
            }
        }
    }
    private void notifyError(String content) {
        Message msg = Message.obtain();
        msg.what = WHAT_SHOW_ERROR_TOAST;
        msg.obj = content;
        //  mHandler.sendMessage(msg);
    }
    private void installApk(File file) {


        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");

        startActivityForResult(intent, REQUEST_CODE_INSTALL);

    }


}
