package com.jky.baselibrary.base;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AlertDialog;

import com.holidaycheck.permissify.DialogText;
import com.holidaycheck.permissify.PermissifyConfig;
import com.jky.baselibrary.R;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public abstract class BaseApplication extends MultiDexApplication {
    private static Handler sHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        sHandler = new Handler(Looper.getMainLooper());
        initPermissify();
        appStart();
    }

    private void initPermissify() {
        PermissifyConfig permissifyConfig = new PermissifyConfig.Builder()
                .withDialogRationaleDialogFactory(new PermissifyConfig.AlertDialogFactory() {
                    @Override
                    public AlertDialog createDialog(Context context, String dialogMsg, DialogInterface.OnClickListener onClickListener) {
                        return new AlertDialog.Builder(context)
                                .setTitle(R.string.default_rationale_dialog_text)
                                .setMessage(dialogMsg)
                                .setPositiveButton(android.R.string.ok, onClickListener)
                                .create();
                    }
                })
                .withDefaultTextForPermissions(new HashMap<String, DialogText>() {{
                    put(Manifest.permission_group.LOCATION, new DialogText(R.string.location_rationale, R.string.location_deny_dialog));
                    put(Manifest.permission_group.CAMERA, new DialogText(R.string.camera_rationale, R.string.camera_deny_dialog));
                    put(Manifest.permission_group.STORAGE, new DialogText(R.string.storage_rationale, R.string.storage_deny_dialog));
                    put(Manifest.permission_group.MICROPHONE, new DialogText(R.string.mic_rationale, R.string.mic_deny_dialog));
                    put(Manifest.permission_group.PHONE, new DialogText(R.string.phone_rationale, R.string.phone_deny_dialog));
                    put(Manifest.permission_group.CONTACTS, new DialogText(R.string.contacts_rationale, R.string.contacts_deny_dialog));
                }})
                .build();

        PermissifyConfig.initDefault(permissifyConfig);
    }

    protected abstract void appStart();

    public static Handler getHandler() {
        return sHandler;
    }

    public String getApplicationTag() {
        return getClass().getSimpleName();
    }

    public String getAppName(int pid) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pid) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return processName;
    }
}
