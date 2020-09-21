package com.example.upc.controller.param;

import com.example.upc.dataobject.SysUser;

/**
 * @author 75186
 */
public class SysUserParam extends SysUser {
    private Integer userId;
    private String weChatId;
    private String name;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getWeChatId() {
        return weChatId;
    }

    public void setWeChatId(String weChatId) {
        this.weChatId = weChatId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
