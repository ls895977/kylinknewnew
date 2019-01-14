package com.maoyongxin.myapplication.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jky.baselibrary.util.common.TimeUtil;
import com.jky.baselibrary.widget.TitleBar;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.CommunityListInfo;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.request.ReqCommunity;
import com.maoyongxin.myapplication.http.response.BaseResp;
import com.maoyongxin.myapplication.http.response.CommunityListResponse;
import com.maoyongxin.myapplication.http.response.CommunityRequestResponse;
import com.maoyongxin.myapplication.http.response.MyCommunityResponse;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.myapp.AppTitleBarActivity;
import com.maoyongxin.myapplication.server.widget.SelectableRoundedImageView;
import com.maoyongxin.myapplication.ui.adapter.NearbyCommunityListAdapter;
import com.maoyongxin.myapplication.ui.widget.NoScrollListView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by maoyongxin on 2017/11/22.
 */

public class AddressHomeCheckActivity extends AppTitleBarActivity {
    @BindView(R.id.img_myCommunityIcon)
    SelectableRoundedImageView imgMyCommunityIcon;
    @BindView(R.id.tv_communityCreatTime)
    TextView tvCommunityCreatTime;
    @BindView(R.id.tv_noCommunity)
    TextView tvNoCommunity;
    @BindView(R.id.tv_communityNote)
    TextView tvCommunityNote;
    @BindView(R.id.tv_communityAddress)
    TextView tvCommunityAddress;
    @BindView(R.id.line_myCommunity)
    LinearLayout lineMyCommunity;
    @BindView(R.id.tv_noNearbyCommunity)
    TextView tvNoNearbyCommunity;
    @BindView(R.id.lv_nearbyCommunityList)
    NoScrollListView lvNearbyCommunityList;
    @BindView(R.id.tv_myCommunityName)
    TextView tvMyCommunityName;
    @BindView(R.id.TitleBar_like)
    TitleBar TitleBarLike;
    @BindView(R.id.tv_myCommunityId)
    TextView tvMyCommunityId;
    @BindView(R.id.wv_bison)
    WebView wvbison;


    private List<CommunityListInfo.CommnunityList> commnunityLists;
    private String myCommunityIcon;
    private String myCommunityName;
    private String myCommunityNote;
    private String myCommunityAddress;
    private String myCommunityCreatTime;
    private String myCommunityId,url;
    private boolean hasCommunity;

    @Override
    protected void initData() {
        super.initData();
        myCommunityIcon = "";
        hasCommunity = false;
        url="http://www.bisonchat.com/home/introduce/index.html";
    }

    @Override
    protected void handlerPassMsg(int target, int target1, Object obj) {

    }

    @Override
    protected void initView() {
        super.initView();
        getMyOwnCommunity();

        getNearbyCommunityList();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//白底黑字+沉浸式导航栏

        initWebView(wvbison);
    }
    private void initWebView(WebView webView) {
        //声明WebSettings子类
        WebSettings webSettings = webView.getSettings();
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.setInitialScale(5);
//如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
//设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

//缩放操作
        webSettings.setSupportZoom(false); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(false); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);


//其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); //关闭webview中缓存\
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        getBaseContext().deleteDatabase("WebView.db");
        getBaseContext().deleteDatabase("WebViewCache.db");

        webView.clearCache(true);

        webView.clearFormData();
        getCacheDir().delete();

        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        webView.getSettings().setDomStorageEnabled(true);

        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);


        //主要用于平板，针对特定屏幕代码调整分辨率
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mDensity = metrics.densityDpi;
        if (mDensity == 240) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == 160) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        } else if (mDensity == 120) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        } else if (mDensity == DisplayMetrics.DENSITY_XHIGH) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == DisplayMetrics.DENSITY_TV) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        wvbison.loadUrl(url);

    }
    @Override
    protected int bindLayout() {
        return R.layout.activity_addresshomecheck;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        TitleBarLike.setOnTitleBarClickListener(new TitleBar.OnClickListener() {
            @Override
            public boolean onClick(int function) {
                if (function == TitleBar.FUNCTION_RIGHT_TEXT) {

                    ReqCommunity.getMyCreatRequest(AddressHomeCheckActivity.this, getActivityTag(), AppApplication.getCurrentUserInfo().getUserId(), new EntityCallBack<CommunityRequestResponse>() {
                        @Override
                        public Class<CommunityRequestResponse> getEntityClass() {
                            return CommunityRequestResponse.class;
                        }

                        @Override
                        public void onSuccess(CommunityRequestResponse resp) {
                            if (resp.is200()) {
                                if (resp.obj.getCommunityRequestList().size() == 0) {
                                    startActivity(new Intent(AddressHomeCheckActivity.this, CreatCommunityActivity.class));
                                } else {
                                    showToastShort("对不起，你已经提交过创建请求或者你已经有自己的社区了，无需再次提交");
                                }
                            }
                        }

                        @Override
                        public void onFailure(Throwable e) {
                            startActivity(new Intent(AddressHomeCheckActivity.this, CreatCommunityActivity.class));
                        }

                        @Override
                        public void onCancelled(Throwable e) {

                        }

                        @Override
                        public void onFinished() {

                        }
                    });

                } else if (function == TitleBar.FUNCTION_LEFT_TEXT) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(AddressHomeCheckActivity.this);

                    builder.setTitle("提示").setMessage("一个账号只能加入一个团队，可以退出后加入其它团队").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final EditText inputServer = new EditText(AddressHomeCheckActivity.this);
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddressHomeCheckActivity.this);
                            builder.setMessage("请输入团队ID").setView(inputServer).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ReqCommunity.joinCommunity(AddressHomeCheckActivity.this, getActivityTag(), AppApplication.getCurrentUserInfo().getUserId(), inputServer.getText().toString(), "", new EntityCallBack<BaseResp>() {
                                        @Override
                                        public Class<BaseResp> getEntityClass() {
                                            return BaseResp.class;
                                        }

                                        @Override
                                        public void onSuccess(BaseResp resp) {
                                            showToastShort(resp.msg);
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
                            }).setNegativeButton("取消", null).show();

                        }
                    }).setNegativeButton("取消", null).show();
                }
                return false;
            }
        });

        lineMyCommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCommunityDetail();
            }
        });
        lvNearbyCommunityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (commnunityLists.get(position).getCommunityId().equals(myCommunityId)) {
                    startCommunityDetail();
                } else {
                    if (hasCommunity) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddressHomeCheckActivity.this);
                        builder.setTitle("提示").setMessage("您已加入其他社区，无权限查看当前小区成员").setPositiveButton("确定", null).show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddressHomeCheckActivity.this);
                        builder.setTitle("提示").setMessage("您还没有加入该小区，无权限查看小区成员，是否加入？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final EditText inputServer = new EditText(AddressHomeCheckActivity.this);
                                AlertDialog.Builder builder = new AlertDialog.Builder(AddressHomeCheckActivity.this);
                                builder.setMessage("请输入申请备注").setView(inputServer).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ReqCommunity.joinCommunity(AddressHomeCheckActivity.this, getActivityTag(), AppApplication.getCurrentUserInfo().getUserId(), commnunityLists.get(position).getCommunityId(), inputServer.getText().toString(), new EntityCallBack<BaseResp>() {
                                            @Override
                                            public Class<BaseResp> getEntityClass() {
                                                return BaseResp.class;
                                            }

                                            @Override
                                            public void onSuccess(BaseResp resp) {
                                                showToastShort(resp.msg);
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
                                }).setNegativeButton("取消", null).show();

                            }
                        }).setNegativeButton("取消", null).show();
                    }
                }
            }
        });
    }

    private void startCommunityDetail() {
        Intent intent = new Intent(AddressHomeCheckActivity.this, CommunityDetailActivity.class);
        intent.putExtra("myCommunityIcon", myCommunityIcon);
        intent.putExtra("myCommunityName", myCommunityName);
        intent.putExtra("myCommunityNote", myCommunityNote);
        intent.putExtra("myCommunityAddress", myCommunityAddress);
        intent.putExtra("myCommunityCreatTime", myCommunityCreatTime);
        intent.putExtra("myCommunityId", myCommunityId+"");
        startActivity(intent);
    }

    private void getMyOwnCommunity() {
        ReqCommunity.getMyCommunity(this, getActivityTag(), AppApplication.getCurrentUserInfo().getUserId(), new EntityCallBack<MyCommunityResponse>() {
            @Override
            public Class<MyCommunityResponse> getEntityClass() {
                return MyCommunityResponse.class;
            }

            @Override
            public void onSuccess(MyCommunityResponse resp) {
                if (resp.is200()) {
                    lineMyCommunity.setVisibility(View.VISIBLE);
                    tvNoCommunity.setVisibility(View.GONE);
                    if (resp.obj.getCommunityImg() != null && !resp.obj.getCommunityImg().equals("")) {
                        Glide.with(AddressHomeCheckActivity.this).load(resp.obj.getCommunityImg()).into(imgMyCommunityIcon);
                        myCommunityIcon = resp.obj.getCommunityImg();
                    } else {
                        Glide.with(AddressHomeCheckActivity.this).load(R.drawable.jingying).into(imgMyCommunityIcon);
                    }
                    tvMyCommunityName.setText(resp.obj.getCommunityName());
                    tvCommunityAddress.setText(resp.obj.getAddress());
                    tvCommunityCreatTime.setText(TimeUtil.getFormatYMDFromMillis(resp.obj.getCreatTime(), "yyyy-MM-dd"));

                    myCommunityAddress = resp.obj.getAddress();
                    myCommunityName = resp.obj.getCommunityName();

                    myCommunityCreatTime = TimeUtil.getFormatYMDFromMillis(resp.obj.getCreatTime(), "yyyy-MM-dd");
                    myCommunityId = resp.obj.getCommunityId() + "";
                    tvMyCommunityId.setText("团队ID" + myCommunityId);
                    hasCommunity = true;

                } else {
                    lineMyCommunity.setVisibility(View.GONE);
                    tvNoCommunity.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Throwable e) {
                lineMyCommunity.setVisibility(View.GONE);
                tvNoCommunity.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(Throwable e) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void getNearbyCommunityList() {
        ReqCommunity.getNearbyCommunityList(this, getActivityTag(), AppApplication.getMyCurrentLocation().getAdCode(), new EntityCallBack<CommunityListResponse>() {
            @Override
            public Class<CommunityListResponse> getEntityClass() {
                return CommunityListResponse.class;
            }

            @Override
            public void onSuccess(CommunityListResponse resp) {
                if (resp.is200()) {
                    if (resp.obj.getCommnunityList().size() == 0) {
                        tvNoNearbyCommunity.setVisibility(View.VISIBLE);
                        lvNearbyCommunityList.setVisibility(View.GONE);
                    } else {
                        tvNoNearbyCommunity.setVisibility(View.GONE);
                        lvNearbyCommunityList.setVisibility(View.VISIBLE);
                        commnunityLists = resp.obj.getCommnunityList();
                        NearbyCommunityListAdapter adapter = new NearbyCommunityListAdapter(commnunityLists, AddressHomeCheckActivity.this);
                        lvNearbyCommunityList.setAdapter(adapter);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
