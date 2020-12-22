package com.example.upc.controller;

import com.example.upc.common.*;
import com.example.upc.controller.param.PageQuery;
import com.example.upc.controller.param.SupervisionEnterpriseCreditParam;
import com.example.upc.controller.searchParam.SupervisionEnterpriseCreditSearchParam;
import com.example.upc.dataobject.SysUser;
import com.example.upc.service.SupervisionEnterpriseCreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.InvocationTargetException;

@Controller
@RequestMapping("/supervision/enterpriseCredit")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class SupervisionEnterpriseCreditController {

    @Autowired
    SupervisionEnterpriseCreditService supervisionEnterpriseCreditService;

    @RequestMapping("/getPage")
    @ResponseBody
    public CommonReturnType getPage(@RequestBody SupervisionEnterpriseCreditSearchParam supervisionEnterpriseCreditSearchParam, SysUser sysUser, PageQuery pageQuery){
        return CommonReturnType.create(supervisionEnterpriseCreditService.getPage(supervisionEnterpriseCreditSearchParam,sysUser,pageQuery));
    }

    @RequestMapping("/getCreditPage")
    @ResponseBody
    public CommonReturnType getCreditPage(@RequestBody SupervisionEnterpriseCreditSearchParam supervisionEnterpriseCreditSearchParam, SysUser sysUser, PageQuery pageQuery){
        return CommonReturnType.create(supervisionEnterpriseCreditService.getCreditPage(supervisionEnterpriseCreditSearchParam,sysUser,pageQuery));
    }

    @RequestMapping("/getById")
    @ResponseBody
    public CommonReturnType getById(@RequestBody SupervisionEnterpriseCreditSearchParam supervisionEnterpriseCreditSearchParam, SysUser sysUser, PageQuery pageQuery){
        return CommonReturnType.create(supervisionEnterpriseCreditService.getById(supervisionEnterpriseCreditSearchParam,sysUser,pageQuery));
    }

    @RequestMapping("/insert")
    @ResponseBody
    public CommonReturnType insert(@RequestBody SupervisionEnterpriseCreditSearchParam supervisionEnterpriseCreditSearchParam) throws InvocationTargetException, IllegalAccessException {
        supervisionEnterpriseCreditService.insert(supervisionEnterpriseCreditSearchParam);
        return CommonReturnType.create(null);
    }

    @RequestMapping("/update")
    @ResponseBody
    public CommonReturnType update(@RequestBody SupervisionEnterpriseCreditSearchParam supervisionEnterpriseCreditSearchParam) throws InvocationTargetException, IllegalAccessException {
        supervisionEnterpriseCreditService.update(supervisionEnterpriseCreditSearchParam);
        return CommonReturnType.create(null);
    }

    @RequestMapping("/delete")
    @ResponseBody
    public CommonReturnType delete(@RequestBody SupervisionEnterpriseCreditSearchParam supervisionEnterpriseCreditSearchParam){
        supervisionEnterpriseCreditService.delete(supervisionEnterpriseCreditSearchParam);
        return CommonReturnType.create(null);
    }
}
