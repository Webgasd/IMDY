package com.example.upc.controller.param;

import com.example.upc.dataobject.SupervisionCa;

public class SupervisionCaParam extends SupervisionCa {
    private String industryName;
    private float caScore;
    public void setIndustryName(String industryName){this.industryName=industryName;}
    public String getIndustryName(){return industryName;}
    public void setCaScore(float caScore){this.caScore=caScore;}
    public float getCaScore(){return caScore;}
}
