package com.maoyongxin.myapplication.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;


import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.google.gson.JsonObject;
import com.jky.baselibrary.widget.TitleBar;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.http.second.HttpNetWorkUtils;
import com.maoyongxin.myapplication.http.second.XGsonUtil;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.myapp.AppConfig;
import com.maoyongxin.myapplication.myapp.AppTitleBarActivity;
import com.maoyongxin.myapplication.server.utils.FileUtil;
import com.maoyongxin.myapplication.server.widget.SelectableRoundedImageView;
import com.maoyongxin.myapplication.server.widget.SimpleChoiceDialog;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class UserEditNewActivity extends AppTitleBarActivity {


    @BindView(R.id.TitleBar_edit)
    TitleBar TitleBarEdit;
    @BindView(R.id.edit_header)
    ImageView editHeader;
    @BindView(R.id.tv_user_nikeName)
    TextView tvUserNikeName;
    @BindView(R.id.line_nikeName)
    LinearLayout lineNikeName;
    @BindView(R.id.tv_user_birthDay)
    TextView tvUserBirthDay;
    @BindView(R.id.line_birthDay)
    LinearLayout lineBirthDay;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.line_sex)
    LinearLayout lineSex;
    @BindView(R.id.tv_community)
    TextView tvCommunity;
    @BindView(R.id.line_community)
    LinearLayout lineCommunity;
    @BindView(R.id.tv_note)
    EditText tvNote;
    @BindView(R.id.line_note)
    LinearLayout lineNote;
    @BindView(R.id.tv_home)
    TextView tvHome;
    @BindView(R.id.line_home)
    LinearLayout lineHome;
    @BindView(R.id.tv_work)
    TextView tvWork;
    @BindView(R.id.line_work)
    LinearLayout lineWork;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.line_phone)
    LinearLayout linePhone;
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
    private String nickName;
    private String note;
    private String phoneNumber;
    private int sex;
    private String localhead;

    @Override
    protected void initData() {
        super.initData();
        mCoverChoices = new ArrayList<>();
//        mCoverChoices.add(getString(R.string.take_photo));
        mCoverChoices.add(getString(R.string.local_photo));
        if (AppApplication.getCurrentUserInfo().getSex() == null) {
            sex = 0;
        } else if (AppApplication.getCurrentUserInfo().getSex().equals("1")) {
            sex = 1;
        } else {
            sex = 2;
        }

        nickName = AppApplication.getCurrentUserInfo().getUserName();
        birthDay = AppApplication.getCurrentUserInfo().getBrithday();
        myHeadPath = AppApplication.getCurrentUserInfo().getHeadImg();
        localhead=myHeadPath;
        phoneNumber=AppApplication.getCurrentUserInfo().getUserPhone();
        note=AppApplication.getCurrentUserInfo().getNote();
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);
    }
    @Override
    protected void initView() {
        super.initView();
        tvNote.setText(AppApplication.getCurrentUserInfo().getNote().toString());
        mProgressDialog = new ProgressDialog(this, android.R.style.Theme_DeviceDefault_Dialog);
        mProgressDialog.setCancelable(true);
        mCoverChoiceDialog = new SimpleChoiceDialog(this, mCoverChoices);
        datePickerDialog = new DatePickerDialog(UserEditNewActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                year = i;
                month = i1 + 1;
                day = i2;
                String months, days;
                if (month < 10) {
                    months = "0" + month;
                } else {
                    months = month + "";
                }
                if (day < 10) {
                    days = "0" + day;
                } else {
                    days = day + "";
                }
                birthDay = year + "" + months + "" + days;
                tvUserBirthDay.setText(birthDay);
            }
        }, year, month, day);
        if (AppApplication.getCurrentUserInfo().getHeadImg() == null || AppApplication.getCurrentUserInfo().getHeadImg().equals("")) {
            Glide.with(this).load(R.mipmap.user_head_img).into(editHeader);
        } else {
            Glide.with(this).load(AppApplication.getCurrentUserInfo().getHeadImg()).into(editHeader);
        }
        setSexyText();
        tvPhone.setText(AppApplication.getCurrentUserInfo().getUserPhone());
        tvUserBirthDay.setText(AppApplication.getCurrentUserInfo().getBrithday());
        tvUserNikeName.setText(AppApplication.getCurrentUserInfo().getUserName());

    }
    @Override
    protected void initEvent() {
        super.initEvent();
        TitleBarEdit.setOnTitleBarClickListener(new TitleBar.OnClickListener() {
            @Override
            public boolean onClick(int function) {
                if(function==TitleBar.FUNCTION_RIGHT_TEXT){
                    Intent intent = new Intent();
                    intent.putExtra("sex", sex);
                    intent.putExtra("birthDay", birthDay);
                    intent.putExtra("nickName", nickName);
                    intent.putExtra("localhead", localhead);
                    intent.putExtra("note", note);
                    setResult(AppApplication.GETMY_USERINFO_OK, intent);
                    finish();
                }
                else if(function==TitleBar.FUNCTION_LEFT_TEXT)
                {
                    Intent intent = new Intent();
                    intent.putExtra("sex", sex);
                    intent.putExtra("birthDay", birthDay);
                    intent.putExtra("nickName", nickName);
                    intent.putExtra("localhead", localhead);
                    intent.putExtra("note", note);
                    setResult(AppApplication.GETMY_USERINFO_OK, intent);
                    finish();
                }
                else if(function==TitleBar.FUNCTION_LEFT_ICON)
                {
                    Intent intent = new Intent();
                    intent.putExtra("sex", sex);
                    intent.putExtra("birthDay", birthDay);
                    intent.putExtra("nickName", nickName);
                    intent.putExtra("localhead", localhead);
                    intent.putExtra("note", note);
                    setResult(AppApplication.GETMY_USERINFO_OK, intent);
                    finish();
                }
                return false;
            }
        });
        mCoverChoiceDialog.setOnChoiceListener(new SimpleChoiceDialog.OnChoiceListener() {
            @Override
            public void onChoice(int index) {
                showPhotoPicker(index);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("sex", sex);
        intent.putExtra("birthDay", birthDay);
        intent.putExtra("nickName", nickName);
        intent.putExtra("localhead", localhead);
        intent.putExtra("note", note);
        setResult(AppApplication.GETMY_USERINFO_OK, intent);
        finish();
    }

    private void setNickName() {
        final EditText inputServer = new EditText(UserEditNewActivity.this);
        AlertDialog.Builder builder = new AlertDialog.Builder(UserEditNewActivity.this);
        builder.setTitle("请输入昵称").setView(inputServer)
                .setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                nickName = inputServer.getText().toString();
                tvUserNikeName.setText(nickName);
            }
        });
        builder.show();
    }
    /*
    private void setNote() {
        final EditText inputServer = new EditText(UserEditNewActivity.this);
        AlertDialog.Builder builder = new AlertDialog.Builder(UserEditNewActivity.this);
        builder.setTitle("请输入个性签名").setView(inputServer)
                .setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                note = inputServer.getText().toString();
                tvNote.setText(note);
            }
        });
        builder.show();
    }*/
    private void showSexyChoiceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("你好").setMessage("请选择您的性别").setPositiveButton("高富帅", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sex = 1;
                setSexyText();
            }
        }).setNegativeButton("白富美", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sex = 2;
                setSexyText();
            }
        }).setNeutralButton("让你们猜", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sex = 0;
                setSexyText();
            }
        }).setCancelable(false).show();
    }

    private void setSexyText() {
        if (sex == 0) {
            tvSex.setText("保密");
        } else if (sex == 1) {
            tvSex.setText("男");
        } else {
            tvSex.setText("女");
        }
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
                            showToastShort("修改头像成功");
                             AppApplication.getCurrentUserInfo().setHeadImg(myHeadPath);
                              AppApplication.getCurrentUserInfo().setServerhead(false);
                            localhead=myHeadPath;
                            Glide.with(UserEditNewActivity.this).load(myHeadPath).into(editHeader);


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




    @Override
    protected int bindLayout() {
        return R.layout.activity_user_edit_new;
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
        HttpNetWorkUtils.uploadHeadImg(this, AppApplication.getCurrentUserInfo().getUserId(), files, myHandler);
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

    private Uri getCutImageUri() {
        mTmpFile = new File(FileUtil.getTempDir(), picName + AVATAR_CUT);
        if (!mTmpFile.exists())
            try {
                mTmpFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return Uri.fromFile(mTmpFile);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//白底黑字+沉浸式导航栏



        ButterKnife.bind(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.edit_header, R.id.line_nikeName, R.id.line_birthDay, R.id.line_sex, R.id.line_community, R.id.line_note, R.id.line_home, R.id.line_work})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.edit_header:
                mCoverChoiceDialog.show();
                break;
            case R.id.line_nikeName:
                setNickName();
                break;
            case R.id.line_birthDay:
                datePickerDialog.show();
                break;
            case R.id.line_sex:
                showSexyChoiceDialog();
                break;
            case R.id.line_community:
                startActivity(new Intent(getActivity(), AddressHomeCheckActivity.class));
                break;
            case R.id.line_note:
                //setNote();
                break;
            case R.id.line_home:
                showToastShort("模块暂无开发");
                break;
            case R.id.line_work:
                showToastShort("模块暂无开发");
                break;
        }
    }
}
