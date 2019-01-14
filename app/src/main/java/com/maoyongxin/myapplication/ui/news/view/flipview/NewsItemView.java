package com.maoyongxin.myapplication.ui.news.view.flipview;


import com.maoyongxin.myapplication.ui.news.data.NewsItem;

public interface NewsItemView {
    void bind(NewsItem gankItem, Listener listener);

    interface Listener {
        void open(NewsItem gankItem);
        void showBottomSheet(NewsItem gankItem);
        void like(NewsItem gankItem);
    }
}