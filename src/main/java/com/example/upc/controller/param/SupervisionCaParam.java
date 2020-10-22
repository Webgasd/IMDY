package com.example.upc.controller.param;

import com.example.upc.dataobject.SupervisionCa;

public class SupervisionCaParam extends SupervisionCa {
    private String industryName;
    private float caScore;
    private String startDate;//获取健康证日期不包括时分秒
    private String endDate;//获取健康证日期不包括时分秒
    public void setStartDate(String startDate){
        this.startDate=startDate;
    }
    public String getStartDate(){
        return startDate;
    }
    public void setEndDate(String endDate){
        this.endDate=endDate;
    }
    public String getEndDate(){
        return endDate;
    }
    public void setIndustryName(String industryName){this.industryName=industryName;}
    public String getIndustryName(){return industryName;}
    public void setCaScore(float caScore){this.caScore=caScore;}
    public float getCaScore(){return caScore;}
}
