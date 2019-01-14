package com.maoyongxin.myapplication.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amap.api.services.district.DistrictResult;
import com.amap.api.services.district.DistrictSearch;
import com.amap.api.services.district.DistrictSearchQuery;
import com.bumptech.glide.Glide;
import com.jky.baselibrary.util.common.TimeUtil;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.SelfPublishInfo;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.request.ReqFindUserById;
import com.maoyongxin.myapplication.http.response.LoginResponse;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.server.utils.NToast;
import com.maoyongxin.myapplication.server.widget.SelectableRoundedImageView;
import com.maoyongxin.myapplication.ui.ShowWebUrlActivity;

import java.util.List;

/**
 * Created by maoyongxin on 2017/12/7.
 */

public class MyPublishedListAdapter extends BaseAdapter {
    private List<SelfPublishInfo.NoticeList> noticeLists;
    private Context context;
    private LayoutInflater inflater;
    private String userHeadUrl;

    public MyPublishedListAdapter(List<SelfPublishInfo.NoticeList> noticeLists, Context context) {
        this.noticeLists = noticeLists;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return noticeLists.size();
    }

    @Override
    public Object getItem(int position) {
        return noticeLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (noticeLists.get(position).getNotice().getNoticeType().equals("1") || noticeLists.get(position).getNotice().getNoticeType().equals("3")) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_company_publish_list, null);
            holder.tv_company_publishTitle = (TextView) convertView.findViewById(R.id.tv_company_publishTitle);
            holder.tv_creatTime = (TextView) convertView.findViewById(R.id.tv_creatTime);
            holder.tv_creatArea = (TextView) convertView.findViewById(R.id.tv_creatArea);
            holder.tv_showDetail = (TextView) convertView.findViewById(R.id.tv_showDetail);
            convertView.setTag(holder);
        } else {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_my_publish_list, null);
            holder.tv_publish_person = (TextView) convertView.findViewById(R.id.tv_publish_person);
            holder.tv_myPublish_title = (TextView) convertView.findViewById(R.id.tv_myPublish_title);
            holder.tv_myPublish_content = (TextView) convertView.findViewById(R.id.tv_myPublish_content);
            holder.tv_myPublish_serviceType = (TextView) convertView.findViewById(R.id.tv_myPublish_serviceType);
            holder.tv_myPublish_productKind = (TextView) convertView.findViewById(R.id.tv_myPublish_productKind);
            holder.img_publish_head = (SelectableRoundedImageView) convertView.findViewById(R.id.img_publish_head);
            convertView.setTag(holder);
        }
        if (noticeLists.get(position).getNotice().getNoticeType().equals("1") || noticeLists.get(position).getNotice().getNoticeType().equals("3")) {
            holder.tv_company_publishTitle.setText(noticeLists.get(position).getNotice().getNoticeTitle());
            holder.tv_creatTime.setText(TimeUtil.getFormatYMDFromMillis(noticeLists.get(position).getNotice().getCreateTime(), "yyyy-MM-dd"));
//            holder.tv_creatArea.setText();
            GeoCoderSearch(noticeLists.get(position).getNotice().getAreaCode(), holder.tv_creatArea);

            holder.tv_showDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent intent = new Intent(context, ShowWebUrlActivity.class);
                    intent.putExtra("userId", noticeLists.get(position).getNotice().getUserId());
                    intent.putExtra("noticeId", noticeLists.get(position).getNotice().getNoticeId());
                    context.startActivity(intent);
                }
            });

        } else {
            if (noticeLists.get(position).getNotice().getUserId().equals(AppApplication.getCurrentUserInfo().getUserId())) {
                holder.tv_publish_person.setText("我的发布");
                Glide.with(context).load(AppApplication.getCurrentUserInfo().getHeadImg()).into(holder.img_publish_head);
            } else {
                holder.tv_publish_person.setText(noticeLists.get(position).getNotice().getUserId() + "发布");
                getUserInfo(noticeLists.get(position).getNotice().getUserId(), holder);
            }
            holder.tv_myPublish_title.setText(noticeLists.get(position).getNotice().getNoticeTitle());
            holder.tv_myPublish_content.setText(noticeLists.get(position).getNotice().getContent());
            if (noticeLists.get(position).getNotice().getBusinessType().equals("1")) {
                holder.tv_myPublish_serviceType.setText("采购");
            } else {
                holder.tv_myPublish_serviceType.setText("出售");
            }
            holder.tv_myPublish_productKind.setText(noticeLists.get(position).getThingName());
        }


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShowWebUrlActivity.class);
                intent.putExtra("userId", noticeLists.get(position).getNotice().getUserId());
                intent.putExtra("noticeId", noticeLists.get(position).getNotice().getNoticeId());
                context.startActivity(intent);
            }
        });
        return convertView;

    }

    class ViewHolder {
        SelectableRoundedImageView img_publish_head;
        TextView tv_publish_person, tv_myPublish_title, tv_myPublish_content, tv_myPublish_serviceType, tv_myPublish_productKind, tv_company_publishTitle, tv_creatTime, tv_creatArea, tv_showDetail;
    }

    private void GeoCoderSearch(String adCode, final TextView textView) {
        DistrictSearch search = new DistrictSearch(context);
        DistrictSearchQuery query = new DistrictSearchQuery();
        query.setKeywords(adCode);//传入关键字
        search.setQuery(query);
        search.setOnDistrictSearchListener(new DistrictSearch.OnDistrictSearchListener() {
            @Override
            public void onDistrictSearched(DistrictResult districtResult) {
                textView.setText(districtResult.getDistrict().get(0).getName());
            }
        });//绑定监听器
        search.searchDistrictAnsy();//开始搜索
    }

    private void getUserInfo(String userId, final ViewHolder holder) {
        ReqFindUserById.findUser(context, "adapter", userId, new EntityCallBack<LoginResponse>() {
            @Override
            public Class<LoginResponse> getEntityClass() {
                return LoginResponse.class;
            }

            @Override
            public void onSuccess(LoginResponse resp) {
                if (resp.is200()) {
                    if (resp.is200()) {
                        userHeadUrl = resp.obj.getHeadImg();
                        Glide.with(context).load(userHeadUrl).into(holder.img_publish_head);
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
