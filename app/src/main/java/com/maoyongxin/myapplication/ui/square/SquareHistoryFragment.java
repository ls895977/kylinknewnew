package com.maoyongxin.myapplication.ui.square;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.MyDynamicListInfo;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.request.ReqMyDynamicList;
import com.maoyongxin.myapplication.http.response.MyDongTaiListResponse;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.ui.editapp.editadapter.SquareAdapter;
import com.maoyongxin.myapplication.ui.news.Inject;
import com.maoyongxin.myapplication.ui.widget.mylistview.LoadListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.maoyongxin.myapplication.myapp.AppFragment.showToastShort;


public class SquareHistoryFragment extends Fragment {
    @BindView(R.id.lv_square_myFabu_history)
    LoadListView lvSquareMyFabuHistory;
    Unbinder unbinder;
    private int listIndex;
    private SquareAdapter listAdapter;
    private List<MyDynamicListInfo.DynamicList> dynamicLists;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_square_history, container, false);
        unbinder = ButterKnife.bind(this, view);
        initData();
        initView();
        initEvent();
        getMyDongTaiList();
        return view;
    }

    private void initEvent() {
        lvSquareMyFabuHistory.setInterface(new LoadListView.ILoadListener() {
            @Override
            public void onLoad() {
                listIndex++;
                getMyDongTaiList();
            }
        });
        lvSquareMyFabuHistory.setReflashInterface(new LoadListView.RLoadListener() {
            @Override
            public void onRefresh() {
                listIndex = 0;
                getMyDongTaiList();
            }
        });
    }
    private Map<String,String> map = new HashMap<>();
    private void getMyDongTaiList() {
        ReqMyDynamicList.getDongtaiList(getActivity(), getTag(), AppApplication.getCurrentUserInfo().getUserId() + "", listIndex + "", new EntityCallBack<MyDongTaiListResponse>() {
            @Override
            public Class<MyDongTaiListResponse> getEntityClass() {
                return MyDongTaiListResponse.class;
            }

            @Override
            public void onSuccess(MyDongTaiListResponse resp) {
                if (resp.is200()) {
                    if (listIndex == 0) {
                        dynamicLists = resp.obj.getDynamicList();
                        listAdapter = new SquareAdapter(dynamicLists, getActivity(), true);
                        listAdapter.setOnDynamicDeletedListener(new SquareAdapter.OnDynamicDeletedListener() {
                            @Override
                            public void delete() {
                                listIndex = 0;
                                getMyDongTaiList();
                            }
                        });
                        lvSquareMyFabuHistory.setAdapter(listAdapter);
                        lvSquareMyFabuHistory.reflashComplete();
                        Intent intent = new Intent();
                        intent.setAction("refreshList");
                        getActivity().sendBroadcast(intent);
                    } else {
                        dynamicLists.addAll(resp.obj.getDynamicList());
                        listAdapter.notifyDataSetChanged();
                        lvSquareMyFabuHistory.loadCompleted();
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
    private void initView() {
    }


    private void initData() {
        listIndex = 0;
        dynamicLists = new ArrayList<>();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }
}
