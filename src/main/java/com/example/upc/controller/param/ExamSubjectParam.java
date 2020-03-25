package com.example.upc.controller.param;

import com.example.upc.dataobject.ExamSubject;

import java.util.Date;

/**
 * @author zcc
 * @date 2019/5/13 10:02
 */
public class ExamSubjectParam extends ExamSubject {

    private String industryName;
    private String workTypeName;
    private Integer topicNumber=0;
    private Integer judgementNumber=0;
    private Integer singleNumber=0;
    private Integer multipleNumber=0;

    public String getIndustryName() {
        return industryName;
    }

    public void setIndustryName(String industryName) {
        this.industryName = industryName;
    }

    public String getWorkTypeName() {
        return workTypeName;
    }

    public void setWorkTypeName(String workTypeName) {
        this.workTypeName = workTypeName;
    }

    public Integer getTopicNumber() {
        return topicNumber;
    }

    public void setTopicNumber(Integer topicNumber) {
        this.topicNumber = topicNumber;
    }

    public Integer getJudgementNumber() {
        return judgementNumber;
    }

    public void setJudgementNumber(Integer judgementNumber) {
        this.judgementNumber = judgementNumber;
    }

    public Integer getSingleNumber() {
        return singleNumber;
    }

    public void setSingleNumber(Integer singleNumber) {
        this.singleNumber = singleNumber;
    }

    public Integer getMultipleNumber() {
        return multipleNumber;
    }

    public void setMultipleNumber(Integer multipleNumber) {
        this.multipleNumber = multipleNumber;
    }
}
