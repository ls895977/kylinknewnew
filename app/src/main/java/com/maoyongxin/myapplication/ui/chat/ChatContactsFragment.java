package com.maoyongxin.myapplication.ui.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.ui.GroupListActivity;
import com.maoyongxin.myapplication.ui.Mingpianshoucangjia;
import com.maoyongxin.myapplication.ui.MyFollowActivity;
import com.maoyongxin.myapplication.ui.mingpian;


public class ChatContactsFragment extends Fragment implements View.OnClickListener {
    private LinearLayout line_myFollow, line_myFriend, line_myGroup, line_MySystem;
    private TextView tv_shoucangjia;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_contact, container, false);
        line_myFollow = (LinearLayout) view.findViewById(R.id.line_myFollow);
        line_myFriend = (LinearLayout) view.findViewById(R.id.line_myFriend);
        line_myGroup = (LinearLayout) view.findViewById(R.id.line_myGroup);
        line_MySystem = (LinearLayout) view.findViewById(R.id.line_MySystem);

        line_myFollow.setOnClickListener(this);
        line_myFriend.setOnClickListener(this);
        line_myGroup.setOnClickListener(this);
        line_MySystem.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.line_myFollow:
                startActivity(new Intent(getActivity(), MyFollowActivity.class));
                break;
            case R.id.line_myFriend:
                startActivity(new Intent(getActivity(),MyFriendsActivity.class));
                break;
            case R.id.line_myGroup:
                startActivity(new Intent(getActivity(), GroupListActivity.class));
                break;
            case R.id.line_MySystem:
                startActivity(new Intent(getActivity(), mingpian.class));
                break;
        }
    }
}
