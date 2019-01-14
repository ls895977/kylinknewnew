package com.maoyongxin.myapplication.ui.editapp.findfragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.ui.AddressHomeCheckActivity;
import com.maoyongxin.myapplication.ui.GroupListActivity;
import com.maoyongxin.myapplication.ui.comunitymateDetailActivity;
import com.maoyongxin.myapplication.ui.mingpian;

import io.rong.imlib.model.UserInfo;


public class renmai_list extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private LinearLayout line_mingpian;
    private LinearLayout line_myteam;
    private LinearLayout line_myshanghui;



    private String myHeadPath;
    private String nickName;
    private String phoneNumber;
    private String personSex;
    private String  personId;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_renmai_list, container, false);
        line_mingpian=(LinearLayout) view.findViewById(R.id.ling_minpian);
        line_myteam=(LinearLayout) view.findViewById(R.id.line_myteam);
        line_myshanghui=(LinearLayout) view.findViewById(R.id.line_myshanghui);



        line_mingpian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),mingpian.class));

            }
        });

        line_myshanghui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), GroupListActivity.class));
            }
        });
        line_myteam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // startActivity(new Intent(getActivity(), AddressHomeCheckActivity.class));
                Intent intent =new Intent(getActivity(),comunitymateDetailActivity.class);

                nickName = AppApplication.getCurrentUserInfo().getUserName();
                personSex=AppApplication.getCurrentUserInfo().getSex()+"";
                myHeadPath = AppApplication.getCurrentUserInfo().getHeadImg();
                phoneNumber=AppApplication.getCurrentUserInfo().getUserPhone();
                personId=AppApplication.getCurrentUserInfo().getUserId();
                intent.putExtra("personId",personId);
                intent.putExtra("personHead",myHeadPath);
                intent.putExtra("personusrename",nickName);
                intent.putExtra("personTel",phoneNumber);
                intent.putExtra("personSex",personSex);
                startActivity(intent);
            }
        });
    return view;




                }





}
