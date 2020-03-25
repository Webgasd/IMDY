package com.example.upc.service;

import com.example.upc.controller.param.PageQuery;
import com.example.upc.controller.param.PageResult;
import com.example.upc.controller.searchParam.OriginExtraSearchParam;
import com.example.upc.dataobject.FormatOriginExtra;
import com.example.upc.dataobject.SysUser;

public interface FormatOriginExtraService {
    PageResult getPage (PageQuery pageQuery, OriginExtraSearchParam originExtraSearchParam, SysUser sysUser);
    void insert(FormatOriginExtra formatOriginExtra, SysUser sysUser);
    void delete(int foId);
    void update(FormatOriginExtra formatOriginExtra, SysUser sysUser);
}
