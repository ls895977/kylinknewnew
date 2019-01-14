package com.maoyongxin.myapplication.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.maoyongxin.myapplication.R;

public class ItemFragment extends Fragment {

    private ImageView iv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item, null);
        iv = (ImageView) view.findViewById(R.id.iv_item);

        if (getArguments()!= null)
        {
            Bundle bundle = getArguments();
            String url = bundle.getString("key");
            Glide.with(getContext()).load(url).into(iv);
        }
        return view;
    }

}
