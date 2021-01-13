package com.example.upc.common.pageConfig;

import com.example.upc.common.ApplicationContextUtil;
import com.example.upc.controller.param.PageQuery;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 分页实现
 *
 * @author hhm
 * @version 1.0
 * @date 2019年12月4日
 */
@Component
@Aspect
public class DoPageAspect {

    PageInfo pageInfo = new PageInfo("items=0-0");

    @Pointcut("@annotation(com.example.upc.common.pageConfig.DoPage)")
    public void annotationPoinCut() {

    }

    @Around("annotationPoinCut()")
    @SuppressWarnings({"unchecked", "rawtypes"})
    public <T> List<T> around(ProceedingJoinPoint joinPoint) throws Throwable {
        //获取连接点方法运行时的入参列表
        Object[] params = joinPoint.getArgs();
        //获取请求/响应
        ServletRequestAttributes servletRequestAttributes = ApplicationContextUtil.getServletActionContext();
        int i = 0;
        boolean flag = false;
        for (i = 0; i < params.length; i++) {
            if (params[i].getClass() == PageQuery.class) {
                PageQuery pageQuery = (PageQuery) params[i];
                if (pageQuery.getPageFlag() == 1) {
                    pageInfo = PageInfoUtil.initPageInfo(pageQuery);
                    flag = true;
                }
                break;
            }
        }
        if (!flag)
        pageInfo = PageInfoUtil.initPageInfo(servletRequestAttributes.getRequest());
        setPageInfo(pageInfo);
        //请求中不包含分页信息时不做分页处理
        if (pageInfo != null) {
            // TODO 后期扩展Oracle的分页，此处需要添加一个路由的方法，根据不同的数据库类型使用不同的分页方式
            PageHelper.startPage(pageInfo.getStart(), pageInfo.getEnd());
        }

        Object list = joinPoint.proceed(params);
        List<T> result = null;
        if (list instanceof ArrayList) {
            result = (List) list;
        }
        if (pageInfo != null) {
            com.github.pagehelper.PageInfo<?> page = new com.github.pagehelper.PageInfo<>(result);
            if (pageInfo.getCount() == 0) {
                pageInfo.setCount((int) page.getTotal());
            }
        } else {
            pageInfo = new PageInfo("items=0-0");
            if (result == null) {
                pageInfo.setCount(0);
            } else {
                pageInfo.setCount(result.size());
            }
        }
        servletRequestAttributes.getResponse().setHeader("Content-Range", pageInfo.toString());
        return result;
    }


    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }


}

