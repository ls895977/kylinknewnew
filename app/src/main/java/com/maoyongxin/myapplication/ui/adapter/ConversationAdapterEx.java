package com.maoyongxin.myapplication.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import io.rong.imkit.model.UIConversation;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.adapter.ConversationListAdapter;
import io.rong.imkit.widget.adapter.MessageListAdapter;
import io.rong.imlib.model.Conversation;

/**
 * Created by weiqinxiao on 15/11/5.
 */
public class ConversationAdapterEx extends MessageListAdapter {
    public ConversationAdapterEx(Context context) {
        super(context);
    }

    @Override
    protected void bindView(View v, int position, UIMessage data) {
        super.bindView(v, position, data);
    }
}
