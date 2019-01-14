package com.maoyongxin.myapplication.ui.square;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.PictureEntity;
import com.maoyongxin.myapplication.http.second.HttpNetWorkUtils;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.server.utils.FileUtil;
import com.maoyongxin.myapplication.server.utils.NToast;
import com.maoyongxin.myapplication.server.widget.ClearWriteEditText;
import com.maoyongxin.myapplication.server.widget.SimpleChoiceDialog;
import com.maoyongxin.myapplication.ui.adapter.GridNetPicAdapter;
import com.maoyongxin.myapplication.ui.widget.mylistview.AntGrideVIew;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.github.rockerhieu.emojicon.EmojiconEditText;


public class SquareVideoFragment extends Fragment {

    @BindView(R.id.edit_fabu_content)
    EmojiconEditText editFabuContent;
    @BindView(R.id.tv_fabu_address)
    TextView tvFabuAddress;
    @BindView(R.id.gv_fabu_picture)
    AntGrideVIew gvFabuPicture;

    @BindView(R.id.fb_video_btn)
    RelativeLayout fb_video_btn;

    @BindView(R.id.fb_video_img)
    ImageView fb_video_img;

    Unbinder unbinder;

    SquareVideoActivity act;
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
    //    private List<PictureEntity> uploadPicPath;
    private String picName;

    private ArrayList<String> mCoverChoices;
    private SimpleChoiceDialog mCoverChoiceDialog;

    private boolean mPermissionWrite;
    private boolean mPermissionRead;
    private File mTmpFile;
    public static final int ADDRESULTCODE = 111;
    private String imgUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_square_fabu, container, false);
        unbinder = ButterKnife.bind(this, view);
        initData();
        initView();
        initEvent();
        return view;
    }

    public static SquareVideoFragment newInstance(String file) {

        Bundle args = new Bundle();
        args.putString("file", file);
        SquareVideoFragment fragment = new SquareVideoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void initView() {
        tvFabuAddress.setText(AppApplication.getMyCurrentLocation().getCityName());
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

        act.setOnFabuClickListener(new SquareVideoActivity.OnFabuClickListener() {
            @Override
            public void doFabu() {

                if (editFabuContent.getText().toString().equals("") || editFabuContent.getText() == null) {
                    NToast.shortToast(getActivity(), "请先输入内容");
                } else {
                    act.mProgressDialog.show();
                    final List<File> files = new ArrayList<File>();

                        File f = new File(file);
                        files.add(f);

                    act.getMyHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            act.mProgressDialog.setMessage("视频上传中");
                            uploadImage(files);
                        }
                    });
                }
            }
        });
    }


    private void uploadImage(List<File> files) {
        HttpNetWorkUtils.uploadDongtaiVideo(getActivity(), AppApplication.getCurrentUserInfo().getUserId(), new String(Base64.encode(editFabuContent.getText().toString().getBytes(), Base64.DEFAULT)), files, act.getMyHandler());
    }
    String file;
    private void initData() {
        fb_video_btn.setVisibility(View.VISIBLE);
        gvFabuPicture.setVisibility(View.GONE);
        mCoverChoices = new ArrayList<>();

        mCoverChoices.add(getString(R.string.local_photo));
        mCoverChoiceDialog = new SimpleChoiceDialog(getActivity(), mCoverChoices);
        act = (SquareVideoActivity) getActivity();
        file = getArguments().getString("file");
        MyAsyncTask myAsyncTack = new MyAsyncTask(fb_video_img);
        myAsyncTack.execute(file);
        fb_video_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), VideoActivity.class);
                intent.putExtra("path", file);
                startActivity(intent);
            }
        });
        mCoverChoiceDialog = new SimpleChoiceDialog(getActivity(), mCoverChoices);
    }

    class MyAsyncTask extends AsyncTask<String, Integer, Bitmap> {
        private View v;

        public MyAsyncTask(View v) {
            this.v = v;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            return getLocalVideoThumbnail(strings[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            BitmapDrawable drawable = new BitmapDrawable(bitmap);
            v.setBackground(drawable);
        }
    }

    //判断文件是否存在
    public boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

        return true;
    }

    /**
     * 获取本地视频的第一帧
     *
     * @param filePath
     * @return
     */
    public Bitmap getLocalVideoThumbnail(String filePath) {
        String imgpath = filePath.replace(".mp4", ".png");
        Bitmap bitmap = null;
        if (fileIsExists(imgpath)) {
            bitmap = BitmapFactory.decodeFile(imgpath);
            return bitmap;
        }


        //MediaMetadataRetriever 是android中定义好的一个类，提供了统一
        //的接口，用于从输入的媒体文件中取得帧和元数据；
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            //根据文件路径获取缩略图
            retriever.setDataSource(filePath);

            //获得第一帧图片
            bitmap = retriever.getFrameAtTime();

//            File file = new File(imgpath);
////            Log.e("name",file.getName());
//            saveCropPic(bitmap, file);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            retriever.release();
        }
        return bitmap;
    }
}
