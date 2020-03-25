package com.example.upc.redis;

import com.example.upc.common.BusinessException;
import com.example.upc.common.EmBusinessError;
import com.example.upc.controller.param.UserParam;
import com.example.upc.dao.SysUserMapper;
import com.example.upc.dataobject.SysUser;
import com.example.upc.util.MD5Util;
import com.example.upc.util.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zcc
 * @date 2019/5/25 21:02
 */
@Service
public class UserSessionService {
    public static final String COOKIE_NAME_TOKEN = "token";

    @Autowired
    RedisService redisService;
    @Autowired
    SysUserMapper sysUserMapper;

    public SysUser getByToken(HttpServletResponse response, String token) {
        if(StringUtils.isEmpty(token)) {
            throw new BusinessException(EmBusinessError.PLEASE_LOGIN);
        }
        SysUser sysUser =(SysUser)redisService.getUser(SessionUserKey.token, token);
        //延长有效期
        if(sysUser != null) {
            addCookie(response, token, sysUser);
        }else {
            throw new BusinessException(EmBusinessError.PLEASE_LOGIN);
        }
        return sysUser;
    }
    public boolean logout(HttpServletRequest request,HttpServletResponse response){
        String paramToken = request.getParameter(UserSessionService.COOKIE_NAME_TOKEN);
        String cookieToken = getCookieValue(request, UserSessionService.COOKIE_NAME_TOKEN);
        if(StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
            return true;
        }
        String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
        redisService.delUser(SessionUserKey.token, token);
        return true;
    }

    public boolean login(HttpServletResponse response, UserParam userParam) {
        if(userParam == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数错误");
        }
        String loginName = userParam.getLoginName();
        String formPass = userParam.getPassword();
        //判断账号是否存在
        SysUser sysUser = sysUserMapper.selectByLoginName(loginName);
        if(sysUser == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"帐号不存在");
        }
        //验证密码
        String dbPass = sysUser.getPassword();
        MD5Util md5Code =new MD5Util();
        if(!md5Code.md5(formPass).equals(dbPass)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"密码错误");
        }
        //生成cookie
        String token	 = sysUser.getId().toString()+'_'+UUIDUtil.uuid();
        addCookie(response, token, sysUser);
        return true;
    }

    private void addCookie(HttpServletResponse response, String token, SysUser sysUser) {
        redisService.setUser(SessionUserKey.token, token, sysUser);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(SessionUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    private String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[]  cookies = request.getCookies();
        if(cookies==null){
            return null;
        }
        for(Cookie cookie : cookies) {
            if(cookie.getName().equals(cookieName)) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
