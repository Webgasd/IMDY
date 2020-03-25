package com.example.upc.controller.param;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class FormatOriginRecordExParam {
    private Integer id;
    @NotBlank(message = "原料类型不能为空")
    private String originType;
    @NotNull(message = "备案日期不能为空")
    private Date recordTime;
    @NotBlank(message = "原料名称不能为空")
    private String originName;
    @NotBlank(message = "原料类别不能为空")
    private String originTypeEx;
    private String producer;
    private String brand;
    private String netContent;
    @NotNull(message = "生产日期不能为空")
    private Date produceTime;
    @NotBlank(message = "保质期不能为空")
    private String keepTime;
    @NotNull(message = "有效期不能为空")
    private Date deadTime;
    @NotNull(message = "进货数不能为空")
    private Float goodsIn;
    @NotBlank(message = "单位不能为空")
    private String goodsType;
    @NotBlank(message = "供应商不能为空")
    private String supplier;
    @NotBlank(message = "供应商类型不能为空")
    private String supplierType;
    private Float goodsOut;
    private Float goods;
    @NotNull(message = "是否索证索票不能为空")
    private String state;
    @NotNull(message = "验收人不能为空")
    private String person;
    private String document;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }



    public String getOriginType() {
        return originType;
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public String getOriginName() {
        return originName;
    }

    public String getOriginTypeEx() {
        return originTypeEx;
    }

    public String getProducer() {
        return producer;
    }

    public String getBrand() {
        return brand;
    }

    public String getNetContent() {
        return netContent;
    }

    public Date getProduceTime() {
        return produceTime;
    }

    public String getKeepTime() {
        return keepTime;
    }

    public Date getDeadTime() {
        return deadTime;
    }

    public Float getGoodsIn() {
        return goodsIn;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public String getSupplier() {
        return supplier;
    }

    public String getSupplierType() {
        return supplierType;
    }

    public void setSupplierType(String supplierType) {
        this.supplierType = supplierType;
    }

    public Float getGoodsOut() {
        return goodsOut;
    }

    public String getState() {
        return state;
    }

    public String getPerson() {
        return person;
    }

    public String getDocument() {
        return document;
    }

    public Float getGoods() {
        return goods;
    }

    public void setGoods(Float goods) {
        this.goods = goods;
    }



    public void setOriginType(String originType) {
        this.originType = originType;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public void setOriginTypeEx(String originTypeEx) {
        this.originTypeEx = originTypeEx;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setNetContent(String netContent) {
        this.netContent = netContent;
    }

    public void setProduceTime(Date produceTime) {
        this.produceTime = produceTime;
    }

    public void setKeepTime(String keepTime) {
        this.keepTime = keepTime;
    }

    public void setDeadTime(Date deadTime) {
        this.deadTime = deadTime;
    }

    public void setGoodsIn(Float goodsIn) {
        this.goodsIn = goodsIn;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public void setGoodsOut(Float goodsOut) {
        this.goodsOut = goodsOut;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public void setDocument(String document) {
        this.document = document;
    }
}
