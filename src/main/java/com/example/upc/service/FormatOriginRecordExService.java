package com.example.upc.service;

import com.example.upc.controller.param.*;
import com.example.upc.controller.searchParam.OriginRecordExSearchParam;
import com.example.upc.dataobject.SysUser;
import org.springframework.web.multipart.MultipartFile;

public interface FormatOriginRecordExService {
    PageResult<FormatOriginRecordExListParam> getPage (PageQuery pageQuery, OriginRecordExSearchParam originRecordExSearchParam);
    PageResult<FormatOriginRecordExListParam> getPageEnterprise (PageQuery pageQuery, Integer id, OriginRecordExSearchParam originRecordExSearchParam);
    void insert(FormatOriginRecordExParam formatOriginRecordExParam, SysUser sysUser);
    PageResult<FormatOriginRecordExListParam> getPageAdmin (PageQuery pageQuery, OriginRecordExSearchParam originRecordExSearchParam);
    void update(FormatOriginRecordExParam formatOriginRecordExParam,SysUser sysUser);
    void delete(int fpId);
    void fail();
    void importExcelEx(MultipartFile file, Integer type);
    void importExcel(MultipartFile file, Integer type,SysUser sysUser);
}
