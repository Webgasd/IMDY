package com.example.upc.controller.param;

import lombok.Data;

@Data
public class EnterpriseInfoParam {
    private Integer enterpriseId;   //企业ID
    private String operationMode;   //主体类型
    private Integer businessState;  //经营状态
    private String enterpriseName;  //企业名称
    private double latitude;        //维度
    private double longitude;       //经度
    private String contactWay;    //联系方式
}
