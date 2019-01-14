package com.maoyongxin.myapplication.ui.editapp.editadapter;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.bumptech.glide.Glide;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.StrangerInfo;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.request.ReqAddFollow;
import com.maoyongxin.myapplication.http.request.ReqCommunity;
import com.maoyongxin.myapplication.http.response.FollowResponse;
import com.maoyongxin.myapplication.http.response.MyCommunityResponse;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.server.utils.NToast;
import com.maoyongxin.myapplication.server.widget.SelectableRoundedImageView;
import com.maoyongxin.myapplication.ui.StrangerDetailActivity;

import java.util.List;

import static com.maoyongxin.myapplication.myapp.AppFragment.showToastShort;


/**
 * Created by Administrator on 2017/11/23.
 */

public class StrangerPersonAdapter extends BaseAdapter {
    List<StrangerInfo.UserList> list;
    Context context;
    LayoutInflater inflater;

    public StrangerPersonAdapter(List<StrangerInfo.UserList> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.item_layout_stranger_peson, null);
            holder.tv_stranger_person_name = (TextView) view.findViewById(R.id.tv_stranger_person_name);
            holder.img_stranger_header = (SelectableRoundedImageView) view.findViewById(R.id.img_stranger_header);
            holder.tv_stranger_address_community = (TextView) view.findViewById(R.id.tv_stranger_address_community);
            holder.tv_stranger_interesting = (TextView) view.findViewById(R.id.tv_stranger_interesting);
            holder.tv_xiaoqu = (TextView) view.findViewById(R.id.tv_xiaoqu);
            holder.btn_follow = (Button) view.findViewById(R.id.btn_follow);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (list.get(i).getUser().getHeadImg() != null && !list.get(i).getUser().getHeadImg().equals("")) {
            Glide.with(context).load(list.get(i).getUser().getHeadImg()).into(holder.img_stranger_header);
        } else {
            holder.img_stranger_header.setImageResource(R.mipmap.user_head_img);
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SelectableRoundedImageView SelectableRoundedImageView=(SelectableRoundedImageView)v.findViewById(R.id.img_stranger_header);

                Intent intent = new Intent(context, StrangerDetailActivity.class);

                intent.putExtra("personId", list.get(i).getUser().getUserId()+"");
                intent.putExtra("personLat", list.get(i).getUser().getLatitude());
                intent.putExtra("personLng", list.get(i).getUser().getLongitude());
                intent.putExtra("personName", list.get(i).getUser().getUserName());
                intent.putExtra("personSex", list.get(i).getUser().getSex());
                intent.putExtra("personHead", list.get(i).getUser().getHeadImg());
            //  context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(StrangerPersonAdapter.this.getClass(),SelectableRoundedImageView,"strange_head");
                 context.startActivity(intent);
            }
        });
        holder.tv_stranger_person_name.setText(list.get(i).getUser().getUserName());

        holder.btn_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLike(list.get(i).getUser().getUserId() + "", list.get(i).getUser().getUserName());
            }
        });
        if (list.get(i).getHobbyList().size() == 0) {
            holder.tv_stranger_interesting.setText("未设置爱好兴趣");
        } else {
            StringBuffer sb = new StringBuffer();
            getUserCommunity(list.get(i).getUser().getUserId() + "", holder);
            for (int j = 0; j < list.get(i).getHobbyList().size(); j++) {
                if (j == list.get(i).getHobbyList().size() - 1) {
                    sb.append(list.get(i).getHobbyList().get(j).getInterestName());
                } else {
                    sb.append(list.get(i).getHobbyList().get(j).getInterestName() + "-");
                }
            }
            holder.tv_stranger_interesting.setText(sb.toString());
        }
        final ViewHolder finalHolder = holder;
        GeocodeSearch geocoderSearch = new GeocodeSearch(context);
        geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onGeocodeSearched(GeocodeResult result, int rCode) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onRegeocodeSearched(RegeocodeResult result, int rCode) {

                String formatAddress = result.getRegeocodeAddress().getFormatAddress();
                finalHolder.tv_stranger_address_community.setText(formatAddress);
            }
        });
        LatLonPoint lp = new LatLonPoint(list.get(i).getUser().getLatitude(), list.get(i).getUser().getLongitude());
        RegeocodeQuery query = new RegeocodeQuery(lp, 200, GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query);
        return view;
    }

    private class ViewHolder {
        TextView tv_stranger_person_name, tv_stranger_address_community, tv_stranger_interesting, tv_xiaoqu;
        SelectableRoundedImageView img_stranger_header;
        Button btn_follow;
    }

    private void doLike(String personId, String userName) {
        ReqAddFollow.addFollow(context, "adapter", AppApplication.getCurrentUserInfo().getUserId(), personId, userName, new EntityCallBack<FollowResponse>() {
            @Override
            public Class<FollowResponse> getEntityClass() {
                return FollowResponse.class;
            }

            @Override
            public void onSuccess(FollowResponse resp) {
                showToastShort(resp.msg);
                NToast.shortToast(context,resp.msg);
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

    private void getUserCommunity(String userId, final ViewHolder holder) {
        ReqCommunity.getMyCommunity(context, "adapter", userId, new EntityCallBack<MyCommunityResponse>() {
            @Override
            public Class<MyCommunityResponse> getEntityClass() {
                return MyCommunityResponse.class;
            }

            @Override
            public void onSuccess(MyCommunityResponse resp) {
                if (resp.is200()) {
                    holder.tv_xiaoqu.setText("#" + resp.obj.getCommunityName());
                } else {
                    holder.tv_xiaoqu.setText("未加入小区");
                }
            }

            @Override
            public void onFailure(Throwable e) {
                holder.tv_xiaoqu.setText("未加入小区");
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
