package com.example.upc.controller.param;

import com.example.upc.dataobject.ExamExam;

import java.util.Date;

/**
 * @author zcc
 * @date 2019/5/13 16:22
 */
public class ExamExamParam extends ExamExam {
    private String workTypeName;
    private String industryName;
    private Float totalScore;
    private Float qualifiedScore;
    private String trainCourseName;

    public String getTrainCourseName() {
        return trainCourseName;
    }

    public void setTrainCourseName(String trainCourseName) {
        this.trainCourseName = trainCourseName;
    }

    public String getWorkTypeName() {
        return workTypeName;
    }

    public void setWorkTypeName(String workTypeName) {
        this.workTypeName = workTypeName;
    }

    public String getIndustryName() {
        return industryName;
    }

    public void setIndustryName(String industryName) {
        this.industryName = industryName;
    }

    public Float getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Float totalScore) {
        this.totalScore = totalScore;
    }

    public Float getQualifiedScore() {
        return qualifiedScore;
    }

    public void setQualifiedScore(Float qualifiedScore) {
        this.qualifiedScore = qualifiedScore;
    }
}
