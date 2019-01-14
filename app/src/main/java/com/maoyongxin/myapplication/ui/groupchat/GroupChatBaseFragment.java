package com.maoyongxin.myapplication.ui.groupchat;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.GroupListInfo;
import com.maoyongxin.myapplication.entity.MyDynamicListInfo;
import com.maoyongxin.myapplication.entity.newsInfo;
import com.maoyongxin.myapplication.entity.shanghuiInfo;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.request.ReqGetBannerList;
import com.maoyongxin.myapplication.http.request.ReqMyDynamicList;
import com.maoyongxin.myapplication.http.response.BannerResponse;
import com.maoyongxin.myapplication.http.response.MyDongTaiListResponse;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.tool.MyScrollView;
import com.maoyongxin.myapplication.ui.GroupListActivity;
import com.maoyongxin.myapplication.ui.News_web;
import com.maoyongxin.myapplication.ui.PublishPartActivity;
import com.maoyongxin.myapplication.ui.ShowWebUrlActivity;
import com.maoyongxin.myapplication.ui.adapter.DynamicRecyclerAdapter;
import com.maoyongxin.myapplication.ui.adapter.news_adapter;
import com.maoyongxin.myapplication.ui.adapter.shanghuiAdapter;
import com.maoyongxin.myapplication.ui.editapp.editadapter.SquareAdapter;
import com.maoyongxin.myapplication.ui.jin_nang;
import com.maoyongxin.myapplication.ui.news.activity.NewisActivity;
import com.maoyongxin.myapplication.ui.widget.BannerView;
import com.maoyongxin.myapplication.ui.widget.CircularAnimUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.maoyongxin.myapplication.myapp.AppFragment.showToastShort;


public class GroupChatBaseFragment extends Fragment {
    @BindView(R.id.list_remen)
    ListView listRemen;


    @BindView(R.id.tv_newGroup)
    TextView tvNewGroup;
    @BindView(R.id.tv_outputGroup)
    TextView tvOutputGroup;
    @BindView(R.id.tv_recieveGroup)
    TextView tvRecieveGroup;
    @BindView(R.id.tv_lastSeeGroup)
    TextView tvLastSeeGroup;


    @BindView(R.id.changePopList)
    TextView changePopList;
    @BindView(R.id.scrollView)
    MyScrollView scrollView;
    @BindView(R.id.banner)
    BannerView banner;
    @BindView(R.id.remen_shanghui)
    LinearLayout remenShanghui;
    @BindView(R.id.search_view)
    LinearLayout search_view;
    @BindView(R.id.more_toutiao)
    TextView moreToutiao;
    @BindView(R.id.news_list)
    ListView newsList;
    @BindView(R.id.more_dynamic)
    TextView moreDynamic;


    @BindView(R.id.sv_searchall)
    SearchView svSearchall;


    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());


    @BindView(R.id.dynmic_recycler)
    RecyclerView dynmic_recycler;
    @BindView(R.id.zhuanfangTilte)
    TextView zhuanfangTilte;
    @BindView(R.id.zhuanfangSubTitle)
    TextView zhuanfangSubTitle;
    @BindView(R.id.zfTime)
    TextView zfTime;
    @BindView(R.id.tozhuanfang)
    LinearLayout tozhuanfang;

    private List<String> imgUrl;

    private List<GroupListInfo.GroupList> groupList;
    private List<newsInfo> newsInfos;
    private List<shanghuiInfo> Popshanghuiinfos;

    private List<shanghuiInfo> newshanghuiinfos;

    private List<shanghuiInfo> remen_news;


    private List<shanghuiInfo> Dynamics;
    private List<MyDynamicListInfo.DynamicList> dynamicLists;

    private String groupName;
    private String groupId;
    private String groupImg;
    private String membernum;
    private String integralNum;
    private String newsTitle;
    private String newsDate;
    private String newsPic;
    private String newsUrl;
    private String hostId,groupNote;
    private int pagenew_next = 1;


    private int pagepopNext = 1;

    private String APinew = "http://st.3dgogo.com/index/chatgroup_gambit/get_chat_group_newest";
    private String ApiPop = "http://st.3dgogo.com/index/chatgroup_gambit/get_chat_group_hottest?page=";
    private String ApiNews = "http://st.3dgogo.com/index/news/get_news_list_api.html";
    private String zhuangfang_url = "http://st.3dgogo.com/index/interview/get_newest_user_interview_api.html";
    private String ApiDynamic = "http://st.3dgogo.com/index/chatgroup_gambit/get_chat_group_hottest?page=";


    private Handler handler;
    private static final int UPDATEONE = 0;
    private static final int UPDATETWO = 1;
    private static final int UPDATENews = 2;
    private static final int UPDATEDynamic = 3;

    private shanghuiAdapter adapterone = new shanghuiAdapter(getActivity(), Popshanghuiinfos);
    private shanghuiAdapter adaptertnews = new shanghuiAdapter(getActivity(), remen_news);
    private shanghuiAdapter adaptertDynamic = new shanghuiAdapter(getActivity(), Dynamics);
    private news_adapter newsAdapter = new news_adapter(getActivity(), newsInfos);
    private SquareAdapter listAdapter;
    private DynamicRecyclerAdapter dynamicAdapter;
    private String zfTLtitle, zfSubTitle, zftime;
    Unbinder unbinder;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_chat_base, container, false);
        unbinder = ButterKnife.bind(this, view);

        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        initData();
        initView();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case UPDATEONE:
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {


                            }
                        }, 100);

                    case UPDATENews:
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //  adaptertDynamic = new shanghuiAdapter(getActivity(), Dynamics);
                                adapterone = new shanghuiAdapter(getActivity(), Popshanghuiinfos);
                                newsAdapter = new news_adapter(getActivity(), newsInfos);
                                newsList.setAdapter(newsAdapter);
                                listRemen.setAdapter(adapterone);
                                zfTime.setText(zftime);
                                zhuanfangTilte.setText(zfTLtitle);
                                zhuanfangSubTitle.setText(zfSubTitle);

                                scrollView.smoothScrollTo(0, 0);
                            }
                        }, 100);

                    case UPDATEDynamic:
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                dynamicAdapter = new DynamicRecyclerAdapter(dynamicLists, getActivity(), true);
                                dynamicAdapter.setHasStableIds(false);
                                dynmic_recycler.setAdapter(dynamicAdapter);

                                scrollView.smoothScrollTo(0, 0);
                            }
                        }, 100);


                        break;
                }
                super.handleMessage(msg);
            }
        };

        tozhuanfang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private void initData() {

        newshanghuiinfos = new ArrayList<>();
        Popshanghuiinfos = new ArrayList<>();
        remen_news = new ArrayList<>();
        Dynamics = new ArrayList<>();
        newsInfos = new ArrayList<>();
        dynamicLists = new ArrayList<>();

        getStrangerDynamic();
        getPoplis(ApiPop, pagepopNext);
        getNews(ApiNews);
        getzhuanfaInfo();

    }

    private void getzhuanfaInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(zhuangfang_url)
                            .build();
                    Response response = client.newCall(request).execute();


                    String responseData = response.body().string();

                    try {

                        JSONObject jsonObject = new JSONObject(responseData);

                        JSONObject data = jsonObject.getJSONObject("info");
                        if (jsonObject.getInt("code") == 200) {

                            zfTLtitle = data.getString("title");
                            zfSubTitle = data.getString("subhead");
                            zftime = data.getString("create_time");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {

                }
            }
        }).start();
    }

    private void getNews(final String api_url) {

//get自身服务器数据


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(api_url).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();

                    try {

                        JSONObject jsonObject = new JSONObject(responseData);

                        JSONArray data = jsonObject.getJSONArray("info");
                        remen_news.clear();
                        for (int i = 0; i < 2; i++) {

                            newsInfo nsInfo = new newsInfo();
                            JSONObject newList = data.getJSONObject(i);
                            newsTitle = parse_Value(newList, "title");
                            newsDate = parse_Value(newList, "create_time");
                            newsPic = parse_Value(newList, "img");
                            newsUrl = parse_Value(newList, "url");
                            nsInfo.setNewsInfo(newsTitle, newsDate, newsPic, newsUrl);
                            newsInfos.add(nsInfo);


                        }
                        Message msg = new Message();
                        msg.what = UPDATENews;
                        handler.sendMessage(msg);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    //  showMessagefail("解析失败");
                }
            }
        }).start();
    }


    private void getPoplis(String api_url, int page) {
        final String api = api_url + page + "";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(api).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();

                    try {

                        JSONObject jsonObject = new JSONObject(responseData);

                        JSONArray data = jsonObject.getJSONObject("info").getJSONArray("data");
                        Popshanghuiinfos.clear();
                        for (int i = 0; i < 3; i++) {

                            shanghuiInfo newshanghuiInfo = new shanghuiInfo();
                            JSONObject newList = data.getJSONObject(i);
                            groupName = parse_Value(newList, "groupName");
                            groupId = parse_Value(newList, "groupId");
                            groupImg = parse_Value(newList, "image");
                            membernum = parse_Value(newList, "memberNum");
                            integralNum = parse_Value(newList, "integralNum");
                            hostId=parse_Value(newList, "grouphostId");
                            groupNote=parse_Value(newList, "groupNote");
                            newshanghuiInfo.setshanghuiInfo(groupImg, groupName, groupId, membernum, integralNum, groupName,hostId,groupNote);

                            Popshanghuiinfos.add(newshanghuiInfo);

                            pagepopNext = jsonObject.getJSONObject("info").getInt("next_page");
                        }

                        Message msg = new Message();
                        msg.what = UPDATEONE;
                        handler.sendMessage(msg);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    //  showMessagefail("解析失败");
                }
            }
        }).start();
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


    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        dynmic_recycler.setLayoutManager(layoutManager);

        setInfo_remen_shanghui(Popshanghuiinfos);

        getActivity().getWindow().setEnterTransition(new Explode().setDuration(2000).setInterpolator(new BounceInterpolator()));

        setMyBanner();
        setScrollView();
        scrollView.smoothScrollTo(0, 0);

        svSearchall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), searchall.class), ActivityOptions.makeSceneTransitionAnimation(getActivity(), view, "seachall").toBundle());

            }
        });

        newsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), News_web.class);
                final TextView tv_title = (TextView) view.findViewById(R.id.Tv_newstitle);
                final TextView imgurl = (TextView) view.findViewById(R.id.imgurl);
                final ImageView newImg = (ImageView) view.findViewById(R.id.Img_news);
                final TextView tv_news = (TextView) view.findViewById(R.id.newsUrl);

                intent.putExtra("imgurl", imgurl.getText());
                intent.putExtra("weburl", tv_news.getText());
                startActivity(intent);
                //startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity(), newImg, "newsImg").toBundle());


            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick({R.id.changePopList, R.id.tv_newGroup, R.id.tv_outputGroup, R.id.tv_recieveGroup, R.id.tv_lastSeeGroup})
    public void onViewClicked(View view) {
        switch (view.getId()) {


            case R.id.tv_newGroup:
                startActivity(new Intent(getActivity(), GroupListActivity.class));
                break;
            case R.id.tv_outputGroup:



                startActivity(new Intent(getActivity(), NewisActivity.class));
                break;

            case R.id.tv_lastSeeGroup:
                CircularAnimUtil.startActivity(getActivity(), jin_nang.class, tvLastSeeGroup, R.color.blue_tiny);
                break;
            case R.id.tv_recieveGroup:
                startActivity(new Intent(getActivity(), PublishPartActivity.class));
                break;
            case R.id.changePopList:
                startActivity(new Intent(getActivity(), GroupListActivity.class));
                break;
        }
    }


    private void setInfo_remen_shanghui(List<shanghuiInfo> remen_shanghui) {
        listRemen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final TextView name = (TextView) view.findViewById(R.id.name_shanghui);
                final TextView num = (TextView) view.findViewById(R.id.num_shanghui);

                final ImageView qtx = (ImageView) view.findViewById(R.id.img_jingxuan);

                final TextView qtx_Img = (TextView) view.findViewById(R.id.headurl);

                //Intent intent = new Intent(getActivity(), GroupChatDetailActivity.class);

                Intent intent = new Intent(getActivity(), GroupChatDetailNewActivity.class);

                intent.putExtra("groupName", name.getText().toString());
                intent.putExtra("groupNum", num.getText().toString());
                intent.putExtra("picUrl", qtx_Img.getText().toString());


                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity(), qtx, "qtx").toBundle());


            }
        });
    }


    private void setMyBanner() {

        ReqGetBannerList.getBanner(getContext(), getActivityTag(), new EntityCallBack<BannerResponse>() {
            @Override
            public Class<BannerResponse> getEntityClass() {
                return BannerResponse.class;
            }

            @Override
            public void onSuccess(BannerResponse resp) {
                if (resp.is200()) {
                    //  mProgressDialog.dismiss();
                    /**
                     * 设置顶部banner
                     */
                    banner.setBannerViewListener(new BannerView.BannerViewListener() {
                        @Override
                        public void onShowImage(ImageView view, String imgUrl) {
                            Glide.with(getActivity()).load(imgUrl).into(view);
                        }

                        @Override
                        public void onItemClick(int position, String targetUrl, Object userData) {
                            Intent intent = new Intent(getActivity(), ShowWebUrlActivity.class);
                            intent.putExtra("url", targetUrl);
                            startActivity(intent);
                        }

                        @Override
                        public View onCreateIndicatorView() {
                            ImageView v = new ImageView(getContext());
                            v.setPadding(10, 0, 0, 0);
                            v.setImageResource(R.drawable.pager_indicator_selector);
                            return v;
                        }
                    });
                    ArrayList<BannerView.BannerInfo> testData = new ArrayList<>();
                    for (int i = 0; i < resp.obj.getBannerList().size(); i++) {
                        testData.add(new BannerView.BannerInfo(resp.obj.getBannerList().get(i).getImgUrl(), resp.obj.getBannerList().get(i).getConnectionAddress()));
                    }
                    banner.setBannerDatas(testData);
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

    public String getActivityTag() {
        return getClass().getSimpleName();
    }

    private int y = 200;
    private int listIndex = 0;

    private void setScrollView() {
        scrollView.setScroll(new MyScrollView.scrollListener() {
            @Override
            public void scroll(int yy) {

                if (yy < y) {
                    int color = (int) ((255 / y) * yy);
                    search_view.setBackgroundColor(Color.argb(color, 255, 255, 255));
                    svSearchall.setBackgroundColor(Color.argb(color, 200, 200, 200));
                } else {
                    search_view.setBackgroundColor(Color.argb(255, 255, 255, 255));
                    svSearchall.setBackgroundColor(Color.argb(200, 200, 200, 200));
                }
            }
        });
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
                        dynamicLists = (resp.obj.getDynamicList());

                        Message msg = new Message();
                        msg.what = UPDATEDynamic;
                        handler.sendMessage(msg);

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

}
