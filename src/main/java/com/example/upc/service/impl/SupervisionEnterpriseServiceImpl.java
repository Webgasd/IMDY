package com.example.upc.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.upc.common.BusinessException;
import com.example.upc.common.EmBusinessError;
import com.example.upc.common.ValidationResult;
import com.example.upc.common.ValidatorImpl;
import com.example.upc.controller.param.*;
import com.example.upc.controller.searchParam.EnterpriseSearchParam;
import com.example.upc.dao.*;
import com.example.upc.dataobject.*;
import com.example.upc.service.*;
import com.example.upc.util.*;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zcc
 * @date 2019/4/25 11:01
 */
@Service
public class SupervisionEnterpriseServiceImpl implements SupervisionEnterpriseService {

    @Autowired
    private SupervisionEnterpriseMapper supervisionEnterpriseMapper;
    @Autowired
    private SupervisionEnMedicalProMapper supervisionEnMedicalProMapper;
    @Autowired
    private SupervisionEnMedicalProIndexMapper supervisionEnMedicalProIndexMapper;
    @Autowired
    private SupervisionEnMedicalBuMapper supervisionEnMedicalBuMapper;
    @Autowired
    private SupervisionEnMedicalBuIndexMapper supervisionEnMedicalBuIndexMapper;
    @Autowired
    private SupervisionEnFoodProMapper supervisionEnFoodProMapper;
    @Autowired
    private SupervisionEnFoodProIndexMapper supervisionEnFoodProIndexMapper;
    @Autowired
    private SupervisionEnDrugsBuMapper supervisionEnDrugsBuMapper;
    @Autowired
    private SupervisionEnDrugsBuIndexMapper supervisionEnDrugsBuIndexMapper;
    @Autowired
    private SupervisionEnDrugsProMapper supervisionEnDrugsProMapper;
    @Autowired
    private SupervisionEnDrugsProIndexMapper supervisionEnDrugsProIndexMapper;
    @Autowired
    private SupervisionEnCosmeticsMapper supervisionEnCosmeticsMapper;
    @Autowired
    private SupervisionEnCosmeticsIndexMapper supervisionEnCosmeticsIndexMapper;
    @Autowired
    private SupervisionEnFoodBuMapper supervisionEnFoodBuMapper;
    @Autowired
    private SupervisionEnFoodBuIndexMapper supervisionEnFoodBuIndexMapper;
    @Autowired
    private SupervisionEnProCategoryMapper supervisionEnProCategoryMapper;
    @Autowired
    private SupervisionEnSmallCaterMapper supervisionEnSmallCaterMapper;
    @Autowired
    private SupervisionEnSmallCaterIndexMapper supervisionEnSmallCaterIndexMapper;
    @Autowired
    private SupervisionEnSmallWorkshopMapper supervisionEnSmallWorkshopMapper;
    @Autowired
    private SupervisionEnSmallWorkshopIndexMapper supervisionEnSmallWorkshopIndexMapper;
    @Autowired
    private SupervisionEnIndustrialProductsMapper supervisionEnIndustrialProductsMapper;
    @Autowired
    private SupervisionEnIndustrialProductsIndexMapper supervisionEnIndustrialProductsIndexMapper;
    @Autowired
    private SysDeptMapper sysDeptMapper;
    @Autowired
    private SysAreaMapper sysAreaMapper;
    @Autowired
    private ValidatorImpl validator;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SupervisionGaService supervisionGaService;
    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private SysAreaService sysAreaService;
    @Autowired
    private SysIndustryService sysIndustryService;
    @Autowired
    private SysDeptIndustryService sysDeptIndustryService;
    @Autowired
    private SysDeptAreaService sysDeptAreaService;
    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;
    @Autowired
    private GridPointsGpsMapper gridPointsGpsMapper;
    @Autowired
    private GridPointsMapper gridPointsMapper;

    @Override
    public List<SmilePoints> getSmileMapPoints(EnterpriseSearchParam enterpriseSearchParam){
        CaculateDisUtil caculateDisUtil = new CaculateDisUtil();
        if (enterpriseSearchParam.getLocation() == null||enterpriseSearchParam.getLocation().equals("")){
            enterpriseSearchParam.setLocation("118.5821878900,37.4489563700");
        }
        String[] gps = enterpriseSearchParam.getLocation().split(",");
        Double gpsA =Double.parseDouble(gps[0]);
        Double gpsB =Double.parseDouble(gps[1]);
        List<SmilePoints> SmilePointsList = supervisionEnterpriseMapper.getListPhone(enterpriseSearchParam);
        for(SmilePoints smilePoints:SmilePointsList){
            String[] gpsTarget = smilePoints.getPoint().split(",");
            Double gpsC =Double.parseDouble(gpsTarget[0]);
            Double gpsD =Double.parseDouble(gpsTarget[1]);
            smilePoints.setDistance((int) caculateDisUtil.Distance(gpsA, gpsB, gpsC, gpsD));
        }
        List<SmilePoints> afterSmilePointsPhoneList  = ListSortUtil.sort(SmilePointsList,"distance",null);
        afterSmilePointsPhoneList = ListSubUtil.sub(afterSmilePointsPhoneList,enterpriseSearchParam.getIndexNum());
        return afterSmilePointsPhoneList;
    }

    @Override
    public Map<String,Integer> getCountPhone(EnterpriseSearchParam enterpriseSearchParam,SysUser sysUser,Integer areaId,boolean searchIndustry){
        if (sysUser.getUserType()==0){
            if(areaId==null){
                enterpriseSearchParam.setAreaList(sysAreaService.getAll().stream().map((sysArea -> sysArea.getId())).collect(Collectors.toList()));
            }else {
                enterpriseSearchParam.setAreaList(sysDeptAreaService.getIdListSearch(areaId));
            }
            if(searchIndustry){
                enterpriseSearchParam.setIndustryList(sysIndustryService.getAll().stream().map((sysIndustry -> sysIndustry.getRemark())).collect(Collectors.toList()));
            }
        }else if(sysUser.getUserType()==2){
            SupervisionGa supervisionGa = supervisionGaService.getById(sysUser.getInfoId());
            if(areaId==null){
                enterpriseSearchParam.setAreaList(sysDeptAreaService.getIdListByDeptId(supervisionGa.getDepartment()));
            }else {
                enterpriseSearchParam.setAreaList(sysDeptAreaService.getIdListSearch(areaId));
            }
            if(searchIndustry){
                enterpriseSearchParam.setIndustryList(sysDeptIndustryService.getListByDeptId(supervisionGa.getDepartment()).stream().map((sysIndustry -> sysIndustry.getRemark())).collect(Collectors.toList()));
            }
            SysDept sysDept = sysDeptService.getById(supervisionGa.getDepartment());
            if(sysDept.getType()==2){
                if(supervisionGa.getType()!=2){
                    enterpriseSearchParam.setSupervisor(supervisionGa.getName());
                }
            }
        }else{
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"非法用户");
        }
        Map<String,Integer> map = new HashMap<>();
        map.put("总数",supervisionEnterpriseMapper.countListPhone(enterpriseSearchParam));
        enterpriseSearchParam.setOperationMode("公司");
        map.put("公司",supervisionEnterpriseMapper.countListPhone(enterpriseSearchParam));
        enterpriseSearchParam.setOperationMode("个体");
        map.put("个体",supervisionEnterpriseMapper.countListPhone(enterpriseSearchParam));
        enterpriseSearchParam.setOperationMode("合作社");
        map.put("合作社",supervisionEnterpriseMapper.countListPhone(enterpriseSearchParam));
        enterpriseSearchParam.setOperationMode("其他");
        map.put("其他",supervisionEnterpriseMapper.countListPhone(enterpriseSearchParam));
        return map;
    }

    @Override
    public EnterpriseCountParam getCount(EnterpriseSearchParam enterpriseSearchParam,SysUser sysUser,Integer areaId,boolean searchIndustry){
        if (sysUser.getUserType()==0){
            if(areaId==null){
                enterpriseSearchParam.setAreaList(sysAreaService.getAll().stream().map((sysArea -> sysArea.getId())).collect(Collectors.toList()));
            }else {
                enterpriseSearchParam.setAreaList(sysDeptAreaService.getIdListSearch(areaId));
            }
            if(searchIndustry){
                enterpriseSearchParam.setIndustryList(sysIndustryService.getAll().stream().map((sysIndustry -> sysIndustry.getRemark())).collect(Collectors.toList()));
            }
        }else if(sysUser.getUserType()==2){
            SupervisionGa supervisionGa = supervisionGaService.getById(sysUser.getInfoId());
            if(areaId==null){
                enterpriseSearchParam.setAreaList(sysDeptAreaService.getIdListByDeptId(supervisionGa.getDepartment()));
            }else {
                enterpriseSearchParam.setAreaList(sysDeptAreaService.getIdListSearch(areaId));
            }
            if(searchIndustry){
                enterpriseSearchParam.setIndustryList(sysDeptIndustryService.getListByDeptId(supervisionGa.getDepartment()).stream().map((sysIndustry -> sysIndustry.getRemark())).collect(Collectors.toList()));
            }
            SysDept sysDept = sysDeptService.getById(supervisionGa.getDepartment());
            if(sysDept.getType()==2){
                if(supervisionGa.getType()!=2){
                    enterpriseSearchParam.setSupervisor(supervisionGa.getName());
                }
            }
        }else{
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"非法用户");
        }
        EnterpriseCountParam enterpriseCountParam = new EnterpriseCountParam();
            enterpriseSearchParam.setOperationMode("公司");
            enterpriseCountParam.setEnterprise(supervisionEnterpriseMapper.countListPC(enterpriseSearchParam));
            enterpriseSearchParam.setOperationMode("个体");
            enterpriseCountParam.setIndividual(supervisionEnterpriseMapper.countListPC(enterpriseSearchParam));
            enterpriseSearchParam.setOperationMode("合作社");
            enterpriseCountParam.setCooperation(supervisionEnterpriseMapper.countListPC(enterpriseSearchParam));
            enterpriseSearchParam.setOperationMode("其他");
            enterpriseCountParam.setOthers(supervisionEnterpriseMapper.countListPC(enterpriseSearchParam));

        if (enterpriseSearchParam.getIndexNum()==1) {
            List<String> industryList1 = new ArrayList<>();
            industryList1.clear();
            industryList1.add("foodBusiness");
            enterpriseSearchParam.setIndustryList(industryList1);
            enterpriseCountParam.setFoodBu(supervisionEnterpriseMapper.countListPCHave(enterpriseSearchParam));
            industryList1.clear();
            industryList1.add("smallCater");
            enterpriseSearchParam.setIndustryList(industryList1);
            enterpriseCountParam.setSmallCater(supervisionEnterpriseMapper.countListPCHave(enterpriseSearchParam));
            industryList1.clear();
            industryList1.add("smallWorkshop");
            enterpriseSearchParam.setIndustryList(industryList1);
            enterpriseCountParam.setSmallWorkshop(supervisionEnterpriseMapper.countListPCHave(enterpriseSearchParam));
            industryList1.clear();
            industryList1.add("foodProduce");
            enterpriseSearchParam.setIndustryList(industryList1);
            enterpriseCountParam.setFoodPro(supervisionEnterpriseMapper.countListPCHave(enterpriseSearchParam));
            industryList1.clear();
            industryList1.add("drugsBusiness");
            enterpriseSearchParam.setIndustryList(industryList1);
            enterpriseCountParam.setDrugsBu(supervisionEnterpriseMapper.countListPCHave(enterpriseSearchParam));
            industryList1.clear();
            industryList1.add("drugsProduce");
            enterpriseSearchParam.setIndustryList(industryList1);
            enterpriseCountParam.setDrugsPro(supervisionEnterpriseMapper.countListPCHave(enterpriseSearchParam));
            industryList1.clear();
            industryList1.add("cosmeticsUse");
            enterpriseSearchParam.setIndustryList(industryList1);
            enterpriseCountParam.setCosmeticsUse(supervisionEnterpriseMapper.countListPCHave(enterpriseSearchParam));
            industryList1.clear();
            industryList1.add("medicalProduce");
            enterpriseSearchParam.setIndustryList(industryList1);
            enterpriseCountParam.setMedicalPro(supervisionEnterpriseMapper.countListPCHave(enterpriseSearchParam));
            industryList1.clear();
            industryList1.add("medicalBusiness");
            enterpriseSearchParam.setIndustryList(industryList1);
            enterpriseCountParam.setMedicalBu(supervisionEnterpriseMapper.countListPCHave(enterpriseSearchParam));
            industryList1.clear();
            industryList1.add("industrialProducts");
            enterpriseSearchParam.setIndustryList(industryList1);
            enterpriseCountParam.setIndustrialProducts(supervisionEnterpriseMapper.countListPCHave(enterpriseSearchParam));
            enterpriseCountParam.setNone(supervisionEnterpriseMapper.countListPCNone(enterpriseSearchParam));
        }
        return enterpriseCountParam;
    }



    @Override
    public PageResult<EnterpriseListResult> getPage(PageQuery pageQuery, EnterpriseSearchParam enterpriseSearchParam,SysUser sysUser,Integer areaId,boolean searchIndustry) {
        if (sysUser.getUserType()==0){
            if(areaId==null){
                enterpriseSearchParam.setAreaList(sysAreaService.getAll().stream().map((sysArea -> sysArea.getId())).collect(Collectors.toList()));
            }else {
                enterpriseSearchParam.setAreaList(sysDeptAreaService.getIdListSearch(areaId));
            }
            if(searchIndustry){
                enterpriseSearchParam.setIndustryList(sysIndustryService.getAll().stream().map((sysIndustry -> sysIndustry.getRemark())).collect(Collectors.toList()));
            }
            enterpriseSearchParam.setUserType("admin");
        }else if(sysUser.getUserType()==2){
            SupervisionGa supervisionGa = supervisionGaService.getById(sysUser.getInfoId());
            if(areaId==null){
                enterpriseSearchParam.setAreaList(sysDeptAreaService.getIdListByDeptId(supervisionGa.getDepartment()));
            }else {
                enterpriseSearchParam.setAreaList(sysDeptAreaService.getIdListSearch(areaId));
            }
            if(searchIndustry){
                enterpriseSearchParam.setIndustryList(sysDeptIndustryService.getListByDeptId(supervisionGa.getDepartment()).stream().map((sysIndustry -> sysIndustry.getRemark())).collect(Collectors.toList()));
            }
            SysDept sysDept = sysDeptService.getById(supervisionGa.getDepartment());
            if(sysDept.getType()==2){
                if(supervisionGa.getType()!=2){
                    enterpriseSearchParam.setSupervisor(supervisionGa.getName());
                }
            }
        }else{
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"非法用户");
        }
        int count= supervisionEnterpriseMapper.countList(enterpriseSearchParam);
        if (count > 0) {
            List<EnterpriseListResult> enterpriseList = supervisionEnterpriseMapper.getPage(pageQuery,enterpriseSearchParam);
            PageResult<EnterpriseListResult> pageResult = new PageResult<>();
            pageResult.setData(enterpriseList);
            pageResult.setTotal(count);
            pageResult.setPageNo(pageQuery.getPageNo());
            pageResult.setPageSize(pageQuery.getPageSize());
            return pageResult;
        }
        PageResult<EnterpriseListResult> pageResult = new PageResult<>();
        return pageResult;
    }

    @Override
    public PageResult<EnterpriseListResult> getPageState(PageQuery pageQuery, EnterpriseSearchParam enterpriseSearchParam,SysUser sysUser,Integer areaId,boolean searchIndustry) {
        if (sysUser.getUserType()==0){
            if(areaId==null){
                enterpriseSearchParam.setAreaList(sysAreaService.getAll().stream().map((sysArea -> sysArea.getId())).collect(Collectors.toList()));
            }else {
                enterpriseSearchParam.setAreaList(sysDeptAreaService.getIdListSearch(areaId));
            }
            if(searchIndustry){
                enterpriseSearchParam.setIndustryList(sysIndustryService.getAll().stream().map((sysIndustry -> sysIndustry.getRemark())).collect(Collectors.toList()));
            }
            enterpriseSearchParam.setUserType("admin");
        }else if(sysUser.getUserType()==2){
            SupervisionGa supervisionGa = supervisionGaService.getById(sysUser.getInfoId());
            if(areaId==null){
                enterpriseSearchParam.setAreaList(sysDeptAreaService.getIdListByDeptId(supervisionGa.getDepartment()));
            }else {
                enterpriseSearchParam.setAreaList(sysDeptAreaService.getIdListSearch(areaId));
            }
            if(searchIndustry){
                enterpriseSearchParam.setIndustryList(sysDeptIndustryService.getListByDeptId(supervisionGa.getDepartment()).stream().map((sysIndustry -> sysIndustry.getRemark())).collect(Collectors.toList()));
            }
            SysDept sysDept = sysDeptService.getById(supervisionGa.getDepartment());
            if(sysDept.getType()==2){
                if(supervisionGa.getType()!=2){
                    enterpriseSearchParam.setSupervisor(supervisionGa.getName());
                }
            }
        }else{
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"非法用户");
        }
        int count= supervisionEnterpriseMapper.countListState(enterpriseSearchParam);
        if (count > 0) {
            List<EnterpriseListResult> enterpriseList = supervisionEnterpriseMapper.getPageState(pageQuery,enterpriseSearchParam);
            PageResult<EnterpriseListResult> pageResult = new PageResult<>();
            pageResult.setData(enterpriseList);
            pageResult.setTotal(count);
            pageResult.setPageNo(pageQuery.getPageNo());
            pageResult.setPageSize(pageQuery.getPageSize());
            return pageResult;
        }
        PageResult<EnterpriseListResult> pageResult = new PageResult<>();
        return pageResult;
    }

    @Override
    public PageResult<EnterpriseListResult> getPageNoUser(PageQuery pageQuery, EnterpriseSearchParam enterpriseSearchParam, Integer areaId, boolean searchIndustry) {
        if(areaId==null){
            enterpriseSearchParam.setAreaList(sysAreaService.getAll().stream().map((sysArea -> sysArea.getId())).collect(Collectors.toList()));
        }else {
            enterpriseSearchParam.setAreaList(sysDeptAreaService.getIdListSearch(areaId));
        }
        if(searchIndustry){
            enterpriseSearchParam.setIndustryList(sysIndustryService.getAll().stream().map((sysIndustry -> sysIndustry.getRemark())).collect(Collectors.toList()));
        }
        int count= supervisionEnterpriseMapper.countList(enterpriseSearchParam);
        if (count > 0) {
            List<EnterpriseListResult> enterpriseList = supervisionEnterpriseMapper.getPage(pageQuery,enterpriseSearchParam);
            PageResult<EnterpriseListResult> pageResult = new PageResult<>();
            pageResult.setData(enterpriseList);
            pageResult.setTotal(count);
            pageResult.setPageNo(pageQuery.getPageNo());
            pageResult.setPageSize(pageQuery.getPageSize());
            return pageResult;
        }
        PageResult<EnterpriseListResult> pageResult = new PageResult<>();
        return pageResult;
    }

//获取某个企业的记录，包括基本信息和许可证，许可证方法这里要改。关联关系表
    @Override
    public EnterpriseParam getById(int id) {
        SupervisionEnterprise supervisionEnterprise= supervisionEnterpriseMapper.selectByPrimaryKey(id);
        if (supervisionEnterprise==null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"无此企业信息");
        }
        EnterpriseParam enterpriseParam = new EnterpriseParam();
        BeanUtils.copyProperties(supervisionEnterprise,enterpriseParam);
        enterpriseParam.setPermissionFamily(supervisionEnterprise.getPermissionType());
        GridPoints gridPoints = gridPointsMapper.getPointByEnterpriseId(id);
        if (gridPoints==null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"无此企业默认定位信息");
        }
        enterpriseParam.setPosition(gridPoints.getPoint());
        if (supervisionEnterprise.getGpsFlag()==0){
                enterpriseParam.setLocation(null);
        }
        if (supervisionEnterprise.getGpsFlag()==1){
            GridPointsGps gridPointsGps = gridPointsGpsMapper.getPointByCodeId(supervisionEnterprise.getIdNumber());
            if (gridPointsGps==null){
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"无此企业更新定位信息");
            }
            else {
                enterpriseParam.setLocation(gridPointsGps.getPoint());
            }
        }
        List<String> permissionType = new ArrayList<>();
        //yes  食品经营
        List<SupervisionEnFoodBu> supervisionEnFoodBuList = supervisionEnFoodBuMapper.getListByEnterpriseId(id);
        if(supervisionEnFoodBuList.size()>0){
            permissionType.add("foodBusiness");
            enterpriseParam.setFoodBusinessList(supervisionEnFoodBuList);
        }
        //yes 食品生产
        List<SupervisionEnFoodPro> supervisionEnFoodProList = supervisionEnFoodProMapper.getListByEnterpriseId(id);
        if (supervisionEnFoodProList.size()>0) {
            permissionType.add("foodProduce");
            List<EnFoodProduceParam> enFoodProduceParamList = new ArrayList<>();//建立list盛放类
            for (SupervisionEnFoodPro supervisionEnFoodPro : supervisionEnFoodProList){
                EnFoodProduceParam enFoodProduceParam = new EnFoodProduceParam();
                BeanUtils.copyProperties(supervisionEnFoodPro,enFoodProduceParam);
                List<SupervisionEnProCategory> list = supervisionEnProCategoryMapper.selectByParentId(supervisionEnFoodPro.getId());
                enFoodProduceParam.setList(list);
                enFoodProduceParamList.add(enFoodProduceParam);
            }
            enterpriseParam.setFoodProduceList(enFoodProduceParamList);
        }
        //yes 药品经营
        List<SupervisionEnDrugsBu> supervisionEnDrugsBuList = supervisionEnDrugsBuMapper.getListByEnterpriseId(id);
        if(supervisionEnDrugsBuList.size()>0){
            permissionType.add("drugsBusiness");
            enterpriseParam.setDrugsBusinessList(supervisionEnDrugsBuList);
        }
        //yes 药品生产
        List<SupervisionEnDrugsPro> supervisionEnDrugsProList = supervisionEnDrugsProMapper.getListByEnterpriseId(id);
        if(supervisionEnDrugsProList.size()>0){
            permissionType.add("drugsProduce");
            enterpriseParam.setDrugsProduceList(supervisionEnDrugsProList);
        }
        //yes,化妆品
        List<SupervisionEnCosmetics> supervisionEnCosmeticsList = supervisionEnCosmeticsMapper.getListByEnterpriseId(id);
        if(supervisionEnCosmeticsList.size()>0){
            permissionType.add("cosmeticsUse");
            enterpriseParam.setCosmeticsList(supervisionEnCosmeticsList);
        }
        //yes 医疗器械生产
        List<SupervisionEnMedicalPro> supervisionEnMedicalProList = supervisionEnMedicalProMapper.getListByEnterpriseId(id);
        if(supervisionEnMedicalProList.size()>0){
            permissionType.add("medicalProduce");
            enterpriseParam.setMedicalProduceList(supervisionEnMedicalProList);
        }
        //yes 医疗器械使用
        List<SupervisionEnMedicalBu> supervisionEnMedicalBuList = supervisionEnMedicalBuMapper.getListByEnterpriseId(id);
        if(supervisionEnMedicalBuList.size()>0){
            permissionType.add("medicalBusiness");
            enterpriseParam.setMedicalBusinessList(supervisionEnMedicalBuList);
        }
        //yes 小餐饮
        List<SupervisionEnSmallCater> supervisionEnSmallCaterList = supervisionEnSmallCaterMapper.getListByEnterpriseId(id);
        if(supervisionEnSmallCaterList.size()>0){
            permissionType.add("smallCater");
            enterpriseParam.setSmallCaterList(supervisionEnSmallCaterList);
        }
        //yes 小作坊
        List<SupervisionEnSmallWorkshop> supervisionEnSmallWorkshopList = supervisionEnSmallWorkshopMapper.getListByEnterpriseId(id);
        if(supervisionEnSmallWorkshopList.size()>0){
            permissionType.add("smallWorkshop");
            enterpriseParam.setSmallWorkshopList(supervisionEnSmallWorkshopList);
        }
        //yes 工业产品
        List<SupervisionEnIndustrialProducts> supervisionEnIndustrialProductsList = supervisionEnIndustrialProductsMapper.getListByEnterpriseId(id);
        if(supervisionEnIndustrialProductsList.size()>0){
            permissionType.add("industrialProducts");
            enterpriseParam.setIndustrialProductsList(supervisionEnIndustrialProductsList);
        }
        //enterpriseParam.setPermissionType(String.join(",",permissionType));
        return enterpriseParam;
    }

    @Override
    public SupervisionEnterprise selectById(int id) {
        return supervisionEnterpriseMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional
    public void insert(String json, SysUser sysUser) {
        SupervisionEnterprise supervisionEnterprise = JSONObject.parseObject(json,SupervisionEnterprise.class);
        EnterpriseParam enterpriseParam = JSON.parseObject(json,EnterpriseParam.class);
        supervisionEnterprise.setPermissionType(enterpriseParam.getPermissionFamily());
        ValidationResult result = validator.validate(supervisionEnterprise);
        if(result.isHasErrors()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
        }
        if(supervisionEnterpriseMapper.countByIdNumber(supervisionEnterprise.getIdNumber(),supervisionEnterprise.getId())>0){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"社会信用代码被占用");
        }
        supervisionEnterprise.setOperateIp("124.124.124");
        supervisionEnterprise.setOperateTime(new Date());
        supervisionEnterprise.setOperator("zcc");
        if (supervisionEnterprise.getGpsFlag()==1){
            JSONObject jsonResult = JSON.parseObject(json);
            String location = jsonResult.getString("location");
            if(location==null){
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"手动定位点位为空");
            }
            GridPointsGps gridPointsGps = gridPointsGpsMapper.getPointByCodeId(supervisionEnterprise.getIdNumber());
            if (gridPointsGps != null){
                gridPointsGps.setPoint(location);
                gridPointsGps.setOperator(sysUser.getUsername());
                gridPointsGps.setOperatorIp("1.1.1.1");
                gridPointsGpsMapper.updateByPrimaryKeySelective(gridPointsGps);
            }
            else {
                GridPointsGps gridPointsGps1 = new GridPointsGps();
                gridPointsGps1.setCodeId(supervisionEnterprise.getIdNumber());
                gridPointsGps1.setAreaId(supervisionEnterprise.getArea());
                gridPointsGps1.setPoint(location);
                gridPointsGps1.setOperator(sysUser.getUsername());
                gridPointsGps1.setOperatorIp("1.1.1.1");
                gridPointsGpsMapper.insertSelective(gridPointsGps1);
            }
        }
        supervisionEnterpriseMapper.insertSelective(supervisionEnterprise);
        if(supervisionEnterprise.getId()==null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"插入失败");
        }
        SysUser sysUser1 = new SysUser();//同时进行用户的插入
        String encryptedPassword = MD5Util.md5("123456+");
        sysUser1.setUsername(supervisionEnterprise.getEnterpriseName());
        sysUser1.setLoginName(supervisionEnterprise.getIdNumber());
        sysUser1.setPassword(encryptedPassword);
        sysUser1.setUserType(1);
        sysUser1.setInfoName(supervisionEnterprise.getEnterpriseName());
        sysUser1.setInfoId(supervisionEnterprise.getId());
        sysUser1.setStatus(0);
        sysUser1.setOperator("操作人");
        sysUser1.setOperateIp("124.124.124");
        sysUser1.setOperateTime(new Date());
        sysUserMapper.insertSelective(sysUser1);
        insertEnterpriseChildrenList(supervisionEnterprise,enterpriseParam);//下方有这个方法，是用来做许可证插入
    }

    @Override
    @Transactional
    public void update(String json, SysUser sysUser) {
        SupervisionEnterprise supervisionEnterprise = JSONObject.parseObject(json,SupervisionEnterprise.class);
        EnterpriseParam enterpriseParam = JSON.parseObject(json,EnterpriseParam.class);
        supervisionEnterprise.setPermissionType(enterpriseParam.getPermissionFamily());
        ValidationResult result = validator.validate(supervisionEnterprise);
        if(result.isHasErrors()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
        }
        if(supervisionEnterpriseMapper.countByIdNumber(supervisionEnterprise.getIdNumber(),supervisionEnterprise.getId())>0){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"社会信用代码被占用");
        }
        SupervisionEnterprise before = supervisionEnterpriseMapper.selectByPrimaryKey(supervisionEnterprise.getId());
        if(before==null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"待更新企业不存在");
        }
        supervisionEnterprise.setOperateIp("124.124.124");
        supervisionEnterprise.setOperateTime(new Date());
        supervisionEnterprise.setOperator("zcc");
        if (supervisionEnterprise.getGpsFlag()==1){
            JSONObject jsonResult = JSON.parseObject(json);
            String location = jsonResult.getString("location");
            if(location==null){
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"手动定位点位为空");
            }
            GridPointsGps gridPointsGps = gridPointsGpsMapper.getPointByCodeId(supervisionEnterprise.getIdNumber());
            if (gridPointsGps != null){
                gridPointsGps.setPoint(location);
                gridPointsGps.setOperator(sysUser.getUsername());
                gridPointsGps.setOperatorIp("1.1.1.1");
                gridPointsGpsMapper.updateByPrimaryKeySelective(gridPointsGps);
            }
            else {
                GridPointsGps gridPointsGps1 = new GridPointsGps();
                gridPointsGps1.setCodeId(supervisionEnterprise.getIdNumber());
                gridPointsGps1.setAreaId(supervisionEnterprise.getArea());
                gridPointsGps1.setPoint(location);
                gridPointsGps1.setOperator(sysUser.getUsername());
                gridPointsGps1.setOperatorIp("1.1.1.1");
                gridPointsGpsMapper.insertSelective(gridPointsGps1);
            }
        }
        insertEnterpriseChildrenList(supervisionEnterprise,enterpriseParam);
        supervisionEnterpriseMapper.updateByPrimaryKeySelectiveEx(supervisionEnterprise);
    }

    //插入子表，这里要修改，子表建议子子表
    void insertEnterpriseChildrenList(SupervisionEnterprise supervisionEnterprise,EnterpriseParam enterpriseParam){

        //如果存在这个type，先判断list是否为空（size），
        //然后找index表中是否存在了这个企业的索引，并拿到索引的id，然后在许可证表中删除indexid为这个id的所有许可证
        //循环接收到的list，如果有index，将index的id拿到赋值到每一个对象的indexid中，然后插入
        if(enterpriseParam.getPermissionFamily().contains("foodBusiness")){
            List<SupervisionEnFoodBu> supervisionEnFoodBuList = enterpriseParam.getFoodBusinessList();
            //检测许可证内容有无。
            if(supervisionEnFoodBuList.size()==0){
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"请输入食品经营许可证内容");
            }
            //先查找index表，找该企业的许可证索引，
            SupervisionEnFoodBuIndex supervisionEnFoodBuIndex = supervisionEnFoodBuIndexMapper.selectByEnterpriseId(supervisionEnterprise.getId());
            //若无改企业的许可证索引。则建立。
            SupervisionEnFoodBuIndex supervisionEnFoodBuIndex1 = new SupervisionEnFoodBuIndex();
            if (supervisionEnFoodBuIndex == null) {
                supervisionEnFoodBuIndex1.setEnterpriseId(supervisionEnterprise.getId());
                supervisionEnFoodBuIndexMapper.insertSelective(supervisionEnFoodBuIndex1);//返回此条目的id
            }
            //若存在则删除许可证表中的所有的许可证给信息。
            else{
                supervisionEnFoodBuMapper.deleteByIndexId(supervisionEnFoodBuIndex.getId());
            }
            SupervisionEnFoodBuIndex supervisionEnFoodBuIndex2 = supervisionEnFoodBuIndexMapper.selectByEnterpriseId(supervisionEnterprise.getId());
            SupervisionEnFoodBuIndex supervisionEnFoodBuIndex3 = new SupervisionEnFoodBuIndex();
            supervisionEnFoodBuIndex3.setId(supervisionEnFoodBuIndex2.getId());
            supervisionEnFoodBuIndex3.setNumber("");
            supervisionEnFoodBuList  = ListSortUtil.sort(supervisionEnFoodBuList,"endTime",null);
            supervisionEnFoodBuIndex3.setEndTime(supervisionEnFoodBuList.get(0).getEndTime());
            //开始循环传入的list。
            for (SupervisionEnFoodBu supervisionEnFoodBu : supervisionEnFoodBuList){
                ValidationResult result = validator.validate(supervisionEnFoodBu);
                if(result.isHasErrors()){
                    throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
                }//检测validator校验。
                supervisionEnFoodBu.setIndexId(supervisionEnFoodBuIndex3.getId());
                supervisionEnFoodBu.setOperateIp("124.124.124");
                supervisionEnFoodBu.setOperateTime(new Date());
                supervisionEnFoodBu.setOperator("zcc");
                supervisionEnFoodBuMapper.insertSelective(supervisionEnFoodBu);
                supervisionEnFoodBuIndex3.setNumber(supervisionEnFoodBuIndex3.getNumber() + ","+ supervisionEnFoodBu.getNumber());
            }
            supervisionEnFoodBuIndexMapper.updateByPrimaryKeySelective(supervisionEnFoodBuIndex3);
        }

        if(enterpriseParam.getPermissionFamily().contains("foodProduce")) {
            List<EnFoodProduceParam> enFoodProduceParamList = enterpriseParam.getFoodProduceList();
            List<Date> dateList = new ArrayList<>();
            //            //检测许可证内容有无。
            if (enFoodProduceParamList.size() == 0) {
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "请输入食品生产许可证内容");
            }
//            //先查找index表，找该企业的许可证索引，
            SupervisionEnFoodProIndex supervisionEnFoodProIndex = supervisionEnFoodProIndexMapper.selectByEnterpriseId(supervisionEnterprise.getId());
//            //若无改企业的许可证索引。则建立。
            SupervisionEnFoodProIndex supervisionEnFoodProIndex1 = new SupervisionEnFoodProIndex();
            if (supervisionEnFoodProIndex == null) {
                supervisionEnFoodProIndex1.setEnterpriseId(supervisionEnterprise.getId());
                supervisionEnFoodProIndexMapper.insertSelective(supervisionEnFoodProIndex1);//返回此条目的id
            }
//            //若存在则删除许可证表中的所有的许可证给信息。
            else {
                supervisionEnFoodProMapper.deleteByIndexId(supervisionEnFoodProIndex.getId());
            }
            SupervisionEnFoodProIndex supervisionEnFoodProIndex2 = supervisionEnFoodProIndexMapper.selectByEnterpriseId(supervisionEnterprise.getId());
            SupervisionEnFoodProIndex supervisionEnFoodProIndex3 = new SupervisionEnFoodProIndex();
            supervisionEnFoodProIndex3.setId(supervisionEnFoodProIndex2.getId());
            supervisionEnFoodProIndex3.setNumber("");
            for (EnFoodProduceParam enFoodProduceParam : enFoodProduceParamList) {
            ValidationResult result = validator.validate(enFoodProduceParam);
            if(result.isHasErrors()){
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
            }
            SupervisionEnFoodPro supervisionEnFoodPro = new SupervisionEnFoodPro();
            BeanUtils.copyProperties(enFoodProduceParam,supervisionEnFoodPro);
            supervisionEnFoodProIndex3.setNumber(supervisionEnFoodProIndex3.getNumber() + ","+ supervisionEnFoodPro.getNumber());
            dateList.add(enFoodProduceParam.getEndTime());
            supervisionEnFoodPro.setIndexId(supervisionEnFoodProIndex3.getId());
            supervisionEnFoodPro.setOperatorIp("124.124.124");
            supervisionEnFoodPro.setOperatorTime(new Date());
            supervisionEnFoodPro.setOperator("zcc");
            supervisionEnFoodProMapper.insertSelective(supervisionEnFoodPro);
            supervisionEnProCategoryMapper.deleteByParentId(supervisionEnFoodPro.getId());
            List<SupervisionEnProCategory> supervisionEnProCategoryList = enFoodProduceParam.getList();
            if(supervisionEnProCategoryList.size()>0){
                supervisionEnProCategoryMapper.batchInsert(supervisionEnProCategoryList.stream().map((list)->{
                    list.setOperateIp("124.124.124");
                    list.setOperateTime(new Date());
                    list.setOperator("zcc");
                    list.setParentId(supervisionEnFoodPro.getId());
                    return list;}).collect(Collectors.toList()));
            }
            }
            List<Date> afterDateList =(List<Date>) ListSortUtil.sort(dateList,null);
            supervisionEnFoodProIndex3.setEndTime(afterDateList.get(0));
            supervisionEnFoodProIndexMapper.updateByPrimaryKeySelective(supervisionEnFoodProIndex3);
        }

        if(enterpriseParam.getPermissionFamily().contains("drugsBusiness")){
            List<SupervisionEnDrugsBu> supervisionEnDrugsBuList = enterpriseParam.getDrugsBusinessList();
            //检测许可证内容有无。
            if(enterpriseParam.getDrugsBusinessList().size()==0){
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"请输入药品经营许可证内容");
            }
            //先查找index表，找该企业的许可证索引，
            SupervisionEnDrugsBuIndex supervisionEnDrugsBuIndex = supervisionEnDrugsBuIndexMapper.selectByEnterpriseId(supervisionEnterprise.getId());
            //若无改企业的许可证索引。则建立。
            SupervisionEnDrugsBuIndex supervisionEnDrugsBuIndex1 = new SupervisionEnDrugsBuIndex();
            if (supervisionEnDrugsBuIndex == null) {
                supervisionEnDrugsBuIndex1.setEnterpriseId(supervisionEnterprise.getId());
                supervisionEnDrugsBuIndexMapper.insertSelective(supervisionEnDrugsBuIndex1);//返回此条目的id
            }
            //若存在则删除许可证表中的所有的许可证给信息。
            else{
                supervisionEnDrugsBuMapper.deleteByIndexId(supervisionEnDrugsBuIndex.getId());
            }
            SupervisionEnDrugsBuIndex supervisionEnDrugsBuIndex2 = supervisionEnDrugsBuIndexMapper.selectByEnterpriseId(supervisionEnterprise.getId());
            SupervisionEnDrugsBuIndex supervisionEnDrugsBuIndex3 = new SupervisionEnDrugsBuIndex();
            supervisionEnDrugsBuIndex3.setId(supervisionEnDrugsBuIndex2.getId());
            supervisionEnDrugsBuIndex3.setNumber("");
            supervisionEnDrugsBuList  = ListSortUtil.sort(supervisionEnDrugsBuList,"endTime",null);
            supervisionEnDrugsBuIndex3.setEndTime(supervisionEnDrugsBuList.get(0).getEndTime());
            //开始循环传入的list。
            for (SupervisionEnDrugsBu supervisionEnDrugsBu : supervisionEnDrugsBuList){
                ValidationResult result = validator.validate(supervisionEnDrugsBu);
                if(result.isHasErrors()){
                    throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
                }//检测validator校验。
                supervisionEnDrugsBu.setIndexId(supervisionEnDrugsBuIndex3.getId());
                supervisionEnDrugsBu.setOperatorIp("124.124.124");
                supervisionEnDrugsBu.setOperatorTime(new Date());
                supervisionEnDrugsBu.setOperator("zcc");
                supervisionEnDrugsBuMapper.insertSelective(supervisionEnDrugsBu);
                supervisionEnDrugsBuIndex3.setNumber(supervisionEnDrugsBuIndex3.getNumber() + ","+ supervisionEnDrugsBu.getNumber());
            }
            supervisionEnDrugsBuIndexMapper.updateByPrimaryKeySelective(supervisionEnDrugsBuIndex3);
        }

        if(enterpriseParam.getPermissionFamily().contains("drugsProduce")){
            List<SupervisionEnDrugsPro> supervisionEnDrugsProList = enterpriseParam.getDrugsProduceList();
            //检测许可证内容有无。
            if(supervisionEnDrugsProList.size()==0){
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"请输入药品经营许可证内容");
            }
            //先查找index表，找该企业的许可证索引，
            SupervisionEnDrugsProIndex supervisionEnDrugsProIndex= supervisionEnDrugsProIndexMapper.selectByEnterpriseId(supervisionEnterprise.getId());
            //若无改企业的许可证索引。则建立。
            SupervisionEnDrugsProIndex supervisionEnDrugsProIndex1 = new SupervisionEnDrugsProIndex();
            if (supervisionEnDrugsProIndex == null) {
                supervisionEnDrugsProIndex1.setEnterpriseId(supervisionEnterprise.getId());
                supervisionEnDrugsProIndexMapper.insertSelective(supervisionEnDrugsProIndex1);//返回此条目的id
            }
            //若存在则删除许可证表中的所有的许可证给信息。
            else{
                supervisionEnDrugsProMapper.deleteByIndexId(supervisionEnDrugsProIndex.getId());
            }
            SupervisionEnDrugsProIndex supervisionEnDrugsProIndex2 = supervisionEnDrugsProIndexMapper.selectByEnterpriseId(supervisionEnterprise.getId());
            SupervisionEnDrugsProIndex supervisionEnDrugsProIndex3 = new SupervisionEnDrugsProIndex();
            supervisionEnDrugsProIndex3.setId(supervisionEnDrugsProIndex2.getId());
            supervisionEnDrugsProIndex3.setNumber("");
            supervisionEnDrugsProList  = ListSortUtil.sort(supervisionEnDrugsProList,"endTime",null);
            supervisionEnDrugsProIndex3.setEndTime(supervisionEnDrugsProList.get(0).getEndTime());
            //开始循环传入的list。
            for (SupervisionEnDrugsPro supervisionEnDrugsPro : supervisionEnDrugsProList){
                ValidationResult result = validator.validate(supervisionEnDrugsPro);
                if(result.isHasErrors()){
                    throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
                }//检测validator校验。
                supervisionEnDrugsPro.setIndexId(supervisionEnDrugsProIndex3.getId());
                supervisionEnDrugsPro.setOperatorIp("124.124.124");
                supervisionEnDrugsPro.setOperatorTime(new Date());
                supervisionEnDrugsPro.setOperator("zcc");
                supervisionEnDrugsProMapper.insertSelective(supervisionEnDrugsPro);
                supervisionEnDrugsProIndex3.setNumber(supervisionEnDrugsProIndex3.getNumber() + ","+ supervisionEnDrugsPro.getNumber());
            }
            supervisionEnDrugsProIndexMapper.updateByPrimaryKeySelective(supervisionEnDrugsProIndex3);
        }

        if(enterpriseParam.getPermissionFamily().contains("cosmeticsUse")){
            List<SupervisionEnCosmetics> supervisionEnCosmeticsList = enterpriseParam.getCosmeticsList();
            //检测许可证内容有无。
            if(supervisionEnCosmeticsList.size()==0){
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"请输入化妆品许可证内容");
            }
            //先查找index表，找该企业的许可证索引，
            SupervisionEnCosmeticsIndex supervisionEnCosmeticsIndex= supervisionEnCosmeticsIndexMapper.selectByEnterpriseId(supervisionEnterprise.getId());
            //若无改企业的许可证索引。则建立。
            SupervisionEnCosmeticsIndex supervisionEnCosmeticsIndex1 = new SupervisionEnCosmeticsIndex();
            if (supervisionEnCosmeticsIndex == null) {
                supervisionEnCosmeticsIndex1.setEnterpriseId(supervisionEnterprise.getId());
                supervisionEnCosmeticsIndexMapper.insertSelective(supervisionEnCosmeticsIndex1);//返回此条目的id
            }
            //若存在则删除许可证表中的所有的许可证给信息。
            else{
                supervisionEnCosmeticsMapper.deleteByIndexId(supervisionEnCosmeticsIndex.getId());
            }
            SupervisionEnCosmeticsIndex supervisionEnCosmeticsIndex2 = supervisionEnCosmeticsIndexMapper.selectByEnterpriseId(supervisionEnterprise.getId());
            SupervisionEnCosmeticsIndex supervisionEnCosmeticsIndex3= new SupervisionEnCosmeticsIndex();
            supervisionEnCosmeticsIndex3.setId(supervisionEnCosmeticsIndex2.getId());
            supervisionEnCosmeticsIndex3.setNumber("");
            supervisionEnCosmeticsList  = ListSortUtil.sort(supervisionEnCosmeticsList,"endTime",null);
            supervisionEnCosmeticsIndex3.setEndTime(supervisionEnCosmeticsList.get(0).getEndTime());
            //开始循环传入的list。
            for (SupervisionEnCosmetics supervisionEnCosmetics : supervisionEnCosmeticsList){
                ValidationResult result = validator.validate(supervisionEnCosmetics);
                if(result.isHasErrors()){
                    throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
                }//检测validator校验。
                supervisionEnCosmetics.setIndexId(supervisionEnCosmeticsIndex3.getId());
                supervisionEnCosmetics.setOperateIp("124.124.124");
                supervisionEnCosmetics.setOperateTime(new Date());
                supervisionEnCosmetics.setOperator("zcc");
                supervisionEnCosmeticsMapper.insertSelective(supervisionEnCosmetics);
                supervisionEnCosmeticsIndex3.setNumber(supervisionEnCosmeticsIndex3.getNumber() + ","+ supervisionEnCosmetics.getRegisterCode());
            }
            supervisionEnCosmeticsIndexMapper.updateByPrimaryKeySelective(supervisionEnCosmeticsIndex3);
        }

        if(enterpriseParam.getPermissionFamily().contains("medicalProduce")){
            List<SupervisionEnMedicalPro> supervisionEnMedicalProList = enterpriseParam.getMedicalProduceList();
            //检测许可证内容有无。
            if(supervisionEnMedicalProList.size()==0){
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"请输入药品生产许可证内容");
            }
            //先查找index表，找该企业的许可证索引，
            SupervisionEnMedicalProIndex supervisionEnMedicalProIndex= supervisionEnMedicalProIndexMapper.selectByEnterpriseId(supervisionEnterprise.getId());
            //若无改企业的许可证索引。则建立。
            SupervisionEnMedicalProIndex supervisionEnMedicalProIndex1 = new SupervisionEnMedicalProIndex();
            if (supervisionEnMedicalProIndex == null) {
                supervisionEnMedicalProIndex1.setEnterpriseId(supervisionEnterprise.getId());
                supervisionEnMedicalProIndexMapper.insertSelective(supervisionEnMedicalProIndex1);//返回此条目的id
            }
            //若存在则删除许可证表中的所有的许可证给信息。
            else{
                supervisionEnMedicalProMapper.deleteByIndexId(supervisionEnMedicalProIndex.getId());
            }
            SupervisionEnMedicalProIndex supervisionEnMedicalProIndex2 = supervisionEnMedicalProIndexMapper.selectByEnterpriseId(supervisionEnterprise.getId());
            SupervisionEnMedicalProIndex supervisionEnMedicalProIndex3 = new SupervisionEnMedicalProIndex();
            supervisionEnMedicalProIndex3.setId(supervisionEnMedicalProIndex2.getId());
            supervisionEnMedicalProIndex3.setNumber("");
            supervisionEnMedicalProList  = ListSortUtil.sort(supervisionEnMedicalProList,"endTime",null);
            supervisionEnMedicalProIndex3.setEndTime(supervisionEnMedicalProList.get(0).getEndTime());
            //开始循环传入的list。
            for (SupervisionEnMedicalPro supervisionEnMedicalPro : supervisionEnMedicalProList){
                ValidationResult result = validator.validate(supervisionEnMedicalPro);
                if(result.isHasErrors()){
                    throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
                }//检测validator校验。
                supervisionEnMedicalPro.setIndexId(supervisionEnMedicalProIndex3.getId());
                supervisionEnMedicalPro.setOperateIp("124.124.124");
                supervisionEnMedicalPro.setOperateTime(new Date());
                supervisionEnMedicalPro.setOperator("zcc");
                supervisionEnMedicalProMapper.insertSelective(supervisionEnMedicalPro);
                supervisionEnMedicalProIndex3.setNumber(supervisionEnMedicalProIndex3.getNumber() + ","+ supervisionEnMedicalPro.getRegisterNumber());
            }
            supervisionEnMedicalProIndexMapper.updateByPrimaryKeySelective(supervisionEnMedicalProIndex3);
        }

        if(enterpriseParam.getPermissionFamily().contains("medicalBusiness")){
            List<SupervisionEnMedicalBu> supervisionEnMedicalBuList = enterpriseParam.getMedicalBusinessList();
            //检测许可证内容有无。
            if(supervisionEnMedicalBuList.size()==0){
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"请输入药品经营许可证内容");
            }
            //先查找index表，找该企业的许可证索引，
            SupervisionEnMedicalBuIndex supervisionEnMedicalBuIndex= supervisionEnMedicalBuIndexMapper.selectByEnterpriseId(supervisionEnterprise.getId());
            //若无改企业的许可证索引。则建立。
            SupervisionEnMedicalBuIndex supervisionEnMedicalBuIndex1 = new SupervisionEnMedicalBuIndex();
            if (supervisionEnMedicalBuIndex == null) {
                supervisionEnMedicalBuIndex1.setEnterpriseId(supervisionEnterprise.getId());
                supervisionEnMedicalBuIndexMapper.insertSelective(supervisionEnMedicalBuIndex1);//返回此条目的id
            }
            //若存在则删除许可证表中的所有的许可证给信息。
            else{
                supervisionEnMedicalBuMapper.deleteByIndexId(supervisionEnMedicalBuIndex.getId());
            }
            SupervisionEnMedicalBuIndex supervisionEnMedicalBuIndex2 = supervisionEnMedicalBuIndexMapper.selectByEnterpriseId(supervisionEnterprise.getId());
            SupervisionEnMedicalBuIndex supervisionEnMedicalBuIndex3 = new SupervisionEnMedicalBuIndex();
            supervisionEnMedicalBuIndex3.setId(supervisionEnMedicalBuIndex2.getId());
            supervisionEnMedicalBuIndex3.setNumber("");
            supervisionEnMedicalBuList  = ListSortUtil.sort(supervisionEnMedicalBuList,"endTime",null);
            supervisionEnMedicalBuIndex3.setEndTime(supervisionEnMedicalBuList.get(0).getEndTime());
            //开始循环传入的list。
            for (SupervisionEnMedicalBu supervisionEnMedicalBu : supervisionEnMedicalBuList){
                ValidationResult result = validator.validate(supervisionEnMedicalBu);
                if(result.isHasErrors()){
                    throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
                }//检测validator校验。
                supervisionEnMedicalBu.setIndexId(supervisionEnMedicalBuIndex3.getId());
                supervisionEnMedicalBu.setOperateIp("124.124.124");
                supervisionEnMedicalBu.setOperateTime(new Date());
                supervisionEnMedicalBu.setOperator("zcc");
                supervisionEnMedicalBuMapper.insertSelective(supervisionEnMedicalBu);
                supervisionEnMedicalBuIndex3.setNumber(supervisionEnMedicalBuIndex3.getNumber() + ","+ supervisionEnMedicalBu.getRegisterNumber());
            }
            supervisionEnMedicalBuIndexMapper.updateByPrimaryKeySelective(supervisionEnMedicalBuIndex3);
        }

        if(enterpriseParam.getPermissionFamily().contains("smallCater")){
            List<SupervisionEnSmallCater> supervisionEnSmallCaterList = enterpriseParam.getSmallCaterList();
            //检测许可证内容有无。
            if(supervisionEnSmallCaterList.size()==0){
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"请输入小餐饮许可证内容");
            }
            //先查找index表，找该企业的许可证索引，
            SupervisionEnSmallCaterIndex supervisionEnSmallCaterIndex= supervisionEnSmallCaterIndexMapper.selectByEnterpriseId(supervisionEnterprise.getId());
            //若无改企业的许可证索引。则建立。
            SupervisionEnSmallCaterIndex supervisionEnSmallCaterIndex1 = new SupervisionEnSmallCaterIndex();
            if (supervisionEnSmallCaterIndex == null) {
                supervisionEnSmallCaterIndex1.setEnterpriseId(supervisionEnterprise.getId());
                supervisionEnSmallCaterIndexMapper.insertSelective(supervisionEnSmallCaterIndex1);//返回此条目的id
            }
            //若存在则删除许可证表中的所有的许可证给信息。
            else{
                supervisionEnSmallCaterMapper.deleteByIndexId(supervisionEnSmallCaterIndex.getId());
            }
            SupervisionEnSmallCaterIndex supervisionEnSmallCaterIndex2 = supervisionEnSmallCaterIndexMapper.selectByEnterpriseId(supervisionEnterprise.getId());
            SupervisionEnSmallCaterIndex supervisionEnSmallCaterIndex3 = new SupervisionEnSmallCaterIndex();
            supervisionEnSmallCaterIndex3.setId(supervisionEnSmallCaterIndex2.getId());
            supervisionEnSmallCaterIndex3.setNumber("");
            supervisionEnSmallCaterList  = ListSortUtil.sort(supervisionEnSmallCaterList,"endTime",null);
            supervisionEnSmallCaterIndex3.setEndTime(supervisionEnSmallCaterList.get(0).getEndTime());
            //开始循环传入的list。
            for (SupervisionEnSmallCater supervisionEnSmallCater : supervisionEnSmallCaterList){
                ValidationResult result = validator.validate(supervisionEnSmallCater);
                if(result.isHasErrors()){
                    throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
                }//检测validator校验。
                supervisionEnSmallCater.setIndexId(supervisionEnSmallCaterIndex3.getId());
                supervisionEnSmallCater.setOperateIp("124.124.124");
                supervisionEnSmallCater.setOperateTime(new Date());
                supervisionEnSmallCater.setOperator("zcc");
                supervisionEnSmallCaterMapper.insertSelective(supervisionEnSmallCater);
                supervisionEnSmallCaterIndex3.setNumber(supervisionEnSmallCaterIndex3.getNumber() + ","+ supervisionEnSmallCater.getRegisterNumber());
            }
            supervisionEnSmallCaterIndexMapper.updateByPrimaryKeySelective(supervisionEnSmallCaterIndex3);
        }

        if(enterpriseParam.getPermissionFamily().contains("smallWorkshop")){
            List<SupervisionEnSmallWorkshop> supervisionEnSmallWorkshopList = enterpriseParam.getSmallWorkshopList();
            //检测许可证内容有无。
            if(supervisionEnSmallWorkshopList.size()==0){
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"请输入小作坊许可证内容");
            }
            //先查找index表，找该企业的许可证索引，
            SupervisionEnSmallWorkshopIndex supervisionEnSmallWorkshopIndex= supervisionEnSmallWorkshopIndexMapper.selectByEnterpriseId(supervisionEnterprise.getId());
            //若无改企业的许可证索引。则建立。
            SupervisionEnSmallWorkshopIndex supervisionEnSmallWorkshopIndex1 = new SupervisionEnSmallWorkshopIndex();
            if (supervisionEnSmallWorkshopIndex == null) {
                supervisionEnSmallWorkshopIndex1.setEnterpriseId(supervisionEnterprise.getId());
                supervisionEnSmallWorkshopIndexMapper.insertSelective(supervisionEnSmallWorkshopIndex1);//返回此条目的id
            }
            //若存在则删除许可证表中的所有的许可证给信息。
            else{
                supervisionEnSmallWorkshopMapper.deleteByIndexId(supervisionEnSmallWorkshopIndex.getId());
            }
            SupervisionEnSmallWorkshopIndex supervisionEnSmallWorkshopIndex2 = supervisionEnSmallWorkshopIndexMapper.selectByEnterpriseId(supervisionEnterprise.getId());
            SupervisionEnSmallWorkshopIndex supervisionEnSmallWorkshopIndex3 = new SupervisionEnSmallWorkshopIndex();
            supervisionEnSmallWorkshopIndex3.setId(supervisionEnSmallWorkshopIndex2.getId());
            supervisionEnSmallWorkshopIndex3.setNumber("");
            supervisionEnSmallWorkshopList  = ListSortUtil.sort(supervisionEnSmallWorkshopList,"endTime",null);
            supervisionEnSmallWorkshopIndex3.setEndTime(supervisionEnSmallWorkshopList.get(0).getEndTime());
            //开始循环传入的list。
            for (SupervisionEnSmallWorkshop supervisionEnSmallWorkshop : supervisionEnSmallWorkshopList){
                ValidationResult result = validator.validate(supervisionEnSmallWorkshop);
                if(result.isHasErrors()){
                    throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
                }//检测validator校验。
                supervisionEnSmallWorkshop.setIndexId(supervisionEnSmallWorkshopIndex3.getId());
                supervisionEnSmallWorkshop.setOperateIp("124.124.124");
                supervisionEnSmallWorkshop.setOperateTime(new Date());
                supervisionEnSmallWorkshop.setOperator("zcc");
                supervisionEnSmallWorkshopMapper.insertSelective(supervisionEnSmallWorkshop);
                supervisionEnSmallWorkshopIndex3.setNumber(supervisionEnSmallWorkshopIndex3.getNumber() + ","+ supervisionEnSmallWorkshop.getRegisterNumber());
            }
            supervisionEnSmallWorkshopIndexMapper.updateByPrimaryKeySelective(supervisionEnSmallWorkshopIndex3);
        }

        if(enterpriseParam.getPermissionFamily().contains("industrialProducts")){
            List<SupervisionEnIndustrialProducts> supervisionEnIndustrialProductsList = enterpriseParam.getIndustrialProductsList();
            //检测许可证内容有无。
            if(supervisionEnIndustrialProductsList.size()==0){
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"请输入工业产品许可证内容");
            }
            //先查找index表，找该企业的许可证索引，
            SupervisionEnIndustrialProductsIndex supervisionEnIndustrialProductsIndex= supervisionEnIndustrialProductsIndexMapper.selectByEnterpriseId(supervisionEnterprise.getId());
            //若无改企业的许可证索引。则建立。
            SupervisionEnIndustrialProductsIndex supervisionEnIndustrialProductsIndex1 = new SupervisionEnIndustrialProductsIndex();
            if (supervisionEnIndustrialProductsIndex == null) {
                supervisionEnIndustrialProductsIndex1.setEnterpriseId(supervisionEnterprise.getId());
                supervisionEnIndustrialProductsIndexMapper.insertSelective(supervisionEnIndustrialProductsIndex1);//返回此条目的id
            }
            //若存在则删除许可证表中的所有的许可证给信息。
            else{
                supervisionEnIndustrialProductsMapper.deleteByIndexId(supervisionEnIndustrialProductsIndex.getId());
            }
            SupervisionEnIndustrialProductsIndex supervisionEnIndustrialProductsIndex2 = supervisionEnIndustrialProductsIndexMapper.selectByEnterpriseId(supervisionEnterprise.getId());
            SupervisionEnIndustrialProductsIndex supervisionEnIndustrialProductsIndex3 = new SupervisionEnIndustrialProductsIndex();
            supervisionEnIndustrialProductsIndex3.setId(supervisionEnIndustrialProductsIndex2.getId());
            supervisionEnIndustrialProductsIndex3.setNumber("");
            supervisionEnIndustrialProductsList  = ListSortUtil.sort(supervisionEnIndustrialProductsList,"endTime",null);
            supervisionEnIndustrialProductsIndex3.setEndTime(supervisionEnIndustrialProductsList.get(0).getEndTime());
            //开始循环传入的list。
            for (SupervisionEnIndustrialProducts supervisionEnIndustrialProducts : supervisionEnIndustrialProductsList){
                ValidationResult result = validator.validate(supervisionEnIndustrialProducts);
                if(result.isHasErrors()){
                    throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
                }//检测validator校验。
                supervisionEnIndustrialProducts.setIndexId(supervisionEnIndustrialProductsIndex3.getId());
                supervisionEnIndustrialProducts.setOperateIp("124.124.124");
                supervisionEnIndustrialProducts.setOperateTime(new Date());
                supervisionEnIndustrialProducts.setOperator("zcc");
                supervisionEnIndustrialProductsMapper.insertSelective(supervisionEnIndustrialProducts);
                supervisionEnIndustrialProductsIndex3.setNumber(supervisionEnIndustrialProductsIndex3.getNumber() + ","+ supervisionEnIndustrialProducts.getRegisterNumber());
            }
            supervisionEnIndustrialProductsIndexMapper.updateByPrimaryKeySelective(supervisionEnIndustrialProductsIndex3);
        }


    }

    @Override
    @Transactional
    public void changeNormal(Integer id) {
        SupervisionEnterprise supervisionEnterprise = new SupervisionEnterprise();
        supervisionEnterprise.setId(id);
        supervisionEnterprise.setBusinessState(1);
        supervisionEnterpriseMapper.updateByPrimaryKeySelective(supervisionEnterprise);
    }

    @Override
    @Transactional
    public void changeAbnormal(Integer id, Integer abId, String content) {
        SupervisionEnterprise supervisionEnterprise = new SupervisionEnterprise();
        supervisionEnterprise.setId(id);
        supervisionEnterprise.setBusinessState(3);
        supervisionEnterprise.setAbnormalId(abId);
        supervisionEnterprise.setAbnormalContent(content);
        supervisionEnterpriseMapper.updateByPrimaryKeySelective(supervisionEnterprise);
    }

    @Override
    @Transactional
    public void delete(int id) {
        SupervisionEnterprise before = supervisionEnterpriseMapper.selectByPrimaryKey(id);
        if(before==null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"待删除企业不存在");
        }
        supervisionEnterpriseMapper.deleteByPrimaryKey(id);

    }



    //改变企业状态
    @Override
    public void changeStop(int id) {
        SupervisionEnterprise supervisionEnterprise = supervisionEnterpriseMapper.selectByPrimaryKey(id);
        if(supervisionEnterprise==null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"待更新企业不存在");
        }
        int isStop;
        if(supervisionEnterprise.getIsStop()==0){
            isStop=1;
        }else{
            isStop=0;
        }
        supervisionEnterpriseMapper.changeStop(id,isStop);
    }

    @Override
    public PageResult<EnterpriseListResult> getPageByEnterpriseId(int id) {
        SupervisionEnterprise supervisionEnterprise = supervisionEnterpriseMapper.selectByPrimaryKey(id);
        EnterpriseListResult enterpriseListResult = new EnterpriseListResult();
        if(supervisionEnterprise==null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"无企业信息");
        }
        BeanUtils.copyProperties(supervisionEnterprise,enterpriseListResult);
        List<EnterpriseListResult> enterpriseList = new ArrayList<>();
        enterpriseList.add(enterpriseListResult);
        PageResult<EnterpriseListResult> pageResult = new PageResult<>();
        pageResult.setData(enterpriseList);
        pageResult.setTotal(1);
        pageResult.setPageNo(1);
        pageResult.setPageSize(1);
        return pageResult;
    }

    @Override
    public Map<Integer,Integer> getStatistics(List<SysIndustry> sysIndustryList, List<Integer> sysAreaList,String supervisor) {
        Map<Integer,Integer> map = new HashMap<>();
        for (SysIndustry sysIndustry:sysIndustryList){
                EnterpriseSearchParam enterpriseSearchParam = new EnterpriseSearchParam();
                List<String> industryList = new ArrayList<>();
                industryList.add(sysIndustry.getRemark());
                enterpriseSearchParam.setAreaList(sysAreaList);
                enterpriseSearchParam.setIndustryList(industryList);
                enterpriseSearchParam.setSupervisor(supervisor);
                map.put(sysIndustry.getId(),supervisionEnterpriseMapper.countList(enterpriseSearchParam));
        }
        return map;
    }
//文件导入
    @Override
    @Transactional
    public JSONObject importExcel(MultipartFile file, Integer type) {

        JSONObject changedNumbers = new JSONObject();
        int updateNumber=0;
        int insertNumber=0;

        List<SysDept> sysDeptList = sysDeptMapper.getAllDept();//将数据库中的部门和id拿出来组成map
        Map<String,Integer> deptMap = new HashMap<>();
        for (SysDept sysDept : sysDeptList){
            deptMap.put(sysDept.getName(),sysDept.getId());
        }

        List<SysArea> sysAreaList = sysAreaMapper.getAllArea();//将数据库中的地区和id拿出来组成map
        Map<String,Integer> areaMap = new HashMap<>();
        for(SysArea sysArea : sysAreaList){
            areaMap.put(sysArea.getName(),sysArea.getId());
        }

        List<EnterpriseListResult> allEnterpriseList = supervisionEnterpriseMapper.getAll();//将数据库中的企业和id拿出来组成map
        Map<String,Integer> enterpriseIdMap = new HashMap<>();
        for (EnterpriseListResult enterpriseListResult : allEnterpriseList){
            enterpriseIdMap.put(enterpriseListResult.getIdNumber(),enterpriseListResult.getId());
        }

//        //建立许可证map，目前先暂时放，如果需要的话仍然是和id关联，不关联信用代码
//        List<SupervisionEnterprise> supervisionEnterpriseList = new ArrayList<>();
//        List<SupervisionEnFoodPro> supervisionEnFoodProList = new ArrayList<>();
//        List<SupervisionEnProCategory> supervisionEnProCategoryList = new ArrayList<>();
//        List<SupervisionEnMedical> supervisionEnMedicalList = new ArrayList<>();
//        List<SupervisionEnFoodCir> supervisionEnFoodCirList = new ArrayList<>();
//        List<SupervisionEnFoodBu> supervisionEnFoodBuList = new ArrayList<>();
//        List<SupervisionEnDrugsBu> supervisionEnDrugsBuList = new ArrayList<>();
//        List<SupervisionEnCommon>  supervisionEnCommonList = new ArrayList<>();
//        List<SupervisionEnCosmetics> supervisionEnCosmeticsList = new ArrayList<>();
        if(type == 7) {
            try {
                XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
                XSSFSheet sheet0 = workbook.getSheetAt(0);
                Map<String, String> errorMap = new HashMap<>();
                List<String> errorList = new ArrayList<String>();
                List<SysUser> sysUserList = new ArrayList<>();

                //开始对企业的信用代码和id对应，存在了就删除许可证
                int rowNumber=sheet0.getPhysicalNumberOfRows();
                for (int j = 0; j <rowNumber; j++) {
                    if (j == 0) {
                        continue;//标题行
                    }
                    XSSFRow titleRow = sheet0.getRow(0);
                    XSSFRow row = sheet0.getRow(j);
                    XSSFRow nextRow = sheet0.getRow(j+1);
                    if((row.getCell(0).getCellType()==CellType.BLANK)&&(row.getCell(1).getCellType()==CellType.BLANK))
                    {
                        break;
                    }
                    int a = j + 1;
                    if(row.getCell(0).getCellType()==CellType.BLANK)
                    {
                        errorList.add("企业基本信息表主表 第" + a + "行"+titleRow.getCell(0).toString()+"为空");
                    }
                    if(row.getCell(1).getCellType()==CellType.BLANK)
                    {
                        errorList.add("企业基本信息表主表 第" + a + "行"+titleRow.getCell(1).toString()+"为空");
                    }
                    if(row.getCell(3).getCellType()==CellType.BLANK)
                    {
                        errorList.add("企业基本信息表主表 第" + a + "行"+titleRow.getCell(3).toString()+"为空");
                    }
                    if(row.getCell(4).getCellType()==CellType.BLANK)
                    {
                        errorList.add("企业基本信息表主表 第" + a + "行"+titleRow.getCell(4).toString()+"为空");
                    }
                    if(row.getCell(5).getCellType()==CellType.BLANK)
                    {
                        errorList.add("企业基本信息表主表 第" + a + "行"+titleRow.getCell(5).toString()+"为空");
                    }
                    if(row.getCell(14).getCellType()==CellType.BLANK)
                    {
                        errorList.add("企业基本信息表主表 第" + a + "行"+titleRow.getCell(14).toString()+"为空");
                    }
                    if(row.getCell(15).getCellType()==CellType.BLANK)
                    {
                        errorList.add("企业基本信息表主表 第" + a + "行"+titleRow.getCell(15).toString()+"为空");
                    }
                    if ((row.getCell(0) != null) && row.getCell(0).getCellType() != CellType.BLANK && row.getCell(0).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(0).toString(), "不是文本类型");
                        errorList.add("企业基本信息表主表 第" + a + "行"+titleRow.getCell(0).toString()+"不是文本类型");
                    }
                    if ((row.getCell(1) != null) && row.getCell(1).getCellType() != CellType.BLANK && row.getCell(1).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(1).toString(), "不是文本类型");
                        errorList.add("企业基本信息表主表 第" + a + "行"+titleRow.getCell(1).toString()+"不是文本类型");
                    }
                    if ((row.getCell(2) != null) && row.getCell(2).getCellType() != CellType.BLANK  && row.getCell(2).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(2).toString(), "不是文本类型");
                        errorList.add("企业基本信息表主表 第" + a + "行"+titleRow.getCell(2).toString()+"不是文本类型");
                    }
                    if ((row.getCell(3) != null)&& row.getCell(3).getCellType() != CellType.BLANK  && row.getCell(3).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(3).toString(), "不是文本类型");
                        errorList.add("企业基本信息表主表 第" + a + "行"+titleRow.getCell(3).toString()+"不是文本类型");
                    }
                    if ((row.getCell(4) != null) && (row.getCell(4).getCellType()!=CellType.BLANK)&& row.getCell(4).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(4).toString(), "不是文本类型");
                        errorList.add("企业基本信息表主表 第" + a + "行"+titleRow.getCell(4).toString()+"不是文本类型");
                    }
                    if ((row.getCell(5) != null) &&(row.getCell(5).getCellType()!=CellType.BLANK)&& row.getCell(5).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(5).toString(), "不是文本类型");
                        errorList.add("企业基本信息表主表 第" + a + "行"+titleRow.getCell(5).toString()+"不是文本类型");
                    }
                    if ((row.getCell(6) != null) && row.getCell(6).getCellType() != CellType.BLANK && row.getCell(6).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(6).toString(), "不是文本类型");
                        errorList.add("企业基本信息表主表 第" + a + "行"+titleRow.getCell(6).toString()+"不是文本类型");
                    }
                    if ((row.getCell(7) != null) && (row.getCell(7).getCellType()!=CellType.BLANK)&& row.getCell(7).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(7).toString(), "不是文本类型");
                        errorList.add("企业基本信息表主表 第" + a + "行"+titleRow.getCell(7).toString()+"不是文本类型");
                    }
                    if ((row.getCell(8) != null) && row.getCell(8).getCellType() != CellType.BLANK && row.getCell(8).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(8).toString(), "不是文本类型");
                        errorList.add("企业基本信息表主表 第" + a + "行"+titleRow.getCell(8).toString()+"不是文本类型");
                    }
                    if ((row.getCell(9) != null)&& row.getCell(9).getCellType() != CellType.BLANK  &&row.getCell(9).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(9).toString(), "不是文本类型");
                        errorList.add("企业基本信息表主表 第" + a + "行"+titleRow.getCell(9).toString()+"不是文本类型");
                    }
                    if ((row.getCell(10) != null) && row.getCell(10).getCellType() != CellType.BLANK &&row.getCell(10).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(10).toString(), "不是文本类型");
                        errorList.add("企业基本信息表主表 第" + a + "行"+titleRow.getCell(10).toString()+"不是文本类型");
                    }
                    if ((row.getCell(11) != null)&& row.getCell(11).getCellType() != CellType.BLANK  &&row.getCell(11).getCellType() != CellType.NUMERIC) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(11).toString(), "不是日期类型");
                        errorList.add("企业基本信息表主表 第" + a + "行"+titleRow.getCell(11).toString()+"不是日期类型");
                    }
                    if ((row.getCell(12) != null)&& row.getCell(12).getCellType() != CellType.BLANK  && row.getCell(12).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(12).toString(), "不是文本类型");
                        errorList.add("企业基本信息表主表 第" + a + "行"+titleRow.getCell(12).toString()+"不是文本类型");
                    }
                    if ((row.getCell(13) != null) && row.getCell(13).getCellType() != CellType.BLANK && row.getCell(13).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(13).toString(),"不是文本类型");
                        errorList.add("企业基本信息表主表 第" + a + "行"+titleRow.getCell(13).toString()+"不是文本类型");
                    }
                    if ((row.getCell(14) != null)&& row.getCell(14).getCellType() != CellType.BLANK && row.getCell(14).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(14).toString(),"不是数字类型");
                        errorList.add("企业基本信息表主表 第" + a + "行"+titleRow.getCell(14).toString()+"不是文本类型");
                    }
                    if ((row.getCell(15) != null) && row.getCell(15).getCellType() != CellType.BLANK && row.getCell(15).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(15).toString(),"不是数字类型");
                        errorList.add("企业基本信息表主表 第" + a + "行"+titleRow.getCell(15).toString()+"不是文本类型");
                    }
                    if ((row.getCell(16) != null) && row.getCell(16).getCellType() != CellType.BLANK && row.getCell(16).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(16).toString(),"不是文本类型");
                        errorList.add("企业基本信息表主表 第" + a + "行"+titleRow.getCell(16).toString()+"不是文本类型");
                    }
                    if ((row.getCell(17) != null)&& row.getCell(17).getCellType() != CellType.BLANK  && row.getCell(17).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(17).toString(),"不是数字类型");
                        errorList.add("企业基本信息表主表 第" + a + "行"+titleRow.getCell(17).toString()+"不是文本类型");
                    }
                    if ((row.getCell(18) != null) && row.getCell(18).getCellType() != CellType.BLANK && row.getCell(18).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(18).toString(), "不是文本类型");
                        errorList.add("企业基本信息表主表 第" + a + "行"+titleRow.getCell(18).toString()+"不是文本类型");
                    }
                    if ((row.getCell(19) != null) && row.getCell(19).getCellType() != CellType.BLANK && row.getCell(19).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(19).toString(),"不是文本类型");
                        errorList.add("企业基本信息表主表 第" + a + "行"+titleRow.getCell(19).toString()+"不是文本类型");
                    }
                    if ((row.getCell(20) != null) && row.getCell(20).getCellType() != CellType.BLANK && row.getCell(20).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(20).toString(),"不是文本类型");
                        errorList.add("企业基本信息表主表 第" + a + "行"+titleRow.getCell(20).toString()+"不是文本类型");
                    }
                }
                if (!errorList.isEmpty()) {
                    System.out.println(errorList);
                    throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, errorList);
                }
                for (int j = 0; j < rowNumber; j++) {
                    if (j == 0) {
                        continue;//标题行
                    }
                    SupervisionEnterprise supervisionEnterprise = new SupervisionEnterprise();
                    XSSFRow row = sheet0.getRow(j);
                    supervisionEnterprise.setOperationMode(ExcalUtils.handleStringXSSF(row.getCell(0)));
                    supervisionEnterprise.setEnterpriseName(ExcalUtils.handleStringXSSF(row.getCell(1)));
                    supervisionEnterprise.setShopName(ExcalUtils.handleStringXSSF(row.getCell(2)));
                    supervisionEnterprise.setIdNumber(ExcalUtils.handleStringXSSF(row.getCell(3)));
                    supervisionEnterprise.setRegisteredAddress(ExcalUtils.handleStringXSSF(row.getCell(4)));
                    supervisionEnterprise.setLegalPerson(ExcalUtils.handleStringXSSF(row.getCell(5)));
                    supervisionEnterprise.setIpIdNumber(ExcalUtils.handleStringXSSF(row.getCell(6)));
                    supervisionEnterprise.setCantacts(ExcalUtils.handleStringXSSF(row.getCell(7)));
                    supervisionEnterprise.setCantactWay(ExcalUtils.handleStringXSSF(row.getCell(8)));
                    supervisionEnterprise.setBusinessTermStart(ExcalUtils.handleStringXSSF(row.getCell(9)));
                    supervisionEnterprise.setBusinessTermEnd(ExcalUtils.handleStringXSSF(row.getCell(10)));
                    supervisionEnterprise.setGivenDate(ExcalUtils.handleDateXSSF(row.getCell(11)));
                    supervisionEnterprise.setGivenGov(ExcalUtils.handleStringXSSF(row.getCell(12)));
                    supervisionEnterprise.setBusinessScale(ExcalUtils.handleStringXSSF(row.getCell(13)));
                    supervisionEnterprise.setArea(areaMap.get(ExcalUtils.handleStringXSSF(row.getCell(14))));
                    supervisionEnterprise.setRegulators(deptMap.get(ExcalUtils.handleStringXSSF(row.getCell(15))));
                    supervisionEnterprise.setSupervisor(ExcalUtils.handleStringXSSF(row.getCell(16)));
                    supervisionEnterprise.setGrid(areaMap.get(ExcalUtils.handleStringXSSF(row.getCell(17))));
                    supervisionEnterprise.setGridPerson(ExcalUtils.handleStringXSSF(row.getCell(18)));
                    supervisionEnterprise.setTransformationType(ExcalUtils.handleStringXSSF(row.getCell(19)));
                    supervisionEnterprise.setIntegrityLevel(ExcalUtils.handleStringXSSF(row.getCell(20)));
                    supervisionEnterprise.setOperator("操作人");
                    supervisionEnterprise.setOperateIp("123.123.123");
                    supervisionEnterprise.setOperateTime(new Date());
                    if (!supervisionEnterprise.getIdNumber().equals("")) {
                        if (enterpriseIdMap.get(supervisionEnterprise.getIdNumber()) != null) {
                            int id = enterpriseIdMap.get(supervisionEnterprise.getIdNumber());
                            supervisionEnterprise.setId(id);
                            supervisionEnterpriseMapper.updateByPrimaryKeySelective(supervisionEnterprise);
                            updateNumber++;
                        } else {//新企业就注册
                            supervisionEnterpriseMapper.insertSelective(supervisionEnterprise);
                            insertNumber++;
                        }
                    }
                }
                changedNumbers.put("updateNumbers",updateNumber);
                changedNumbers.put("insertNumbers",insertNumber);

                allEnterpriseList = supervisionEnterpriseMapper.getAll();//将数据库中的企业和id拿出来组成map
                enterpriseIdMap = new HashMap<>();
                for (EnterpriseListResult enterpriseListResult : allEnterpriseList){
                    enterpriseIdMap.put(enterpriseListResult.getIdNumber(),enterpriseListResult.getId());
                }

                //食品经营许可证
                List<SupervisionEnFoodBu> supervisionEnFoodBuList = supervisionEnFoodBuMapper.getAll();
                Map <String,Integer> numberMap =new HashMap<>();
                for (SupervisionEnFoodBu supervisionEnFoodBu:supervisionEnFoodBuList){
                    numberMap.put(supervisionEnFoodBu.getNumber(),supervisionEnFoodBu.getId());
                }
                List<SupervisionEnFoodBuIndex> supervisionEnFoodBuIndexList = supervisionEnFoodBuIndexMapper.getAll();
                Map <String,Integer> numberFoodBuIndexMap =new HashMap<>();
                for (SupervisionEnFoodBuIndex supervisionEnFoodBuIndex:supervisionEnFoodBuIndexList){
                    numberFoodBuIndexMap.put(supervisionEnFoodBuIndex.getNumber(),supervisionEnFoodBuIndex.getId());
                }
                XSSFSheet sheet1 = workbook.getSheetAt(1);
                for (int j = 0; j <sheet1.getPhysicalNumberOfRows(); j++) {
                    if (j == 0) {
                        continue;//标题行
                    }
                    XSSFRow titleRow = sheet1.getRow(0);
                    XSSFRow row = sheet1.getRow(j);
                    XSSFRow nextRow = sheet1.getRow(j+1);
                    if((row.getCell(1).getCellType()==CellType.BLANK)&&(row.getCell(2).getCellType()==CellType.BLANK))
                    {
                        break;
                    }
                    int a = j + 1;
                    if(row.getCell(1).getCellType()==CellType.BLANK)
                    {
                        errorList.add("食品经营许可证 第" + a + "行"+titleRow.getCell(1).toString()+"为空");
                    }
                    if(row.getCell(2).getCellType()==CellType.BLANK)
                    {
                        errorList.add("食品经营许可证 第" + a + "行"+titleRow.getCell(2).toString()+"为空");
                    }
                    if(row.getCell(12).getCellType()==CellType.BLANK)
                    {
                        errorList.add("食品经营许可证 第" + a + "行"+titleRow.getCell(12).toString()+"为空");
                    }
                    if ((row.getCell(0) != null) && row.getCell(0).getCellType() != CellType.BLANK && row.getCell(0).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(0).toString(), "不是文本类型");
                        errorList.add("食品经营许可证 第" + a + "行"+titleRow.getCell(0).toString()+"不是文本类型");
                    }
                    if ((row.getCell(1) != null) && row.getCell(1).getCellType() != CellType.BLANK && row.getCell(1).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(1).toString(), "不是文本类型");
                        errorList.add("食品经营许可证 第" + a + "行"+titleRow.getCell(1).toString()+"不是文本类型");
                    }
                    if ((row.getCell(2) != null) && row.getCell(2).getCellType() != CellType.BLANK  && row.getCell(2).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(2).toString(), "不是文本类型");
                        errorList.add("食品经营许可证 第" + a + "行"+titleRow.getCell(2).toString()+"不是文本类型");
                    }
                    if ((row.getCell(3) != null)&& row.getCell(3).getCellType() != CellType.BLANK  && row.getCell(3).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(3).toString(), "不是文本类型");
                        errorList.add("食品经营许可证 第" + a + "行"+titleRow.getCell(3).toString()+"不是文本类型");
                    }
                    if ((row.getCell(4) != null) && (row.getCell(4).getCellType()!=CellType.BLANK)&& row.getCell(4).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(4).toString(), "不是文本类型");
                        errorList.add("食品经营许可证 第" + a + "行"+titleRow.getCell(4).toString()+"不是文本类型");
                    }
                    if ((row.getCell(5) != null) &&(row.getCell(5).getCellType()!=CellType.BLANK)&& row.getCell(5).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(5).toString(), "不是文本类型");
                        errorList.add("食品经营许可证 第" + a + "行"+titleRow.getCell(5).toString()+"不是文本类型");
                    }
                    if ((row.getCell(6) != null) && row.getCell(6).getCellType() != CellType.BLANK && row.getCell(6).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(6).toString(), "不是文本类型");
                        errorList.add("食品经营许可证 第" + a + "行"+titleRow.getCell(6).toString()+"不是文本类型");
                    }
                    if ((row.getCell(7) != null) && (row.getCell(7).getCellType()!=CellType.BLANK)&& row.getCell(7).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(7).toString(), "不是文本类型");
                        errorList.add("食品经营许可证 第" + a + "行"+titleRow.getCell(7).toString()+"不是文本类型");
                    }
                    if ((row.getCell(8) != null) && row.getCell(8).getCellType() != CellType.BLANK && row.getCell(8).getCellType() != CellType.NUMERIC) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(8).toString(), "不是文本类型");
                        errorList.add("食品经营许可证 第" + a + "行"+titleRow.getCell(8).toString()+"不是日期类型");
                    }
                    if ((row.getCell(9) != null)&& row.getCell(9).getCellType() != CellType.BLANK  &&row.getCell(9).getCellType() != CellType.NUMERIC) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(9).toString(), "不是文本类型");
                        errorList.add("食品经营许可证 第" + a + "行"+titleRow.getCell(9).toString()+"不是日期类型");
                    }
                    if ((row.getCell(10) != null) && row.getCell(10).getCellType() != CellType.BLANK &&row.getCell(10).getCellType() != CellType.NUMERIC) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(10).toString(), "不是文本类型");
                        errorList.add("食品经营许可证 第" + a + "行"+titleRow.getCell(10).toString()+"不是日期类型");
                    }
                    if ((row.getCell(11) != null)&& row.getCell(11).getCellType() != CellType.BLANK  &&row.getCell(11).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(11).toString(), "不是日期类型");
                        errorList.add("食品经营许可证 第" + a + "行"+titleRow.getCell(11).toString()+"不是文本类型");
                    }
                    if ((row.getCell(12) != null)&& row.getCell(12).getCellType() != CellType.BLANK  && row.getCell(12).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(12).toString(), "不是文本类型");
                        errorList.add("食品经营许可证 第" + a + "行"+titleRow.getCell(12).toString()+"不是文本类型");
                    }
                    if ((row.getCell(13) != null) && row.getCell(13).getCellType() != CellType.BLANK && row.getCell(13).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(13).toString(),"不是文本类型");
                        errorList.add("食品经营许可证 第" + a + "行"+titleRow.getCell(13).toString()+"不是文本类型");
                    }
                    if ((row.getCell(14) != null)&& row.getCell(14).getCellType() != CellType.BLANK && row.getCell(14).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(14).toString(),"不是数字类型");
                        errorList.add("食品经营许可证 第" + a + "行"+titleRow.getCell(14).toString()+"不是文本类型");
                    }
                    if ((row.getCell(15) != null) && row.getCell(15).getCellType() != CellType.BLANK && row.getCell(15).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(15).toString(),"不是数字类型");
                        errorList.add("食品经营许可证 第" + a + "行"+titleRow.getCell(15).toString()+"不是文本类型");
                    }
                    if ((row.getCell(16) != null) && row.getCell(16).getCellType() != CellType.BLANK && row.getCell(16).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(16).toString(),"不是文本类型");
                        errorList.add("食品经营许可证 第" + a + "行"+titleRow.getCell(16).toString()+"不是文本类型");
                    }
                }
                if (!errorList.isEmpty()) {
                    System.out.println(errorList);
                    throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, errorList);
                }
                for (int j = 0; j < sheet1.getPhysicalNumberOfRows(); j++) {
                    if (j == 0) {
                        continue;//标题行
                    }
                    SupervisionEnFoodBu supervisionEnFoodBu = new SupervisionEnFoodBu();
                    SupervisionEnFoodBuIndex supervisionEnFoodBuIndex = new SupervisionEnFoodBuIndex();
                    XSSFRow row = sheet1.getRow(j);
                    if(enterpriseIdMap.get(ExcalUtils.handleStringXSSF(row.getCell(1)))!=null)
                    {
                        int id= enterpriseIdMap.get(ExcalUtils.handleStringXSSF(row.getCell(1)));
                        supervisionEnFoodBuIndex.setEnterpriseId(id);
                    }
                    supervisionEnFoodBu.setBusinessName(ExcalUtils.handleStringXSSF(row.getCell(0)));
                    supervisionEnFoodBu.setNumber(ExcalUtils.handleStringXSSF(row.getCell(2)));
                    supervisionEnFoodBu.setBusinessFormat(ExcalUtils.handleStringXSSF(row.getCell(3)));
                    supervisionEnFoodBu.setCategory(ExcalUtils.handleStringXSSF(row.getCell(4)));
                    supervisionEnFoodBu.setBusinessNotes(ExcalUtils.handleStringXSSF(row.getCell(5)));
                    supervisionEnFoodBu.setBusinessProject(ExcalUtils.handleStringXSSF(row.getCell(6)));
                    supervisionEnFoodBu.setBusinessAddress(ExcalUtils.handleStringXSSF(row.getCell(7)));
                    supervisionEnFoodBu.setStartTime(ExcalUtils.handleDateXSSF(row.getCell(8)));
                    supervisionEnFoodBu.setEndTime(ExcalUtils.handleDateXSSF(row.getCell(9)));
                    supervisionEnFoodBu.setGiveTime(ExcalUtils.handleDateXSSF(row.getCell(10)));
                    supervisionEnFoodBu.setLicenseAuthority(ExcalUtils.handleStringXSSF(row.getCell(11)));
                    supervisionEnFoodBu.setCheckType(ExcalUtils.handleStringXSSF(row.getCell(12)));
                    supervisionEnFoodBu.setDynamicGrade(ExcalUtils.handleStringXSSF(row.getCell(13)));
                    supervisionEnFoodBu.setYearAssessment(ExcalUtils.handleStringXSSF(row.getCell(14)));
                    supervisionEnFoodBu.setPatrolFrequency(ExcalUtils.handleStringXSSF(row.getCell(15)));
                    supervisionEnFoodBu.setEnterpriseScale(ExcalUtils.handleStringXSSF(row.getCell(16)));
                    supervisionEnFoodBu.setOperator("操作人");
                    supervisionEnFoodBu.setOperateIp("123.123.123");
                    supervisionEnFoodBu.setOperateTime(new Date());
                    if(!supervisionEnFoodBu.getNumber().equals("")) {
                        if(numberMap.get(supervisionEnFoodBu.getNumber())!=null)
                        {
                            //只进行许可证表的更新
                            int id = numberMap.get(supervisionEnFoodBu.getNumber());
                            supervisionEnFoodBu.setId(id);
                            id = numberFoodBuIndexMap.get(supervisionEnFoodBu.getNumber());
                            supervisionEnFoodBu.setIndexId(id);
                            supervisionEnFoodBuMapper.updateByPrimaryKey(supervisionEnFoodBu);
                        }
                        else{
                            supervisionEnFoodBuIndex.setNumber(supervisionEnFoodBu.getNumber());
                            supervisionEnFoodBuIndex.setEndTime(new Date());
                            int indexId=supervisionEnFoodBuIndexMapper.insertSelective(supervisionEnFoodBuIndex);
                            supervisionEnFoodBu.setIndexId(indexId);
                            supervisionEnFoodBuMapper.insertSelective(supervisionEnFoodBu);
                        }
                    }
                }

                //食品生产许可证
                List<SupervisionEnFoodPro> supervisionEnFoodProList1 = supervisionEnFoodProMapper.getAll();
                Map <String,Integer> numberFoodProMap =new HashMap<>();
                for (SupervisionEnFoodPro supervisionEnFoodPro:supervisionEnFoodProList1){
                    numberFoodProMap.put(supervisionEnFoodPro.getNumber(),supervisionEnFoodPro.getId());
                }

                List<SupervisionEnFoodProIndex> supervisionEnFoodProIndexList = supervisionEnFoodProIndexMapper.getAll();
                Map <String,Integer> numberFoodBuProIndexMap =new HashMap<>();
                for (SupervisionEnFoodProIndex supervisionEnFoodProIndex:supervisionEnFoodProIndexList){
                    numberFoodBuProIndexMap.put(supervisionEnFoodProIndex.getNumber(),supervisionEnFoodProIndex.getId());
                }
                XSSFSheet sheet2 = workbook.getSheetAt(2);
                for (int j = 0; j <sheet2.getPhysicalNumberOfRows(); j++) {
                    if (j == 0) {
                        continue;//标题行
                    }
                    XSSFRow titleRow = sheet2.getRow(0);
                    XSSFRow row = sheet2.getRow(j);
                    if((row.getCell(1).getCellType()==CellType.BLANK)&&(row.getCell(2).getCellType()==CellType.BLANK))
                    {
                        break;
                    }
                    int a = j + 1;
                    if(row.getCell(1).getCellType()==CellType.BLANK)
                    {
                        errorList.add("食品生产许可证 第" + a + "行"+titleRow.getCell(1).toString()+"为空");
                    }
                    if(row.getCell(2).getCellType()==CellType.BLANK)
                    {
                        errorList.add("食品生产许可证 第" + a + "行"+titleRow.getCell(2).toString()+"为空");
                    }
                    if(row.getCell(9).getCellType()==CellType.BLANK)
                    {
                        errorList.add("食品生产许可证 第" + a + "行"+titleRow.getCell(9).toString()+"为空");
                    }
                    if ((row.getCell(0) != null) && row.getCell(0).getCellType() != CellType.BLANK && row.getCell(0).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(0).toString(), "不是文本类型");
                        errorList.add("食品生产许可证 第" + a + "行"+titleRow.getCell(0).toString()+"不是文本类型");
                    }
                    if ((row.getCell(1) != null) && row.getCell(1).getCellType() != CellType.BLANK && row.getCell(1).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(1).toString(), "不是文本类型");
                        errorList.add("食品生产许可证 第" + a + "行"+titleRow.getCell(1).toString()+"不是文本类型");
                    }
                    if ((row.getCell(2) != null) && row.getCell(2).getCellType() != CellType.BLANK  && row.getCell(2).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(2).toString(), "不是文本类型");
                        errorList.add("食品生产许可证 第" + a + "行"+titleRow.getCell(2).toString()+"不是文本类型");
                    }
                    if ((row.getCell(3) != null)&& row.getCell(3).getCellType() != CellType.BLANK  && row.getCell(3).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(3).toString(), "不是文本类型");
                        errorList.add("食品生产许可证 第" + a + "行"+titleRow.getCell(3).toString()+"不是文本类型");
                    }
                    if ((row.getCell(4) != null) && (row.getCell(4).getCellType()!=CellType.BLANK)&& row.getCell(4).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(4).toString(), "不是文本类型");
                        errorList.add("食品生产许可证 第" + a + "行"+titleRow.getCell(4).toString()+"不是文本类型");
                    }
                    if ((row.getCell(5) != null) &&(row.getCell(5).getCellType()!=CellType.BLANK)&& row.getCell(5).getCellType() != CellType.NUMERIC) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(5).toString(), "不是文本类型");
                        errorList.add("食品生产许可证 第" + a + "行"+titleRow.getCell(5).toString()+"不是文本类型");
                    }
                    if ((row.getCell(6) != null) && row.getCell(6).getCellType() != CellType.BLANK && row.getCell(6).getCellType() != CellType.NUMERIC) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(6).toString(), "不是文本类型");
                        errorList.add("食品生产许可证 第" + a + "行"+titleRow.getCell(6).toString()+"不是文本类型");
                    }
                    if ((row.getCell(7) != null) && (row.getCell(7).getCellType()!=CellType.BLANK)&& row.getCell(7).getCellType() != CellType.NUMERIC) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(7).toString(), "不是文本类型");
                        errorList.add("食品生产许可证 第" + a + "行"+titleRow.getCell(7).toString()+"不是文本类型");
                    }
                    if ((row.getCell(8) != null) && row.getCell(8).getCellType() != CellType.BLANK && row.getCell(8).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(8).toString(), "不是文本类型");
                        errorList.add("食品生产许可证 第" + a + "行"+titleRow.getCell(8).toString()+"不是日期类型");
                    }
                    if ((row.getCell(9) != null)&& row.getCell(9).getCellType() != CellType.BLANK  &&row.getCell(9).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(9).toString(), "不是文本类型");
                        errorList.add("食品生产许可证 第" + a + "行"+titleRow.getCell(9).toString()+"不是日期类型");
                    }
                    if ((row.getCell(10) != null) && row.getCell(10).getCellType() != CellType.BLANK &&row.getCell(10).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(10).toString(), "不是文本类型");
                        errorList.add("食品生产许可证 第" + a + "行"+titleRow.getCell(10).toString()+"不是日期类型");
                    }
                    if ((row.getCell(11) != null)&& row.getCell(11).getCellType() != CellType.BLANK  &&row.getCell(11).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(11).toString(), "不是日期类型");
                        errorList.add("食品生产许可证 第" + a + "行"+titleRow.getCell(11).toString()+"不是文本类型");
                    }
                    if ((row.getCell(12) != null)&& row.getCell(12).getCellType() != CellType.BLANK  && row.getCell(12).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(12).toString(), "不是文本类型");
                        errorList.add("食品生产许可证 第" + a + "行"+titleRow.getCell(12).toString()+"不是文本类型");
                    }
                    if ((row.getCell(13) != null) && row.getCell(13).getCellType() != CellType.BLANK && row.getCell(13).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(13).toString(),"不是文本类型");
                        errorList.add("食品生产许可证 第" + a + "行"+titleRow.getCell(13).toString()+"不是文本类型");
                    }
                }
                if (!errorList.isEmpty()) {
                    System.out.println(errorList);
                    throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, errorList);
                }
                for (int j = 0; j < sheet2.getPhysicalNumberOfRows(); j++) {
                    if (j == 0) {
                        continue;//标题行
                    }
                    SupervisionEnFoodPro supervisionEnFoodPro = new SupervisionEnFoodPro();
                    SupervisionEnFoodProIndex supervisionEnFoodProIndex = new SupervisionEnFoodProIndex();
                    XSSFRow row = sheet2.getRow(j);
                    if(enterpriseIdMap.get(ExcalUtils.handleStringXSSF(row.getCell(1)))!=null)
                    {
                        int id= enterpriseIdMap.get(ExcalUtils.handleStringXSSF(row.getCell(1)));
                        supervisionEnFoodProIndex.setEnterpriseId(id);
                    }
                    supervisionEnFoodPro.setBusinessName(ExcalUtils.handleStringXSSF(row.getCell(0)));
                    supervisionEnFoodPro.setNumber(ExcalUtils.handleStringXSSF(row.getCell(2)));
                    supervisionEnFoodPro.setFoodType(ExcalUtils.handleStringXSSF(row.getCell(3)));
                    supervisionEnFoodPro.setBusinessAddress(ExcalUtils.handleStringXSSF(row.getCell(4)));
                    supervisionEnFoodPro.setStartTime(ExcalUtils.handleDateXSSF(row.getCell(5)));
                    supervisionEnFoodPro.setEndTime(ExcalUtils.handleDateXSSF(row.getCell(6)));
                    supervisionEnFoodPro.setGiveTime(ExcalUtils.handleDateXSSF(row.getCell(7)));
                    supervisionEnFoodPro.setLicenseAuthority(ExcalUtils.handleStringXSSF(row.getCell(8)));
                    supervisionEnFoodPro.setCheckType(ExcalUtils.handleStringXSSF(row.getCell(9)));
                    supervisionEnFoodPro.setDynamicGrade(ExcalUtils.handleStringXSSF(row.getCell(10)));
                    supervisionEnFoodPro.setYearAssessment(ExcalUtils.handleStringXSSF(row.getCell(11)));
                    supervisionEnFoodPro.setPatrolFrequency(ExcalUtils.handleStringXSSF(row.getCell(12)));
                    supervisionEnFoodPro.setEnterpriseScale(ExcalUtils.handleStringXSSF(row.getCell(13)));
                    supervisionEnFoodPro.setOperator("操作人");
                    supervisionEnFoodPro.setOperatorIp("123.123.123");
                    supervisionEnFoodPro.setOperatorTime(new Date());
                    if(!supervisionEnFoodPro.getNumber().equals("")) {
                        if(numberFoodProMap.get(supervisionEnFoodPro.getNumber())!=null)
                        {
                            //只进行许可证表的更新
                            int id = numberFoodProMap.get(supervisionEnFoodPro.getNumber());
                            supervisionEnFoodPro.setId(id);
                            id = numberFoodBuProIndexMap.get(supervisionEnFoodPro.getNumber());
                            supervisionEnFoodPro.setIndexId(id);
                            supervisionEnFoodProMapper.updateByPrimaryKey(supervisionEnFoodPro);
                        }
                        else{
                            supervisionEnFoodProIndex.setNumber(supervisionEnFoodPro.getNumber());
                            supervisionEnFoodProIndex.setEndTime(new Date());
                            int indexId=supervisionEnFoodProIndexMapper.insertSelective(supervisionEnFoodProIndex);
                            supervisionEnFoodPro.setIndexId(indexId);
                            supervisionEnFoodProMapper.insertSelective(supervisionEnFoodPro);
                        }
                    }
                }

                //食品生产添加剂
                supervisionEnFoodProList1 = supervisionEnFoodProMapper.getAll();
                numberFoodProMap =new HashMap<>();
                for (SupervisionEnFoodPro supervisionEnFoodPro:supervisionEnFoodProList1){
                    numberFoodProMap.put(supervisionEnFoodPro.getNumber(),supervisionEnFoodPro.getId());
                }
                List<SupervisionEnProCategory> supervisionEnProCategoryList = supervisionEnProCategoryMapper.getAll();
                Map <Integer,Integer> numberProCategoryMap =new HashMap<>();
                for (SupervisionEnProCategory supervisionEnProCategory:supervisionEnProCategoryList){
                    numberProCategoryMap.put(supervisionEnProCategory.getParentId(),supervisionEnProCategory.getId());
                }
                XSSFSheet sheet3 = workbook.getSheetAt(3);
                for (int j = 0; j <sheet3.getPhysicalNumberOfRows(); j++) {
                    if (j == 0) {
                        continue;//标题行
                    }
                    XSSFRow titleRow = sheet3.getRow(0);
                    XSSFRow row = sheet3.getRow(j);
                    if((row.getCell(0).getCellType()==CellType.BLANK)&&(row.getCell(1).getCellType()==CellType.BLANK))
                    {
                        break;
                    }
                    int a = j + 1;
                    if(row.getCell(0).getCellType()==CellType.BLANK)
                    {
                        errorList.add("食品生产许可证 第" + a + "行"+titleRow.getCell(0).toString()+"为空");
                    }
                    if(row.getCell(1).getCellType()==CellType.BLANK)
                    {
                        errorList.add("食品生产许可证 第" + a + "行"+titleRow.getCell(1).toString()+"为空");
                    }

                    if ((row.getCell(0) != null) && row.getCell(0).getCellType() != CellType.BLANK && row.getCell(0).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(0).toString(), "不是文本类型");
                        errorList.add("食品生产许可证 第" + a + "行"+titleRow.getCell(0).toString()+"不是文本类型");
                    }
                    if ((row.getCell(1) != null) && row.getCell(1).getCellType() != CellType.BLANK && row.getCell(1).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(1).toString(), "不是文本类型");
                        errorList.add("食品生产许可证 第" + a + "行"+titleRow.getCell(1).toString()+"不是文本类型");
                    }
                    if ((row.getCell(2) != null) && row.getCell(2).getCellType() != CellType.BLANK  && row.getCell(2).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(2).toString(), "不是文本类型");
                        errorList.add("食品生产许可证 第" + a + "行"+titleRow.getCell(2).toString()+"不是文本类型");
                    }
                    if ((row.getCell(3) != null)&& row.getCell(3).getCellType() != CellType.BLANK  && row.getCell(3).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(3).toString(), "不是文本类型");
                        errorList.add("食品生产许可证 第" + a + "行"+titleRow.getCell(3).toString()+"不是文本类型");
                    }
                    if ((row.getCell(4) != null) && (row.getCell(4).getCellType()!=CellType.BLANK)&& row.getCell(4).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(4).toString(), "不是文本类型");
                        errorList.add("食品生产许可证 第" + a + "行"+titleRow.getCell(4).toString()+"不是文本类型");
                    }
                    if ((row.getCell(5) != null) &&(row.getCell(5).getCellType()!=CellType.BLANK)&& row.getCell(5).getCellType() != CellType.NUMERIC) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(5).toString(), "不是文本类型");
                        errorList.add("食品生产许可证 第" + a + "行"+titleRow.getCell(5).toString()+"不是文本类型");
                    }
                }
                if (!errorList.isEmpty()) {
                    System.out.println(errorList);
                    throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, errorList);
                }
                for (int j = 0; j < sheet3.getPhysicalNumberOfRows(); j++) {
                    if (j == 0) {
                        continue;//标题行
                    }
                    SupervisionEnProCategory supervisionEnProCategory = new SupervisionEnProCategory();
                    XSSFRow row = sheet3.getRow(j);
                    if(numberFoodProMap.get(ExcalUtils.handleStringXSSF(row.getCell(1)))!=null)
                    {
                        int id= numberFoodProMap.get(ExcalUtils.handleStringXSSF(row.getCell(1)));
                        supervisionEnProCategory.setParentId(id);
                    }
                    supervisionEnProCategory.setCategory(ExcalUtils.handleStringXSSF(row.getCell(2)));
                    supervisionEnProCategory.setCode(ExcalUtils.handleStringXSSF(row.getCell(3)));
                    supervisionEnProCategory.setName(ExcalUtils.handleStringXSSF(row.getCell(4)));
                    supervisionEnProCategory.setDetail(ExcalUtils.handleStringXSSF(row.getCell(5)));
                    supervisionEnProCategory.setOperator("操作人");
                    supervisionEnProCategory.setOperateIp("123.123.123");
                    supervisionEnProCategory.setOperateTime(new Date());
                        if(numberProCategoryMap.get(supervisionEnProCategory.getParentId())!=null)
                        {
                            //只进行许可证表的更新
                            int id = numberProCategoryMap.get(supervisionEnProCategory.getParentId());
                            supervisionEnProCategory.setId(id);
                            supervisionEnProCategoryMapper.updateByPrimaryKey(supervisionEnProCategory);
                        }
                        else{
                            supervisionEnProCategoryMapper.insertSelective(supervisionEnProCategory);
                        }
                }

                //药品经营许可证
                List<SupervisionEnDrugsBu> supervisionEnDrugsBuList = supervisionEnDrugsBuMapper.getAll();
                Map <String,Integer> numberDrugsMap =new HashMap<>();
                for (SupervisionEnDrugsBu supervisionEnDrugsBu:supervisionEnDrugsBuList){
                    numberDrugsMap.put(supervisionEnDrugsBu.getNumber(),supervisionEnDrugsBu.getId());
                }
                List<SupervisionEnDrugsBuIndex> supervisionEnDrugsBuIndexList = supervisionEnDrugsBuIndexMapper.getAll();
                Map <String,Integer> numberDrugsIndexMap =new HashMap<>();
                for (SupervisionEnDrugsBuIndex supervisionEnDrugsBuIndex:supervisionEnDrugsBuIndexList){
                    numberDrugsIndexMap.put(supervisionEnDrugsBuIndex.getNumber(),supervisionEnDrugsBuIndex.getId());
                }
                XSSFSheet sheet6 = workbook.getSheetAt(6);
                for (int j = 0; j <sheet6.getPhysicalNumberOfRows(); j++) {
                    if (j == 0) {
                        continue;//标题行
                    }
                    XSSFRow titleRow = sheet6.getRow(0);
                    XSSFRow row = sheet6.getRow(j);
                    XSSFRow nextRow = sheet6.getRow(j+1);
                    if((row.getCell(1).getCellType()==CellType.BLANK)&&(row.getCell(2).getCellType()==CellType.BLANK))
                    {
                        break;
                    }
                    int a = j + 1;
                    if(row.getCell(1).getCellType()==CellType.BLANK)
                    {
                        errorList.add("药品经营许可证 第" + a + "行"+titleRow.getCell(1).toString()+"为空");
                    }
                    if(row.getCell(2).getCellType()==CellType.BLANK)
                    {
                        errorList.add("药品经营许可证 第" + a + "行"+titleRow.getCell(2).toString()+"为空");
                    }
                    if(row.getCell(12).getCellType()==CellType.BLANK)
                    {
                        errorList.add("药品经营许可证 第" + a + "行"+titleRow.getCell(12).toString()+"为空");
                    }
                    if ((row.getCell(0) != null) && row.getCell(0).getCellType() != CellType.BLANK && row.getCell(0).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(0).toString(), "不是文本类型");
                        errorList.add("药品经营许可证 第" + a + "行"+titleRow.getCell(0).toString()+"不是文本类型");
                    }
                    if ((row.getCell(1) != null) && row.getCell(1).getCellType() != CellType.BLANK && row.getCell(1).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(1).toString(), "不是文本类型");
                        errorList.add("药品经营许可证 第" + a + "行"+titleRow.getCell(1).toString()+"不是文本类型");
                    }
                    if ((row.getCell(2) != null) && row.getCell(2).getCellType() != CellType.BLANK  && row.getCell(2).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(2).toString(), "不是文本类型");
                        errorList.add("药品经营许可证 第" + a + "行"+titleRow.getCell(2).toString()+"不是文本类型");
                    }
                    if ((row.getCell(3) != null)&& row.getCell(3).getCellType() != CellType.BLANK  && row.getCell(3).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(3).toString(), "不是文本类型");
                        errorList.add("药品经营许可证 第" + a + "行"+titleRow.getCell(3).toString()+"不是文本类型");
                    }
                    if ((row.getCell(4) != null) && (row.getCell(4).getCellType()!=CellType.BLANK)&& row.getCell(4).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(4).toString(), "不是文本类型");
                        errorList.add("药品经营许可证 第" + a + "行"+titleRow.getCell(4).toString()+"不是文本类型");
                    }
                    if ((row.getCell(5) != null) &&(row.getCell(5).getCellType()!=CellType.BLANK)&& row.getCell(5).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(5).toString(), "不是文本类型");
                        errorList.add("药品经营许可证 第" + a + "行"+titleRow.getCell(5).toString()+"不是文本类型");
                    }
                    if ((row.getCell(6) != null) && row.getCell(6).getCellType() != CellType.BLANK && row.getCell(6).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(6).toString(), "不是文本类型");
                        errorList.add("药品经营许可证 第" + a + "行"+titleRow.getCell(6).toString()+"不是文本类型");
                    }
                    if ((row.getCell(7) != null) && (row.getCell(7).getCellType()!=CellType.BLANK)&& row.getCell(7).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(7).toString(), "不是文本类型");
                        errorList.add("药品经营许可证 第" + a + "行"+titleRow.getCell(7).toString()+"不是文本类型");
                    }
                    if ((row.getCell(8) != null) && row.getCell(8).getCellType() != CellType.BLANK && row.getCell(8).getCellType() != CellType.NUMERIC) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(8).toString(), "不是文本类型");
                        errorList.add("药品经营许可证 第" + a + "行"+titleRow.getCell(8).toString()+"不是日期类型");
                    }
                    if ((row.getCell(9) != null)&& row.getCell(9).getCellType() != CellType.BLANK  &&row.getCell(9).getCellType() != CellType.NUMERIC) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(9).toString(), "不是文本类型");
                        errorList.add("药品经营许可证 第" + a + "行"+titleRow.getCell(9).toString()+"不是日期类型");
                    }
                    if ((row.getCell(10) != null) && row.getCell(10).getCellType() != CellType.BLANK &&row.getCell(10).getCellType() != CellType.NUMERIC) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(10).toString(), "不是文本类型");
                        errorList.add("药品经营许可证 第" + a + "行"+titleRow.getCell(10).toString()+"不是日期类型");
                    }
                    if ((row.getCell(11) != null)&& row.getCell(11).getCellType() != CellType.BLANK  &&row.getCell(11).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(11).toString(), "不是日期类型");
                        errorList.add("药品经营许可证 第" + a + "行"+titleRow.getCell(11).toString()+"不是文本类型");
                    }
                    if ((row.getCell(12) != null)&& row.getCell(12).getCellType() != CellType.BLANK  && row.getCell(12).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(12).toString(), "不是文本类型");
                        errorList.add("药品经营许可证 第" + a + "行"+titleRow.getCell(12).toString()+"不是文本类型");
                    }
                    if ((row.getCell(13) != null) && row.getCell(13).getCellType() != CellType.BLANK && row.getCell(13).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(13).toString(),"不是文本类型");
                        errorList.add("药品经营许可证 第" + a + "行"+titleRow.getCell(13).toString()+"不是文本类型");
                    }
                    if ((row.getCell(14) != null)&& row.getCell(14).getCellType() != CellType.BLANK && row.getCell(14).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(14).toString(),"不是数字类型");
                        errorList.add("药品经营许可证 第" + a + "行"+titleRow.getCell(14).toString()+"不是文本类型");
                    }
                    if ((row.getCell(15) != null) && row.getCell(15).getCellType() != CellType.BLANK && row.getCell(15).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(15).toString(),"不是数字类型");
                        errorList.add("药品经营许可证 第" + a + "行"+titleRow.getCell(15).toString()+"不是文本类型");
                    }
                    if ((row.getCell(16) != null) && row.getCell(16).getCellType() != CellType.BLANK && row.getCell(16).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(16).toString(),"不是文本类型");
                        errorList.add("药品经营许可证 第" + a + "行"+titleRow.getCell(16).toString()+"不是文本类型");
                    }
                }
                if (!errorList.isEmpty()) {
                    System.out.println(errorList);
                    throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, errorList);
                }
                for (int j = 0; j < sheet6.getPhysicalNumberOfRows(); j++) {
                    if (j == 0) {
                        continue;//标题行
                    }
                    SupervisionEnDrugsBu supervisionEnDrugsBu = new SupervisionEnDrugsBu();
                    SupervisionEnDrugsBuIndex supervisionEnDrugsBuIndex = new SupervisionEnDrugsBuIndex();
                    XSSFRow row = sheet6.getRow(j);
                    if(enterpriseIdMap.get(ExcalUtils.handleStringXSSF(row.getCell(1)))!=null)
                    {
                        int id= enterpriseIdMap.get(ExcalUtils.handleStringXSSF(row.getCell(1)));
                        supervisionEnDrugsBuIndex.setEnterpriseId(id);
                    }
                    supervisionEnDrugsBu.setBusinessName(ExcalUtils.handleStringXSSF(row.getCell(0)));
                    supervisionEnDrugsBu.setNumber(ExcalUtils.handleStringXSSF(row.getCell(2)));
                    supervisionEnDrugsBu.setOperationMode(ExcalUtils.handleStringXSSF(row.getCell(3)));
                    supervisionEnDrugsBu.setBusinessScope(ExcalUtils.handleStringXSSF(row.getCell(4)));
                    supervisionEnDrugsBu.setEnterprisePerson(ExcalUtils.handleStringXSSF(row.getCell(5)));
                    supervisionEnDrugsBu.setQualityPerson(ExcalUtils.handleStringXSSF(row.getCell(6)));
                    supervisionEnDrugsBu.setBusinessAddress(ExcalUtils.handleStringXSSF(row.getCell(7)));
                    supervisionEnDrugsBu.setStartTime(ExcalUtils.handleDateXSSF(row.getCell(8)));
                    supervisionEnDrugsBu.setEndTime(ExcalUtils.handleDateXSSF(row.getCell(9)));
                    supervisionEnDrugsBu.setGiveTime(ExcalUtils.handleDateXSSF(row.getCell(10)));
                    supervisionEnDrugsBu.setLicenseAuthority(ExcalUtils.handleStringXSSF(row.getCell(11)));
                    supervisionEnDrugsBu.setCheckType(ExcalUtils.handleStringXSSF(row.getCell(12)));
                    supervisionEnDrugsBu.setDynamicGrade(ExcalUtils.handleStringXSSF(row.getCell(13)));
                    supervisionEnDrugsBu.setYearAssessment(ExcalUtils.handleStringXSSF(row.getCell(14)));
                    supervisionEnDrugsBu.setPatrolFrequency(ExcalUtils.handleStringXSSF(row.getCell(15)));
                    supervisionEnDrugsBu.setEnterpriseScale(ExcalUtils.handleStringXSSF(row.getCell(16)));
                    supervisionEnDrugsBu.setOperator("操作人");
                    supervisionEnDrugsBu.setOperatorIp("123.123.123");
                    supervisionEnDrugsBu.setOperatorTime(new Date());
                    if(!supervisionEnDrugsBu.getNumber().equals("")) {
                        if(numberDrugsMap.get(supervisionEnDrugsBu.getNumber())!=null)
                        {
                            //只进行许可证表的更新
                            int id = numberDrugsMap.get(supervisionEnDrugsBu.getNumber());
                            supervisionEnDrugsBu.setId(id);
                            id=numberDrugsIndexMap.get(supervisionEnDrugsBu.getNumber());
                            supervisionEnDrugsBu.setIndexId(id);
                            supervisionEnDrugsBuMapper.updateByPrimaryKey(supervisionEnDrugsBu);
                        }
                        else{
                            supervisionEnDrugsBuIndex.setNumber(supervisionEnDrugsBu.getNumber());
                            supervisionEnDrugsBuIndex.setEndTime(new Date());
                            int indexId = supervisionEnDrugsBuIndexMapper.insertSelective(supervisionEnDrugsBuIndex);
                            supervisionEnDrugsBu.setIndexId(indexId);
                            supervisionEnDrugsBuMapper.insertSelective(supervisionEnDrugsBu);
                        }
                    }
                }

                //药品生产许可证
                List<SupervisionEnDrugsPro> supervisionEnDrugsProsList = supervisionEnDrugsProMapper.getAll();
                Map <String,Integer> numberDrugsProMap =new HashMap<>();
                for (SupervisionEnDrugsPro supervisionEnDrugsPro:supervisionEnDrugsProsList){
                    numberDrugsProMap.put(supervisionEnDrugsPro.getNumber(),supervisionEnDrugsPro.getId());
                }
                List<SupervisionEnDrugsProIndex> supervisionEnDrugsProIndexList = supervisionEnDrugsProIndexMapper.getAll();
                Map <String,Integer> numberDrugsProIndexMap =new HashMap<>();
                for (SupervisionEnDrugsProIndex supervisionEnDrugsProIndex:supervisionEnDrugsProIndexList){
                    numberDrugsProIndexMap.put(supervisionEnDrugsProIndex.getNumber(),supervisionEnDrugsProIndex.getId());
                }

                XSSFSheet sheet7 = workbook.getSheetAt(7);
                for (int j = 0; j <sheet7.getPhysicalNumberOfRows(); j++) {
                    if (j == 0) {
                        continue;//标题行
                    }
                    XSSFRow titleRow = sheet7.getRow(0);
                    XSSFRow row = sheet7.getRow(j);
                    XSSFRow nextRow = sheet7.getRow(j+1);
                    if((row.getCell(1).getCellType()==CellType.BLANK)&&(row.getCell(2).getCellType()==CellType.BLANK))
                    {
                        break;
                    }
                    int a = j + 1;
                    if(row.getCell(1).getCellType()==CellType.BLANK)
                    {
                        errorList.add("药品生产许可证 第" + a + "行"+titleRow.getCell(1).toString()+"为空");
                    }
                    if(row.getCell(2).getCellType()==CellType.BLANK)
                    {
                        errorList.add("药品生产许可证 第" + a + "行"+titleRow.getCell(2).toString()+"为空");
                    }
                    if(row.getCell(12).getCellType()==CellType.BLANK)
                    {
                        errorList.add("药品生产许可证 第" + a + "行"+titleRow.getCell(12).toString()+"为空");
                    }
                    if ((row.getCell(0) != null) && row.getCell(0).getCellType() != CellType.BLANK && row.getCell(0).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(0).toString(), "不是文本类型");
                        errorList.add("药品生产许可证 第" + a + "行"+titleRow.getCell(0).toString()+"不是文本类型");
                    }
                    if ((row.getCell(1) != null) && row.getCell(1).getCellType() != CellType.BLANK && row.getCell(1).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(1).toString(), "不是文本类型");
                        errorList.add("药品生产许可证 第" + a + "行"+titleRow.getCell(1).toString()+"不是文本类型");
                    }
                    if ((row.getCell(2) != null) && row.getCell(2).getCellType() != CellType.BLANK  && row.getCell(2).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(2).toString(), "不是文本类型");
                        errorList.add("药品生产许可证 第" + a + "行"+titleRow.getCell(2).toString()+"不是文本类型");
                    }
                    if ((row.getCell(3) != null)&& row.getCell(3).getCellType() != CellType.BLANK  && row.getCell(3).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(3).toString(), "不是文本类型");
                        errorList.add("药品生产许可证 第" + a + "行"+titleRow.getCell(3).toString()+"不是文本类型");
                    }
                    if ((row.getCell(4) != null) && (row.getCell(4).getCellType()!=CellType.BLANK)&& row.getCell(4).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(4).toString(), "不是文本类型");
                        errorList.add("药品生产许可证 第" + a + "行"+titleRow.getCell(4).toString()+"不是文本类型");
                    }
                    if ((row.getCell(5) != null) &&(row.getCell(5).getCellType()!=CellType.BLANK)&& row.getCell(5).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(5).toString(), "不是文本类型");
                        errorList.add("药品生产许可证 第" + a + "行"+titleRow.getCell(5).toString()+"不是文本类型");
                    }
                    if ((row.getCell(6) != null) && row.getCell(6).getCellType() != CellType.BLANK && row.getCell(6).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(6).toString(), "不是文本类型");
                        errorList.add("药品生产许可证 第" + a + "行"+titleRow.getCell(6).toString()+"不是文本类型");
                    }
                    if ((row.getCell(7) != null) && (row.getCell(7).getCellType()!=CellType.BLANK)&& row.getCell(7).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(7).toString(), "不是文本类型");
                        errorList.add("药品生产许可证 第" + a + "行"+titleRow.getCell(7).toString()+"不是文本类型");
                    }
                    if ((row.getCell(8) != null) && row.getCell(8).getCellType() != CellType.BLANK && row.getCell(8).getCellType() != CellType.NUMERIC) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(8).toString(), "不是文本类型");
                        errorList.add("药品生产许可证 第" + a + "行"+titleRow.getCell(8).toString()+"不是日期类型");
                    }
                    if ((row.getCell(9) != null)&& row.getCell(9).getCellType() != CellType.BLANK  &&row.getCell(9).getCellType() != CellType.NUMERIC) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(9).toString(), "不是文本类型");
                        errorList.add("药品生产许可证 第" + a + "行"+titleRow.getCell(9).toString()+"不是日期类型");
                    }
                    if ((row.getCell(10) != null) && row.getCell(10).getCellType() != CellType.BLANK &&row.getCell(10).getCellType() != CellType.NUMERIC) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(10).toString(), "不是文本类型");
                        errorList.add("药品生产许可证 第" + a + "行"+titleRow.getCell(10).toString()+"不是日期类型");
                    }
                    if ((row.getCell(11) != null)&& row.getCell(11).getCellType() != CellType.BLANK  &&row.getCell(11).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(11).toString(), "不是日期类型");
                        errorList.add("药品生产许可证 第" + a + "行"+titleRow.getCell(11).toString()+"不是文本类型");
                    }
                    if ((row.getCell(12) != null)&& row.getCell(12).getCellType() != CellType.BLANK  && row.getCell(12).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(12).toString(), "不是文本类型");
                        errorList.add("药品生产许可证 第" + a + "行"+titleRow.getCell(12).toString()+"不是文本类型");
                    }
                    if ((row.getCell(13) != null) && row.getCell(13).getCellType() != CellType.BLANK && row.getCell(13).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(13).toString(),"不是文本类型");
                        errorList.add("药品生产许可证 第" + a + "行"+titleRow.getCell(13).toString()+"不是文本类型");
                    }
                    if ((row.getCell(14) != null)&& row.getCell(14).getCellType() != CellType.BLANK && row.getCell(14).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(14).toString(),"不是数字类型");
                        errorList.add("药品生产许可证 第" + a + "行"+titleRow.getCell(14).toString()+"不是文本类型");
                    }
                    if ((row.getCell(15) != null) && row.getCell(15).getCellType() != CellType.BLANK && row.getCell(15).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(15).toString(),"不是数字类型");
                        errorList.add("药品生产许可证 第" + a + "行"+titleRow.getCell(15).toString()+"不是文本类型");
                    }
                    if ((row.getCell(16) != null) && row.getCell(16).getCellType() != CellType.BLANK && row.getCell(16).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(16).toString(),"不是文本类型");
                        errorList.add("药品生产许可证 第" + a + "行"+titleRow.getCell(16).toString()+"不是文本类型");
                    }
                }
                if (!errorList.isEmpty()) {
                    System.out.println(errorList);
                    throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, errorList);
                }
                for (int j = 0; j < sheet7.getPhysicalNumberOfRows(); j++) {
                    if (j == 0) {
                        continue;//标题行
                    }
                    SupervisionEnDrugsPro supervisionEnDrugsPro = new SupervisionEnDrugsPro();
                    SupervisionEnDrugsProIndex supervisionEnDrugsProIndex = new SupervisionEnDrugsProIndex();
                    XSSFRow row = sheet7.getRow(j);
                    if(enterpriseIdMap.get(ExcalUtils.handleStringXSSF(row.getCell(1)))!=null)
                    {
                        int id= enterpriseIdMap.get(ExcalUtils.handleStringXSSF(row.getCell(1)));
                        supervisionEnDrugsProIndex.setEnterpriseId(id);
                    }
                    supervisionEnDrugsPro.setBusinessName(ExcalUtils.handleStringXSSF(row.getCell(0)));
                    supervisionEnDrugsPro.setNumber(ExcalUtils.handleStringXSSF(row.getCell(2)));
                    supervisionEnDrugsPro.setOperationMode(ExcalUtils.handleStringXSSF(row.getCell(3)));
                    supervisionEnDrugsPro.setBusinessScope(ExcalUtils.handleStringXSSF(row.getCell(4)));
                    supervisionEnDrugsPro.setEnterprisePerson(ExcalUtils.handleStringXSSF(row.getCell(5)));
                    supervisionEnDrugsPro.setQualityPerson(ExcalUtils.handleStringXSSF(row.getCell(6)));
                    supervisionEnDrugsPro.setBusinessAddress(ExcalUtils.handleStringXSSF(row.getCell(7)));
                    supervisionEnDrugsPro.setStartTime(ExcalUtils.handleDateXSSF(row.getCell(8)));
                    supervisionEnDrugsPro.setEndTime(ExcalUtils.handleDateXSSF(row.getCell(9)));
                    supervisionEnDrugsPro.setGiveTime(ExcalUtils.handleDateXSSF(row.getCell(10)));
                    supervisionEnDrugsPro.setLicenseAuthority(ExcalUtils.handleStringXSSF(row.getCell(11)));
                    supervisionEnDrugsPro.setCheckType(ExcalUtils.handleStringXSSF(row.getCell(12)));
                    supervisionEnDrugsPro.setDynamicGrade(ExcalUtils.handleStringXSSF(row.getCell(13)));
                    supervisionEnDrugsPro.setYearAssessment(ExcalUtils.handleStringXSSF(row.getCell(14)));
                    supervisionEnDrugsPro.setPatrolFrequency(ExcalUtils.handleStringXSSF(row.getCell(15)));
                    supervisionEnDrugsPro.setEnterpriseScale(ExcalUtils.handleStringXSSF(row.getCell(16)));
                    supervisionEnDrugsPro.setOperator("操作人");
                    supervisionEnDrugsPro.setOperatorIp("123.123.123");
                    supervisionEnDrugsPro.setOperatorTime(new Date());
                    if(!supervisionEnDrugsPro.getNumber().equals("")) {
                        if(numberDrugsProMap.get(supervisionEnDrugsPro.getNumber())!=null)
                        {
                            //只进行许可证表的更新
                            int id = numberDrugsProMap.get(supervisionEnDrugsPro.getNumber());
                            supervisionEnDrugsPro.setId(id);
                            id=numberDrugsProIndexMap.get(supervisionEnDrugsPro.getNumber());
                            supervisionEnDrugsPro.setIndexId(id);
                            supervisionEnDrugsProMapper.updateByPrimaryKey(supervisionEnDrugsPro);
                        }
                        else{
                            supervisionEnDrugsProIndex.setNumber(supervisionEnDrugsPro.getNumber());
                            supervisionEnDrugsProIndex.setEndTime(new Date());
                            int indexId=supervisionEnDrugsProIndexMapper.insertSelective(supervisionEnDrugsProIndex);
                            supervisionEnDrugsPro.setIndexId(indexId);
                            supervisionEnDrugsProMapper.insertSelective(supervisionEnDrugsPro);
                        }
                    }
                }

                //化妆品生产许可证
                List<SupervisionEnCosmetics> supervisionEnCosmeticsList = supervisionEnCosmeticsMapper.getAll();
                Map <String,Integer> numberCosmeticsMap =new HashMap<>();
                for (SupervisionEnCosmetics supervisionEnCosmetics:supervisionEnCosmeticsList){
                    numberCosmeticsMap.put(supervisionEnCosmetics.getRegisterCode(),supervisionEnCosmetics.getId());
                }
                List<SupervisionEnCosmeticsIndex> supervisionEnCosmeticsIndexList = supervisionEnCosmeticsIndexMapper.getAll();
                Map <String,Integer> numberCosmeticsIndexMap =new HashMap<>();
                for (SupervisionEnCosmeticsIndex supervisionEnCosmeticsIndex:supervisionEnCosmeticsIndexList){
                    numberCosmeticsIndexMap.put(supervisionEnCosmeticsIndex.getNumber(),supervisionEnCosmeticsIndex.getId());
                }
                XSSFSheet sheet10 = workbook.getSheetAt(10);
                for (int j = 0; j <sheet10.getPhysicalNumberOfRows(); j++) {
                    if (j == 0) {
                        continue;//标题行
                    }
                    XSSFRow titleRow = sheet10.getRow(0);
                    XSSFRow row = sheet10.getRow(j);
                    if((row.getCell(1).getCellType()==CellType.BLANK)&&(row.getCell(2).getCellType()==CellType.BLANK))
                    {
                        break;
                    }
                    int a = j + 1;
                    if(row.getCell(1).getCellType()==CellType.BLANK)
                    {
                        errorList.add("化妆品生产许可证 第" + a + "行"+titleRow.getCell(1).toString()+"为空");
                    }
                    if(row.getCell(2).getCellType()==CellType.BLANK)
                    {
                        errorList.add("化妆品生产许可证 第" + a + "行"+titleRow.getCell(2).toString()+"为空");
                    }
                    if(row.getCell(10).getCellType()==CellType.BLANK)
                    {
                        errorList.add("化妆品生产许可证 第" + a + "行"+titleRow.getCell(10).toString()+"为空");
                    }
                    if ((row.getCell(0) != null) && row.getCell(0).getCellType() != CellType.BLANK && row.getCell(0).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(0).toString(), "不是文本类型");
                        errorList.add("化妆品生产许可证 第" + a + "行"+titleRow.getCell(0).toString()+"不是文本类型");
                    }
                    if ((row.getCell(1) != null) && row.getCell(1).getCellType() != CellType.BLANK && row.getCell(1).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(1).toString(), "不是文本类型");
                        errorList.add("化妆品生产许可证 第" + a + "行"+titleRow.getCell(1).toString()+"不是文本类型");
                    }
                    if ((row.getCell(2) != null) && row.getCell(2).getCellType() != CellType.BLANK  && row.getCell(2).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(2).toString(), "不是文本类型");
                        errorList.add("化妆品生产许可证 第" + a + "行"+titleRow.getCell(2).toString()+"不是文本类型");
                    }
                    if ((row.getCell(3) != null)&& row.getCell(3).getCellType() != CellType.BLANK  && row.getCell(3).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(3).toString(), "不是文本类型");
                        errorList.add("化妆品生产许可证 第" + a + "行"+titleRow.getCell(3).toString()+"不是文本类型");
                    }
                    if ((row.getCell(4) != null) && (row.getCell(4).getCellType()!=CellType.BLANK)&& row.getCell(4).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(4).toString(), "不是文本类型");
                        errorList.add("化妆品生产许可证 第" + a + "行"+titleRow.getCell(4).toString()+"不是文本类型");
                    }
                    if ((row.getCell(5) != null) &&(row.getCell(5).getCellType()!=CellType.BLANK)&& row.getCell(5).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(5).toString(), "不是文本类型");
                        errorList.add("化妆品生产许可证 第" + a + "行"+titleRow.getCell(5).toString()+"不是文本类型");
                    }
                    if ((row.getCell(6) != null) && row.getCell(6).getCellType() != CellType.BLANK && row.getCell(6).getCellType() != CellType.NUMERIC) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(6).toString(), "不是文本类型");
                        errorList.add("化妆品生产许可证 第" + a + "行"+titleRow.getCell(6).toString()+"不是文本类型");
                    }
                    if ((row.getCell(7) != null) && (row.getCell(7).getCellType()!=CellType.BLANK)&& row.getCell(7).getCellType() != CellType.NUMERIC) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(7).toString(), "不是文本类型");
                        errorList.add("化妆品生产许可证 第" + a + "行"+titleRow.getCell(7).toString()+"不是文本类型");
                    }
                    if ((row.getCell(8) != null) && row.getCell(8).getCellType() != CellType.BLANK && row.getCell(8).getCellType() != CellType.NUMERIC) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(8).toString(), "不是文本类型");
                        errorList.add("化妆品生产许可证 第" + a + "行"+titleRow.getCell(8).toString()+"不是日期类型");
                    }
                    if ((row.getCell(9) != null)&& row.getCell(9).getCellType() != CellType.BLANK  &&row.getCell(9).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(9).toString(), "不是文本类型");
                        errorList.add("化妆品生产许可证 第" + a + "行"+titleRow.getCell(9).toString()+"不是日期类型");
                    }
                    if ((row.getCell(10) != null) && row.getCell(10).getCellType() != CellType.BLANK &&row.getCell(10).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(10).toString(), "不是文本类型");
                        errorList.add("化妆品生产许可证 第" + a + "行"+titleRow.getCell(10).toString()+"不是日期类型");
                    }
                    if ((row.getCell(11) != null)&& row.getCell(11).getCellType() != CellType.BLANK  &&row.getCell(11).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(11).toString(), "不是日期类型");
                        errorList.add("化妆品生产许可证 第" + a + "行"+titleRow.getCell(11).toString()+"不是文本类型");
                    }
                    if ((row.getCell(12) != null)&& row.getCell(12).getCellType() != CellType.BLANK  && row.getCell(12).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(12).toString(), "不是文本类型");
                        errorList.add("化妆品生产许可证 第" + a + "行"+titleRow.getCell(12).toString()+"不是文本类型");
                    }
                    if ((row.getCell(13) != null) && row.getCell(13).getCellType() != CellType.BLANK && row.getCell(13).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(13).toString(),"不是文本类型");
                        errorList.add("化妆品生产许可证 第" + a + "行"+titleRow.getCell(13).toString()+"不是文本类型");
                    }
                    if ((row.getCell(14) != null)&& row.getCell(14).getCellType() != CellType.BLANK && row.getCell(14).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(14).toString(),"不是数字类型");
                        errorList.add("化妆品生产许可证 第" + a + "行"+titleRow.getCell(14).toString()+"不是文本类型");
                    }
                }
                if (!errorList.isEmpty()) {
                    System.out.println(errorList);
                    throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, errorList);
                }
                for (int j = 0; j < sheet10.getPhysicalNumberOfRows(); j++) {
                    if (j == 0) {
                        continue;//标题行
                    }
                    SupervisionEnCosmetics supervisionEnCosmetics = new SupervisionEnCosmetics();
                    SupervisionEnCosmeticsIndex supervisionEnCosmeticsIndex = new SupervisionEnCosmeticsIndex();
                    XSSFRow row = sheet10.getRow(j);
                    if(enterpriseIdMap.get(ExcalUtils.handleStringXSSF(row.getCell(1)))!=null)
                    {
                        int id= enterpriseIdMap.get(ExcalUtils.handleStringXSSF(row.getCell(1)));
                        supervisionEnCosmeticsIndex.setEnterpriseId(id);
                    }
                    supervisionEnCosmetics.setBusinessName(ExcalUtils.handleStringXSSF(row.getCell(0)));
                    supervisionEnCosmetics.setRegisterCode(ExcalUtils.handleStringXSSF(row.getCell(2)));
                    supervisionEnCosmetics.setLicenseProject(ExcalUtils.handleStringXSSF(row.getCell(3)));
                    supervisionEnCosmetics.setQualityPerson(ExcalUtils.handleStringXSSF(row.getCell(4)));
                    supervisionEnCosmetics.setBusinessAddress(ExcalUtils.handleStringXSSF(row.getCell(5)));
                    supervisionEnCosmetics.setStartTime(ExcalUtils.handleDateXSSF(row.getCell(6)));
                    supervisionEnCosmetics.setEndTime(ExcalUtils.handleDateXSSF(row.getCell(7)));
                    supervisionEnCosmetics.setGiveTime(ExcalUtils.handleDateXSSF(row.getCell(8)));
                    supervisionEnCosmetics.setLicenseAuthority(ExcalUtils.handleStringXSSF(row.getCell(9)));
                    supervisionEnCosmetics.setCheckType(ExcalUtils.handleStringXSSF(row.getCell(10)));
                    supervisionEnCosmetics.setDynamicGrade(ExcalUtils.handleStringXSSF(row.getCell(11)));
                    supervisionEnCosmetics.setYearAssessment(ExcalUtils.handleStringXSSF(row.getCell(12)));
                    supervisionEnCosmetics.setPatrolFrequency(ExcalUtils.handleStringXSSF(row.getCell(13)));
                    supervisionEnCosmetics.setEnterpriseScale(ExcalUtils.handleStringXSSF(row.getCell(14)));
                    supervisionEnCosmetics.setOperator("操作人");
                    supervisionEnCosmetics.setOperateIp("123.123.123");
                    supervisionEnCosmetics.setOperateTime(new Date());
                    if(!supervisionEnCosmetics.getRegisterCode().equals("")) {
                        if(numberCosmeticsMap.get(supervisionEnCosmetics.getRegisterCode())!=null)
                        {
                            //只进行许可证表的更新
                            int id = numberCosmeticsMap.get(supervisionEnCosmetics.getRegisterCode());
                            supervisionEnCosmetics.setId(id);
                            id=numberCosmeticsIndexMap.get(supervisionEnCosmetics.getRegisterCode());
                            supervisionEnCosmetics.setIndexId(id);
                            supervisionEnCosmeticsMapper.updateByPrimaryKey(supervisionEnCosmetics);
                        }
                        else{
                            supervisionEnCosmeticsIndex.setNumber(supervisionEnCosmetics.getRegisterCode());
                            supervisionEnCosmeticsIndex.setEndTime(new Date());
                            int indexId=supervisionEnCosmeticsIndexMapper.insertSelective(supervisionEnCosmeticsIndex);
                            supervisionEnCosmetics.setIndexId(indexId);
                            supervisionEnCosmeticsMapper.insertSelective(supervisionEnCosmetics);
                        }
                    }
                }

                //医疗器械经营许可证
                List<SupervisionEnMedicalBu> supervisionEnMedicalBuList = supervisionEnMedicalBuMapper.getAll();
                Map <String,Integer> numberMedicalMap =new HashMap<>();
                for (SupervisionEnMedicalBu supervisionEnMedicalBu:supervisionEnMedicalBuList){
                    numberMedicalMap.put(supervisionEnMedicalBu.getRegisterNumber(),supervisionEnMedicalBu.getId());
                }
                List<SupervisionEnMedicalBuIndex> supervisionEnMedicalBuIndexList = supervisionEnMedicalBuIndexMapper.getAll();
                Map <String,Integer> numberMedicalIndexMap =new HashMap<>();
                for (SupervisionEnMedicalBuIndex supervisionEnMedicalBuIndex:supervisionEnMedicalBuIndexList){
                    numberMedicalIndexMap.put(supervisionEnMedicalBuIndex.getNumber(),supervisionEnMedicalBuIndex.getId());
                }
                XSSFSheet sheet8 = workbook.getSheetAt(8);
                for (int j = 0; j <sheet8.getPhysicalNumberOfRows(); j++) {
                    if (j == 0) {
                        continue;//标题行
                    }
                    XSSFRow titleRow = sheet8.getRow(0);
                    XSSFRow row = sheet8.getRow(j);
                    if((row.getCell(1).getCellType()==CellType.BLANK)&&(row.getCell(2).getCellType()==CellType.BLANK))
                    {
                        break;
                    }
                    int a = j + 1;
                    if(row.getCell(1).getCellType()==CellType.BLANK)
                    {
                        errorList.add("医疗器械经营许可证 第" + a + "行"+titleRow.getCell(1).toString()+"为空");
                    }
                    if(row.getCell(2).getCellType()==CellType.BLANK)
                    {
                        errorList.add("医疗器械经营许可证 第" + a + "行"+titleRow.getCell(2).toString()+"为空");
                    }
                    if(row.getCell(10).getCellType()==CellType.BLANK)
                    {
                        errorList.add("医疗器械经营许可证 第" + a + "行"+titleRow.getCell(10).toString()+"为空");
                    }
                    if ((row.getCell(0) != null) && row.getCell(0).getCellType() != CellType.BLANK && row.getCell(0).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(0).toString(), "不是文本类型");
                        errorList.add("医疗器械经营许可证 第" + a + "行"+titleRow.getCell(0).toString()+"不是文本类型");
                    }
                    if ((row.getCell(1) != null) && row.getCell(1).getCellType() != CellType.BLANK && row.getCell(1).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(1).toString(), "不是文本类型");
                        errorList.add("医疗器械经营许可证 第" + a + "行"+titleRow.getCell(1).toString()+"不是文本类型");
                    }
                    if ((row.getCell(2) != null) && row.getCell(2).getCellType() != CellType.BLANK  && row.getCell(2).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(2).toString(), "不是文本类型");
                        errorList.add("医疗器械经营许可证 第" + a + "行"+titleRow.getCell(2).toString()+"不是文本类型");
                    }
                    if ((row.getCell(3) != null)&& row.getCell(3).getCellType() != CellType.BLANK  && row.getCell(3).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(3).toString(), "不是文本类型");
                        errorList.add("医疗器械经营许可证 第" + a + "行"+titleRow.getCell(3).toString()+"不是文本类型");
                    }
                    if ((row.getCell(4) != null) && (row.getCell(4).getCellType()!=CellType.BLANK)&& row.getCell(4).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(4).toString(), "不是文本类型");
                        errorList.add("医疗器械经营许可证 第" + a + "行"+titleRow.getCell(4).toString()+"不是文本类型");
                    }
                    if ((row.getCell(5) != null) &&(row.getCell(5).getCellType()!=CellType.BLANK)&& row.getCell(5).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(5).toString(), "不是文本类型");
                        errorList.add("医疗器械经营许可证 第" + a + "行"+titleRow.getCell(5).toString()+"不是文本类型");
                    }
                    if ((row.getCell(6) != null) && row.getCell(6).getCellType() != CellType.BLANK && row.getCell(6).getCellType() != CellType.NUMERIC) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(6).toString(), "不是文本类型");
                        errorList.add("医疗器械经营许可证 第" + a + "行"+titleRow.getCell(6).toString()+"不是文本类型");
                    }
                    if ((row.getCell(7) != null) && (row.getCell(7).getCellType()!=CellType.BLANK)&& row.getCell(7).getCellType() != CellType.NUMERIC) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(7).toString(), "不是文本类型");
                        errorList.add("医疗器械经营许可证 第" + a + "行"+titleRow.getCell(7).toString()+"不是文本类型");
                    }
                    if ((row.getCell(8) != null) && row.getCell(8).getCellType() != CellType.BLANK && row.getCell(8).getCellType() != CellType.NUMERIC) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(8).toString(), "不是文本类型");
                        errorList.add("医疗器械经营许可证 第" + a + "行"+titleRow.getCell(8).toString()+"不是日期类型");
                    }
                    if ((row.getCell(9) != null)&& row.getCell(9).getCellType() != CellType.BLANK  &&row.getCell(9).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(9).toString(), "不是文本类型");
                        errorList.add("医疗器械经营许可证 第" + a + "行"+titleRow.getCell(9).toString()+"不是日期类型");
                    }
                    if ((row.getCell(10) != null) && row.getCell(10).getCellType() != CellType.BLANK &&row.getCell(10).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(10).toString(), "不是文本类型");
                        errorList.add("医疗器械经营许可证 第" + a + "行"+titleRow.getCell(10).toString()+"不是日期类型");
                    }
                    if ((row.getCell(11) != null)&& row.getCell(11).getCellType() != CellType.BLANK  &&row.getCell(11).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(11).toString(), "不是日期类型");
                        errorList.add("医疗器械经营许可证 第" + a + "行"+titleRow.getCell(11).toString()+"不是文本类型");
                    }
                    if ((row.getCell(12) != null)&& row.getCell(12).getCellType() != CellType.BLANK  && row.getCell(12).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(12).toString(), "不是文本类型");
                        errorList.add("医疗器械经营许可证 第" + a + "行"+titleRow.getCell(12).toString()+"不是文本类型");
                    }
                    if ((row.getCell(13) != null) && row.getCell(13).getCellType() != CellType.BLANK && row.getCell(13).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(13).toString(),"不是文本类型");
                        errorList.add("医疗器械经营许可证 第" + a + "行"+titleRow.getCell(13).toString()+"不是文本类型");
                    }
                    if ((row.getCell(14) != null)&& row.getCell(14).getCellType() != CellType.BLANK && row.getCell(14).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(14).toString(),"不是数字类型");
                        errorList.add("医疗器械经营许可证 第" + a + "行"+titleRow.getCell(14).toString()+"不是文本类型");
                    }
                }
                if (!errorList.isEmpty()) {
                    System.out.println(errorList);
                    throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, errorList);
                }
                for (int j = 0; j < sheet8.getPhysicalNumberOfRows(); j++) {
                    if (j == 0) {
                        continue;//标题行
                    }
                    SupervisionEnMedicalBu supervisionEnMedicalBu = new SupervisionEnMedicalBu();
                    SupervisionEnMedicalBuIndex supervisionEnMedicalBuIndex = new SupervisionEnMedicalBuIndex();
                    XSSFRow row = sheet8.getRow(j);
                    if(enterpriseIdMap.get(ExcalUtils.handleStringXSSF(row.getCell(1)))!=null)
                    {
                        int id= enterpriseIdMap.get(ExcalUtils.handleStringXSSF(row.getCell(1)));
                        supervisionEnMedicalBuIndex.setEnterpriseId(id);
                    }
                    supervisionEnMedicalBu.setBusinessName(ExcalUtils.handleStringXSSF(row.getCell(0)));
                    supervisionEnMedicalBu.setRegisterNumber(ExcalUtils.handleStringXSSF(row.getCell(2)));
                    supervisionEnMedicalBu.setCategory(ExcalUtils.handleStringXSSF(row.getCell(3)));
                    supervisionEnMedicalBu.setMedicalSubject(ExcalUtils.handleStringXSSF(row.getCell(4)));
                    supervisionEnMedicalBu.setBusinessAddress(ExcalUtils.handleStringXSSF(row.getCell(5)));
                    supervisionEnMedicalBu.setStartTime(ExcalUtils.handleDateXSSF(row.getCell(6)));
                    supervisionEnMedicalBu.setEndTime(ExcalUtils.handleDateXSSF(row.getCell(7)));
                    supervisionEnMedicalBu.setGiveTime(ExcalUtils.handleDateXSSF(row.getCell(8)));
                    supervisionEnMedicalBu.setLssueAuthority(ExcalUtils.handleStringXSSF(row.getCell(9)));
                    supervisionEnMedicalBu.setCheckType(ExcalUtils.handleStringXSSF(row.getCell(10)));
                    supervisionEnMedicalBu.setDynamicGrade(ExcalUtils.handleStringXSSF(row.getCell(11)));
                    supervisionEnMedicalBu.setYearAssessment(ExcalUtils.handleStringXSSF(row.getCell(12)));
                    supervisionEnMedicalBu.setPatrolFrequency(ExcalUtils.handleStringXSSF(row.getCell(13)));
                    supervisionEnMedicalBu.setEnterpriseScale(ExcalUtils.handleStringXSSF(row.getCell(14)));
                    supervisionEnMedicalBu.setOperator("操作人");
                    supervisionEnMedicalBu.setOperateIp("123.123.123");
                    supervisionEnMedicalBu.setOperateTime(new Date());
                    if(!supervisionEnMedicalBu.getRegisterNumber().equals("")) {
                        if(numberMedicalMap.get(supervisionEnMedicalBu.getRegisterNumber())!=null)
                        {
                            //只进行许可证表的更新
                            int id = numberMedicalMap.get(supervisionEnMedicalBu.getRegisterNumber());
                            supervisionEnMedicalBu.setId(id);
                            id=numberMedicalIndexMap.get(supervisionEnMedicalBu.getRegisterNumber());
                            supervisionEnMedicalBu.setIndexId(id);
                            supervisionEnMedicalBuMapper.updateByPrimaryKey(supervisionEnMedicalBu);
                        }
                        else{
                            supervisionEnMedicalBuIndex.setNumber(supervisionEnMedicalBu.getRegisterNumber());
                            supervisionEnMedicalBuIndex.setEndTime(new Date());
                            int indexId=supervisionEnMedicalBuIndexMapper.insertSelective(supervisionEnMedicalBuIndex);
                            supervisionEnMedicalBu.setIndexId(indexId);
                            supervisionEnMedicalBuMapper.insertSelective(supervisionEnMedicalBu);
                        }
                    }
                }

                //医疗器械生产许可证
                List<SupervisionEnMedicalPro> supervisionEnMedicalProList = supervisionEnMedicalProMapper.getAll();
                Map <String,Integer> numberMedicalProMap =new HashMap<>();
                for (SupervisionEnMedicalPro supervisionEnMedicalPro:supervisionEnMedicalProList){
                    numberMedicalProMap.put(supervisionEnMedicalPro.getRegisterNumber(),supervisionEnMedicalPro.getId());
                }
                List<SupervisionEnMedicalProIndex> supervisionEnMedicalProIndexList = supervisionEnMedicalProIndexMapper.getAll();
                Map <String,Integer> numberMedicalProIndexMap =new HashMap<>();
                for (SupervisionEnMedicalProIndex supervisionEnMedicalProIndex:supervisionEnMedicalProIndexList){
                    numberMedicalProIndexMap.put(supervisionEnMedicalProIndex.getNumber(),supervisionEnMedicalProIndex.getId());
                }
                XSSFSheet sheet9 = workbook.getSheetAt(9);
                for (int j = 0; j <sheet9.getPhysicalNumberOfRows(); j++) {
                    if (j == 0) {
                        continue;//标题行
                    }
                    XSSFRow titleRow = sheet9.getRow(0);
                    XSSFRow row = sheet9.getRow(j);
                    if((row.getCell(1).getCellType()==CellType.BLANK)&&(row.getCell(2).getCellType()==CellType.BLANK))
                    {
                        break;
                    }
                    int a = j + 1;
                    if(row.getCell(1).getCellType()==CellType.BLANK)
                    {
                        errorList.add("医疗器械生产许可证 第" + a + "行"+titleRow.getCell(1).toString()+"为空");
                    }
                    if(row.getCell(2).getCellType()==CellType.BLANK)
                    {
                        errorList.add("医疗器械生产许可证 第" + a + "行"+titleRow.getCell(2).toString()+"为空");
                    }
                    if(row.getCell(10).getCellType()==CellType.BLANK)
                    {
                        errorList.add("医疗器械生产许可证 第" + a + "行"+titleRow.getCell(10).toString()+"为空");
                    }
                    if ((row.getCell(0) != null) && row.getCell(0).getCellType() != CellType.BLANK && row.getCell(0).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(0).toString(), "不是文本类型");
                        errorList.add("医疗器械生产许可证 第" + a + "行"+titleRow.getCell(0).toString()+"不是文本类型");
                    }
                    if ((row.getCell(1) != null) && row.getCell(1).getCellType() != CellType.BLANK && row.getCell(1).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(1).toString(), "不是文本类型");
                        errorList.add("医疗器械生产许可证 第" + a + "行"+titleRow.getCell(1).toString()+"不是文本类型");
                    }
                    if ((row.getCell(2) != null) && row.getCell(2).getCellType() != CellType.BLANK  && row.getCell(2).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(2).toString(), "不是文本类型");
                        errorList.add("医疗器械生产许可证 第" + a + "行"+titleRow.getCell(2).toString()+"不是文本类型");
                    }
                    if ((row.getCell(3) != null)&& row.getCell(3).getCellType() != CellType.BLANK  && row.getCell(3).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(3).toString(), "不是文本类型");
                        errorList.add("医疗器械生产许可证 第" + a + "行"+titleRow.getCell(3).toString()+"不是文本类型");
                    }
                    if ((row.getCell(4) != null) && (row.getCell(4).getCellType()!=CellType.BLANK)&& row.getCell(4).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(4).toString(), "不是文本类型");
                        errorList.add("医疗器械生产许可证 第" + a + "行"+titleRow.getCell(4).toString()+"不是文本类型");
                    }
                    if ((row.getCell(5) != null) &&(row.getCell(5).getCellType()!=CellType.BLANK)&& row.getCell(5).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(5).toString(), "不是文本类型");
                        errorList.add("医疗器械生产许可证 第" + a + "行"+titleRow.getCell(5).toString()+"不是文本类型");
                    }
                    if ((row.getCell(6) != null) && row.getCell(6).getCellType() != CellType.BLANK && row.getCell(6).getCellType() != CellType.NUMERIC) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(6).toString(), "不是文本类型");
                        errorList.add("医疗器械生产许可证 第" + a + "行"+titleRow.getCell(6).toString()+"不是文本类型");
                    }
                    if ((row.getCell(7) != null) && (row.getCell(7).getCellType()!=CellType.BLANK)&& row.getCell(7).getCellType() != CellType.NUMERIC) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(7).toString(), "不是文本类型");
                        errorList.add("医疗器械生产许可证 第" + a + "行"+titleRow.getCell(7).toString()+"不是文本类型");
                    }
                    if ((row.getCell(8) != null) && row.getCell(8).getCellType() != CellType.BLANK && row.getCell(8).getCellType() != CellType.NUMERIC) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(8).toString(), "不是文本类型");
                        errorList.add("医疗器械生产许可证 第" + a + "行"+titleRow.getCell(8).toString()+"不是日期类型");
                    }
                    if ((row.getCell(9) != null)&& row.getCell(9).getCellType() != CellType.BLANK  &&row.getCell(9).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(9).toString(), "不是文本类型");
                        errorList.add("医疗器械生产许可证 第" + a + "行"+titleRow.getCell(9).toString()+"不是日期类型");
                    }
                    if ((row.getCell(10) != null) && row.getCell(10).getCellType() != CellType.BLANK &&row.getCell(10).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(10).toString(), "不是文本类型");
                        errorList.add("医疗器械生产许可证 第" + a + "行"+titleRow.getCell(10).toString()+"不是日期类型");
                    }
                    if ((row.getCell(11) != null)&& row.getCell(11).getCellType() != CellType.BLANK  &&row.getCell(11).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(11).toString(), "不是日期类型");
                        errorList.add("医疗器械生产许可证 第" + a + "行"+titleRow.getCell(11).toString()+"不是文本类型");
                    }
                    if ((row.getCell(12) != null)&& row.getCell(12).getCellType() != CellType.BLANK  && row.getCell(12).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(12).toString(), "不是文本类型");
                        errorList.add("医疗器械生产许可证 第" + a + "行"+titleRow.getCell(12).toString()+"不是文本类型");
                    }
                    if ((row.getCell(13) != null) && row.getCell(13).getCellType() != CellType.BLANK && row.getCell(13).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(13).toString(),"不是文本类型");
                        errorList.add("医疗器械生产许可证 第" + a + "行"+titleRow.getCell(13).toString()+"不是文本类型");
                    }
                    if ((row.getCell(14) != null)&& row.getCell(14).getCellType() != CellType.BLANK && row.getCell(14).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(14).toString(),"不是数字类型");
                        errorList.add("医疗器械生产许可证 第" + a + "行"+titleRow.getCell(14).toString()+"不是文本类型");
                    }
                }
                if (!errorList.isEmpty()) {
                    System.out.println(errorList);
                    throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, errorList);
                }
                for (int j = 0; j < sheet9.getPhysicalNumberOfRows(); j++) {
                    if (j == 0) {
                        continue;//标题行
                    }
                    SupervisionEnMedicalPro supervisionEnMedicalPro = new SupervisionEnMedicalPro();
                    SupervisionEnMedicalProIndex supervisionEnMedicalProIndex = new SupervisionEnMedicalProIndex();
                    XSSFRow row = sheet9.getRow(j);
                    if(enterpriseIdMap.get(ExcalUtils.handleStringXSSF(row.getCell(1)))!=null)
                    {
                        int id= enterpriseIdMap.get(ExcalUtils.handleStringXSSF(row.getCell(1)));
                        supervisionEnMedicalProIndex.setEnterpriseId(id);
                    }
                    supervisionEnMedicalPro.setBusinessName(ExcalUtils.handleStringXSSF(row.getCell(0)));
                    supervisionEnMedicalPro.setRegisterNumber(ExcalUtils.handleStringXSSF(row.getCell(2)));
                    supervisionEnMedicalPro.setProduceScale(ExcalUtils.handleStringXSSF(row.getCell(3)));
                    supervisionEnMedicalPro.setEnterprisePerson(ExcalUtils.handleStringXSSF(row.getCell(4)));
                    supervisionEnMedicalPro.setBusinessAddress(ExcalUtils.handleStringXSSF(row.getCell(5)));
                    supervisionEnMedicalPro.setStartTime(ExcalUtils.handleDateXSSF(row.getCell(6)));
                    supervisionEnMedicalPro.setEndTime(ExcalUtils.handleDateXSSF(row.getCell(7)));
                    supervisionEnMedicalPro.setGiveTime(ExcalUtils.handleDateXSSF(row.getCell(8)));
                    supervisionEnMedicalPro.setLssueAuthority(ExcalUtils.handleStringXSSF(row.getCell(9)));
                    supervisionEnMedicalPro.setCheckType(ExcalUtils.handleStringXSSF(row.getCell(10)));
                    supervisionEnMedicalPro.setDynamicGrade(ExcalUtils.handleStringXSSF(row.getCell(11)));
                    supervisionEnMedicalPro.setYearAssessment(ExcalUtils.handleStringXSSF(row.getCell(12)));
                    supervisionEnMedicalPro.setPatrolFrequency(ExcalUtils.handleStringXSSF(row.getCell(13)));
                    supervisionEnMedicalPro.setEnterpriseScale(ExcalUtils.handleStringXSSF(row.getCell(14)));
                    supervisionEnMedicalPro.setOperator("操作人");
                    supervisionEnMedicalPro.setOperateIp("123.123.123");
                    supervisionEnMedicalPro.setOperateTime(new Date());
                    if(!supervisionEnMedicalPro.getRegisterNumber().equals("")) {
                        if(numberMedicalProMap.get(supervisionEnMedicalPro.getRegisterNumber())!=null)
                        {
                            //只进行许可证表的更新
                            int id = numberMedicalProMap.get(supervisionEnMedicalPro.getRegisterNumber());
                            supervisionEnMedicalPro.setId(id);
                            id = numberMedicalProIndexMap.get(supervisionEnMedicalPro.getRegisterNumber());
                            supervisionEnMedicalPro.setIndexId(id);
                            supervisionEnMedicalProMapper.updateByPrimaryKey(supervisionEnMedicalPro);
                        }
                        else{
                            supervisionEnMedicalProIndex.setNumber(supervisionEnMedicalPro.getRegisterNumber());
                            supervisionEnMedicalProIndex.setEndTime(new Date());
                            int indexId=supervisionEnMedicalProIndexMapper.insertSelective(supervisionEnMedicalProIndex);
                            supervisionEnMedicalPro.setIndexId(indexId);
                            supervisionEnMedicalProMapper.insertSelective(supervisionEnMedicalPro);
                        }
                    }
                }

                //小餐饮
                List<SupervisionEnSmallCater> supervisionEnSmallCaterList = supervisionEnSmallCaterMapper.getAll();
                Map <String,Integer> numberSmallCaterMap =new HashMap<>();
                for (SupervisionEnSmallCater supervisionEnSmallCater:supervisionEnSmallCaterList){
                    numberSmallCaterMap.put(supervisionEnSmallCater.getRegisterNumber(),supervisionEnSmallCater.getId());
                }
                List<SupervisionEnSmallCaterIndex> supervisionEnSmallCaterIndexList = supervisionEnSmallCaterIndexMapper.getAll();
                Map <String,Integer> numberSmallCaterIndexMap =new HashMap<>();
                for (SupervisionEnSmallCaterIndex supervisionEnSmallCaterIndex:supervisionEnSmallCaterIndexList){
                    numberSmallCaterIndexMap.put(supervisionEnSmallCaterIndex.getNumber(),supervisionEnSmallCaterIndex.getId());
                }
                XSSFSheet sheet4 = workbook.getSheetAt(4);
                for (int j = 0; j <sheet4.getPhysicalNumberOfRows(); j++) {
                    if (j == 0) {
                        continue;//标题行
                    }
                    XSSFRow titleRow = sheet4.getRow(0);
                    XSSFRow row = sheet4.getRow(j);
                    if((row.getCell(1).getCellType()==CellType.BLANK)&&(row.getCell(2).getCellType()==CellType.BLANK))
                    {
                        break;
                    }
                    int a = j + 1;
                    if(row.getCell(1).getCellType()==CellType.BLANK)
                    {
                        errorList.add("小餐饮 第" + a + "行"+titleRow.getCell(1).toString()+"为空");
                    }
                    if(row.getCell(2).getCellType()==CellType.BLANK)
                    {
                        errorList.add("小餐饮 第" + a + "行"+titleRow.getCell(2).toString()+"为空");
                    }
                    if(row.getCell(10).getCellType()==CellType.BLANK)
                    {
                        errorList.add("小餐饮 第" + a + "行"+titleRow.getCell(10).toString()+"为空");
                    }
                    if ((row.getCell(0) != null) && row.getCell(0).getCellType() != CellType.BLANK && row.getCell(0).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(0).toString(), "不是文本类型");
                        errorList.add("小餐饮 第" + a + "行"+titleRow.getCell(0).toString()+"不是文本类型");
                    }
                    if ((row.getCell(1) != null) && row.getCell(1).getCellType() != CellType.BLANK && row.getCell(1).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(1).toString(), "不是文本类型");
                        errorList.add("小餐饮 第" + a + "行"+titleRow.getCell(1).toString()+"不是文本类型");
                    }
                    if ((row.getCell(2) != null) && row.getCell(2).getCellType() != CellType.BLANK  && row.getCell(2).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(2).toString(), "不是文本类型");
                        errorList.add("小餐饮 第" + a + "行"+titleRow.getCell(2).toString()+"不是文本类型");
                    }
                    if ((row.getCell(3) != null)&& row.getCell(3).getCellType() != CellType.BLANK  && row.getCell(3).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(3).toString(), "不是文本类型");
                        errorList.add("小餐饮 第" + a + "行"+titleRow.getCell(3).toString()+"不是文本类型");
                    }
                    if ((row.getCell(4) != null) && (row.getCell(4).getCellType()!=CellType.BLANK)&& row.getCell(4).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(4).toString(), "不是文本类型");
                        errorList.add("小餐饮 第" + a + "行"+titleRow.getCell(4).toString()+"不是文本类型");
                    }
                    if ((row.getCell(5) != null) &&(row.getCell(5).getCellType()!=CellType.BLANK)&& row.getCell(5).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(5).toString(), "不是文本类型");
                        errorList.add("小餐饮 第" + a + "行"+titleRow.getCell(5).toString()+"不是文本类型");
                    }
                    if ((row.getCell(6) != null) && row.getCell(6).getCellType() != CellType.BLANK && row.getCell(6).getCellType() != CellType.NUMERIC) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(6).toString(), "不是文本类型");
                        errorList.add("小餐饮 第" + a + "行"+titleRow.getCell(6).toString()+"不是文本类型");
                    }
                    if ((row.getCell(7) != null) && (row.getCell(7).getCellType()!=CellType.BLANK)&& row.getCell(7).getCellType() != CellType.NUMERIC) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(7).toString(), "不是文本类型");
                        errorList.add("小餐饮 第" + a + "行"+titleRow.getCell(7).toString()+"不是文本类型");
                    }
                    if ((row.getCell(8) != null) && row.getCell(8).getCellType() != CellType.BLANK && row.getCell(8).getCellType() != CellType.NUMERIC) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(8).toString(), "不是文本类型");
                        errorList.add("小餐饮 第" + a + "行"+titleRow.getCell(8).toString()+"不是日期类型");
                    }
                    if ((row.getCell(9) != null)&& row.getCell(9).getCellType() != CellType.BLANK  &&row.getCell(9).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(9).toString(), "不是文本类型");
                        errorList.add("小餐饮 第" + a + "行"+titleRow.getCell(9).toString()+"不是日期类型");
                    }
                    if ((row.getCell(10) != null) && row.getCell(10).getCellType() != CellType.BLANK &&row.getCell(10).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(10).toString(), "不是文本类型");
                        errorList.add("小餐饮 第" + a + "行"+titleRow.getCell(10).toString()+"不是日期类型");
                    }
                    if ((row.getCell(11) != null)&& row.getCell(11).getCellType() != CellType.BLANK  &&row.getCell(11).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(11).toString(), "不是日期类型");
                        errorList.add("小餐饮 第" + a + "行"+titleRow.getCell(11).toString()+"不是文本类型");
                    }
                    if ((row.getCell(12) != null)&& row.getCell(12).getCellType() != CellType.BLANK  && row.getCell(12).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(12).toString(), "不是文本类型");
                        errorList.add("小餐饮 第" + a + "行"+titleRow.getCell(12).toString()+"不是文本类型");
                    }
                    if ((row.getCell(13) != null) && row.getCell(13).getCellType() != CellType.BLANK && row.getCell(13).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(13).toString(),"不是文本类型");
                        errorList.add("小餐饮 第" + a + "行"+titleRow.getCell(13).toString()+"不是文本类型");
                    }
                    if ((row.getCell(14) != null)&& row.getCell(14).getCellType() != CellType.BLANK && row.getCell(14).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(14).toString(),"不是数字类型");
                        errorList.add("小餐饮 第" + a + "行"+titleRow.getCell(14).toString()+"不是文本类型");
                    }
                }
                if (!errorList.isEmpty()) {
                    System.out.println(errorList);
                    throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, errorList);
                }
                for (int j = 0; j < sheet4.getPhysicalNumberOfRows(); j++) {
                    if (j == 0) {
                        continue;//标题行
                    }
                    SupervisionEnSmallCater supervisionEnSmallCater = new SupervisionEnSmallCater();
                    SupervisionEnSmallCaterIndex supervisionEnSmallCaterIndex = new SupervisionEnSmallCaterIndex();
                    XSSFRow row = sheet9.getRow(j);
                    if(enterpriseIdMap.get(ExcalUtils.handleStringXSSF(row.getCell(1)))!=null)
                    {
                        int id= enterpriseIdMap.get(ExcalUtils.handleStringXSSF(row.getCell(1)));
                        supervisionEnSmallCaterIndex.setEnterpriseId(id);
                    }
                    supervisionEnSmallCater.setBusinessName(ExcalUtils.handleStringXSSF(row.getCell(0)));
                    supervisionEnSmallCater.setRegisterNumber(ExcalUtils.handleStringXSSF(row.getCell(2)));
                    supervisionEnSmallCater.setMainCategory(ExcalUtils.handleStringXSSF(row.getCell(3)));
                    supervisionEnSmallCater.setMainSubject(ExcalUtils.handleStringXSSF(row.getCell(4)));
                    supervisionEnSmallCater.setBusinessAddress(ExcalUtils.handleStringXSSF(row.getCell(5)));
                    supervisionEnSmallCater.setStartTime(ExcalUtils.handleDateXSSF(row.getCell(6)));
                    supervisionEnSmallCater.setEndTime(ExcalUtils.handleDateXSSF(row.getCell(7)));
                    supervisionEnSmallCater.setGiveTime(ExcalUtils.handleDateXSSF(row.getCell(8)));
                    supervisionEnSmallCater.setLssueAuthority(ExcalUtils.handleStringXSSF(row.getCell(9)));
                    supervisionEnSmallCater.setCheckType(ExcalUtils.handleStringXSSF(row.getCell(10)));
                    supervisionEnSmallCater.setDynamicGrade(ExcalUtils.handleStringXSSF(row.getCell(11)));
                    supervisionEnSmallCater.setYearAssessment(ExcalUtils.handleStringXSSF(row.getCell(12)));
                    supervisionEnSmallCater.setPatrolFrequency(ExcalUtils.handleStringXSSF(row.getCell(13)));
                    supervisionEnSmallCater.setEnterpriseScale(ExcalUtils.handleStringXSSF(row.getCell(14)));
                    supervisionEnSmallCater.setOperator("操作人");
                    supervisionEnSmallCater.setOperateTime(new Date());
                    supervisionEnSmallCater.setOperateIp("123.123.123");
                    if(!supervisionEnSmallCater.getRegisterNumber().equals("")) {
                        if(numberSmallCaterMap.get(supervisionEnSmallCater.getRegisterNumber())!=null)
                        {
                            //只进行许可证表的更新
                            int id = numberSmallCaterMap.get(supervisionEnSmallCater.getRegisterNumber());
                            supervisionEnSmallCater.setId(id);
                            id= numberSmallCaterIndexMap.get(supervisionEnSmallCater.getRegisterNumber());
                            supervisionEnSmallCater.setIndexId(id);
                            supervisionEnSmallCaterMapper.updateByPrimaryKey(supervisionEnSmallCater);
                        }
                        else{
                            supervisionEnSmallCaterIndex.setNumber(supervisionEnSmallCater.getRegisterNumber());
                            supervisionEnSmallCaterIndex.setEndTime(new Date());
                            int indexId=supervisionEnSmallCaterIndexMapper.insertSelective(supervisionEnSmallCaterIndex);
                            supervisionEnSmallCater.setIndexId(indexId);
                            supervisionEnSmallCaterMapper.insertSelective(supervisionEnSmallCater);
                        }
                    }
                }

                //小作坊
                List<SupervisionEnSmallWorkshop> supervisionEnSmallWorkshopList = supervisionEnSmallWorkshopMapper.getAll();
                Map <String,Integer> numberSmallWorkshopMap =new HashMap<>();
                for (SupervisionEnSmallWorkshop supervisionEnSmallWorkshop:supervisionEnSmallWorkshopList){
                    numberSmallWorkshopMap.put(supervisionEnSmallWorkshop.getRegisterNumber(),supervisionEnSmallWorkshop.getId());
                }
                List<SupervisionEnSmallWorkshopIndex> supervisionEnSmallWorkshopIndexList = supervisionEnSmallWorkshopIndexMapper.getAll();
                Map <String,Integer> numberSmallWorkshopIndexMap =new HashMap<>();
                for (SupervisionEnSmallWorkshopIndex supervisionEnSmallWorkshopIndex:supervisionEnSmallWorkshopIndexList){
                    numberSmallWorkshopIndexMap.put(supervisionEnSmallWorkshopIndex.getNumber(),supervisionEnSmallWorkshopIndex.getId());
                }
                XSSFSheet sheet5 = workbook.getSheetAt(5);
                for (int j = 0; j <sheet5.getPhysicalNumberOfRows(); j++) {
                    if (j == 0) {
                        continue;//标题行
                    }
                    XSSFRow titleRow = sheet5.getRow(0);
                    XSSFRow row = sheet5.getRow(j);
                    if((row.getCell(1).getCellType()==CellType.BLANK)&&(row.getCell(2).getCellType()==CellType.BLANK))
                    {
                        break;
                    }
                    int a = j + 1;
                    if(row.getCell(1).getCellType()==CellType.BLANK)
                    {
                        errorList.add("小作坊 第" + a + "行"+titleRow.getCell(1).toString()+"为空");
                    }
                    if(row.getCell(2).getCellType()==CellType.BLANK)
                    {
                        errorList.add("小作坊 第" + a + "行"+titleRow.getCell(2).toString()+"为空");
                    }
                    if(row.getCell(10).getCellType()==CellType.BLANK)
                    {
                        errorList.add("小作坊 第" + a + "行"+titleRow.getCell(10).toString()+"为空");
                    }
                    if ((row.getCell(0) != null) && row.getCell(0).getCellType() != CellType.BLANK && row.getCell(0).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(0).toString(), "不是文本类型");
                        errorList.add("小作坊 第" + a + "行"+titleRow.getCell(0).toString()+"不是文本类型");
                    }
                    if ((row.getCell(1) != null) && row.getCell(1).getCellType() != CellType.BLANK && row.getCell(1).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(1).toString(), "不是文本类型");
                        errorList.add("小作坊 第" + a + "行"+titleRow.getCell(1).toString()+"不是文本类型");
                    }
                    if ((row.getCell(2) != null) && row.getCell(2).getCellType() != CellType.BLANK  && row.getCell(2).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(2).toString(), "不是文本类型");
                        errorList.add("小作坊 第" + a + "行"+titleRow.getCell(2).toString()+"不是文本类型");
                    }
                    if ((row.getCell(3) != null)&& row.getCell(3).getCellType() != CellType.BLANK  && row.getCell(3).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(3).toString(), "不是文本类型");
                        errorList.add("小作坊 第" + a + "行"+titleRow.getCell(3).toString()+"不是文本类型");
                    }
                    if ((row.getCell(4) != null) && (row.getCell(4).getCellType()!=CellType.BLANK)&& row.getCell(4).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(4).toString(), "不是文本类型");
                        errorList.add("小作坊 第" + a + "行"+titleRow.getCell(4).toString()+"不是文本类型");
                    }
                    if ((row.getCell(5) != null) &&(row.getCell(5).getCellType()!=CellType.BLANK)&& row.getCell(5).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(5).toString(), "不是文本类型");
                        errorList.add("小作坊 第" + a + "行"+titleRow.getCell(5).toString()+"不是文本类型");
                    }
                    if ((row.getCell(6) != null) && row.getCell(6).getCellType() != CellType.BLANK && row.getCell(6).getCellType() != CellType.NUMERIC) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(6).toString(), "不是文本类型");
                        errorList.add("小作坊 第" + a + "行"+titleRow.getCell(6).toString()+"不是文本类型");
                    }
                    if ((row.getCell(7) != null) && (row.getCell(7).getCellType()!=CellType.BLANK)&& row.getCell(7).getCellType() != CellType.NUMERIC) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(7).toString(), "不是文本类型");
                        errorList.add("小作坊 第" + a + "行"+titleRow.getCell(7).toString()+"不是文本类型");
                    }
                    if ((row.getCell(8) != null) && row.getCell(8).getCellType() != CellType.BLANK && row.getCell(8).getCellType() != CellType.NUMERIC) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(8).toString(), "不是文本类型");
                        errorList.add("小作坊 第" + a + "行"+titleRow.getCell(8).toString()+"不是日期类型");
                    }
                    if ((row.getCell(9) != null)&& row.getCell(9).getCellType() != CellType.BLANK  &&row.getCell(9).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(9).toString(), "不是文本类型");
                        errorList.add("小作坊 第" + a + "行"+titleRow.getCell(9).toString()+"不是日期类型");
                    }
                    if ((row.getCell(10) != null) && row.getCell(10).getCellType() != CellType.BLANK &&row.getCell(10).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(10).toString(), "不是文本类型");
                        errorList.add("小作坊 第" + a + "行"+titleRow.getCell(10).toString()+"不是日期类型");
                    }
                    if ((row.getCell(11) != null)&& row.getCell(11).getCellType() != CellType.BLANK  &&row.getCell(11).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(11).toString(), "不是日期类型");
                        errorList.add("小作坊 第" + a + "行"+titleRow.getCell(11).toString()+"不是文本类型");
                    }
                    if ((row.getCell(12) != null)&& row.getCell(12).getCellType() != CellType.BLANK  && row.getCell(12).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(12).toString(), "不是文本类型");
                        errorList.add("小作坊 第" + a + "行"+titleRow.getCell(12).toString()+"不是文本类型");
                    }
                    if ((row.getCell(13) != null) && row.getCell(13).getCellType() != CellType.BLANK && row.getCell(13).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(13).toString(),"不是文本类型");
                        errorList.add("小作坊 第" + a + "行"+titleRow.getCell(13).toString()+"不是文本类型");
                    }
                    if ((row.getCell(14) != null)&& row.getCell(14).getCellType() != CellType.BLANK && row.getCell(14).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(14).toString(),"不是数字类型");
                        errorList.add("小作坊 第" + a + "行"+titleRow.getCell(14).toString()+"不是文本类型");
                    }
                }
                if (!errorList.isEmpty()) {
                    System.out.println(errorList);
                    throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, errorList);
                }
                for (int j = 0; j < sheet5.getPhysicalNumberOfRows(); j++) {
                    if (j == 0) {
                        continue;//标题行
                    }
                    SupervisionEnSmallWorkshop supervisionEnSmallWorkshop = new SupervisionEnSmallWorkshop();
                    SupervisionEnSmallWorkshopIndex supervisionEnSmallWorkshopIndex= new SupervisionEnSmallWorkshopIndex();
                    XSSFRow row = sheet9.getRow(j);
                    if(enterpriseIdMap.get(ExcalUtils.handleStringXSSF(row.getCell(1)))!=null)
                    {
                        int id= enterpriseIdMap.get(ExcalUtils.handleStringXSSF(row.getCell(1)));
                        supervisionEnSmallWorkshopIndex.setEnterpriseId(id);
                    }
                    supervisionEnSmallWorkshop.setBusinessName(ExcalUtils.handleStringXSSF(row.getCell(0)));
                    supervisionEnSmallWorkshop.setRegisterNumber(ExcalUtils.handleStringXSSF(row.getCell(2)));
//                    supervisionEnSmallWorkshop.(ExcalUtils.handleStringXSSF(row.getCell(3)));
                    supervisionEnSmallWorkshop.setFoodType(ExcalUtils.handleStringXSSF(row.getCell(4)));
                    supervisionEnSmallWorkshop.setBusinessAddress(ExcalUtils.handleStringXSSF(row.getCell(5)));
                    supervisionEnSmallWorkshop.setStartTime(ExcalUtils.handleDateXSSF(row.getCell(6)));
                    supervisionEnSmallWorkshop.setEndTime(ExcalUtils.handleDateXSSF(row.getCell(7)));
                    supervisionEnSmallWorkshop.setGiveTime(ExcalUtils.handleDateXSSF(row.getCell(8)));
                    supervisionEnSmallWorkshop.setLssueAuthority(ExcalUtils.handleStringXSSF(row.getCell(9)));
                    supervisionEnSmallWorkshop.setCheckType(ExcalUtils.handleStringXSSF(row.getCell(10)));
                    supervisionEnSmallWorkshop.setDynamicGrade(ExcalUtils.handleStringXSSF(row.getCell(11)));
                    supervisionEnSmallWorkshop.setYearAssessment(ExcalUtils.handleStringXSSF(row.getCell(12)));
                    supervisionEnSmallWorkshop.setPatrolFrequency(ExcalUtils.handleStringXSSF(row.getCell(13)));
                    supervisionEnSmallWorkshop.setEnterpriseScale(ExcalUtils.handleStringXSSF(row.getCell(14)));
                    supervisionEnSmallWorkshop.setOperator("操作人");
                    supervisionEnSmallWorkshop.setOperateIp("123.123.123");
                    supervisionEnSmallWorkshop.setOperateTime(new Date());
                    if(!supervisionEnSmallWorkshop.getRegisterNumber().equals("")) {
                        if(numberSmallWorkshopMap.get(supervisionEnSmallWorkshop.getRegisterNumber())!=null)
                        {
                            //只进行许可证表的更新
                            int id = numberSmallWorkshopMap.get(supervisionEnSmallWorkshop.getRegisterNumber());
                            supervisionEnSmallWorkshop.setId(id);
                            id=numberSmallWorkshopIndexMap.get(supervisionEnSmallWorkshop.getRegisterNumber());
                            supervisionEnSmallWorkshop.setIndexId(id);
                            supervisionEnSmallWorkshopMapper.updateByPrimaryKey(supervisionEnSmallWorkshop);
                        }
                        else{
                            supervisionEnSmallWorkshopIndex.setNumber(supervisionEnSmallWorkshop.getRegisterNumber());
                            supervisionEnSmallWorkshopIndex.setEndTime(new Date());
                            int indexId=supervisionEnSmallWorkshopIndexMapper.insertSelective(supervisionEnSmallWorkshopIndex);
                            supervisionEnSmallWorkshop.setIndexId(indexId);
                            supervisionEnSmallWorkshopMapper.insertSelective(supervisionEnSmallWorkshop);
                        }
                    }
                }

                //工业产品许可证
                List<SupervisionEnIndustrialProducts> supervisionEnIndustrialProductsList = supervisionEnIndustrialProductsMapper.getAll();
                Map <String,Integer> numberIndustrialMap =new HashMap<>();
                for (SupervisionEnIndustrialProducts supervisionEnIndustrialProducts:supervisionEnIndustrialProductsList){
                    numberIndustrialMap.put(supervisionEnIndustrialProducts.getRegisterNumber(),supervisionEnIndustrialProducts.getId());
                }
                List<SupervisionEnIndustrialProductsIndex> supervisionEnIndustrialProductsIndexList = supervisionEnIndustrialProductsIndexMapper.getAll();
                Map <String,Integer> numberIndustrialIndexMap =new HashMap<>();
                for (SupervisionEnIndustrialProductsIndex supervisionEnIndustrialProductsIndex:supervisionEnIndustrialProductsIndexList){
                    numberIndustrialIndexMap.put(supervisionEnIndustrialProductsIndex.getNumber(),supervisionEnIndustrialProductsIndex.getId());
                }
                XSSFSheet sheet11 = workbook.getSheetAt(11);
                for (int j = 0; j <sheet11.getPhysicalNumberOfRows(); j++) {
                    if (j == 0) {
                        continue;//标题行
                    }
                    XSSFRow titleRow = sheet11.getRow(0);
                    XSSFRow row = sheet11.getRow(j);
                    if((row.getCell(1).getCellType()==CellType.BLANK)&&(row.getCell(2).getCellType()==CellType.BLANK))
                    {
                        break;
                    }
                    int a = j + 1;
                    if(row.getCell(1).getCellType()==CellType.BLANK)
                    {
                        errorList.add("工业产品许可证 第" + a + "行"+titleRow.getCell(1).toString()+"为空");
                    }
                    if(row.getCell(2).getCellType()==CellType.BLANK)
                    {
                        errorList.add("工业产品许可证 第" + a + "行"+titleRow.getCell(2).toString()+"为空");
                    }
                    if(row.getCell(9).getCellType()==CellType.BLANK)
                    {
                        errorList.add("工业产品许可证 第" + a + "行"+titleRow.getCell(9).toString()+"为空");
                    }
                    if ((row.getCell(0) != null) && row.getCell(0).getCellType() != CellType.BLANK && row.getCell(0).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(0).toString(), "不是文本类型");
                        errorList.add("工业产品许可证 第" + a + "行"+titleRow.getCell(0).toString()+"不是文本类型");
                    }
                    if ((row.getCell(1) != null) && row.getCell(1).getCellType() != CellType.BLANK && row.getCell(1).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(1).toString(), "不是文本类型");
                        errorList.add("工业产品许可证 第" + a + "行"+titleRow.getCell(1).toString()+"不是文本类型");
                    }
                    if ((row.getCell(2) != null) && row.getCell(2).getCellType() != CellType.BLANK  && row.getCell(2).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(2).toString(), "不是文本类型");
                        errorList.add("工业产品许可证 第" + a + "行"+titleRow.getCell(2).toString()+"不是文本类型");
                    }
                    if ((row.getCell(3) != null)&& row.getCell(3).getCellType() != CellType.BLANK  && row.getCell(3).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(3).toString(), "不是文本类型");
                        errorList.add("工业产品许可证 第" + a + "行"+titleRow.getCell(3).toString()+"不是文本类型");
                    }
                    if ((row.getCell(4) != null) && (row.getCell(4).getCellType()!=CellType.BLANK)&& row.getCell(4).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(4).toString(), "不是文本类型");
                        errorList.add("工业产品许可证 第" + a + "行"+titleRow.getCell(4).toString()+"不是文本类型");
                    }
                    if ((row.getCell(5) != null) &&(row.getCell(5).getCellType()!=CellType.BLANK)&& row.getCell(5).getCellType() != CellType.NUMERIC) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(5).toString(), "不是文本类型");
                        errorList.add("工业产品许可证 第" + a + "行"+titleRow.getCell(5).toString()+"不是文本类型");
                    }
                    if ((row.getCell(6) != null) && row.getCell(6).getCellType() != CellType.BLANK && row.getCell(6).getCellType() != CellType.NUMERIC) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(6).toString(), "不是文本类型");
                        errorList.add("工业产品许可证 第" + a + "行"+titleRow.getCell(6).toString()+"不是文本类型");
                    }
                    if ((row.getCell(7) != null) && (row.getCell(7).getCellType()!=CellType.BLANK)&& row.getCell(7).getCellType() != CellType.NUMERIC) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(7).toString(), "不是文本类型");
                        errorList.add("工业产品许可证 第" + a + "行"+titleRow.getCell(7).toString()+"不是文本类型");
                    }
                    if ((row.getCell(8) != null) && row.getCell(8).getCellType() != CellType.BLANK && row.getCell(8).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(8).toString(), "不是文本类型");
                        errorList.add("工业产品许可证 第" + a + "行"+titleRow.getCell(8).toString()+"不是日期类型");
                    }
                    if ((row.getCell(9) != null)&& row.getCell(9).getCellType() != CellType.BLANK  &&row.getCell(9).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(9).toString(), "不是文本类型");
                        errorList.add("工业产品许可证 第" + a + "行"+titleRow.getCell(9).toString()+"不是日期类型");
                    }
                    if ((row.getCell(10) != null) && row.getCell(10).getCellType() != CellType.BLANK &&row.getCell(10).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(10).toString(), "不是文本类型");
                        errorList.add("工业产品许可证 第" + a + "行"+titleRow.getCell(10).toString()+"不是日期类型");
                    }
                    if ((row.getCell(11) != null)&& row.getCell(11).getCellType() != CellType.BLANK  &&row.getCell(11).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(11).toString(), "不是日期类型");
                        errorList.add("工业产品许可证 第" + a + "行"+titleRow.getCell(11).toString()+"不是文本类型");
                    }
                    if ((row.getCell(12) != null)&& row.getCell(12).getCellType() != CellType.BLANK  && row.getCell(12).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(12).toString(), "不是文本类型");
                        errorList.add("工业产品许可证 第" + a + "行"+titleRow.getCell(12).toString()+"不是文本类型");
                    }
                    if ((row.getCell(13) != null) && row.getCell(13).getCellType() != CellType.BLANK && row.getCell(13).getCellType() != CellType.STRING) {
//                        errorMap.put("第" + a + "行"+titleRow.getCell(13).toString(),"不是文本类型");
                        errorList.add("工业产品许可证 第" + a + "行"+titleRow.getCell(13).toString()+"不是文本类型");
                    }
                }
                if (!errorList.isEmpty()) {
                    System.out.println(errorList);
                    throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, errorList);
                }
                for (int j = 0; j < sheet11.getPhysicalNumberOfRows(); j++) {
                    if (j == 0) {
                        continue;//标题行
                    }
                    SupervisionEnIndustrialProducts supervisionEnIndustrialProducts = new SupervisionEnIndustrialProducts();
                    SupervisionEnIndustrialProductsIndex supervisionEnIndustrialProductsIndex= new SupervisionEnIndustrialProductsIndex();
                    XSSFRow row = sheet11.getRow(j);
                    if(enterpriseIdMap.get(ExcalUtils.handleStringXSSF(row.getCell(1)))!=null)
                    {
                        int id= enterpriseIdMap.get(ExcalUtils.handleStringXSSF(row.getCell(1)));
                        supervisionEnIndustrialProducts.setIndexId(id);
                        supervisionEnIndustrialProductsIndex.setEnterpriseId(id);
                    }
                    supervisionEnIndustrialProducts.setBusinessName(ExcalUtils.handleStringXSSF(row.getCell(0)));
                    supervisionEnIndustrialProducts.setRegisterNumber(ExcalUtils.handleStringXSSF(row.getCell(2)));
                    supervisionEnIndustrialProducts.setProductsName(ExcalUtils.handleStringXSSF(row.getCell(3)));
                    supervisionEnIndustrialProducts.setBusinessAddress(ExcalUtils.handleStringXSSF(row.getCell(4)));
                    supervisionEnIndustrialProducts.setStartTime(ExcalUtils.handleDateXSSF(row.getCell(5)));
                    supervisionEnIndustrialProducts.setEndTime(ExcalUtils.handleDateXSSF(row.getCell(6)));
                    supervisionEnIndustrialProducts.setGiveTime(ExcalUtils.handleDateXSSF(row.getCell(7)));
                    supervisionEnIndustrialProducts.setLssueAuthority(ExcalUtils.handleStringXSSF(row.getCell(8)));
                    supervisionEnIndustrialProducts.setCheckType(ExcalUtils.handleStringXSSF(row.getCell(9)));
                    supervisionEnIndustrialProducts.setDynamicGrade(ExcalUtils.handleStringXSSF(row.getCell(10)));
                    supervisionEnIndustrialProducts.setYearAssessment(ExcalUtils.handleStringXSSF(row.getCell(11)));
                    supervisionEnIndustrialProducts.setPatrolFrequency(ExcalUtils.handleStringXSSF(row.getCell(12)));
                    supervisionEnIndustrialProducts.setEnterpriseScale(ExcalUtils.handleStringXSSF(row.getCell(13)));
                    supervisionEnIndustrialProducts.setOperator("操作人");
                    supervisionEnIndustrialProducts.setOperateIp("123.123.123");
                    supervisionEnIndustrialProducts.setOperateTime(new Date());
                    if(!supervisionEnIndustrialProducts.getRegisterNumber().equals("")) {
                        if(numberIndustrialMap.get(supervisionEnIndustrialProducts.getRegisterNumber())!=null)
                        {
                            //只进行许可证表的更新
                            int id = numberIndustrialMap.get(supervisionEnIndustrialProducts.getRegisterNumber());
                            supervisionEnIndustrialProducts.setId(id);
                            id = numberIndustrialIndexMap.get(supervisionEnIndustrialProducts.getRegisterNumber());
                            supervisionEnIndustrialProducts.setIndexId(id);
                            supervisionEnIndustrialProductsMapper.updateByPrimaryKey(supervisionEnIndustrialProducts);
                        }
                        else{
                            supervisionEnIndustrialProductsIndex.setNumber(supervisionEnIndustrialProducts.getRegisterNumber());
                            supervisionEnIndustrialProductsIndex.setEndTime(new Date());
                            int indexId=supervisionEnIndustrialProductsIndexMapper.insertSelective(supervisionEnIndustrialProductsIndex);
                            supervisionEnIndustrialProducts.setIndexId(indexId);
                            supervisionEnIndustrialProductsMapper.insertSelective(supervisionEnIndustrialProducts);
                        }
                    }
                }
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return changedNumbers;
//                if(updateEnterpriseIds.size()>0){//修改这里，这里是删除覆盖，所以id会变动，要修改为修改更新
//                    supervisionEnterpriseMapper.batchDelete(updateEnterpriseIds);
//                    supervisionEnCosmeticsMapper.deleteByEnterpriseIds(updateEnterpriseIds);
//                    supervisionEnMedicalMapper.deleteByEnterpriseIds(updateEnterpriseIds);
//                    supervisionEnDrugsBuMapper.deleteByEnterpriseIds(updateEnterpriseIds);
//                    supervisionEnFoodCirMapper.deleteByEnterpriseIds(updateEnterpriseIds);
//                    supervisionEnFoodBuMapper.deleteByEnterpriseIds(updateEnterpriseIds);
//                    supervisionEnCommonMapper.deleteByEnterpriseIds(updateEnterpriseIds);
//                    supervisionEnFoodProMapper.deleteByEnterpriseIds(updateEnterpriseIds);
//                    supervisionEnProCategoryMapper.deleteByEnterpriseIds(updateEnterpriseIds);
//                }
            //开始插入企业的list
//                if(supervisionEnterpriseList.size()>0){
//                    supervisionEnterpriseMapper.batchInsert(supervisionEnterpriseList);
//                }
//                //替换企业的信用代码和id，用于用户的插入
//                Map<String,Integer> enterpriseMap = new HashMap<>();
//                for(SupervisionEnterprise supervisionEnterprise : supervisionEnterpriseList){
//                    enterpriseMap.put(supervisionEnterprise.getIdNumber(),supervisionEnterprise.getId());
//                }
//                //插入新企业的用户
//                //如果在前边的企业先做了插入操作，那就必须要同时做用户的插入操作，这一切都是基于先前的操作前的判错机制才能够顺利进行下去
//                if(sysUserList.size()>0){
//                    for(SysUser sysUser:sysUserList){
//                        sysUser.setInfoId(enterpriseMap.get(sysUser.getLoginName()));
//                    }
//                    sysUserMapper.batchInsert(sysUserList);
//                }
//                //插入企业用户角色进用户-角色表
//                List<SysRoleUser> sysRoleUserList = new ArrayList<>();
//                for(SysUser sysUser:sysUserList)
//                {
//                    SysRoleUser sysRoleUser =new SysRoleUser();
//                    sysRoleUser.setUserId(sysUser.getId());
//                    sysRoleUser.setRoleId(27);
//                    sysRoleUser.setOperateTime(new Date());
//                    sysRoleUser.setOperator("操作人");
//                    sysRoleUser.setOperateIp("123.124.124");
//                    sysRoleUserList.add(sysRoleUser);
//                }
//                if(sysRoleUserList.size()>0){
//                    sysRoleUserMapper.batchInsert(sysRoleUserList);
//                }
//                //许可证子表插入，如果是分表插入的话可以分开写方法，
//                //食品经营
//                XSSFSheet sheet1 = workbook.getSheetAt(1);
//                for (int j = 0; j < sheet1.getPhysicalNumberOfRows(); j++) {
//                    if (j == 0) {
//                        continue;//标题行
//                    }
//                    SupervisionEnFoodBu supervisionEnFoodBu = new SupervisionEnFoodBu();
//                    XSSFRow row = sheet1.getRow(j);
//                    supervisionEnFoodBu.setEnterpriseId(enterpriseMap.get(ExcalUtils.handleStringXSSF(row.getCell(0))));
//                    supervisionEnFoodBu.setNumber(ExcalUtils.handleStringXSSF(row.getCell(1)));
//                    supervisionEnFoodBu.setBusinessFormat(ExcalUtils.handleStringXSSF(row.getCell(2)));
//                    supervisionEnFoodBu.setBusinessNotes(ExcalUtils.handleStringXSSF(row.getCell(3)));
//                    supervisionEnFoodBu.setIsInternet(ExcalUtils.handleStringXSSF(row.getCell(4)));
//                    supervisionEnFoodBu.setWebsite(ExcalUtils.handleStringXSSF(row.getCell(5)));
//                    supervisionEnFoodBu.setIsReal(ExcalUtils.handleStringXSSF(row.getCell(6)));
//                    supervisionEnFoodBu.setBusinessProject(ExcalUtils.handleStringXSSF(row.getCell(7)));
//                    supervisionEnFoodBu.setGiveTime(ExcalUtils.handleDateXSSF(row.getCell(8)));
//                    supervisionEnFoodBu.setStartTime(ExcalUtils.handleDateXSSF(row.getCell(9)));
//                    supervisionEnFoodBu.setEndTime(ExcalUtils.handleDateXSSF(row.getCell(10)));
//                    supervisionEnFoodBu.setValidityAge(ExcalUtils.handleFloatXSSF(row.getCell(11)));
//                    supervisionEnFoodBu.setCategory(ExcalUtils.handleStringXSSF(row.getCell(12)));
//                    supervisionEnFoodBu.setLicenseAuthority(ExcalUtils.handleStringXSSF(row.getCell(13)));
//                    supervisionEnFoodBu.setRemark(ExcalUtils.handleStringXSSF(row.getCell(14)));
//                    supervisionEnFoodBu.setOperator("操作人");
//                    supervisionEnFoodBu.setOperateIp("123.123.123");
//                    supervisionEnFoodBu.setOperateTime(new Date());
//                    if(supervisionEnFoodBu.getEnterpriseId()!=null) {
//                        supervisionEnFoodBuList.add(supervisionEnFoodBu);
//                    }
//                }
//                if(supervisionEnFoodBuList.size()>0){
//                    supervisionEnFoodBuMapper.batchInsert(supervisionEnFoodBuList);
//                }
//                //餐饮服务
//                XSSFSheet sheet2 = workbook.getSheetAt(2);
//                for (int j = 0; j < sheet2.getPhysicalNumberOfRows(); j++) {
//                    if (j == 0) {
//                        continue;//标题行
//                    }
//                    SupervisionEnCommon supervisionEnCommon = new SupervisionEnCommon();
//                    XSSFRow row = sheet2.getRow(j);
//                    supervisionEnCommon.setEnterpriseId(enterpriseMap.get(ExcalUtils.handleStringXSSF(row.getCell(0))));
//                    supervisionEnCommon.setNumber(ExcalUtils.handleStringXSSF(row.getCell(1)));
//                    supervisionEnCommon.setGiveTime(ExcalUtils.handleDateXSSF(row.getCell(2)));
//                    supervisionEnCommon.setStartTime(ExcalUtils.handleDateXSSF(row.getCell(3)));
//                    supervisionEnCommon.setEndTime(ExcalUtils.handleDateXSSF(row.getCell(4)));
//                    supervisionEnCommon.setValidityAge(ExcalUtils.handleFloatXSSF(row.getCell(5)));
//                    supervisionEnCommon.setBusinessType(ExcalUtils.handleStringXSSF(row.getCell(6)));
//                    supervisionEnCommon.setLicenseAuthority(ExcalUtils.handleStringXSSF(row.getCell(7)));
//                    supervisionEnCommon.setRemark(ExcalUtils.handleStringXSSF(row.getCell(8)));
//                    supervisionEnCommon.setOperator("操作人");
//                    supervisionEnCommon.setOperateIp("123.123.123");
//                    supervisionEnCommon.setOperateTime(new Date());
//                    if(supervisionEnCommon.getEnterpriseId()!=null) {
//                        supervisionEnCommonList.add(supervisionEnCommon);
//                    }
//                }
//                if(supervisionEnCommonList.size()>0){
//                    supervisionEnCommonMapper.batchInsert(supervisionEnCommonList);
//                }
//                //食品流通
//                XSSFSheet sheet3 = workbook.getSheetAt(3);
//                for (int j = 0; j < sheet3.getPhysicalNumberOfRows(); j++) {
//                    if (j == 0) {
//                        continue;//标题行
//                    }
//                    SupervisionEnFoodCir supervisionEnFoodCir = new SupervisionEnFoodCir();
//                    XSSFRow row = sheet3.getRow(j);
//                    supervisionEnFoodCir.setEnterpriseId(enterpriseMap.get(ExcalUtils.handleStringXSSF(row.getCell(0))));
//                    supervisionEnFoodCir.setNumber(ExcalUtils.handleStringXSSF(row.getCell(1)));
//                    supervisionEnFoodCir.setRegisterAdress(ExcalUtils.handleStringXSSF(row.getCell(2)));
//                    supervisionEnFoodCir.setBodyType(ExcalUtils.handleStringXSSF(row.getCell(3))=="个体"?1:0);
//                    supervisionEnFoodCir.setProduceAddress(ExcalUtils.handleStringXSSF(row.getCell(4)));
//                    supervisionEnFoodCir.setBusinessProject(ExcalUtils.handleStringXSSF(row.getCell(5)));
//                    supervisionEnFoodCir.setBusinessType(ExcalUtils.handleStringXSSF(row.getCell(6)));
//                    supervisionEnFoodCir.setGiveTime(ExcalUtils.handleDateXSSF(row.getCell(7)));
//                    supervisionEnFoodCir.setStartTime(ExcalUtils.handleDateXSSF(row.getCell(8)));
//                    supervisionEnFoodCir.setEndTime(ExcalUtils.handleDateXSSF(row.getCell(9)));
//                    supervisionEnFoodCir.setValidityAge(ExcalUtils.handleFloatXSSF(row.getCell(10)));
//                    supervisionEnFoodCir.setCopiesNumber(ExcalUtils.handleIntegerXSSF(row.getCell(11)));
//                    supervisionEnFoodCir.setLicenseAuthority(ExcalUtils.handleStringXSSF(row.getCell(12)));
//                    supervisionEnFoodCir.setLssuer(ExcalUtils.handleStringXSSF(row.getCell(13)));
//                    supervisionEnFoodCir.setInspector(ExcalUtils.handleStringXSSF(row.getCell(14)));
//                    supervisionEnFoodCir.setBusinessMode(ExcalUtils.handleStringXSSF(row.getCell(15)));
//                    supervisionEnFoodCir.setRemark(ExcalUtils.handleStringXSSF(row.getCell(16)));
//                    supervisionEnFoodCir.setOperator("操作人");
//                    supervisionEnFoodCir.setOperateIp("123.123.123");
//                    supervisionEnFoodCir.setOperateTime(new Date());
//                    if(supervisionEnFoodCir.getEnterpriseId()!=null) {
//                        supervisionEnFoodCirList.add(supervisionEnFoodCir);
//                    }
//                }
//                if(supervisionEnFoodCirList.size()>0){
//                    supervisionEnFoodCirMapper.batchInsert(supervisionEnFoodCirList);
//                }
//                //食品生产
//                XSSFSheet sheet4 = workbook.getSheetAt(4);
//                for (int j = 0; j < sheet4.getPhysicalNumberOfRows(); j++) {
//                    if (j == 0) {
//                        continue;//标题行
//                    }
//                    SupervisionEnFoodPro supervisionEnFoodPro = new SupervisionEnFoodPro();
//                    XSSFRow row = sheet4.getRow(j);
//                    supervisionEnFoodPro.setEnterpriseId(enterpriseMap.get(ExcalUtils.handleStringXSSF(row.getCell(0))));
//                    supervisionEnFoodPro.setNumber(ExcalUtils.handleStringXSSF(row.getCell(1)));
//                    supervisionEnFoodPro.setGiveTime(ExcalUtils.handleDateXSSF(row.getCell(2)));
//                    supervisionEnFoodPro.setStartTime(ExcalUtils.handleDateXSSF(row.getCell(3)));
//                    supervisionEnFoodPro.setEndTime(ExcalUtils.handleDateXSSF(row.getCell(4)));
//                    supervisionEnFoodPro.setValidityAge(ExcalUtils.handleFloatXSSF(row.getCell(5)));
//                    supervisionEnFoodPro.setLicenseAuthority(ExcalUtils.handleStringXSSF(row.getCell(6)));
//                    supervisionEnFoodPro.setOthers(ExcalUtils.handleStringXSSF(row.getCell(7)));
//                    supervisionEnFoodPro.setOperator("操作人");
//                    supervisionEnFoodPro.setOperatorIp("123.123.123");
//                    supervisionEnFoodPro.setOperatorTime(new Date());
//                    if(supervisionEnFoodPro.getEnterpriseId()!=null){
//                        supervisionEnFoodProList.add(supervisionEnFoodPro);
//                    }
//                }
//                if(supervisionEnFoodProList.size()>0){
//                    supervisionEnFoodProMapper.batchInsert(supervisionEnFoodProList);
//                }
//                XSSFSheet sheet5 = workbook.getSheetAt(5);
//                for (int j = 0; j < sheet5.getPhysicalNumberOfRows(); j++) {
//                    if (j == 0) {
//                        continue;//标题行
//                    }
//                    SupervisionEnProCategory supervisionEnProCategory = new SupervisionEnProCategory();
//                    XSSFRow row = sheet5.getRow(j);
//                    supervisionEnProCategory.setParentId(enterpriseMap.get(ExcalUtils.handleStringXSSF(row.getCell(0))));
//                    supervisionEnProCategory.setCategory(ExcalUtils.handleStringXSSF(row.getCell(1)));
//                    supervisionEnProCategory.setCode(ExcalUtils.handleStringXSSF(row.getCell(2)));
//                    supervisionEnProCategory.setName(ExcalUtils.handleStringXSSF(row.getCell(3)));
//                    supervisionEnProCategory.setDetail(ExcalUtils.handleStringXSSF(row.getCell(4)));
//                    supervisionEnProCategory.setOperator("操作人");
//                    supervisionEnProCategory.setOperateIp("123.123.123");
//                    supervisionEnProCategory.setOperateTime(new Date());
//                    if(supervisionEnProCategory.getParentId()!=null) {
//                        supervisionEnProCategoryList.add(supervisionEnProCategory);
//                    }
//                }
//                if(supervisionEnProCategoryList.size()>0){
//                    supervisionEnProCategoryMapper.batchInsert(supervisionEnProCategoryList);
//                }
//
//               //药品经营
//                XSSFSheet sheet6 = workbook.getSheetAt(6);
//                for (int j = 0; j < sheet6.getPhysicalNumberOfRows(); j++) {
//                    if (j == 0) {
//                        continue;//标题行
//                    }
//                    SupervisionEnDrugsBu supervisionEnDrugsBu = new SupervisionEnDrugsBu();
//                    XSSFRow row = sheet6.getRow(j);
//                    supervisionEnDrugsBu.setEnterpriseId(enterpriseMap.get(ExcalUtils.handleStringXSSF(row.getCell(0))));
//                    supervisionEnDrugsBu.setNumber(ExcalUtils.handleStringXSSF(row.getCell(1)));
//                    supervisionEnDrugsBu.setQualityPerson(ExcalUtils.handleStringXSSF(row.getCell(2)));
//                    supervisionEnDrugsBu.setOperationMode(ExcalUtils.handleStringXSSF(row.getCell(3)));
//                    supervisionEnDrugsBu.setWarehouseAddress(ExcalUtils.handleStringXSSF(row.getCell(4)));
//                    supervisionEnDrugsBu.setGiveTime(ExcalUtils.handleDateXSSF(row.getCell(5)));
//                    supervisionEnDrugsBu.setStartTime(ExcalUtils.handleDateXSSF(row.getCell(6)));
//                    supervisionEnDrugsBu.setEndTime(ExcalUtils.handleDateXSSF(row.getCell(7)));
//                    supervisionEnDrugsBu.setGspNumber(ExcalUtils.handleStringXSSF(row.getCell(8)));
//                    supervisionEnDrugsBu.setGspStartTime(ExcalUtils.handleDateXSSF(row.getCell(9)));
//                    supervisionEnDrugsBu.setGspFinishTime(ExcalUtils.handleDateXSSF(row.getCell(10)));
//                    supervisionEnDrugsBu.setValidityTime(ExcalUtils.handleFloatXSSF(row.getCell(11)));
//                    supervisionEnDrugsBu.setLicenseAuthority(ExcalUtils.handleStringXSSF(row.getCell(12)));
//                    supervisionEnDrugsBu.setBusinessScope(ExcalUtils.handleStringXSSF(row.getCell(13)));
//                    supervisionEnDrugsBu.setRemark(ExcalUtils.handleStringXSSF(row.getCell(14)));
//                    supervisionEnDrugsBu.setOperator("操作人");
//                    supervisionEnDrugsBu.setOperatorIp("123.123.123");
//                    supervisionEnDrugsBu.setOperatorTime(new Date());
//                    if(supervisionEnDrugsBu.getEnterpriseId()!=null) {
//                        supervisionEnDrugsBuList.add(supervisionEnDrugsBu);
//                    }
//                }
//                if(supervisionEnDrugsBuList.size()>0){
//                    supervisionEnDrugsBuMapper.batchInsert(supervisionEnDrugsBuList);
//                }

//                XSSFSheet sheet7 = workbook.getSheetAt(7);
//                for (int j = 0; j < sheet7.getPhysicalNumberOfRows(); j++) {
//                    if (j == 0) {
//                        continue;//标题行
//                    }
//                    SupervisionEnMedical supervisionEnMedical = new SupervisionEnMedical();
//                    XSSFRow row = sheet7.getRow(j);
//                    supervisionEnMedical.setEnterpriseId(enterpriseMap.get(ExcalUtils.handleStringXSSF(row.getCell(0))));
//                    supervisionEnMedical.setRegisterNumber(ExcalUtils.handleStringXSSF(row.getCell(1)));
//                    supervisionEnMedical.setSuperviseCategory(ExcalUtils.handleStringXSSF(row.getCell(2)));
//                    supervisionEnMedical.setLssueAuthority(ExcalUtils.handleStringXSSF(row.getCell(3)));
//                    supervisionEnMedical.setGiveTime(ExcalUtils.handleDateXSSF(row.getCell(4)));
//                    supervisionEnMedical.setStartTime(ExcalUtils.handleDateXSSF(row.getCell(5)));
//                    supervisionEnMedical.setEndTime(ExcalUtils.handleDateXSSF(row.getCell(6)));
//                    supervisionEnMedical.setMedicalSubject(ExcalUtils.handleStringXSSF(row.getCell(7)));
//                    supervisionEnMedical.setRemark(ExcalUtils.handleStringXSSF(row.getCell(8)));
//                    supervisionEnMedical.setOperator("操作人");
//                    supervisionEnMedical.setOperateIp("123.123.123");
//                    supervisionEnMedical.setOperateTime(new Date());
//                    if(supervisionEnMedical.getEnterpriseId()!=null) {
//                        supervisionEnMedicalList.add(supervisionEnMedical);
//                    }
//                }
//                if(supervisionEnMedicalList.size()>0){
//                    supervisionEnMedicalMapper.batchInsert(supervisionEnMedicalList);
//                }
//                XSSFSheet sheet8 = workbook.getSheetAt(8);
//                for (int j = 0; j < sheet8.getPhysicalNumberOfRows(); j++) {
//                    if (j == 0) {
//                        continue;//标题行
//                    }
//                    SupervisionEnCosmetics supervisionEnCosmetics = new SupervisionEnCosmetics();
//                    XSSFRow row = sheet8.getRow(j);
//                    supervisionEnCosmetics.setEnterpriseId(enterpriseMap.get(ExcalUtils.handleStringXSSF(row.getCell(0))));
//                    supervisionEnCosmetics.setRegisterCode(ExcalUtils.handleStringXSSF(row.getCell(1)));
//                    supervisionEnCosmetics.setValidityAge(ExcalUtils.handleFloatXSSF(row.getCell(2)));
//                    supervisionEnCosmetics.setLicenseAuthority(ExcalUtils.handleStringXSSF(row.getCell(3)));
//                    supervisionEnCosmetics.setWarehouse(ExcalUtils.handleStringXSSF(row.getCell(4)));
//                    supervisionEnCosmetics.setGiveTime(ExcalUtils.handleDateXSSF(row.getCell(5)));
//                    supervisionEnCosmetics.setStartTime(ExcalUtils.handleDateXSSF(row.getCell(6)));
//                    supervisionEnCosmetics.setEndTime(ExcalUtils.handleDateXSSF(row.getCell(7)));
//                    supervisionEnCosmetics.setLicenseProject(ExcalUtils.handleStringXSSF(row.getCell(8)));
//                    supervisionEnCosmetics.setRemark(ExcalUtils.handleStringXSSF(row.getCell(9)));
//                    supervisionEnCosmetics.setOperator("操作人");
//                    supervisionEnCosmetics.setOperateIp("123.123.123");
//                    supervisionEnCosmetics.setOperateTime(new Date());
//                    if(supervisionEnCosmetics.getEnterpriseId()!=null) {
//                        supervisionEnCosmeticsList.add(supervisionEnCosmetics);
//                    }
//                }
//                if (supervisionEnCosmeticsList.size()>0){
//                    supervisionEnCosmeticsMapper.batchInsert(supervisionEnCosmeticsList);
//                }
//                workbook.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }else{
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "文件错误");
        }
    }



    //这两个方法是在map中查找是否有这个文件中的地区和部门，有才转化，没有即抛错，这一步如果放在导入赋值之前做判错，后边就无需判断了。
    Integer importCheckArea(String areaName,Map<String,Integer> areaMap,String idNumber){
        if(areaMap.get(areaName)!=null||StringUtils.isEmpty(idNumber)){
            return areaMap.get(areaName);
        }else{
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,areaName+"区域不存在");
        }
    }

    Integer importCheckDept(String deptName,Map<String,Integer> deptMap,String idNumber){
        if(deptMap.get(deptName)!=null||StringUtils.isEmpty(idNumber)){
            return deptMap.get(deptName);
        }else{
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,deptName+"部门不存在");
        }
    }

}
