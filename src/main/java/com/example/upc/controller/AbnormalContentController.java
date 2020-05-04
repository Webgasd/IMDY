package com.example.upc.controller;

import com.example.upc.common.CommonReturnType;
import com.example.upc.controller.param.PageQuery;
import com.example.upc.dataobject.AbnormalContent;
import com.example.upc.service.AbnormalContentService;
import com.example.upc.service.SupervisionEnterpriseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/abnormal")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class AbnormalContentController {
    @Autowired
    private AbnormalContentService abnormalContentService;
    @Autowired
    private SupervisionEnterpriseService supervisionEnterpriseService;

    @RequestMapping("/getPage")
    @ResponseBody
    public CommonReturnType getPage(PageQuery pageQuery){
        return CommonReturnType.create(abnormalContentService.getPage(pageQuery));

    }
    @RequestMapping("/insert")
    @ResponseBody
    public CommonReturnType insert(AbnormalContent abnormalContent){
        abnormalContentService.insert(abnormalContent);
        return CommonReturnType.create(null);
    }

    @RequestMapping("/update")
    @ResponseBody
    public CommonReturnType update(AbnormalContent abnormalContent){
        abnormalContentService.update(abnormalContent);
        return CommonReturnType.create(null);
    }

    @RequestMapping("/getList")
    @ResponseBody
    public CommonReturnType getList(){
        return CommonReturnType.create(abnormalContentService.getList());
    }

    @RequestMapping("/changeNormal")
    @ResponseBody
    public CommonReturnType changeNormal(Integer enterpriseId){
        supervisionEnterpriseService.changeNormal(enterpriseId);
        return CommonReturnType.create(null);
    }

    @RequestMapping("/insertContent")
    @ResponseBody
    public CommonReturnType changeAbnormal(Integer enterpriseId, Integer abnormal, String abnormalContent){
        supervisionEnterpriseService.changeAbnormal(enterpriseId,abnormal,abnormalContent);
        return CommonReturnType.create(null);
    }

}
