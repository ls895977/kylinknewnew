package com.maoyongxin.myapplication.ui.fragment;

import android.app.ActivityOptions;
import android.content.Context;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.FollowListInfo;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.request.ReqGetFollowList;
import com.maoyongxin.myapplication.http.response.FollowListResponse;
import com.maoyongxin.myapplication.indecator.ViewPagerDoubleIndicator;
import com.maoyongxin.myapplication.myapp.AppApplication;

import com.maoyongxin.myapplication.ui.EventMessage;
import com.maoyongxin.myapplication.ui.MainActivity;

import com.maoyongxin.myapplication.ui.PollingService;
import com.maoyongxin.myapplication.ui.editapp.findfragment.SquareFragment;
import com.maoyongxin.myapplication.ui.news.activity.Fragment1;
import com.maoyongxin.myapplication.ui.widget.DragPointView;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.ThreadMode;
import io.rong.imlib.model.UserInfo;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static org.greenrobot.eventbus.ThreadMode.BACKGROUND;
import static org.greenrobot.eventbus.ThreadMode.MAIN;


public class New_discover extends Fragment implements View.OnClickListener {
    private Button btn_renmai_list;
    private Button btn_renmai;
    private ViewPagerDoubleIndicator linTopindicator;
    private ViewPager vpViewPager;
    Unbinder unbinder;
    private List<Button> btnList;
    private List<Fragment> fragList;
    private int prePosition = 0;
    private DragPointView mUnreadNumView;
    private Uri headUri;
    private UserInfo userInfo;
    private EditText tv_search;
    private EventBus eventBus;
    public static final String EVNET_TAG = "EventService";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_discover, container, false);
        linTopindicator = (ViewPagerDoubleIndicator) view.findViewById(R.id.linTopindicator);
        btn_renmai_list = (Button) view.findViewById(R.id.btn_renmai_list);
        btn_renmai = (Button) view.findViewById(R.id.btn_renmai);
        tv_search = (EditText) view.findViewById(R.id.tv_search);
        vpViewPager = (ViewPager) view.findViewById(R.id.remmai_viewpager);
        btn_renmai_list.setOnClickListener(this);
        btn_renmai.setOnClickListener(this);
        //第二界面
        initData();
        showFragment();
        unbinder = ButterKnife.bind(this, view);
        tv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), dynamic_search.class);
               // intent.putExtra("content",content);
                //startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity(), tv_search, "srcEdit").toBundle());
                startActivity(intent);
            }
        });
/*

        tv_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode==keyEvent.KEYCODE_ENTER)
                {

                     String content=   tv_search.getText().toString();
                      //EventBus.getDefault().post(new EventMessage<String>(3, content));

                    Intent intent = new Intent(getActivity(), dynamic_search.class);
                    intent.putExtra("content",content);
                    //startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity(), tv_search, "srcEdit").toBundle());
                     startActivity(intent);

                }

                return false;
            }
        });
*/
        return view;
    }

    private void getdynamicSearch(String keyword)
    {

        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();


    }
    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    private SquareFragment squareFragment = new SquareFragment();
    private void initData() {
        btnList = new ArrayList<Button>();
        btnList.add(btn_renmai);
        btnList.add(btn_renmai_list);
        fragList = new ArrayList<Fragment>();
        fragList.add(squareFragment);
        fragList.add(new Fragment1());
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_renmai:
                vpViewPager.setCurrentItem(0);
                break;
            case R.id.btn_renmai_list:
                vpViewPager.setCurrentItem(1);
                break;

            case R.id.img_search:

                break;
        }
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
        vpViewPager.setAdapter(vpAdapter);

        /**
         * 指示器联动
         */
        vpViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                linTopindicator.scroll(position, positionOffset);
            }

            @Override
            public void onPageSelected(int position) {
                btnList.get(prePosition).setTextColor(Color.parseColor("#999999"));
                btnList.get(position).setTextColor(Color.parseColor("#565656"));
                prePosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override

    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {

            // 相当于onResume()方法
//            getMyFollowList();
            squareFragment.shua();
        } else {

            // 相当于onpause()方法

        }

    }

}
