package com.maoyongxin.myapplication.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.InterestingInfo;
import com.maoyongxin.myapplication.entity.MyInterestingInfo;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.request.ReqDeleteInterestRequest;
import com.maoyongxin.myapplication.http.request.ReqFindInterestList;
import com.maoyongxin.myapplication.http.request.ReqGetMyInteresting;
import com.maoyongxin.myapplication.http.request.ReqUploadInterest;
import com.maoyongxin.myapplication.http.response.BaseResp;
import com.maoyongxin.myapplication.http.response.GetMyInterestResponse;
import com.maoyongxin.myapplication.http.response.InterestResponse;
import com.maoyongxin.myapplication.http.response.UploadInterestResponse;
import com.maoyongxin.myapplication.myapp.AppActivity;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.ui.adapter.InterestingGridAdapter;
import com.maoyongxin.myapplication.ui.adapter.MyInterestingGridAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class InterestChoiceActivity extends AppActivity {

    @BindView(R.id.ll_bar)
    LinearLayout llBar;
    @BindView(R.id.gv_myChoicedHobby)
    GridView gvMyChoicedHobby;
    @BindView(R.id.line_myChoice)
    LinearLayout lineMyChoice;
    @BindView(R.id.lv_interest1)
    ListView lvInterest1;
    @BindView(R.id.lv_interest2)
    ListView lvInterest2;
    @BindView(R.id.lv_interest3)
    GridView lvInterest3;
    @BindView(R.id.line_addInterest)
    LinearLayout lineAddInterest;
    @BindView(R.id.btn_save_interest)
    Button btnSaveInterest;
    private ProgressDialog mProgressDialog;
    private InterestingGridAdapter adapter;
    private MyInterestingGridAdapter myAdapter;
    private List<InterestingInfo.InterestList> interestLists1;
    private List<InterestingInfo.InterestList> interestLists2;
    private List<InterestingInfo.InterestList> interestLists3;
    private List<MyInterestingInfo.UserInterestList> myInterestLists;
    private String interest3Id;
    private int firstPosition;

    @Override
    protected int bindLayout() {
        return R.layout.activity_interest_choice;
    }

    @Override
    protected void initView() {
        super.initView();
        mProgressDialog = new ProgressDialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(getString(R.string.processing));
    }

    @Override
    protected void doBusiness() {
        super.doBusiness();
        getMyInteresting();
        getInterestList(0 + "");
    }

    protected LayoutAnimationController getAnimationController() {
        LayoutAnimationController controller;
        Animation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);//从0.5倍放大到1倍
        anim.setDuration(1000);
        controller = new LayoutAnimationController(anim, 0.1f);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        return controller;
    }

    @Override
    protected void initData() {
        super.initData();
        interestLists1 = new ArrayList<>();
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        gvMyChoicedHobby.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ReqDeleteInterestRequest.dealRequest(InterestChoiceActivity.this, getActivityTag(), AppApplication.getCurrentUserInfo().getUserId(), myInterestLists.get(position).getInterestId() + "", new EntityCallBack<BaseResp>() {
                    @Override
                    public Class<BaseResp> getEntityClass() {
                        return BaseResp.class;
                    }

                    @Override
                    public void onSuccess(BaseResp resp) {
                        if (resp.is200()) {
                            showToastShort(resp.msg);
                            getMyInteresting();
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
        });
        btnSaveInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myInterestLists.size() != 0) {
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < myInterestLists.size(); i++) {
                        sb.append(myInterestLists.get(i).getInterestName() + ",");
                    }
                    Intent intent = new Intent();
                    intent.putExtra("interesting", sb.toString());
                    setResult(AppApplication.UPLOAD_INTERESTOK, intent);
                    finish();
                } else {
                    showToastShort("请添加您的爱好");
                }
            }
        });
    }

    private void add_hobby() {
        ReqUploadInterest.setInterest(InterestChoiceActivity.this, getActivityTag(), AppApplication.getCurrentUserInfo().getUserId(), interest3Id, new EntityCallBack<UploadInterestResponse>() {
            @Override
            public Class<UploadInterestResponse> getEntityClass() {
                return UploadInterestResponse.class;
            }

            @Override
            public void onSuccess(UploadInterestResponse resp) {
                if (resp.is200()) {
                    showToastShort(resp.msg);
                    initView();
                    doBusiness();
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

    private void getMyInteresting() {
        mProgressDialog.show();
        ReqGetMyInteresting.getInteresting(this, getActivityTag(), AppApplication.getCurrentUserInfo().getUserId(), new EntityCallBack<GetMyInterestResponse>() {
            @Override
            public Class<GetMyInterestResponse> getEntityClass() {
                return GetMyInterestResponse.class;
            }

            @Override
            public void onSuccess(GetMyInterestResponse resp) {
                mProgressDialog.dismiss();
                if (resp.is200()) {
                    myInterestLists = resp.obj.getUserInterestList();
                    myAdapter = new MyInterestingGridAdapter(myInterestLists, InterestChoiceActivity.this);
                    gvMyChoicedHobby.setAdapter(myAdapter);
                    adapter.notifyDataSetChanged();
                    if (myInterestLists.size() == 0) {
                        lineMyChoice.setVisibility(View.GONE);
                    } else if (myInterestLists.size() == 5) {
                        lineAddInterest.setVisibility(View.GONE);
                        showToastShort("每个人最多选择5个爱好");
                    } else {
                        lineMyChoice.setVisibility(View.VISIBLE);
                        lineAddInterest.setVisibility(View.VISIBLE);
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


    private void getInterestList(String parentId) {
        mProgressDialog.show();
        ReqFindInterestList.findList(this, getActivityTag(), parentId, new EntityCallBack<InterestResponse>() {
            @Override
            public Class<InterestResponse> getEntityClass() {
                return InterestResponse.class;
            }

            @Override
            public void onSuccess(InterestResponse resp) {
                mProgressDialog.dismiss();
                if (resp.is200()) {
                    if (resp.obj.getInterestList().get(0).getLevel() == 1) {
                        interestLists1 = resp.obj.getInterestList();
                        adapter = new InterestingGridAdapter(interestLists1, InterestChoiceActivity.this);
                        lvInterest1.setAdapter(adapter);
                        lvInterest1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                getInterestList(interestLists1.get(position).getInterestId());
                            }
                        });
                    } else if (resp.obj.getInterestList().get(0).getLevel() == 2) {
                        interestLists2 = resp.obj.getInterestList();
                        adapter = new InterestingGridAdapter(interestLists2, InterestChoiceActivity.this);
                        lvInterest2.setAdapter(adapter);
                        lvInterest2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                getInterestList(interestLists2.get(position).getInterestId());
                            }
                        });
                    } else {
                        interestLists3 = resp.obj.getInterestList();
                        adapter = new InterestingGridAdapter(interestLists3, InterestChoiceActivity.this);
                        lvInterest3.setAdapter(adapter);
                        lvInterest3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                adapter.notifyDataSetChanged();
                                interest3Id=interestLists3.get(position).getInterestId();
                                add_hobby();
                            }
                        });
                    }

                } else {
                    showToastShort(resp.msg);
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
