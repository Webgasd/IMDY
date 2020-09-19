package com.example.upc.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JsonToImageUrl {
    /**
     * 将上传的照片JSON格式转换为String图片地址
     * @param jsonObj
     * @return imgUrl
     */
    public static String JSON2ImageUrl(Object jsonObj) {
        JSONArray jsonArray = JSONArray.fromObject(jsonObj);
        JSONObject jsonObject1 = JSONObject.fromObject(jsonArray.get(0));
        JSONObject jsonObject2 = JSONObject.fromObject(jsonObject1.get("response"));
        // 图片存储地址记得上传的时候更改IP
//        String host = "/Users/weixj/Desktop/wph/IMDY/upload/";
//        String host = "http://127.0.0.1:8080/upload/picture/";
        String host = "http://123.234.130.3:8080/upload/picture/";
        String imgUrl = host+ jsonObject2.get("data");
        return imgUrl;
    }

    public static String JSON2ImageUrl2(Object jsonObj) {
        JSONArray jsonArray = JSONArray.fromObject(jsonObj);
        JSONObject jsonObject1 = JSONObject.fromObject(jsonArray.get(0));
        JSONObject jsonObject2 = JSONObject.fromObject(jsonObject1.get("response"));
        // 图片存储地址记得上传的时候更改IP
//        String host = "/Users/weixj/Desktop/wph/IMDY/upload/";
//        String host = "http://127.0.0.1:8080/upload/picture/";
        String host = "http://123.234.130.3:8080/upload/";
        String imgUrl = host+ jsonObject2.get("data");
        return imgUrl;
    }
}
