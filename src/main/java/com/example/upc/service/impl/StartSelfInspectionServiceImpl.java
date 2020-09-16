package com.example.upc.service.impl;

import com.example.upc.common.BusinessException;
import com.example.upc.common.EmBusinessError;
import com.example.upc.common.ValidationResult;
import com.example.upc.common.ValidatorImpl;
import com.example.upc.controller.searchParam.InspectionSearchParam;
import com.example.upc.dao.InspectionPositionMapper;
import com.example.upc.dao.StartSelfInspectionMapper;
import com.example.upc.dao.SupervisionEnterpriseMapper;
import com.example.upc.dataobject.*;
import com.example.upc.service.StartSelfInspectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
    @Autowired
    private InspectionPositionMapper inspectionPositionMapper;

    @Override
    @Transactional(rollbackFor=Exception.class)
    public void insert(InspectionList inspectionList, SysUser sysUser){
        List<StartSelfInspection> startSelfInspection = inspectionList.getStartSelfInspectionList();
        String inspectionPositionName = inspectionList.getInspectionPosition();
//        ValidationResult result = validator.validate(startSelfInspection);
//        if(result.isHasErrors()){
//            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
//        }

        SupervisionEnterprise supervisionEnterprise = supervisionEnterpriseMapper.selectByPrimaryKey(sysUser.getInfoId());
        if (supervisionEnterprise==null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"无此企业信息");
        }

        Date inspectionDate = startSelfInspection.get(0).getInspectTime();
        String inspector = startSelfInspection.get(0).getInspector();
        InspectionPosition inspectionPosition = new InspectionPosition();
        inspectionPosition.setEnterprise(sysUser.getInfoId());
        inspectionPosition.setInspectionTime(inspectionDate);
        inspectionPosition.setInspector(inspector);
        inspectionPosition.setInspectionPositionName(inspectionPositionName);
        inspectionPositionMapper.insertSelective(inspectionPosition);

        for (StartSelfInspection item:startSelfInspection
             ) {
            item.setEnterprise(sysUser.getInfoId());
            item.setInspectionPosition(inspectionPosition.getId());
            item.setOperator(sysUser.getUsername());
            item.setOperatorIp("124.124.124");
            startSelfInspectionMapper.insertSelective(item);
        }

    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public void update(InspectionList inspectionList ,SysUser sysUser){
        List<StartSelfInspection> startSelfInspection = inspectionList.getStartSelfInspectionList();
        String inspectionPositionName = inspectionList.getInspectionPosition();
        ValidationResult result = validator.validate(startSelfInspection);
        if(result.isHasErrors()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
        }

        SupervisionEnterprise supervisionEnterprise = supervisionEnterpriseMapper.selectByPrimaryKey(sysUser.getInfoId());
        if (supervisionEnterprise==null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"无此企业信息");
        }

        Date inspectionDate = startSelfInspection.get(0).getInspectTime();
        String inspector = startSelfInspection.get(0).getInspector();
        InspectionPosition inspectionPosition = new InspectionPosition();
        inspectionPosition.setId(inspectionList.getInspectionPositionId());
        inspectionPosition.setEnterprise(sysUser.getInfoId());
        inspectionPosition.setInspectionTime(inspectionDate);
        inspectionPosition.setInspector(inspector);
        inspectionPosition.setInspectionPositionName(inspectionPositionName);
        inspectionPositionMapper.updateByPrimaryKeySelective(inspectionPosition);

        for (StartSelfInspection item:startSelfInspection
        ) {
            item.setEnterprise(sysUser.getInfoId());
            item.setInspectionPosition(inspectionPosition.getId());
            item.setOperator(sysUser.getUsername());
            item.setOperatorIp("124.124.124");
            startSelfInspectionMapper.updateByPrimaryKeySelective(item);
        }
    }

    @Override
    public void delete(int id){
        StartSelfInspection startSelfInspection = startSelfInspectionMapper.selectByPrimaryKey(id);
        if(startSelfInspection==null){
            throw new BusinessException(EmBusinessError.ID_ERROR);
        }
        inspectionPositionMapper.deleteByPrimaryKey(id);
        startSelfInspectionMapper.deleteByPosition(id);
    }

    @Override
    public List<StartSelfInspection> getByEnterpriseId (InspectionSearchParam inspectionSearchParam, int id){
        return startSelfInspectionMapper.getByEnterpriseId(inspectionSearchParam, id);
    }

    @Override
    public List<InspectionPosition> getInspectionPositionByDate (InspectionSearchParam inspectionSearchParam, int id){
        return inspectionPositionMapper.getInspectionPositionByDate(inspectionSearchParam,id);
    }

    @Override
    public List<StartSelfInspection> getInspectionByPosition(int positionId){
        return startSelfInspectionMapper.getInspectionByPosition(positionId);
    }
}
