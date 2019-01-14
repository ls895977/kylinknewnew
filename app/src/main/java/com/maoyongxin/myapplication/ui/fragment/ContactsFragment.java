package com.maoyongxin.myapplication.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.FriendsInfo;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.request.ReqGetMyFriendsList;
import com.maoyongxin.myapplication.http.response.FriendsResponse;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.server.CharacterParser;
import com.maoyongxin.myapplication.server.PinyinComparator;
import com.maoyongxin.myapplication.server.utils.NToast;
import com.maoyongxin.myapplication.server.widget.SelectableRoundedImageView;
import com.maoyongxin.myapplication.ui.GroupListActivity;
import com.maoyongxin.myapplication.ui.MyFollowActivity;
import com.maoyongxin.myapplication.ui.PublishPartActivity;
import com.maoyongxin.myapplication.ui.SearchUserActivity;
import com.maoyongxin.myapplication.ui.adapter.SortGroupMemberAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imkit.mention.SideBar;

/**
 * tab 2 通讯录的 Fragment
 * Created by Bob on 2015/1/25.
 */
public class ContactsFragment extends Fragment implements View.OnClickListener {
    private LinearLayout line_header;
    private SelectableRoundedImageView mSelectableRoundedImageView;
    private TextView mNameTextView;
    private TextView mNoFriends;
    private TextView mUnreadTextView;
    private View mHeadView;
    private EditText mSearchEditText;
    private ListView mListView;
    private PinyinComparator mPinyinComparator;
    private SideBar mSidBar;
    /**
     * 中部展示的字母提示
     */
    private TextView mDialogTextView;

    private List<FriendsInfo.FriendList> mFilteredFriendList;
    /**
     * 好友列表的 mFriendListAdapter
     */
    private SortGroupMemberAdapter adapter;
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser mCharacterParser;
    /**
     * 根据拼音来排列ListView里面的数据类
     */

    private String mId;
    private String mCacheName;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_address, container, false);
        initView(view);
        initData();
        getFriendList();
        return view;
    }

    private void initView(View view) {
        mSearchEditText = (EditText) view.findViewById(R.id.search);
        mListView = (ListView) view.findViewById(R.id.country_lvcountry);
        mNoFriends = (TextView) view.findViewById(R.id.title_layout_no_friends);
        mSidBar = (SideBar) view.findViewById(R.id.sidrbar);
        mDialogTextView = (TextView) view.findViewById(R.id.title_layout_catalog);

        mSidBar.setTextView(mDialogTextView);
        LayoutInflater mLayoutInflater = LayoutInflater.from(getActivity());
        mHeadView = mLayoutInflater.inflate(R.layout.item_contact_list_header, null);
        mUnreadTextView = (TextView) mHeadView.findViewById(R.id.tv_unread);
        line_header = (LinearLayout) mHeadView.findViewById(R.id.line_header);
        line_header.setVisibility(View.GONE);
        RelativeLayout newFriendsLayout = (RelativeLayout) mHeadView.findViewById(R.id.re_newfriends);
        RelativeLayout groupLayout = (RelativeLayout) mHeadView.findViewById(R.id.re_chatroom);
        RelativeLayout qunLayout = (RelativeLayout) mHeadView.findViewById(R.id.re_group);
        RelativeLayout publicServiceLayout = (RelativeLayout) mHeadView.findViewById(R.id.publicservice);
        RelativeLayout selfLayout = (RelativeLayout) mHeadView.findViewById(R.id.contact_me_item);
        mSelectableRoundedImageView = (SelectableRoundedImageView) mHeadView.findViewById(R.id.contact_me_img);
        mNameTextView = (TextView) mHeadView.findViewById(R.id.contact_me_name);
//        mListView.addHeaderView(mHeadView);
        updatePersonalUI();
        selfLayout.setOnClickListener(this);
        groupLayout.setOnClickListener(this);
        newFriendsLayout.setOnClickListener(this);
        publicServiceLayout.setOnClickListener(this);
        qunLayout.setOnClickListener(this);
        //设置右侧触摸监听
        mSidBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mListView.setSelection(position);
                }

            }
        });
        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void updatePersonalUI() {
        mId = AppApplication.getCurrentUserInfo().getUserId();
        mCacheName = AppApplication.getCurrentUserInfo().getUserName();
        mNameTextView.setText(mCacheName);
        if (AppApplication.getCurrentUserInfo().getHeadImg() != null && !AppApplication.getCurrentUserInfo().getHeadImg().equals("")) {
            Glide.with(this).load(AppApplication.getCurrentUserInfo().getHeadImg()).into(mSelectableRoundedImageView);
        } else {
            Glide.with(this).load(R.mipmap.user_head_img).into(mSelectableRoundedImageView);
        }
    }

    private void getFriendList() {
        ReqGetMyFriendsList.getFriends(getActivity(), getTag(), AppApplication.getCurrentUserInfo().getUserId(), new EntityCallBack<FriendsResponse>() {
            @Override
            public Class<FriendsResponse> getEntityClass() {
                return FriendsResponse.class;
            }

            @Override
            public void onSuccess(FriendsResponse resp) {
                if (resp.is200()) {
                    mFilteredFriendList = resp.obj.getFriendList();
                    String[] names = new String[mFilteredFriendList.size()];
                    for (int i = 0; i < mFilteredFriendList.size(); i++) {
                        names[i] = mFilteredFriendList.get(i).getUserName();
                    }
                    mFilteredFriendList = filledData(mFilteredFriendList);
                    // 根据a-z进行排序源数据
                    Collections.sort(mFilteredFriendList, mPinyinComparator);
                    adapter = new SortGroupMemberAdapter(getActivity(), mFilteredFriendList);
                    mListView.setAdapter(adapter);
                    if (mFilteredFriendList.size() == 0) {
                        mNoFriends.setVisibility(View.VISIBLE);
                    } else {
                        mNoFriends.setVisibility(View.GONE);
                    }
                } else {
                    NToast.shortToast(getActivity(), resp.msg);
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

    @Override
    public void onResume() {
        super.onResume();
        getFriendList();
        if (AppApplication.getCurrentUserInfo().getHeadImg().equals("")) {
            Glide.with(this).load(R.mipmap.user_head_img).into(mSelectableRoundedImageView);
        } else {
            Glide.with(this).load(AppApplication.getCurrentUserInfo().getHeadImg()).into(mSelectableRoundedImageView);
        }
        mNameTextView.setText(AppApplication.getCurrentUserInfo().getUserName());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.contact_me_item:
                RongIM.getInstance().startPrivateChat(getActivity(), mId, mCacheName);
                break;
            case R.id.re_newfriends:
                startActivity(new Intent(getActivity(), SearchUserActivity.class));
                break;
            case R.id.publicservice:
                startActivity(new Intent(getActivity(), PublishPartActivity.class));
                break;
            case R.id.re_chatroom://我的关注
                startActivity(new Intent(getActivity(), MyFollowActivity.class));
                break;
            case R.id.re_group://群聊
                startActivity(new Intent(getActivity(), GroupListActivity.class));
                break;
        }
    }

    private void initData() {
        mFilteredFriendList = new ArrayList<>();
        mListView.setAdapter(adapter);
        //实例化汉字转拼音类
        mCharacterParser = CharacterParser.getInstance();
        mPinyinComparator = new PinyinComparator();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mDialogTextView != null) {
            mDialogTextView.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 为ListView填充数据
     *
     * @param date
     * @return
     */
    private List<FriendsInfo.FriendList> filledData(List<FriendsInfo.FriendList> date) {
        List<FriendsInfo.FriendList> mSortList = new ArrayList<FriendsInfo.FriendList>();

        for (int i = 0; i < date.size(); i++) {
            FriendsInfo.FriendList sortModel = new FriendsInfo.FriendList();
            sortModel.setUserName(date.get(i).getUserName());
            sortModel.setUserId(date.get(i).getUserId());
            sortModel.setUserPhone(date.get(i).getUserPhone());
            sortModel.setHeadImg(date.get(i).getHeadImg());
            sortModel.setUserNoteName(date.get(i).getUserNoteName());
            // 汉字转换成拼音
            String pinyin = mCharacterParser.getSpelling(date.get(i).getUserName());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<FriendsInfo.FriendList> filterDateList = new ArrayList<FriendsInfo.FriendList>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = mFilteredFriendList;
            mNoFriends.setVisibility(View.GONE);
        } else {
            filterDateList.clear();
            for (FriendsInfo.FriendList sortModel : mFilteredFriendList) {
                String name = sortModel.getUserName();
                if (name.indexOf(filterStr.toString()) != -1
                        || mCharacterParser.getSpelling(name).startsWith(
                        filterStr.toString())) {
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, mPinyinComparator);
        adapter.updateListView(filterDateList);
        if (filterDateList.size() == 0) {
            mNoFriends.setVisibility(View.GONE);
        }
    }

    public Object[] getSections() {
        return null;
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return mFilteredFriendList.get(position).getSortLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < mFilteredFriendList.size(); i++) {
            String sortStr = mFilteredFriendList.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

}
