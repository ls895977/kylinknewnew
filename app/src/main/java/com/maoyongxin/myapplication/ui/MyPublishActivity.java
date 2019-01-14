package com.maoyongxin.myapplication.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.PictureEntity;
import com.maoyongxin.myapplication.entity.SelfPublishInfo;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.request.ReqGetProType;
import com.maoyongxin.myapplication.http.request.ReqGetSelfPublishList;
import com.maoyongxin.myapplication.http.response.ProTypeResponse;
import com.maoyongxin.myapplication.http.response.SelfPublishResponse;
import com.maoyongxin.myapplication.http.second.HttpNetWorkUtils;
import com.maoyongxin.myapplication.http.second.XGsonUtil;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.myapp.AppHandlerActivity;
import com.maoyongxin.myapplication.server.utils.FileUtil;
import com.maoyongxin.myapplication.server.widget.ClearWriteEditText;
import com.maoyongxin.myapplication.server.widget.SimpleChoiceDialog;
import com.maoyongxin.myapplication.server.widget.wheelSelectDialog.WheelSelectDialog;
import com.maoyongxin.myapplication.ui.adapter.GridNetPicAdapter;
import com.maoyongxin.myapplication.ui.adapter.MyPublishedListAdapter;
import com.maoyongxin.myapplication.ui.widget.mylistview.LoadListView;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MyPublishActivity extends AppHandlerActivity {

    @BindView(R.id.edit_myPublish_title)
    ClearWriteEditText editMyPublishTitle;
    @BindView(R.id.edit_myPublish_content)
    ClearWriteEditText editMyPublishContent;
    @BindView(R.id.gv_myPublish_picture)
    GridView gvMyPublishPicture;
    @BindView(R.id.activity_my_publish)
    LinearLayout activityMyPublish;
    @BindView(R.id.btn_doUoload)
    Button btnDoUoload;
    @BindView(R.id.tv_publish_noticeType)
    TextView tvPublishNoticeType;
    @BindView(R.id.tv_publish_businessType)
    TextView tvPublishBusinessType;
    @BindView(R.id.tv_publish_thingType)
    TextView tvPublishThingType;
    @BindView(R.id.lv_myPublish_list)
    LoadListView lvMyPublishList;
    private TextView tv_edit_tab;
    private LinearLayout line_push_myPublish;
    private boolean isEditLayoutShow;
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

    private WheelSelectDialog myProductTypeSelectDialog;
    private String[] types;
    private String parentType;
    private String choiceTypeId;
    private boolean isSelectDialogShow;
    private WheelSelectDialog myNoticeTypeSelectDialog;
    private String[] noticeTypes;
    private String noticeTypeId;
    private boolean isNoticeTypeSelectDialogShow;
    private WheelSelectDialog myServiceTypeSelectDialog;
    private String[] serviceTypes;
    private String serviceTypeId;
    private boolean isServiceTypeSelectDialogShow;
    private List<SelfPublishInfo.NoticeList> noticeLists;
    private MyPublishedListAdapter publishAdapter;
    private int listIndex;
    @Override
    protected void onResume() {
        super.onResume();
        listIndex = 0;
        getMyPublishedList(listIndex);
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_my_publish;
    }

    @Override
    protected void initData() {
        super.initData();
        listIndex = 0;
        noticeLists = new ArrayList<>();
        noticeTypeId = 2 + "";
        mCoverChoices = new ArrayList<>();
//        mCoverChoices.add(getString(R.string.take_photo));
        mCoverChoices.add(getString(R.string.local_photo));

        uploadPicPath = new ArrayList<>();
        PictureEntity pictureEntity = new PictureEntity(1);
        pictureEntity.setImgResource(R.mipmap.picture_default);
        uploadPicPath.add(pictureEntity);
        isSelectDialogShow = false;
        isNoticeTypeSelectDialogShow = false;
        isServiceTypeSelectDialogShow = false;
        isEditLayoutShow = false;
    }

    @Override
    protected void initView() {
        super.initView();
        mProgressDialog = new ProgressDialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setMessage(getString(R.string.processing));
        tv_edit_tab = (TextView) findViewById(R.id.tv_edit_tab);
        line_push_myPublish = (LinearLayout) findViewById(R.id.line_push_myPublish);
        tv_edit_tab.setText("我要发布");
        line_push_myPublish.setVisibility(View.GONE);
        adapter = new GridNetPicAdapter(MyPublishActivity.this, uploadPicPath);
        gvMyPublishPicture.setAdapter(adapter);
        mCoverChoiceDialog = new SimpleChoiceDialog(this, mCoverChoices);
        noticeTypes = new String[2];
        noticeTypes[0] = "跳转地址";
        noticeTypes[1] = "内容显示";
        myNoticeTypeSelectDialog = new WheelSelectDialog(this, noticeTypes, null);
        myNoticeTypeSelectDialog.setOnWheelSelectedListener(new WheelSelectDialog.OnWheelSelectedListener() {
            @Override
            public void onSelect(String selected, int index) {
                if (index == 0) {
                    noticeTypeId = 1 + "";
                    tvPublishNoticeType.setText(selected);
                } else {
                    noticeTypeId = 2 + "";
                    tvPublishNoticeType.setText(selected);
                }
            }
        });
        serviceTypes = new String[2];
        serviceTypes[0] = "采购";
        serviceTypes[1] = "出售";
        myServiceTypeSelectDialog = new WheelSelectDialog(this, serviceTypes, null);
        myServiceTypeSelectDialog.setOnWheelSelectedListener(new WheelSelectDialog.OnWheelSelectedListener() {
            @Override
            public void onSelect(String selected, int index) {
                if (index == 0) {
                    serviceTypeId = 1 + "";
                    tvPublishBusinessType.setText(selected);
                } else {
                    serviceTypeId = 2 + "";
                    tvPublishBusinessType.setText(selected);
                }
                setDrawableRightNormal(tvPublishBusinessType);
                isServiceTypeSelectDialogShow = false;
            }
        });
    }

    @Override
    protected void doBusiness() {
        super.doBusiness();
        getMyPublishedList(0);
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        tv_edit_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditLayoutShow == false) {
                    tv_edit_tab.setText("收起");
                    isEditLayoutShow = true;
                    line_push_myPublish.setVisibility(View.VISIBLE);
                    setDrawableRightUp(tv_edit_tab);

                } else {
                    tv_edit_tab.setText("我要发布");
                    isEditLayoutShow = false;
                    line_push_myPublish.setVisibility(View.GONE);
                    setDrawableRightDown(tv_edit_tab);
                }
            }
        });
        tvPublishThingType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelectDialogShow == false) {
                    isSelectDialogShow = true;
                    getProductType(0 + "");
                    setDrawableRightDown(tvPublishThingType);

                } else {
                    isSelectDialogShow = false;
                    myProductTypeSelectDialog.dismiss();
                    setDrawableRightNormal(tvPublishThingType);
                }
            }
        });
        tvPublishNoticeType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNoticeTypeSelectDialogShow == false) {
                    myNoticeTypeSelectDialog.show();
                    isNoticeTypeSelectDialogShow = true;
                    setDrawableRightDown(tvPublishNoticeType);
                } else {
                    isNoticeTypeSelectDialogShow = false;
                    myNoticeTypeSelectDialog.dismiss();
                    setDrawableRightNormal(tvPublishNoticeType);
                }
            }
        });
        tvPublishBusinessType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isServiceTypeSelectDialogShow == false) {
                    myServiceTypeSelectDialog.show();
                    isServiceTypeSelectDialogShow = true;
                    setDrawableRightDown(tvPublishBusinessType);
                } else {
                    isServiceTypeSelectDialogShow = false;
                    myServiceTypeSelectDialog.dismiss();
                    setDrawableRightNormal(tvPublishBusinessType);
                }
            }
        });
        gvMyPublishPicture.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        btnDoUoload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editMyPublishTitle.getText().toString().equals("") || editMyPublishTitle.getText().toString() == null) {
                    showToastShort("请输入标题");
                } else {
                    if (editMyPublishContent.getText().toString().equals("") || editMyPublishContent.getText().toString() == null) {
                        showToastShort("请输入内容");
                    } else {
                        if (serviceTypeId == null) {
                            showToastShort("请设置业务");
                        } else {
                            if (choiceTypeId == null) {
                                showToastShort("请选择产品类别");
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
                    }
                }

            }
        });
        lvMyPublishList.setInterface(new LoadListView.ILoadListener() {
            @Override
            public void onLoad() {
                listIndex++;
                getMyPublishedList(listIndex);
            }
        });
        lvMyPublishList.setReflashInterface(new LoadListView.RLoadListener() {
            @Override
            public void onRefresh() {
                listIndex = 0;
                getMyPublishedList(listIndex);
            }
        });
    }

    @Override
    protected void handlerPassMsg(int target, int target1, Object obj) {
        switch (target) {
            case HttpNetWorkUtils.UPLOAD_MYPUBLISH:
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
                            setDrawableRightDown(tv_edit_tab);
                            getMyPublishedList(0);
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
            case 123:
                mProgressDialog.dismiss();
                adapter = new GridNetPicAdapter(MyPublishActivity.this, uploadPicPath);
                gvMyPublishPicture.setAdapter(adapter);
                break;
        }
    }

    private void uploadImage(List<File> files) {
        HttpNetWorkUtils.upLoadMyPublish(MyPublishActivity.this, editMyPublishTitle.getText().toString(), noticeTypeId + "", AppApplication.getCurrentUserInfo().getUserId(), AppApplication.getMyCurrentLocation().getAdCode(), editMyPublishContent.getText().toString(), serviceTypeId + "", choiceTypeId, files, myHandler);
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

    private void getMyPublishedList(final int listIndex) {
        if (listIndex == 0) {
            mProgressDialog.setMessage("获取我的发布中");
            mProgressDialog.show();
        }
        ReqGetSelfPublishList.getMyPublish(this, getActivityTag(), AppApplication.getCurrentUserInfo().getUserId(), listIndex + "", new EntityCallBack<SelfPublishResponse>() {
            @Override
            public Class<SelfPublishResponse> getEntityClass() {
                return SelfPublishResponse.class;
            }

            @Override
            public void onSuccess(SelfPublishResponse resp) {
                mProgressDialog.dismiss();
                if (resp.is200()) {
                    if (listIndex == 0) {
                        noticeLists = resp.obj.getNoticeList();
                        publishAdapter = new MyPublishedListAdapter(noticeLists, MyPublishActivity.this);
                        lvMyPublishList.setAdapter(publishAdapter);
                        lvMyPublishList.reflashComplete();
                    } else {
                        noticeLists.addAll(resp.obj.getNoticeList());
                        publishAdapter.notifyDataSetChanged();
                        lvMyPublishList.loadCompleted();
                    }
                    adapter.notifyDataSetChanged();
                    lvMyPublishList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            if (!noticeLists.get(i - 1).getNotice().getNoticeType().equals("1") && !noticeLists.get(i - 1).getNotice().getNoticeType().equals("3")) {
                                Intent intent = new Intent(MyPublishActivity.this, PublicDetailActivity.class);
                                intent.putExtra("userId", noticeLists.get(i - 1).getNotice().getUserId());
                                intent.putExtra("noticeId", noticeLists.get(i - 1).getNotice().getNoticeId());
                                startActivity(intent);
                            }
                        }
                    });
                } else {
                    showToastShort(resp.msg);
                }
            }

            @Override
            public void onFailure(Throwable e) {
                mProgressDialog.dismiss();
            }

            @Override
            public void onCancelled(Throwable e) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void getProductType(final String parentId) {
        mProgressDialog.setMessage("获取物品分类中");
        mProgressDialog.show();
        ReqGetProType.getTypeList(this, getActivityTag(), parentId, 520115+ "", new EntityCallBack<ProTypeResponse>() {
            @Override
            public Class<ProTypeResponse> getEntityClass() {
                return ProTypeResponse.class;
            }

            @Override
            public void onSuccess(final ProTypeResponse resp) {
                mProgressDialog.dismiss();
                if (resp.is200()) {
                    types = new String[resp.obj.getThingTypeList().size()];
                    for (int i = 0; i < resp.obj.getThingTypeList().size(); i++) {
                        types[i] = resp.obj.getThingTypeList().get(i).getTypeName();
                    }
                    myProductTypeSelectDialog = new WheelSelectDialog(MyPublishActivity.this, types, null);

                    if (resp.obj.getThingTypeList().get(0).getTypeLevel() == 1) {
                        myProductTypeSelectDialog.setTtile("请选择产品类别");
                        myProductTypeSelectDialog.setOnWheelSelectedListener(new WheelSelectDialog.OnWheelSelectedListener() {
                            @Override
                            public void onSelect(String selected, int index) {
                                parentType = resp.obj.getThingTypeList().get(index).getTypeName();
                                choiceTypeId = resp.obj.getThingTypeList().get(index).getTypeId() + "";
                                getProductType(choiceTypeId);
                            }
                        });
                        myProductTypeSelectDialog.show();
                    } else if (resp.obj.getThingTypeList().get(0).getTypeLevel() == 2) {
                        myProductTypeSelectDialog.setTtile("请选择" + parentType + "种类");
                        myProductTypeSelectDialog.setOnWheelSelectedListener(new WheelSelectDialog.OnWheelSelectedListener() {
                            @Override
                            public void onSelect(String selected, int index) {
                                parentType = resp.obj.getThingTypeList().get(index).getTypeName();
                                choiceTypeId = resp.obj.getThingTypeList().get(index).getTypeId() + "";
                                getProductType(choiceTypeId);
                            }
                        });
                        myProductTypeSelectDialog.show();
                    } else {
                        myProductTypeSelectDialog.setTtile("请选择" + parentType + "种类");
                        myProductTypeSelectDialog.setOnWheelSelectedListener(new WheelSelectDialog.OnWheelSelectedListener() {
                            @Override
                            public void onSelect(String selected, int index) {
                                choiceTypeId = resp.obj.getThingTypeList().get(index).getTypeId() + "";
                                tvPublishThingType.setText(resp.obj.getThingTypeList().get(index).getTypeName());
                                setDrawableRightNormal(tvPublishThingType);
                            }
                        });
                        myProductTypeSelectDialog.show();
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

    private void setDrawableRightNormal(TextView view) {
        Drawable drawable = getResources().getDrawable(R.mipmap.right_arrow);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        view.setCompoundDrawables(null, null, drawable, null);
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
