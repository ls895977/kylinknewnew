package com.maoyongxin.myapplication.ui.news.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.request.ReqNewsZanPlun;
import com.maoyongxin.myapplication.http.response.NewsZPResponse;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.maoyongxin.myapplication.ui.news.activity.Contant.MeUrl2;

/**
 * Created by Administrator on 2018/5/22 0022.
 */

public class Fragment1 extends Fragment {

    View mView;

    @BindView(R.id.banner1)
    BannerLayout bannerLayout1;

    Unbinder unbinder;

    @BindView(R.id.recycle1)
    RecyclerView recycle1;
    private List<news1BeanLeix.ObjBean.NewsListBean> newsList;

    private List<new4bean.ObjBean.NewsListBean> bannerList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment1, null);
        ButterKnife.bind(this, mView);
        recycle1.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recycle1.setItemAnimator(null);
        recycle1.setHasFixedSize(true);
        recycle1.setNestedScrollingEnabled(false);
        NewsAdapter newsAdapter = new NewsAdapter();
        recycle1.setAdapter(newsAdapter);
        initview();
        initBanner();
        return mView;
    }

    private void initBanner() {
        OkHttpUtils.post().url(MeUrl2 + "newscontroller/getNews")
                .addParams("lableId", 2 + "")
                .build().execute(new StringCallback() {

            @Override
            public void onError(Call call, Exception e, int id) {
                initBanner();
            }

            @Override
            public void onResponse(String response, int id) {
                Gson gson = new Gson();
                new4bean new4bean = gson.fromJson(response, com.maoyongxin.myapplication.ui.news.activity.new4bean.class);
                bannerList = new4bean.getObj().getNewsList();
                List<String> res = new ArrayList<>();
                List<String> titles = new ArrayList<>();
                if (bannerList != null || bannerList.size() > 0) {
                    for (int position = 0; position < 3; position++) {
                        res.add("http://118.24.2.164:8083/news/" + bannerList.get(position).getImg() + "");
                        titles.add(bannerList.get(position).getTitle() + "");
                    }
                } else {

                }
                if (bannerLayout1 != null) {
                    bannerLayout1.setViewUrls(res, titles);
                }
                bannerLayout1.setOnBannerItemClickListener(new BannerLayout.OnBannerItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Toast.makeText(getContext(), "position:" + position, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }


    private void initview() {

        OkHttpUtils.post().url(MeUrl2 + "newscontroller/getNews")
                .addParams("lableId", 2 + "")

                .build().execute(new StringCallback() {


            @Override
            public void onError(Call call, Exception e, int id) {
                //Log.e("//", "onError: " + e);
            }

            @Override
            public void onResponse(String response, int id) {
                //Log.e("aasasassasasasasasas", "onResponse: " + response);
                Gson gson = new Gson();
                news1BeanLeix news1BeanLeix = gson.fromJson(response, com.maoyongxin.myapplication.ui.news.activity.news1BeanLeix.class);
                newsList = news1BeanLeix.getObj().getNewsList();


                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recycle1.setLayoutManager(layoutManager);


                NewsAdapter newsAdapter = new NewsAdapter();
                recycle1.setAdapter(newsAdapter);
                newsAdapter.notifyDataSetChanged();
            }
        });


    }

    public class NewsAdapter extends RecyclerView.Adapter {


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_newslayout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            final ViewHolder myViewHolder = (ViewHolder) holder;
            Random random = new Random();
            if(position==0)
            {
                myViewHolder.toplin.setVisibility(View.VISIBLE);
            }
            else
            {
                myViewHolder.toplin.setVisibility(View.GONE);
            }
            myViewHolder.noticeTitle1.setText(newsList.get(position).getTitle());
            myViewHolder.noticeSource.setText(newsList.get(position).getSource());
            myViewHolder.noticeSender1.setText(timeStamp2Date(newsList.get(position).getCreateTime(), "MM-dd") + "");
            getZP(newsList.get(position).getId() + "", myViewHolder);
            myViewHolder.noticeSender.setText((newsList.get(position).getReadNum() + random.nextInt(1000)) + "");//阅读量

            Glide.with(getContext()).load("http://118.24.2.164:8083/news/" + newsList.get(position).getImg() + "").into(myViewHolder.noticeImage);
            final int ids = newsList.get(position).getId();
            final int uid = newsList.get(position).getUid();
            final String newstitle = newsList.get(position).getTitle();
            final String content = newsList.get(position).getContent();
            final String source = newsList.get(position).getSource();
            final String Imgurl = "http://118.24.2.164:8083/news/" + newsList.get(position).getImg() + "";
            myViewHolder.linh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sPayrecordActivity = new Intent(getContext(), fragment1Activity.class);
                    sPayrecordActivity.putExtra("id1", ids + "");
                    sPayrecordActivity.putExtra("uid", uid + "");
                    sPayrecordActivity.putExtra("newstitle", newstitle + "");
                    sPayrecordActivity.putExtra("content", content + "");
                    sPayrecordActivity.putExtra("source", source + "");
                    sPayrecordActivity.putExtra("news_Img", Imgurl);
                    startActivity(sPayrecordActivity);
                }
            });
        }

        @Override
        public int getItemCount() {
            return newsList == null ? 0 : newsList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.notice_image)
            ImageView noticeImage;

            @BindView(R.id.notice_source)
            TextView noticeSource;

            @BindView(R.id.notice_title1)
            TextView noticeTitle1;


            @BindView(R.id.notice_sender1)
            TextView noticeSender1;
            @BindView(R.id.notice_sender)
            TextView noticeSender;
            @BindView(R.id.linh)
            LinearLayout linh;

            @BindView(R.id.toplin)
            LinearLayout toplin;




            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }

        }

        private void getZP(final String newsId, final ViewHolder myviewHolder) {
            OkHttpUtils.post().url("http://st.3dgogo.com/index/news/get_read_post_num_api.html")
                    .addParams("id",newsId)
                    .build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {

                }

                @Override
                public void onResponse(String response, int id) {


                    try {

                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getInt("code") == 200) {
                            String read_num = jsonObject.getJSONObject("info").getString("read_num");
                            String post_num = jsonObject.getJSONObject("info").getString("post_num");
                            myviewHolder.noticeSender.setText(read_num);


                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        //  showMessagefail("深度解析失败3");
                                            }
                }
                    });
        }



        private void getnewZP(String newsId) {
            ReqNewsZanPlun.getnewsZP(getActivity(), getTag(), newsId, new EntityCallBack<NewsZPResponse>() {
                @Override
                public Class<NewsZPResponse> getEntityClass() {
                    return NewsZPResponse.class;
                }

                @Override
                public void onSuccess(NewsZPResponse resp) {
                    resp.obj.getNumPlun();
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

        public String timeStamp2Date(long time, String format) {
            if (format == null || format.isEmpty()) {
                format = "yyyy-MM-dd HH:mm:ss";
            }
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(new Date(time));
        }
    }


}
