package com.maoyongxin.myapplication.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.InterestingInfo;
import com.maoyongxin.myapplication.entity.SelfPublishInfo;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.request.ReqGetAddress;
import com.maoyongxin.myapplication.http.request.ReqGetBannerList;
import com.maoyongxin.myapplication.http.request.ReqGetProType;
import com.maoyongxin.myapplication.http.request.ReqGetPublishListByThing;
import com.maoyongxin.myapplication.http.response.AddressResponse;
import com.maoyongxin.myapplication.http.response.BannerResponse;
import com.maoyongxin.myapplication.http.response.ProTypeResponse;
import com.maoyongxin.myapplication.http.response.SelfPublishResponse;
import com.maoyongxin.myapplication.myapp.AppActivity;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.server.widget.wheelSelectDialog.WheelSelectDialog;
import com.maoyongxin.myapplication.ui.adapter.InterestingGridAdapter;
import com.maoyongxin.myapplication.ui.adapter.MyPublishedListAdapter;
import com.maoyongxin.myapplication.ui.news.activity.BannerLayout;
import com.maoyongxin.myapplication.ui.widget.BannerView;
import com.maoyongxin.myapplication.ui.widget.mylistview.LoadListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.maoyongxin.myapplication.R.id.tv_publish_area;

public class PublishPartActivity extends AppActivity {

    @BindView(R.id.search_title)
    LinearLayout searchTitle;

    @BindView(tv_publish_area)
    TextView tvPublishArea;
    @BindView(R.id.tv_publishKind)
    TextView tvPublishKind;
    @BindView(R.id.tv_publishTime)
    TextView tvPublishTime;
    @BindView(R.id.lv_publish_list)
    LoadListView lvPublishList;

    @BindView(R.id.tv_myLocationArea)
    TextView tvMyLocationArea;
    @BindView(R.id.tv_refresh_location)
    TextView tvRefreshLocation;
    @BindView(R.id.lv_province)
    ListView lvProvince;
    @BindView(R.id.lv_city)
    ListView lvCity;
    @BindView(R.id.lv_area)
    GridView lvArea;
    @BindView(R.id.line_cityChooser)
    LinearLayout lineCityChooser;
    @BindView(R.id.tv_returnBack)
    TextView tvReturnBack;
    @BindView(R.id.img_addPublish)
    ImageView imgAddPublish;
    @BindView(R.id.zone_line)
    LinearLayout zoneLine;
    /**
     * 地区选择参数
     */
    private BannerLayout publishBanner;
    private boolean isCityChooserVisible;
    private List<InterestingInfo.InterestList> province;
    private List<InterestingInfo.InterestList> city;
    private List<InterestingInfo.InterestList> area;
    private List<String> areaCode;

    private InterestingGridAdapter addressAdapter;
    private WheelSelectDialog myProductTypeSelectDialog;
    private String[] types;
    private String parentType;
    private boolean isSelectDialogShow;


    private WheelSelectDialog myDateSelectDialog;
    private String[] dates;
    private boolean isDateSelectDialogShow;
    private ProgressDialog mProgressDialog;
    private String myChoiceAreaCode;
    private String choiceTypeId;
    private String choiceDate;
    private int pageIndex;

    private MyPublishedListAdapter myPublishedListAdapter;
    private List<SelfPublishInfo.NoticeList> noticeLists;

    @Override
    protected int bindLayout() {
        return R.layout.activity_publish_part;
    }

    @Override
    protected void initView() {
        super.initView();

        View view=View.inflate(getApplicationContext(),R.layout.publish_head,null);
        lvPublishList.addHeaderView(view);
        getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        publishBanner=(BannerLayout)view.findViewById(R.id.publishBanner);
        myChoiceAreaCode ="510117";
        //myChoiceAreaCode = AppApplication.getMyCurrentLocation().getAdCode();
        mProgressDialog = new ProgressDialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setMessage(getString(R.string.processing));
        lineCityChooser.setVisibility(View.GONE);
        tvMyLocationArea.setText(AppApplication.getMyCurrentLocation().getCityName() + "  " + AppApplication.getMyCurrentLocation().getAddress());
        setMyBanner();
        dates = new String[3];
        dates[0] = getStringDateShort(0);
        dates[1] = getStringDateShort(1);
        dates[2] = getStringDateShort(2);
        myDateSelectDialog = new WheelSelectDialog(PublishPartActivity.this, dates, null);
        myDateSelectDialog.setOnWheelSelectedListener(new WheelSelectDialog.OnWheelSelectedListener() {
            @Override
            public void onSelect(String selected, int index) {
                choiceDate = selected;
                tvPublishTime.setText(choiceDate);
                getPublishList(0);
            }
        });
        getPublishList(0);
    }

    private void setMyBanner() {
        List<String> res = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        if(true){
            for(int position=0;position<2;position++)
            {
                res.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531323395272&di=8c70a352698cfba9d899837c5ac3b0ec&imgtype=0&src=http%3A%2F%2Fimage.chinabgao.com%2Fimages%2Fcontent%2F20160908%2F20160908015034_34261.jpg");
                titles.add("");
            }
        }
        else{

        }
        if (publishBanner != null) {
            publishBanner.setViewUrls(res, titles);
        }

    }

    /**
     * 获取现在时间
     *
     * @return 返回短时间字符串格式yyyy-MM-dd
     */
    public static String getStringDateShort(int index) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String dateString;
        if (index == 0) {
            dateString = format.format(System.currentTimeMillis());
        } else if (index == 1) {
            dateString = format.format(System.currentTimeMillis() - 1000 * 60 * 60 * 24);
        } else {
            dateString = format.format(System.currentTimeMillis() - 1000 * 60 * 60 * 48);
        }
        return dateString;
    }

    @Override
    protected void doBusiness() {
        super.doBusiness();

    }

    private void getPublishList(final int pageIndex) {
        mProgressDialog.setMessage("查找中");
        mProgressDialog.show();
        ReqGetPublishListByThing.getPublishList(this, getActivityTag(), myChoiceAreaCode, choiceTypeId, choiceDate, pageIndex + "", new EntityCallBack<SelfPublishResponse>() {
            @Override
            public Class<SelfPublishResponse> getEntityClass() {
                return SelfPublishResponse.class;
            }

            @Override
            public void onSuccess(SelfPublishResponse resp) {
                mProgressDialog.dismiss();
                if (resp.is200()) {
                    if (pageIndex == 0) {
                        noticeLists = resp.obj.getNoticeList();

                        myPublishedListAdapter = new MyPublishedListAdapter(noticeLists, PublishPartActivity.this);

                        lvPublishList.setAdapter(myPublishedListAdapter);

                        lvPublishList.reflashComplete();
                    } else {
                        noticeLists.addAll(resp.obj.getNoticeList());
                        myPublishedListAdapter.notifyDataSetChanged();
                        lvPublishList.loadCompleted();
                    }
                    lvPublishList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            if (!noticeLists.get(i - 1).getNotice().getNoticeType().equals("1") && !noticeLists.get(i - 1).getNotice().getNoticeType().equals("3")) {
                                Intent intent = new Intent(PublishPartActivity.this, PublicDetailActivity.class);
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
//        myChoiceAreaCode
        ReqGetProType.getTypeList(this, getActivityTag(), parentId, 520115 + "", new EntityCallBack<ProTypeResponse>() {
            @Override
            public Class<ProTypeResponse> getEntityClass() {
                return ProTypeResponse.class;
            }

            @Override
            public void onSuccess(final ProTypeResponse resp) {
                if (resp.is200()) {
                    mProgressDialog.dismiss();
                    types = new String[resp.obj.getThingTypeList().size()];
                    for (int i = 0; i < resp.obj.getThingTypeList().size(); i++) {
                        types[i] = resp.obj.getThingTypeList().get(i).getTypeName();
                    }
                    myProductTypeSelectDialog = new WheelSelectDialog(PublishPartActivity.this, types, null);

                    if (resp.obj.getThingTypeList().get(0).getTypeLevel() == 1) {
                        myProductTypeSelectDialog.setTtile("请选择物品种类");
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
                                tvPublishKind.setText(resp.obj.getThingTypeList().get(index).getTypeName());
                                isCityChooserVisible = false;
                                setDrawableRightNormal(tvPublishKind);
                                getPublishList(0);
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

    @Override
    protected void initEvent() {
        super.initEvent();
        tvRefreshLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvMyLocationArea.setText(AppApplication.getMyCurrentLocation().getCityName());
            }
        });
        lvProvince.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getCityList(province.get(position).getInterestId());
            }
        });
        lvCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getAreaList(city.get(position).getInterestId());
            }
        });
        lvArea.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isCityChooserVisible = false;

                lineCityChooser.setVisibility(View.GONE);
                setDrawableRightNormal(tvPublishArea);
                tvPublishArea.setText(area.get(position).getInterestName());
                myChoiceAreaCode = areaCode.get(position);
                getPublishList(0);
                zoneLine.setVisibility(View.GONE);
            }
        });
        tvPublishArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoneLine.setVisibility(View.VISIBLE);


                if (isCityChooserVisible == false) {
                    getProvinceList();
                    isCityChooserVisible = true;
                    lineCityChooser.setVisibility(View.VISIBLE);
                    setDrawableRightDown(tvPublishArea);
                } else {
                    isCityChooserVisible = false;
                    lineCityChooser.setVisibility(View.GONE);
                    setDrawableRightNormal(tvPublishArea);
                }
            }
        });
        tvPublishKind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelectDialogShow == false) {
                    isSelectDialogShow = true;
                    getProductType(0 + "");
                    setDrawableRightDown(tvPublishKind);
                } else {
                    isSelectDialogShow = false;
                    myProductTypeSelectDialog.dismiss();
                    setDrawableRightNormal(tvPublishKind);
                }
            }
        });
        tvPublishTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDateSelectDialogShow == false) {
                    myDateSelectDialog.show();
                    isDateSelectDialogShow = true;
                    setDrawableRightDown(tvPublishTime);
                } else {
                    isDateSelectDialogShow = false;
                    myDateSelectDialog.dismiss();
                    setDrawableRightNormal(tvPublishTime);
                }
            }
        });
        tvReturnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvMyLocationArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCityChooserVisible = false;
                lineCityChooser.setVisibility(View.GONE);
                setDrawableRightNormal(tvPublishArea);
                tvPublishArea.setText(AppApplication.getMyCurrentLocation().getAddress());
                myChoiceAreaCode = AppApplication.getMyCurrentLocation().getAdCode();
                getPublishList(0);
            }
        });
        imgAddPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PublishPartActivity.this, MyPublishActivity.class));
//                CircularAnimUtil.startActivity(PublishPartActivity.this, MyPublishActivity.class, imgAddPublish, R.color.blue_title);
            }
        });
        lvPublishList.setInterface(new LoadListView.ILoadListener() {
            @Override
            public void onLoad() {
                pageIndex++;
                getPublishList(pageIndex);
                lvPublishList.loadCompleted();
            }
        });
        lvPublishList.setReflashInterface(new LoadListView.RLoadListener() {
            @Override
            public void onRefresh() {
                pageIndex = 0;
                getPublishList(pageIndex);
                lvPublishList.reflashComplete();
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        isCityChooserVisible = false;
        isSelectDialogShow = false;
        isDateSelectDialogShow = false;
        choiceDate = 0 + "";
        choiceTypeId = 0 + "";
        pageIndex = 0;
        myChoiceAreaCode = AppApplication.getMyCurrentLocation().getAdCode();
    }


    private void getProvinceList() {
        ReqGetAddress.getAddress(this, getActivityTag(), 0 + "", new EntityCallBack<AddressResponse>() {
            @Override
            public Class<AddressResponse> getEntityClass() {
                return AddressResponse.class;
            }

            @Override
            public void onSuccess(AddressResponse resp) {
                if (resp.is200()) {
                    province = new ArrayList<InterestingInfo.InterestList>();
                    for (int i = 0; i < resp.obj.getAddressList().size(); i++) {
                        InterestingInfo.InterestList myProvince = new InterestingInfo.InterestList();
                        myProvince.setInterestName(resp.obj.getAddressList().get(i).getAddressName());
                        myProvince.setInterestId(resp.obj.getAddressList().get(i).getAddressId() + "");
                        myProvince.setLevel(1);
                        province.add(myProvince);
                    }
                    addressAdapter = new InterestingGridAdapter(province, PublishPartActivity.this);
                    lvProvince.setAdapter(addressAdapter);
                    addressAdapter.notifyDataSetChanged();
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


    private void getCityList(String provinceId) {
        ReqGetAddress.getAddress(this, getActivityTag(), provinceId, new EntityCallBack<AddressResponse>() {
            @Override
            public Class<AddressResponse> getEntityClass() {
                return AddressResponse.class;
            }

            @Override
            public void onSuccess(AddressResponse resp) {
                if (resp.is200()) {
                    city = new ArrayList<InterestingInfo.InterestList>();
                    for (int i = 0; i < resp.obj.getAddressList().size(); i++) {
                        InterestingInfo.InterestList myCity = new InterestingInfo.InterestList();
                        myCity.setLevel(2);
                        myCity.setInterestName(resp.obj.getAddressList().get(i).getAddressName());
                        myCity.setInterestId(resp.obj.getAddressList().get(i).getAddressId() + "");
                        city.add(myCity);
                    }
                    addressAdapter = new InterestingGridAdapter(city, PublishPartActivity.this);
                    lvCity.setAdapter(addressAdapter);
                    addressAdapter.notifyDataSetChanged();
                    mProgressDialog.dismiss();
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

    private void getAreaList(String cityId) {
        ReqGetAddress.getAddress(this, getActivityTag(), cityId, new EntityCallBack<AddressResponse>() {
            @Override
            public Class<AddressResponse> getEntityClass() {
                return AddressResponse.class;
            }

            @Override
            public void onSuccess(AddressResponse resp) {
                if (resp.is200()) {
                    area = new ArrayList<InterestingInfo.InterestList>();
                    areaCode = new ArrayList<String>();
                    for (int i = 0; i < resp.obj.getAddressList().size(); i++) {
                        InterestingInfo.InterestList myArea = new InterestingInfo.InterestList();
                        myArea.setInterestName(resp.obj.getAddressList().get(i).getAddressName());
                        String myAreaCode;
                        myAreaCode = resp.obj.getAddressList().get(i).getAddressId() + "";
                        myArea.setLevel(3);
                        areaCode.add(myAreaCode);
                        area.add(myArea);
                    }
                    addressAdapter = new InterestingGridAdapter(area, PublishPartActivity.this);
                    lvArea.setAdapter(addressAdapter);
                    addressAdapter.notifyDataSetChanged();
                    mProgressDialog.dismiss();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
