package com.maoyongxin.myapplication.ui.fragment;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.indecator.ViewPagerDoubleIndicator;
import com.maoyongxin.myapplication.ui.editapp.findfragment.SquareFragment;
import com.maoyongxin.myapplication.ui.widget.DragPointView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.rong.imlib.model.UserInfo;


public class mainPage extends Fragment{
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mainpage, container, false);
        linTopindicator = (ViewPagerDoubleIndicator) view.findViewById(R.id.linTopindicator);
        btn_renmai_list=(Button)view.findViewById(R.id.btn_renmai_list);
        btn_renmai=(Button)view.findViewById(R.id.btn_renmai);

        vpViewPager= (ViewPager) view.findViewById(R.id.remmai_viewpager);

        initData();
        showFragment();
        unbinder = ButterKnife.bind(this, view);
        return view;
    }
    private void initData() {



        btnList = new ArrayList<Button>();
        btnList.add(btn_renmai);
        btnList.add(btn_renmai_list);




        fragList = new ArrayList<Fragment>();


        fragList.add(new SquareFragment());
        //fragList.add(new Fragment3());



    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> list;

        public ViewPagerAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public Fragment getItem(int position) {
            if(position==0){
                return list.get(0);
            }else{
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
}
