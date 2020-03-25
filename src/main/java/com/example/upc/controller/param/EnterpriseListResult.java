package com.example.upc.controller.param;

import java.util.Date;

/**
 * @author zcc
 * @date 2019/7/17 15:29
 */
public class EnterpriseListResult {
    private Integer id;
    private String enterpriseName;
    private String enterpriseScale;
    private String idNumber;
    private Integer area;
    private String grid;
    private String supervisor;
    private String regulators;
    private Integer isStop;
    private Integer foodBusiness;
    private Integer foodCommon;
    private Integer foodCirculate;
    private Integer foodProduce;
    private Integer drugsBusiness;
    private Integer medicalUse;
    private Integer cosmeticsUse;
    private Date endTime;

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getEnterpriseScale() {
        return enterpriseScale;
    }

    public void setEnterpriseScale(String enterpriseScale) {
        this.enterpriseScale = enterpriseScale;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public Integer getArea() {
        return area;
    }

    public void setArea(Integer area) {
        this.area = area;
    }

    public String getGrid() {
        return grid;
    }

    public void setGrid(String grid) {
        this.grid = grid;
    }

    public String getRegulators() {
        return regulators;
    }

    public void setRegulators(String regulators) {
        this.regulators = regulators;
    }

    public Integer getIsStop() {
        return isStop;
    }

    public void setIsStop(Integer isStop) {
        this.isStop = isStop;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getFoodBusiness() {
        return foodBusiness;
    }

    public void setFoodBusiness(Integer foodBusiness) {
        this.foodBusiness = foodBusiness;
    }

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
