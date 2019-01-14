package com.maoyongxin.myapplication.ui.chat;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.StrangerInfo;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.request.ReqFindUserById;
import com.maoyongxin.myapplication.http.request.ReqGetStrangerList;
import com.maoyongxin.myapplication.http.response.LoginResponse;
import com.maoyongxin.myapplication.http.response.StrangerResponse;
import com.maoyongxin.myapplication.myapp.AppActivity;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.server.utils.NToast;
import com.maoyongxin.myapplication.server.widget.ClearWriteEditText;
import com.maoyongxin.myapplication.server.widget.SelectableRoundedImageView;
import com.maoyongxin.myapplication.ui.StrangerDetailActivity;
import com.maoyongxin.myapplication.ui.UserDetailActivity;
import com.maoyongxin.myapplication.ui.editapp.editadapter.StrangerPersonAdapter;
import com.maoyongxin.myapplication.ui.widget.mylistview.LoadListView;


import java.util.List;

import butterknife.BindView;

public class FindAndSearchActivity extends AppActivity {

    @BindView(R.id.tv_returnBack)
    TextView tvReturnBack;
    @BindView(R.id.edit_checkKeyWord)
    ClearWriteEditText editCheckKeyWord;
    @BindView(R.id.tv_addMaillist)
    TextView tvAddMaillist;
    @BindView(R.id.tv_addByTiaoJian)
    TextView tvAddByTiaoJian;
    @BindView(R.id.line_seeMore)
    LinearLayout lineSeeMore;
    @BindView(R.id.lv_stranger)
    LoadListView lvStranger;
    @BindView(R.id.search_header)
    SelectableRoundedImageView searchHeader;
    @BindView(R.id.search_name)
    TextView searchName;
    @BindView(R.id.search_result)
    LinearLayout searchResult;
    private String userId;
    private String userName;
    private String headUrl;
    private List<StrangerInfo.UserList> list;
    StrangerPersonAdapter adapter;
    @Override
    protected int bindLayout() {
        return R.layout.activity_find_and_search;
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void doBusiness() {
        getStrangerList();
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        tvReturnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        editCheckKeyWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchResult.setVisibility(View.GONE);
                ReqFindUserById.findUser(FindAndSearchActivity.this, getActivityTag(), s.toString(), new EntityCallBack<LoginResponse>() {
                    @Override
                    public Class<LoginResponse> getEntityClass() {
                        return LoginResponse.class;
                    }

                    @Override
                    public void onSuccess(LoginResponse resp) {
                        if (resp.is200()) {
                            searchName.setText(resp.obj.getUserName());
                            userId = resp.obj.getUserId();
                            userName = resp.obj.getUserName();
                            if (resp.obj.getHeadImg().equals("")) {
                                Glide.with(FindAndSearchActivity.this).load(R.mipmap.user_head_img).into(searchHeader);
                            } else {
                                Glide.with(FindAndSearchActivity.this).load(resp.obj.getHeadImg()).into(searchHeader);
                            }

                            headUrl = resp.obj.getHeadImg();
                            searchResult.setVisibility(View.VISIBLE);
                        } else {
                            searchResult.setVisibility(View.GONE);
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
            public void afterTextChanged(Editable s) {

            }
        });
        searchResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FindAndSearchActivity.this, StrangerDetailActivity.class);
               // intent.putExtra("inType", "add_friends");
                intent.putExtra("personId", userId);
               // intent.putExtra("userName", userName);
              //  intent.putExtra("userHeadImg", headUrl);
                startActivity(intent);
            }
        });
    }

    private void getStrangerList() {
        ReqGetStrangerList.getList(FindAndSearchActivity.this, getActivityTag(), AppApplication.getCurrentUserInfo().getUserId(), 0 + "", new EntityCallBack<StrangerResponse>() {
            @Override
            public Class<StrangerResponse> getEntityClass() {
                return StrangerResponse.class;
            }

            @Override
            public void onSuccess(StrangerResponse resp) {
                if (resp.is200()) {
                    list = resp.obj.getUserList();
                    adapter = new StrangerPersonAdapter(list, getActivity());
                    lvStranger.setAdapter(adapter);
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
}
