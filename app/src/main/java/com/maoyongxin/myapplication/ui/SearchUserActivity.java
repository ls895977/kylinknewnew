package com.maoyongxin.myapplication.ui;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.RequestListInfo;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.request.ReqFindUserById;
import com.maoyongxin.myapplication.http.request.ReqGetRequestList;
import com.maoyongxin.myapplication.http.response.LoginResponse;
import com.maoyongxin.myapplication.http.response.RequestListResponse;
import com.maoyongxin.myapplication.myapp.AppActivity;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.server.widget.SelectableRoundedImageView;
import com.maoyongxin.myapplication.ui.adapter.RequestListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SearchUserActivity extends AppActivity {


    @BindView(R.id.search_edit)
    EditText searchEdit;
    @BindView(R.id.search_header)
    SelectableRoundedImageView searchHeader;
    @BindView(R.id.search_name)
    TextView searchName;
    @BindView(R.id.search_result)
    LinearLayout searchResult;
    @BindView(R.id.lv_requestList)
    ListView lvRequestList;
    private String userId;
    private String userName;
    private String headUrl;

    @Override
    protected int bindLayout() {
        return R.layout.activity_search_user;
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void doBusiness() {
        super.doBusiness();
        getRequestList();
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchResult.setVisibility(View.GONE);
                ReqFindUserById.findUser(SearchUserActivity.this, getActivityTag(), s.toString(), new EntityCallBack<LoginResponse>() {
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
                                Glide.with(SearchUserActivity.this).load(R.mipmap.user_head_img).into(searchHeader);
                            } else {
                                Glide.with(SearchUserActivity.this).load(resp.obj.getHeadImg()).into(searchHeader);
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
                Intent intent = new Intent(SearchUserActivity.this, UserDetailActivity.class);
                intent.putExtra("inType", "add_friends");
                intent.putExtra("userId", userId);
                intent.putExtra("userName", userName);
                intent.putExtra("userHeadImg", headUrl);
                startActivity(intent);
            }
        });
    }

    private void getRequestList() {
        ReqGetRequestList.getList(this, getActivityTag(), AppApplication.getCurrentUserInfo().getUserId(), new EntityCallBack<RequestListResponse>() {
            @Override
            public Class<RequestListResponse> getEntityClass() {
                return RequestListResponse.class;
            }

            @Override
            public void onSuccess(RequestListResponse resp) {
                if (resp.is200()) {
                    List<RequestListInfo.RequestList> requestListInfos = new ArrayList<RequestListInfo.RequestList>();
                    requestListInfos = resp.obj.getRequestList();
                    if (requestListInfos.size() != 0) {
                        RequestListAdapter adapter = new RequestListAdapter(SearchUserActivity.this, requestListInfos);
                        lvRequestList.setAdapter(adapter);
                        lvRequestList.setVisibility(View.VISIBLE);
                    } else {
                        lvRequestList.setVisibility(View.GONE);
                    }
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
