package com.maoyongxin.myapplication.ui.groupchat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.ui.fragment.HeaderViewPagerFragment;


public class GroupFaxianFragment extends HeaderViewPagerFragment {

    private View view;
    public static GroupFaxianFragment newInstance() {
        return new GroupFaxianFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_group_chat_faxian, container, false);
        return view;
    }

    @Override
    public View getScrollableView() {
        return view;
    }
}
