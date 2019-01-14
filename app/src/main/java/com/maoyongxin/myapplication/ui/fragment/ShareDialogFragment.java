package com.maoyongxin.myapplication.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.ui.JiGuangSharePlatformModel;

import java.util.ArrayList;
import java.util.List;

public class ShareDialogFragment extends BottomSheetDialogFragment {

    private static final String ARG_ITEM_COUNT = "item_count";
    private Listener mListener;

    private List<JiGuangSharePlatformModel> list;

    public static ShareDialogFragment newInstance(ArrayList<JiGuangSharePlatformModel> list) {
        final ShareDialogFragment fragment = new ShareDialogFragment();
        final Bundle args = new Bundle();
        args.putParcelableArrayList("list", list);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            list = getArguments().getParcelableArrayList("list");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shareplatform_list_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setAdapter(new SharePlatformAdapter(list));
        view.findViewById(R.id.iv_dialog_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        final Fragment parent = getParentFragment();
        if (parent != null) {
            mListener = (Listener) parent;
        } else {
            mListener = (Listener) context;
        }
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    public interface Listener {
        void onSharePlatformClicked(int position);
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;
        final TextView text;

        ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.fragment_shareplatform_list_dialog_item, parent, false));
            text = (TextView) itemView.findViewById(R.id.tv_platform);
            imageView = (ImageView) itemView.findViewById(R.id.iv_platform);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onSharePlatformClicked(getAdapterPosition());
                        dismiss();
                    }
                }
            });
        }

    }

    private class SharePlatformAdapter extends RecyclerView.Adapter<ViewHolder> {

        private List<JiGuangSharePlatformModel> list;

        public SharePlatformAdapter(List<JiGuangSharePlatformModel> list) {
            this.list = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.text.setText(list.get(position).getPlatformName());
            holder.imageView.setImageResource(list.get(position).getDrawableID());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

    }

}
