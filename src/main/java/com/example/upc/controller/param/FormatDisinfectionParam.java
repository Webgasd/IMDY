package com.example.upc.controller.param;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class FormatDisinfectionParam {
    private Integer id;
    @NotBlank(message = "物品名称不能为空")
    private String name;
    @NotNull(message = "数量不能为空")
    private Integer amount;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @NotNull(message = "消毒日期不能为空")
    private Date date;
    private String person;
    @NotNull(message = "开始时不能为空")
    private Integer start1;
    @NotNull(message = "开始分不能为空")
    private Integer start2;
    @NotNull(message = "结束时不能为空")
    private Integer end1;
    @NotNull(message = "结束分不能为空")
    private Integer end2;
    @NotBlank(message = "消毒方式不能为空")
    private String way;
    private String remark;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
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
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public String getPerson() {
        return person;
    }
    public void setPerson(String person) {
        this.person = person == null ? null : person.trim();
    }

    public Integer getStart1() {
        return start1;
    }

    public Integer getStart2() {
        return start2;
    }

    public Integer getEnd1() {
        return end1;
    }

    public Integer getEnd2() {
        return end2;
    }

    public void setStart1(Integer start1) {
        this.start1 = start1;
    }

    public void setStart2(Integer start2) {
        this.start2 = start2;
    }

    public void setEnd1(Integer end1) {
        this.end1 = end1;
    }

    public void setEnd2(Integer end2) {
        this.end2 = end2;
    }

    public String getWay() {
        return way;
    }
    public void setWay(String way) {
        this.way = way == null ? null : way.trim();
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

}
