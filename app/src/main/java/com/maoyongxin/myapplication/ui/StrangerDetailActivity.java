package com.maoyongxin.myapplication.ui;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.bumptech.glide.Glide;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.request.ReqAddFollow;
import com.maoyongxin.myapplication.http.request.ReqFindUserById;
import com.maoyongxin.myapplication.http.response.FollowResponse;
import com.maoyongxin.myapplication.http.response.LoginResponse;
import com.maoyongxin.myapplication.indecator.ViewPagerDoubleIndicator;
import com.maoyongxin.myapplication.myapp.AppActivity;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.server.widget.SelectableRoundedImageView;
import com.maoyongxin.myapplication.ui.editapp.strangerfragment.CommunityFragment;
import com.maoyongxin.myapplication.ui.editapp.strangerfragment.DongtaiFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imkit.RongIM;

public class StrangerDetailActivity extends AppActivity {
    @BindView(R.id.img_stranger_back)
    ImageView img_stranger_back;
    @BindView(R.id.stranger_header)
    SelectableRoundedImageView stranger_header;
    @BindView(R.id.tv_stranger_name)
    TextView tvStrangerName;
    @BindView(R.id.btn_like)
    TextView btnLike;
    @BindView(R.id.btn_dynamic)
    Button btnDynamic;
    @BindView(R.id.btn_community)
    Button btnCommunity;
    @BindView(R.id.tv_stranger_address)
    TextView tvStrangerAddress;
    @BindView(R.id.lin_topindicator)
    ViewPagerDoubleIndicator linTopindicator;
    @BindView(R.id.vp_myviewPager)
    ViewPager vpMyviewPager;
    @BindView(R.id.btn_contact)
    Button btnContact;
    private String personId;
    private LatLng personLatlng;
    private String sex;
    private String headImgUrl;
    private String userName;
    private List<Button> btnList;
    private List<Fragment> fragList;
    private int prePosition = 0;

    @Override
    protected void initView() {
        super.initView();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//白底黑字+沉浸式导航栏
        btnList = new ArrayList<Button>();
        btnList.add(btnDynamic);
        btnList.add(btnCommunity);
        fragList = new ArrayList<Fragment>();
        fragList.add(new DongtaiFragment());
        fragList.add(new CommunityFragment());
        showFragment();
        getPersonInfo();
        img_stranger_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public String getPersonId() {
        return personId;
    }

    private void getPersonInfo() {
        ReqFindUserById.findUser(this, getActivityTag(), personId, new EntityCallBack<LoginResponse>() {
            @Override
            public Class<LoginResponse> getEntityClass() {
                return LoginResponse.class;
            }

            @Override
            public void onSuccess(LoginResponse resp) {
                if (resp.is200()) {
                    sex = resp.obj.getSex();
                    headImgUrl = resp.obj.getHeadImg();
                    userName = resp.obj.getUserName();
                    LatLng latLng = new LatLng(Double.parseDouble(resp.obj.getLatitude()), Double.parseDouble(resp.obj.getLongitude()));
                    personLatlng = latLng;
                    if (sex.equals("0")) {
                        setDrawableBoy(tvStrangerName);
                    } else if (sex.equals("1")) {
                        setDrawableBoy(tvStrangerName);
                    } else {
                        setDrawableGirl(tvStrangerName);
                    }
                    tvStrangerName.setText(userName);
                    if (headImgUrl.equals("")) {
                        Glide.with(StrangerDetailActivity.this).load(R.mipmap.user_head_img).into(stranger_header);
                    } else {
                        Glide.with(StrangerDetailActivity.this).load(headImgUrl).into(stranger_header);
                    }

                    GeocodeSearch geocoderSearch = new GeocodeSearch(StrangerDetailActivity.this);
                    geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
                        @Override
                        public void onGeocodeSearched(GeocodeResult result, int rCode) {
                            // TODO Auto-generated method stub
                        }

                        @Override
                        public void onRegeocodeSearched(RegeocodeResult result, int rCode) {

                            String formatAddress = result.getRegeocodeAddress().getFormatAddress();
                            //tvStrangerAddress.setText(formatAddress);
                            tvStrangerAddress.setText("会员ID"+personId);
                        }
                    });
                    LatLonPoint lp = new LatLonPoint(Double.parseDouble(resp.obj.getLatitude()), Double.parseDouble(resp.obj.getLongitude()));
                    RegeocodeQuery query = new RegeocodeQuery(lp, 200, GeocodeSearch.AMAP);
                    geocoderSearch.getFromLocationAsyn(query);
                } else {
                    showToastShort("获取个人信息失败，请重试");
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

    private void setDrawableBoy(TextView view) {
        Drawable drawable = getResources().getDrawable(R.mipmap.icon_boy);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
        view.setCompoundDrawables(drawable, null, null, null);//画在左边
    }

    private void setDrawableGirl(TextView view) {
        Drawable drawable = getResources().getDrawable(R.mipmap.icon_girl);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
        view.setCompoundDrawables(drawable, null, null, null);//画在左边
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void initData() {
        // super.initData();
        personId = getIntent().getStringExtra("personId");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> list;

        public ViewPagerAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return list.get(0);
            } else {
                return list.get(1);
            }
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }

    /**
     * 展示fragment
     */
    private void showFragment() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        ViewPagerAdapter vpAdapter = new ViewPagerAdapter(fm, fragList);
        vpAdapter.notifyDataSetChanged();
        vpMyviewPager.setAdapter(vpAdapter);

        /**
         * 指示器联动
         */
        vpMyviewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                linTopindicator.scroll(position, positionOffset);
            }

            @Override
            public void onPageSelected(int position) {
                btnList.get(prePosition).setTextColor(Color.parseColor("#80000000"));
                btnList.get(position).setTextColor(Color.parseColor("#80000000"));
                prePosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLike();
            }
        });

        btnDynamic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vpMyviewPager.setCurrentItem(0);
            }
        });
        btnCommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vpMyviewPager.setCurrentItem(1);
            }
        });

        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RongIM.getInstance().startPrivateChat(getActivity(), personId, tvStrangerName.getText().toString());
            }
        });
    }

    private void doLike() {

        //Log.e("guanzhu","personId:"+personId+"====userName:"+userName);
        ReqAddFollow.addFollow(this, getActivityTag(), AppApplication.getCurrentUserInfo().getUserId(), personId, userName, new EntityCallBack<FollowResponse>() {
            @Override
            public Class<FollowResponse> getEntityClass() {
                return FollowResponse.class;
            }

            @Override
            public void onSuccess(FollowResponse resp) {
                showToastShort(resp.msg);
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
    protected int bindLayout() {
        return R.layout.activity_stranger_detail;
    }

}
