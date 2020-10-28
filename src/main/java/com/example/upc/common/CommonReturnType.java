package com.example.upc.common;

import com.alibaba.fastjson.JSONObject;
import com.example.upc.config.picConfig.PicMini;
import com.example.upc.config.picConfig.GetUser;
import com.example.upc.util.JsonToImageUrl;
import org.apache.commons.collections4.Get;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.Map;

import static com.example.upc.common.ResponseUtil.toMessage;

/**
 * @author zcc
 * @date 2019/3/25 20:13
 */
//返回通用格式
@Component
public class CommonReturnType {
    @Autowired
    GetUser getUser;

    private String status;//success或fail

    private Object data;

    private static GetUser getUser2;

    @PostConstruct
    public void initialize() {
        getUser2 = getUser;
    }

    public static CommonReturnType create(Object result) {
        return CommonReturnType.create(result, "success", "返回成功");
    }

    public static CommonReturnType create(Object result, String status) {
        return CommonReturnType.create(result, status, "返回成功");
    }

    public static CommonReturnType create(Object result, String status, String message) {
        CommonReturnType type = new CommonReturnType();
        HttpServletResponse resp = ApplicationContextUtil.getServletActionContext().getResponse();
        assert resp != null;
        resp.setContentType(HttpConstants.CONTENT_TYPE_JSON);
        //返回对象转化为Map类型键值对
        Map<String, Object> obj = toMessage(result);
        //去掉其中的空值
        String response = JSONUtil.stringify(obj, false);
        System.out.println(response);
//        //String转化为JSONObject
//        JSONObject jsonObject = JSONObject.parseObject(JSONObject.parseObject(String.valueOf(response)).get("data").toString());
//        //如果是微信小程序端则进行图片路径转化
//        if (getUser2.getUserPlat() == 1) {
//            Field[] fields = FieldUtils.getAllFields(result.getClass());
//            if (fields != null) {
//                for (Field field : fields) {
//                    if (!field.isAccessible()) {
//                        field.setAccessible(true);
//                    }
//                    PicMini picTrans = field.getAnnotation(PicMini.class);
//                    if (picTrans != null) {
//                        String name = picTrans.value();
//                        if (jsonObject.get(name) != null) {
//                            jsonObject.put(name, JsonToImageUrl.JSON2ImageUrl(jsonObject.get(name)));
//                        }
//                    }
//                }
//            }
//        }

        type.setStatus(status);
//        JSONObject jsonObject = JSONObject.parseObject(JSONObject.parseObject(String.valueOf(response)).get("data").toString());
        JSONObject jsonObject = JSONObject.parseObject(String.valueOf(response));
        type.setData(jsonObject.get("data"));
        return type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
