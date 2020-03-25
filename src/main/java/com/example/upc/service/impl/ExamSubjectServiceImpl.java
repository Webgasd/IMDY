package com.example.upc.service.impl;

import com.example.upc.common.BusinessException;
import com.example.upc.common.EmBusinessError;
import com.example.upc.controller.param.ExamSubjectParam;
import com.example.upc.controller.param.PageQuery;
import com.example.upc.controller.param.PageResult;
import com.example.upc.dao.*;
import com.example.upc.dataobject.ExamSubject;
import com.example.upc.dataobject.SysIndustry;
import com.example.upc.dataobject.SysWorkType;
import com.example.upc.service.ExamSubjectService;
import com.example.upc.service.ExamSubjectTopicService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zcc
 * @date 2019/4/29 20:54
 */
@Service
public class ExamSubjectServiceImpl implements ExamSubjectService {
    @Autowired
    private ExamSubjectMapper examSubjectMapper;
    @Autowired
    private SysIndustryMapper sysIndustryMapper;
    @Autowired
    private SysWorkTypeMapper sysWorkTypeMapper;
    @Autowired
    private ExamSubjectTopicService examSubjectTopicService;
    @Autowired
    private ExamSubjectTopicMapper examSubjectTopicMapper;

    @Override
    public PageResult<ExamSubjectParam> getPage(PageQuery pageQuery) {
        int count=examSubjectMapper.countList();
        if (count > 0) {
            List<ExamSubjectParam> examSubjectParamList = examSubjectMapper.getPage(pageQuery);
            PageResult<ExamSubjectParam> pageResult = new PageResult<>();
            pageResult.setData(examSubjectParamList);
            pageResult.setTotal(count);
            pageResult.setPageNo(pageQuery.getPageNo());
            pageResult.setPageSize(pageQuery.getPageSize());
            return pageResult;
        }
        PageResult<ExamSubjectParam> pageResult = new PageResult<>();
        return pageResult;
    }

    @Override
    @Transactional
    public void insert(ExamSubject examSubject,List<Integer> topicIds) {
        if(examSubjectMapper.countByIndustryAndWorkType(examSubject.getIndustry(),examSubject.getWorkType())>0){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"存在相同科目");
        }
        examSubject.setOperator("操作人");
        examSubject.setOperatorIp("14.124.124");
        examSubject.setOperateTime(new Date());
        examSubjectMapper.insertSelective(examSubject);
        examSubjectTopicService.changeSubjectTopics(examSubject.getId(),topicIds);
    }

    @Override
    @Transactional
    public void update(ExamSubject examSubject,List<Integer> topicIds) {
        ExamSubject before = examSubjectMapper.selectByPrimaryKey(examSubject.getId());
        if(before == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"待更新科目不存在");
        }
        examSubject.setOperator("操作人");
        examSubject.setOperatorIp("14.124.124");
        examSubject.setOperateTime(new Date());
        examSubjectMapper.updateByPrimaryKeySelective(examSubject);
        examSubjectTopicService.changeSubjectTopics(examSubject.getId(),topicIds);
    }

    @Override
    @Transactional
    public void delete(int id) {
        ExamSubject examSubject = examSubjectMapper.selectByPrimaryKey(id);
        if(examSubject==null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"待删除的不存在，无法删除");
        }
        examSubjectMapper.deleteByPrimaryKey(id);
        examSubjectTopicService.deleteBySubjectId(id);
    }

    @Override
    public List<Integer> getSubjectTopicIds(int id) {
        return examSubjectTopicMapper.getTopicIdListBySubjectId(id);
    }
}
