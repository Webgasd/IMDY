package com.example.upc.dao;

import com.example.upc.controller.param.ExamCaTopic;
import com.example.upc.controller.param.PageQuery;
import com.example.upc.controller.searchParam.ExamTopicSearchParam;
import com.example.upc.dataobject.ExamTopicBank;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExamTopicBankMapper {

    int deleteByPrimaryKey(Integer id);
    int insert(ExamTopicBank record);
    int insertSelective(ExamTopicBank record);
    ExamTopicBank selectByPrimaryKey(Integer id);
    int updateByPrimaryKeySelective(ExamTopicBank record);
    int updateByPrimaryKey(ExamTopicBank record);

    int countList();
    List<ExamTopicBank> getList();
    List<ExamTopicBank> getPage(@Param("page") PageQuery page,@Param("search") ExamTopicSearchParam examTopicSearchParam);
    int countByTitle(@Param("title") String title);
    List<ExamTopicBank> getByIdList(@Param("idList") List<Integer> idList);
    List<ExamCaTopic> getCaExamList(@Param("caId") int caId, @Param("examId") int examId, @Param("idList") List<Integer> idList);
}