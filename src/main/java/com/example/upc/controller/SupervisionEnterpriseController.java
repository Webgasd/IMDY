package com.example.upc.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.upc.common.BusinessException;
import com.example.upc.common.CommonReturnType;
import com.example.upc.common.EmBusinessError;
import com.example.upc.controller.param.PageQuery;
import com.example.upc.controller.searchParam.EnterpriseSearchParam;
import com.example.upc.dataobject.*;
import com.example.upc.service.*;
import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
        import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zcc
 * @date 2019/4/25 10:47
 */
@Controller
@RequestMapping("/supervision/enterprise")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class SupervisionEnterpriseController {
    @Autowired
    private SupervisionEnterpriseService supervisionEnterpriseService;
    @Autowired
    private SysDeptIndustryService sysDeptIndustryService;
    @Autowired
    private SysDeptAreaService sysDeptAreaService;
    @Autowired
    private SysTreeService sysTreeService;
    @Autowired
    private SupervisionConfigCategoryService supervisionConfigCategoryService;
    @Autowired
    private SupervisionConfigLicenceService supervisionConfigLicenceService;
    @Autowired
    private SupervisionGaService supervisionGaService;
    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private SysAreaService sysAreaService;
    @Autowired
    private SysIndustryService sysIndustryService;
    @Autowired
    private SupervisionCaService supervisionCaService;
    @Autowired
    private GridPointsService gridPointsService;

    @RequestMapping("/getPage")
    @ResponseBody
    public CommonReturnType getPage(@RequestBody String json, SysUser sysUser){
        JSONObject jsonObject = JSON.parseObject(json);
        EnterpriseSearchParam enterpriseSearchParam= JSON.parseObject(json,EnterpriseSearchParam.class);
        PageQuery pageQuery=JSON.parseObject(json,PageQuery.class);
        Integer areaId = null;
        if(!StringUtils.isEmpty(jsonObject.getJSONArray("areaList").get(0)))
        {
            areaId = (Integer)jsonObject.getJSONArray("areaList").get(0);
        }
        boolean searchIndustry = StringUtils.isEmpty(jsonObject.getJSONArray("industryList").get(0));

        if(sysUser.getUserType()==1){
            return CommonReturnType.create(supervisionEnterpriseService.getPageByEnterpriseId(sysUser.getInfoId()));
        }else{
            return CommonReturnType.create(supervisionEnterpriseService.getPage(pageQuery,enterpriseSearchParam,sysUser,areaId,searchIndustry));
        }
    }

    //统计企业信息,不同区域不同业态权限统计
    @RequestMapping("/getStatistics")
    @ResponseBody
    public CommonReturnType getStatistics(SysUser sysUser){
        if (sysUser.getUserType()==0){
            return CommonReturnType.create(supervisionEnterpriseService.getStatistics(sysIndustryService.getAll(),sysAreaService.getAll().stream().map((sysArea -> sysArea.getId())).collect(Collectors.toList()),null));
        }else if(sysUser.getUserType()==1){
            return CommonReturnType.create(new HashMap<>());
        }
        else if(sysUser.getUserType()==2){
            SupervisionGa supervisionGa = supervisionGaService.getById(sysUser.getInfoId());
            List<SysIndustry> sysIndustryList = sysDeptIndustryService.getListByDeptId(supervisionGa.getDepartment());
            List<Integer> sysAreaList = sysDeptAreaService.getIdListByDeptId(supervisionGa.getDepartment());
            SysDept sysDept = sysDeptService.getById(supervisionGa.getDepartment());
            String supervisor = null;
            if(sysDept.getType()==2){
                if(supervisionGa.getType()!=2){
                    supervisor=supervisionGa.getName();
                }
            }
            return CommonReturnType.create(supervisionEnterpriseService.getStatistics(sysIndustryList,sysAreaList,supervisor));
        }else{
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"非法用户");
        }
    }

//根据某个企业id来查看这个企业的信息，主要接口，要修改这里的关联方法
    @RequestMapping("/getById")
    @ResponseBody
    public CommonReturnType getById(int id){
        return CommonReturnType.create(supervisionEnterpriseService.getById(id));
    }

    //无用户登录时测试用
    @RequestMapping("/getPageNoUser")
    @ResponseBody
    public CommonReturnType getPageNoUser(@RequestBody String json){
        JSONObject jsonObject = JSON.parseObject(json);
        EnterpriseSearchParam enterpriseSearchParam= JSON.parseObject(json,EnterpriseSearchParam.class);
        PageQuery pageQuery=JSON.parseObject(json,PageQuery.class);
        Integer areaId = null;
        if(!StringUtils.isEmpty(jsonObject.getJSONArray("areaList").get(0))){
            areaId = (Integer)jsonObject.getJSONArray("areaList").get(0);}
        boolean searchIndustry = StringUtils.isEmpty(jsonObject.getJSONArray("industryList").get(0));

        return CommonReturnType.create(supervisionEnterpriseService.getPageNoUser(pageQuery,enterpriseSearchParam,areaId,searchIndustry));

    }


    @RequestMapping("/getQrcodeById")
    @ResponseBody
    public CommonReturnType getQrcodeById(int id){
        Map<String,Object> map = new HashMap<>();
        map.put("enterprise",supervisionEnterpriseService.getById(id));
        map.put("caList",supervisionCaService.getAllByEnterpriseId(id));
        return CommonReturnType.create(map);
    }

    @RequestMapping("/changeStop")
    @ResponseBody
    public CommonReturnType changeStop(int id){
        supervisionEnterpriseService.changeStop(id);
        return CommonReturnType.create(null);
    }

    //获取部门树
    @RequestMapping("/getDeptAndGrid")
    @ResponseBody
    public CommonReturnType getDeptAndGrid(){
        Map<String,Object> map = new HashMap<>();
        map.put("deptTree",sysTreeService.deptTree());
        return CommonReturnType.create(map);
    }

    @RequestMapping("/insert")
    @ResponseBody
    public CommonReturnType insert(@RequestBody String json){
        supervisionEnterpriseService.insert(json);
        return CommonReturnType.create(null);
    }

    @RequestMapping("/update")
    @ResponseBody
    public CommonReturnType update(@RequestBody String json){
        supervisionEnterpriseService.update(json);
        return CommonReturnType.create(null);
    }

    //删除企业同时删除定位中的企业，要改，在service中写删除定位，或者在这写两个表都删除
    @RequestMapping("/delete")
    @ResponseBody
    public CommonReturnType delete(int id){
        supervisionEnterpriseService.delete(id);
        gridPointsService.deleteByEnterpriseId(id);
        return CommonReturnType.create(null);
    }

    @RequestMapping("/getCateAndLicence")
    @ResponseBody
    public CommonReturnType getCateAndSend(int id){
        Map<String,Object> map = new HashMap<>();
        map.put("categoryList",supervisionConfigCategoryService.getByPermiss(id));
        map.put("licenceList",supervisionConfigLicenceService.getByPermiss(id));
        return CommonReturnType.create(map);
    }

    //企业导入，着重修改这里，使之能够不删除修改
//    @RequestMapping("/importExcel")
//    @ResponseBody
//    public CommonReturnType importExcel(MultipartFile file) {
//        String fileName = file.getOriginalFilename();
//        if(fileName.matches("^.+\\.(?i)(xls)$")){//03版本excel,xls
//            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"该文件类型已不支持，请使用07版本后缀为.xlsx版本导入");
//        }else if (fileName.matches("^.+\\.(?i)(xlsx)$")){//07版本,xlsx
//            supervisionEnterpriseService.importExcel(file,7);
//        }
//        return CommonReturnType.create(null);
//    }
}
