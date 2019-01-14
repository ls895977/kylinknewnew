package com.maoyongxin.myapplication.ui.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jky.baselibrary.util.common.TimeUtil;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.MyDynamicListInfo;
import com.maoyongxin.myapplication.entity.PictureEntity;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.request.ReqCommunity;
import com.maoyongxin.myapplication.http.request.ReqFindUserById;
import com.maoyongxin.myapplication.http.request.ReqGetBannerList;
import com.maoyongxin.myapplication.http.request.ReqMyDynamicList;
import com.maoyongxin.myapplication.http.response.BannerResponse;
import com.maoyongxin.myapplication.http.response.BaseResp;
import com.maoyongxin.myapplication.http.response.LoginResponse;
import com.maoyongxin.myapplication.http.response.MyCommunityResponse;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.server.utils.NToast;
import com.maoyongxin.myapplication.server.widget.SelectableRoundedImageView;
import com.maoyongxin.myapplication.ui.ShowWebUrlActivity;
import com.maoyongxin.myapplication.ui.editapp.editadapter.SquareAdapter;
import com.maoyongxin.myapplication.ui.groupchat.DynamicDetail;
import com.maoyongxin.myapplication.ui.widget.BannerView;
import com.maoyongxin.myapplication.ui.widget.mylistview.AntGrideVIew;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2018-05-08.
 */

public class DynamicRecyclerAdapter extends RecyclerView.Adapter<DynamicRecyclerAdapter.MyHolder> {
    List<MyDynamicListInfo.DynamicList> list;
    Context context;
    LayoutInflater inflater;
    private GridDynamicPicAdapter adapter;
    private SquareAdapter.OnDynamicDeletedListener onDynamicDeletedListener;
    private boolean fromSquare;
    private String dynamicInfo = "http://st.3dgogo.com/index/user_dynamic/get_user_dynamic_post_num.html";

    public DynamicRecyclerAdapter(List<MyDynamicListInfo.DynamicList> list, Context context, boolean fromSquare) {
        this.list = list;
        this.context = context;
        this.fromSquare = fromSquare;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_layout_square, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, int i) {
        final int position = i;
        String userId = list.get(i).getDynamic().getUserId() + "";
        if (userId.equals(AppApplication.getCurrentUserInfo().getUserId())) {
            holder.img_delete.setVisibility(View.VISIBLE);
        } else {
            holder.img_delete.setVisibility(View.GONE);
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

        holder.tv_square_msgTitle.setText(list.get(i).getDynamic().getDynamicContent());
        holder.tv_creatTime.setText(TimeUtil.cmpLastMillisDesc(Long.parseLong(list.get(i).getDynamic().getCreateTime())));
        holder.numZan.setText(holder.NUMDZ + "");
        holder.numPL.setText(holder.NUMPL + "");

        final List<PictureEntity> imgPics = new ArrayList<>();
        final ArrayList<String> picsurl = new ArrayList<>();
        for (int j = 0; j < list.get(i).getImgList().size(); j++) {
            PictureEntity pictureEntity = new PictureEntity(1);
            pictureEntity.setImgUrl(list.get(i).getImgList().get(j).getImgUrl());
            picsurl.add(list.get(i).getImgList().get(j).getImgUrl());
            if (j < 2) {
                imgPics.add(pictureEntity);

            }

        }
        if (imgPics.size() == 0) {

        } else {
            holder.gv_DynamicPics.setVisibility(View.VISIBLE);
        }
        adapter = new GridDynamicPicAdapter(context, imgPics,picsurl);
        holder.gv_DynamicPics.setAdapter(adapter);
        getUserInfo(list.get(i).getDynamic().getUserId() + "", holder, dynamicInfo, list.get(position).getDynamic().getDynamicId() + "");
        //   getDynamicZan(dynamicInfo,holder,list.get(position).getDynamic().getDynamicId()+"");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DynamicDetail.class);
                intent.putExtra("dynamicString", holder.tv_square_msgTitle.getText());
                intent.putExtra("usrName", holder.tv_square_person_name.getText());
                intent.putExtra("time", holder.tv_creatTime.getText());
                intent.putExtra("dynamicId", list.get(position).getDynamic().getDynamicId() + "");
                intent.putExtra("companyName", holder.tv_userAdd.getText().toString());
                intent.putExtra("picUrl", holder.dynamic_head);
                intent.putExtra("parentId", list.get(position).getDynamic().getUserId() + "");
                intent.putStringArrayListExtra("pics", picsurl);
                intent.putExtra("numDZ", holder.NUMDZ);
                intent.putExtra("numPL", holder.NUMPL);
                intent.putStringArrayListExtra("pics", picsurl);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // 定义内部类继承ViewHolder
    class MyHolder extends RecyclerView.ViewHolder {
        TextView tv_square_person_name, tv_square_msgTitle, tv_creatTime, tv_userAdd, numPL, numZan;
        SelectableRoundedImageView img_square_header;
        AntGrideVIew gv_DynamicPics;
        ImageView img_delete;
        LinearLayout id1;
        String dynamic_head;
        int NUMPL, NUMDZ;

        public MyHolder(View view) {
            super(view);
            img_square_header = (SelectableRoundedImageView) view.findViewById(R.id.img_square_header);
            tv_square_person_name = (TextView) view.findViewById(R.id.tv_square_person_name);
            tv_square_msgTitle = (TextView) view.findViewById(R.id.tv_square_msgTitle);
            tv_creatTime = (TextView) view.findViewById(R.id.tv_creatTime);
            tv_userAdd = (TextView) view.findViewById(R.id.tv_userAdd);
            gv_DynamicPics = (AntGrideVIew) view.findViewById(R.id.gv_DynamicPics);
            img_delete = (ImageView) view.findViewById(R.id.img_delete);
            numPL = (TextView) view.findViewById(R.id.numPL);
            numZan = (TextView) view.findViewById(R.id.numZan);

            id1 = (LinearLayout) view.findViewById(R.id.id);


        }

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

    private void getUserInfo(String userId, final MyHolder holder, final String api_url, final String dynamicId) {
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
                        holder.dynamic_head = resp.obj.getHeadImg();
                    } else {
                        Glide.with(context).load(R.mipmap.user_head_img).into(holder.img_square_header);
                    }
                    holder.tv_square_person_name.setText(resp.obj.getUserName());
                    getUserCommunity(resp.obj.getUserId() + "", holder);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                OkHttpClient client = new OkHttpClient();
                                Request request = new Request.Builder().url(api_url + "?dynamicId=" + dynamicId)
                                        .build();
                                Response response = client.newCall(request).execute();


                                String responseData = response.body().string();

                                try {

                                    JSONObject jsonObject = new JSONObject(responseData);

                                    JSONObject data = jsonObject.getJSONObject("info");
                                    if (jsonObject.getInt("code") == 200) {
                                        holder.NUMDZ = data.getInt("praiseNum");
                                        holder.NUMPL = data.getInt("commentNum");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } catch (Exception e) {

                            }
                        }
                    }).start();


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

    private void getUserCommunity(String userId, final MyHolder holder) {
        ReqCommunity.getMyCommunity(context, "adapter", userId, new EntityCallBack<MyCommunityResponse>() {
            @Override
            public Class<MyCommunityResponse> getEntityClass() {
                return MyCommunityResponse.class;
            }

            @Override
            public void onSuccess(MyCommunityResponse resp) {
                if (resp.is200()) {
                    holder.tv_userAdd.setText(resp.obj.getCommunityName());
                } else {
                    holder.tv_userAdd.setText("没有小区的自由人");
                }
            }

            @Override
            public void onFailure(Throwable e) {
                holder.tv_userAdd.setText("没有小区的自由人");
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
