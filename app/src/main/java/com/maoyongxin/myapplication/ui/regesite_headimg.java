package com.maoyongxin.myapplication.ui;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.Resource;
import com.google.gson.JsonObject;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.request.ReqRefreshToken;
import com.maoyongxin.myapplication.http.request.ReqUserServer;
import com.maoyongxin.myapplication.http.response.ChangeUserInfoResponse;
import com.maoyongxin.myapplication.http.response.LoginResponse;
import com.maoyongxin.myapplication.http.response.RefreshTokenResponse;
import com.maoyongxin.myapplication.http.second.HttpNetWorkUtils;
import com.maoyongxin.myapplication.http.second.XGsonUtil;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.myapp.AppTitleBarActivity;
import com.maoyongxin.myapplication.server.utils.FileUtil;
import com.maoyongxin.myapplication.server.utils.NToast;
import com.maoyongxin.myapplication.server.widget.SelectableRoundedImageView;
import com.maoyongxin.myapplication.server.widget.SimpleChoiceDialog;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

public class regesite_headimg extends AppTitleBarActivity {
    @BindView(R.id.headimg)
    SelectableRoundedImageView headimg;
    @BindView(R.id.dologin)
    Button dologin;

    @BindView(R.id.user_resource)
    EditText userresource;

    private ProgressDialog mProgressDialog;
    private boolean mPermissionCamera;
    private static final int REQCODE_TAKE_PHOTO = 1;
    private static final int REQCODE_PERMISSION_COVER = 10;
    private static final int REQCODE_LOCAL_PHOTO = 20;
    private String AVATAR_ORI = "avatar_ori.jpg";
    private String AVATAR_CUT = "avatar.jpg";
    private ArrayList<String> mCoverChoices;
    private SimpleChoiceDialog mCoverChoiceDialog;
    private boolean mPermissionWrite;
    private boolean mPermissionRead;
    private File mTmpFile;
    private String myHeadPath;
    private String picName;
    private DatePickerDialog datePickerDialog;
    private int year, month, day;
    private String birthDay;

    private String note;
    private String phoneNumber;
    private int sex;

    private String userId,nickname;
    private String userpsswd;
    private Boolean uploaded=false;
    @Override
    protected int bindLayout() {
        return (R.layout.activity_regesite_headimg);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        super.initData();
        userId = getIntent().getStringExtra("userId");
        nickname=getIntent().getStringExtra("nickname");
        userpsswd = getIntent().getStringExtra("userpsswd");
        mCoverChoices = new ArrayList<>();
        mCoverChoices.add(getString(R.string.local_photo));

    }

    @Override
    protected void initView() {
        super.initView();
        mProgressDialog = new ProgressDialog(this, android.R.style.Theme_DeviceDefault_Dialog);
        mProgressDialog.setCancelable(true);
        mCoverChoiceDialog = new SimpleChoiceDialog(this, mCoverChoices);
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        headimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCoverChoiceDialog.show();
            }
        });
        mCoverChoiceDialog.setOnChoiceListener(new SimpleChoiceDialog.OnChoiceListener() {
            @Override
            public void onChoice(int index) {
                showPhotoPicker(index);
            }
        });
        dologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                note= userresource.getText().toString();
                if(note!=null&&!note.equals(""))
                {
                    if(uploaded)
                    {
                        changeMyUserInfo();
                        doLogin();
                    }
                   else
                    {
                        showToastShort("请设置头像");
                    }
                }
                else
                {
                    showToastShort("个人描述不能为空");
                }

            }
        });
    }

    private void showPhotoPicker(int index) {
        mPermissionWrite = getPermissifyManager().hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        mPermissionRead = getPermissifyManager().hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        mPermissionCamera = getPermissifyManager().hasPermission(Manifest.permission.CAMERA);
        String select = mCoverChoices.get(index);
        if (getString(R.string.take_photo).equals(select)) {
            if (mPermissionWrite && mPermissionCamera) {
                if (FileUtil.hasSdcard()) {
                    mProgressDialog.show();
                    Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, getAvatarUri(true));
                    startActivityForResult(cameraIntent, REQCODE_TAKE_PHOTO);
                }
            } else if (!mPermissionWrite) {
                getPermissifyManager().callWithPermission(this, REQCODE_PERMISSION_COVER + index, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            } else {
                getPermissifyManager().callWithPermission(this, REQCODE_PERMISSION_COVER + index, Manifest.permission.CAMERA);
            }
        } else if (getString(R.string.local_photo).equals(select)) {
            if (mPermissionRead) {
                Album.album(this) // 图片和视频混选。
//                        .multipleChoice() // 多选模式，单选模式为：singleChoice()。
                        .singleChoice()
                        .requestCode(REQCODE_LOCAL_PHOTO) // 请求码，会在listener中返回。
                        .columnCount(3) // 页面列表的列数。
                        .camera(true) // 是否在Item中出现相机。
                        .onResult(new Action<ArrayList<AlbumFile>>() {
                            @Override
                            public void onAction(int requestCode, @NonNull ArrayList<AlbumFile> result) {
                                myHeadPath = result.get(0).getPath();
                                final List<File> files = new ArrayList<>();
                                files.add(new File(myHeadPath));
                                myHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mProgressDialog.setMessage("上传中");
                                        mProgressDialog.show();
                                        upLoadHeadImg(files);
                                    }
                                });
                            }
                        }).onCancel(new Action<String>() {
                    @Override
                    public void onAction(int requestCode, @NonNull String result) {

                    }
                }).start();
            } else {
                getPermissifyManager().callWithPermission(this, REQCODE_PERMISSION_COVER + index, Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }
    }

    private void upLoadHeadImg(List<File> files) {
        HttpNetWorkUtils.uploadHeadImg(this, userId, files, myHandler);
        uploaded=true;
    }

    private Uri getAvatarUri(boolean force) {
        if (force) {
            createAvatarFile();
            Uri imageUri;
            if (mTmpFile != null && mTmpFile.exists()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    String authority = getActivity().getPackageName() + ".provider";
                    imageUri = FileProvider.getUriForFile(getActivity(), authority, mTmpFile);
                } else {
                    imageUri = Uri.fromFile(mTmpFile);
                }
                return imageUri;
            } else {
                return null;
            }
        } else {
            File file = getAvatarFile();
            if (FileUtil.isFileNotEmpty(file))
                return Uri.fromFile(file);
            else
                return null;
        }
    }

    private void createAvatarFile() {
        mTmpFile = new File(FileUtil.getTempDir(), picName + AVATAR_ORI);
        if (mTmpFile.exists())
            mTmpFile.delete();
        try {
            mTmpFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getAvatarFile() {
        mTmpFile = new File(FileUtil.getTempDir(), picName + AVATAR_ORI);
        if (!mTmpFile.exists()) {
            mTmpFile = null;
        }
        return mTmpFile;
    }

    @Override
    protected void handlerPassMsg(int target, int target1, Object obj) {
        switch (target) {
            case HttpNetWorkUtils.UPLOAD_MYHEAD:
                mProgressDialog.dismiss();
                if (null != obj && !obj.toString().equals("")) {
                    try {
                        JsonObject jsonObject = XGsonUtil.getJsonObject(obj.toString());
                        int code = XGsonUtil.getJsonInt(jsonObject, "code");
                        if (code == 200) {
                            showToastShort("头像设置成功");
                            Glide.with(this).load(myHeadPath).into(headimg);
                            dologin.setClickable(true);
                            dologin.setBackgroundColor(getResources().getColor(R.color.blue_tiny) );
                        } else {
                            showToastShort("上传失败");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    showToastShort("服务器未返回数据");
                }
                break;
        }
    }

    private void doLogin() {
        mProgressDialog.show();
        ReqUserServer.doLogin(this, getActivityTag(), userId, userpsswd, new EntityCallBack<LoginResponse>() {
            @Override
            public Class<LoginResponse> getEntityClass() {
                return LoginResponse.class;
            }

            @Override
            public void onSuccess(LoginResponse resp) {
                if (resp.is200()) {
                    AppApplication.setLogNum(userId);
                    AppApplication.setLogPsw(userpsswd);
                    AppApplication.setMyPassword(userpsswd);
                    AppApplication.setCurrentUserInfo(resp.obj);
                    connect(regesite_headimg.this, resp.obj.getToken(), resp.obj.getUserId());
                } else {
                    showToastShort(resp.msg);
                }
                mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Throwable e) {
                mProgressDialog.dismiss();
                showToastShort("登录失败");
            }

            @Override
            public void onCancelled(Throwable e) {
                mProgressDialog.dismiss();
            }

            @Override
            public void onFinished() {
                mProgressDialog.dismiss();
            }
        });
    }

    public void connect(final Context context, String token, final String userId) {
        RongIM.connect(token, new RongIMClient.ConnectCallback() {

            /**
             * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
             * 2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
             */
            @Override
            public void onTokenIncorrect() {
                ReqRefreshToken.refreshToken(regesite_headimg.this, getActivityTag(), userId, new EntityCallBack<RefreshTokenResponse>() {
                    @Override
                    public Class<RefreshTokenResponse> getEntityClass() {
                        return RefreshTokenResponse.class;
                    }

                    @Override
                    public void onSuccess(RefreshTokenResponse resp) {
                        if (resp.is200()) {
                            connect(regesite_headimg.this, resp.obj.getToken(), resp.obj.getUserId() + "");
                        } else {
                            NToast.shortToast(regesite_headimg.this, resp.msg);
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
                    showToastShort("登录成功");
                    Log.d("LoginActivity", "--onSuccess" + userid);
                    Intent intent=new Intent(context,MainActivity.class);
                    intent.putExtra("userId",userId);
                    startActivity(intent);
                    finish();
                }
            }

            /**
             * 连接融云失败
             *
             * @param errorCode 错误码，可到官网 查看错误码对应的注释
             */
            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.d("LoginActivity", "--onError--ErrorCode：" + errorCode);
            }
        });
    }

    private void changeMyUserInfo() {

        ReqUserServer.changeUserInfo(getActivity(), getActivityTag(),userId, note+"", sex + "",  "1991-11-01", nickname + "", new EntityCallBack<ChangeUserInfoResponse>() {
            @Override
            public Class<ChangeUserInfoResponse> getEntityClass() {
                return ChangeUserInfoResponse.class;
            }

            @Override
            public void onSuccess(ChangeUserInfoResponse resp) {
                mProgressDialog.dismiss();
                if (resp.is200()) {
                    AppApplication.setCurrentUserInfo(resp.obj);

                    NToast.shortToast(getActivity(), "保存成功");
                } else {
                    NToast.shortToast(getActivity(), "获取个人信息失败");
                }
            }

            @Override
            public void onFailure(Throwable e) {
                // Log.e("???", "Throwable:111 " +e);
            }

            @Override
            public void onCancelled(Throwable e) {
                // Log.e("???", "Throwable: " +e);
            }

            @Override
            public void onFinished() {
                //Log.e("???", "onFinished: " );
            }
        });
    }

}
