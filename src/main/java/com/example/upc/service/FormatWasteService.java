package com.example.upc.service;

import com.example.upc.controller.param.FormatWasteParam;
import com.example.upc.controller.param.PageQuery;
import com.example.upc.controller.param.PageResult;
import com.example.upc.controller.searchParam.WasteSearchParam;
import com.example.upc.dataobject.FormatWaste;
import com.example.upc.dataobject.SysUser;

import java.util.List;

public interface FormatWasteService {
    PageResult getPage (PageQuery pageQuery, WasteSearchParam wasteSearchParam);
    PageResult getPageEnterprise (PageQuery pageQuery, Integer id, WasteSearchParam wasteSearchParam);
    PageResult getPageAdmin (PageQuery pageQuery, WasteSearchParam wasteSearchParam);
    void insert(FormatWasteParam formatWasteParam, SysUser sysUser);
    void update(FormatWasteParam formatWasteParam, SysUser sysUser);
    void delete(int fwId);
    void fail();
    List<FormatWaste> getPageEnterprise2 ( Integer id, WasteSearchParam wasteSearchParam);
}
