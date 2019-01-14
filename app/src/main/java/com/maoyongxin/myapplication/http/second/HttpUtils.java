package com.maoyongxin.myapplication.http.second;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.maoyongxin.myapplication.entity.RequestData;
import com.maoyongxin.myapplication.http.second.okhttp.OkHttpUtils;
import com.maoyongxin.myapplication.http.second.okhttp.listener.impl.UIProgressListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by maoyongxin on 2017/12/14.
 */

public class HttpUtils {
    public static final int ERROR = 404;
    public static final int TOKENFAIL = 444;//token验证失败
    public static final int GETRSAFAIL = 445;//判断是否有服务器key

    /**
     * post请求 提交键值对
     *
     * @param data
     * @param handler
     * @param tag
     * @param show    显示加载
     */
    public static void doPost(Context context, RequestData data, final Handler handler, final int tag, final boolean show) {
        System.out.print(data.getUri());
        OkHttpUtils.getInstance().doPost(data.getUri(), data.getDataMap(), new OkHttpUtils.OnOkHttpCallBackListener() {
            @Override
            public void onFail() {
                Log.v("网络请求", "连接失败");
                Message msg = new Message();
                msg.what = ERROR;
                msg.arg1 = tag;
                msg.obj = null;
                handler.sendMessage(msg);
            }

            @Override
            public void onSuccess(String response) {
                Log.v("网络请求", response);

                Message msg = new Message();
                msg.what = tag;
                msg.arg1 = tag;
                msg.obj = response;
                handler.sendMessage(msg);
            }
        });
    }

    /**
     * post请求 提交json
     *
     * @param json
     * @param handler
     * @param tag
     * @param show    显示加载
     */
    public static void doPost(Context context, String url, String json, final Handler handler, final int tag, final boolean show) {

        OkHttpUtils.getInstance().doPost(url, json, new OkHttpUtils.OnOkHttpCallBackListener() {
            @Override
            public void onFail() {
                Log.v("网络请求", "连接失败");

                Message msg = new Message();
                msg.what = ERROR;
                msg.arg1 = tag;
                msg.obj = null;
                handler.sendMessage(msg);
            }

            @Override
            public void onSuccess(String response) {
                Log.v("网络请求", response);

                Message msg = new Message();
                msg.what = tag;
                msg.arg1 = tag;
                msg.obj = response;
                handler.sendMessage(msg);
            }
        });
    }

    public static void doUploadImage(Context context, final RequestData data, final Handler handler, final int tag, final boolean show) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("aa", "--------getUri-----" + data.getUri() + "-------getDataMap------" + data.getDataMap() + "------getFileMap-----" + data.getFileMap().get("file"));
                OkHttpUtils.getInstance().uploadFile(data.getUri(), data.getDataMap(), data.getFileMap().get("file"), new UIProgressListener() {
                    @Override
                    public void onUIProgress(long currentBytes, long contentLength, boolean done) {
                        Log.e("sssssssssssssss",currentBytes+"========="+contentLength);
                        Message message = new Message();
                        message.what=0x333;
                        message.arg1=tag;

                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("allCount",contentLength);
                            jsonObject.put("size",currentBytes);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        message.obj = jsonObject.toString();
                        handler.sendMessage(message);
                    }
                }, new OkHttpUtils.OnOkHttpCallBackListener() {


                    @Override
                    public void onFail() {
                        Log.e("aa", "--------onFail-----");
                        Message message = new Message();
                        message.what = ERROR;
                        message.arg1 = tag;
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onSuccess(String response) {
                        Log.e("aa", "--------onSuccess-----" + response);
                        Message message = new Message();
                        message.what = tag;
                        message.arg1 = tag;
                        message.obj = response;
                        handler.sendMessage(message);
                    }
                });
            }
        }).start();

    }
}
