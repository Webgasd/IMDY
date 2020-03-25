package com.example.upc.controller.param;

import com.example.upc.dataobject.*;

/**
 * @author zcc
 * @date 2019/7/1 23:10
 */
public class EnterpriseParam extends SupervisionEnterprise {
    private SupervisionEnCommon foodCommon ;
    private SupervisionEnFoodBu foodBusiness;
    private SupervisionEnFoodCir foodCirculate;
    private SupervisionEnCosmetics cosmeticsUse;
    private EnFoodProduceParam foodProduce;
    private SupervisionEnDrugsBu drugsBusiness;
    private SupervisionEnMedical medicalUse;

    public SupervisionEnCommon getFoodCommon() {
        return foodCommon;
    }

    public void setFoodCommon(SupervisionEnCommon foodCommon) {
        this.foodCommon = foodCommon;
    }

    public SupervisionEnFoodBu getFoodBusiness() {
        return foodBusiness;
    }

    public void setFoodBusiness(SupervisionEnFoodBu foodBusiness) {
        this.foodBusiness = foodBusiness;
    }

    public SupervisionEnFoodCir getFoodCirculate() {
        return foodCirculate;
    }

    public void setFoodCirculate(SupervisionEnFoodCir foodCirculate) {
        this.foodCirculate = foodCirculate;
    }

    public SupervisionEnCosmetics getCosmeticsUse() {
        return cosmeticsUse;
    }

    public void setCosmeticsUse(SupervisionEnCosmetics cosmeticsUse) {
        this.cosmeticsUse = cosmeticsUse;
    }

    public EnFoodProduceParam getFoodProduce() {
        return foodProduce;
    }

    public void setFoodProduce(EnFoodProduceParam foodProduce) {
        this.foodProduce = foodProduce;
    }

    public SupervisionEnDrugsBu getDrugsBusiness() {
        return drugsBusiness;
    }

    public void setDrugsBusiness(SupervisionEnDrugsBu drugsBusiness) {
        this.drugsBusiness = drugsBusiness;
    }

    public SupervisionEnMedical getMedicalUse() {
        return medicalUse;
    }

    public void setMedicalUse(SupervisionEnMedical medicalUse) {
        this.medicalUse = medicalUse;
    }
}
