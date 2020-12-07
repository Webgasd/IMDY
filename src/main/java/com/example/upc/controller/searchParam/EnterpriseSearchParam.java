package com.example.upc.controller.searchParam;

import com.example.upc.dataobject.SysArea;
import com.example.upc.dataobject.SysIndustry;

import java.util.Date;
import java.util.List;

/**
 * @author zcc
 * @date 2019/7/12 10:40
 */
public class EnterpriseSearchParam {
    private String enterpriseName;
    private String storeName;
    private String creditCode;
    private String registeredAddress;
    private String businessAddress;
    private String licenseNumber;
    private Date startTime;
    private Date endTime;
    private List<Integer> areaList;
    private List<String> industryList;
    private String enterpriseScale;
    private Integer isStop;
    private String legalPerson;
    private String cantactWay;
    private Integer abnormalId;
    private String permissionStatus;
    private Integer dept;
    private String supervisor;
    private String userType;
    private String location;
    private Integer dis;
    private String operationMode;
    private Integer businessState;
    private Integer indexNum;

    //判断异常企业库或僵尸企业库
    private Integer judgeType = 0;

    public String getLegalPerson() {
        return legalPerson;
    }

    public void setLegalPerson(String legalPerson) {
        this.legalPerson = legalPerson;
    }

    public Integer getAbnormalId() {
        return abnormalId;
    }

    public void setAbnormalId(Integer abnormalId) {
        this.abnormalId = abnormalId;
    }

    public Integer getIndexNum() {
        return indexNum;
    }

    public void setIndexNum(Integer indexNum) {
        this.indexNum = indexNum;
    }

    public String getOperationMode() {
        return operationMode;
    }

    public void setOperationMode(String operationMode) {
        this.operationMode = operationMode;
    }

    public Integer getBusinessState() {
        return businessState;
    }

    public void setBusinessState(Integer businessState) {
        this.businessState = businessState;
    }

    public Integer getDis() {
        return dis;
    }

    public void setDis(Integer dis) {
        this.dis = dis;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getEnterpriseScale() {
        return enterpriseScale;
    }

    public void setEnterpriseScale(String enterpriseScale) {
        this.enterpriseScale = enterpriseScale;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getCreditCode() {
        return creditCode;
    }

    public void setCreditCode(String creditCode) {
        this.creditCode = creditCode;
    }

    public String getRegisteredAddress() {
        return registeredAddress;
    }

    public void setRegisteredAddress(String registeredAddress) {
        this.registeredAddress = registeredAddress;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public List<Integer> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<Integer> areaList) {
        this.areaList = areaList;
    }

    public List<String> getIndustryList() {
        return industryList;
    }

    public void setIndustryList(List<String> industryList) {
        this.industryList = industryList;
    }

    public Integer getIsStop() {
        return isStop;
    }

    public void setIsStop(Integer isStop) {
        this.isStop = isStop;
    }

    public Integer getDept() {
        return dept;
    }

    public void setDept(Integer dept) {
        this.dept = dept;
    }

    public String getPermissionStatus() {
        return permissionStatus;
    }

    public void setPermissionStatus(String permissionStatus) {
        this.permissionStatus = permissionStatus;
    }


    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public Integer getJudgeType() {
        return judgeType;
    }

    public void setJudgeType(Integer judgeType) {
        this.judgeType = judgeType;
    }

    public String getCantactWay() {
        return cantactWay;
    }

    public void setCantactWay(String cantactWay) {
        this.cantactWay = cantactWay;
    }
}
