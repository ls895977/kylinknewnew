package com.maoyongxin.myapplication.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.FriendsInfo;
import com.maoyongxin.myapplication.myapp.AppConfig;
import com.maoyongxin.myapplication.server.widget.SelectableRoundedImageView;
import com.maoyongxin.myapplication.ui.StrangerDetailActivity;
import com.maoyongxin.myapplication.ui.UserDetailActivity;

import java.util.List;

import io.rong.imkit.RongIM;

/**
 * Created by dingke on 2017/8/21.
 */

public class SortGroupMemberAdapter extends BaseAdapter implements SectionIndexer {

    private List<FriendsInfo.FriendList> list = null;
    private Context mContext;
    private List<FriendsInfo.FriendList> addressBookInfos;
    private Intent intent;

    public SortGroupMemberAdapter(Context mContext, List<FriendsInfo.FriendList> addressBookInfos) {
        this.mContext = mContext;
        this.list = addressBookInfos;
        this.addressBookInfos = addressBookInfos;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<FriendsInfo.FriendList> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder viewHolder = null;
        final FriendsInfo.FriendList mContent = list.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.activity_group_member_item, null);
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
            viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
            viewHolder.tvBuMen = (TextView) view.findViewById(R.id.bumen);
            viewHolder.ivTouXiang = (SelectableRoundedImageView) view.findViewById(R.id.addressbook_touxiang_item);
            viewHolder.line_addressbook_item = (LinearLayout) view.findViewById(R.id.line_addressbook_item);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        // 根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);

        // 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            viewHolder.tvLetter.setVisibility(View.VISIBLE);
            viewHolder.tvLetter.setText(mContent.getSortLetters());
        } else {
            viewHolder.tvLetter.setVisibility(View.GONE);
        }

        viewHolder.tvTitle.setText(this.list.get(position).getUserNoteName());
        final String headImg;
        if (addressBookInfos.get(position).getHeadImg() != null && !addressBookInfos.get(position).getHeadImg().equals("")) {
            headImg=AppConfig.sRootUrl+"/logincontroller/getHeadImg/"+addressBookInfos.get(position).getHeadImg();
            Glide.with(mContext).load(headImg).into(viewHolder.ivTouXiang);
        } else {
            headImg=R.mipmap.user_head_img+"";
            Glide.with(mContext).load(Integer.parseInt(headImg)).into(viewHolder.ivTouXiang);
        }
        viewHolder.tvBuMen.setText("昵称:" + addressBookInfos.get(position).getUserNoteName());
        viewHolder.line_addressbook_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent intent = new Intent(mContext, StrangerDetailActivity.class);
                //intent.putExtra("inType", "my_friends");
                // intent.putExtra("userName", addressBookInfos.get(position).getUserNoteName());
                // intent.putExtra("personId", addressBookInfos.get(position).getUserId());
                // intent.putExtra("userHeadImg",headImg);
               // mContext.startActivity(intent);
                RongIM.getInstance().startPrivateChat(mContext, addressBookInfos.get(position).getUserId(), addressBookInfos.get(position).getUserNoteName());
            }
        });
        return view;

    }


    final static class ViewHolder {
        LinearLayout line_addressbook_item;
        TextView tvLetter;
        TextView tvTitle;
        TextView tvBuMen;
        SelectableRoundedImageView ivTouXiang;
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return list.get(position).getSortLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    /**
     * 提取英文的首字母，非英文字母用#代替。
     *
     * @param str
     * @return
     */
    private String getAlpha(String str) {
        String sortStr = str.trim().substring(0, 1).toUpperCase();
        // 正则表达式，判断首字母是否是英文字母
        if (sortStr.matches("[A-Z]")) {
            return sortStr;
        } else {
            return "#";
        }
    }

    @Override
    public Object[] getSections() {
        return null;
    }
}

