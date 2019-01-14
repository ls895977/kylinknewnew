package com.maoyongxin.myapplication.ui.news.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.maoyongxin.myapplication.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;


public class NewisActivity extends Fragment {
    @BindView(R.id.revert)
    RelativeLayout revert;
    @BindView(R.id.recycle1)
    RecyclerView recycle1;
    @BindView(R.id.recycle2)
    RecyclerView recycle2;
    @BindView(R.id.activity_perssondetails)
    LinearLayout activityPerssondetails;
    private ArrayList<String> list_title;
    private ArrayList<Fragment> list_fragment;
    private TabLayout mViewById;
    private ViewPager tweet_vp;
    private adaptera fAdapter;
    private List<newsBeanLeix.ObjBean.NewsLableListBean> newsLableList;
    int  s = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setContentView(R.layout.activity_newis);
        ButterKnife.bind(getActivity());
        mViewById = (TabLayout)getActivity().findViewById(R.id.tab_tweet);
        tweet_vp = (ViewPager) getActivity().findViewById(R.id.tweet_vp);


        initView();

    }


    private void initView() {

        Fragment1 stateFragment = new Fragment1();
        news_remen detailFragment = new news_remen();
       // Fragment3 stateFragment3 = new Fragment3();
        Fragment4 detailFragment4 = new Fragment4();
        Fragment5 stateFragment5 = new Fragment5();
        Fragment6 detailFragment6 = new Fragment6();
        Fragment7 detailFragment7 = new Fragment7();
        list_fragment = new ArrayList<>();
        list_fragment.add(stateFragment);
        list_fragment.add(detailFragment);
      //  list_fragment.add(stateFragment3);
        list_fragment.add(detailFragment4);
        list_fragment.add(detailFragment6);
        list_fragment.add(detailFragment7);
        mViewById.setTabMode(TabLayout.MODE_SCROLLABLE);



        list_title = new ArrayList<>();
        iniat();
//        list_title.add(" 最新数据  ");
//        list_title.add("  排行榜  ");
//        list_title.add("  上热评  ");
//        list_title.add("  评测室  ");
//        list_title.add("  发布会  ");
//        list_title.add("  阳台  ");
//        list_title.add("werds");
//        mViewById.addTab(mViewById.newTab().setText(list_title.get(0)));
//        mViewById.addTab(mViewById.newTab().setText(list_title.get(1)));
//        mViewById.addTab(mViewById.newTab().setText(list_title.get(2)));
//        mViewById.addTab(mViewById.newTab().setText(list_title.get(3)));
//        mViewById.addTab(mViewById.newTab().setText(list_title.get(4)));
//        mViewById.addTab(mViewById.newTab().setText(list_title.get(5)));
//        mViewById.addTab(mViewById.newTab().setText(list_title.get(6)));

    }

    private void iniat() {

        OkHttpUtils.post().url(Contant.MeUrl2 + "newslablecontroller/getNewsLable")
//                .addParams("versionCode", versionCode + "")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                //Log.e("//", "onError: " + e);
            }

            @Override
            public void onResponse(String response, int id) {
                //Log.e("response", "onResponse: " + response);
                Gson gson = new Gson();
                newsBeanLeix newsBeanLeix = gson.fromJson(response, newsBeanLeix.class);
                newsLableList = newsBeanLeix.getObj().getNewsLableList();

              for(int i = 0 ; i<newsLableList.size();i++){
                  String lableName = newsLableList.get(i).getLableName();
                  list_title.add(newsLableList.get(i).getLableName());
                  mViewById.addTab(mViewById.newTab().setText(lableName));

                  fAdapter = new adaptera(getActivity().getSupportFragmentManager(), list_fragment, list_title);
                  tweet_vp.setAdapter(fAdapter);
                  mViewById.setupWithViewPager(tweet_vp);
              }
            }
        });

    }

    private void indo() {
        LinearLayoutManager ms = new LinearLayoutManager(getActivity());

        ms.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycle2.setLayoutManager(ms);
        aineirong roomAdapter = new aineirong();
        recycle2.setAdapter(roomAdapter);
        roomAdapter.notifyDataSetChanged();


    }

    private void networka(int lableId) {
        String url = Contant.MeUrl2 + "newscontroller/getNews";
        OkHttpUtils.post().url(url)

                .addParams("lableId ", lableId + "")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                //Log.e("//???", "onError: "+e );
            }

            @Override
            public void onResponse(String response, int id) {
                //Log.e("////", "onResponse: "+response );
            }
        });

    }


    public class aineirong extends RecyclerView.Adapter {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.ainei, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            final ViewHolder myViewHolder = (ViewHolder) holder;
            myViewHolder.xian.setVisibility(View.GONE);

            myViewHolder.texta.setText(newsLableList.get(position).getLableName());
//            s=position;
            if(position!=s){
                myViewHolder.xian.setVisibility(View.GONE);
            }else {
                myViewHolder.xian.setVisibility(View.VISIBLE);
                s=100;
            }
            myViewHolder.linear.setOnClickListener(new View.OnClickListener() {

                private int lableId;

                @Override
                public void onClick(View v) {
                    s=position;
                    lableId = newsLableList.get(position).getLableId();
//                    name = body.get(position).getName();
                    networka(lableId);
                    notifyDataSetChanged();
                }
            });

        }

        @Override
        public int getItemCount() {
            return newsLableList == null ? 0 : newsLableList.size();
        }

         class ViewHolder extends RecyclerView.ViewHolder{
            @BindView(R.id.texta)
            TextView texta;
            @BindView(R.id.xian)
            ImageView xian;
            @BindView(R.id.linear)
            LinearLayout linear;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }

}
