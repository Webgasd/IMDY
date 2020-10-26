package com.example.upc.config.picConfig;

import com.example.upc.controller.param.NearEnterprise;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Component
@Aspect
public class PicMiniAspect {
    @Pointcut("@annotation(com.example.upc.config.picConfig.PicMini)")
    public void annotationPoinCut(){

    }

    @Around("annotationPoinCut()")
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void around(ProceedingJoinPoint proceedingJoinPoint)throws Throwable{
        Object object = proceedingJoinPoint.proceed();
        object.getClass();
    }
}
