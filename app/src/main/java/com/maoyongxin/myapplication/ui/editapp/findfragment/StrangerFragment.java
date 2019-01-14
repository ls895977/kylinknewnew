package com.maoyongxin.myapplication.ui.editapp.findfragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.StrangerInfo;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.request.ReqGetStrangerList;
import com.maoyongxin.myapplication.http.request.ReqGetStrangerListByKeyword;
import com.maoyongxin.myapplication.http.response.StrangerResponse;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.server.utils.NToast;
import com.maoyongxin.myapplication.server.widget.ClearWriteEditText;
import com.maoyongxin.myapplication.server.widget.SelectableRoundedImageView;
import com.maoyongxin.myapplication.ui.StrangerDetailActivity;
import com.maoyongxin.myapplication.ui.editapp.editadapter.StrangerPersonAdapter;

import java.util.List;


/**
 * Created by Administrator on 2017/11/23.
 */

public class StrangerFragment extends Fragment {
    private List<StrangerInfo.UserList> list;
    StrangerPersonAdapter adapter;
    private ListView lv_stranger_person;
    private ClearWriteEditText edit_search_title;
    private TextView tv_hasPushStranger;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stranger, null);
        View lvStr=inflater.inflate(R.layout.qiling_head,null);
        lv_stranger_person = (ListView) view.findViewById(R.id.lv_stranger_person);

        lv_stranger_person.addHeaderView(lvStr);
        edit_search_title = (ClearWriteEditText) view.findViewById(R.id.edit_search);
        tv_hasPushStranger = (TextView) view.findViewById(R.id.tv_hasPushStranger);
        edit_search_title.setHint("请输入对方昵称");
        initData();
        initEvent();
        return view;
    }

    private void initData() {
        getStrangerList();
    }

    private void searchStranger(String keyWords) {
        ReqGetStrangerListByKeyword.getStranger(getActivity(), getTag(), AppApplication.getCurrentUserInfo().getUserId(), keyWords, new EntityCallBack<StrangerResponse>() {
            @Override
            public Class<StrangerResponse> getEntityClass() {
                return StrangerResponse.class;
            }

            @Override
            public void onSuccess(StrangerResponse resp) {
                if (resp.is200()) {
                    list = resp.obj.getUserList();
                    adapter = new StrangerPersonAdapter(list, getActivity());
                    lv_stranger_person.setAdapter(adapter);
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

    private void getStrangerList() {
        ReqGetStrangerList.getList(getActivity(), getTag(), AppApplication.getCurrentUserInfo().getUserId(), 0 + "", new EntityCallBack<StrangerResponse>() {
            @Override
            public Class<StrangerResponse> getEntityClass() {
                return StrangerResponse.class;
            }

            @Override
            public void onSuccess(StrangerResponse resp) {
                if (resp.is200()) {
                    list = resp.obj.getUserList();
                    if (list.size() == 0) {
                        searchStranger("");
                    } else {
                        adapter = new StrangerPersonAdapter(list, getActivity());
                        lv_stranger_person.setAdapter(adapter);
                        tv_hasPushStranger.setText("为您找到" + list.size() + "个和您对口味的陌生人");
                        tv_hasPushStranger.setVisibility(View.VISIBLE);
                    }
                } else {
                    NToast.shortToast(getActivity(), resp.msg);
                    searchStranger("");
                }
            }

            @Override
            public void onFailure(Throwable e) {
                searchStranger("");
            }

            @Override
            public void onCancelled(Throwable e) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void initEvent() {
        lv_stranger_person.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                SelectableRoundedImageView strange_head=(SelectableRoundedImageView)view.findViewById(R.id.img_stranger_header);
                TextView textView=(TextView)view.findViewById(R.id.tv_stranger_person_name);

                Intent intent=new Intent(getActivity(), StrangerDetailActivity.class);
                intent.putExtra("personName",textView.getText());
              // startActivity(intent);
               // startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity(),strange_head,"strange_head").toBundle());




            }
        });
        tv_hasPushStranger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStrangerList();
            }
        });
        edit_search_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchStranger(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
