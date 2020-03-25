package com.example.upc.controller.searchParam;


import java.util.Date;
import java.util.List;

public class DisinfectionSearchParam {

    private String enterprise;
    private String name;
    private Integer amount;
    private Date start;
    private Date end;
    private String person;
    private List<Integer> areaList;

    public String getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(String enterprise) {
        this.enterprise = enterprise;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
    public Integer getAmount() {
        return amount;
    }
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getPerson() {
        return person;
    }
    public void setPerson(String person) {
        this.person = person == null ? null : person.trim();
    }
    public List<Integer> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<Integer> areaList) {
        this.areaList = areaList;
    }
}
