package com.example.upc.service;

import com.example.upc.controller.param.ExamCaTopic;
import com.example.upc.dataobject.ExamTopicBank;

import java.util.List;

/**
 * @author zcc
 * @date 2019/4/29 22:10
 */
public interface ExamSubjectTopicService {
    List<ExamTopicBank> getListBySubject(int SubjectId);
    List<ExamCaTopic> getCaListBySubject(int caId,int examId,int SubjectId);
    void changeSubjectTopics(int subjectId,List<Integer> topicIdList);
    void deleteBySubjectId(int id);
}
