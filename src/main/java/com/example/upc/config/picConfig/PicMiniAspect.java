package com.example.upc.config.picConfig;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;


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
