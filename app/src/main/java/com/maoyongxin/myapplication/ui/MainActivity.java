package com.maoyongxin.myapplication.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.request.ReqFindUserById;
import com.maoyongxin.myapplication.http.request.ReqGroup;
import com.maoyongxin.myapplication.http.request.ReqUploadLocation;
import com.maoyongxin.myapplication.http.request.ReqUserServer;
import com.maoyongxin.myapplication.http.response.BaiduPushResponse;
import com.maoyongxin.myapplication.http.response.BaseResp;
import com.maoyongxin.myapplication.http.response.GroupResponse;
import com.maoyongxin.myapplication.http.response.LoginResponse;
import com.maoyongxin.myapplication.myapp.AppActivity;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.myapp.AppConfig;
import com.maoyongxin.myapplication.server.utils.NToast;
import com.maoyongxin.myapplication.server.widget.LoadDialog;
import com.maoyongxin.myapplication.tool.NotificationReceiver;
import com.maoyongxin.myapplication.tool.PermissionsChecker;
import com.maoyongxin.myapplication.tool.PollingUtils;
import com.maoyongxin.myapplication.tool.SpUtils;
import com.maoyongxin.myapplication.tool.TagAliasOperatorHelper;
import com.maoyongxin.myapplication.ui.chat.ChatBaseFragment;
import com.maoyongxin.myapplication.ui.editapp.findfragment.MyDiscoveryFragment;
import com.maoyongxin.myapplication.ui.editapp.minefragment.MineFragment;
import com.maoyongxin.myapplication.ui.fragment.New_discover;
import com.maoyongxin.myapplication.ui.widget.DragPointView;
import com.maoyongxin.myapplication.ui.widget.MorePopWindow;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import io.rong.common.RLog;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ContactNotificationMessage;
import io.rong.message.InformationNotificationMessage;
import me.leolin.shortcutbadger.ShortcutBadger;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.maoyongxin.myapplication.tool.TagAliasOperatorHelper.ACTION_SET;
//import io.rong.toolkit.TestActivity;

@SuppressWarnings("deprecation")
public class MainActivity extends AppActivity implements
        ViewPager.OnPageChangeListener,
        View.OnClickListener, IUnReadMessageObserver
        , DragPointView.OnDragListencer{
    private static final int REQUEST_CODE = 0; // 请求码
    private static int sequence;
    public static ViewPager mViewPager;
    private List<Fragment> mFragment = new ArrayList<>();
    private ImageView moreImage, mImageChats, mImageContact, mImageFind, mImageMe, mMineRed;
    private TextView mTextChats, mTextContact, mTextFind, mTextMe;
    private DragPointView mUnreadNumView;
    private ImageView mSearchImageView;
    public RelativeLayout title;
    private NotificationReceiver receiver = new NotificationReceiver();
    private UserInfo userInfo;
    private Uri headUri;
    private TextView msg_number;
    private String picurl;
    /**
     * 会话列表的fragment
     */
    private long mLastBackMillis;
    private long mDoublePressExitPeriod;

    private String mDoublePressExitHint;
    private ConversationListFragment mConversationListFragment = null;
    private boolean isDebug;
    private Context mContext;
    private Conversation.ConversationType[] mConversationsTypes = null;
    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.CAMERA
    };
    private PermissionsChecker mPermissionsChecker; // 权限检测器


    @Override
    protected void initView() {
        super.initView();
        ActivityCollector.addActivity(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mContext = this;
        isDebug = false;
        initViews();
        changeTextViewColor();
        changeSelectedTabState(0);
        initMainViewPager();

        uploadLocation(AppApplication.getMyCurrentLocation().getLongtitude(), AppApplication.getMyCurrentLocation().getLatitude());

        mPermissionsChecker = new PermissionsChecker(this);

        makeSpAuto(MainActivity.this);

        ShortcutBadger.applyCount(MainActivity.this, 3); //for 1.1.4+

        setAlias();

    }


    public void uploadLocation(final String longtitude, final String latitude) {
        ReqUploadLocation.uploadLocation(getApplicationContext(), getActivityTag(), AppApplication.getCurrentUserInfo().getUserId(), longtitude, latitude, new EntityCallBack<BaseResp>() {
            @Override
            public Class<BaseResp> getEntityClass() {
                return BaseResp.class;
            }

            @Override
            public void onSuccess(BaseResp resp) {
                if (resp.is200()) {
//                    showToastShort("上传位置信息成功");
                } else {
                    uploadLocation(longtitude, latitude);
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
    protected void onResume() {
        super.onResume();
        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        }
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            showToastLong("请进入设置，开启定位等必须权限，否则无法正常使用");
        }
    }

    @Override
    protected void initEvent() {
        super.initEvent();

    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_main;
    }




    private void initViews() {
        enableDoublePressExit(2000, "再次点击退出程序");
        RelativeLayout chatRLayout = (RelativeLayout) findViewById(R.id.seal_chat);
        RelativeLayout contactRLayout = (RelativeLayout) findViewById(R.id.seal_contact_list);
        RelativeLayout foundRLayout = (RelativeLayout) findViewById(R.id.seal_find);
        RelativeLayout mineRLayout = (RelativeLayout) findViewById(R.id.seal_me);
        mImageChats = (ImageView) findViewById(R.id.tab_img_chats);
        mImageContact = (ImageView) findViewById(R.id.tab_img_contact);
        mImageFind = (ImageView) findViewById(R.id.tab_img_find);
        mImageMe = (ImageView) findViewById(R.id.tab_img_me);
        mTextChats = (TextView) findViewById(R.id.tab_text_chats);
        mTextContact = (TextView) findViewById(R.id.tab_text_contact);
        mTextFind = (TextView) findViewById(R.id.tab_text_find);
        mTextMe = (TextView) findViewById(R.id.tab_text_me);
        mMineRed = (ImageView) findViewById(R.id.mine_red);
        moreImage = (ImageView) findViewById(R.id.seal_more);
        mSearchImageView = (ImageView) findViewById(R.id.ac_iv_search);
        msg_number = (TextView) findViewById(R.id.msg_number);
        title = (RelativeLayout) findViewById(R.id.title);
        chatRLayout.setOnClickListener(this);
        contactRLayout.setOnClickListener(this);
        foundRLayout.setOnClickListener(this);
        mineRLayout.setOnClickListener(this);

        moreImage.setOnClickListener(this);
        mSearchImageView.setOnClickListener(this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//透明导航栏
         getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);



        //判断是否第一次进入，true 则插入一条欢迎消息
        Boolean isFirst = SpUtils.getBoolean(this, "isFirst", true);
        if (isFirst)
        {
            RongIM.getInstance().getRongIMClient().insertMessage(Conversation.ConversationType.PRIVATE,
                   "10069",
                    null, InformationNotificationMessage.obtain("欢迎加入彼信商业社区~~"),null);

            isFirst = false;
            SpUtils.putBoolean(this,"isFirst",false);
        }


        getMsgNumber();
        setRongUserInfo(getIntent().getStringExtra("userId"),
             AppApplication.getCurrentUserInfo().getUserName()+"",
                   Uri.parse(AppApplication.getCurrentUserInfo().getHeadImg()+""));



        RongIM.setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {
            @Override
            public boolean onReceived(Message message, int i) {
                //Log.e("wei", i + "=====");
              getMsgNumber();
                refreshList();

                refreshMsg();
                return false;
            }

            public void refreshList() {
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
                                                RongIM.getInstance().setMessageAttachedUserInfo(true);
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
                                        getActivityTag(),
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

                                            String groupName = resp.obj.getGroupId()+"";
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
            }
        });


        RongIM.setConversationBehaviorListener(new RongIM.ConversationBehaviorListener() {
            @Override
            public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {

                Intent in = new Intent(context, StrangerDetailActivity.class);
                in.putExtra("personId", userInfo.getUserId());
                context.startActivity(in);

                return false;
            }

            @Override
            public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
                return false;
            }

            @Override
            public boolean onMessageClick(Context context, View view, Message message) {
                return false;
            }

            @Override
            public boolean onMessageLinkClick(Context context, String s) {
                return false;
            }

            @Override
            public boolean onMessageLongClick(Context context, View view, Message message) {
                return false;
            }
        });
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
    //设置融云用户信息
    private void setRongUserInfo(final String id, final String name, final Uri uri) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (RongIM.getInstance() != null) {
                    RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
                        @Override
                        public UserInfo getUserInfo(String s) {

                            return new UserInfo(id,name,uri);
                        }
                    }, true);
                }
            }
        }).start();

    }

    private void setRongGroupInfo(final String groupId, final String groupName, final Uri uri)
    {
        if (RongIM.getInstance()!=null)
        {
            RongIM.setGroupInfoProvider(new RongIM.GroupInfoProvider() {
                @Override
                public Group getGroupInfo(String s) {
                    return new Group(groupId,groupName,uri);
                }
            },true);
        }
    }



    public void refreshMsg() {
      RongIM.getInstance().clearMessages(Conversation.ConversationType.NONE,this.getClass()
      .getSimpleName());
      RongIM.getInstance().removeConversation(Conversation.ConversationType.NONE,this.getClass().getSimpleName());
    }

    private void getMsgNumber() {
        RongIMClient.getInstance().getTotalUnreadCount(new RongIMClient.ResultCallback<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                Log.i("wei", integer + "");
                if (integer == 0) {
                    //msg_number.setVisibility(View.GONE);
                } else if (integer > 99) {
                    //   msg_number.setVisibility(View.VISIBLE);
                    //   msg_number.setText("99");
                } else {
                    //   msg_number.setVisibility(View.VISIBLE);
                    //  msg_number.setText(String.valueOf(integer));
                }

            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getMsgNumber();
    }

    private void initMainViewPager() {

        mViewPager = (ViewPager) findViewById(R.id.main_viewpager);
        mUnreadNumView = (DragPointView) findViewById(R.id.seal_num);
        mUnreadNumView.setOnClickListener(this);
        mUnreadNumView.setDragListencer(this);



        mFragment.add(new ChatBaseFragment());
        mFragment.add(new New_discover());
        mFragment.add(new MyDiscoveryFragment());
        mFragment.add(new MineFragment());




        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragment.get(position);
            }

            @Override
            public int getCount() {
                return mFragment.size();
            }
        };
        mViewPager.setAdapter(fragmentPagerAdapter);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setOnPageChangeListener(this);
        mViewPager.setCurrentItem(0);
        initData();
    }

    @Override
    public void onDestroy() {
        RongIM.getInstance().removeUnReadMessageCountChangedObserver(this);
        System.out.println("Stop polling service...");
        PollingUtils.stopPollingService(this, PollingService.class, PollingService.ACTION);
        super.onDestroy();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        changeTextViewColor();
        changeSelectedTabState(position);
        if (position == 1) {
            title.setVisibility(View.VISIBLE);
        } else {
            title.setVisibility(View.GONE);
        }
    }

    private void changeTextViewColor() {
        mImageChats.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_chat));
        mImageContact.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_contacts));
        mImageFind.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_found));
        mImageMe.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_me));
        mTextChats.setTextColor(Color.parseColor("#abadbb"));
        mTextContact.setTextColor(Color.parseColor("#abadbb"));
        mTextFind.setTextColor(Color.parseColor("#abadbb"));
        mTextMe.setTextColor(Color.parseColor("#abadbb"));
    }

    private void changeSelectedTabState(int position) {
        switch (position) {
            case 2:
                mTextChats.setTextColor(Color.parseColor("#0099ff"));
                mImageChats.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_chat_hover));
                break;
            case 0:
                mTextContact.setTextColor(Color.parseColor("#0099ff"));
                mImageContact.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_contacts_hover));
                break;
            case 1:
                mTextFind.setTextColor(Color.parseColor("#0099ff"));
                mImageFind.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_found_hover));
                break;
            case 3:
                mTextMe.setTextColor(Color.parseColor("#0099ff"));
                mImageMe.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_me_hover));
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    long firstClick = 0;
    long secondClick = 0;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.seal_contact_list:
                title.setVisibility(View.GONE);
                if (mViewPager.getCurrentItem() == 0) {
                    if (firstClick == 0) {
                        firstClick = System.currentTimeMillis();
                    } else {
                        secondClick = System.currentTimeMillis();
                    }
                    RLog.i("MainActivity", "time = " + (secondClick - firstClick));
                    if (secondClick - firstClick > 0 && secondClick - firstClick <= 800) {
//                        mConversationListFragment.focusUnreadItem();
                        firstClick = 0;
                        secondClick = 0;
                    } else if (firstClick != 0 && secondClick != 0) {
                        firstClick = 0;
                        secondClick = 0;
                    }
                }
                mViewPager.setCurrentItem(0, false);
                break;
            case R.id.seal_find:
                title.setVisibility(View.GONE);
                mViewPager.setCurrentItem(1, false);
                break;
            case R.id.seal_chat:
                title.setVisibility(View.VISIBLE);
                mViewPager.setCurrentItem(2, false);
                break;
            case R.id.seal_me:
                title.setVisibility(View.GONE);
                mViewPager.setCurrentItem(3, false);
                mMineRed.setVisibility(View.GONE);
                break;
            case R.id.seal_more:
                MorePopWindow morePopWindow = new MorePopWindow(MainActivity.this);
                morePopWindow.showPopupWindow(moreImage);
                break;
            case R.id.ac_iv_search:
                startActivity(new Intent(MainActivity.this, SearchUserActivity.class));
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getBooleanExtra("systemconversation", false)) {
            mViewPager.setCurrentItem(0, false);
        }
    }

    protected void initData() {
        final Conversation.ConversationType[] conversationTypes = {
                Conversation.ConversationType.PRIVATE,
                Conversation.ConversationType.GROUP, Conversation.ConversationType.SYSTEM,
                Conversation.ConversationType.PUBLIC_SERVICE, Conversation.ConversationType.APP_PUBLIC_SERVICE
        };
        RongIM.getInstance().addUnReadMessageCountChangedObserver(this, conversationTypes);
        getConversationPush();// 获取 push 的 id 和 target
        getPushMessage();





      //


    }

    private void getConversationPush() {
        if (getIntent() != null && getIntent().hasExtra("PUSH_CONVERSATIONTYPE") && getIntent().hasExtra("PUSH_TARGETID")) {

            final String conversationType = getIntent().getStringExtra("PUSH_CONVERSATIONTYPE");
            final String targetId = getIntent().getStringExtra("PUSH_TARGETID");


            RongIM.getInstance().getConversation(Conversation.ConversationType.valueOf(conversationType), targetId, new RongIMClient.ResultCallback<Conversation>() {
                @Override
                public void onSuccess(Conversation conversation) {

                    if (conversation != null) {

                        if (conversation.getLatestMessage() instanceof ContactNotificationMessage) { //好友消息的push
//                            startActivity(new Intent(MainActivity.this, NewFriendListActivity.class));
                        } else {
                            Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon().appendPath("conversation")
                                    .appendPath(conversationType).appendQueryParameter("targetId", targetId).build();
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(uri);
                            startActivity(intent);
                        }
                    }
                }

                @Override
                public void onError(RongIMClient.ErrorCode e) {

                }
            });
        }
    }

    /**
     * 得到不落地 push 消息
     */
    private void getPushMessage() {
        Intent intent = getIntent();
        if (intent != null && intent.getData() != null && intent.getData().getScheme().equals("rong")) {
            String path = intent.getData().getPath();
            if (path.contains("push_message")) {
                SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
                String cacheToken = sharedPreferences.getString("loginToken", "");
                if (TextUtils.isEmpty(cacheToken)) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                } else {
                    if (!RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED)) {
                        LoadDialog.show(mContext);
                        RongIM.connect(cacheToken, new RongIMClient.ConnectCallback() {
                            @Override
                            public void onTokenIncorrect() {
                                LoadDialog.dismiss(mContext);
                            }

                            @Override
                            public void onSuccess(String s) {
                                LoadDialog.dismiss(mContext);
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode e) {
                                LoadDialog.dismiss(mContext);
                            }
                        });
                    }
                }
            }
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus() && event.getAction() == MotionEvent.ACTION_UP) {
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onDragOut() {
        mUnreadNumView.setVisibility(View.GONE);
        NToast.shortToast(getActivity(), getString(R.string.clear_success));
        RongIM.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                if (conversations != null && conversations.size() > 0) {
                    for (Conversation c : conversations) {
                        RongIM.getInstance().clearMessagesUnreadStatus(c.getConversationType(), c.getTargetId(), null);
                    }
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode e) {

            }
        }, mConversationsTypes);
    }

    @Override
    public void onCountChanged(int i) {
        if (i == 0) {
            mUnreadNumView.setVisibility(View.GONE);
        } else if (i > 0 && i < 100) {
            mUnreadNumView.setVisibility(View.VISIBLE);
            mUnreadNumView.setText(String.valueOf(i));
        } else {
            mUnreadNumView.setVisibility(View.VISIBLE);
            mUnreadNumView.setText(R.string.no_read_message);
        }
    }

    private void setAlias() {
        TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean();
        tagAliasBean.action = ACTION_SET;
        sequence++;
        tagAliasBean.alias = AppApplication.getCurrentUserInfo().getUserId();
        tagAliasBean.isAliasAction = true;
        Log.d("push", tagAliasBean.toString());
        TagAliasOperatorHelper.getInstance().handleAction(mContext.getApplicationContext(), sequence, tagAliasBean);

        Log.d("----", "id is:" + JPushInterface.getRegistrationID(this));
        ReqUserServer.uploadBaiduPushInfo(this, "id",
                Long.parseLong(AppApplication.getCurrentUserInfo().getUserId()),
                JPushInterface.getRegistrationID(this), AppConfig.DEVICE_TYPE, new EntityCallBack<BaiduPushResponse>() {
                    @Override
                    public Class<BaiduPushResponse> getEntityClass() {
                        return BaiduPushResponse.class;
                    }

                    @Override
                    public void onSuccess(BaiduPushResponse resp) {
                        Log.d("---", "success");
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
