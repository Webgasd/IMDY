package com.example.upc.service.impl;

import com.example.upc.common.BusinessException;
import com.example.upc.common.EmBusinessError;
import com.example.upc.controller.param.GridPoints1;
import com.example.upc.controller.param.SmilePoints;
import com.example.upc.controller.param.enterpriseId;
import com.example.upc.controller.searchParam.EnterpriseSearchParam;
import com.example.upc.dao.GridPointsGpsMapper;
import com.example.upc.dao.GridPointsMapper;
import com.example.upc.dao.SupervisionEnterpriseMapper;
import com.example.upc.dao.SysAreaMapper;
import com.example.upc.dataobject.*;
import com.example.upc.service.GridPointsService;
import com.example.upc.service.SysAreaService;
import com.example.upc.service.model.MapIndustryNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zcc
 * @date 2019/8/13 11:12
 */
@Service
public class GridPointsServiceImpl implements GridPointsService {
    @Autowired
    private GridPointsMapper gridPointsMapper;
    @Autowired
    private SysAreaMapper sysAreaMapper;
    @Autowired
    private SupervisionEnterpriseMapper supervisionEnterpriseMapper;
    @Autowired
    private SysAreaService sysAreaService;
    @Autowired
    private GridPointsGpsMapper gridPointsGpsMapper;

    @Override
    public List<GridPoints> getAll() {
        return gridPointsMapper.getAll();
    }
    @Override
    public List<GridPoints1> getAll1() {
        return gridPointsMapper.getAll1();
    }

    @Override
    public List<GridPoints> getByAreaId(int id) {
        return gridPointsMapper.getByAreaId(id);
    }
    @Override
    public int checkPoint(int id){
        return gridPointsMapper.checkPoint(id);
    }
    @Override
    public int updatePoint(GridPoints record){
        return gridPointsMapper.updatePoint(record);
    }
    @Override
    public int insertPoint(GridPoints record){
        return gridPointsMapper.insertSelective(record);
    }
    @Override
    public List<SmilePoints> getSmileMapPoints(EnterpriseSearchParam enterpriseSearchParam){
        return gridPointsMapper.getSmileAll(enterpriseSearchParam);
    }

    @Override
    public  List<enterpriseId> getEnterpriseByName(String name) {
        return gridPointsMapper.getEnterpriseByName(name);
    }
    @Override
    public Map<String, Object> getAreaEnterprise(){
        List<SysArea> sysAreaList = sysAreaMapper.getAllAreaPa();//在地区表中查找备注为区域的记录
        List<Integer> areaIntegers=sysAreaService.getAll().stream().map((sysArea -> sysArea.getId())).collect(Collectors.toList());
        //获取所有区域的id列表
        Map<Integer,Object> areaCount= new HashMap<>();
        for(SysArea sysArea:sysAreaList){//循环备注为区域的list结果
            List<Integer> areaIdList = new ArrayList<>();
            areaIdList.add(sysArea.getId());//循环盛放搜索结果中的id，下面就是建立
            //以此为一组，分别是构建不同的产业类型
            EnterpriseSearchParam foodCommonEnterpriseSearchParam = new EnterpriseSearchParam();//建立
            List<String> fooCommonList = new ArrayList<>();
            fooCommonList.add("foodCommon");
            foodCommonEnterpriseSearchParam.setAreaList(areaIdList);
            foodCommonEnterpriseSearchParam.setIndustryList(fooCommonList);

            EnterpriseSearchParam foodCirculateEnterpriseSearchParam = new EnterpriseSearchParam();
            List<String> foodCirculateList = new ArrayList<>();
            foodCirculateList.add("foodCirculate");
            foodCirculateEnterpriseSearchParam.setAreaList(areaIdList);
            foodCirculateEnterpriseSearchParam.setIndustryList(foodCirculateList);

            EnterpriseSearchParam foodProduceEnterpriseSearchParam = new EnterpriseSearchParam();
            List<String> foodProduceList = new ArrayList<>();
            foodProduceList.add("foodProduce");
            foodProduceEnterpriseSearchParam.setAreaList(areaIdList);
            foodProduceEnterpriseSearchParam.setIndustryList(foodProduceList);

            EnterpriseSearchParam cosmeticsUseEnterpriseSearchParam = new EnterpriseSearchParam();
            List<String> cosmeticsUseList = new ArrayList<>();
            cosmeticsUseList.add("cosmeticsUse");
            cosmeticsUseEnterpriseSearchParam.setAreaList(areaIdList);
            cosmeticsUseEnterpriseSearchParam.setIndustryList(cosmeticsUseList);

            EnterpriseSearchParam drugsBusinessEnterpriseSearchParam = new EnterpriseSearchParam();
            List<String> drugsBusinessList = new ArrayList<>();
            drugsBusinessList.add("drugsBusiness");
            drugsBusinessEnterpriseSearchParam.setAreaList(areaIdList);
            drugsBusinessEnterpriseSearchParam.setIndustryList(drugsBusinessList);

            EnterpriseSearchParam medicalUseEnterpriseSearchParam = new EnterpriseSearchParam();
            List<String> medicalUseList = new ArrayList<>();
            medicalUseList.add("medicalUse");
            medicalUseEnterpriseSearchParam.setAreaList(areaIdList);
            medicalUseEnterpriseSearchParam.setIndustryList(medicalUseList);

            MapIndustryNumber mapIndustryNumber = new MapIndustryNumber();
            mapIndustryNumber.setFoodCommon(supervisionEnterpriseMapper.countList(foodCommonEnterpriseSearchParam));
            mapIndustryNumber.setFoodCirculate(supervisionEnterpriseMapper.countList(foodCirculateEnterpriseSearchParam));
            mapIndustryNumber.setFoodProduce(supervisionEnterpriseMapper.countList(foodProduceEnterpriseSearchParam));
            mapIndustryNumber.setCosmeticsUse(supervisionEnterpriseMapper.countList(cosmeticsUseEnterpriseSearchParam));
            mapIndustryNumber.setDrugsBusiness(supervisionEnterpriseMapper.countList(drugsBusinessEnterpriseSearchParam));
            mapIndustryNumber.setMedicalUse(supervisionEnterpriseMapper.countList(medicalUseEnterpriseSearchParam));
            areaCount.put(sysArea.getId(),mapIndustryNumber);//向map中存放地区id和当前地区的6个产业类型中企业的数量
        }
        EnterpriseSearchParam enterpriseSearchParam = new EnterpriseSearchParam();
        enterpriseSearchParam.setAreaList(areaIntegers);//放置areaId的list
        List<String> industryList = new ArrayList<>();
        industryList.add("foodCommon");
        industryList.add("foodCirculate");
        industryList.add("foodProduce");
        industryList.add("cosmeticsUse");
        industryList.add("drugsBusiness");
        industryList.add("medicalUse");
        enterpriseSearchParam.setIndustryList(industryList);//放置产业类型的list
        //抛出map
        Map<String,Object> map = new HashMap<>();
        map.put("areaCount",areaCount);//包含1、所有地区以及每个地区下的6大类产业的企业数量
        map.put("allCount",supervisionEnterpriseMapper.countList(enterpriseSearchParam));//2、所有企业的数量
        map.put("areaList",sysAreaList);//所有地区
        return map;
    }
    @Override
    @Transactional
    public int deleteByEnterpriseId(int id){
        return gridPointsMapper.deleteByEnterpriseId(id);
    }

    @Override
    public GridPoints getPointByEnterpriseId(Integer id) {
        SupervisionEnterprise supervisionEnterprise = supervisionEnterpriseMapper.selectByPrimaryKey(id);
            if (supervisionEnterprise.getGpsFlag() == 0) {
                GridPoints gridPoints = gridPointsMapper.getPointByEnterpriseId(id);
                return gridPoints;
            }
            else if (supervisionEnterprise.getGpsFlag() == 1) {
                GridPointsGps gridPointsGps = gridPointsGpsMapper.getPointByCodeId(supervisionEnterprise.getIdNumber());
                GridPoints gridPoints1 = new GridPoints();
                gridPoints1.setEnterpriseId(supervisionEnterprise.getId());
                gridPoints1.setAreaId(supervisionEnterprise.getArea());
                gridPoints1.setPoint(gridPointsGps.getPoint());
                return gridPoints1;
            }
            else {
                throw new BusinessException(EmBusinessError.UPDATE_ERROR);
            }
    }

    @Override
    public int getVideoIdByEnterprise(int id) {
        return gridPointsMapper.getVideoIdByEnterprise(id);
    }

    @Override
    public void updateEnterprisePoint(int id, String code, String points, SysUser sysUser) {
        SupervisionEnterprise supervisionEnterprise = supervisionEnterpriseMapper.selectByPrimaryKey(id);
        if (supervisionEnterprise != null){
            supervisionEnterprise.setGpsFlag(1);
            supervisionEnterpriseMapper.updateByPrimaryKeySelective(supervisionEnterprise);
            GridPointsGps gridPointsGps = gridPointsGpsMapper.getPointByCodeId(code);
            if (gridPointsGps != null){
                gridPointsGps.setPoint(points);
                gridPointsGps.setOperator(sysUser.getUsername());
                gridPointsGps.setOperatorIp("1.1.1.1");
                gridPointsGpsMapper.updateByPrimaryKeySelective(gridPointsGps);
            }
            else {
                GridPointsGps gridPointsGps1 = new GridPointsGps();
                gridPointsGps1.setCodeId(code);
                gridPointsGps1.setAreaId(supervisionEnterprise.getArea());
                gridPointsGps1.setPoint(points);
                gridPointsGps1.setOperator(sysUser.getUsername());
                gridPointsGps1.setOperatorIp("1.1.1.1");
                gridPointsGpsMapper.insertSelective(gridPointsGps1);
            }
        }
        else{
            throw new BusinessException(EmBusinessError.UPDATE_ERROR);
        }
    }
}
