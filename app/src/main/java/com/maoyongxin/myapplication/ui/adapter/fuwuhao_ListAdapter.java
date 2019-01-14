package com.maoyongxin.myapplication.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.bumptech.glide.Glide;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.MyCommunityPersonInfo;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.request.ReqCommunity;
import com.maoyongxin.myapplication.http.response.BaseResp;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.server.utils.NToast;
import com.maoyongxin.myapplication.server.widget.SelectableRoundedImageView;

import java.util.List;

import io.rong.imkit.RongIM;

/**
 * Created by maoyongxin on 2017/12/7.
 */

public class fuwuhao_ListAdapter extends BaseAdapter {
    private String communityId;
    private String superManagerId;
    private List<String> managerId;
    private List<MyCommunityPersonInfo.UserList> userLists;
    private Context context;
    private LayoutInflater inflater;
    private OnRefreshListener onRefreshListener;
    private boolean isManager = false;
    private boolean isFromFragment=false;

    public fuwuhao_ListAdapter(String communityId, String superManagerId, List<String> managerId, List<MyCommunityPersonInfo.UserList> userLists, Context context, boolean isFromFragment) {
        this.communityId = communityId;
        this.userLists = userLists;
        this.context = context;
        this.superManagerId = superManagerId;
        this.managerId = managerId;
        this.inflater = LayoutInflater.from(context);
        this.isFromFragment=isFromFragment;
    }

    @Override
    public int getCount() {
        return userLists.size();
    }

    @Override
    public Object getItem(int position) {
        return userLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_fuwuhao_list, null);
            holder.img_head_communityPerson = (SelectableRoundedImageView) convertView.findViewById(R.id.img_head_communityPerson);
            holder.tv_communityPersonName = (TextView) convertView.findViewById(R.id.tv_communityPersonName);
            holder.tv_communityPersonType = (TextView) convertView.findViewById(R.id.tv_communityPersonType);
            holder.tv_communityPersonNote = (TextView) convertView.findViewById(R.id.tv_communityPersonNote);
            holder.tv_communityPersonAddress = (TextView) convertView.findViewById(R.id.tv_communityPersonAddress);
            holder.fuwuhao_contact  =(Button) convertView.findViewById(R.id.fuwuhao_contact);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (userLists.get(position).getHeadImg() != null && !userLists.get(position).getHeadImg().equals("")) {
            Glide.with(context).load(userLists.get(position).getHeadImg()).into(holder.img_head_communityPerson);
        } else {
            Glide.with(context).load(R.mipmap.user_head_img).into(holder.img_head_communityPerson);
        }
        holder.tv_communityPersonName.setText(userLists.get(position).getUserName());
        holder.tv_communityPersonNote.setText(userLists.get(position).getNote());
        if(isFromFragment){

        }else{
            for (int i = 0; i < managerId.size(); i++) {
                if (userLists.get(position).getUserId().equals(managerId.get(i))) {
                    isManager = true;

                    break;
                } else {
                    isManager = false;

                }
            }
            if (userLists.get(position).getUserId().equals(superManagerId)) {
                holder.tv_communityPersonType.setText("管理员");

            } else {
                if (isManager==false) {
                    holder.tv_communityPersonType.setText("业务经理");

                } else {
                    holder.tv_communityPersonType.setText("高级业务经理");

                }
            }
        }

        GeocodeSearch geocoderSearch = new GeocodeSearch(context);
        final ViewHolder finalHolder = holder;
        geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onGeocodeSearched(GeocodeResult result, int rCode) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
                finalHolder.tv_communityPersonAddress.setVisibility(View.VISIBLE);
                String formatAddress = result.getRegeocodeAddress().getFormatAddress();
                finalHolder.tv_communityPersonAddress.setText(formatAddress);
            }
        });
        LatLonPoint lp = new LatLonPoint(userLists.get(position).getLatitude(), userLists.get(position).getLongitude());
        RegeocodeQuery query = new RegeocodeQuery(lp, 200, GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query);
        holder.fuwuhao_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RongIM.getInstance().startPrivateChat(context, userLists.get(position).getUserId(),userLists.get(position).getUserName());
            }
        });
        return convertView;
    }

    class ViewHolder {
        SelectableRoundedImageView img_head_communityPerson;
        TextView tv_communityPersonName, tv_communityPersonType, tv_communityPersonNote, tv_communityPersonAddress;
        Button fuwuhao_contact;
    }

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    public interface OnRefreshListener {
        void refresh();
    }
}
