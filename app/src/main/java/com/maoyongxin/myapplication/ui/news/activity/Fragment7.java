package com.maoyongxin.myapplication.ui.news.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.maoyongxin.myapplication.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * Created by Administrator on 2018/5/22 0022.
 */

public class Fragment7 extends Fragment {

    View mView;

    Unbinder unbinder;
    @BindView(R.id.recycle1)
    RecyclerView recycle1;

    private List<fragment7Bean.ObjBean.NewsListBean> newsList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment7, null);
        unbinder = ButterKnife.bind(this, mView);
        initview();
        return mView;
    }

    private void initview() {

        OkHttpUtils.post().url(Contant.MeUrl2 + "newscontroller/getNews")
                .addParams("lableId", 13 + "")

                .build().execute(new StringCallback() {



            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                Gson gson = new Gson();
                fragment7Bean new4bean = gson.fromJson(response, fragment7Bean.class);
                newsList = new4bean.getObj().getNewsList();

                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recycle1.setLayoutManager(layoutManager);
                aineirong aineirong = new aineirong();
                recycle1.setAdapter(aineirong);
                aineirong.notifyDataSetChanged();
            }
        });

    }


    public class aineirong extends RecyclerView.Adapter {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.falist7, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            final ViewHolder myViewHolder = (ViewHolder) holder;
            myViewHolder.noticeTitle1.setText(newsList.get(position).getTitle());
            myViewHolder.noticeSender1.setText(newsList.get(position).getCreateTime() + "");
            myViewHolder.dsad.setText("最新新闻");
            myViewHolder.noticeSender.setText("浏览量：" + newsList.get(position).getReadNum() + "");
            final int ids = newsList.get(position).getId();
            final int uid = newsList.get(position).getUid();
            final String content = newsList.get(position).getContent();
            myViewHolder.  linh.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sPayrecordActivity = new Intent(getContext(), fragment1Activity.class);
                    sPayrecordActivity.putExtra("id1",ids+"");
                    sPayrecordActivity.putExtra("uid",uid+"");
                    sPayrecordActivity.putExtra("content",content+"");
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
            @BindView(R.id.notice_reade1)
            TextView noticeReade1;
            @BindView(R.id.notice_title1)
            TextView noticeTitle1;
            @BindView(R.id.notice_sdtime1)
            TextView noticeSdtime1;
            @BindView(R.id.notice_sender1)
            TextView noticeSender1;
            @BindView(R.id.notice_sender)
            TextView noticeSender;
            @BindView(R.id.linh)
            LinearLayout linh;
            @BindView(R.id.dsad)
            TextView dsad;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }

}
