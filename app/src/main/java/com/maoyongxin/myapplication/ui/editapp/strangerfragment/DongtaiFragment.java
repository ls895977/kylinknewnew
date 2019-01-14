package com.maoyongxin.myapplication.ui.editapp.strangerfragment;

import android.view.View;
import android.widget.TextView;

import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.MyDynamicListInfo;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.request.ReqMyDynamicList;
import com.maoyongxin.myapplication.http.response.MyDongTaiListResponse;
import com.maoyongxin.myapplication.myapp.AppFragment;
import com.maoyongxin.myapplication.ui.StrangerDetailActivity;
import com.maoyongxin.myapplication.ui.editapp.editadapter.SquareAdapter;
import com.maoyongxin.myapplication.ui.widget.mylistview.LoadListView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.Unbinder;

/**
 * Created by maoyongxin on 2017/12/20.
 */

public class DongtaiFragment extends AppFragment {
    @BindView(R.id.lv_dongtaiList)
    LoadListView lv_dongtaiList;
    Unbinder unbinder;
    @BindView(R.id.tv_noDynamic)
    TextView tvNoDynamic;
    Unbinder unbinder1;
    private int listIndex;
    private SquareAdapter adapter;
    private List<MyDynamicListInfo.DynamicList> dynamicLists;
    StrangerDetailActivity act;

    @Override
    protected int bindLayout() {
        return R.layout.fragment_dongtai;
    }

    @Override
    protected void initView() {
        super.initView();
        act= (StrangerDetailActivity) getActivity();
        getStrangerDynamic();
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        lv_dongtaiList.setInterface(new LoadListView.ILoadListener() {
            @Override
            public void onLoad() {
                listIndex++;
                getStrangerDynamic();
            }
        });
        lv_dongtaiList.setReflashInterface(new LoadListView.RLoadListener() {
            @Override
            public void onRefresh() {
                listIndex = 0;
                getStrangerDynamic();
            }
        });
    }
private Map<String,String> map = new HashMap<>();
    private void getStrangerDynamic() {
        ReqMyDynamicList.getDongtaiList(getActivity(), getTag(), act.getPersonId(), listIndex + "", new EntityCallBack<MyDongTaiListResponse>() {
            @Override
            public Class<MyDongTaiListResponse> getEntityClass() {
                return MyDongTaiListResponse.class;
            }

            @Override
            public void onSuccess(MyDongTaiListResponse resp) {
                if (resp.is200()) {
                    if (listIndex == 0) {
                        if(resp.obj.getDynamicList().size()==0){
                            tvNoDynamic.setVisibility(View.VISIBLE);
                        }else{
                            tvNoDynamic.setVisibility(View.GONE);
                            dynamicLists = resp.obj.getDynamicList();
                            adapter = new SquareAdapter(dynamicLists, getActivity(), false);
                            lv_dongtaiList.setAdapter(adapter);
                            lv_dongtaiList.reflashComplete();
                        }
                    } else {
                        dynamicLists.addAll(resp.obj.getDynamicList());
                        adapter.notifyDataSetChanged();
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
