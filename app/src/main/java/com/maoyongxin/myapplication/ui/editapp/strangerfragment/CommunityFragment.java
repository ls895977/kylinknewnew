package com.maoyongxin.myapplication.ui.editapp.strangerfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jky.baselibrary.util.common.TimeUtil;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.request.ReqCommunity;
import com.maoyongxin.myapplication.http.response.CommunityUsersResponse;
import com.maoyongxin.myapplication.http.response.MyCommunityResponse;
import com.maoyongxin.myapplication.myapp.AppFragment;
import com.maoyongxin.myapplication.server.widget.SelectableRoundedImageView;
import com.maoyongxin.myapplication.ui.StrangerDetailActivity;
import com.maoyongxin.myapplication.ui.adapter.CommunityPersonListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by maoyongxin on 2018/1/4.
 */

public class CommunityFragment extends AppFragment {
    @BindView(R.id.img_DetailCommunityIcon)
    SelectableRoundedImageView imgDetailCommunityIcon;
    @BindView(R.id.tv_CommunityName)
    TextView tvCommunityName;
    @BindView(R.id.tv_communityCreatTime)
    TextView tvCommunityCreatTime;
    @BindView(R.id.tv_communityAddress)
    TextView tvCommunityAddress;
    @BindView(R.id.tv_communityNote)
    TextView tvCommunityNote;
    Unbinder unbinder;
    @BindView(R.id.tv_noCommunity)
    TextView tvNoCommunity;
    @BindView(R.id.line_myCommunity)
    LinearLayout lineMyCommunity;
    @BindView(R.id.lv_communityUser)
    ListView lvCommunityUser;
    StrangerDetailActivity act;
    @Override
    protected int bindLayout() {
        return R.layout.fragment_community;
    }

    @Override
    protected void initView() {
        super.initView();
        act= (StrangerDetailActivity) getActivity();
        getUserCommunity();
    }
    private void getCommunityUser(final String communityId){
        ReqCommunity.getMyCommunityPersons(getActivity(), getActivityTag(), act.getPersonId(), communityId, new EntityCallBack<CommunityUsersResponse>() {
            @Override
            public Class<CommunityUsersResponse> getEntityClass() {
                return CommunityUsersResponse.class;
            }

            @Override
            public void onSuccess(CommunityUsersResponse resp) {
                if(resp.is200()){
                    CommunityPersonListAdapter adapter=new CommunityPersonListAdapter(communityId,resp.obj.getSuperManagerUserId(),resp.obj.getManagerUserId(),resp.obj.getUserList(),getActivity(),true);
                    lvCommunityUser.setAdapter(adapter);
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
    private void getUserCommunity() {
        ReqCommunity.getMyCommunity(getActivity(), getActivityTag(), act.getPersonId(), new EntityCallBack<MyCommunityResponse>() {
            @Override
            public Class<MyCommunityResponse> getEntityClass() {
                return MyCommunityResponse.class;
            }

            @Override
            public void onSuccess(MyCommunityResponse resp) {
                if (resp.is200()) {
                    lineMyCommunity.setVisibility(View.GONE);
                    tvNoCommunity.setVisibility(View.GONE);
                    if (resp.obj.getCommunityId() == null || resp.obj.getCommunityId().equals("")) {
                        lineMyCommunity.setVisibility(View.GONE);
                        tvNoCommunity.setVisibility(View.VISIBLE);
                    } else {
                        if (!resp.obj.getCommunityImg().equals("")) {
                            Glide.with(getActivity()).load(resp.obj.getCommunityImg()).into(imgDetailCommunityIcon);
                        } else {
                            Glide.with(getActivity()).load(R.mipmap.community_icon_default).into(imgDetailCommunityIcon);
                        }
                        tvCommunityAddress.setText(resp.obj.getAddressName());
                        tvCommunityCreatTime.setText(TimeUtil.getFormatYMDFromMillis(resp.obj.getCreatTime(), "yyyy-MM-dd"));
                        tvCommunityName.setText(resp.obj.getCommunityName());
                        tvCommunityNote.setText(resp.obj.getCommunityNote());
                        getCommunityUser(resp.obj.getCommunityId());
                    }
                } else {
                    lineMyCommunity.setVisibility(View.GONE);
                    tvNoCommunity.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Throwable e) {
                lineMyCommunity.setVisibility(View.GONE);
                tvNoCommunity.setVisibility(View.VISIBLE);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
