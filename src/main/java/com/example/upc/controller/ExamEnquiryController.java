package com.example.upc.controller;

import com.example.upc.common.CommonReturnType;
import com.example.upc.controller.param.PageQuery;
import com.example.upc.controller.searchParam.ExamEnquirySearchParam;
import com.example.upc.controller.searchParam.TrainPersonSearchParam;
import com.example.upc.service.ExamCaExamService;
import com.example.upc.service.ExamExamService;
import com.example.upc.service.ExamTrainCaMaterialService;
import com.example.upc.service.ExamTrainCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author zcc
 * @date 2019/6/11 17:00
 */
@Controller
@RequestMapping("/exam/enquiry")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class ExamEnquiryController {
    @Autowired
    private ExamCaExamService examCaExamService;
    @Autowired
    private ExamExamService examExamService;
    @Autowired
    private ExamTrainCaMaterialService examTrainCaMaterialService;
    @Autowired
    private ExamTrainCourseService examTrainCourseService;
    @RequestMapping("/getPage")
    @ResponseBody
    public CommonReturnType getPage(PageQuery pageQuery, ExamEnquirySearchParam examEnquirySearchParam){
        return CommonReturnType.create(examCaExamService.getPage(pageQuery,examEnquirySearchParam));
    }

    @RequestMapping("/getCaTopicResult")
    @ResponseBody
    public CommonReturnType getCaTopicList(int examId,int subjectId,int caId){
        return CommonReturnType.create(examExamService.getExamTopicList(caId,examId,subjectId));
    }

    @RequestMapping("/getTrainPage")
    @ResponseBody
    public CommonReturnType getTrainPage(PageQuery pageQuery, TrainPersonSearchParam trainPersonSearchParam){
        return CommonReturnType.create(examTrainCaMaterialService.getPage(pageQuery,trainPersonSearchParam));
    }

    @RequestMapping("/getCaTrainResult")
    @ResponseBody
    public CommonReturnType getCaTrainResult(int courseId,int caId){
        return CommonReturnType.create(examTrainCourseService.getCourseMaterialIds(courseId,caId));
    }
}
