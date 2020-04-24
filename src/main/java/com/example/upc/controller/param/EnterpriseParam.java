package com.example.upc.controller.param;

import com.example.upc.dataobject.*;

import java.util.List;

/**
 * @author zcc
 * @date 2019/7/1 23:10
 */
public class EnterpriseParam extends SupervisionEnterprise {

    private SupervisionEnFoodBu foodBusiness;
    private List<SupervisionEnFoodBu> foodBusinessList;

    private SupervisionEnCosmetics cosmeticsUse;
    private List<SupervisionEnCosmetics> cosmeticsList;
    private EnFoodProduceParam foodProduce;
    private List<EnFoodProduceParam> foodProduceList;
    private SupervisionEnDrugsBu drugsBusiness;
    private List<SupervisionEnDrugsBu> drugsBusinessList;
    private List<SupervisionEnDrugsPro> drugsProduceList;
    private SupervisionEnMedicalBu medicalBusiness;
    private List<SupervisionEnMedicalBu> medicalBusinessList;
    private List<SupervisionEnMedicalPro> medicalProduceList;
    private List<SupervisionEnSmallCater> smallCaterList;
    private List<SupervisionEnSmallWorkshop> smallWorkshopList;
    private List<SupervisionEnIndustrialProducts> industrialProductsList;


    public List<SupervisionEnCosmetics> getCosmeticsList() {
        return cosmeticsList;
    }

    public void setCosmeticsList(List<SupervisionEnCosmetics> cosmeticsList) {
        this.cosmeticsList = cosmeticsList;
    }

    public List<EnFoodProduceParam> getFoodProduceList() {
        return foodProduceList;
    }

    public void setFoodProduceList(List<EnFoodProduceParam> foodProduceList) {
        this.foodProduceList = foodProduceList;
    }

    public List<SupervisionEnDrugsBu> getDrugsBusinessList() {
        return drugsBusinessList;
    }

    public void setDrugsBusinessList(List<SupervisionEnDrugsBu> drugsBusinessList) {
        this.drugsBusinessList = drugsBusinessList;
    }

    public List<SupervisionEnDrugsPro> getDrugsProduceList() {
        return drugsProduceList;
    }

    public void setDrugsProduceList(List<SupervisionEnDrugsPro> drugsProduceList) {
        this.drugsProduceList = drugsProduceList;
    }

    public SupervisionEnMedicalBu getMedicalBusiness() {
        return medicalBusiness;
    }

    public void setMedicalBusiness(SupervisionEnMedicalBu medicalBusiness) {
        this.medicalBusiness = medicalBusiness;
    }

    public List<SupervisionEnMedicalBu> getMedicalBusinessList() {
        return medicalBusinessList;
    }

    public void setMedicalBusinessList(List<SupervisionEnMedicalBu> medicalBusinessList) {
        this.medicalBusinessList = medicalBusinessList;
    }

    public List<SupervisionEnMedicalPro> getMedicalProduceList() {
        return medicalProduceList;
    }

    public void setMedicalProduceList(List<SupervisionEnMedicalPro> medicalProduceList) {
        this.medicalProduceList = medicalProduceList;
    }

    public List<SupervisionEnFoodBu> getFoodBusinessList() {
        return foodBusinessList;
    }

    public void setFoodBusinessList(List<SupervisionEnFoodBu> foodBusinessList) {
        this.foodBusinessList = foodBusinessList;
    }

    public SupervisionEnFoodBu getFoodBusiness() {
        return foodBusiness;
    }

    public void setFoodBusiness(SupervisionEnFoodBu foodBusiness) {
        this.foodBusiness = foodBusiness;
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

    public List<SupervisionEnSmallCater> getSmallCaterList() {
        return smallCaterList;
    }

    public void setSmallCaterList(List<SupervisionEnSmallCater> smallCaterList) {
        this.smallCaterList = smallCaterList;
    }

    public List<SupervisionEnSmallWorkshop> getSmallWorkshopList() {
        return smallWorkshopList;
    }

    public void setSmallWorkshopList(List<SupervisionEnSmallWorkshop> smallWorkshopList) {
        this.smallWorkshopList = smallWorkshopList;
    }

    public List<SupervisionEnIndustrialProducts> getIndustrialProductsList() {
        return industrialProductsList;
    }

    public void setIndustrialProductsList(List<SupervisionEnIndustrialProducts> industrialProductsList) {
        this.industrialProductsList = industrialProductsList;
    }
}
