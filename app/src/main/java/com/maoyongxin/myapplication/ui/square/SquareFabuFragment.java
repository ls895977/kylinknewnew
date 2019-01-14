package com.maoyongxin.myapplication.ui.square;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.PictureEntity;
import com.maoyongxin.myapplication.http.second.HttpNetWorkUtils;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.server.utils.FileUtil;
import com.maoyongxin.myapplication.server.utils.NToast;
import com.maoyongxin.myapplication.server.widget.ClearWriteEditText;
import com.maoyongxin.myapplication.server.widget.SimpleChoiceDialog;
import com.maoyongxin.myapplication.ui.MyDongtaiActivity;
import com.maoyongxin.myapplication.ui.adapter.GridNetPicAdapter;
import com.maoyongxin.myapplication.ui.widget.mylistview.AntGrideVIew;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.github.rockerhieu.emojicon.EmojiconEditText;


public class SquareFabuFragment extends Fragment {

    @BindView(R.id.edit_fabu_content)
    EmojiconEditText editFabuContent;
    @BindView(R.id.tv_fabu_address)
    TextView tvFabuAddress;
    @BindView(R.id.gv_fabu_picture)
    AntGrideVIew gvFabuPicture;
    Unbinder unbinder;

    SquareBaseActivity act;
    //拍照过程
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


    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_square_fabu, container, false);
        unbinder = ButterKnife.bind(this, view);

        initData();
        initView();
        initEvent();
        return view;
    }

    private void initView() {
        tvFabuAddress.setText(AppApplication.getMyCurrentLocation().getCityName());
    }

    private void showPhotoPicker(int index) {
        mPermissionWrite = act.getMyPermissifyManager().hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        mPermissionRead = act.getMyPermissifyManager().hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        mPermissionCamera = act.getMyPermissifyManager().hasPermission(Manifest.permission.CAMERA);
        String select = mCoverChoices.get(index);
        if (getString(R.string.take_photo).equals(select)) {
            if (mPermissionWrite && mPermissionCamera) {
                if (FileUtil.hasSdcard()) {
                    act.mProgressDialog.show();
                    Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, getAvatarUri(true));
                    startActivityForResult(cameraIntent, REQCODE_TAKE_PHOTO);
                }
            } else if (!mPermissionWrite) {
                act.getMyPermissifyManager().callWithPermission(act, REQCODE_PERMISSION_COVER + index, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            } else {
                act.getMyPermissifyManager().callWithPermission(act, REQCODE_PERMISSION_COVER + index, Manifest.permission.CAMERA);
            }
        } else if (getString(R.string.local_photo).equals(select)) {
            if (mPermissionRead) {
                Album.album(this) // 图片和视频混选。
                        .multipleChoice() // 多选模式，单选模式为：singleChoice()。
                        .requestCode(REQCODE_LOCAL_PHOTO) // 请求码，会在listener中返回。
                        .columnCount(3) // 页面列表的列数。
                        .selectCount(10 - uploadPicPath.size())  // 最多选择几张图片。
                        .camera(true) // 是否在Item中出现相机。
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
                                        act.mProgressDialog.dismiss();
                                        adapter = new GridNetPicAdapter(getActivity(), uploadPicPath);
                                        gvFabuPicture.setAdapter(adapter);
                                    }
                                }
                            }
                        }).onCancel(new Action<String>() {
                    @Override
                    public void onAction(int requestCode, @NonNull String result) {

                    }
                }).start();
            } else {
                act.getMyPermissifyManager().callWithPermission(act, REQCODE_PERMISSION_COVER + index, Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQCODE_TAKE_PHOTO) {
            if (resultCode == -1) {
                Uri uri = getAvatarUri(false);
                if (uri == null) {//有部分低端机型，保存相片到本地需要一定时间...
                    new Thread() {
                        @Override
                        public void run() {
                            int cnt = 10;
                            boolean success = false;
                            while (cnt > 0) {
                                SystemClock.sleep(1000);
                                cnt--;
                                Uri uriT = getAvatarUri(false);
                                if (uriT != null) {
                                    success = true;
                                    break;
                                }
                            }
                            final boolean finalSuccess = success;
                            AppApplication.getHandler().post(new Runnable() {
                                @Override
                                public void run() {
                                    act.mProgressDialog.dismiss();
                                    Uri uriH = getAvatarUri(false);
                                    if (finalSuccess)
                                        resizeImage(uriH);
                                    else
                                        NToast.shortToast(getActivity(), "处理缓慢，请重试");
                                }
                            });
                        }
                    }.start();
                } else {
                    act.mProgressDialog.dismiss();
                    resizeImage(uri);
                }
            } else
                act.mProgressDialog.dismiss();
        } else if (requestCode == REQCODE_RESIZE) {
            if (resultCode == -1) {
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
        Uri newUri = uri;
        String authority = getActivity().getPackageName() + ".provider";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (!ContentResolver.SCHEME_CONTENT.equals(uri.getScheme()))
                newUri = FileProvider.getUriForFile(getActivity(), authority, getAvatarFile());
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


    private void initEvent() {
        gvFabuPicture.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        act.setOnFabuClickListener(new SquareBaseActivity.OnFabuClickListener() {
            @Override
            public void doFabu() {

                if (editFabuContent.getText().toString().equals("") || editFabuContent.getText() == null) {
                    NToast.shortToast(getActivity(), "请先输入内容");
                } else {
                    act.mProgressDialog.show();
                    final List<File> files = new ArrayList<File>();
                    for (int i = 0; i < uploadPicPath.size()-1; i++) {
                        File file = new File(uploadPicPath.get(i).getFilePath());
                        files.add(file);
                    }
                    act.getMyHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            act.mProgressDialog.setMessage("上传中");
                            uploadImage(files);
                        }
                    });
                }
            }
        });
    }


    private void uploadImage(List<File> files) {
        HttpNetWorkUtils.uploadDongtai(getActivity(), AppApplication.getCurrentUserInfo().getUserId(),new String(Base64.encode(editFabuContent.getText().toString().getBytes(), Base64.DEFAULT)), files, act.getMyHandler());
    }

    private void initData() {
        mCoverChoices = new ArrayList<>();

        mCoverChoices.add(getString(R.string.local_photo));
        mCoverChoiceDialog = new SimpleChoiceDialog(getActivity(), mCoverChoices);
        act = (SquareBaseActivity) getActivity();
        uploadPicPath = new ArrayList<>();
        PictureEntity pictureEntity = new PictureEntity(1);
        pictureEntity.setImgResource(R.mipmap.picture_default);
        uploadPicPath.add(pictureEntity);
        adapter = new GridNetPicAdapter(getActivity(), uploadPicPath);
        gvFabuPicture.setAdapter(adapter);
        mCoverChoiceDialog = new SimpleChoiceDialog(getActivity(), mCoverChoices);
    }
}
