package com.maoyongxin.myapplication.ui.editapp.editadapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.jky.baselibrary.util.common.TimeUtil;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.MyDynamicListInfo;
import com.maoyongxin.myapplication.entity.PictureEntity;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.request.ReqAddFollow;
import com.maoyongxin.myapplication.http.request.ReqCommunity;
import com.maoyongxin.myapplication.http.request.ReqFindUserById;
import com.maoyongxin.myapplication.http.request.ReqGetBannerList;
import com.maoyongxin.myapplication.http.request.ReqMyDynamicList;
import com.maoyongxin.myapplication.http.response.BannerResponse;
import com.maoyongxin.myapplication.http.response.BaseResp;
import com.maoyongxin.myapplication.http.response.FollowResponse;
import com.maoyongxin.myapplication.http.response.LoginResponse;
import com.maoyongxin.myapplication.http.response.MyCommunityResponse;
import com.maoyongxin.myapplication.http.response.MyDongTaiListResponse;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.server.utils.NToast;
import com.maoyongxin.myapplication.server.widget.SelectableRoundedImageView;
import com.maoyongxin.myapplication.ui.JiGuangSharePlatformModel;
import com.maoyongxin.myapplication.ui.News_web;
import com.maoyongxin.myapplication.ui.ShowWebUrlActivity;
import com.maoyongxin.myapplication.ui.StrangerDetailActivity;
import com.maoyongxin.myapplication.ui.adapter.DoubleDynamicPicAdapter;
import com.maoyongxin.myapplication.ui.adapter.GridDynamicPicAdapter;
import com.maoyongxin.myapplication.ui.adapter.SingleDynamicPicAdapter;
import com.maoyongxin.myapplication.ui.fragment.ShareDialogFragment;
import com.maoyongxin.myapplication.ui.groupchat.Comment_Detail;
import com.maoyongxin.myapplication.ui.groupchat.DynamicDetail;
import com.maoyongxin.myapplication.ui.widget.BannerView;
import com.maoyongxin.myapplication.ui.widget.mylistview.AntGrideVIew;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

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
import io.github.rockerhieu.emojicon.EmojiconTextView;
import okhttp3.Call;

import static android.view.View.GONE;
import static com.maoyongxin.myapplication.myapp.AppFragment.showToastShort;


/**
 * Created by Administrator on 2017/11/23.
 */

public class SquareAdapter extends BaseAdapter implements ShareDialogFragment.Listener  {
    List<MyDynamicListInfo.DynamicList> list;

    Context context;
    LayoutInflater inflater;
    private GridDynamicPicAdapter   adapter;
    private SingleDynamicPicAdapter sigleAdapter;
    private DoubleDynamicPicAdapter doubleDynamicPicAdapter;
    private OnDynamicDeletedListener onDynamicDeletedListener;
    private boolean fromSquare;


    private String webapi = "http://st.3dgogo.com/index/enterprise_publicity/get_community_id_details_url_api.html?community_id=";
    private Map<String, String> map = new HashMap<>();
    private ArrayList<JiGuangSharePlatformModel> sharelist = new ArrayList<>();
    public SquareAdapter(List<MyDynamicListInfo.DynamicList> list, Context context, boolean fromSquare) {
        this.list = list;
        this.context = context;
        this.fromSquare = fromSquare;
        inflater = LayoutInflater.from(context);


    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    @Override
    public int getCount() {
        if (fromSquare) {
            return list.size() + 1;
        } else {
            return list.size();
        }
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (i == 0 && fromSquare) {
            view = inflater.inflate(R.layout.item_banner_view, null);
            BannerView bannerView = (BannerView) view.findViewById(R.id.banner);
            initBannerView(bannerView);
            return view;
        } else {
            if (fromSquare) {
                i--;
            }
            final ViewHolder holder;
            if (view == null || view.getTag() == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.item_layout_square, null);
                holder.img_square_header = (SelectableRoundedImageView) view.findViewById(R.id.img_square_header);
                holder.id = (LinearLayout) view.findViewById(R.id.id);
                holder.tv_square_person_name = (TextView) view.findViewById(R.id.tv_square_person_name);
                holder.tv_square_msgTitle = (EmojiconTextView) view.findViewById(R.id.tv_square_msgTitle);
                holder.tv_creatTime = (TextView) view.findViewById(R.id.tv_creatTime);
                holder.tv_userAdd = (TextView) view.findViewById(R.id.tv_userAdd);
                holder.gv_DynamicPics = (AntGrideVIew) view.findViewById(R.id.gv_DynamicPics);
                holder.img_delete = (ImageView) view.findViewById(R.id.img_delete);
                holder.two_DynamicPics = (AntGrideVIew) view.findViewById(R.id.two_DynamicPics);
                holder.single_img = (AntGrideVIew) view.findViewById(R.id.single_img);
                holder.numZan = (TextView) view.findViewById(R.id.numZan);
                holder.numPingLun = (TextView) view.findViewById(R.id.numPL);
                holder.follow_button=(Button)view.findViewById(R.id.follow_button);
                holder.zanImg=(ImageView)view.findViewById(R.id.zanImg);
                holder.ll_zan=(LinearLayout)view.findViewById(R.id.ll_zan);
                holder.id2=(LinearLayout)view.findViewById(R.id.id2);
                holder.ic_share=(ImageView)view.findViewById(R.id.ic_share);
                holder.id1=(LinearLayout)view.findViewById(R.id.id1);
                holder.all_view=(LinearLayout)view.findViewById(R.id.all_view);
                holder.ll_share=(LinearLayout)view.findViewById(R.id.ll_share);
                holder.ll_community=(LinearLayout)view.findViewById(R.id.ll_community);

                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            final int position = i;
            final String userId = list.get(i).getDynamic().getUserId() + "";


            holder.tv_userAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.joined) {
                        Intent intent = new Intent(context, News_web.class);
                        intent.putExtra("weburl", webapi + holder.CommunityId.toString());
                        intent.putExtra("company_name", holder.tv_userAdd.getText());
                        intent.putExtra("communityId",holder.CommunityId);
                        intent.putExtra("targetUserId", userId);
                        context.startActivity(intent);
                        backdatatoserver( holder.CommunityId.toString());
                    } else {
                        Toast.makeText(context, "该会员暂没有开通企业展示", Toast.LENGTH_SHORT).show();
                    }

                }
            });


            if (userId.equals(AppApplication.getCurrentUserInfo().getUserId())) {
                holder.img_delete.setVisibility(View.VISIBLE);
            } else {
                holder.img_delete.setVisibility(GONE);
            }
            holder.img_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("提示").setMessage("你确定要删除这条动态么？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    deleteMyDynamic(list.get(position).getDynamic().getDynamicId() + "");
                                }
                            }).setNegativeButton("取消", null).show();
                }
            });

            holder.tv_square_msgTitle.setText(new String (Base64.decode(list.get(i).getDynamic().getDynamicContent().getBytes(), Base64.DEFAULT)));
            holder.tv_creatTime.setText(TimeUtil.cmpLastMillisDesc(Long.parseLong(list.get(i).getDynamic().getCreateTime())));

            iniImg(holder,list.get(position).getDynamic().getDynamicId()+"");

            holder.CommunityId=list.get(i).getDynamic().getDynamicId()+"";

            final ArrayList<PictureEntity> imgPics = new ArrayList<>();
            final ArrayList<String> picsurl = new ArrayList<>();



            if (list.get(i).getImgList() != null) {

                for (int j = 0; j < list.get(i).getImgList().size(); j++) {

                    PictureEntity pictureEntity = new PictureEntity(1);
                    holder.sharetype=1;

                    pictureEntity.setImgUrl(list.get(i).getImgList().get(j).getImgUrl());

                    picsurl.add(list.get(i).getImgList().get(j).getImgUrl());

                    if (list.get(i).getVideoList() != null) {

                        for (int videoCount = 0; videoCount < list.get(i).getVideoList().size(); videoCount++) {

                            if (pictureEntity.getImgUrl().contains( list.get(i).getVideoList().get(videoCount).getVideoUrl().split("\\.")[0]  )) {
                                pictureEntity.setType(2);
                                holder.sharetype=2;
                                holder.videourl=list.get(i).getVideoList().get(videoCount).getShareUrl();



                                holder.sharetitle=list.get(i).getDynamic().getDynamicContent();
                                pictureEntity.setVideoUrl(list.get(i).getVideoList().get(videoCount).getVideoUrl());
                                pictureEntity.setTitle(list.get(i).getDynamic().getDynamicContent());

                            }
                        }

                    }

                    imgPics.add(pictureEntity);
                }
            }

            if (list.get(i).getVideoList() != null &&
                    imgPics.size() == 0 && list.get(i).getVideoList().size() == 0) {
                holder.gv_DynamicPics.setVisibility(GONE);
            } else {
                holder.gv_DynamicPics.setVisibility(View.VISIBLE);
            }

            if(list.get(i).getImgList().size()>0)
            {
                Log.e("1111111111111111111111","i:"+i+"=====size:"+picsurl.size()+"======url"+picsurl.get(0));
                switch (list.get(i).getImgList().size()) {

                    case 1:

                            sigleAdapter = new SingleDynamicPicAdapter(context,imgPics,picsurl);



                        holder.single_img.setVisibility(View.VISIBLE);
                        holder.single_img.setAdapter(sigleAdapter);
                        holder.gv_DynamicPics.setVisibility(GONE);
                        holder.two_DynamicPics.setVisibility(GONE);

                        break;

                    case 2:
                        doubleDynamicPicAdapter = new DoubleDynamicPicAdapter(context, imgPics, picsurl);
                        holder.two_DynamicPics.setAdapter(doubleDynamicPicAdapter);
                        holder.two_DynamicPics.setVisibility(View.VISIBLE);

                        holder.single_img.setVisibility(GONE);
                        holder.gv_DynamicPics.setVisibility(GONE);
                        break;

                    default:
                        adapter = new GridDynamicPicAdapter(context, imgPics, picsurl);

                        holder.gv_DynamicPics.setAdapter(adapter);
                        holder.gv_DynamicPics.setVisibility(View.VISIBLE);

                        holder.single_img.setVisibility(GONE);
                        holder.two_DynamicPics.setVisibility(GONE);


                        break;

                }
            }

            String userid = list.get(i).getDynamic().getUserId() + "";

            if (map.containsKey(userid)) {

                holder.follow_button.setBackground(context.getDrawable(R.drawable.bg_radius_white));
                holder.follow_button.setTextColor(context.getResources().getColor(R.color.blue_tiny));
              //  holder.follow_button.setText("已关注");
                holder.follow_button.setVisibility(View.INVISIBLE);

            } else {
                holder.follow_button.setVisibility(View.VISIBLE);
                holder.follow_button.setBackground(context.getDrawable(R.drawable.bg_radius_blue_tiny));
                holder.follow_button.setTextColor(context.getResources().getColor(R.color.white));
                holder.follow_button.setText("关注");

            }
            getUserInfo(userid, holder);
            final ViewHolder finalHolder1 = holder;
            holder.id.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent newintent=new Intent(context,Comment_Detail.class);
                    newintent.putExtra("dynamicId",list.get(position).getDynamic().getDynamicId() + "");
                    newintent.putExtra("communityName",finalHolder1.tv_userAdd.getText().toString());
                    newintent.putExtra("community_id",holder.CommunityId);
                    newintent.putExtra("parentUserId",list.get(position).getDynamic().getUserId() + "");

                    context.startActivity(newintent);
                }
            });


            holder.id1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("分享")
                            .setIcon(R.mipmap.wechat_moment)
                            .setNegativeButton("微信好友", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    shareWeChat(holder);
                                }
                            });
                    builder.setPositiveButton("朋友圈", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                                    shareWeChatMoments(holder);
                        }
                    });
                    builder.show();
                }

            });
            holder.id2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent newintent=new Intent(context,Comment_Detail.class);
                    newintent.putExtra("dynamicId",list.get(position).getDynamic().getDynamicId() + "");
                    newintent.putExtra("communityName",finalHolder1.tv_userAdd.getText().toString());
                    newintent.putExtra("community_id",holder.CommunityId);
                    newintent.putExtra("parentUserId",list.get(position).getDynamic().getUserId() + "");

                    context.startActivity(newintent);
                }
            });



            holder.img_square_header.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, StrangerDetailActivity.class);
                    intent.putExtra("personId", list.get(position).getDynamic().getUserId() + "");
                    context.startActivity(intent);

                }
            });


            holder.follow_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //Log.e("guanzhu", "personId:" + list.get(position).getDynamic().getUserId() + "====userName:" + finalHolder1.tv_square_person_name.getText().toString());
                    doLike(String.valueOf(list.get(position).getDynamic().getUserId()), finalHolder1.tv_square_person_name.getText().toString());
                }
            });

            holder.ll_zan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dianzan(holder,"dynamic_id",list.get(position).getDynamic().getDynamicId()+"");
                }
            });
//            holder.sharebitmap=convertViewToBitmap(holder.all_view);

            return view;
        }
    }

    private void doLike(String personId, String userName) {
        ReqAddFollow.addFollow(context, "adapter", AppApplication.getCurrentUserInfo().getUserId(), personId, userName, new EntityCallBack<FollowResponse>() {
            @Override
            public Class<FollowResponse> getEntityClass() {
                return FollowResponse.class;
            }

            @Override
            public void onSuccess(FollowResponse resp) {
//                showToastShort(resp.msg);
                Toast.makeText(context, resp.msg, Toast.LENGTH_SHORT).show();
                if(item!=null){
                    item.Item();
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

    private void dianzan(final ViewHolder holder, String zanType,  String dianzanID){//点赞
        OkHttpUtils.post().url("http://st.3dgogo.com/index/user_dynamic/set_user_praise_num_api.html")
                .addParams("type", "1")
                .addParams("user_id", AppApplication.getCurrentUserInfo().getUserId())
                .addParams("zdm", zanType)
                .addParams(zanType, dianzanID)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                try
                {
                    holder.zanImg.setImageResource(R.mipmap.gooded_big);
                    int numzan= Integer.valueOf(holder.numZan.getText()+"")+1;
                    holder.numZan.setText(numzan+"");

                    holder.zanImg.startAnimation(AnimationUtils.loadAnimation(

                            context, R.anim.zan_anim));


                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }


            }
        });
    }


    private void deleteMyDynamic(String dynamicId) {
        ReqMyDynamicList.deleteMyDynamic(context, "adapter", AppApplication.getCurrentUserInfo().getUserId(), dynamicId, new EntityCallBack<BaseResp>() {
            @Override
            public Class<BaseResp> getEntityClass() {
                return BaseResp.class;
            }

            @Override
            public void onSuccess(BaseResp resp) {
                if (resp.is200()) {
                    NToast.shortToast(context, resp.getMsg());
                    onDynamicDeletedListener.delete();
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

    private void initBannerView(final BannerView view) {
        ReqGetBannerList.getBanner(context, "adapter", new EntityCallBack<BannerResponse>() {
            @Override
            public Class<BannerResponse> getEntityClass() {
                return BannerResponse.class;
            }

            @Override
            public void onSuccess(BannerResponse resp) {
                if (resp.is200()) {
                    /**
                     * 设置顶部banner
                     */
                    view.setBannerViewListener(new BannerView.BannerViewListener() {
                        @Override
                        public void onShowImage(ImageView view, String imgUrl) {
                            Glide.with(context).load(imgUrl).into(view);
                        }

                        @Override
                        public void onItemClick(int position, String targetUrl, Object userData) {
                            Intent intent = new Intent(context, ShowWebUrlActivity.class);
                            intent.putExtra("url", targetUrl);
                            context.startActivity(intent);
                        }

                        @Override
                        public View onCreateIndicatorView() {
                            ImageView v = new ImageView(context);
                            v.setPadding(10, 0, 0, 0);
                            v.setImageResource(R.drawable.pager_indicator_selector);
                            return v;
                        }
                    });
                    ArrayList<BannerView.BannerInfo> testData = new ArrayList<>();
                    for (int i = 0; i < resp.obj.getBannerList().size(); i++) {
                        testData.add(new BannerView.BannerInfo(resp.obj.getBannerList().get(i).getImgUrl(), resp.obj.getBannerList().get(i).getConnectionAddress()));
                    }
                    view.setBannerDatas(testData);
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

    private String user = "";

    private void getUserInfo(String userId, final ViewHolder holder) {
        ReqFindUserById.findUser(context, "adapter", userId, new EntityCallBack<LoginResponse>() {
            @Override
            public Class<LoginResponse> getEntityClass() {
                return LoginResponse.class;
            }

            @Override
            public void onSuccess(LoginResponse resp) {
                if (resp.is200()) {
                    if (resp.obj.getHeadImg() != null && !resp.obj.getHeadImg().equals("")) {
                        Glide.with(context).load(resp.obj.getHeadImg()).into(holder.img_square_header);
                    } else {
                        Glide.with(context).load(R.mipmap.user_head_img).into(holder.img_square_header);
                    }
                    user = resp.obj.getUserName();
                    holder.tv_square_person_name.setText(resp.obj.getUserName());
                    holder.dynamic_head = resp.obj.getHeadImg();
                    getUserCommunity(resp.obj.getUserId(), holder);
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
    private void iniImg(final ViewHolder viewHolder,String dynamicId) {

        OkHttpUtils.post().url("http://st.3dgogo.com/index/user_dynamic/get_user_dynamic_post_num.html")
                .addParams("dynamicId",dynamicId)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                        viewHolder.numZan.setText(jsonObject.getJSONObject("info").getString("praiseNum"));
                        viewHolder.numPingLun.setText(jsonObject.getJSONObject("info").getString("commentNum"));


                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }


            }
        });

    }
    private void getUserCommunity(String userId, final ViewHolder holder) {
        ReqCommunity.getMyCommunity(context, "adapter", userId, new EntityCallBack<MyCommunityResponse>() {
            @Override
            public Class<MyCommunityResponse> getEntityClass() {
                return MyCommunityResponse.class;
            }

            @Override
            public void onSuccess(MyCommunityResponse resp) {
                if (resp.is200()) {
                    holder.tv_userAdd.setText(resp.obj.getCommunityName());
                    holder.ll_community.setVisibility(View.VISIBLE);
                    holder.CommunityId = resp.obj.getCommunityId();
                    holder.joined = true;
                } else {
                    holder.ll_community.setVisibility(View.GONE);
                    holder.tv_userAdd.setText("暂未开通服务号");
                    holder.joined = false;
                }
            }

            @Override
            public void onFailure(Throwable e) {
                holder.tv_userAdd.setText("暂未开通服务号");
                holder.ll_community.setVisibility(View.GONE);
                holder.joined = false;
            }

            @Override
            public void onCancelled(Throwable e) {

            }

            @Override
            public void onFinished() {

            }
        });
    }



    private class ViewHolder {
        TextView tv_square_person_name, tv_creatTime, tv_userAdd;
        EmojiconTextView tv_square_msgTitle;
        SelectableRoundedImageView img_square_header;
        AntGrideVIew gv_DynamicPics;
        AntGrideVIew two_DynamicPics;
        AntGrideVIew single_img;
        ImageView img_delete;
        String dynamic_head, CommunityId,videourl,sharetitle;
        LinearLayout id,ll_community;
        TextView numZan;
        TextView numPingLun;
        Boolean joined = false;
        Button follow_button;
        ImageView zanImg;
        LinearLayout ll_zan,id2,id1,all_view,ll_share;
        ImageView ic_share;
        Bitmap sharebitmap;
        int sharetype;
    }

    public interface OnDynamicDeletedListener {
        void delete();
    }

    public void setOnDynamicDeletedListener(OnDynamicDeletedListener onDynamicDeletedListener) {
        this.onDynamicDeletedListener = onDynamicDeletedListener;
    }


    public interface Item{
        public void Item();
    }
    public Item item;
    public void setItem(Item item){
        this.item = item;
    }

    @Override
    public void onSharePlatformClicked(int position) {
        Toast.makeText(context, "正在准备分享...", Toast.LENGTH_SHORT).show();
        JiGuangSharePlatformModel model = sharelist.get(position);

        switch (model.getPlatFormType()) {
            case WE_CHAT:
              //  shareWeChat(,sharetype);
                break;
            case WE_CHAT_MOMNETS:
             //   shareWeChatMoments(sharetype);
                break;
        }
    }

    private void shareWeChat(ViewHolder holder) {
        try {
            JShareInterface.share(Wechat.Name, generateParams(holder), new PlatActionListener() {
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

    private void shareWeChatMoments(ViewHolder holder) {
        JShareInterface.share(WechatMoments.Name, generateParams(holder), null);
    }



    private ShareParams generateParams(ViewHolder holder) {
        ShareParams shareParams = new ShareParams();
        switch (holder.sharetype)
        {
            case 1:
                shareParams.setShareType(Platform.SHARE_IMAGE);
                holder.sharebitmap=convertViewToBitmap(holder);
                shareParams.setImageData(holder.sharebitmap);
                break;
            case 2:
                shareParams.setShareType(Platform.SHARE_VIDEO);
                shareParams.setTitle(holder.sharetitle);
                shareParams.setText("加入彼信商业社区，跟踪实时商业动态");
                shareParams.setUrl(holder.videourl);
               // shareParams.setImageData(shareBit);
                holder.sharebitmap=convertViewToBitmap(holder);
                shareParams.setImageData(holder.sharebitmap);
                //视频分享到朋友圈
               // http://st.3dgogo.com:8083
                break;

        }



        return shareParams;
    }

    private Bitmap convertViewToBitmap(ViewHolder viewHolder) {

        viewHolder.all_view.setDrawingCacheEnabled(true);
        viewHolder.ll_share.setVisibility(View.VISIBLE);
        viewHolder.all_view.buildDrawingCache();  //启用DrawingCache并创建位图
        Bitmap bitmap = Bitmap.createBitmap(viewHolder.all_view.getDrawingCache()); //创建一个DrawingCache的拷贝，因为DrawingCache得到的位图在禁用后会被回收
        viewHolder.ll_share.setVisibility(GONE);
        viewHolder.all_view.setDrawingCacheEnabled(false);

        return bitmap;
    }
    private void backdatatoserver(final String comm)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.post().url("http://st.3dgogo.com/index/enterprise_publicity_statistics/setPublicityNumApi.html")
                        .addParams("communityId",comm)
                        .addParams("nick_name",AppApplication.getCurrentUserInfo().getUserName())
                        .addParams("type","1")
                        .addParams("uid",AppApplication.getCurrentUserInfo().getUserId())

                        .build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {

                    }
                });

            }
        }).start();
    }
}
