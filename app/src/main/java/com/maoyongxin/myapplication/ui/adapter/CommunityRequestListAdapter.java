package com.maoyongxin.myapplication.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.CommunityAddRequestInfo;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.request.ReqCommunity;
import com.maoyongxin.myapplication.http.response.BaseResp;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.server.utils.NToast;

import java.util.List;

/**
 * Created by maoyongxin on 2017/12/7.
 */

public class CommunityRequestListAdapter extends BaseAdapter {
    private List<CommunityAddRequestInfo.RequestList> requestLists;
    private Context context;
    private LayoutInflater inflater;
    private OnRefreshListener onRefreshListener;
    private boolean isJoin;

    public CommunityRequestListAdapter(List<CommunityAddRequestInfo.RequestList> requestLists, Context context, boolean isJoin) {
        this.requestLists = requestLists;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.isJoin = isJoin;
    }

    @Override
    public int getCount() {
        return requestLists.size();
    }

    @Override
    public Object getItem(int position) {
        return requestLists.get(position);
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
            convertView = inflater.inflate(R.layout.item_community_request, null);
            holder.tv_requestContent = (TextView) convertView.findViewById(R.id.tv_requestContent);
            holder.tv_requestNote = (TextView) convertView.findViewById(R.id.tv_requestNote);
            holder.btn_yes = (Button) convertView.findViewById(R.id.btn_yes);
            holder.btn_no = (Button) convertView.findViewById(R.id.btn_no);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (isJoin) {
            holder.tv_requestContent.setText(requestLists.get(position).getUserId() + "请求加入小区");
        } else {
            holder.tv_requestContent.setText(requestLists.get(position).getUserId() + "请求成为管理员");
        }
        holder.tv_requestNote.setText("备注：" + requestLists.get(position).getNote());
        holder.btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isJoin){
                    dealJoinRequest(requestLists.get(position).getUserId()+"",requestLists.get(position).getCommunityId()+"",2+"");
                }else{
                    dealManagerRequest(requestLists.get(position).getUserId()+"",requestLists.get(position).getCommunityId()+"",2+"");
                }
            }
        });
        holder.btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isJoin){
                    dealJoinRequest(requestLists.get(position).getUserId()+"",requestLists.get(position).getCommunityId()+"",3+"");
                }else{
                    dealManagerRequest(requestLists.get(position).getUserId()+"",requestLists.get(position).getCommunityId()+"",3+"");
                }
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView tv_requestContent, tv_requestNote;
        Button btn_yes, btn_no;
    }

    private void dealJoinRequest(String requestId, String communityId, String addType) {
        ReqCommunity.dealJoinRequest(context, "adapter", AppApplication.getCurrentUserInfo().getUserId(), requestId, communityId, addType, new EntityCallBack<BaseResp>() {
            @Override
            public Class<BaseResp> getEntityClass() {
                return BaseResp.class;
            }

            @Override
            public void onSuccess(BaseResp resp) {
                NToast.shortToast(context, resp.msg);
                if (resp.is200()) {
                    onRefreshListener.refresh();
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

    private void dealManagerRequest(String requestId, String communityId, String addType) {
        ReqCommunity.dealSuperRequest(context, "adapter", AppApplication.getCurrentUserInfo().getUserId(), requestId, communityId, addType, new EntityCallBack<BaseResp>() {
            @Override
            public Class<BaseResp> getEntityClass() {
                return BaseResp.class;
            }

            @Override
            public void onSuccess(BaseResp resp) {
                NToast.shortToast(context, resp.msg);
                if (resp.is200()) {
                    onRefreshListener.refresh();
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

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    public interface OnRefreshListener {
        void refresh();
    }
}
