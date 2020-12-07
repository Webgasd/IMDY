package com.example.upc.service;


import com.example.upc.controller.param.PageQuery;
import com.example.upc.controller.param.SupervisionEnterpriseCreditParam;
import com.example.upc.controller.searchParam.SupervisionEnterpriseCreditSearchParam;
import com.example.upc.dataobject.SupervisionEnterpriseCredit;
import com.example.upc.dataobject.SysUser;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface SupervisionEnterpriseCreditService {
    List<SupervisionEnterpriseCreditParam> getPage(SupervisionEnterpriseCreditSearchParam supervisionEnterpriseCreditSearchParam,SysUser sysUser,PageQuery pageQuery);
    List<SupervisionEnterpriseCredit> getById(SupervisionEnterpriseCreditSearchParam supervisionEnterpriseCreditSearchParam, SysUser sysUser, PageQuery pageQuery);
    void insert(SupervisionEnterpriseCreditSearchParam supervisionEnterpriseCreditSearchParam) throws InvocationTargetException, IllegalAccessException;
    void update(SupervisionEnterpriseCreditSearchParam supervisionEnterpriseCreditSearchParam) throws InvocationTargetException, IllegalAccessException;
    void delete(SupervisionEnterpriseCreditSearchParam supervisionEnterpriseCreditSearchParam);
}
