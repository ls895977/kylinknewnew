package com.maoyongxin.myapplication.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.HuatiInfo;
import com.maoyongxin.myapplication.ui.bean.HuatihuifuRecyclePinlun;
import com.maoyongxin.myapplication.ui.groupchat.huatihuifu;


public class CommentDialogFragment extends DialogFragment {
    private static final String TAG = "CommentDialogFragment";
    private static final String KEY = "huati_info";
    private HuatihuifuRecyclePinlun.InfoBean.DataBeanX huatiInfo;
    private LinearLayout llTitle;
    private LinearLayout llHuifu;
    private LinearLayout llZhuanfa;
    private LinearLayout llFuzhi;
    private LinearLayout llTousu;

    private TextView txtTitle;
    private TextView txtZhuanfa;
    private TextView txtFuzhi;
    private TextView txtHuifu;
    private TextView txtTousu;

    public CommentDialogFragment() {
    }

    public static CommentDialogFragment newInstance(HuatihuifuRecyclePinlun.InfoBean.DataBeanX huatiInfo) {
        CommentDialogFragment fragment = new CommentDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY, huatiInfo);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            huatiInfo = getArguments().getParcelable(KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment_dialog, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        initData(view);
        return view;
    }

    private void initData(View view) {
        llTitle = (LinearLayout) view.findViewById(R.id.ll_comment_title);
        llHuifu = (LinearLayout) view.findViewById(R.id.ll_comment_huifu);
        llZhuanfa = (LinearLayout) view.findViewById(R.id.ll_comment_zhuanfa);
        llFuzhi = (LinearLayout) view.findViewById(R.id.ll_comment_fuzhi);
        llTousu = (LinearLayout) view.findViewById(R.id.ll_comment_tousu);
        txtTitle = (TextView) view.findViewById(R.id.txt_comment_title);
        txtHuifu = (TextView) view.findViewById(R.id.txt_comment_huifu);
        txtZhuanfa = (TextView) view.findViewById(R.id.txt_comment_zhuanfa);
        txtTousu = (TextView) view.findViewById(R.id.txt_comment_tousu);
        txtFuzhi = (TextView) view.findViewById(R.id.txt_comment_fuzhi);
        try {
            txtTitle.setText(huatiInfo.getUserName() + ":" + new String(Base64.decode(huatiInfo.getContent().getBytes(), Base64.DEFAULT)));
        } catch (IllegalArgumentException exception) {
            txtTitle.setText(huatiInfo.getUserName() + ":" + huatiInfo.getContent());
        }
        llHuifu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), txtTitle.getText().toString(), Toast.LENGTH_SHORT).show();
                getActivity().startActivityForResult(new Intent(getActivity(), huatihuifu.class)
                        .putExtra("gambit_id", huatiInfo.getGambit_id())
                        .putExtra("username", huatiInfo.getUserName())
                        .putExtra("groupId", huatiInfo.getGroup_id())
                        .putExtra("parentUserId", huatiInfo.getId()),11);
                dismiss();
            }
        });
    }
}
