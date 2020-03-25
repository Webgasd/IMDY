package com.example.upc.service.impl;

import com.example.upc.controller.param.ExamCaTopic;
import com.example.upc.dao.ExamSubjectTopicMapper;
import com.example.upc.dao.ExamTopicBankMapper;
import com.example.upc.dataobject.ExamSubjectTopic;
import com.example.upc.dataobject.ExamTopicBank;
import com.example.upc.service.ExamSubjectTopicService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author zcc
 * @date 2019/4/29 22:11
 */
@Service
public class       ExamSubjectTopicServiceImpl implements ExamSubjectTopicService {
    @Autowired
    private ExamSubjectTopicMapper examSubjectTopicMapper;
    @Autowired
    private ExamTopicBankMapper examTopicBankMapper;

    @Override
    public List<ExamTopicBank> getListBySubject(int SubjectId) {
        List<Integer> topicIdList = examSubjectTopicMapper.getTopicIdListBySubjectId(SubjectId);
        if (CollectionUtils.isEmpty(topicIdList)) {
            return Lists.newArrayList();
        }
        return examTopicBankMapper.getByIdList(topicIdList);
    }

    @Override
    public List<ExamCaTopic> getCaListBySubject(int caId, int examId, int SubjectId) {
        List<Integer> topicIdList = examSubjectTopicMapper.getTopicIdListBySubjectId(SubjectId);
        if (CollectionUtils.isEmpty(topicIdList)) {
            return Lists.newArrayList();
        }
        return examTopicBankMapper.getCaExamList(caId,examId,topicIdList);
    }

    @Override
    public void changeSubjectTopics(int subjectId, List<Integer> topicIdList) {
        List<Integer> originTopicIdList = examSubjectTopicMapper.getTopicIdListBySubjectId(subjectId);
        if (originTopicIdList.size() == topicIdList.size()) {
            Set<Integer> originTopicIdSet = Sets.newHashSet(originTopicIdList);
            Set<Integer> topicIdSet = Sets.newHashSet(topicIdList);
            originTopicIdSet.removeAll(topicIdSet);
            if (CollectionUtils.isEmpty(originTopicIdSet)) {
                return;
            }
        }
        updateRoleUsers(subjectId, topicIdList);
    }

    @Override
    public void deleteBySubjectId(int id) {
        examSubjectTopicMapper.deleteBySubjectId(id);
    }

    @Transactional
    public void updateRoleUsers(int subjectId, List<Integer> topicIdList) {
         examSubjectTopicMapper.deleteBySubjectId(subjectId);

        if (CollectionUtils.isEmpty(topicIdList)) {
            return;
        }
        List<ExamSubjectTopic> subjectTopicList = Lists.newArrayList();
        for (Integer topicId : topicIdList) {
            ExamSubjectTopic examSubjectTopic = new ExamSubjectTopic();
            examSubjectTopic.setSubjectId(subjectId);
            examSubjectTopic.setTopicId(topicId);
            examSubjectTopic.setOperator("操作人");
            examSubjectTopic.setOperateIp("124.124.124");
            examSubjectTopic.setOperateTime(new Date());
            subjectTopicList.add(examSubjectTopic);
        }
        examSubjectTopicMapper.batchInsert(subjectTopicList);
    }
}
