package com.example.upc.service.impl;

import com.example.upc.common.BusinessException;
import com.example.upc.common.EmBusinessError;
import com.example.upc.common.ValidationResult;
import com.example.upc.common.ValidatorImpl;
import com.example.upc.common.pageConfig.DoPage;
import com.example.upc.controller.param.EnterpriseListResult;
import com.example.upc.controller.param.PageQuery;
import com.example.upc.controller.param.PageResult;
import com.example.upc.controller.param.SupervisionEnterpriseCreditParam;
import com.example.upc.controller.searchParam.SupervisionEnterpriseCreditSearchParam;
import com.example.upc.dao.SupervisionEnterpriseCreditMapper;
import com.example.upc.dataobject.SupervisionEnterpriseCredit;
import com.example.upc.dataobject.SysUser;
import com.example.upc.service.SupervisionEnterpriseCreditService;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SupervisionEnterpriseCreditServiceImpl implements SupervisionEnterpriseCreditService {
    @Autowired
    SupervisionEnterpriseCreditMapper supervisionEnterpriseCreditMapper;
    @Autowired
    private ValidatorImpl validator;

    @Override
    public PageResult<SupervisionEnterpriseCreditParam> getPage(SupervisionEnterpriseCreditSearchParam supervisionEnterpriseCreditSearchParam, SysUser sysUser, PageQuery pageQuery){
        if(sysUser.getUserType()==0) {
            int count = supervisionEnterpriseCreditMapper.counListCredit();
            if(count>0) {
                PageResult<SupervisionEnterpriseCreditParam> pageResult = new PageResult<>();
                List<SupervisionEnterpriseCreditParam> supervisionEnterpriseCreditParamList=supervisionEnterpriseCreditMapper.getPage(supervisionEnterpriseCreditSearchParam, pageQuery);
                pageResult.setData(supervisionEnterpriseCreditParamList);
                pageResult.setTotal(count);
                pageResult.setPageNo(pageQuery.getPageNo());
                pageResult.setPageSize(pageQuery.getPageSize());
                return pageResult;
            }
            PageResult<SupervisionEnterpriseCreditParam> pageResult = new PageResult<>();
            return pageResult;
        }
        else{
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"没有权限");
        }
    }

    @Override
    public PageResult<SupervisionEnterpriseCredit> getById(SupervisionEnterpriseCreditSearchParam supervisionEnterpriseCreditSearchParam, SysUser sysUser, PageQuery pageQuery){
        if(sysUser.getUserType()==0) {
            List<SupervisionEnterpriseCredit> supervisionEnterpriseCreditList = supervisionEnterpriseCreditMapper.selectByEnterpriseId(supervisionEnterpriseCreditSearchParam,pageQuery);
            PageResult<SupervisionEnterpriseCredit> pageResult = new PageResult<>();
            pageResult.setData(supervisionEnterpriseCreditList);
            pageResult.setTotal(supervisionEnterpriseCreditList.size());
            pageResult.setPageNo(pageQuery.getPageNo());
            pageResult.setPageSize(pageQuery.getPageSize());
            return pageResult;
        }
        if(sysUser.getUserType()==2){
            supervisionEnterpriseCreditSearchParam.setEnterpriseId(sysUser.getInfoId());
            List<SupervisionEnterpriseCredit> supervisionEnterpriseCreditList = supervisionEnterpriseCreditMapper.selectByEnterpriseId(supervisionEnterpriseCreditSearchParam,pageQuery);
            PageResult<SupervisionEnterpriseCredit> pageResult = new PageResult<>();
            pageResult.setData(supervisionEnterpriseCreditList);
            pageResult.setTotal(supervisionEnterpriseCreditList.size());
            pageResult.setPageNo(pageQuery.getPageNo());
            pageResult.setPageSize(pageQuery.getPageSize());
            return pageResult;
        }
        PageResult<SupervisionEnterpriseCredit> pageResult = new PageResult<>();
        return pageResult;
    }

    @Override
    public void insert(SupervisionEnterpriseCreditSearchParam supervisionEnterpriseCreditSearchParam) throws InvocationTargetException, IllegalAccessException {
        ValidationResult validationResult=validator.validate(supervisionEnterpriseCreditSearchParam);
        if(validationResult.isHasErrors())
        {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,validationResult.getErrMsg());
        }
        SupervisionEnterpriseCredit supervisionEnterpriseCredit = new SupervisionEnterpriseCredit();
        supervisionEnterpriseCredit.setEnterpriseId(supervisionEnterpriseCreditSearchParam.getEnterpriseId());
        supervisionEnterpriseCredit.setViolationTitle(supervisionEnterpriseCreditSearchParam.getViolationTitle());
        supervisionEnterpriseCredit.setViolationType(supervisionEnterpriseCreditSearchParam.getViolationType());
        supervisionEnterpriseCredit.setViolationDate(supervisionEnterpriseCreditSearchParam.getViolationDate());
        supervisionEnterpriseCredit.setAgentPerson(supervisionEnterpriseCreditSearchParam.getAgentPerson());
        supervisionEnterpriseCredit.setOperator("operator");
        supervisionEnterpriseCredit.setOperatorIp("127.0.0.1");
        supervisionEnterpriseCredit.setOperatorTime(new Date());
        supervisionEnterpriseCreditMapper.insertSelective(supervisionEnterpriseCredit);
    }

    @Override
    public void update(SupervisionEnterpriseCreditSearchParam supervisionEnterpriseCreditSearchParam) throws InvocationTargetException, IllegalAccessException {
        ValidationResult validationResult=validator.validate(supervisionEnterpriseCreditSearchParam);
        if(validationResult.isHasErrors())
        {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,validationResult.getErrMsg());
        }
        SupervisionEnterpriseCredit supervisionEnterpriseCredit = new SupervisionEnterpriseCredit();
        supervisionEnterpriseCredit.setId(supervisionEnterpriseCreditSearchParam.getId());
        supervisionEnterpriseCredit.setEnterpriseId(supervisionEnterpriseCreditSearchParam.getEnterpriseId());
        supervisionEnterpriseCredit.setViolationTitle(supervisionEnterpriseCreditSearchParam.getViolationTitle());
        supervisionEnterpriseCredit.setViolationType(supervisionEnterpriseCreditSearchParam.getViolationType());
        supervisionEnterpriseCredit.setViolationDate(supervisionEnterpriseCreditSearchParam.getViolationDate());
        supervisionEnterpriseCredit.setAgentPerson(supervisionEnterpriseCreditSearchParam.getAgentPerson());
        supervisionEnterpriseCredit.setOperator("operator");
        supervisionEnterpriseCredit.setOperatorIp("127.0.0.1");
        supervisionEnterpriseCredit.setOperatorTime(new Date());
        supervisionEnterpriseCreditMapper.updateByPrimaryKey(supervisionEnterpriseCredit);
    }

    @Override
    public void delete(SupervisionEnterpriseCreditSearchParam supervisionEnterpriseCreditSearchParam){
        if(supervisionEnterpriseCreditSearchParam.getId()!=null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"没有待删除企业参数");
        }
        supervisionEnterpriseCreditMapper.deleteByPrimaryKey(supervisionEnterpriseCreditSearchParam.getId());
    }
}
