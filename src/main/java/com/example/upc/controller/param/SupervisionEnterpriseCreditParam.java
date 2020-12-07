package com.example.upc.controller.param;

import com.example.upc.dataobject.SupervisionEnterpriseCredit;

public class SupervisionEnterpriseCreditParam extends SupervisionEnterpriseCredit {
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
}
