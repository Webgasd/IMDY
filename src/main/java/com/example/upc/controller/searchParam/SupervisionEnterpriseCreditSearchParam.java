package com.example.upc.controller.searchParam;

import com.example.upc.dataobject.SupervisionEnterpriseCredit;

public class SupervisionEnterpriseCreditSearchParam  extends SupervisionEnterpriseCredit {
    private String enterpriseName;
    private String legalPerson;
    private String operationMode;
    private String idNumber;
    private String businessAddress;

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

    public String getOperationMode() {
        return operationMode;
    }

    public void setOperationMode(String operationMode) {
        this.operationMode = operationMode;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }
}
