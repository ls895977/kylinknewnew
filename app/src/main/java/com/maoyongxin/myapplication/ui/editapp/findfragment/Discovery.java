package com.maoyongxin.myapplication.ui.editapp.findfragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.TextPaint;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.ScaleAnimation;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.jky.baselibrary.util.image.glide.GlideCircleTransform;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.LocationInfo;
import com.maoyongxin.myapplication.entity.PoiInfoBean;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.request.ReqCommunity;
import com.maoyongxin.myapplication.http.request.ReqNearByUser;
import com.maoyongxin.myapplication.http.request.ReqPoiList;
import com.maoyongxin.myapplication.http.response.CommunityListResponse;
import com.maoyongxin.myapplication.http.response.NearbyUserResponse;
import com.maoyongxin.myapplication.http.response.PoiTypeResponse;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.server.utils.NToast;
import com.maoyongxin.myapplication.server.widget.ClearWriteEditText;
import com.maoyongxin.myapplication.server.widget.SelectableRoundedImageView;
import com.maoyongxin.myapplication.tool.NaviActivity;
import com.maoyongxin.myapplication.tool.ToastUtil;
import com.maoyongxin.myapplication.tool.VipNaviActivity;
import com.maoyongxin.myapplication.ui.MyPayActivity;
import com.maoyongxin.myapplication.ui.StrangerDetailActivity;
import com.maoyongxin.myapplication.ui.adapter.PoiListAdapter;
import com.maoyongxin.myapplication.ui.widget.CircularAnimUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.view.View.GONE;


/**
 * Created by Administrator on 2017/11/23.
 */
//1、vip 2、普通 3、会员
public class Discovery extends Fragment {

    Unbinder unbinder;
    private TextureMapView map;
    private AMap aMap;
    private LinearLayout line_search, line_poiType;
    private Button btn_showRoad;
    private ListView list_poiResult;

    private ArrayList<PoiInfoBean> infoBeen;
    private ArrayList<PoiInfoBean> vipinfoBeen;
    private boolean viewDerectionIsUp = true;
    private ProgressDialog mProgressDialog;
    private double myLatitude, myLngtitude;
    private String myAdCode;
    private String myAddress;
    private ClearWriteEditText edit_poi_search;
    private PoiSearch poiSearch;
    private PoiSearch.Query query;
    private PoiListAdapter adapter;
    private String mCurrentCity;
    private double poiLat;
    private double poiLng;
    private TextView tv_poiType1, tv_poiType2, tv_poiType3,top_text;
    private String poiType;
    private ImageView img_doSearch;
    private int paddingCenter;
    private int paddingTop;
    private int paddingBottom;
    private int paddingNum = 500;
    private boolean isSearChedPoi = true;
    private Spinner sp_hangye;
    private Boolean isUseable=false;
    private String userid;
    private String searchableApi="http://st.3dgogo.com/index/user/is_get_enterprise_num.html?uid=";
    private CheckBox cus_company,bison_vip,vip_company;
    private final double EARTH_RADIUS = 6378137.0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discovery, null);

        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//透明导航栏
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//白底黑字+沉浸式导航栏


        map = (TextureMapView) view.findViewById(R.id.map);
        line_search = (LinearLayout) view.findViewById(R.id.line_search);
        line_poiType = (LinearLayout) view.findViewById(R.id.line_poiType);
        list_poiResult = (ListView) view.findViewById(R.id.list_poiResult);
        btn_showRoad = (Button) view.findViewById(R.id.btn_showRoad);
        img_doSearch = (ImageView) view.findViewById(R.id.img_doSearch);
        tv_poiType1 = (TextView) view.findViewById(R.id.tv_poiType1);
        top_text=(TextView)view.findViewById(R.id.top_text);
        tv_poiType2 = (TextView) view.findViewById(R.id.tv_poiType2);
        tv_poiType3 = (TextView) view.findViewById(R.id.tv_poiType3);
        sp_hangye = (Spinner) view.findViewById(R.id.hangye_spinner);
        poiType = "企业";
        edit_poi_search = (ClearWriteEditText) view.findViewById(R.id.edit_poi_search);
        cus_company=(CheckBox)view.findViewById(R.id.cus_company);
        bison_vip=(CheckBox)view.findViewById(R.id.bison_vip);
        vip_company=(CheckBox)view.findViewById(R.id.vip_company);



        ViewTreeObserver vto = map.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                map.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                paddingNum = map.getHeight() / 2;
            }
        });

        sp_hangye.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] hangye = getResources().getStringArray(R.array.hangy);
                edit_poi_search.setText(hangye[position]);
                edit_poi_search.setHint("输入关键词查找企业");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        map.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = map.getMap();
        }
        initdata();
        initView();
        initEvent();
        //getNearbyCommunityList();
        // getStrangerList();

        getPoiList();


        unbinder = ButterKnife.bind(this, view);
        edit_poi_search.setHint("找客户、上彼信");
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void getPoiList() {
        ReqPoiList.getPoiList(getActivity(), getTag(), new EntityCallBack<PoiTypeResponse>() {
            @Override
            public Class<PoiTypeResponse> getEntityClass() {
                return PoiTypeResponse.class;
            }

            @Override
            public void onSuccess(final PoiTypeResponse resp) {
                if (resp.is200()) {
//
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

    private void addMarker(String lat, String lng, String myAddress, String snippet, boolean isBig) {
        mProgressDialog.dismiss();
        MarkerOptions makerOpt = getMarkerOptions(lat, lng, myAddress, snippet, isBig);
        Marker maker = aMap.addMarker(makerOpt);
        maker.setInfoWindowEnable(false);
        if (isBig) {
            Animation scAnimation = new ScaleAnimation(maker.getRotateAngle(), 0.5f, 0.5f, 0.5f);
            long duration = 10L;
            scAnimation.setDuration(duration);
            scAnimation.setInterpolator(new LinearInterpolator());

        } else {

        }
    }

    private void addPoiMarker(String lat, String lng) {
        MarkerOptions makerOpt = getPoiMarkerOptions(lat, lng);
        Marker maker = aMap.addMarker(makerOpt);
        maker.setInfoWindowEnable(false);
        Animation scAnimation = new ScaleAnimation(maker.getRotateAngle(), 0.5f, 0.5f, 0.5f);
        long duration = 500L;
        scAnimation.setDuration(duration);
        scAnimation.setInterpolator(new LinearInterpolator());
        maker.setAnimation(scAnimation);
        maker.startAnimation();
    }

    private void addCommunityInfoMarker(String lat, String lng, String vipname) {
        MarkerOptions makerOpt = getCommunityInfoOptions(lat, lng, AppApplication.getMyCurrentLocation().getCityName(), vipname);
        Marker maker = aMap.addMarker(makerOpt);
        maker.setInfoWindowEnable(false);
        Animation scAnimation = new ScaleAnimation(maker.getRotateAngle(), 0.5f, 0.5f, 0.5f);
        long duration = 500L;
        scAnimation.setDuration(duration);
        scAnimation.setInterpolator(new LinearInterpolator());
        maker.setFlat(false);

        maker.setSnippet(vipname);
        maker.setIcon(BitmapDescriptorFactory.fromView(getvipView(vipname)));



    }
    private void initdata()
    {
        userid=AppApplication.getCurrentUserInfo().getUserId().toString();
        getSearchstate();
    }
    private void getSearchstate()
    {


//get自身服务器数据

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(searchableApi+userid).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    try {

                        JSONObject jsonObject = new JSONObject(responseData);



                        if(jsonObject.getInt("code")==200)
                        {
                            isUseable=true;

                        }
                        else
                        {
                            isUseable=false;
                        }




                    } catch (Exception e) {
                        Toast.makeText(getActivity(),"状态校验失败，由彼信免费提供查询",Toast.LENGTH_SHORT).show();
                        isUseable=true;
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    Toast.makeText(getActivity(),"状态校验失败，由彼信免费提供查询",Toast.LENGTH_SHORT).show();
                    isSearChedPoi=true;
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private String parse_Value(JSONObject data, String value) {
        String com_value = "";
        if (data.has(value)) {
            try {
                com_value = data.getString(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return com_value;
    }


    public void initView() {

        Toast.makeText(getActivity(),"滑动地图查看周边企业",Toast.LENGTH_SHORT).show();

        getSearchstate();

        mProgressDialog = new ProgressDialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setMessage(getString(R.string.locating));
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(600000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.showBuildings(false);
        aMap.getUiSettings().setZoomControlsEnabled(true);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.moveCamera(CameraUpdateFactory.zoomTo(16));
        aMap.getUiSettings().setScaleControlsEnabled(true);
        aMap.getUiSettings().setZoomControlsEnabled(true);
        aMap.getUiSettings().setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);



        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {

                mProgressDialog.dismiss();
                myLatitude = location.getLatitude();
                myLngtitude = location.getLongitude();
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
                        String city = result.getRegeocodeAddress().getCity();
                        mCurrentCity = city;
                        myAdCode = result.getRegeocodeAddress().getAdCode();
                    }
                });
                LatLonPoint lp = new LatLonPoint(myLatitude, myLngtitude);
                RegeocodeQuery query = new RegeocodeQuery(lp, 200, GeocodeSearch.AMAP);
                geocoderSearch.getFromLocationAsyn(query);
                LocationInfo locationInfo = new LocationInfo();
                locationInfo.setLatitude(myLatitude + "");
                locationInfo.setLngtitude(myLngtitude + "");
                locationInfo.setCityName(mCurrentCity);
                locationInfo.setAdCode(myAdCode);
                AppApplication.setMyCurrentLocation(locationInfo);
            }
        });


        poiLat = Double.parseDouble(AppApplication.getMyCurrentLocation().getLatitude());
        poiLng = Double.parseDouble(AppApplication.getMyCurrentLocation().getLongtitude());

        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker)
            {
                if (marker.isInfoWindowEnable()) {

                    if (marker.isFlat())
                    {
                        if (marker.isInfoWindowShown()) {
                            marker.hideInfoWindow();
                            return true;
                        } else {
                            marker.showInfoWindow();
                            return true;
                        }
                    }
                    else {
                        Intent intent = new Intent(getActivity(), StrangerDetailActivity.class);
                        intent.putExtra("personId", Integer.parseInt(marker.getSnippet()));
                        startActivity(intent);
                        return true;
                    }
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(marker.getSnippet()).setMessage("是否查看：" + marker.getTitle() + "\n" + "的数据")
                            .setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startNavi(marker);
                        }
                    }).show();
                    return true;
                }


            }
        });
        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                poiLat = cameraPosition.target.latitude;
                poiLng = cameraPosition.target.longitude;
                getallpoi();

            }
        });
        aMap.setOnMapTouchListener(new AMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        getView().getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        getView().getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
            }
        });
        aMap.setInfoWindowAdapter(new AMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                // TODO Auto-generated method stub
                View infoWindow = getActivity().getLayoutInflater().inflate(R.layout.infowindow_marker_community, null);//display为自定义layout文件
                TextView tv_infoCommunityName = (TextView) infoWindow.findViewById(R.id.tv_infoCommunityName);
                tv_infoCommunityName.setText(marker.getSnippet());
                SelectableRoundedImageView img_communityMarker = (SelectableRoundedImageView) infoWindow.findViewById(R.id.img_communityMarker);
                Glide.with(getActivity()).load(R.mipmap.community_icon_default).into(img_communityMarker);
                return infoWindow;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });

        aMap.moveCamera(CameraUpdateFactory.zoomTo(20));
    }

    private void startNavi(Marker marker) {

        Intent intent = new Intent(getActivity(), NaviActivity.class);
        intent.putExtra("poiName", marker.getTitle());
        intent.putExtra("poiAddress", marker.getSnippet());
        intent.putExtra("myLat", myLatitude + "");
        intent.putExtra("myLng", myLngtitude + "");
        intent.putExtra("myAddress", myAddress);
        intent.putExtra("poiLat", marker.getPosition().latitude + "");
        intent.putExtra("poiLng", marker.getPosition().longitude + "");
        intent.putExtra("vipstatu",isUseable);
        CircularAnimUtil.startActivity(getActivity(), intent, btn_showRoad, R.color.blue_title);


    }

    private void startNaviFromList(String poiName, String snippet, String latitude, String lngtitude,String targetid,int targettype) {

        switch (targettype)
        {
            case 1:
                Intent vipintent = new Intent(getActivity(), VipNaviActivity.class);
                vipintent.putExtra("targetid", targetid);
                vipintent.putExtra("poiName", poiName);
                vipintent.putExtra("myAddress", myAddress);
                vipintent.putExtra("vipstatu",isUseable);
                vipintent.putExtra("poiAddress", snippet);


                CircularAnimUtil.startActivity(getActivity(), vipintent, btn_showRoad, R.color.blue_title);
                break;
            case 2:
                Intent intent = new Intent(getActivity(), NaviActivity.class);
                intent.putExtra("poiName", snippet);
                intent.putExtra("poiAddress", poiName);
                intent.putExtra("myLat", myLatitude + "");
                intent.putExtra("myLng", myLngtitude + "");
                intent.putExtra("myAddress", myAddress);
                intent.putExtra("poiLat", latitude + "");
                intent.putExtra("poiLng", lngtitude + "");
                intent.putExtra("vipstatu",isUseable);
                CircularAnimUtil.startActivity(getActivity(), intent, btn_showRoad, R.color.blue_title);
                break;


        }





    }

    private MarkerOptions getMarkerOptions(String lat, String lng, String myAddress, String snippet, boolean isBig) {
        double mlat = Double.parseDouble(lat);
        double mlng = Double.parseDouble(lng);

        LatLng ll = new LatLng(mlat, mlng);
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(ll);
        markerOption.title(myAddress).snippet(snippet);
        markerOption.setFlat(false);
        markerOption.draggable(false);//设置Marker可拖动
        if (isBig) {
            markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                    .decodeResource(getResources(), R.mipmap.icon_point_restraunt_poi_big)));
            // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        } else {
            markerOption.icon(BitmapDescriptorFactory.fromView(getMyView(snippet)));
        }
        return markerOption;
    }

    private MarkerOptions getCommunityInfoOptions(String lat, String lng, String city, String snippet) {
        double mlat = Double.parseDouble(lat);
        double mlng = Double.parseDouble(lng);
        LatLng ll = new LatLng(mlat, mlng);
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(ll);
        markerOption.title(city).snippet(snippet);
        markerOption.draggable(false);//设置Marker可拖动
        BitmapDescriptorFactory.fromView(getMyView("讨论组"));
        markerOption.setFlat(true);//设置marker平贴地图效果
        return markerOption;
    }
    protected View getMyView(String pm_val) {
        View view=getLayoutInflater().inflate(R.layout.mymaker, null);
        TextView tv_val=(TextView) view.findViewById(R.id.marker_tv_val);
        tv_val.setText(pm_val);
        return view;
    }
    protected View getvipView(String pm_val) {
        View view=getLayoutInflater().inflate(R.layout.vipmaker, null);
        TextView tv_val=(TextView) view.findViewById(R.id.marker_tv_val);
        tv_val.setText(pm_val);
        return view;
    }


    private MarkerOptions getPoiMarkerOptions(String lat, String lng) {
        double mlat = Double.parseDouble(lat);
        double mlng = Double.parseDouble(lng);
        LatLng ll = new LatLng(mlat, mlng);
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(ll);
        markerOption.setFlat(false);
        markerOption.draggable(true);//设置Marker可拖动
        markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(getResources(), R.mipmap.icon_poi_add)));
        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        return markerOption;
    }

    private void getStrangerList() {
        ReqNearByUser.getUsers(getActivity(), getTag(), AppApplication.getCurrentUserInfo().getUserId(), poiLat + "", poiLng + "", new EntityCallBack<NearbyUserResponse>() {
            @Override
            public Class<NearbyUserResponse> getEntityClass() {
                return NearbyUserResponse.class;
            }

            @Override
            public void onSuccess(NearbyUserResponse resp) {
                if (resp.is200()) {
                    for (int i = 0; i < resp.obj.getUserlist().size(); i++) {
                        drawMarkers(new LatLng(resp.obj.getUserlist().get(i).getLatitude(), resp.obj.getUserlist().get(i).getLongitude()), resp.obj.getUserlist().get(i).getHeadImg(), resp.obj.getUserlist().get(i).getUserId() + "");
                    }
                } else {
                    NToast.shortToast(getActivity(), resp.msg);
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
     * 绘制系统默认的1种marker背景图片
     */
    public void drawMarkers(final LatLng latlng,final String userhead, final String id) {
        //获取自定义View
        final View view = getActivity().getLayoutInflater().inflate(R.layout.layout_user_marker, null);
        final ImageView iv_user = (ImageView) view.findViewById(R.id.img_myUserHeadIcon);
        if (!userhead.equals("") && userhead != null) {
            Glide.with(getActivity()).load(userhead).transform(new GlideCircleTransform(getActivity())).into(new SimpleTarget<GlideDrawable>() {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    iv_user.setImageDrawable(resource);

                    Marker marker = aMap.addMarker(new MarkerOptions().position(latlng)
                            .icon(BitmapDescriptorFactory.fromView(view)).draggable(true));
                    marker.setInfoWindowEnable(true);
                    marker.setSnippet(id);


                }
            });
        }

    }

    private void initEvent() {

        edit_poi_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER) {
                    if (edit_poi_search.getText().toString().equals("") || edit_poi_search.getText().toString() == null) {
                        aMap.clear();

                    } else {
                        getallpoi();
                    }
                }
                return false;
            }
        });
        img_doSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_poi_search.getText().toString().equals("") || edit_poi_search.getText().toString() == null) {

                    list_poiResult.setVisibility(GONE);
                } else {


                    if(infoBeen.size()<=15)
                    {
                        aMap.moveCamera(CameraUpdateFactory.zoomTo(18));
                    }
                    else if(15<infoBeen.size()&&infoBeen.size()<25)
                    {
                        aMap.moveCamera(CameraUpdateFactory.zoomTo(16));
                    }
                    else
                    {
                        aMap.moveCamera(CameraUpdateFactory.zoomTo(14));
                    }
                    getallpoi();


                }
            }
        });
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        map.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        map.onSaveInstanceState(outState);
    }

    private void searchPoi(String keyWord, String cityCode, double myLatitude, double myLngtitude) {
        mProgressDialog.dismiss();
        query = new PoiSearch.Query(keyWord, poiType, cityCode);
        //keyWord表示搜索字符串，
        // cityCode表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
        query.setPageSize(30);// 设置每页最多返回多少条poiitem
        query.setPageNum(1);//设置查询页码

        poiSearch = new PoiSearch(getActivity(), query);
        poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(myLatitude,
                myLngtitude), 2000));
        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int j) {

                if (poiResult.getPois().size() != 0) {
                    LatLng poiLatlng = new LatLng(poiLat, poiLng);
                    infoBeen = new ArrayList<PoiInfoBean>();
                    for (int i = 0; i < poiResult.getPois().size(); i++) {
                        PoiInfoBean poi = new PoiInfoBean();
                        poi.setLatitude(poiResult.getPois().get(i).getLatLonPoint().getLatitude() + "");
                        poi.setLngtitude(poiResult.getPois().get(i).getLatLonPoint().getLongitude() + "");
                        poi.setPoiAddress(poiResult.getPois().get(i).getSnippet());
                        poi.setPoiDistance(poiResult.getPois().get(i).getDistance() + "公里");
                        poi.setPoiType(poiResult.getPois().get(i).getTypeDes());
                        poi.setPoiName(poiResult.getPois().get(i).getTitle());
                        if (poiResult.getPois().get(i).getTitle().endsWith("有限公司")) {
                            infoBeen.add(poi);
                        }
                    }
                    isSearChedPoi = true;

                    adapter = new PoiListAdapter(getActivity(), infoBeen);
                    list_poiResult.setAdapter(adapter);

                    adapter.setOnLineClicklistener(new PoiListAdapter.OnLineClicklistener() {
                        @Override
                        public void lineClick(int position) {

                            //获取地图上所有Marker
                            if (isSearChedPoi == false) {
                                List<Marker> mapScreenMarkers = aMap.getMapScreenMarkers();
                                for (int i = 0; i < mapScreenMarkers.size(); i++) {
                                    Marker marker = mapScreenMarkers.get(i);
                                    if (i == mapScreenMarkers.size() - 1) {
                                        marker.remove();
                                    }
                                }
                            } else {
                                isSearChedPoi = false;
                            }
                            addMarker(infoBeen.get(position).getLatitude(), infoBeen.get(position).getLngtitude(), infoBeen.get(position).getPoiAddress(), infoBeen.get(position).getPoiName(), true);
                        }
                    });
                    adapter.setOnDetailClicklistener(new PoiListAdapter.OnDetailClicklistener() {
                        @Override
                        public void goDetail(String poiName, String snippet, String latitude, String lngtitude,String targetid,int targettype) {
                            startNaviFromList(poiName, snippet, latitude, lngtitude,targetid,targettype);
                        }
                    });
                    list_poiResult.setVisibility(View.VISIBLE);

                    for (int i = 0; i < infoBeen.size(); i++) {

                        addMarker(infoBeen.get(infoBeen.size()-i).getLatitude(), infoBeen.get(infoBeen.size()-i).getLngtitude(), infoBeen.get(infoBeen.size()-i).getPoiAddress(), infoBeen.get(infoBeen.size()-i).getPoiName(), false);
                    }
                    if (viewDerectionIsUp == true) {



                        viewDerectionIsUp = false;
                    }
                } else {

                }

            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {

            }
        });
        poiSearch.searchPOIAsyn();
    }
    private void getNearbyCommunityList() {
        ReqCommunity.getNearbyCommunityList(getActivity(), getTag(), myAdCode, new EntityCallBack<CommunityListResponse>() {
            @Override
            public Class<CommunityListResponse> getEntityClass() {
                return CommunityListResponse.class;
            }

            @Override
            public void onSuccess(CommunityListResponse resp) {

                if (resp.is200()) {
                    vipinfoBeen=new ArrayList<PoiInfoBean>();
                    vipinfoBeen.clear();
                    for (int i = 0; i < resp.obj.getCommnunityList().size(); i++) {

                        PoiInfoBean poi = new PoiInfoBean();
                        poi.setLatitude(resp.obj.getCommnunityList().get(i).getLatitude() + "");
                        poi.setLngtitude(resp.obj.getCommnunityList().get(i).getLongitude() + "");
                        poi.setPoiName(resp.obj.getCommnunityList().get(i).getCommunityName());
                        poi.setPoiAddress(resp.obj.getCommnunityList().get(i).getAddress());
                        poi.setPoiType("彼信认证企业："+resp.obj.getCommnunityList().get(i).getCommunityNote());
                        poi.setTargetId(resp.obj.getCommnunityList().get(i).getCommunityId());
                        poi.setPoinote(1);
                        if(gps2m(resp.obj.getCommnunityList().get(i).getLatitude(),resp.obj.getCommnunityList().get(i).getLongitude() ,poiLat ,poiLng)<500)
                        {
                            vipinfoBeen.add(poi);
                        }

                    }
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
    private void searchPoiAllcity(String keyWord, String cityCode, double myLatitude, double myLngtitude) {
        mProgressDialog.dismiss();
        query = new PoiSearch.Query(keyWord, poiType, null);
        //keyWord表示搜索字符串，
        // cityCode表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
        query.setPageSize(20);// 设置每页最多返回多少条poiitem
        query.setPageNum(1);//设置查询页码

        poiSearch = new PoiSearch(getActivity(), query);
        poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(myLatitude,
                myLngtitude), 2110000000));
        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int j) {

                if (poiResult.getPois().size() != 0) {
                    LatLng poiLatlng = new LatLng(poiLat, poiLng);

                    infoBeen = new ArrayList<PoiInfoBean>();

                    if (vip_company.isChecked()&&!vipinfoBeen.isEmpty())
                    {
                        for (int i = 0; i < poiResult.getPois().size(); i++)
                        {
                            try{
                                infoBeen.add(vipinfoBeen.get(i));
                            }
                              catch (Exception e)
                              {
                                  e.printStackTrace();
                              }

                        }

                    }
                    if(cus_company.isChecked())
                    {
                        for (int i = 0; i < poiResult.getPois().size(); i++) {
                            PoiInfoBean poi = new PoiInfoBean();
                            poi.setLatitude(poiResult.getPois().get(i).getLatLonPoint().getLatitude() + "");
                            poi.setLngtitude(poiResult.getPois().get(i).getLatLonPoint().getLongitude() + "");
                            poi.setPoiAddress(poiResult.getPois().get(i).getSnippet());
                            poi.setPoiDistance(poiResult.getPois().get(i).getDistance() + "公里");
                            poi.setPoiType(poiResult.getPois().get(i).getTypeDes());
                            poi.setPoiName(poiResult.getPois().get(i).getTitle());
                            poi.setPoinote(2);
                            if (poiResult.getPois().get(i).getTitle().endsWith("有限公司")) {
                                infoBeen.add(poi);
                            }
                        }
                    }


                    isSearChedPoi = true;

                    adapter = new PoiListAdapter(getActivity(), infoBeen);
                    list_poiResult.setAdapter(adapter);

                    adapter.setOnLineClicklistener(new PoiListAdapter.OnLineClicklistener() {
                        @Override
                        public void lineClick(int position) {

                    if (isSearChedPoi == false) {
                        List<Marker> mapScreenMarkers = aMap.getMapScreenMarkers();
                        for (int i = 0; i < mapScreenMarkers.size(); i++) {
                        Marker marker = mapScreenMarkers.get(i);
                        if (i == mapScreenMarkers.size() - 1) {
                                marker.remove();
                                    }
                                }
                            } else {
                                isSearChedPoi = false;
                            }
                           addMarker(infoBeen.get(position).getLatitude(), infoBeen.get(position).getLngtitude(), infoBeen.get(position).getPoiAddress(), infoBeen.get(position).getPoiName(), true);
                        }
                    });
                    adapter.setOnDetailClicklistener(new PoiListAdapter.OnDetailClicklistener() {
                        @Override
                        public void goDetail(String poiName, String snippet, String latitude, String lngtitude,String targetid,int targettype) {
                            startNaviFromList(poiName, snippet, latitude, lngtitude,targetid,targettype);
                        }
                    });
                    list_poiResult.setVisibility(View.VISIBLE);

                    for (int i = 0; i < infoBeen.size(); i++) {

                            if(infoBeen.get(i).getPoinote()==2)
                            {
                                addMarker(infoBeen.get(i).getLatitude(), infoBeen.get(i).getLngtitude(), infoBeen.get(i).getPoiAddress(), infoBeen.get(i).getPoiName(), false);

                            }


                    }
                    for (int i = 0; i < infoBeen.size(); i++) {
                        if(infoBeen.get(i).getPoinote()==1)
                             {
                                addCommunityInfoMarker(infoBeen.get(i).getLatitude(), infoBeen.get(i).getLngtitude(),infoBeen.get(i).getPoiName());

                             }


                    }

                    if (viewDerectionIsUp == true) {

                        //list_poiResult.setVisibility(GONE);

                        viewDerectionIsUp = false;
                    }
                } else {
                    list_poiResult.removeAllViews();


                }

            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {

            }
        });
        poiSearch.searchPOIAsyn();
    }
    //掉下来还回弹一次
    private void dropInto(final Marker marker) {

        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final LatLng markerLatlng = marker.getPosition();
        Projection proj = aMap.getProjection();
        Point markerPoint = proj.toScreenLocation(markerLatlng);
        Point startPoint = new Point(markerPoint.x, markerPoint.y - 20);// 从marker的屏幕上方下落
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 300;// 动画总时长

        final Interpolator interpolator = new AccelerateInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * markerLatlng.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * markerLatlng.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                if (t < 1.0) {
                    handler.postDelayed(this, 20);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    private void checkVip() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();


                try {

                    RequestBody requestBody = new FormBody.Builder()
                            .add("uid", userid)
                            .build();

                    Request request = new Request.Builder()
                            .url("http://st.3dgogo.com/index/user/set_get_enterprise_num.html")
                            .post(requestBody)
                            .build();


                    try {
                        Call call = okHttpClient.newCall(request);
                        //判断请求是否成功
                        try {
                            Response response = call.execute();

                            try {

                                JSONObject jsonObject = new JSONObject(response.body().string());

                                if (jsonObject.getInt("code")== 200) {
                                    isUseable=true;
                                }
                                else if (jsonObject.getInt("code") == 500) {

                                    isUseable=false;
                                }
                            } catch (Exception e) {
                                //  showMessagefail("深度解析失败3");


                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();

                    }


                }
                catch (Exception e)
                {
                    e.printStackTrace();

                }
            }
        }).start();

    }

    private void getallpoi()
    {
        aMap.clear();

        if(vip_company.isChecked())
        {
            getNearbyCommunityList();//get the nearby communityList
        }

        searchPoiAllcity(edit_poi_search.getText().toString(), AppApplication.getMyCurrentLocation().getCityName(), poiLat, poiLng);


       if(bison_vip.isChecked())
          {
             getStrangerList();
          }
    }



    private double gps2m(double lat_a, double lng_a, double lat_b, double lng_b) {

            double radLat1 = (lat_a * Math.PI / 180.0);

            double radLat2 = (lat_b * Math.PI / 180.0);

            double a = radLat1 - radLat2;

             double b = (lng_a - lng_b) * Math.PI / 180.0;

            double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));

            s = s * EARTH_RADIUS;

            s = Math.round(s * 10000) / 10000;

            return s;

    }


}
