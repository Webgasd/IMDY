package com.example.upc.service;

import com.example.upc.controller.searchParam.InspectionSearchParam;
import com.example.upc.dataobject.StartSelfInspection;
import com.example.upc.dataobject.SysUser;

import java.util.List;

public interface StartSelfInspectionService {
    void insert(StartSelfInspection startSelfInspection, SysUser sysUser);
    void update(StartSelfInspection startSelfInspection, SysUser sysUser);
    void delete(int id);
    List<StartSelfInspection> getByEnterpriseId (InspectionSearchParam inspectionSearchParam, int id);
}
