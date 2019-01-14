package com.jky.baselibrary.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;

/**
 * BaseBroadcastReceiver for Dynamic Register
 */
public abstract class BaseReceiver extends BroadcastReceiver {

    protected Context mContext;

    public BaseReceiver(Context context) {
        mContext = context;
    }

    public void register(String... actions) {
        IntentFilter intentFilter = new IntentFilter();
        if (actions != null)
            for (String action : actions)
                intentFilter.addAction(action);
        mContext.registerReceiver(this, intentFilter);
    }

    public void unregister() {
        mContext.unregisterReceiver(this);
    }

}
