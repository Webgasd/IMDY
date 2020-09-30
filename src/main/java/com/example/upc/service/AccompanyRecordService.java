package com.example.upc.service;

import com.example.upc.controller.searchParam.AccompanySearchParam;
import com.example.upc.dataobject.AccompanyRecord;
import com.example.upc.dataobject.SysUser;

import java.util.Date;

public interface AccompanyRecordService {
    void insert(AccompanyRecord accompanyRecord, SysUser sysUser);

    void update(AccompanyRecord accompanyRecord, SysUser sysUser);

    void delete(AccompanyRecord accompanyRecord, SysUser sysUser);

    Object getAccompanyRecord(SysUser sysUser);

    Object getAccompanyRecordByDate(Date startDate, SysUser sysUser);
}
