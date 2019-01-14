package com.maoyongxin.myapplication.ui.editapp.findfragment;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdate;
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
import com.maoyongxin.myapplication.ui.AddressHomeCheckActivity;
import com.maoyongxin.myapplication.ui.FindeComapany;
import com.maoyongxin.myapplication.ui.Getstranger.Getstanger;
import com.maoyongxin.myapplication.ui.Mingpianshoucangjia;
import com.maoyongxin.myapplication.ui.PublishPartActivity;
import com.maoyongxin.myapplication.ui.PublishParty_new;
import com.maoyongxin.myapplication.ui.Publish_new;
import com.maoyongxin.myapplication.ui.StrangerDetailActivity;
import com.maoyongxin.myapplication.ui.adapter.PoiListAdapter;
import com.maoyongxin.myapplication.ui.editapp.findfragment.zhihu.Act_ZhiHuTItle;
import com.maoyongxin.myapplication.ui.editapp.renmai.Yellow_page;
import com.maoyongxin.myapplication.ui.fragment.HeaderViewPagerFragment;
import com.maoyongxin.myapplication.ui.mingpian;
import com.maoyongxin.myapplication.ui.widget.CircularAnimUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.view.View.GONE;


/**
 * Created by Administrator on 2017/11/23.
 */

public class MyDiscoveryFragment extends HeaderViewPagerFragment {

    Unbinder unbinder;
    private TextureMapView map;
    private AMap aMap;
    private LinearLayout line_search, line_poiType;
    private Button btn_showRoad;
    private ListView list_poiResult;
    private ArrayList<PoiInfoBean> vipinfoBeen;
    private ArrayList<PoiInfoBean> infoBeen;
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
    private TextView top_text;
    private String poiType;
    private ImageView img_doSearch;
    private int paddingCenter;
    private int paddingTop;
    private int paddingBottom;
    private int paddingNum = 500;
    private boolean isSearChedPoi = true;
    private Spinner sp_hangye;
    private CardView tohuiyuan,yellow_page,shangwuhuodong,line_myteam;
    private final double EARTH_RADIUS = 6378137.0;
    private boolean isFirstTime=true;
    private ImageView zhihu;
    public static MyDiscoveryFragment newInstance() {
        return new MyDiscoveryFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_discovery, null);
        View headerView =inflater.inflate(R.layout.list_head, null);
        //第三界面
        map = (TextureMapView) view.findViewById(R.id.map);
        line_search = (LinearLayout) view.findViewById(R.id.line_search);
        top_text=(TextView)view.findViewById(R.id.top_text);
        line_poiType = (LinearLayout) view.findViewById(R.id.line_poiType);
        tohuiyuan=(CardView)view.findViewById(R.id.tohuiyuan);
        yellow_page=(CardView)view.findViewById(R.id.yellow_page);
        list_poiResult = (ListView) view.findViewById(R.id.list_poiResult);
        list_poiResult.addHeaderView(headerView);
        shangwuhuodong=(CardView)view.findViewById(R.id.shangwuhuodong);
        zhihu=view.findViewById(R.id.button_zhihu);
        line_myteam=(CardView)view.findViewById(R.id.line_myteam);

        btn_showRoad = (Button) view.findViewById(R.id.btn_showRoad);
        img_doSearch = (ImageView) view.findViewById(R.id.img_doSearch);

        sp_hangye = (Spinner) view.findViewById(R.id.hangye_spinner);
        poiType = "企业";
        edit_poi_search = (ClearWriteEditText) view.findViewById(R.id.edit_poi_search);
        ViewTreeObserver vto = map.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                map.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                paddingNum = map.getHeight() / 2;
            }
        });
        shangwuhuodong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getActivity(), Yellow_page.class);
                startActivity(intent);

            }
        });

        zhihu.setOnClickListener(new View.OnClickListener() {//仿知乎列表处
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Act_ZhiHuTItle.class));
            }
        });
        line_myteam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddressHomeCheckActivity.class));
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

        initView();
        initEvent();
        unbinder = ButterKnife.bind(this, view);
        edit_poi_search.setHint("找客户、上彼信");
        aMap.moveCamera(CameraUpdateFactory.zoomTo(10));
        searchPoi("企业", AppApplication.getMyCurrentLocation().getCityName(), poiLat, poiLng);
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        tohuiyuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), Getstanger.class);
                startActivity(intent);
            }
        });
        yellow_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "内测功能，敬请期待", Toast.LENGTH_SHORT).show();
                /*
                Intent intent =new Intent(getActivity(), Publish_new.class);//进入公告板
                startActivity(intent);
                */
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
            maker.setAnimation(scAnimation);
            maker.startAnimation();
        } else {
            dropInto(maker);
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
        maker.setIcon(BitmapDescriptorFactory.fromView(getvipView(vipname)));

    }
    protected View getvipView(String pm_val) {
        View view=getLayoutInflater().inflate(R.layout.vipmaker, null);
        TextView tv_val=(TextView) view.findViewById(R.id.marker_tv_val);
        tv_val.setText(pm_val);
        return view;
    }

    public void initView() {
        mProgressDialog = new ProgressDialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setMessage(getString(R.string.locating));
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(3000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色

        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.showBuildings(false);
        aMap.getUiSettings().setZoomControlsEnabled(true);

        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        aMap.moveCamera(CameraUpdateFactory.zoomTo(18));
        aMap.getUiSettings().setScaleControlsEnabled(false);
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.getUiSettings().setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);
        getallpoi();

        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Intent intent = new Intent(getActivity(), FindeComapany.class);
                startActivity(intent);


            }
        });


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
        //  addPoiMarker(poiLat + "", poiLng + "");

        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                poiLat = cameraPosition.target.latitude;
                poiLng = cameraPosition.target.longitude;
               // getNearbyCommunityList();
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

    }




    private void startNaviFromList(String poiName, String snippet, String latitude, String lngtitude) {
        Intent intent = new Intent(getActivity(), NaviActivity.class);
        intent.putExtra("poiName", snippet);
        intent.putExtra("poiAddress", poiName);
        intent.putExtra("myLat", myLatitude + "");
        intent.putExtra("myLng", myLngtitude + "");
        intent.putExtra("myAddress", myAddress);
        intent.putExtra("poiLat", latitude + "");
        intent.putExtra("poiLng", lngtitude + "");
        CircularAnimUtil.startActivity(getActivity(), intent, btn_showRoad, R.color.blue_title);
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
            markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                    .decodeResource(getResources(), R.mipmap.icon_poi_new)));
            // 将Marker设置为贴地显示，可以双指下拉地图查看效果
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
        markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(getResources(), R.mipmap.community_marker)));
        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.setFlat(true);//设置marker平贴地图效果
        return markerOption;
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
    public void drawMarkers(final LatLng latlng, String userhead, final String id) {
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
//        upInto(marker);
                    dropInto(marker);
                }
            });
        }
//创建
//        Animation scAnimation = new ScaleAnimation(marker.getRotateAngle(), 0.5f, 0.5f, 0.5f);
//        long duration = 500L;
//        scAnimation.setDuration(duration);
//        scAnimation.setInterpolator(new LinearInterpolator());
//        marker.setAnimation(scAnimation);
//        marker.startAnimation();
    }

    private void initEvent() {

        edit_poi_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER) {
                    if (edit_poi_search.getText().toString().equals("") || edit_poi_search.getText().toString() == null) {
                        aMap.clear();

                    } else {
                        searchPoi(edit_poi_search.getText().toString(), AppApplication.getMyCurrentLocation().getCityName(), poiLat, poiLng);
                    }
                }
                return false;
            }
        });
        img_doSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_poi_search.getText().toString().equals("") || edit_poi_search.getText().toString() == null) {
                    aMap.clear();
                    list_poiResult.setVisibility(GONE);
                } else {
                    searchPoi(edit_poi_search.getText().toString(), AppApplication.getMyCurrentLocation().getCityName(), poiLat, poiLng);
                }
            }
        });
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
        query.setPageSize(50);// 设置每页最多返回多少条poiitem
        query.setPageNum(1);//设置查询页码

        poiSearch = new PoiSearch(getActivity(), query);
        poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(myLatitude,
                myLngtitude), 5000));
        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int j) {
                aMap.clear();
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
                            startNaviFromList(poiName, snippet, latitude, lngtitude);
                        }
                    });


                    for (int i = 0; i < infoBeen.size(); i++) {
                        addMarker(infoBeen.get(i).getLatitude(), infoBeen.get(i).getLngtitude(), infoBeen.get(i).getPoiAddress(), infoBeen.get(i).getPoiName(), false);
                    }
                    if (viewDerectionIsUp == true) {

                        list_poiResult.setVisibility(GONE);

                        viewDerectionIsUp = false;
                    }
                } else {

                }

            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {

            }
        });
       // poiSearch.searchPOIAsyn();
    }

    //掉下来还回弹一次
    private void dropInto(final Marker marker) {

        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final LatLng markerLatlng = marker.getPosition();
        Projection proj = aMap.getProjection();
        Point markerPoint = proj.toScreenLocation(markerLatlng);
        Point startPoint = new Point(markerPoint.x, markerPoint.y - 50);// 从marker的屏幕上方下落
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 800;// 动画总时长

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
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public View getScrollableView() {
        return list_poiResult;
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

    private void getallpoi()
    {
        aMap.clear();

        getNearbyCommunityList();//get the nearby communityList
        searchPoiAllcity(edit_poi_search.getText().toString(), AppApplication.getMyCurrentLocation().getCityName(), poiLat, poiLng);



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

                    if (!vipinfoBeen.isEmpty())
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



                    isSearChedPoi = true;

                    adapter = new PoiListAdapter(getActivity(), infoBeen);
                    list_poiResult.setAdapter(adapter);



                   // list_poiResult.setVisibility(View.VISIBLE);

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
}
