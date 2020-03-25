package com.example.upc.common;

/**
 * @author zcc
 * @date 2019/3/25 20:19
 */
//包装器业务异常类实现
public class BusinessException extends RuntimeException implements CommonError{
    private CommonError commonError;

    //直接EmBusinessError传参用于构造异常
    public BusinessException(CommonError commonError){
        super();
        this.commonError = commonError;
    }
    //自定义
    public BusinessException(CommonError commonError,String errMsg){
        super();
        this.commonError = commonError;
        this.commonError.setErrMsg(errMsg);
    }

    @Override
    public int getErrCode() {
        return this.commonError.getErrCode();
    }

    @Override
    public String getErrMsg() {
        return this.commonError.getErrMsg();
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.commonError.setErrMsg(errMsg);
        return this;
    }
}
