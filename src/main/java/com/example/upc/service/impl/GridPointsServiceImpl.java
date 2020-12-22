package com.example.upc.service.impl;

import com.example.upc.common.BusinessException;
import com.example.upc.common.EmBusinessError;
import com.example.upc.controller.param.GridPoints1;
import com.example.upc.controller.param.SmilePoints;
import com.example.upc.controller.param.enterpriseId;
import com.example.upc.controller.searchParam.EnterpriseSearchParam;
import com.example.upc.dao.*;
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
    @Autowired
    private ActionJournalMapper actionJournalMapper;

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
    public List<SmilePoints> getSmileMapPointsPhone(EnterpriseSearchParam enterpriseSearchParam){
        if (enterpriseSearchParam.getDis() == null||enterpriseSearchParam.getDis().equals("")) {
            enterpriseSearchParam.setDis(1000);
        }
        if (enterpriseSearchParam.getLocation() == null||enterpriseSearchParam.getLocation().equals("")){
            enterpriseSearchParam.setLocation("118.5821878900,37.4489563700");
        }
        String[] gps = enterpriseSearchParam.getLocation().split(",");
        Double gpsA =Double.parseDouble(gps[0]);
        Double gpsB =Double.parseDouble(gps[1]);
        Float gps1 = (float) (gpsA - enterpriseSearchParam.getDis() * 0.00001141);
        Float gps2 = (float) (gpsA + enterpriseSearchParam.getDis() * 0.00001141);
        Float gps3 = (float) (gpsB - enterpriseSearchParam.getDis() * 0.00000899);
        Float gps4 = (float) (gpsB + enterpriseSearchParam.getDis() * 0.00000899);
        List<SmilePoints> smilePointsList = gridPointsMapper.getSmileAllPhone(enterpriseSearchParam,gps1,gps2,gps3,gps4);
        return smilePointsList;
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
            EnterpriseSearchParam foodBusinessEnterpriseSearchParam = new EnterpriseSearchParam();//建立
            List<String> fooBusinessList = new ArrayList<>();
            fooBusinessList.add("foodBusiness");
            foodBusinessEnterpriseSearchParam.setAreaList(areaIdList);
            foodBusinessEnterpriseSearchParam.setIndustryList(fooBusinessList);

            EnterpriseSearchParam smallCaterEnterpriseSearchParam = new EnterpriseSearchParam();//建立
            List<String> smallCaterList = new ArrayList<>();
            smallCaterList.add("smallCater");
            smallCaterEnterpriseSearchParam.setAreaList(areaIdList);
            smallCaterEnterpriseSearchParam.setIndustryList(smallCaterList);

            EnterpriseSearchParam smallWorkshopEnterpriseSearchParam = new EnterpriseSearchParam();//建立
            List<String> smallWorkshopList = new ArrayList<>();
            smallWorkshopList.add("smallWorkshop");
            smallWorkshopEnterpriseSearchParam.setAreaList(areaIdList);
            smallWorkshopEnterpriseSearchParam.setIndustryList(smallWorkshopList);

            EnterpriseSearchParam foodProduceEnterpriseSearchParam = new EnterpriseSearchParam();
            List<String> foodProduceList = new ArrayList<>();
            foodProduceList.add("foodProduce");
            foodProduceEnterpriseSearchParam.setAreaList(areaIdList);
            foodProduceEnterpriseSearchParam.setIndustryList(foodProduceList);

            EnterpriseSearchParam drugsBusinessEnterpriseSearchParam = new EnterpriseSearchParam();
            List<String> drugsBusinessList = new ArrayList<>();
            drugsBusinessList.add("drugsBusiness");
            drugsBusinessEnterpriseSearchParam.setAreaList(areaIdList);
            drugsBusinessEnterpriseSearchParam.setIndustryList(drugsBusinessList);

            EnterpriseSearchParam drugsProduceEnterpriseSearchParam = new EnterpriseSearchParam();
            List<String> drugsProduceList = new ArrayList<>();
            drugsProduceList.add("drugsProduce");
            drugsProduceEnterpriseSearchParam.setAreaList(areaIdList);
            drugsProduceEnterpriseSearchParam.setIndustryList(drugsProduceList);


            EnterpriseSearchParam cosmeticsUseEnterpriseSearchParam = new EnterpriseSearchParam();
            List<String> cosmeticsUseList = new ArrayList<>();
            cosmeticsUseList.add("cosmeticsUse");
            cosmeticsUseEnterpriseSearchParam.setAreaList(areaIdList);
            cosmeticsUseEnterpriseSearchParam.setIndustryList(cosmeticsUseList);

            EnterpriseSearchParam medicalProduceEnterpriseSearchParam = new EnterpriseSearchParam();
            List<String> medicalProduceList = new ArrayList<>();
            medicalProduceList.add("medicalProduce");
            medicalProduceEnterpriseSearchParam.setAreaList(areaIdList);
            medicalProduceEnterpriseSearchParam.setIndustryList(medicalProduceList);

            EnterpriseSearchParam medicalBusinessEnterpriseSearchParam = new EnterpriseSearchParam();
            List<String> medicalBusinessList = new ArrayList<>();
            medicalBusinessList.add("medicalBusiness");
            medicalBusinessEnterpriseSearchParam.setAreaList(areaIdList);
            medicalBusinessEnterpriseSearchParam.setIndustryList(medicalBusinessList);

            EnterpriseSearchParam industrialProductsEnterpriseSearchParam = new EnterpriseSearchParam();
            List<String> industrialProductsList = new ArrayList<>();
            industrialProductsList.add("industrialProducts");
            industrialProductsEnterpriseSearchParam.setAreaList(areaIdList);
            industrialProductsEnterpriseSearchParam.setIndustryList(industrialProductsList);

            MapIndustryNumber mapIndustryNumber = new MapIndustryNumber();
            mapIndustryNumber.setFoodBusiness(supervisionEnterpriseMapper.countList(foodBusinessEnterpriseSearchParam));
            mapIndustryNumber.setSmallCater(supervisionEnterpriseMapper.countList(smallCaterEnterpriseSearchParam));
            mapIndustryNumber.setSmallWorkshop(supervisionEnterpriseMapper.countList(smallWorkshopEnterpriseSearchParam));
            mapIndustryNumber.setFoodProduce(supervisionEnterpriseMapper.countList(foodProduceEnterpriseSearchParam));
            mapIndustryNumber.setDrugsBusiness(supervisionEnterpriseMapper.countList(drugsBusinessEnterpriseSearchParam));
            mapIndustryNumber.setDrugsProduce(supervisionEnterpriseMapper.countList(drugsProduceEnterpriseSearchParam));
            mapIndustryNumber.setCosmeticsUse(supervisionEnterpriseMapper.countList(cosmeticsUseEnterpriseSearchParam));
            mapIndustryNumber.setMedicalBusiness(supervisionEnterpriseMapper.countList(medicalBusinessEnterpriseSearchParam));
            mapIndustryNumber.setMedicalProduce(supervisionEnterpriseMapper.countList(medicalProduceEnterpriseSearchParam));
            mapIndustryNumber.setIndustrialProducts(supervisionEnterpriseMapper.countList(industrialProductsEnterpriseSearchParam));
            areaCount.put(sysArea.getId(),mapIndustryNumber);//向map中存放地区id和当前地区的6个产业类型中企业的数量
        }
        EnterpriseSearchParam enterpriseSearchParam = new EnterpriseSearchParam();
        enterpriseSearchParam.setAreaList(areaIntegers);//放置areaId的list
        List<String> industryList = new ArrayList<>();
        industryList.add("foodBusiness");
        industryList.add("smallCater");
        industryList.add("smallWorkshop");
        industryList.add("foodProduce");
        industryList.add("drugsBusiness");
        industryList.add("drugsProduce");
        industryList.add("cosmeticsUse");
        industryList.add("medicalProduce");
        industryList.add("medicalBusiness");
        industryList.add("industrialProducts");
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
            supervisionEnterprise.setBusinessState(2);
            supervisionEnterpriseMapper.updateByPrimaryKeySelective(supervisionEnterprise);
            GridPointsGps gridPointsGps = gridPointsGpsMapper.getPointByCodeId(code);
            if (gridPointsGps != null){
                gridPointsGps.setPoint(points);
                gridPointsGps.setOperator(sysUser.getUsername());
                gridPointsGps.setOperatorIp("1.1.1.1");
                gridPointsGpsMapper.updateByPrimaryKeySelective(gridPointsGps);
                ActionJournal actionJournal = new ActionJournal();
                actionJournal.setPerson(sysUser.getInfoName());
                actionJournal.setTime(new Date());
                actionJournal.setModule("地图点位");
                actionJournalMapper.insertSelective(actionJournal);
            }
            else {
                GridPointsGps gridPointsGps1 = new GridPointsGps();
                gridPointsGps1.setCodeId(code);
                gridPointsGps1.setAreaId(supervisionEnterprise.getArea());
                gridPointsGps1.setPoint(points);
                gridPointsGps1.setOperator(sysUser.getUsername());
                gridPointsGps1.setOperatorIp("1.1.1.1");
                gridPointsGpsMapper.insertSelective(gridPointsGps1);
                ActionJournal actionJournal = new ActionJournal();
                actionJournal.setPerson(sysUser.getInfoName());
                actionJournal.setTime(new Date());
                actionJournal.setModule("地图点位");
                actionJournalMapper.insertSelective(actionJournal);
            }
        }
        else{
            throw new BusinessException(EmBusinessError.UPDATE_ERROR);
        }
    }
}
