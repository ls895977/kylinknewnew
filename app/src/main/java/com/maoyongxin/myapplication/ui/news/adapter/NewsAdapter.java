package com.maoyongxin.myapplication.ui.news.adapter;

import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.ui.news.data.NewsItem;
import com.maoyongxin.myapplication.ui.news.view.flipview.NewsItemView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class NewsAdapter extends RecyclerView.Adapter implements NewsItemView.Listener {

    private static final String TAG = "GankAdapter";

    private List<Page> pageList = new ArrayList<>();
    private List<NewsItem> gankItems = new ArrayList<>();

    private Listener mListener;
    private Context mContext;
    private boolean mHasMore = true;

    public NewsAdapter(Context context, Listener listener) {
        mListener = listener;
        mContext = context;
    }

    public void setHasMore(boolean hasMore) {
        if (mHasMore == hasMore) {
            return;
        }
        mHasMore = hasMore;
        notifyItemChanged(getItemCount() - 1);
    }

    public void clear() {
        int size = pageList.size();
        pageList.clear();
        gankItems.clear();
        notifyItemRangeRemoved(0, size);
    }

    public void addData(List<NewsItem> gankItemList) {
        gankItemList.addAll(gankItems);
        gankItems.clear();

        FirstPage bigImagePage = FirstPage.gen(gankItemList);
        if (bigImagePage != null) {
            pageList.add(bigImagePage);
            notifyItemInserted(pageList.size() - 1);
        }

        while (gankItemList.size() / 4 != 0) {
            NormalPage normal = NormalPage.gen(gankItemList);
            pageList.add(normal);
            notifyItemInserted(pageList.size() - 1);
        }

        if (gankItemList.size() != 0) {
            gankItems.addAll(gankItemList);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "create view holder " + viewType);

        if (viewType == Page.TYPE_BIG_IMAGE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.page_first, parent, false);
            return new FirstPageVh(view);
        }

        if (viewType == Page.TYPE_NORMAL) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.page_normal, parent, false);
            return new NormalVh(view);
        }

        if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.page_loading, parent, false);
            return new LoadingVh(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);

        if (type == Page.TYPE_BIG_IMAGE) {
            FirstPageVh imageVh = (FirstPageVh) holder;
            imageVh.bind(pageList.get(position));
        }

        if (type == Page.TYPE_NORMAL) {
            NormalVh normalVh = (NormalVh) holder;
            normalVh.bind(pageList.get(position));
        }

        if (type == 0) {
            LoadingVh loadingVh = (LoadingVh) holder;
            ((LoadingVh) holder).bind(mHasMore);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position < pageList.size()) {
            return pageList.get(position).getType();
        } else {
            return 0;
        }
    }

    @Override
    public int getItemCount() {
        return pageList.size() + 1;
    }

    public int getDataCount() {
        return pageList.size();
    }

    @Override
    public void open(NewsItem gankItem) {
        //todo
    }

    @Override
    public void showBottomSheet(NewsItem gankItem) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext);
        NewsItemView gankItemView = (NewsItemView) LayoutInflater.from(mContext).inflate(R.layout.bottom_sheet, null, false);
        gankItemView.bind(gankItem, NewsAdapter.this);
        bottomSheetDialog.setContentView((View) gankItemView);
        bottomSheetDialog.show();
    }

    @Override
    public void like(NewsItem gankItem) {
        if (gankItem.like) {
            mListener.showInfo("已喜欢");
        } else {
            mListener.showInfo("取消喜欢");
        }

        //todo 不喜欢/喜欢 之后的逻辑处理
    }

    class LoadingVh extends RecyclerView.ViewHolder {

        @BindView(R.id.loading)
        ProgressBar loading;
        @BindView(R.id.text)
        TextView textView;

        LoadingVh(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(boolean hasMore) {
            if (hasMore) {
                loading.setVisibility(View.VISIBLE);
                textView.setText("加载中...");
            } else {
                loading.setVisibility(View.GONE);
                textView.setText("没有更多内容");
            }
        }
    }

    class FirstPageVh extends RecyclerView.ViewHolder {

        @BindViews({R.id.item_head, R.id.item1})
        NewsItemView[] items;

        FirstPageVh(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(Page page) {
            for (int i = 0; i < page.getSize() && i < items.length; i++) {
                items[i].bind(page.items[i], NewsAdapter.this);
            }
        }
    }

    class NormalVh extends RecyclerView.ViewHolder {
        @BindViews({R.id.item1, R.id.item2, R.id.item3, R.id.item4})
        NewsItemView items[];

        NormalVh(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(Page page) {
            for (int i = 0; i < page.getSize() && i < items.length; i++) {
                items[i].bind(page.items[i], NewsAdapter.this);
            }
        }
    }

    private abstract static class Page {
        static final int TYPE_BIG_IMAGE = 1;
        static final int TYPE_NORMAL = 2;

        NewsItem[] items;

        abstract int getType();

        abstract int getSize();
    }

    /**
     * 通常为某天信息的第一页
     * 样式为上方一个大图片，下方一个内容
     */
    private static class FirstPage extends Page {
        static FirstPage gen(List<NewsItem> gankItemList) {
            NewsItem imageItem = null;

            if (gankItemList == null || gankItemList.size() <= 1) {
                return null;
            }

            Iterator<NewsItem> gankItemIterator = gankItemList.iterator();
            while (gankItemIterator.hasNext()) {
                NewsItem item = gankItemIterator.next();
                if (item.type.equals("重点新闻")) {
                    imageItem = item;
                    gankItemIterator.remove();
                    break;
                }
            }

            if (imageItem == null) {
                return null;
            }

            int count = 2;
            int i = 1;
            NewsItem[] items = new NewsItem[count];
            items[0] = imageItem;

            gankItemIterator = gankItemList.iterator();
            while (gankItemIterator.hasNext() && count > i) {
                NewsItem item = gankItemIterator.next();
                items[i] = item;
                i++;
                gankItemIterator.remove();
            }

            FirstPage bigImagePage = new FirstPage();
            bigImagePage.items = items;
            return bigImagePage;
        }

        @Override
        int getSize() {
            return items == null ? 0 : items.length;
        }

        @Override
        int getType() {
            return TYPE_BIG_IMAGE;
        }
    }

    /**
     * 这是通常形式的 page
     * 样式为四个内容竖直排列
     */
    private static class NormalPage extends Page {
        static NormalPage gen(List<NewsItem> gankItemList) {
            if (gankItemList == null || gankItemList.isEmpty()) {
                return null;
            }

            int size = gankItemList.size() > 4 ? 4 : gankItemList.size();
            NewsItem[] items = new NewsItem[size];
            gankItemList.subList(0, size).toArray(items);
            gankItemList.removeAll(Arrays.asList(items));

            NormalPage normalPage = new NormalPage();
            normalPage.items = items;

            return normalPage;
        }

        int getSize() {
            return items == null ? 0 : items.length;
        }

        @Override
        int getType() {
            return TYPE_NORMAL;
        }
    }

    public interface Listener {
        void showInfo(String info);
    }
}
