package com.example.upc.dao;

import com.example.upc.controller.param.ExamSubjectParam;
import com.example.upc.controller.param.PageQuery;
import com.example.upc.dataobject.ExamSubject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExamSubjectMapper {

    int deleteByPrimaryKey(Integer id);
    int insert(ExamSubject record);
    int insertSelective(ExamSubject record);
    ExamSubject selectByPrimaryKey(Integer id);
    int updateByPrimaryKeySelective(ExamSubject record);
    int updateByPrimaryKey(ExamSubject record);

    int countList();
    List<ExamSubjectParam> getPage(@Param("page") PageQuery page);
    int countByIndustryAndWorkType(@Param("industry") int industry,@Param("workType") int workType);
}