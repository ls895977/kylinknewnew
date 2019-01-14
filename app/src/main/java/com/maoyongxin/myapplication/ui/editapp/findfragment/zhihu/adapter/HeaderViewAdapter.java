package com.maoyongxin.myapplication.ui.editapp.findfragment.zhihu.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.ui.editapp.findfragment.zhihu.bean.ZhiHuBean;

public class HeaderViewAdapter extends BaseQuickAdapter<ZhiHuBean.InfoBean.DataBean, BaseViewHolder> {
    private Context context;
    public HeaderViewAdapter(Context context1) {
        super(R.layout.itemview_zhihutitle);
        this.context=context1;
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, ZhiHuBean.InfoBean.DataBean item) {
        viewHolder.setText(R.id.name, item.getResource_name());
        viewHolder.setText(R.id.item_id, item.getIntro());
        Glide.with(context).load("http://st.3dgogo.com/"+item.getImg()).placeholder(R.mipmap.icon_x1).into((ImageView) viewHolder.getView(R.id.zhihutitl_hader));
    }
}