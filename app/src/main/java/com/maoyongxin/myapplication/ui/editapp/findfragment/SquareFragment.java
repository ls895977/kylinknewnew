package com.maoyongxin.myapplication.ui.editapp.findfragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import android.support.annotation.UiThread;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.CardView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.FollowListInfo;
import com.maoyongxin.myapplication.entity.MyDynamicListInfo;
import com.maoyongxin.myapplication.entity.shanghuiInfo;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.request.ReqGetFollowList;
import com.maoyongxin.myapplication.http.request.ReqMyDynamicList;
import com.maoyongxin.myapplication.http.response.FollowListResponse;
import com.maoyongxin.myapplication.http.response.MyDongTaiListResponse;
import com.maoyongxin.myapplication.myapp.AppApplication;

import com.maoyongxin.myapplication.tool.AddPicture_PopupWindow;
import com.maoyongxin.myapplication.ui.EventMessage;
import com.maoyongxin.myapplication.ui.JiGuangSharePlatformModel;
import com.maoyongxin.myapplication.ui.editapp.editadapter.SquareAdapter;
import com.maoyongxin.myapplication.ui.fragment.ShareDialogFragment;
import com.maoyongxin.myapplication.ui.groupchat.GroupAllList;
import com.maoyongxin.myapplication.ui.groupchat.GroupChatDetailActivity;
import com.maoyongxin.myapplication.ui.groupchat.GroupChatDetailNewActivity;
import com.maoyongxin.myapplication.ui.groupchat.GroupChatHuatiDetial;
import com.maoyongxin.myapplication.ui.luXiangActivity;
import com.maoyongxin.myapplication.ui.square.SquareBaseActivity;
import com.maoyongxin.myapplication.ui.square.SquareVideoActivity;
import com.maoyongxin.myapplication.ui.widget.CircularAnimUtil;
import com.maoyongxin.myapplication.ui.widget.mylistview.LoadListView;
import com.video.utils.CheckPermissionsUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jiguang.share.android.api.JShareInterface;
import cn.jiguang.share.android.api.PlatActionListener;
import cn.jiguang.share.android.api.Platform;
import cn.jiguang.share.android.api.ShareParams;
import cn.jiguang.share.wechat.Wechat;
import cn.jiguang.share.wechat.WechatMoments;
import de.greenrobot.event.EventBus;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;
import static com.maoyongxin.myapplication.myapp.AppFragment.showToastShort;
/**
 * 社区=动态
 * Created by maoyongxin on 2017/11/23.
 */
public class SquareFragment extends Fragment implements ShareDialogFragment.Listener, View.OnClickListener {
    private LoadListView lv_square;
    private ImageView img_dongtai_floating;
    private int listIndex;
    private SquareAdapter listAdapter;
    private CardView sh_card1, sh_card2, sh_card3;
    private ImageView sh_img1, sh_img2, sh_img3;
    private TextView sh_name1, sh_name2, sh_name3;
    private TextView sh_num1, sh_num2, sh_num3, tv_showMore, msg_count;
    private CardView cd_pop;
    private List<shanghuiInfo> shanghuiList = new ArrayList<>();
    private String groupName, groupId, groupImg, membernum, integralNum, holderId, groupNote;
    private String msmcount = "0";
    private ArrayList<JiGuangSharePlatformModel> list = new ArrayList<>();
    private int pagepopNext = 1;
    private static final int UPDATEONE = 0;
    private String ApiPop = "http://st.3dgogo.com/index/chatgroup_gambit/get_chat_group_hottest?page=";
    private Handler handler;
    private List<MyDynamicListInfo.DynamicList> dynamicLists;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            listIndex = 0;
            getStrangerDynamic();
        }
    };
    private Bitmap shareBit;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        View view = inflater.inflate(R.layout.fragment_square, null);
        lv_square = (LoadListView) view.findViewById(R.id.lv_square);
        View lvHead = inflater.inflate(R.layout.hangye_head, null);
        msg_count = (TextView) lvHead.findViewById(R.id.msg_count);
        cd_pop = (CardView) lvHead.findViewById(R.id.cd_pop);
        tv_showMore = (TextView) lvHead.findViewById(R.id.tv_showMore);
        sh_card1 = (CardView) lvHead.findViewById(R.id.shanghui_card1);
        sh_card2 = (CardView) lvHead.findViewById(R.id.shanghui_card2);
        sh_card3 = (CardView) lvHead.findViewById(R.id.shanghui_card3);
        sh_img1 = (ImageView) lvHead.findViewById(R.id.shanghui_img1);
        sh_img2 = (ImageView) lvHead.findViewById(R.id.shanghui_img2);
        sh_img3 = (ImageView) lvHead.findViewById(R.id.shanghui_img3);
        sh_name1 = (TextView) lvHead.findViewById(R.id.shanghui_name1);
        sh_name2 = (TextView) lvHead.findViewById(R.id.shanghui_name2);
        sh_name3 = (TextView) lvHead.findViewById(R.id.shanghui_name3);
        sh_num1 = (TextView) lvHead.findViewById(R.id.shanghui_num1);
        sh_num2 = (TextView) lvHead.findViewById(R.id.shanghui_num2);
        sh_num3 = (TextView) lvHead.findViewById(R.id.shanghui_num3);
        cd_pop.setOnClickListener(this);
        tv_showMore.setOnClickListener(this);
        sh_card1.setOnClickListener(this);
        sh_card2.setOnClickListener(this);
        sh_card3.setOnClickListener(this);
        lv_square.addHeaderView(lvHead);
        img_dongtai_floating = (ImageView) view.findViewById(R.id.img_dongtai_floating);
        getMyFollowList();
        img_dongtai_floating.setOnClickListener(this);
        listIndex = 0;
        getPoplisthere(ApiPop, pagepopNext);
        getStrangerDynamic();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == UPDATEONE) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            updateHeadview();
                        }
                    }, 1);
                }

            }
        };
        return view;
    }

    AddPicture_PopupWindow addPicture_popupWindow;

    // hader弹框
    private void pai() {
        addPicture_popupWindow = new AddPicture_PopupWindow(getActivity());
        addPicture_popupWindow.showAtLocation(
                sh_img1, Gravity.BOTTOM
                        | Gravity.CENTER, 0, 0);
        addPicture_popupWindow.setClick(new AddPicture_PopupWindow.PopClickListener() {
            @Override
            public void btn1() {
                Intent intent = new Intent(getActivity(), SquareBaseActivity.class);
                startActivity(intent);
            }

            @Override
            public void btn2() {
                Intent intent = new Intent(getActivity(), luXiangActivity.class);
                startActivityForResult(intent, 0x123);
            }
        });
    }


    @Subscribe
    public void onEventMainThread(EventMessage eventMessage) {
        switch (eventMessage.getType()) {
            case 1:
                cd_pop.setVisibility(View.GONE);
                break;
            case 2:
                msmcount = eventMessage.getData().toString();
                cd_pop.setVisibility(View.VISIBLE);
                msg_count.setText("您有" + msmcount + "条新回复");
                break;
            case 3:
                msg_count.setText(eventMessage.getData().toString());
                searchDynamic(eventMessage.getData().toString());
                break;
        }


    }
    private void searchDynamic(final String keyword) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.get().url("http://st.3dgogo.com/index/user_dynamic/search_user_dynamic_api.html")
                        .addParams("search", keyword)
                        .build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                    }
                });
            }
        }).start();
    }
    private void updateHeadview() {
        sh_name1.setText(shanghuiList.get(0).getGroupName().toString());
        sh_name2.setText(shanghuiList.get(1).getGroupName().toString());
        sh_name3.setText(shanghuiList.get(2).getGroupName().toString());
        Glide.with(getActivity()).load(shanghuiList.get(0).getPic()).into(sh_img1);
        Glide.with(getActivity()).load(shanghuiList.get(1).getPic()).into(sh_img2);
        Glide.with(getActivity()).load(shanghuiList.get(2).getPic()).into(sh_img3);
    }
    private void getPoplisthere(String api_url, int page) {
        String api = api_url + page + "";
        OkHttpUtils.post().url(api).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray data = jsonObject.getJSONObject("info").getJSONArray("data");
                    shanghuiList.clear();
                    for (int i = 0; i < 3; i++) {
                        shanghuiInfo newshanghuiInfo = new shanghuiInfo();
                        JSONObject newList = data.getJSONObject(i);
                        groupName = parse_Value(newList, "groupName");
                        groupId = parse_Value(newList, "groupId");
                        groupImg = parse_Value(newList, "image");
                        membernum = parse_Value(newList, "memberNum");
                        integralNum = parse_Value(newList, "integralNum");
                        holderId = parse_Value(newList, "groupHostId");
                        groupNote = parse_Value(newList, "groupNote");
                        newshanghuiInfo.setshanghuiInfo(groupImg, groupName, groupId, membernum, integralNum, groupName, holderId, groupNote);
                        shanghuiList.add(newshanghuiInfo);
                        pagepopNext = jsonObject.getJSONObject("info").getInt("next_page");
                    }
                    Message msg = new Message();
                    msg.what = UPDATEONE;
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

    }
    private String parse_Value(JSONObject data, String value) {
        String com_value = "";
        if (data.has(value)) {
            try {
                com_value = data.getString(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return com_value;
    }
    private void initEvent() {
        lv_square.setInterface(new LoadListView.ILoadListener() {
            @Override
            public void onLoad() {
                listIndex++;
                getStrangerDynamic();
            }
        });
        lv_square.setReflashInterface(new LoadListView.RLoadListener() {
            @Override
            public void onRefresh() {
                listIndex = 0;
                getStrangerDynamic();
            }
        });

    }
    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction("refreshList");
        getActivity().registerReceiver(receiver, filter);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
    }
    private void getStrangerDynamic() {
        ReqMyDynamicList.getStrangerDynamicList(getActivity(), getTag(), AppApplication.getCurrentUserInfo().getUserId(), listIndex + "", new EntityCallBack<MyDongTaiListResponse>() {
            @Override
            public Class<MyDongTaiListResponse> getEntityClass() {
                return MyDongTaiListResponse.class;
            }

            @Override
            public void onSuccess(MyDongTaiListResponse resp) {
                if (resp.is200()) {
                    if (listIndex == 0) {
                        dynamicLists = resp.obj.getDynamicList();
                        listAdapter = new SquareAdapter(dynamicLists, getActivity(), true);
                        listAdapter.setItem(new SquareAdapter.Item() {
                            @Override
                            public void Item() {
                                getMyFollowList();
                            }
                        });
                        listAdapter.setMap(map);
                        lv_square.setAdapter(listAdapter);
                        lv_square.reflashComplete();
                        listAdapter.setOnDynamicDeletedListener(new SquareAdapter.OnDynamicDeletedListener() {
                            @Override
                            public void delete() {
                                listIndex = 0;
                                getStrangerDynamic();
                            }
                        });
                    } else {
                        dynamicLists.addAll(resp.obj.getDynamicList());
                        listAdapter.notifyDataSetChanged();
                        lv_square.loadCompleted();
                    }

                } else {
                    showToastShort(resp.msg);
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
    private Map<String, String> map = new HashMap<>();
    private void getMyFollowList() {
        ReqGetFollowList.getList(getActivity(), getTag(), AppApplication.getCurrentUserInfo().getUserId(), new EntityCallBack<FollowListResponse>() {
            @Override
            public Class<FollowListResponse> getEntityClass() {
                return FollowListResponse.class;
            }

            @Override
            public void onSuccess(final FollowListResponse resp) {
                if (resp.is200()) {
                    map.clear();
                    for (FollowListInfo.FollowList bean : resp.obj.getFollowList()) {
                        map.put(String.valueOf(bean.getFollowUserId()), String.valueOf(bean.getUserId()));
                    }
                } else {
                }
                if (listAdapter != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            hr.sendEmptyMessage(0x123);
                        }
                    }).start();
                } else {
                    initEvent();
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
    Handler hr = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            listAdapter.setMap(map);
            listAdapter.notifyDataSetChanged();
            return false;
        }
    });
    public void shua() {
        getMyFollowList();
    }
    /**
     * 微信分享
     */
    private void shareWeChat() {
        try {
            JShareInterface.share(Wechat.Name, generateParams(), new PlatActionListener() {
                @Override
                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                    Log.d("---", "onComplete:" + i);
                }

                @Override
                public void onError(Platform platform, int i, int i1, Throwable throwable) {
                    Log.e("----", "platform:" + i + "____" + platform.getName() + throwable.getMessage());
                }

                @Override
                public void onCancel(Platform platform, int i) {
                    Log.e("---", "onCancel:" + i);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void shareWeChatMoments() {
        JShareInterface.share(WechatMoments.Name, generateParams(), null);
    }
    private ShareParams generateParams() {
        ShareParams shareParams = new ShareParams();
        shareParams.setShareType(Platform.SHARE_IMAGE);
        shareParams.setImageData(shareBit);
        return shareParams;
    }
    @Override
    public void onSharePlatformClicked(int position) {
        Toast.makeText(getContext(), "正在准备分享...", Toast.LENGTH_SHORT).show();
        JiGuangSharePlatformModel model = list.get(position);

        switch (model.getPlatFormType()) {
            case WE_CHAT:
                shareWeChat();
                break;
            case WE_CHAT_MOMNETS:
                shareWeChatMoments();
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x123) {
            if (resultCode == RESULT_OK) {
                String file = data.getStringExtra("output");

                Intent intent = new Intent(getActivity(), SquareVideoActivity.class);
                intent.putExtra("file", file);
                startActivity(intent);
            }
        }
    }
    Intent intent;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shanghui_card1://创业锦囊
                intent = new Intent(getActivity(), GroupChatDetailNewActivity.class);
                intent.putExtra("groupName", sh_name1.getText().toString());
                intent.putExtra("peopleNum", sh_num1.getText().toString());
                intent.putExtra("groupNum", shanghuiList.get(0).getGroupNum().toString());
                intent.putExtra("groupNote", shanghuiList.get(0).getGroupNote().toString());
                intent.putExtra("picUrl", shanghuiList.get(0).getPic());
                intent.putExtra("hostId", shanghuiList.get(0).getHolderId());
                Pair<View, String> shanghuiHead = new Pair<View, String>(sh_img1, "qtx");
                Pair<View, String> shanghuiName = new Pair<View, String>(sh_name1, "shanghuiName");
                Pair<View, String> shanghuiNum = new Pair<View, String>(sh_num1, "shanghuiNum");
                ActivityOptionsCompat optionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), shanghuiHead, shanghuiName, shanghuiNum);
                startActivity(intent, optionsCompat.toBundle());
                break;
            case R.id.shanghui_card2://企业交流
                intent = new Intent(getActivity(), GroupChatDetailNewActivity.class);
                intent.putExtra("groupName", sh_name2.getText().toString());
                intent.putExtra("peopleNum", sh_num2.getText().toString());
                intent.putExtra("groupNum", shanghuiList.get(1).getGroupNum().toString());
                intent.putExtra("picUrl", shanghuiList.get(1).getPic());
                intent.putExtra("groupNote", shanghuiList.get(0).getGroupNote().toString());
                intent.putExtra("hostId", shanghuiList.get(1).getHolderId());
                intent.putExtra("groupNote", shanghuiList.get(1).getGroupNote().toString());
                Pair<View, String> shanghuiHead1 = new Pair<View, String>(sh_img2, "qtx");
                Pair<View, String> shanghuiName1 = new Pair<View, String>(sh_name2, "shanghuiName");
                Pair<View, String> shanghuiNum1 = new Pair<View, String>(sh_num2, "shanghuiNum");
                ActivityOptionsCompat optionsCompat1 =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), shanghuiHead1, shanghuiName1, shanghuiNum1);
                startActivity(intent, optionsCompat1.toBundle());
                break;
            case R.id.shanghui_card3://资源整合
                Intent intent = new Intent(getActivity(), GroupChatDetailNewActivity.class);
                intent.putExtra("groupName", sh_name3.getText().toString());
                intent.putExtra("peopleNum", sh_num3.getText().toString());
                intent.putExtra("groupNote", shanghuiList.get(2).getGroupNote().toString());
                intent.putExtra("groupNum", shanghuiList.get(2).getGroupNum().toString());
                intent.putExtra("picUrl", shanghuiList.get(2).getPic());
                intent.putExtra("hostId", shanghuiList.get(2).getHolderId());
                Pair<View, String> shanghuiHead2 = new Pair<View, String>(sh_img3, "qtx");
                Pair<View, String> shanghuiName2 = new Pair<View, String>(sh_name3, "shanghuiName");
                Pair<View, String> shanghuiNum2 = new Pair<View, String>(sh_num3, "shanghuiNum");
                ActivityOptionsCompat optionsCompat2 =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), shanghuiHead2, shanghuiName2, shanghuiNum2);
                startActivity(intent, optionsCompat2.toBundle());
                break;
            case R.id.tv_showMore:
                startActivity(new Intent(getActivity(), AllGroupList.class));
                break;
            case R.id.cd_pop:
                Intent intent1 = new Intent(getActivity(), New_comment.class);
                if (msmcount.equals("1")) {
                    cd_pop.setVisibility(View.GONE);
                }
                startActivity(intent1);
                break;
            case R.id.img_dongtai_floating:
                pai();
                break;
        }
    }
}
