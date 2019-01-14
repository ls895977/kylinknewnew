package com.maoyongxin.myapplication.ui.editapp.findfragment.zhihu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.ui.editapp.findfragment.zhihu.bean.ZhiHuGridBean;
import com.maoyongxin.myapplication.ui.editapp.findfragment.zhihu.dialog.Dialog_ZhiHuButoom;

import java.util.List;

public class GrdivewAdater extends BaseAdapter {
    private List<ZhiHuGridBean.InfoBean> mDrawableList;
    private LayoutInflater mInflater;
    private Context mContext;

    public GrdivewAdater(Context context, List<ZhiHuGridBean.InfoBean> drawableList) {
        mDrawableList = drawableList;
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return mDrawableList.size();
    }

    public Object getItem(int position) {
        return mDrawableList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ItemViewTag viewTag;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_zhihutitlegrid, null);
            viewTag = new ItemViewTag(convertView);
            convertView.setTag(viewTag);
        } else {
            viewTag = (ItemViewTag) convertView.getTag();
        }
        ZhiHuGridBean.InfoBean item = mDrawableList.get(position);
        viewTag.mName.setText(item.getName());
        viewTag.mName.setSelected(item.isIsof());
        return convertView;
    }

    class ItemViewTag {
        protected TextView mName;

        public ItemViewTag(View view) {
            mName = view.findViewById(R.id.item_zhihugridviewname);
        }
    }

}