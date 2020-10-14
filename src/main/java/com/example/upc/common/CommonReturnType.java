package com.example.upc.common;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static com.example.upc.common.ResponseUtil.toMessage;

/**
 * @author zcc
 * @date 2019/3/25 20:13
 */
//返回通用格式
public class CommonReturnType {
    private String status;//success或fail

    private Object data;

    public static CommonReturnType create(Object result){
        return CommonReturnType.create(result, "success","返回成功");
    }

    public static CommonReturnType create(Object result,String status){
        return CommonReturnType.create(result, status,"返回成功");
    }

    public static CommonReturnType create(Object result, String status,String message){
        CommonReturnType type = new CommonReturnType();
        type.setStatus(status);
        HttpServletResponse resp = ApplicationContextUtil.getServletActionContext().getResponse();
        assert resp != null;
        resp.setContentType(HttpConstants.CONTENT_TYPE_JSON);
        Map<String, Object> obj = toMessage(result);
        String response = JSONUtil.stringify(obj, false);
        type.setData(JSONObject.parseObject(String.valueOf(response)).get("data"));
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
