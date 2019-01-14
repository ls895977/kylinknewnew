package com.maoyongxin.myapplication.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.request.ReqCommunity;
import com.maoyongxin.myapplication.http.response.BaseResp;
import com.maoyongxin.myapplication.http.response.MyCommunityResponse;
import com.maoyongxin.myapplication.http.second.HttpNetWorkUtils;
import com.maoyongxin.myapplication.http.second.XGsonUtil;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.myapp.AppHandlerActivity;
import com.maoyongxin.myapplication.server.utils.FileUtil;
import com.maoyongxin.myapplication.server.widget.ClearWriteEditText;
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

public class CommunityEditActivity extends AppHandlerActivity {

    @BindView(R.id.img_communityEdit)
    SelectableRoundedImageView imgCommunityEdit;
    @BindView(R.id.edit_communityNote)
    ClearWriteEditText editCommunityNote;
    @BindView(R.id.btn_changeCommunity)
    Button btnChangeCommunity;
    ProgressDialog mProgressDialog;
    @BindView(R.id.btn_changeCommunityIcon)
    Button btnChangeCommunityIcon;
    private String myCommunityIcon;
    private String myCommunityNote;
    private String myCommunityId;
    private boolean communityIconChanged;
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
    private String picName;
    private File mTmpFile;
    private String myPicturePath;
    private List<File> picFile;

    @Override
    protected int bindLayout() {
        return R.layout.activity_community_edit;
    }

    @Override
    protected void initData() {
        super.initData();
        mCoverChoices = new ArrayList<>();
//        mCoverChoices.add(getString(R.string.take_photo));
        mCoverChoices.add(getString(R.string.local_photo));
        communityIconChanged = false;
    }

    @Override
    protected void initView() {
        super.initView();
        mProgressDialog = new ProgressDialog(this, android.R.style.Theme_DeviceDefault_Dialog);
        mProgressDialog.setMessage("获取信息中，请稍候");
        mCoverChoiceDialog = new SimpleChoiceDialog(this, mCoverChoices);
        mCoverChoiceDialog.setOnChoiceListener(new SimpleChoiceDialog.OnChoiceListener() {
            @Override
            public void onChoice(int index) {
                showPhotoPicker(index);
            }
        });
    }

    @Override
    protected void doBusiness() {
        super.doBusiness();
        getMyCommunity();
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        btnChangeCommunityIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCoverChoiceDialog.show();
            }
        });
        btnChangeCommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (communityIconChanged) {
                    doUploadPic(picFile);
                } else {
                    updateCommunityInfo(editCommunityNote.getText().toString());
                }
            }
        });
    }

    private void doUploadPic(final List<File> files) {
        myHandler.post(new Runnable() {
            @Override
            public void run() {
                mProgressDialog.setMessage("上传中");
                mProgressDialog.show();
                doCreateCommunity(files);
            }
        });
    }

    private void doCreateCommunity(List<File> files) {
        HttpNetWorkUtils.uploadCommunityImg(this, AppApplication.getCurrentUserInfo().getUserId(), myCommunityId, files, myHandler);
    }

    private void showPhotoPicker(int index) {
        mPermissionWrite = getPermissifyManager().hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        mPermissionRead = getPermissifyManager().hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        mPermissionCamera = getPermissifyManager().hasPermission(Manifest.permission.CAMERA);
        String select = mCoverChoices.get(index);
        if (getString(R.string.take_photo).equals(select)) {
            if (mPermissionWrite && mPermissionCamera) {
                if (FileUtil.hasSdcard()) {
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
//                        .selectCount(9 - uploadPicPath.size())  // 最多选择几张图片。
                        .camera(false) // 是否在Item中出现相机。
                        .onResult(new Action<ArrayList<AlbumFile>>() {
                            @Override
                            public void onAction(int requestCode, @NonNull ArrayList<AlbumFile> result) {
                                myPicturePath = result.get(0).getPath();
                                picFile = new ArrayList<>();
                                picFile.add(new File(myPicturePath));
                                Glide.with(CommunityEditActivity.this).load(picFile.get(0)).into(imgCommunityEdit);
                                communityIconChanged = true;
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

    private void getMyCommunity() {
        mProgressDialog.show();
        ReqCommunity.getMyCommunity(this, getActivityTag(), AppApplication.getCurrentUserInfo().getUserId(), new EntityCallBack<MyCommunityResponse>() {
            @Override
            public Class<MyCommunityResponse> getEntityClass() {
                return MyCommunityResponse.class;
            }

            @Override
            public void onSuccess(MyCommunityResponse resp) {
                mProgressDialog.dismiss();
                if (resp.is200()) {
                    myCommunityId = resp.obj.getCommunityId();
                    myCommunityIcon = resp.obj.getCommunityImg();
                    if (!myCommunityIcon.equals("")) {
                        Glide.with(CommunityEditActivity.this).load(myCommunityIcon).into(imgCommunityEdit);
                    } else {
                        Glide.with(CommunityEditActivity.this).load(R.mipmap.community_icon_default).into(imgCommunityEdit);
                    }
                    editCommunityNote.setText(resp.obj.getCommunityNote().toString());
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
    private void updateCommunityInfo(String myCommunityNote){
        ReqCommunity.updateCommunityNote(this, getActivityTag(), AppApplication.getCurrentUserInfo().getUserId(), myCommunityNote, myCommunityId, new EntityCallBack<BaseResp>() {
            @Override
            public Class<BaseResp> getEntityClass() {
                return BaseResp.class;
            }

            @Override
            public void onSuccess(BaseResp resp) {
                showToastShort(resp.msg);
                if(resp.is200()){
                    finish();
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
    @Override
    protected void handlerPassMsg(int target, int target1, Object obj) {
        switch (target) {
            case HttpNetWorkUtils.UPDATE_COMMUNITYICON:
                mProgressDialog.dismiss();
                if (null != obj && !obj.toString().equals("")) {
                    try {
                        JsonObject jsonObject = XGsonUtil.getJsonObject(obj.toString());
                        int code = XGsonUtil.getJsonInt(jsonObject, "code");
                        if (code == 200) {
                            updateCommunityInfo(editCommunityNote.getText().toString());
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
}
