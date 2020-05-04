package com.example.upc.service;

import com.example.upc.controller.param.*;
import com.example.upc.controller.searchParam.EnterpriseSearchParam;
import com.example.upc.dataobject.SupervisionEnterprise;
import com.example.upc.dataobject.SysIndustry;
import com.example.upc.dataobject.SysUser;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author zcc
 * @date 2019/4/25 11:00
 */
public interface SupervisionEnterpriseService {
   PageResult<EnterpriseListResult> getPage(PageQuery pageQuery, EnterpriseSearchParam enterpriseSearchParam,SysUser sysUser,Integer areaId,boolean searchIndustry);
   PageResult<EnterpriseListResult> getPageState(PageQuery pageQuery, EnterpriseSearchParam enterpriseSearchParam,SysUser sysUser,Integer areaId,boolean searchIndustry);
   PageResult<EnterpriseListResult> getPageNoUser(PageQuery pageQuery, EnterpriseSearchParam enterpriseSearchParam,Integer areaId,boolean searchIndustry);
   EnterpriseParam getById(int id);
   SupervisionEnterprise selectById(int id);
   void insert(String json, SysUser sysUser);
   void update(String json, SysUser sysUser);
   void delete(int id);
   void changeStop(int id);
   void changeNormal(Integer id);
   void changeAbnormal(Integer id, Integer abId, String content);
   Map<Integer,Integer> getStatistics(List<SysIndustry> sysIndustryList, List<Integer> sysAreaList,String supervisor);
   //void importExcel(MultipartFile file, Integer type);
   PageResult<EnterpriseListResult> getPageByEnterpriseId(int id);
}
