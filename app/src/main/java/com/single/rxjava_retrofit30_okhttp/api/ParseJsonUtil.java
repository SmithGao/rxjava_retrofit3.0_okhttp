package com.single.rxjava_retrofit30_okhttp.api;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

/**
 * 作者：Donn on 16/5/27 16:19
 * 作用：解析json
 */
public class ParseJsonUtil {
    private static Gson gson;

    public static <T> Object jsonStrToBean(String jsonStr, Class<T> obj) {
        T t = null;
        //设置bean可以不完全接收返回来的json数据
        ApiManager.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            t= ApiManager.objectMapper.readValue(jsonStr, obj);
            return t;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return t;
    }

    public static <T> ArrayList jsonStrToListBean(String jsonStr,  Class<T> obj) {
        ArrayList<T> t = null;
        //设置bean可以不完全接收返回来的json
        ApiManager.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JavaType javaType = ApiManager.objectMapper.getTypeFactory().constructParametricType(
                ArrayList.class, obj);
        try {
            t= ApiManager.objectMapper.readValue(jsonStr, javaType);
            return t;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return t;
    }

}
