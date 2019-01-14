package com.maoyongxin.myapplication.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.InterestingInfo;
import com.maoyongxin.myapplication.entity.SelfPublishInfo;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.request.ReqGetAddress;
import com.maoyongxin.myapplication.http.request.ReqGetPublishListByThing;
import com.maoyongxin.myapplication.http.response.AddressResponse;
import com.maoyongxin.myapplication.http.response.SelfPublishResponse;
import com.maoyongxin.myapplication.myapp.AppActivity;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.server.widget.HeaderViewPager;
import com.maoyongxin.myapplication.server.widget.wheelSelectDialog.WheelSelectDialog;
import com.maoyongxin.myapplication.ui.adapter.InterestingGridAdapter;
import com.maoyongxin.myapplication.ui.adapter.MyPublishedListAdapter;
import com.maoyongxin.myapplication.ui.fragment.HeaderViewPagerFragment;
import com.maoyongxin.myapplication.ui.news.activity.BannerLayout;
import com.maoyongxin.myapplication.ui.widget.mylistview.LoadListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.maoyongxin.myapplication.ui.PublishPartActivity.getStringDateShort;

public class PublishParty_new extends AppActivity {
    @BindView(R.id.tv_publish_area)
    TextView tvPublishArea;
    @BindView(R.id.tv_publishKind)
    TextView tvPublishKind;
    @BindView(R.id.tv_publishTime)
    TextView tvPublishTime;
    @BindView(R.id.lv_publish_list)
    LoadListView lvPublishList;
    @BindView(R.id.img_floating)
    ImageView imgFloating;
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
    @BindView(R.id.zone_line)
    LinearLayout zoneLine;
    @BindView(R.id.line_cityChooser)
    LinearLayout lineCityChooser;
    @BindView(R.id.scrollableLayout)
    HeaderViewPager scrollableLayout;

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
    public List<HeaderViewPagerFragment> fragments;

    @Override
    protected int bindLayout() {
        return (R.layout.activity_publish_party_new);
    }

    @Override
    protected void initData() {
        //super.initData();
        isCityChooserVisible = false;
        isSelectDialogShow = false;
        isDateSelectDialogShow = false;
        choiceDate = 0 + "";
        choiceTypeId = 0 + "";
        pageIndex = 0;
        myChoiceAreaCode = AppApplication.getMyCurrentLocation().getAdCode();
    }

    @Override
    protected void initView() {
        //  super.initView();

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        scrollableLayout.setScrollContainer(false);
        myChoiceAreaCode = "510117";

        mProgressDialog = new ProgressDialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setMessage(getString(R.string.processing));
        lineCityChooser.setVisibility(View.GONE);
        tvMyLocationArea.setText(AppApplication.getMyCurrentLocation().getCityName() + "  " + AppApplication.getMyCurrentLocation().getAddress());

        dates = new String[3];
        dates[0] = getStringDateShort(0);
        dates[1] = getStringDateShort(1);
        dates[2] = getStringDateShort(2);
        myDateSelectDialog = new WheelSelectDialog(getActivity(), dates, null);
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

    @Override
    protected void initEvent() {
        //super.initEvent();
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.activity_publish_party_new);
    }
    private class ContentAdapter extends FragmentPagerAdapter {

        public ContentAdapter(FragmentManager fm) {
            super(fm);
        }

        public String[] titles = new String[]{"热门话题", "我的"};

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
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
                    addressAdapter = new InterestingGridAdapter(province, getActivity());
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
                    addressAdapter = new InterestingGridAdapter(city, getActivity());
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
                    addressAdapter = new InterestingGridAdapter(area, getActivity());
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

                        myPublishedListAdapter = new MyPublishedListAdapter(noticeLists, getActivity());

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
                                Intent intent = new Intent(getActivity(), PublicDetailActivity.class);
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
                e.printStackTrace();
            }

            @Override
            public void onCancelled(Throwable e) {
                e.printStackTrace();
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

}
