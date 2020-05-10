package com.example.upc.controller.param;

import com.example.upc.dataobject.*;

import java.util.List;

/**
 * @author zcc
 * @date 2019/7/1 23:10
 */
public class EnterpriseParam extends SupervisionEnterprise {

    private List<SupervisionEnFoodBu> foodBusinessList;
    private List<SupervisionEnCosmetics> cosmeticsList;
    private List<EnFoodProduceParam> foodProduceList;
    private List<SupervisionEnDrugsBu> drugsBusinessList;
    private List<SupervisionEnDrugsPro> drugsProduceList;
    private List<SupervisionEnMedicalBu> medicalBusinessList;
    private List<SupervisionEnMedicalPro> medicalProduceList;
    private List<SupervisionEnSmallCater> smallCaterList;
    private List<SupervisionEnSmallWorkshop> smallWorkshopList;
    private List<SupervisionEnIndustrialProducts> industrialProductsList;
    private String location;
    private String position;
    private String permissionFamily;

    public String getPermissionFamily() {
        return permissionFamily;
    }

    public void setPermissionFamily(String permissionFamily) {
        this.permissionFamily = permissionFamily;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

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
