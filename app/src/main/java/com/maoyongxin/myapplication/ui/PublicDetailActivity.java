package com.maoyongxin.myapplication.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.PictureEntity;
import com.maoyongxin.myapplication.entity.PublicDetailInfo;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.request.ReqDeletePublish;
import com.maoyongxin.myapplication.http.request.ReqFindUserById;
import com.maoyongxin.myapplication.http.request.ReqPublicDetail;
import com.maoyongxin.myapplication.http.response.BaseResp;
import com.maoyongxin.myapplication.http.response.LoginResponse;
import com.maoyongxin.myapplication.http.response.PublishDetailResponse;
import com.maoyongxin.myapplication.myapp.AppActivity;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.ui.adapter.GridNetPicAdapter;
import com.maoyongxin.myapplication.ui.widget.mylistview.AntGrideVIew;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class PublicDetailActivity extends AppActivity {
    @BindView(R.id.tv_publish_detail_userName)
    TextView tvPublishDetailUserName;
    @BindView(R.id.tv_publish_detail_title)
    TextView tvPublishDetailTitle;
    @BindView(R.id.tv_publish_detail_content)
    TextView tvPublishDetailContent;
    @BindView(R.id.gv_publish_detail_pic)
    AntGrideVIew gvPublishDetailPic;
    @BindView(R.id.img_publish_delete)
    ImageView imgPublishDelete;
    @BindView(R.id.img_head)
    ImageView imgHead;
    private GridNetPicAdapter adapter;
    private String userId;
    private String noticeId;
    private String userName;
    private String userHeadUrl;
    private String noticeContent;
    private String noticeTitle;
    private List<PublicDetailInfo.ImgList> imgLists;
    private ProgressDialog mProgressDialog;

    @Override
    protected void initData() {
        super.initData();
        userId = getIntent().getStringExtra("userId");
        noticeId = getIntent().getStringExtra("noticeId");
    }

    @Override
    protected void doBusiness() {
        super.doBusiness();
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        imgPublishDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PublicDetailActivity.this);
                builder.setTitle("提示")
                        .setMessage("您确定要删除这条公告么？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteMyPublsih(noticeId);
                            }
                        }).setNegativeButton("取消", null).show();
            }
        });
    }

    private void getUserInfo(final String userId) {
        mProgressDialog.show();
        ReqFindUserById.findUser(this, getActivityTag(), userId, new EntityCallBack<LoginResponse>() {
            @Override
            public Class<LoginResponse> getEntityClass() {
                return LoginResponse.class;
            }

            @Override
            public void onSuccess(LoginResponse resp) {
                if (resp.is200()) {
                    if (resp.obj.getHeadImg() != null && !resp.obj.getHeadImg().equals("")) {
                        userHeadUrl = resp.obj.getHeadImg();
                    }else{
                        userHeadUrl = null;
                    }
                    userName = resp.obj.getUserName();
                    tvPublishDetailUserName.setText(userName);
                    if (userHeadUrl != null && !userHeadUrl.equals("")) {
                        Glide.with(PublicDetailActivity.this).load(userHeadUrl).into(imgHead);
                    } else {
                        Glide.with(PublicDetailActivity.this).load(R.mipmap.user_head_img).into(imgHead);
                    }
                } else {
                    showToastLong("用户信息获取失败，请稍后重试");
                }
                mProgressDialog.dismiss();
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

    private void deleteMyPublsih(String noticeId) {
        mProgressDialog.setMessage("删除中");
        mProgressDialog.show();
        ReqDeletePublish.deletePublish(this, getActivityTag(), AppApplication.getCurrentUserInfo().getUserId(), noticeId, new EntityCallBack<BaseResp>() {
            @Override
            public Class<BaseResp> getEntityClass() {
                return BaseResp.class;
            }

            @Override
            public void onSuccess(BaseResp resp) {
                mProgressDialog.dismiss();
                if (resp.is200()) {
                    showToastShort("删除成功");
                    finish();
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

    private void getPublishDetail(String userId, String noticeId) {
        mProgressDialog.show();
        ReqPublicDetail.getPublic(this, getActivityTag(), userId, noticeId, new EntityCallBack<PublishDetailResponse>() {
            @Override
            public Class<PublishDetailResponse> getEntityClass() {
                return PublishDetailResponse.class;
            }

            @Override
            public void onSuccess(PublishDetailResponse resp) {
                if (resp.is200()) {
                    noticeTitle = resp.obj.getNoticeDetail().getNoticeTitle();
                    noticeContent = resp.obj.getNoticeDetail().getContent();
                    imgLists = resp.obj.getImgList();
                    List<PictureEntity> pictureEntities = new ArrayList<>();
                    for (int i = 0; i < imgLists.size(); i++) {
                        PictureEntity pictureEntity = new PictureEntity(1);
                        pictureEntity.setImgUrl(imgLists.get(i).getImgUrl());
                        pictureEntities.add(pictureEntity);
                    }
                    adapter = new GridNetPicAdapter(PublicDetailActivity.this, pictureEntities);
                    gvPublishDetailPic.setAdapter(adapter);
                    tvPublishDetailTitle.setText(noticeTitle);
                    tvPublishDetailContent.setText(noticeContent);


                } else {
                    showToastLong(resp.msg);
                }
                mProgressDialog.dismiss();
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
    protected int bindLayout() {
        return R.layout.activity_public_detail;
    }

    @Override
    protected void initView() {
        super.initView();
        mProgressDialog = new ProgressDialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setMessage(getString(R.string.processing));

        getPublishDetail(userId, noticeId);
        if (userId.equals(AppApplication.getCurrentUserInfo().getUserId())) {
            userName = AppApplication.getCurrentUserInfo().getUserName();
            imgPublishDelete.setVisibility(View.VISIBLE);
            if (AppApplication.getCurrentUserInfo().getHeadImg() != null && !AppApplication.getCurrentUserInfo().getHeadImg().equals("")) {
                userHeadUrl = AppApplication.getCurrentUserInfo().getHeadImg();
            } else {
                userHeadUrl = null;
            }
        } else {
            imgPublishDelete.setVisibility(View.GONE);
            getUserInfo(userId);
        }
        if (userHeadUrl != null && !userHeadUrl.equals("")) {
            Glide.with(this).load(userHeadUrl).into(imgHead);
        } else {
            Glide.with(this).load(R.mipmap.user_head_img).into(imgHead);
        }
        tvPublishDetailUserName.setText(userName);
    }
}
