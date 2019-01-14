package com.maoyongxin.myapplication.ui;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;

import java.io.File;

public class PackageUtils {

	/**
	 * 获得版本名称
	 * 
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context) {
		PackageManager pm = context.getPackageManager();
		try {
			// 获得清单文件的对象
			PackageInfo packageInfo = pm.getPackageInfo(
					context.getPackageName(), 0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 版本号
	 * 
	 * @param context
	 * @return
	 */
	public static int getVersionCode(Context context) {
		PackageManager pm = context.getPackageManager();
		try {
			// 获得清单文件的对象
			PackageInfo packageInfo = pm.getPackageInfo(
					context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static String getAppName(Context context, PackageInfo info) {
		PackageManager pm = context.getPackageManager();
		ApplicationInfo applicationInfo = info.applicationInfo;
		return applicationInfo.loadLabel(pm).toString();
	}

	public static Drawable getAppIcon(Context context, PackageInfo info) {
		PackageManager pm = context.getPackageManager();
		ApplicationInfo applicationInfo = info.applicationInfo;
		return applicationInfo.loadIcon(pm);
	}

	public static long getAppSpace(PackageInfo info) {
		ApplicationInfo applicationInfo = info.applicationInfo;

		// applicationInfo.dataDir
		String sourceDir = applicationInfo.sourceDir;
		File file = new File(sourceDir);
		return file.length();
	}

	public static boolean isInstallSD(PackageInfo info) {
		ApplicationInfo applicationInfo = info.applicationInfo;
		int flags = applicationInfo.flags;// 应用的标记，能力或是特性

		boolean isInsallSD = false;
		if ((flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == ApplicationInfo.FLAG_EXTERNAL_STORAGE) {
			isInsallSD = true;
		}
		return isInsallSD;
	}
	
	public static boolean isSystemApp(PackageInfo info) {
		ApplicationInfo applicationInfo = info.applicationInfo;
		int flags = applicationInfo.flags;// 应用的标记，能力或是特性

		boolean isSystemApp = false;
		if ((flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
			isSystemApp = true;
		}
		return isSystemApp;
	}
}
