package com.example.upc.service.impl;

import com.alibaba.druid.sql.ast.statement.SQLForeignKeyImpl;
import com.example.upc.common.BusinessException;
import com.example.upc.common.EmBusinessError;
import com.example.upc.controller.searchParam.OnlineBusinessSearchParam;
import com.example.upc.dao.OnlineBusinessMapper;
import com.example.upc.dataobject.OnlineBusiness;
import com.example.upc.dataobject.SysUser;
import com.example.upc.util.miniProgram.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.upc.service.OnlineBusinessService;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class OnlineBusinessSerciveimpl implements OnlineBusinessService {
    @Autowired
    OnlineBusinessMapper onlineBusinessMapper;

    @Override
    public OnlineBusiness getMessageByEnterpriseId(OnlineBusinessSearchParam onlineBusinessSearchParam) {
        OnlineBusiness onlineBusiness = onlineBusinessMapper.getMessageByEnterpriseId(onlineBusinessSearchParam.getEnterpriseId());
        if (onlineBusiness == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "无此企业信息");
        }
        return onlineBusiness;
    }

    @Override
    public void insertMessageByEnterpriseId(OnlineBusiness onlineBusiness) {
        OnlineBusiness onlineBusiness1 = onlineBusinessMapper.getMessageByEnterpriseId(onlineBusiness.getEnterpriseId());
        onlineBusiness.setOperateIp("124.124.124");
        onlineBusiness.setOperateTime(new Date());
        onlineBusiness.setOparetor("zcc");
        if (onlineBusiness1 == null) {
            onlineBusinessMapper.insert(onlineBusiness);
        } else {
            onlineBusinessMapper.deleteByPrimaryKey(onlineBusiness.getId());
            onlineBusinessMapper.insert(onlineBusiness);
        }

        return;
    }
}