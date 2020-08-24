package com.example.upc.service;

import com.example.upc.controller.param.FormatLeaveParam;
import com.example.upc.controller.param.FormatLeaveSupParam;
import com.example.upc.controller.param.PageQuery;
import com.example.upc.controller.param.PageResult;
import com.example.upc.controller.searchParam.LeaveSearchParam;
import com.example.upc.dataobject.FormatLeaveSample;
import com.example.upc.dataobject.SysUser;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface FormatLeaveService {
    PageResult<FormatLeaveSupParam> getPage(PageQuery pageQuery, LeaveSearchParam leaveSearchParam);
    PageResult<FormatLeaveSupParam> getPageAdmin(PageQuery pageQuery, LeaveSearchParam leaveSearchParam);
    FormatLeaveParam getById(int id);
    PageResult<FormatLeaveSample> getPageEnterprise (PageQuery pageQuery, Integer id, LeaveSearchParam leaveSearchParam);
    void insert(String json, SysUser sysUser);
    void delete(int fpId);
    void update(String json,SysUser sysUser);
    void fail();
    void importExcel(MultipartFile file, Integer type, SysUser sysUser);
    void importExcelAdmin(MultipartFile file, Integer type, SysUser sysUser);
    /**
     * 小程序专用service
     */
    List<Object> getFoodSamplesRecord(int enterpriseId, Date startDate);
//    String updateFoodSamplesRecord(Map<String,Object> updateData);
}
