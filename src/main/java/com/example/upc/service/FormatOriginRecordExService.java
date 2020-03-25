package com.example.upc.service;

import com.example.upc.controller.param.FormatOriginRecordExEnParam;
import com.example.upc.controller.param.FormatOriginRecordExParam;
import com.example.upc.controller.param.PageQuery;
import com.example.upc.controller.param.PageResult;
import com.example.upc.controller.searchParam.OriginRecordExSearchParam;
import com.example.upc.dataobject.SysUser;
import org.springframework.web.multipart.MultipartFile;

public interface FormatOriginRecordExService {
    PageResult<FormatOriginRecordExEnParam> getPage (PageQuery pageQuery, OriginRecordExSearchParam originRecordExSearchParam);
    PageResult<FormatOriginRecordExEnParam> getPageEnterprise (PageQuery pageQuery, Integer id, OriginRecordExSearchParam originRecordExSearchParam);
    void insert(FormatOriginRecordExParam formatOriginRecordExParam, SysUser sysUser);
    PageResult<FormatOriginRecordExEnParam> getPageAdmin (PageQuery pageQuery, OriginRecordExSearchParam originRecordExSearchParam);
    void update(FormatOriginRecordExParam formatOriginRecordExParam,SysUser sysUser);
    void delete(int fpId);
    void fail();
    void importExcelEx(MultipartFile file, Integer type);
    void importExcel(MultipartFile file, Integer type,SysUser sysUser);
}
