package com.example.upc.service.impl;

import com.example.upc.common.BusinessException;
import com.example.upc.common.EmBusinessError;
import com.example.upc.common.ValidationResult;
import com.example.upc.common.ValidatorImpl;
import com.example.upc.controller.searchParam.InspectionSearchParam;
import com.example.upc.dao.StartSelfInspectionMapper;
import com.example.upc.dao.SupervisionEnterpriseMapper;
import com.example.upc.dataobject.FormatPicture;
import com.example.upc.dataobject.StartSelfInspection;
import com.example.upc.dataobject.SupervisionEnterprise;
import com.example.upc.dataobject.SysUser;
import com.example.upc.service.StartSelfInspectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 董志涵
 */
@Service
public class StartSelfInspectionServiceImpl implements StartSelfInspectionService {
    @Autowired
    private ValidatorImpl validator;
    @Autowired
    private StartSelfInspectionMapper startSelfInspectionMapper;
    @Autowired
    private SupervisionEnterpriseMapper supervisionEnterpriseMapper;

    @Override
    @Transactional(rollbackFor=Exception.class)
    public void insert(StartSelfInspection startSelfInspection, SysUser sysUser){
        ValidationResult result = validator.validate(startSelfInspection);
        if(result.isHasErrors()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
        }

        SupervisionEnterprise supervisionEnterprise = supervisionEnterpriseMapper.selectByPrimaryKey(sysUser.getInfoId());
        if (supervisionEnterprise==null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"无此企业信息");
        }
        startSelfInspection.setEnterprise(sysUser.getInfoId());
        startSelfInspection.setOperator(sysUser.getUsername());
        startSelfInspection.setOperatorIp("124.124.124");
        startSelfInspectionMapper.insertSelective(startSelfInspection);
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public void update(StartSelfInspection startSelfInspection,SysUser sysUser){
        ValidationResult result = validator.validate(startSelfInspection);
        if(result.isHasErrors()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
        }

        if(startSelfInspectionMapper.selectByPrimaryKey(startSelfInspection.getId())==null){
            throw new BusinessException(EmBusinessError.UPDATE_ERROR);
        }
        startSelfInspection.setEnterprise(sysUser.getInfoId());
        startSelfInspection.setOperator(sysUser.getUsername());
        startSelfInspection.setOperatorIp("124.124.124");
        startSelfInspectionMapper.updateByPrimaryKeySelective(startSelfInspection);
    }

    @Override
    public void delete(int id){
        StartSelfInspection startSelfInspection = startSelfInspectionMapper.selectByPrimaryKey(id);
        if(startSelfInspection==null){
            throw new BusinessException(EmBusinessError.ID_ERROR);
        }
        startSelfInspectionMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<StartSelfInspection> getByEnterpriseId (InspectionSearchParam inspectionSearchParam, int id){
        return startSelfInspectionMapper.getByEnterpriseId(inspectionSearchParam, id);
    }
}
