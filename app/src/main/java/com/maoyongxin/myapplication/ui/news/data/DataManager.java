package com.maoyongxin.myapplication.ui.news.data;

import android.app.AlertDialog;
import android.util.Log;

import com.google.gson.Gson;
import com.maoyongxin.myapplication.ui.news.Inject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;

public class DataManager {
    private static final String TAG = "DataManager";

    private static volatile DataManager sInstance;
    public static DataManager getInstance() {
        if (sInstance == null) {
            synchronized (DataManager.class) {
                if (sInstance == null) {
                    sInstance = new DataManager();
                }
            }
        }

        return sInstance;
    }




    /**
     * 加载更多内容
     * 在分页时 loadMore 从上次加载到的位置继续
     * 第一次调用从数据库缓存中最新时间开始加载
     * 如果返回的数据为空，意味着已经没有更多内容
     *
     * @param lastLoaded 上次加载到的日期，用于分页加载
     * @return Observable
     */
    public Observable<List<NewsItem>> loadMore(String lastLoaded) {
        String day;

        return loadFromDay(null);
    }

    /**
     * 从 day 开始进行加载任务，发现新的内容后停止
     * 如果没有内容，加载前一天的内容,如果返回的数据为空，意味着日期以前已经没有更多内容
     *
     * @param day 加载日期
     */
    private Observable<List<NewsItem>> loadFromDay(final String day) {
        Log.d(TAG, "loadFromDay: " + day);
        if (isOver(day)) {
            List<NewsItem> emptyList = new ArrayList<>();
            return Observable.just(emptyList);
        } else return load(day).flatMap(new Func1<List<NewsItem>, Observable<List<NewsItem>>>() {
            @Override
            public Observable<List<NewsItem>> call(List<NewsItem> NewsItemList) {
                // 检查是否获得了数据
                if (NewsItemList.size() == 0) {
                    String dayBack = dayBack(day);
                    return loadFromDay(dayBack);
                } else {
                    return Observable.just(NewsItemList);
                }
            }
        });
    }


    /**
     * 从当前时间加载数据
     * 调用者应通过返回数据中的时间判断是否有更新的内容
     *
     * @return Observable
     */
    public Observable<List<NewsItem>> loadNew() {
        return loadFromDay(getToday());
    }

    /**
     * 加载某一天的数据，首先检查数据库，否则从网络中加载并更新数据库
     *
     * @param dayStr 日期
     * @return Observable
     */
    private Observable<List<NewsItem>> load(final String dayStr) {
        Log.d(TAG, "load: " + dayStr);

        String content = readFile("news.json");
        Gson gson = new Gson();
        NewsResponse response = gson.fromJson(content, NewsResponse.class);
        List<NewsItem> NewsItemList = new ArrayList<>();
        if (response.hasData()) {
            for (Map.Entry<String, List<NewsItem>> entry : response.results.entrySet()) {
                NewsItemList.addAll(entry.getValue());
            }
        }
        for (NewsItem NewsItem : NewsItemList) {
            NewsItem.day = dayStr;
        }
        return Observable.just(NewsItemList);


//        History history = checkLoadHistory(dayStr);
//        if (history == null) {
//
//            return loadFromGank(dayStr).map(new Func1<List<NewsItem>, List<NewsItem>>() {
//                @Override
//                public List<NewsItem> call(List<NewsItem> NewsItemList) {
//                    updateDb(NewsItemList, dayStr);
//                    return NewsItemList;
//                }
//            });
//        } else {
//            return loadFormDb(dayStr);
//        }
    }

    private String readFile(String filename) {
        StringBuilder sb = new StringBuilder();

        try {
            InputStream json = Inject.provideContext().getAssets().open(filename);
            BufferedReader in = new BufferedReader(new InputStreamReader(json, "UTF-8"));

            String str;
            while ((str = in.readLine()) != null) {
                sb.append(str);
            }

            in.close();
        } catch (Exception e) {
            new AlertDialog.Builder(Inject.provideContext())
                    .setTitle("Error")
                    .setMessage(
                            "The JSON file was not able to load properly. These tests won't work until you completely kill this demo app and restart it.")
                    .setPositiveButton("OK", null)
                    .show();
        }

        return sb.toString();
    }

    private static boolean isToday(String dayStr) {
        int[] dayInt = dayStr2Int(dayStr);

        return isToday(dayInt[0], dayInt[1], dayInt[2]);
    }

    private static boolean isToday(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        int yearToday = calendar.get(Calendar.YEAR);
        int monthToday = calendar.get(Calendar.MONTH) + 1;
        int dayToday = calendar.get(Calendar.DAY_OF_MONTH);

        return year == yearToday && monthToday == month && dayToday == day;
    }

    private static int[] dayStr2Int(String dayStr) {
        String[] parts = dayStr.split("/");
        int year = Integer.valueOf(parts[0]);
        int month = Integer.valueOf(parts[1]);
        int day = Integer.valueOf(parts[2]);

        return new int[]{year, month, day};
    }

    private static String dayInt2Str(int year, int month, int day) {
        return year + "/" + month + "/" + day;
    }

    private static final long A_DAY = 24 * 60 * 60 * 1000;

    /**
     * 获取前一天的日期字符串, 例如输入 2016/12/16
     *
     * @return 前一天的时间 2016/12/15
     */
    private static String dayBack(String dayStr) {
        int[] dayInts = dayStr2Int(dayStr);
        Calendar calendar = Calendar.getInstance();
        calendar.set(dayInts[0], dayInts[1] - 1, dayInts[2]);
        Date date = calendar.getTime();
        long time = date.getTime() - A_DAY;
        date.setTime(time);
        calendar.setTime(date);

        return dayInt2Str(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
    }

    private static String dayNext(String dayStr) {
        int[] dayInts = dayStr2Int(dayStr);
        Calendar calendar = Calendar.getInstance();
        calendar.set(dayInts[0], dayInts[1] - 1, dayInts[2]);
        Date date = calendar.getTime();
        long time = date.getTime() + A_DAY;
        date.setTime(time);
        calendar.setTime(date);

        return dayInt2Str(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
    }

    private static String getToday() {
        Calendar calendar = Calendar.getInstance();
        return dayInt2Str(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * gank.io 最早的时间应该是 2015/05/18,
     *
     * @param dayStr 日期
     * @return 是否超过了 gank 的初始时间
     */
    private static boolean isOver(String dayStr) {
        int[] day = dayStr2Int(dayStr);
        Calendar gankStart = Calendar.getInstance();
        gankStart.set(2015, 4, 18);
        Calendar checkTime = Calendar.getInstance();
        checkTime.set(day[0], day[1] - 1, day[2]);

        return checkTime.before(gankStart);
    }
}
