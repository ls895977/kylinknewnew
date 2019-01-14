package com.maoyongxin.myapplication.entity;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * http请求数据封装
 * Created by admin on 2017/3/2.
 */

public class RequestData {
    public String uri; //请求地址
    public Map<String,String> dataMap;//请求的数据
    public Map<String,List<File>> fileMap;//提交的文件

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Map<String, String> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<String, String> dataMap) {
        this.dataMap = dataMap;
    }

    public Map<String, List<File>> getFileMap() {
        return fileMap;
    }

    public void setFileMap(Map<String, List<File>> fileMap) {
        this.fileMap = fileMap;
    }
}
