package com.maoyongxin.myapplication.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.ScaleAnimation;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.http.second.HttpNetWorkUtils;
import com.maoyongxin.myapplication.http.second.XGsonUtil;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.myapp.AppTitleBarActivity;
import com.maoyongxin.myapplication.server.utils.FileUtil;
import com.maoyongxin.myapplication.server.widget.ClearWriteEditText;
import com.maoyongxin.myapplication.server.widget.SimpleChoiceDialog;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.maoyongxin.myapplication.R.id.map_addressChoice;

public class CreatCommunityActivity extends AppTitleBarActivity {

    @BindView(map_addressChoice)
    MapView mapAddressChoice;
    @BindView(R.id.edit_communityName)
    ClearWriteEditText editCommunityName;
    @BindView(R.id.edit_communityNote)
    ClearWriteEditText editCommunityNote;
    @BindView(R.id.img_communityCreate)
    ImageView imgCommunityCreate;
    @BindView(R.id.btn_doCreate)
    TextView btnDoCreate;
    @BindView(R.id.activity_creat_community)
    LinearLayout activityCreatCommunity;
    @BindView(R.id.tv_communityAddress)
    TextView tvCommunityAddress;
    @BindView(R.id.line_map)
    LinearLayout lineMap;
    @BindView(R.id.icon_back)
    ImageView iconBack;
    private AMap aMap;
    private double myLatitude, myLngtitude;
    private String myAdCode;
    private String myAddress;
    private String mySimpleAddress;
    private String mCurrentCity;
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
    private ProgressDialog mProgressDialog;

    @Override
    protected int bindLayout() {
        return R.layout.activity_creat_community;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//白底黑字+沉浸式导航栏

        mProgressDialog = new ProgressDialog(this, android.R.style.Theme_DeviceDefault_Dialog);
        mProgressDialog.setCancelable(true);
        setMapHeight(3);
        mCoverChoiceDialog = new SimpleChoiceDialog(this, mCoverChoices);
        mCoverChoiceDialog.setOnChoiceListener(new SimpleChoiceDialog.OnChoiceListener() {
            @Override
            public void onChoice(int index) {
                showPhotoPicker(index);
            }
        });
        mapAddressChoice.onCreate(savedInstanceState);
        if (aMap == null) {

            aMap = mapAddressChoice.getMap();
            aMap.moveCamera(CameraUpdateFactory.zoomTo(13));
        }
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(600000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                myLatitude = location.getLatitude();
                myLngtitude = location.getLongitude();
                GeoCoderSearch();
            }
        });
        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                myLatitude = latLng.latitude;
                myLngtitude = latLng.longitude;
                GeoCoderSearch();
                aMap.clear();
                addPoiMarker(myLatitude + "", myLngtitude + "");
            }
        });
    }

    private void setMapHeight(int bei) {
        WindowManager wm = this.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) lineMap.getLayoutParams();
        lp.height = height / bei;
        lp.width = width;
        lineMap.setLayoutParams(lp);
    }

    private void GeoCoderSearch() {
        GeocodeSearch geocoderSearch = new GeocodeSearch(getActivity());
        geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onGeocodeSearched(GeocodeResult result, int rCode) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
                String formatAddress = result.getRegeocodeAddress().getFormatAddress();
                myAddress = formatAddress;
                mySimpleAddress = result.getRegeocodeAddress().getFormatAddress().toString();
                String city = result.getRegeocodeAddress().getCity();
                mCurrentCity = city;
                myAdCode = result.getRegeocodeAddress().getAdCode();
                if (mySimpleAddress == null || mySimpleAddress.equals("")) {
                    mySimpleAddress = "此地暂无地理小区信息";
                }
                tvCommunityAddress.setText(mySimpleAddress);
            }
        });
        LatLonPoint lp = new LatLonPoint(myLatitude, myLngtitude);
        RegeocodeQuery query = new RegeocodeQuery(lp, 200, GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query);
    }

    private void addPoiMarker(String lat, String lng) {
        MarkerOptions makerOpt = getPoiMarkerOptions(lat, lng);
        Marker maker = aMap.addMarker(makerOpt);
        Animation scAnimation = new ScaleAnimation(maker.getRotateAngle(), 0.5f, 0.5f, 0.5f);
        long duration = 500L;
        scAnimation.setDuration(duration);
        scAnimation.setInterpolator(new LinearInterpolator());
        maker.setAnimation(scAnimation);
        maker.startAnimation();
    }

    private MarkerOptions getPoiMarkerOptions(String lat, String lng) {
        double mlat = Double.parseDouble(lat);
        double mlng = Double.parseDouble(lng);
        LatLng ll = new LatLng(mlat, mlng);
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(ll);
        markerOption.draggable(true);//设置Marker可拖动
        markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(getResources(), R.mipmap.icon_poi_add)));
        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.setFlat(false);//设置marker平贴地图效果
        return markerOption;
    }

    @Override
    protected void initData() {
        super.initData();
        mCoverChoices = new ArrayList<>();
//        mCoverChoices.add(getString(R.string.take_photo));
        mCoverChoices.add(getString(R.string.local_photo));
    }

    @Override
    protected void handlerPassMsg(int target, int target1, Object obj) {
        switch (target) {
            case HttpNetWorkUtils.CREATE_COMMUNITY:
                mProgressDialog.dismiss();
                if (null != obj && !obj.toString().equals("")) {
                    try {
                        JsonObject jsonObject = XGsonUtil.getJsonObject(obj.toString());
                        int code = XGsonUtil.getJsonInt(jsonObject, "code");
                        if (code == 200) {
                            showToastShort("请求成功！请等待后台审核通过");
                            finish();
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
                        .camera(false) // 是否在Item中出现相机。
                        .onResult(new Action<ArrayList<AlbumFile>>() {
                            @Override
                            public void onAction(int requestCode, @NonNull ArrayList<AlbumFile> result) {
                                myPicturePath = result.get(0).getPath();
                                picFile = new ArrayList<>();
                                picFile.add(new File(myPicturePath));
                                Glide.with(CreatCommunityActivity.this).load(picFile.get(0)).into(imgCommunityCreate);
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
    protected void initEvent() {
        super.initEvent();
        iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        imgCommunityCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCoverChoiceDialog.show();
            }
        });
        btnDoCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (picFile == null) {
                    showToastShort("请选择描述图片");
                } else {
                    if (editCommunityName.getText().toString() != null && !editCommunityName.getText().toString().equals("")) {
                        if (editCommunityNote.getText().toString() != null && !editCommunityNote.getText().toString().equals("")) {
                            doUploadCreate(picFile);
                        } else {
                            showToastShort("请输入社区备注");
                        }
                    } else {
                        showToastShort("请输入社区名字");
                    }
                }
            }
        });
    }

    private void doUploadCreate(final List<File> files) {
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
        HttpNetWorkUtils.uploadCreateCommunity(this, editCommunityName.getText().toString(), editCommunityNote.getText().toString(), myAdCode, myAddress, mySimpleAddress, myLngtitude + "", myLatitude + "", AppApplication.getCurrentUserInfo().getUserId(), files, myHandler);
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
