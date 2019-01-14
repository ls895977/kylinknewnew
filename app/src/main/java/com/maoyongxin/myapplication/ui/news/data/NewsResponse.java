package com.maoyongxin.myapplication.ui.news.data;

import java.util.List;
import java.util.Map;


public class NewsResponse {
    public boolean error;
    public Map<String, List<NewsItem>> results;
    public List<String> category;

    public boolean hasData() {
        return results != null && results.size() > 0;
    }
}
