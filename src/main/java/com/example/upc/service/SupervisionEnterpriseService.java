package com.example.upc.service;

import com.alibaba.fastjson.JSONObject;
import com.example.upc.controller.param.*;
import com.example.upc.controller.searchParam.EnterpriseSearchParam;
import com.example.upc.dataobject.SupervisionEnterprise;
import com.example.upc.dataobject.SysIndustry;
import com.example.upc.dataobject.SysUser;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @author zcc
 * @date 2019/4/25 11:00
 */
public interface SupervisionEnterpriseService {
   SmilePointsParam getSmileMapPoints(EnterpriseSearchParam enterpriseSearchParam);
   Map<String,Integer> getCountPhone(EnterpriseSearchParam enterpriseSearchParam,SysUser sysUser,Integer areaId,boolean searchIndustry);
   EnterpriseCountParam getCount(EnterpriseSearchParam enterpriseSearchParam,SysUser sysUser,Integer areaId,boolean searchIndustry);
   PageResult<EnterpriseListResult> getPage(PageQuery pageQuery, EnterpriseSearchParam enterpriseSearchParam,SysUser sysUser,Integer areaId,boolean searchIndustry);
   PageResult<EnterpriseListResult> getPageState(PageQuery pageQuery, EnterpriseSearchParam enterpriseSearchParam,SysUser sysUser,Integer areaId,boolean searchIndustry);
   PageResult<EnterpriseListResult> getPageNoUser(PageQuery pageQuery, EnterpriseSearchParam enterpriseSearchParam,Integer areaId,boolean searchIndustry);
   EnterpriseParam getById(int id) throws ParseException;
   SupervisionEnterprise selectById(int id);
   void insert(String json, SysUser sysUser);
   void update(String json, SysUser sysUser);
   void delete(int id);
   void changeStop(int id);
   void changeNormal(Integer id, Integer bId);
   void changeAbnormal(Integer id, Integer abId, String content);
   Map<Integer,Integer> getStatistics(List<SysIndustry> sysIndustryList, List<Integer> sysAreaList,String supervisor);
   JSONObject importExcel(MultipartFile file, Integer type);
   PageResult<EnterpriseListResult> getPageByEnterpriseId(int id);
   void changeGpsFlag();
   /**
    * 小程序专用service
    */
   Map<String,Object> getFoodBusinessLicenseById(int id);   //小程序获取企业许可信息
   Map<String, Object> getLicensePhotosById(int id);  //小程序获取证照图片
   Map<String, Object> updateLicensePhotosById(int enterpriseId,String businessLicensePhoto,String foodBusinessPhoto);   //小程序更新证照图片
   void importInspectExcel(MultipartFile file) throws IOException; //导入100家企业点位
  //获取所有gps
   List<EnterpriseInfoParam> getEnterpriseInfo(EnterpriseSearchParam enterpriseSearchParam);

   List<EnterpriseInfoParam> getEnterpriseInfoByDate(EnterpriseSearchParam enterpriseSearchParam);

    void updateBaseEnterpriseInfo(String json, SysUser sysUser);
}
