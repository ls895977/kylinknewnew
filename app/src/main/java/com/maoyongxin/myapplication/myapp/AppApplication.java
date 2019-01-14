package com.maoyongxin.myapplication.myapp;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.jky.baselibrary.LibConfig;
import com.jky.baselibrary.base.BaseApplication;
import com.jky.baselibrary.util.image.glide.GlideCircleTransform;
import com.maoyongxin.myapplication.entity.LocationInfo;
import com.maoyongxin.myapplication.entity.UserInfoService;
import com.maoyongxin.myapplication.ui.GroupListActivity;
import com.maoyongxin.myapplication.ui.news.Inject;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.File;
import java.util.List;

import cn.jiguang.share.android.api.JShareInterface;
import cn.jiguang.share.android.api.PlatformConfig;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;


/**
 * Created by dingke on 2017/8/3.
 */

public class AppApplication extends BaseApplication {
    public static final String WEIXIN_APP_ID = "wxb0657547b9176773";
    private static final String WEIXIN_APP_SECRET = "e1bf85e8ddf5550e9b623ba6082dff95";
    private static final String QQ_APP_ID = "";
    private static final String QQ_APP_KEY = "";
    private static final String WEIBO_APP_KEY = "";
    private static final String WEIBO_APP_SECRET = "";
    private static final String WEIBO_REDIRECT_URL = "";

    private static AppApplication sApplication;
    private static GlideCircleTransform sCircleTransform;
    private static UserInfoService sCurrentUserInfo;
    private static LocationInfo myCurrentLocation;
    private static String myPassword;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    private static OnAddressGetListener addressGetListener;
    public static final int NORESULT = 0;
    public static final int ISREGISTER_OK = 1;
    public static final int POISEARCH_OK = 2;
    public static final int POISEARCH_LIST_OK = 3;
    public static final int UPLOAD_INTERESTOK = 4;
    public static final int GETMY_USERINFO = 5;
    public static final int GETMY_USERINFO_OK = 6;
    public static final int NOCHANGE_USERINFO = 7;
    public static String logNum;
    public static String logPsw;
    public static String updatemsg;
    public static String mDownloadUrl;

    public static void setmDownloadUrl(String DownloadUrl) {
        AppApplication.mDownloadUrl = DownloadUrl;
    }

    public static String getmDownloadUrl() {
        return mDownloadUrl;
    }


    public static void setUpdatemsg(String UpdateMsg) {
        AppApplication.updatemsg = UpdateMsg;
    }

    public static String getUpdatemsg() {
        return updatemsg;
    }


    public static String getLogNum() {
        return logNum;
    }

    public static void setLogNum(String logNum) {
        AppApplication.logNum = logNum;
    }

    public static String getLogPsw() {
        return logPsw;
    }

    public static void setLogPsw(String logPsw) {
        AppApplication.logPsw = logPsw;
    }

    public static String getMyPassword() {
        return myPassword;
    }

    public static void setMyPassword(String myPassword) {
        AppApplication.myPassword = myPassword;
    }

    public static void setOnAddressGetListener(OnAddressGetListener onAddressGetListener) {
        AppApplication.addressGetListener = onAddressGetListener;
    }

    public interface OnAddressGetListener {
        void getAddress(boolean isGetAddress);
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        JPushInterface.setDebugMode(true);//正式版的时候设置false，关闭调试
//        JPushInterface.init(this);
        Inject.injectContext(this);
        initUniversalImageLoader();
        initJiguangShare();
        registerToWX();
    }

    public static IWXAPI mWxApi;

    private void registerToWX() {
        //第二个参数是指你应用在微信开放平台上的AppID
        mWxApi = WXAPIFactory.createWXAPI(this, WEIXIN_APP_ID, false);
        // 将该app注册到微信
        mWxApi.registerApp(WEIXIN_APP_ID);
    }

    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    //可在中解析amapLocation获取相应内容。
                    myCurrentLocation = new LocationInfo();
                    myCurrentLocation.setAddress(aMapLocation.getAddress());
                    myCurrentLocation.setLatitude(aMapLocation.getLatitude() + "");
                    myCurrentLocation.setLngtitude(aMapLocation.getLongitude() + "");
                    myCurrentLocation.setCityName(aMapLocation.getCity());
                    myCurrentLocation.setAdCode(aMapLocation.getAdCode());
                    setMyCurrentLocation(myCurrentLocation);
                    addressGetListener.getAddress(true);
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.d("locErro", "\"AmapError\", \"location Error, ErrCode:\" + aMapLocation.getErrorCode() + \", errInfo:\" + aMapLocation.getErrorInfo(): ");
                    addressGetListener.getAddress(false);
                }
            }
        }
    };


    public static LocationInfo getMyCurrentLocation() {
        if (myCurrentLocation == null) {
            return null;
        } else {
            return myCurrentLocation;
        }
    }

    public static void setMyCurrentLocation(LocationInfo myCurrentLocation) {
        AppApplication.myCurrentLocation = myCurrentLocation;
    }


    @Override
    protected void appStart() {
        //初始化单例
        sApplication = this;
        //初始化环境
        AppConfig.initEnvironment();

        //Android N以上版本拍照相册Uri问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }


        //初始化Lib模组
        LibConfig.init(this, AppConfig.DEBUG);
        //初始化xUtils
        org.xutils.x.Ext.init(this);
        org.xutils.x.Ext.setDebug(AppConfig.DEBUG);
        sCircleTransform = new GlideCircleTransform(this);
        RongIM.init(getApplicationContext());
        initLocationInfo();
    }


    private void initLocationInfo() {
        //默认定位北京
        myCurrentLocation = new LocationInfo();
        myCurrentLocation.setAddress("北京市");
        myCurrentLocation.setLatitude("39.9088600000");
        myCurrentLocation.setLngtitude("116.3973900000");
        myCurrentLocation.setCityName("北京市");
        myCurrentLocation.setAdCode(110000 + "");
        setMyCurrentLocation(myCurrentLocation);
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        initLocationOption();
        //启动定位
        mLocationClient.startLocation();
    }

    //
//
    private void initLocationOption() {
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果：
        // 该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //获取最近3s内精度最高的一次定位结果：
        // 设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        //mLocationOption.setOnceLocationLatest(true);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(1000);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否允许模拟位置,默认为true，允许模拟位置
        // mLocationOption.setMockEnable(true);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        // mLocationOption.setHttpTimeOut(20000);
        //关闭缓存机制
        //mLocationOption.setLocationCacheEnable(false);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
    }

    public static GlideCircleTransform getCircleTransform() {
        return sCircleTransform;
    }

    public static AppApplication getInstance() {
        return sApplication;
    }

    public static void setCurrentUserInfo(UserInfoService currentUserInfo) {
        AppApplication.sCurrentUserInfo = currentUserInfo;
    }

    public static UserInfoService getCurrentUserInfo() {
        return sCurrentUserInfo;
    }

    private void initUniversalImageLoader() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(new ColorDrawable(Color.parseColor("#f0f0f0")))
                .resetViewBeforeLoading(true)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        int memClass = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE))
                .getMemoryClass();
        int memCacheSize = 1024 * 1024 * memClass / 8;

        File cacheDir = new File(Environment.getExternalStorageDirectory().getPath() + "/jiecao/cache");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .threadPoolSize(3)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .memoryCache(new UsingFreqLimitedMemoryCache(memCacheSize))
                .memoryCacheSize(memCacheSize)
                .diskCacheSize(50 * 1024 * 1024)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .diskCache(new UnlimitedDiskCache(cacheDir))
                .imageDownloader(new BaseImageDownloader(this, 5 * 1000, 30 * 1000))
                .defaultDisplayImageOptions(options)
                .build();
        ImageLoader.getInstance().init(config);
    }

    private void initJiguangShare() {
        JShareInterface.setDebugMode(true);
        PlatformConfig platformConfig = new PlatformConfig()
//                .setQQ(QQ_APP_ID, QQ_APP_KEY)
                .setWechat(WEIXIN_APP_ID, WEIXIN_APP_SECRET);
//                .setSinaWeibo(WEIBO_APP_KEY, WEIXIN_APP_SECRET, WEIBO_REDIRECT_URL);
        JShareInterface.init(this, platformConfig);

        List<String> successPlatform = JShareInterface.getPlatformList();
        for (String s : successPlatform) {
            //Log.e("---", "===>>" + s);
        }
    }


}
