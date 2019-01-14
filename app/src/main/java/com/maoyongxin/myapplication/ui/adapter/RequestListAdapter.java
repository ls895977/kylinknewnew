package com.maoyongxin.myapplication.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.RequestListInfo;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.request.ReqDealRequest;
import com.maoyongxin.myapplication.http.request.ReqFindUserById;
import com.maoyongxin.myapplication.http.response.BaseResp;
import com.maoyongxin.myapplication.http.response.LoginResponse;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.server.utils.NToast;
import com.maoyongxin.myapplication.server.widget.SelectableRoundedImageView;
import com.maoyongxin.myapplication.ui.RecieverFriendsPushActivity;

import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.InformationNotificationMessage;

/**
 * Created by maoyongxin on 2017/12/4.
 */

public class RequestListAdapter extends BaseAdapter {
    private Context context;
    private List<RequestListInfo.RequestList> requestListInfos;
    private LayoutInflater inflater;
    private OnFreshListener freshListener;

    public RequestListAdapter(Context context, List<RequestListInfo.RequestList> requestListInfos) {
        this.context = context;
        this.requestListInfos = requestListInfos;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return requestListInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return requestListInfos.get(position);
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
            convertView = inflater.inflate(R.layout.item_request_list, null);
            holder.tv_list_state = (TextView) convertView.findViewById(R.id.tv_list_state);
            holder.tv_list_data = (TextView) convertView.findViewById(R.id.tv_list_data);
            holder.btn_accept = (Button) convertView.findViewById(R.id.btn_accept);
            holder.line_myRequest_item = (LinearLayout) convertView.findViewById(R.id.line_myRequest_item);
            holder.tv_requestUserName = (TextView) convertView.findViewById(R.id.tv_requestUserName);
            holder.img_requestUserHead = (SelectableRoundedImageView) convertView.findViewById(R.id.img_requestUserHead);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.btn_accept.setVisibility(View.GONE);
        holder.tv_list_state.setVisibility(View.VISIBLE);
        findUser(requestListInfos.get(position).getUserId(), holder);
        if (requestListInfos.get(position).getUserId().equals(AppApplication.getCurrentUserInfo().getUserId())) {//作为请求方
            if (requestListInfos.get(position).getRequestState().equals("1")) {
                holder.tv_list_state.setText("已提交");
               // holder.tv_list_state.setBackgroundColor(context.getResources().getColor(R.color.def_sub_2_yellow));
                holder.tv_list_data.setText("您请求添加" + requestListInfos.get(position).getRequestUserId() + "为好友");
            } else if (requestListInfos.get(position).getRequestState().equals("2")) {
                holder.tv_list_state.setText("已接受");
               // holder.tv_list_state.setBackgroundColor(context.getResources().getColor(R.color.blue_tiny));
                holder.tv_list_data.setText(requestListInfos.get(position).getRequestUserId() + "接收您的添加为好友");
            } else {
              //  holder.tv_list_state.setBackgroundColor(context.getResources().getColor(R.color.def_sub_1_pink));
                holder.tv_list_state.setText("已拒绝");

                holder.tv_list_data.setText(requestListInfos.get(position).getRequestUserId() + "拒绝您的添加为好友");
            }
        } else {//作为接收方
            if (requestListInfos.get(position).getRequestState().equals("1")) {
                holder.btn_accept.setVisibility(View.VISIBLE);
                holder.tv_list_state.setVisibility(View.GONE);
                holder.tv_list_data.setText(requestListInfos.get(position).getNote());

            } else if (requestListInfos.get(position).getRequestState().equals("2")) {
              //  holder.tv_list_state.setBackgroundColor(context.getResources().getColor(R.color.blue_tiny));
               holder.tv_list_state.setText("已接受");
                holder.tv_list_data.setText(requestListInfos.get(position).getRequestUserId() + "成为您的好友");
            } else {
              //  holder.tv_list_state.setBackgroundColor(context.getResources().getColor(R.color.def_sub_1_pink));
                holder.tv_list_state.setText("已拒绝");
                holder.tv_list_data.setText(requestListInfos.get(position).getRequestUserId() + "已被您拒绝");
            }

        }
        final ViewHolder finalHolder = holder;
//        holder.line_myRequest_item.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                doClick(position, finalHolder);
//            }
//        });
        holder.btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dealRequest(requestListInfos.get(position).getRequestId());
            }
        });
        return convertView;
    }

    private void dealRequest(String requestId) {
        ReqDealRequest.dealRequest(context, "adapter", AppApplication.getCurrentUserInfo().getUserId(), requestId, 2 + "", new EntityCallBack<BaseResp>() {
            @Override
            public Class<BaseResp> getEntityClass() {
                return BaseResp.class;
            }

            @Override
            public void onSuccess(BaseResp resp) {
                if (resp.is200()) {
                    NToast.shortToast(context, "成功添加对方为好友，你们现在可以聊天啦");
                    freshListener.fresh();
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

    private void doClick(int position, ViewHolder holder) {
        if (requestListInfos.get(position).getUserId().equals(AppApplication.getCurrentUserInfo().getUserId())) {//作为请求方
            if (requestListInfos.get(position).getRequestState().equals("1")) {
                Toast.makeText(context, "已经提交好友请求，请等待回复", Toast.LENGTH_SHORT).show();
            } else if (requestListInfos.get(position).getRequestState().equals("2")) {
                Toast.makeText(context, "对方已接受您的请求", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "抱歉，对方拒绝添加您为好友", Toast.LENGTH_SHORT).show();
            }
        } else {//作为接收方
            if (requestListInfos.get(position).getRequestState().equals("1")) {
                Intent intent = new Intent(context, RecieverFriendsPushActivity.class);
                intent.putExtra("userId", requestListInfos.get(position).getRequestId());
                intent.putExtra("data", requestListInfos.get(position).getRequestDate());
                intent.putExtra("requestId", requestListInfos.get(position).getRequestId());
                context.startActivity(intent);
            } else if (requestListInfos.get(position).getRequestState().equals("2")) {
                Toast.makeText(context, "对方现在是您的好友", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "您已拒绝添加对方为好友", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void findUser(String userId, final ViewHolder holder) {
        ReqFindUserById.findUser(context, "adapter", userId, new EntityCallBack<LoginResponse>() {
            @Override
            public Class<LoginResponse> getEntityClass() {
                return LoginResponse.class;
            }

            @Override
            public void onSuccess(LoginResponse resp) {
                if (resp.is200()) {
                    holder.tv_requestUserName.setText(resp.obj.getUserName());
                    if (resp.obj.getHeadImg().equals("")) {
                        Glide.with(context).load(R.mipmap.user_head_img).into(holder.img_requestUserHead);
                    } else {
                        Glide.with(context).load(resp.obj.getHeadImg()).into(holder.img_requestUserHead);
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

    private class ViewHolder {
        private LinearLayout line_myRequest_item;
        TextView tv_list_state, tv_list_data, tv_requestUserName;
        SelectableRoundedImageView img_requestUserHead;
        Button btn_accept;
    }

    public void setOnFreshListener(OnFreshListener onFreshListener) {
        this.freshListener = onFreshListener;
    }

    public interface OnFreshListener {
        void fresh();
    }
}
