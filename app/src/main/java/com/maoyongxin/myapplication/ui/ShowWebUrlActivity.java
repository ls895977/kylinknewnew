package com.maoyongxin.myapplication.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.bumptech.glide.load.engine.Resource;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.PictureEntity;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.request.ReqPublicDetail;
import com.maoyongxin.myapplication.http.response.PublishDetailResponse;
import com.maoyongxin.myapplication.myapp.AppActivity;
import com.maoyongxin.myapplication.ui.fragment.ShareDialogFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jiguang.share.android.api.JShareInterface;
import cn.jiguang.share.android.api.PlatActionListener;
import cn.jiguang.share.android.api.Platform;
import cn.jiguang.share.android.api.ShareParams;
import cn.jiguang.share.wechat.Wechat;
import cn.jiguang.share.wechat.WechatMoments;

public class ShowWebUrlActivity extends AppActivity  implements ShareDialogFragment.Listener {


    @BindView(R.id.img_close)
    ImageView imgClose;
    @BindView(R.id.web_myContent)
    WebView webMyContent;
    @BindView(R.id.activity_show_web_url)
    RelativeLayout activityShowWebUrl;

    @BindView(R.id.shangretowechat)
    TextView shangretowechat;

    private String tvPublishDetailContent;
    private String tvPublishDetailTitle;
    private  Bitmap bitmap;
    private ArrayList<JiGuangSharePlatformModel> list = new ArrayList<>();

    @Override
    protected int bindLayout() {
        return (R.layout.activity_show_web_url);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//白底黑字+沉浸式导航栏
        super.onCreate(savedInstanceState);


        //透明导航栏


        bitmap=BitmapFactory.decodeResource(getResources(), R.mipmap.app_icon);//简单获取位图

        ButterKnife.bind(this);


    }

    @Override
    protected void initData() {
        //super.initData();
    }

    @Override
    protected void initView() {
        //super.initView();
        getPublishDetail(getIntent().getStringExtra("userId"), getIntent().getStringExtra("noticeId"));
    }

    @Override
    protected void initEvent() {
       // super.initEvent();
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        shangretowechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showShareDialog();
            }
        });
    }

    //改写物理按键——返回的逻辑
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webMyContent.canGoBack()) {
                webMyContent.goBack();//返回上一页面
                return true;
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initWebView(WebView webView) {
        //声明WebSettings子类
        WebSettings webSettings = webView.getSettings();

//如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);


        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        //设置支持缩放

//设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

//缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

//其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存

        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        webView.getSettings().setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webMyContent.loadUrl(tvPublishDetailContent);

    }

    private void getPublishDetail(String userId, String noticeId) {

        ReqPublicDetail.getPublic(this, getClass().getSimpleName(), userId, noticeId, new EntityCallBack<PublishDetailResponse>() {
            @Override
            public Class<PublishDetailResponse> getEntityClass() {
                return PublishDetailResponse.class;
            }

            @Override
            public void onSuccess(PublishDetailResponse resp) {
                if (resp.is200()) {
                    String noticeTitle = resp.obj.getNoticeDetail().getNoticeTitle();
                    String noticeContent = resp.obj.getNoticeDetail().getContent();

                    List<PictureEntity> pictureEntities = new ArrayList<>();


                    tvPublishDetailTitle = noticeTitle;
                    tvPublishDetailContent = noticeContent;

                    initWebView(webMyContent);
                } else {

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
    public void onSharePlatformClicked(int position) {
        Toast.makeText(this, "正在准备分享...", Toast.LENGTH_SHORT).show();
        JiGuangSharePlatformModel model = list.get(position);

        switch (model.getPlatFormType()) {
            case WE_CHAT:
                shareWeChat(tvPublishDetailTitle,tvPublishDetailContent);
                break;
            case WE_CHAT_MOMNETS:
                shareWeChatMoments(tvPublishDetailTitle,tvPublishDetailContent);
                break;
        }
    }
    private void shareWeChat(String title,String url) {
        JShareInterface.share(Wechat.Name, generateParams(title,url), new PlatActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Log.e("share", "onComplete:" + i);
            }

            @Override
            public void onError(Platform platform, int i, int i1, Throwable throwable) {
                Log.e("share", "platform:" + i + "____" + platform.getName() + throwable.getMessage());
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Log.d("---", "onCancel:" + i);
            }
        });
    }

    private void shareWeChatMoments(String title,String url) {
        JShareInterface.share(WechatMoments.Name, generateParams(title,url), null);
    }
    private void showShareDialog() {
        list.clear();
        List<String> successPlatform = JShareInterface.getPlatformList();

        for (String s : successPlatform) {
            //Log.e("---", "===>>" + s + "===>>" + JShareInterface.isClientValid(s));
            JShareInterface.isClientValid(s);
            if (s.equals("WechatFavorite")) {
                continue;
            }
            list.add(new JiGuangSharePlatformModel(s));

        }
        ShareDialogFragment dialogFragment = ShareDialogFragment.newInstance(list);
        dialogFragment.show(getSupportFragmentManager(),"tag");
    }
    private ShareParams generateParams(String title,String url) {
        ShareParams shareParams = new ShareParams();
        shareParams.setShareType(Platform.SHARE_WEBPAGE);
        shareParams.setTitle(title);
        shareParams.setUrl(url);
        shareParams.setText("彼信商业社区，数据永久免费"+"\n"+"(若界面显示不完全，请开启横屏或用电脑打开)");
        shareParams.setImageData(bitmap);
        /*
        shareParams.setShareType(Platform.SHARE_TEXT);
        shareParams.setTitle(title);
        shareParams.setText( url+"\n"+title);
        */
        return shareParams;
    }
}
