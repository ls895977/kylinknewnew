package com.maoyongxin.myapplication.ui.chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.RequestListInfo;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.request.ReqFindUserById;
import com.maoyongxin.myapplication.http.request.ReqGetRequestList;
import com.maoyongxin.myapplication.http.request.ReqGroup;
import com.maoyongxin.myapplication.http.response.GroupResponse;
import com.maoyongxin.myapplication.http.response.LoginResponse;
import com.maoyongxin.myapplication.http.response.RequestListResponse;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.ui.adapter.ConversationListAdapterEx;
import com.maoyongxin.myapplication.ui.widget.DragPointView;
import com.maoyongxin.myapplication.ui.widget.MorePopWindow;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imkit.model.UIConversation;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;
import io.rong.message.InformationNotificationMessage;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ChatBaseFragment extends Fragment{
    private Button btn_conversation_list;
    private Button btn_addressBook;
    //  private ViewPagerDoubleIndicator linTopindicator;
    private ViewPager vpViewPager;
    private ImageView img_Search;
    Unbinder unbinder;
    private List<Button> btnList;
    private List<Fragment> fragList;
    private int prePosition = 0;
    private DragPointView mUnreadNumView;
    private Uri headUri;
    private UserInfo userInfo;
    private ImageView nameBook;
    private String picurl;
    /**
     * 会话列表的fragment
     */
    private ConversationListFragment mConversationListFragment = null;
    private boolean isDebug;
    private Context mContext;
    private Conversation.ConversationType[] mConversationsTypes = null;//第一界面

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_base, container, false);
        btn_conversation_list= (Button) view.findViewById(R.id.btn_conversation_list);
        nameBook=(ImageView)view.findViewById(R.id.nameBook);

        img_Search= (ImageView) view.findViewById(R.id.img_search);
        img_Search.setOnClickListener(new View.OnClickListener() {//好友添加
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),FindAndSearchActivity.class));
                MorePopWindow morePopWindow = new MorePopWindow(getActivity());
                morePopWindow.showPopupWindow(img_Search);
            }
        });
        vpViewPager= (ViewPager) view.findViewById(R.id.vp_myviewPager);
        nameBook.setOnClickListener(new View.OnClickListener() {//通讯录
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),MyFriendsActivity.class);
                startActivity(intent);
            }
        });
        initData();
        shownewFragment();
        unbinder = ButterKnife.bind(this, view);
        return view;


    }

    private void initData() {
        headUri = Uri.parse((String) AppApplication.getCurrentUserInfo().getHeadImg());
        userInfo = new UserInfo(AppApplication.getCurrentUserInfo().getUserId(), AppApplication.getCurrentUserInfo().getUserName(), headUri);
        btnList = new ArrayList<Button>();
        btnList.add(btn_conversation_list);
        fragList = new ArrayList<Fragment>();
        Fragment conversationList = initConversationList();
        fragList.add(conversationList);
        listItemClick();
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    refreshSystemInfo();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(task,5000,5000);
    }


    private void refreshList() {
        RongIM.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(final List<Conversation> conversations) {
                if (conversations == null)
                {
                    return;
                }
                for (int i = 0; i < conversations.size(); i++) {
                    if(conversations.get(i).getConversationType()==Conversation.ConversationType.PRIVATE)
                    {
                        ReqFindUserById.findUser(getActivity(), getActivity().getClass().getSimpleName(),
                                conversations.get(i).getTargetId(), new EntityCallBack<LoginResponse>() {
                                    @Override
                                    public Class<LoginResponse> getEntityClass() {
                                        return LoginResponse.class;
                                    }

                                    @Override
                                    public void onSuccess(LoginResponse resp) {

                                        String id = resp.obj.getUserId();
                                        setRongUserInfo(
                                                id,resp.obj.getUserName(),
                                                Uri.parse(resp.obj.getHeadImg()));

                                        RongIM.getInstance().refreshUserInfoCache(new UserInfo(id,resp.obj.getUserName(),
                                                Uri.parse(resp.obj.getHeadImg())));
                                        //Log.e("tag","--------  ** "+id+"  "+resp.obj.getUserName());
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
                    else if(conversations.get(i).getConversationType()==Conversation.ConversationType.GROUP){
                        ReqGroup.getGroupInfo(getActivity(),
                                getTag(),
                                conversations.get(i).getTargetId(),
                                new EntityCallBack<GroupResponse>() {
                                    @Override
                                    public Class<GroupResponse> getEntityClass() {
                                        return GroupResponse.class;
                                    }

                                    @Override
                                    public void onSuccess(GroupResponse resp) {
                                        if (resp.is200()) {
                                            String id = resp.obj.getGroupId()+"";

                                            String groupName = resp.obj.getGroupName()+"";    ///TODO
                                            getgroupurl(id,groupName);



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
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });

//        Boolean isFirst = SpUtils.getBoolean(getContext(), "isFirst", false);
//        if (isFirst)
//        {
        refreshSystemInfo();
//        }
    }
    //设置融云用户信息
    private void setRongUserInfo(final String id, final String name, final Uri uri) {
        if (RongIM.getInstance() != null) {
            RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
                @Override
                public UserInfo getUserInfo(String s) {

                    return new UserInfo(id,name,uri);
                }
            }, true);
        }
    }

    private void setRongGroupInfo(final String groupId, final String groupName, final Uri uri)
    {
        if (RongIM.getInstance()!=null)
        {
            RongIM.setGroupInfoProvider(new RongIM.GroupInfoProvider() {
                @Override
                public Group getGroupInfo(String s) {
                    //Log.e("/'''[][][][]", "getGroupInfo: "+groupName );
                    return new Group(groupId,groupName,uri);
                }
            },true);
        }
    }

    private void   getgroupurl(final String groupId,final String groipName) {
        OkHttpUtils.post().url("http://st.3dgogo.com/index/chatgroup_gambit/get_chatgroup_image.html")
                .addParams("group_id",groupId)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                try {

                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getInt("code") == 200) {

                        picurl=jsonObject.getString("image");
                        setRongGroupInfo(groupId,
                                groipName,
                                Uri.parse(picurl)
                        );
                        RongIM.getInstance().refreshGroupInfoCache(new Group(groupId,groipName,Uri.parse(picurl)));

                    } else if (jsonObject.getInt("code") == 500) {

                        Toast.makeText(getActivity(), "抱歉，修改失败！请再次提交", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();


                }
            }
        });

    }
    private void refreshSystemInfo() {
        ReqGetRequestList.getList(getContext(), getTag(), AppApplication.getCurrentUserInfo().getUserId(), new EntityCallBack<RequestListResponse>() {
            @Override
            public Class<RequestListResponse> getEntityClass() {
                return RequestListResponse.class;
            }

            @Override
            public void onSuccess(RequestListResponse resp) {
                if (resp.is200())
                {
                    List<RequestListInfo.RequestList> requestListInfos = new ArrayList<RequestListInfo.RequestList>();
                    requestListInfos = resp.obj.getRequestList();

                    if (requestListInfos.size() != 0)
                    {

                        for (int i = 0;i < requestListInfos.size();i++)
                        {

                            if (!AppApplication.getCurrentUserInfo().getUserId().equals(requestListInfos.get(i).getUserId()))
                            {
                                if (requestListInfos.get(i).getRequestState().equals("1"))
                                {
                                    RongIM.getInstance().getRongIMClient().insertMessage(Conversation.ConversationType.SYSTEM,
                                            requestListInfos.get(0).getUserId(),
                                            null, InformationNotificationMessage.obtain("好友申请"),
                                            null);
                                }
                            }

                        }
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
    //设置融云用户信息
//    private void setRongUserInfo(final String id, final String name, final Uri uri) {
//        if (RongIM.getInstance() != null) {
//            RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
//                @Override
//                public UserInfo getUserInfo(String s) {
//
//                    return new UserInfo(id,name,uri);
//                }
//            }, true);
//        }
//    }


    private Fragment initConversationList() {

        if (mConversationListFragment == null) {
            ConversationListFragment listFragment = new ConversationListFragment();
            ConversationListAdapterEx adapterEx = new ConversationListAdapterEx(RongContext.getInstance());
            listFragment.setAdapter(adapterEx);

            Uri uri;
            if (isDebug) {
                uri = Uri.parse("rong://" + getActivity().getApplicationInfo().packageName).buildUpon()
                        .appendPath("conversationlist")
                        .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "true") //设置私聊会话是否聚合显示
                        .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")//群组
                        .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                        .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                        .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//系统
                        .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "true")

                        .build();
                mConversationsTypes = new Conversation.ConversationType[]{Conversation.ConversationType.PRIVATE,
                        Conversation.ConversationType.GROUP,
                        Conversation.ConversationType.PUBLIC_SERVICE,
                        Conversation.ConversationType.APP_PUBLIC_SERVICE,
                        Conversation.ConversationType.SYSTEM,
                        Conversation.ConversationType.DISCUSSION
                };

            } else {
                uri = Uri.parse("rong://" +getActivity().getApplicationInfo().packageName).buildUpon()
                        .appendPath("conversationlist")
                        .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                        .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//群组
                        .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                        .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                        .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//系统
                        .build();
                mConversationsTypes = new Conversation.ConversationType[]{Conversation.ConversationType.PRIVATE,
                        Conversation.ConversationType.GROUP,
                        Conversation.ConversationType.PUBLIC_SERVICE,
                        Conversation.ConversationType.APP_PUBLIC_SERVICE,
                        Conversation.ConversationType.SYSTEM

                };
            }
            listFragment.setUri(uri);
            mConversationListFragment = listFragment;
            return listFragment;
        } else {
            return mConversationListFragment;
        }




    }



    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> list;

        public ViewPagerAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public Fragment getItem(int position) {


            return list.get(0);

        }

        @Override
        public int getCount() {
            return list.size();
        }
    }

    /**
     * 展示fragment
     */
    private void shownewFragment() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        ViewPagerAdapter vpAdapter = new ViewPagerAdapter(fm, fragList);
        vpAdapter.notifyDataSetChanged();
        vpViewPager.setAdapter(vpAdapter);

        /**
         * 指示器联动
         */
        vpViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //   linTopindicator.scroll(position, positionOffset);
            }

            @Override
            public void onPageSelected(int position) {
                btnList.get(prePosition).setTextColor(Color.parseColor("#565656"));
                btnList.get(position).setTextColor(Color.parseColor("#999999"));
                prePosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        refreshList();
    }

    private void listItemClick() {
        RongIM.setConversationListBehaviorListener(new MyConversationListBehaviorListener());
    }


    private class MyConversationListBehaviorListener implements RongIM.ConversationListBehaviorListener{

        /** 当点击会话头像后执行。
         * @param context
         * @param conversationType
         * @param s
         * @return
         */
        @Override
        public boolean onConversationPortraitClick(Context context, Conversation.ConversationType conversationType, String s) {
            return false;
        }

        @Override
        public boolean onConversationPortraitLongClick(Context context, Conversation.ConversationType conversationType, String s) {
            return false;
        }

        @Override
        public boolean onConversationLongClick(Context context, View view, UIConversation uiConversation) {
            return false;
        }

        /** 点击会话列表中的 item 时执行。
         * @param context
         * @param view
         * @param uiConversation
         * @return
         */
        @Override
        public boolean onConversationClick(Context context, View view, UIConversation uiConversation) {
            String targetId = uiConversation.getConversationTargetId();
            Conversation.ConversationType type = uiConversation.getConversationType();
            if (type == Conversation.ConversationType.SYSTEM) {
                context.startActivity(new Intent(context, FriendMessagesActivity.class));
                return true;
            }
            return false;
        }
    }

}
