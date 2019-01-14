package com.maoyongxin.myapplication.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.GroupMessageInfo;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.request.ReqGroup;
import com.maoyongxin.myapplication.http.response.BaseResp;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.server.utils.NToast;

import java.util.List;

/**
 * Created by maoyongxin on 2018/1/11.
 */

public class GroupMessageAdapter extends BaseAdapter {
    private Context context;
    private List<GroupMessageInfo.UserList> messages;
    private LayoutInflater inflater;
    private OnfreshListener onfreshListener;

    public void setOnfreshListener(OnfreshListener onfreshListener) {
        this.onfreshListener = onfreshListener;
    }

    public GroupMessageAdapter(Context context, List<GroupMessageInfo.UserList> messages) {
        this.context = context;
        this.messages = messages;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_group_message, null);
            holder.tv_messageTitle = (TextView) convertView.findViewById(R.id.tv_messageTitle);
            holder.tv_messageNote = (TextView) convertView.findViewById(R.id.tv_messageNote);
            holder.btn_yes = (Button) convertView.findViewById(R.id.btn_yes);
            holder.btn_no = (Button) convertView.findViewById(R.id.btn_no);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_messageTitle.setText(messages.get(position).getJoinUserName() + "请求入群");
        holder.tv_messageNote.setText("备注：" + messages.get(position).getJoinUserNote());
        holder.btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dealMessage(2 + "", messages.get(position).getJoinUserId() + "");
            }
        });
        holder.btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dealMessage(1 + "", messages.get(position).getJoinUserId() + "");
            }
        });
        return convertView;
    }

    private class ViewHolder {
        private TextView tv_messageTitle, tv_messageNote;
        private Button btn_yes, btn_no;
    }

    public interface OnfreshListener {
        void fresh();
    }

    private void dealMessage(String state, String joinuserId) {
        ReqGroup.dealGroupMessages(context, "adapter", AppApplication.getCurrentUserInfo().getUserId(), messages.get(0).getGroupId() + "", joinuserId, state, new EntityCallBack<BaseResp>() {
            @Override
            public Class<BaseResp> getEntityClass() {
                return BaseResp.class;
            }

            @Override
            public void onSuccess(BaseResp resp) {
                if (resp.is200()) {
                    NToast.shortToast(context,resp.msg);
                    onfreshListener.fresh();
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
