package com.maoyongxin.myapplication.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.MyDynamicListInfo;
import com.maoyongxin.myapplication.entity.PictureEntity;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.request.ReqMyDynamicList;
import com.maoyongxin.myapplication.http.response.MyDongTaiListResponse;
import com.maoyongxin.myapplication.http.second.HttpNetWorkUtils;
import com.maoyongxin.myapplication.http.second.XGsonUtil;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.myapp.AppHandlerActivity;
import com.maoyongxin.myapplication.server.utils.FileUtil;
import com.maoyongxin.myapplication.server.widget.ClearWriteEditText;
import com.maoyongxin.myapplication.server.widget.SimpleChoiceDialog;
import com.maoyongxin.myapplication.ui.adapter.GridNetPicAdapter;
import com.maoyongxin.myapplication.ui.editapp.editadapter.SquareAdapter;
import com.maoyongxin.myapplication.ui.widget.mylistview.AntGrideVIew;
import com.maoyongxin.myapplication.ui.widget.mylistview.LoadListView;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class MyDongtaiActivity extends AppHandlerActivity {
    @BindView(R.id.edit_myDongtai_content)
    ClearWriteEditText editMyDongtaiContent;
    @BindView(R.id.gv_myDongtai_picture)
    AntGrideVIew gvMyDongtaiPicture;
    @BindView(R.id.btn_doUoload_dongtai)
    Button btnDoUoloadDongtai;
    @BindView(R.id.line_dongtai_myDongtai)
    LinearLayout lineDongtaiMyDongtai;
    @BindView(R.id.tv_edit_tab)
    TextView tvEditTab;
    @BindView(R.id.activity_my_dongtai)
    LinearLayout activityMyDongtai;
    @BindView(R.id.llv_myDynamicList)
    LoadListView llvMyDynamicList;
    private boolean isEditDongtaiShow;
    //拍照过程
    private ProgressDialog mProgressDialog;
    private boolean mPermissionWriteSdcard;
    private boolean mPermissionReadSdcard;
    private boolean mPermissionCamera;


    public static final int REQCODE = 101;
    private static final int REQCODE_TAKE_PHOTO = 1;
    private static final int REQCODE_ALBUM = 2;
    private static final int REQCODE_RESIZE = 3;


    private static final int REQCODE_PERMISSION_COVER = 10;
    private static final int REQCODE_LOCAL_PHOTO = 20;
    private String AVATAR_ORI = "avatar_ori.jpg";
    private String AVATAR_CUT = "avatar.jpg";
    private GridNetPicAdapter adapter;
    private List<PictureEntity> uploadPicPath;
    private String picName;

    private ArrayList<String> mCoverChoices;
    private SimpleChoiceDialog mCoverChoiceDialog;

    private boolean mPermissionWrite;
    private boolean mPermissionRead;
    private File mTmpFile;
    public static final int ADDRESULTCODE = 111;
    private String imgUrl;
    private int listIndex;
    private SquareAdapter listAdapter;
    private List<MyDynamicListInfo.DynamicList> dynamicLists;

    @Override
    protected void initData() {
        super.initData();
        isEditDongtaiShow = false;
        mCoverChoices = new ArrayList<>();
//        mCoverChoices.add(getString(R.string.take_photo));
        mCoverChoices.add(getString(R.string.local_photo));

        uploadPicPath = new ArrayList<>();
        PictureEntity pictureEntity = new PictureEntity(1);
        pictureEntity.setImgResource(R.mipmap.picture_default);
        uploadPicPath.add(pictureEntity);
        listIndex = 0;
        dynamicLists = new ArrayList<>();

    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_my_dongtai;
    }

    @Override
    protected void initView() {
        super.initView();
        mProgressDialog = new ProgressDialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setMessage(getString(R.string.processing));
        lineDongtaiMyDongtai.setVisibility(View.GONE);
        adapter = new GridNetPicAdapter(MyDongtaiActivity.this, uploadPicPath);
        gvMyDongtaiPicture.setAdapter(adapter);
        mCoverChoiceDialog = new SimpleChoiceDialog(this, mCoverChoices);
    }

    @Override
    protected void doBusiness() {
        super.doBusiness();
        getMyDongTaiList();
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        tvEditTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditDongtaiShow) {
                    isEditDongtaiShow = false;
                    lineDongtaiMyDongtai.setVisibility(View.GONE);
                    setDrawableRightDown(tvEditTab);
                    tvEditTab.setText("我要发布");
                } else {
                    isEditDongtaiShow = true;
                    lineDongtaiMyDongtai.setVisibility(View.VISIBLE);
                    setDrawableRightUp(tvEditTab);
                    tvEditTab.setText("收起");
                }
            }
        });
        gvMyDongtaiPicture.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == uploadPicPath.size() - 1) {
                    mCoverChoiceDialog.show();
                }
            }
        });
        mCoverChoiceDialog.setOnChoiceListener(new SimpleChoiceDialog.OnChoiceListener() {
            @Override
            public void onChoice(int index) {
                showPhotoPicker(index);
            }
        });
        btnDoUoloadDongtai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editMyDongtaiContent.getText().toString().equals("") || editMyDongtaiContent.getText() == null) {
                    showToastShort("请先输入内容");
                } else {
                    mProgressDialog.show();
                    final List<File> files = new ArrayList<File>();
                    for (int i = 0; i < uploadPicPath.size() - 1; i++) {
                        File file = new File(uploadPicPath.get(i).getFilePath().toString());
                        files.add(file);
                    }
                    myHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mProgressDialog.setMessage("上传中");
                            uploadImage(files);
                        }
                    });
                }
            }
        });
        llvMyDynamicList.setInterface(new LoadListView.ILoadListener() {
            @Override
            public void onLoad() {
                listIndex++;
                getMyDongTaiList();
            }
        });
        llvMyDynamicList.setReflashInterface(new LoadListView.RLoadListener() {
            @Override
            public void onRefresh() {
                listIndex = 0;
                getMyDongTaiList();
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
                        .multipleChoice() // 多选模式，单选模式为：singleChoice()。
//                        .singleChoice()
                        .requestCode(REQCODE_LOCAL_PHOTO) // 请求码，会在listener中返回。
                        .columnCount(3) // 页面列表的列数。
                        .selectCount(9 - uploadPicPath.size())  // 最多选择几张图片。
                        .camera(false) // 是否在Item中出现相机。
                        .onResult(new Action<ArrayList<AlbumFile>>() {
                            @Override
                            public void onAction(int requestCode, @NonNull ArrayList<AlbumFile> result) {
                                uploadPicPath.remove(uploadPicPath.size() - 1);

                                for (int i = 0; i < result.size(); i++) {
                                    PictureEntity pictureEntity = new PictureEntity(1);
                                    pictureEntity.setFilePath(result.get(i).getPath());
                                    uploadPicPath.add(pictureEntity);
                                    if (i == result.size() - 1) {
                                        pictureEntity = new PictureEntity(1);
                                        pictureEntity.setImgResource(R.mipmap.picture_default);//添加新的默认图片
                                        uploadPicPath.add(pictureEntity);
                                        Message msg = new Message();
                                        msg.what = 123;

                                        myHandler.sendMessage(msg);
                                    }
                                }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQCODE_TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {
                Uri uri = getAvatarUri(false);
                if (uri == null) {//有部分低端机型，保存相片到本地需要一定时间...
                    logI("Processing SLOW!");
                    new Thread() {
                        @Override
                        public void run() {
                            int cnt = 10;
                            boolean success = false;
                            while (cnt > 0) {
                                logI("Processing SLOW! " + cnt + " second(s) count down.");
                                SystemClock.sleep(1000);
                                cnt--;
                                Uri uriT = getAvatarUri(false);
                                if (uriT != null) {
                                    success = true;
                                    break;
                                }
                            }
                            final boolean finalSuccess = success;
                            logI(finalSuccess ? "Processing DONE!" : "Processing TIMEOUT!");
                            AppApplication.getHandler().post(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressDialog.dismiss();
                                    Uri uriH = getAvatarUri(false);
                                    if (finalSuccess)
                                        resizeImage(uriH);
                                    else
                                        showToastLong("处理缓慢，请重试");
                                }
                            });
                        }
                    }.start();
                } else {
                    logI("Direct Resize");
                    mProgressDialog.dismiss();
                    resizeImage(uri);
                }
            } else
                mProgressDialog.dismiss();
        } else if (requestCode == REQCODE_RESIZE) {
            if (resultCode == RESULT_OK) {
                String avatar = getCutImageUri().getPath();  //得到处理后的图片
                PictureEntity mypicutre = new PictureEntity(1);
                mypicutre.setFilePath(avatar);
                uploadPicPath.remove(uploadPicPath.size() - 1);
                uploadPicPath.add(mypicutre);
                mypicutre = new PictureEntity(1);
                mypicutre.setImgResource(R.mipmap.picture_default);
                uploadPicPath.add(mypicutre);
                adapter.notifyDataSetChanged();
            }
        }
    }

    public void resizeImage(Uri uri) {
        logI(uri.getPath());
        Uri newUri = uri;
        String authority = getActivity().getPackageName() + ".provider";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (!ContentResolver.SCHEME_CONTENT.equals(uri.getScheme()))
                newUri = FileProvider.getUriForFile(getApplicationContext(), authority, getAvatarFile());
        }

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(newUri, "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getCutImageUri());
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, REQCODE_RESIZE);
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
    protected void handlerPassMsg(int target, int target1, Object obj) {
        switch (target) {
            case 123:
                mProgressDialog.dismiss();
                adapter = new GridNetPicAdapter(MyDongtaiActivity.this, uploadPicPath);
                gvMyDongtaiPicture.setAdapter(adapter);
                break;
            case HttpNetWorkUtils.UPLOAD_DONGTAI:
                mProgressDialog.dismiss();
                if (null != obj && !obj.toString().equals("")) {
                    try {
                        JsonObject jsonObject = XGsonUtil.getJsonObject(obj.toString());
                        int code = XGsonUtil.getJsonInt(jsonObject, "code");
                        if (code == 200) {

                            showToastShort("上传成功");
                            initData();
                            initView();
                            initEvent();
                            setDrawableRightDown(tvEditTab);
                            tvEditTab.setText("我要发布");
                            getMyDongTaiList();
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
    private Map<String,String> map = new HashMap<>();
    private void getMyDongTaiList() {
        ReqMyDynamicList.getDongtaiList(this, getActivityTag(), AppApplication.getCurrentUserInfo().getUserId() + "", listIndex + "", new EntityCallBack<MyDongTaiListResponse>() {
            @Override
            public Class<MyDongTaiListResponse> getEntityClass() {
                return MyDongTaiListResponse.class;
            }

            @Override
            public void onSuccess(MyDongTaiListResponse resp) {
                if (resp.is200()) {
                    if (listIndex == 0) {
                        dynamicLists = resp.obj.getDynamicList();
                        listAdapter = new SquareAdapter(dynamicLists, MyDongtaiActivity.this, false);
                        listAdapter.setOnDynamicDeletedListener(new SquareAdapter.OnDynamicDeletedListener() {
                            @Override
                            public void delete() {
                                listIndex = 0;
                                getMyDongTaiList();
                            }
                        });
                        llvMyDynamicList.setAdapter(listAdapter);
                        llvMyDynamicList.reflashComplete();
                        Intent intent = new Intent();
                        intent.setAction("refreshList");
                        MyDongtaiActivity.this.sendBroadcast(intent);
                    } else {
                        dynamicLists.addAll(resp.obj.getDynamicList());
                        listAdapter.notifyDataSetChanged();
                        llvMyDynamicList.loadCompleted();
                    }

                } else {
                    showToastShort(resp.msg);
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


    private void uploadImage(List<File> files) {
        HttpNetWorkUtils.uploadDongtai(MyDongtaiActivity.this, AppApplication.getCurrentUserInfo().getUserId(), editMyDongtaiContent.getText().toString(), files, myHandler);
    }

    private void setDrawableRightDown(TextView view) {
        Drawable drawable = getResources().getDrawable(R.mipmap.down_arrow);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        view.setCompoundDrawables(null, null, drawable, null);
    }

    private void setDrawableRightUp(TextView view) {
        Drawable drawable = getResources().getDrawable(R.mipmap.up_arrow_blue);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        view.setCompoundDrawables(null, null, drawable, null);
    }
}
