package com.example.upc.service.impl;

import com.example.upc.common.BusinessException;
import com.example.upc.common.EmBusinessError;
import com.example.upc.controller.searchParam.AccompanySearchParam;
import com.example.upc.dao.AccompanyRecordMapper;
import com.example.upc.dataobject.AccompanyRecord;
import com.example.upc.dataobject.FormatDisinfection;
import com.example.upc.dataobject.SysUser;
import com.example.upc.service.AccompanyRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AccompanyRecordServiceImpl implements AccompanyRecordService {
    @Autowired
    AccompanyRecordMapper accompanyRecordMapper;

    @Override
    public void insert(AccompanyRecord accompanyRecord, SysUser sysUser) {
        accompanyRecord.setOperator(sysUser.getLoginName());
        accompanyRecord.setOperatorIp("127.0.0.1");
        accompanyRecord.setOperatorTime(new Date());
        accompanyRecord.setEnterpriseId(sysUser.getInfoId());
        accompanyRecordMapper.insert(accompanyRecord);
        return;
    }

    @Override
    public void update(AccompanyRecord accompanyRecord, SysUser sysUser) {
        AccompanyRecord accompanyRecord1 =accompanyRecordMapper.selectByInfoIdAndId(sysUser.getInfoId(),accompanyRecord.getId());
        if(accompanyRecord1==null){
            throw new BusinessException(EmBusinessError.UPDATE_ERROR);
        }
        accompanyRecord.setOperator(sysUser.getLoginName());
        accompanyRecord.setEnterpriseId(sysUser.getInfoId());
        accompanyRecord.setOperatorTime(new Date());
        accompanyRecord.setOperatorIp("127.0.0.1");
        accompanyRecordMapper.updateByPrimaryKey(accompanyRecord);
        return;
    }

    @Override
    public void delete(AccompanyRecord accompanyRecord, SysUser sysUser) {
        accompanyRecordMapper.deleteByPrimaryKey(accompanyRecord.getId());
        return;
    }

    @Override
    public Object getAccompanyRecord( SysUser sysUser) {
        List<AccompanyRecord> accList = accompanyRecordMapper.getAllRecord(sysUser.getInfoId());
        return accList;
    }

    @Override
    public Object getAccompanyRecordByDate(Date startDate, SysUser sysUser) {
        Date endDate = new Date();
        endDate.setTime(startDate.getTime()+86399999);
        List<FormatDisinfection> searchList = accompanyRecordMapper.getAccompanyRecordByDate(sysUser.getInfoId(), startDate, endDate);
        return searchList;
    }
}
