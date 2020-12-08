package com.example.upc.controller.param;

import com.example.upc.dataobject.SupervisionEnterpriseCredit;

public class SupervisionEnterpriseCreditParamDetail extends SupervisionEnterpriseCredit {
    private String operationMode;
    private String idNumber;
    private String businessAddress;
    private String enterpriseName;
    private String legalPerson;
    private String cantactWay;
    private Integer violationNumber;

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getLegalPerson() {
        return legalPerson;
    }

    public void setLegalPerson(String legalPerson) {
        this.legalPerson = legalPerson;
    }

    public String getCantactWay() {
        return cantactWay;
    }

    public void setCantactWay(String cantactWay) {
        this.cantactWay = cantactWay;
    }

    public Integer getViolationNumber() {
        return violationNumber;
    }

    public void setViolationNumber(Integer violationNumber) {
        this.violationNumber = violationNumber;
    }

    public String getOperationMode() {
        return operationMode;
    }

    public void setOperationMode(String operationMode) {
        this.operationMode = operationMode;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }
}
