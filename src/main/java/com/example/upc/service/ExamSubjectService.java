package com.example.upc.service;

import com.example.upc.controller.param.ExamSubjectParam;
import com.example.upc.controller.param.PageQuery;
import com.example.upc.controller.param.PageResult;
import com.example.upc.dataobject.ExamSubject;

import java.util.List;

/**
 * @author zcc
 * @date 2019/4/29 20:54
 */
public interface ExamSubjectService {
    PageResult<ExamSubjectParam> getPage(PageQuery pageQuery);
    void insert(ExamSubject examSubject,List<Integer> topicIds);
    void update(ExamSubject examSubject, List<Integer> topicIds);
    void delete(int id);
    List<Integer> getSubjectTopicIds(int id);
}
