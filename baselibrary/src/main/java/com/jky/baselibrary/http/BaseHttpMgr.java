package com.jky.baselibrary.http;

import android.content.Context;

import com.jky.baselibrary.http.callback.HttpCommonCallback;
import com.jky.baselibrary.util.common.Logger;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public abstract class BaseHttpMgr {

    public static final String TAG = BaseHttpMgr.class.getSimpleName();
    public static final String GET = "GET";
    public static final String POST = "POST";

    private static final List<Request> sRequestList;

    static {
        sRequestList = new ArrayList<>();
    }

    public void stringRequest(Context context,Request request, HttpCommonCallback callback) {
        enqueue(request);
    }

    private void enqueue(Request request) {
        synchronized (sRequestList) {
            sRequestList.add(request);
        }
    }

    public void cancelTag(Object httpTag) {
        if (httpTag == null)
            return;
        Logger.i(TAG, "cancelTag: " + httpTag);
        synchronized (sRequestList) {
            for (int i = sRequestList.size() - 1; i >= 0; i--) {
                Request request = sRequestList.get(i);
                if (httpTag.equals(request.getTag())) {
                    sRequestList.remove(request);
                }
            }
        }
    }

    public void deleteRequest(Request request) {
        if (request == null)
            return;
        synchronized (sRequestList) {
            if (sRequestList.contains(request))
                sRequestList.remove(request);
        }
    }

    public boolean isCancelled(Request request) {
        boolean res;
        synchronized (sRequestList) {
            res = !sRequestList.contains(request);
        }
        return res;
    }
}
