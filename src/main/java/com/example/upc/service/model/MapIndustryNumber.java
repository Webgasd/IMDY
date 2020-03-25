package com.example.upc.service.model;

/**
 * @author zcc
 * @date 2019/11/1 20:23
 */
public class MapIndustryNumber {
    private Integer foodCommon;
    private Integer foodCirculate;
    private Integer foodProduce;
    private Integer drugsBusiness;
    private Integer medicalUse;
    private Integer cosmeticsUse;

    public Integer getFoodCommon() {
        return foodCommon;
    }

    public void setFoodCommon(Integer foodCommon) {
        this.foodCommon = foodCommon;
    }

    public Integer getFoodCirculate() {
        return foodCirculate;
    }

    public void setFoodCirculate(Integer foodCirculate) {
        this.foodCirculate = foodCirculate;
    }

    public Integer getFoodProduce() {
        return foodProduce;
    }

    public void setFoodProduce(Integer foodProduce) {
        this.foodProduce = foodProduce;
    }

    public Integer getDrugsBusiness() {
        return drugsBusiness;
    }

    public void setDrugsBusiness(Integer drugsBusiness) {
        this.drugsBusiness = drugsBusiness;
    }

    public Integer getMedicalUse() {
        return medicalUse;
    }

    public void setMedicalUse(Integer medicalUse) {
        this.medicalUse = medicalUse;
    }

    public Integer getCosmeticsUse() {
        return cosmeticsUse;
    }

    public void setCosmeticsUse(Integer cosmeticsUse) {
        this.cosmeticsUse = cosmeticsUse;
    }
}
