package com.maoyongxin.myapplication.http.second;

import android.content.Context;
import android.os.Handler;

import com.maoyongxin.myapplication.entity.RequestData;
import com.maoyongxin.myapplication.tool.CompressPhotoUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by maoyongxin on 2017/12/14.
 */

public class HttpNetWorkUtils {
    public static final int UPLOAD_MYPUBLISH = 210;//上传公告
    public static final int UPLOAD_DONGTAI = 220;//发布动态
    public static final int UPLOAD_DONGTALVIDEO = 330;
    public static final int UPLOAD_MYHEAD = 230;//上传头像
    public static final int CREATE_COMMUNITY = 240;//申请创建社区
    public static final int UPDATE_COMMUNITYICON = 260;//修改社区图片


    /**
     * @param context
     * @param handler
     */
    public static void upLoadMyPublish(final Context context, final String noticeTitle, final String noticeType, final String userId, final String areacode, final String content, final String businessType, final String thingTypeId, List<File> files, final Handler handler) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            list.add(files.get(i).getPath());
        }
        new CompressPhotoUtils().CompressPhoto(context, list, new CompressPhotoUtils.CompressCallBack() {
            @Override
            public void success(List<String> list) {
                List<File> files = new ArrayList<File>();
                for (int i = 0; i < list.size(); i++) {
                    File file = new File(list.get(i));
                    files.add(file);
                }
                Map<String, String> dataMap = new HashMap<String, String>();
                dataMap.put("noticeTitle", noticeTitle);
                dataMap.put("noticeType", noticeType);
                dataMap.put("userId", userId);
                dataMap.put("areaCode", areacode);
                dataMap.put("content", content);
                dataMap.put("businessType", businessType);
                dataMap.put("thingTypeId", thingTypeId);
                RequestData data = new RequestData();
                data.setUri(HttpContants.UPLOAD_MYPUBLISH);
                data.setDataMap(dataMap);
                Map<String, List<File>> fileMap = new HashMap<>();
                fileMap.put("file", files);
                data.setFileMap(fileMap);
                HttpUtils.doUploadImage(context, data, handler, UPLOAD_MYPUBLISH, true);
            }
        });
    }

    /**
     * @param context
     * @param handler
     */
    public static void uploadDongtai(final Context context, final String userId, final String dynamicContent, List<File> files, final Handler handler) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            list.add(files.get(i).getPath());
        }
        new CompressPhotoUtils().CompressPhoto(context, list, new CompressPhotoUtils.CompressCallBack() {
            @Override
            public void success(List<String> list) {
                List<File> files = new ArrayList<File>();
                for (int i = 0; i < list.size(); i++) {
                    File file = new File(list.get(i));
                    files.add(file);
                }
                Map<String, String> dataMap = new HashMap<String, String>();
                dataMap.put("userId", userId);
                dataMap.put("dynamicContent", dynamicContent);
                RequestData data = new RequestData();
                data.setUri(HttpContants.UPLOAD_DONGTAI);
                data.setDataMap(dataMap);
                Map<String, List<File>> fileMap = new HashMap<>();
                fileMap.put("file", files);
                data.setFileMap(fileMap);
                HttpUtils.doUploadImage(context, data, handler, UPLOAD_DONGTAI, true);
            }
        });

    }


    /**
     * @param context
     * @param handler
     */
    public static void uploadDongtaiVideo(final Context context, final String userId, final String dynamicContent, List<File> files, final Handler handler) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            list.add(files.get(i).getPath());
        }
        new CompressPhotoUtils().CompressPhoto(context, list, new CompressPhotoUtils.CompressCallBack() {
            @Override
            public void success(List<String> list) {
                List<File> files = new ArrayList<File>();
                for (int i = 0; i < list.size(); i++) {
                    File file = new File(list.get(i));
                    files.add(file);
                }
                Map<String, String> dataMap = new HashMap<String, String>();
                dataMap.put("userId", userId);
                dataMap.put("dynamicContent", dynamicContent);
                RequestData data = new RequestData();
                data.setUri(HttpContants.UPLOAD_DONGTAI);
                data.setDataMap(dataMap);
                Map<String, List<File>> fileMap = new HashMap<>();
                fileMap.put("file", files);
                data.setFileMap(fileMap);
                HttpUtils.doUploadImage(context, data, handler, UPLOAD_DONGTALVIDEO, true);
            }
        });

    }


    public static void uploadHeadImg(final Context context, final String userId, List<File> files, final Handler handler) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            list.add(files.get(i).getPath());
        }
        new CompressPhotoUtils().CompressPhoto(context, list, new CompressPhotoUtils.CompressCallBack() {
            @Override
            public void success(List<String> list) {
                List<File> files = new ArrayList<File>();
                for (int i = 0; i < list.size(); i++) {
                    File file = new File(list.get(i));
                    files.add(file);
                }
                Map<String, String> dataMap = new HashMap<String, String>();
                dataMap.put("userId", userId);
                RequestData data = new RequestData();
                data.setUri(HttpContants.UPLOAD_HEADIMG);
                data.setDataMap(dataMap);
                Map<String, List<File>> fileMap = new HashMap<>();
                fileMap.put("file", files);
                data.setFileMap(fileMap);
                HttpUtils.doUploadImage(context, data, handler, UPLOAD_MYHEAD, true);
            }
        });
    }
    public static void uploadCommunityImg(final Context context, final String userId,final String communityId, List<File> files, final Handler handler) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            list.add(files.get(i).getPath());
        }
        new CompressPhotoUtils().CompressPhoto(context, list, new CompressPhotoUtils.CompressCallBack() {
            @Override
            public void success(List<String> list) {
                List<File> files = new ArrayList<File>();
                for (int i = 0; i < list.size(); i++) {
                    File file = new File(list.get(i));
                    files.add(file);
                }
                Map<String, String> dataMap = new HashMap<String, String>();
                dataMap.put("userId", userId);
                dataMap.put("communityId", communityId);
                RequestData data = new RequestData();
                data.setUri(HttpContants.CHANGE_COMMUNITY_ICON);
                data.setDataMap(dataMap);
                Map<String, List<File>> fileMap = new HashMap<>();
                fileMap.put("file", files);
                data.setFileMap(fileMap);
                HttpUtils.doUploadImage(context, data, handler, UPDATE_COMMUNITYICON, true);
            }
        });
    }
    public static void uploadCreateCommunity(final Context context, final String communityName,final String communityNote,final String areaCode,
                                             final String address,final String addressName,
                                             final String longitude,final String latitude,final String userId,List<File> files, final Handler handler) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            list.add(files.get(i).getPath());
        }
        new CompressPhotoUtils().CompressPhoto(context, list, new CompressPhotoUtils.CompressCallBack() {
            @Override
            public void success(List<String> list) {
                List<File> files = new ArrayList<File>();
                for (int i = 0; i < list.size(); i++) {
                    File file = new File(list.get(i));
                    files.add(file);
                }
                Map<String, String> dataMap = new HashMap<String, String>();
                dataMap.put("communityName", communityName);
                dataMap.put("communityNote", communityNote);
                dataMap.put("areaCode", areaCode);
                dataMap.put("address", address);
                dataMap.put("addressName", addressName);
                dataMap.put("longitude", longitude);
                dataMap.put("latitude", latitude);
                dataMap.put("userId", userId);
                RequestData data = new RequestData();
                data.setUri(HttpContants.CREATE_COMMUNITY);
                data.setDataMap(dataMap);
                Map<String, List<File>> fileMap = new HashMap<>();
                fileMap.put("file", files);
                data.setFileMap(fileMap);
                HttpUtils.doUploadImage(context, data, handler, CREATE_COMMUNITY, true);
            }



        });
    }
}
