package com.jky.baselibrary.util.common;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

public class GsonUtil {
    private static Gson sGson;

    static {
        sGson = new Gson();
    }

    public static <T> T json2Entity(String json, Class<T> clazz) {
        return sGson.fromJson(json, clazz);
    }

    public static Map jsonObj2Map(String json) {
        return sGson.fromJson(json, new TypeToken<Map>() {
        }.getType());
    }

    public static List jsonArr2List(String json) {
        return sGson.fromJson(json, new TypeToken<List>() {
        }.getType());
    }

    public static String entity2Json(Object entity) {
        return sGson.toJson(entity);
    }

    public static String list2Json(List list) {
        return sGson.toJson(list);
    }

    public static String map2Json(Map map) {
        return sGson.toJson(map);
    }
}
