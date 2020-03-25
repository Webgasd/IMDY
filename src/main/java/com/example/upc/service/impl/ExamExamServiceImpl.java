package com.example.upc.service.impl;

import com.example.upc.common.BusinessException;
import com.example.upc.common.EmBusinessError;
import com.example.upc.controller.param.*;
import com.example.upc.dao.*;
import com.example.upc.dataobject.*;
import com.example.upc.service.ExamExamService;
import com.example.upc.service.ExamSubjectTopicService;
import com.example.upc.service.ExamSubmitService;
import com.example.upc.service.ExamTrainCourseMaterialService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author zcc
 * @date 2019/4/29 20:42
 */
@Service
public class ExamExamServiceImpl implements ExamExamService {
    @Autowired
    private ExamExamMapper examExamMapper;
    @Autowired
    private ExamSubjectTopicService examSubjectTopicService;
    @Autowired
    private ExamSubjectMapper examSubjectMapper;
    @Autowired
    private ExamSubmitService examSubmitService;
    @Autowired
    private ExamCaExamMapper examCaExamMapper;
    @Autowired
    private ExamTrainCourseMaterialService examTrainCourseMaterialService;
    @Autowired
    private SupervisionCaMapper supervisionCaMapper;

    @Override
    public PageResult<ExamExamParam> getPage(PageQuery pageQuery) {
        int count=examExamMapper.countList();
        if (count > 0) {
            List<ExamExamParam> examExamList = examExamMapper.getPage(pageQuery);
            PageResult<ExamExamParam> pageResult = new PageResult<>();
            pageResult.setData(examExamList);
            pageResult.setTotal(count);
            pageResult.setPageNo(pageQuery.getPageNo());
            pageResult.setPageSize(pageQuery.getPageSize());
            return pageResult;
        }
        PageResult<ExamExamParam> pageResult = new PageResult<>();
        return pageResult;
    }

    @Override
    @Transactional
    public void insert(ExamExam examExam) {
       if(examExamMapper.countByName(examExam.getName())>0){
           throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"存在相同名称考试");
       }
       examExam.setOperateTime(new Date());
       examExam.setOperatorIp("124.124.124");
       examExam.setOperator("操作人");
       examExamMapper.insertSelective(examExam);
    }

    @Override
    @Transactional
    public void update(ExamExam examExam) {
        ExamExam before = examExamMapper.selectByPrimaryKey(examExam.getId());
        if(before==null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"待更新考试不存在");
        }
        examExam.setOperateTime(new Date());
        examExam.setOperatorIp("124.124.124");
        examExam.setOperator("操作人");
        examExamMapper.updateByPrimaryKeySelective(examExam);
    }

    @Override
    public void delete(int id) {
        ExamExam examExam = examExamMapper.selectByPrimaryKey(id);
        if(examExam==null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"待删除的不存在，无法删除");
        }
        examExamMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Map<String, Object> getExamTopicList(int caId,int examId,int subjectId) {
        List<ExamCaTopic>  examCaTopicList = examSubjectTopicService.getCaListBySubject(caId,examId,subjectId);
        ExamSubject examSubject = examSubjectMapper.selectByPrimaryKey(subjectId);
        Map<String, Object> map = Maps.newHashMap();
        map.put("list",examCaTopicList);
        map.put("subject",examSubject);
        return map;
    }

    @Override
    public List<ExamCaExamParam> getWorkTypeExamList(SysUser sysUser) {
        SupervisionCa supervisionCa = supervisionCaMapper.selectByPrimaryKey(sysUser.getInfoId());
        List<ExamCaExam> examCaExamList = examCaExamMapper.getByCaId(sysUser.getId());
        List<ExamCaExamParam> examExamList = examExamMapper.getByWorkType(supervisionCa.getWorkType());
        List<ExamCaExamParam> newExamList = examExamList.stream().map(examExam -> {
             if(checkTrain(examExam.getTrainCourse(),sysUser.getId())){
                examExam.setRemark("未完成培训");
            }
            else if(examCaExamList.stream().filter(examCaExam -> examCaExam.getExamId()==examExam.getId()).collect(Collectors.toList()).size()>0){
                        examExam.setRemark("已完成考试");
                    }else {
                        examExam.setRemark("点击进入考试");
                    }
            return examExam;
        }).collect(Collectors.toList());
        return newExamList;
    }

    private boolean checkTrain(int courseId,int caId){
        List<TrainCaMaterialParam> trainCaMaterialParamList = examTrainCourseMaterialService.getCaListByCourseId(caId,courseId);
        if(trainCaMaterialParamList.stream().filter(trainCaMaterialParam -> trainCaMaterialParam.getCompletionRate()!=1.0).collect(Collectors.toList()).size()>0){
            return true;
        }else {
            return false;
        }
    }

    @Override
    @Transactional
    public void changeCaExam(int caId, int examId,ExamSubject examSubject, List<ExamCaTopic> examCaTopicList) {
         float totalScore = examCaTopicList.stream().filter(examCaTopic -> examCaTopic.getAnswer().equals(examCaTopic.getCheck())).collect(Collectors.toList())
                 .stream().map(ExamCaTopic::getScore).reduce((i, j) -> i + j).get();
        examCaExamMapper.deleteByCaIdAndExamId(caId,examId);
        ExamCaExam examCaExam = new ExamCaExam();
        examCaExam.setCaId(caId);
        examCaExam.setExamId(examId);
        examCaExam.setExamScore(totalScore);
        examCaExam.setExamResult(totalScore>examSubject.getQualifiedScore()?1:0);
        examCaExam.setExamDate(new Date());
        examCaExam.setOperator("操作人");
        examCaExam.setOperateIp("124.124.124");
        examCaExam.setOperateTime(new Date());
        examCaExamMapper.insertSelective(examCaExam);
        examSubmitService.changeCaSubmit(caId,examId,examCaTopicList);
    }
}
