package com.jky.baselibrary.util.common;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.jky.baselibrary.LibConfig;
import com.jky.baselibrary.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {

    public static int getAgeFromBirthMillis(long birthMillis) {
        Calendar birthday = Calendar.getInstance();
        birthday.setTimeInMillis(birthMillis);
        Calendar now = Calendar.getInstance();
        int age = now.get(Calendar.YEAR) - birthday.get(Calendar.YEAR);
        boolean fullAgeThisYear = now.get(Calendar.DAY_OF_YEAR) >= birthday.get(Calendar.DAY_OF_YEAR);
        return fullAgeThisYear ? age : age - 1;
    }

    /**
     * Since 1970-1-1
     */
    public static long getMillsFromYMD(int year, int month, int day) {
        long res = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            res = sdf.parse(year + "-" + month + "-" + day).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static String getFormatYMDFromMillis(long millis, @Nullable String format) {
        String pattern = format;
        if (TextUtils.isEmpty(format))
            pattern = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String res = sdf.format(new Date(millis));
        return res;
    }

    /**
     * 当前时间与给定时间比较，得出上一次时间是在多久以前
     *
     * @param lastMillis 上一次时间的毫秒数（比当前时间小才科学）
     * @return 上次时间与当前时间的间隔
     */
    public static String cmpLastMillisDesc(long lastMillis) {
//        if (lastMillis <= 0)
//            return "";
        String res;
        String unit;
        long now = System.currentTimeMillis();
        long interval = now - lastMillis;
        if (interval < 60 * 60 * 1000) {//分钟
            unit = LibConfig.getApplication().getString(R.string.x_minutes_ago);
            res = ((int) (interval / 1000 / 60)) + unit;
        } else if (interval < 24 * 60 * 60 * 1000) {//小时
            unit = LibConfig.getApplication().getString(R.string.x_hours_ago);
            res = ((int) (interval / 1000 / 60 / 60)) + unit;
        } else if (interval < 365L * 24 * 60 * 60 * 1000) {//天
            unit = LibConfig.getApplication().getString(R.string.x_days_ago);
            res = ((int) (interval / 1000 / 60 / 60 / 24)) + unit;
        } else {
            unit = LibConfig.getApplication().getString(R.string.x_years_ago);
            res = ((int) (interval / 1000 / 60 / 60 / 24 / 365)) + unit;
        }
        return res;
    }

    /**
     * 获得一个适当的上一次时间描述文字
     */
    public static String getLastMillisDesc(long lastMillis) {
        String res;
        Calendar calendarLast = Calendar.getInstance();
        calendarLast.setTimeInMillis(lastMillis);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        boolean isYesterday;
        boolean isToday;
        {
            int dayLast = calendarLast.get(Calendar.DAY_OF_YEAR);
            isToday = dayLast == calendar.get(Calendar.DAY_OF_YEAR);
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            isYesterday = dayLast == calendar.get(Calendar.DAY_OF_YEAR);
        }

        int month = calendarLast.get(Calendar.MONTH) + 1;
        int day = calendarLast.get(Calendar.DAY_OF_MONTH);
        int hour = calendarLast.get(Calendar.HOUR_OF_DAY);
        int minute = calendarLast.get(Calendar.MINUTE);
        if (isToday) {
            res = hour + ":" + (minute < 10 ? "0" + minute : minute);
        } else if (isYesterday) {
            res = LibConfig.getApplication().getString(R.string.yesterday) + " " + hour + ":" + (minute < 10 ? "0" + minute : minute);
        } else {
            res = month + LibConfig.getApplication().getString(R.string.month)
                    + day + LibConfig.getApplication().getString(R.string.day);
        }
        return res;
    }

    public static boolean isSameTime(long compareTime, long deviationSec) {
        long localTime = System.currentTimeMillis();
        long localSec = localTime / 1000;
        long serverSec = compareTime / 1000;
        long deviation = Math.abs(deviationSec);
        return Math.abs(localSec - serverSec) <= deviation;
    }

    /**
     * @param month 1-12
     * @return 0 白羊 3.21-4.19
     * 1 金牛 4.20-5.20
     * 2 双子 5.21-6.21
     * 3 巨蟹 6.22-7.22
     * 4 狮子 7.23-8.22
     * 5 处女 8.23-9.22
     * 6 天秤 9.23-10.23
     * 7 天蝎 10.24-11.22
     * 8 射手 11.23-12.21
     * 9 摩羯 12.22-1.19
     * 10 水瓶 1.20-2.18;
     * 11 双鱼 2.19-3.20;
     */

    public static int getConstellationFromMD(int month, int day) {
        int res = 9;
        int d = month * 100 + day;
        Logger.i("TimeUtil", "getConstellationFromMD()--> " + d);
        if (d >= 321 && d <= 419) {
            res = 0;
        } else if (d >= 420 && d <= 520) {
            res = 1;
        } else if (d >= 521 && d <= 621) {
            res = 2;
        } else if (d >= 622 && d <= 722) {
            res = 3;
        } else if (d >= 723 && d <= 822) {
            res = 4;
        } else if (d >= 823 && d <= 922) {
            res = 5;
        } else if (d >= 923 && d <= 1023) {
            res = 6;
        } else if (d >= 1024 && d <= 1122) {
            res = 7;
        } else if (d >= 1123 && d <= 1221) {
            res = 8;
        } else if (d >= 120 && d <= 218) {
            res = 10;
        } else if (d >= 219 && d <= 320) {
            res = 11;
        }
        return res;
    }

}
