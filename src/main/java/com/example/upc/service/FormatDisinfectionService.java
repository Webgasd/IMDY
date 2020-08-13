package com.example.upc.service;

import com.example.upc.controller.param.FormatDisinfectionParam;
import com.example.upc.controller.param.FormatDisinfectionSupParam;
import com.example.upc.controller.param.PageQuery;
import com.example.upc.controller.param.PageResult;
import com.example.upc.controller.searchParam.DisinfectionSearchParam;
import com.example.upc.dataobject.FormatDisinfection;
import com.example.upc.dataobject.SysUser;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

public interface FormatDisinfectionService {
    PageResult<FormatDisinfectionSupParam> getPage (PageQuery pageQuery, DisinfectionSearchParam disinfectionSearchParam);
    PageResult<FormatDisinfectionSupParam> getPageAdmin (PageQuery pageQuery, DisinfectionSearchParam disinfectionSearchParam);
    PageResult getPageEnterprise (PageQuery pageQuery, Integer id, DisinfectionSearchParam disinfectionSearchParam);
    void insert(FormatDisinfectionParam formatDisinfectionParam, SysUser sysUser);
    void delete(int fdId);
    void update(FormatDisinfectionParam formatDisinfectionParam, SysUser sysUser);
    void fail();
    void importExcel(MultipartFile file, Integer type,SysUser sysUser);
    void importExcelEx(MultipartFile file, Integer type);
    /**
     * 小程序专用service
     */
    List<Object> getDisinfectionRecord(int enterpeiseId, Date startDate);
}
