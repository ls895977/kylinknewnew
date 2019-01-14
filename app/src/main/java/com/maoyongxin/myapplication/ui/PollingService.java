package com.maoyongxin.myapplication.ui;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;
import okhttp3.Call;

public class PollingService extends Service {
    public static final String ACTION = "com.maoyongxin.myapplication.ui.PollingService";

    private Notification mNotification;
    private NotificationManager mManager;
    public static final String EVNET_TAG = "EventService";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new PollingThread().start();
        return super.onStartCommand(intent, flags, startId);


    }

    class PollingThread extends Thread {
        @Override
        public void run() {

            try {
                Thread.sleep(1000 * 5);
                String uid = "";
                if (AppApplication.getCurrentUserInfo() == null || AppApplication.getCurrentUserInfo().getUserId() == null) {
                    return;
                }
                uid = AppApplication.getCurrentUserInfo().getUserId();
                if (!uid.equals("")) {
                    Getrepeat();
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("broad", "no,userId");
            }


        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void Getrepeat() {

        String api_adress = "http://st.3dgogo.com/index/msg_alert/getMsgAlertCountApi.html";

        OkHttpUtils.get().url(api_adress)
                .addParams("uid1", AppApplication.getCurrentUserInfo().getUserId() + "")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {

                try {
                    JSONObject data = new JSONObject(response);


                    if (data.getInt("code") == 200) {
                       // Log.e("broad", "老铁，您有" + data.getString("count") + "条未读信息");
                        EventBus.getDefault().post(new EventMessage<String>(2, data.getString("count")));
                    } else if (data.getInt("code") == 500) {
                       // Log.i("broad", "抱歉，没有新的回复");
                        EventBus.getDefault().post(new EventMessage<String>(1, ""));
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
