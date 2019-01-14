package com.maoyongxin.myapplication.ui.news.activity;

import android.animation.Animator;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.ui.news.Inject;
import com.maoyongxin.myapplication.ui.news.fragment.NewsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsActivity extends NewsBaseActivity {

    private static final String TAG = "NewsActivity";

    @BindView(R.id.scrim)
    View scrim;
    @BindView(R.id.loading)
    ProgressBar loading;
    @BindView(R.id.text)
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ButterKnife.bind(this);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_news, new NewsFragment())
                .commitAllowingStateLoss();

    }


    public void setLoading(boolean isLoading) {
        Log.d(TAG, "setLoading: " + isLoading);

        scrim.removeCallbacks(hideInfo);
        scrim.animate().cancel();
        scrim.animate().setListener(null);

        if (isLoading) {
            scrim.setVisibility(View.VISIBLE);
            loading.setVisibility(View.VISIBLE);
            text.setText("加载中...");
            scrim.setAlpha(0f);
            scrim.animate().alpha(1f).start();
        } else {
            scrim.animate().alpha(0f).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    scrim.setVisibility(View.INVISIBLE);
                }

                @Override public void onAnimationStart(Animator animation) { }
                @Override public void onAnimationCancel(Animator animation) { }
                @Override public void onAnimationRepeat(Animator animation) { }
            }).start();
        }
    }

    public void showInfo(String info) {
        Log.d(TAG, "showInfo: " + info);

        scrim.animate().cancel();
        scrim.removeCallbacks(hideInfo);
        scrim.animate().setListener(null);

        scrim.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
        text.setText(info);
        scrim.setAlpha(1f);
        scrim.postDelayed(hideInfo, 800);
    }

    private Runnable hideInfo = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "run: hideInfo");

            scrim.animate().cancel();
            scrim.animate().alpha(0f).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    scrim.setVisibility(View.INVISIBLE);
                }

                @Override public void onAnimationStart(Animator animation) { }
                @Override public void onAnimationCancel(Animator animation) { }
                @Override public void onAnimationRepeat(Animator animation) { }
            }).start();
        }
    };

}
