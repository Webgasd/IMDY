package com.example.upc.common;

/**
 * @author zcc
 * @date 2019/3/25 20:13
 */
//返回通用格式
public class CommonReturnType {
    private String status;//success或fail

    private Object data;

    public static CommonReturnType create(Object result){
        return CommonReturnType.create(result,"success");
    }
    public static CommonReturnType create(Object result,String status){
        CommonReturnType type = new CommonReturnType();
        type.setStatus(status);
        type.setData(result);
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
