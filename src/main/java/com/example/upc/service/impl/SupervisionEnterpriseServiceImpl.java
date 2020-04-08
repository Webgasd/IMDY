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
import com.example.upc.util.ExcalUtils;
import com.example.upc.util.MD5Util;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
    private SupervisionEnCommonMapper supervisionEnCommonMapper;
    @Autowired
    private SupervisionEnMedicalMapper supervisionEnMedicalMapper;
    @Autowired
    private SupervisionEnFoodProMapper supervisionEnFoodProMapper;
    @Autowired
    private SupervisionEnDrugsBuMapper supervisionEnDrugsBuMapper;
    @Autowired
    private SupervisionEnCosmeticsMapper supervisionEnCosmeticsMapper;
    @Autowired
    private SupervisionEnFoodCirMapper supervisionEnFoodCirMapper;
    @Autowired
    private SupervisionEnFoodBuMapper supervisionEnFoodBuMapper;
    @Autowired
    private SupervisionEnProCategoryMapper supervisionEnProCategoryMapper;
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


    @Override
    public EnterpriseParam getById(int id) {
        SupervisionEnterprise supervisionEnterprise= supervisionEnterpriseMapper.selectByPrimaryKey(id);
        EnterpriseParam enterpriseParam = new EnterpriseParam();
        BeanUtils.copyProperties(supervisionEnterprise,enterpriseParam);
        List<String> permissionType = new ArrayList<>();
        SupervisionEnFoodBu supervisionEnFoodBu = supervisionEnFoodBuMapper.selectByEnterpriseId(id);
        if(supervisionEnFoodBu!=null){
            permissionType.add("foodBusiness");
            enterpriseParam.setFoodBusiness(supervisionEnFoodBu);
        }
        SupervisionEnCommon supervisionEnCommon = supervisionEnCommonMapper.selectByEnterpriseId(id);
        if(supervisionEnCommon!=null){
            permissionType.add("foodCommon");
            enterpriseParam.setFoodCommon(supervisionEnCommon);
        }
        SupervisionEnFoodCir supervisionEnFoodCir = supervisionEnFoodCirMapper.selectByEnterpriseId(id);
        if(supervisionEnFoodCir!=null){
            permissionType.add("foodCirculate");
            enterpriseParam.setFoodCirculate(supervisionEnFoodCir);
        }
        SupervisionEnFoodPro supervisionEnFoodPro = supervisionEnFoodProMapper.selectByEnterpriseId(id);
        if (supervisionEnFoodPro != null) {
            permissionType.add("foodProduce");
            EnFoodProduceParam enFoodProduceParam = new EnFoodProduceParam();
            BeanUtils.copyProperties(supervisionEnFoodPro,enFoodProduceParam);
            List<SupervisionEnProCategory> list = supervisionEnProCategoryMapper.selectByParentId(id);
            enFoodProduceParam.setList(list);
            enterpriseParam.setFoodProduce(enFoodProduceParam);
        }

        SupervisionEnDrugsBu supervisionEnDrugsBu = supervisionEnDrugsBuMapper.selectByEnterpriseId(id);
        if(supervisionEnDrugsBu!=null){
            permissionType.add("drugsBusiness");
            enterpriseParam.setDrugsBusiness(supervisionEnDrugsBu);
        }
        SupervisionEnMedical supervisionEnMedical = supervisionEnMedicalMapper.selectByEnterpriseId(id);
        if(supervisionEnMedical!=null){
            permissionType.add("medicalUse");
            enterpriseParam.setMedicalUse(supervisionEnMedical);
        }
        SupervisionEnCosmetics supervisionEnCosmetics = supervisionEnCosmeticsMapper.selectByEnterpriseId(id);
        if(supervisionEnCosmetics!=null){
            permissionType.add("cosmeticsUse");
            enterpriseParam.setCosmeticsUse(supervisionEnCosmetics);
        }
        enterpriseParam.setPermissionType(String.join(",",permissionType));
        return enterpriseParam;
    }

    @Override
    public SupervisionEnterprise selectById(int id) {
        return supervisionEnterpriseMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional
    public void insert(String json) {
        SupervisionEnterprise supervisionEnterprise = JSONObject.parseObject(json,SupervisionEnterprise.class);
        ValidationResult result = validator.validate(supervisionEnterprise);
        if(result.isHasErrors()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
        }
        if(supervisionEnterpriseMapper.countByIdNumber(supervisionEnterprise.getIdNumber(),supervisionEnterprise.getId())>0){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"社会信用代码被占用");
        }
        supervisionEnterpriseMapper.insertSelective(supervisionEnterprise);
        if(supervisionEnterprise.getId()==null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"插入失败");
        }
        supervisionEnterprise.setOperateIp("124.124.124");
        supervisionEnterprise.setOperateTime(new Date());
        supervisionEnterprise.setOperator("zcc");
        SysUser sysUser = new SysUser();
        String encryptedPassword = MD5Util.md5("123456+");
        sysUser.setUsername(supervisionEnterprise.getEnterpriseName());
        sysUser.setLoginName(supervisionEnterprise.getIdNumber());
        sysUser.setPassword(encryptedPassword);
        sysUser.setUserType(1);
        sysUser.setInfoName(supervisionEnterprise.getEnterpriseName());
        sysUser.setInfoId(supervisionEnterprise.getId());
        sysUser.setStatus(0);
        sysUser.setOperator("操作人");
        sysUser.setOperateIp("124.124.124");
        sysUser.setOperateTime(new Date());
        sysUserMapper.insertSelective(sysUser);
        insertEnterpriseChildrenList(supervisionEnterprise,json);
    }

    @Override
    @Transactional
    public void update(String json) {
        SupervisionEnterprise supervisionEnterprise = JSONObject.parseObject(json,SupervisionEnterprise.class);
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
        insertEnterpriseChildrenList(supervisionEnterprise,json);
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
        supervisionEnCosmeticsMapper.deleteByEnterpriseId(id);
        supervisionEnMedicalMapper.deleteByEnterpriseId(id);
        supervisionEnDrugsBuMapper.deleteByEnterpriseId(id);
        supervisionEnFoodCirMapper.deleteByEnterpriseId(id);
        supervisionEnFoodBuMapper.deleteByEnterpriseId(id);
        supervisionEnCommonMapper.deleteByEnterpriseId(id);
        supervisionEnFoodProMapper.deleteByEnterpriseId(id);
        supervisionEnProCategoryMapper.deleteByParentId(id);

    }

    //插入子表，这里要修改，子表建议子子表
    void insertEnterpriseChildrenList(SupervisionEnterprise supervisionEnterprise,String json){
        EnterpriseParam enterpriseParam = JSON.parseObject(json,EnterpriseParam.class);
        if(supervisionEnterprise.getPermissionType()==null){
            supervisionEnterprise.setPermissionType("");
        }
        supervisionEnCosmeticsMapper.deleteByEnterpriseId(supervisionEnterprise.getId());
        supervisionEnMedicalMapper.deleteByEnterpriseId(supervisionEnterprise.getId());
        supervisionEnDrugsBuMapper.deleteByEnterpriseId(supervisionEnterprise.getId());
        supervisionEnFoodCirMapper.deleteByEnterpriseId(supervisionEnterprise.getId());
        supervisionEnFoodBuMapper.deleteByEnterpriseId(supervisionEnterprise.getId());
        supervisionEnCommonMapper.deleteByEnterpriseId(supervisionEnterprise.getId());
        supervisionEnFoodProMapper.deleteByEnterpriseId(supervisionEnterprise.getId());
        supervisionEnProCategoryMapper.deleteByParentId(supervisionEnterprise.getId());
        if(supervisionEnterprise.getPermissionType().contains("foodBusiness")){
            SupervisionEnFoodBu supervisionEnFoodBu = enterpriseParam.getFoodBusiness();
            if(supervisionEnFoodBu==null){
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"请输入许可内容");
            }
            ValidationResult result = validator.validate(supervisionEnFoodBu);
            if(result.isHasErrors()){
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
            }
            supervisionEnFoodBu.setEnterpriseId(supervisionEnterprise.getId());
            supervisionEnFoodBu.setOperateIp("124.124.124");
            supervisionEnFoodBu.setOperateTime(new Date());
            supervisionEnFoodBu.setOperator("zcc");
            supervisionEnFoodBuMapper.insertSelective(supervisionEnFoodBu);
        }
        if(supervisionEnterprise.getPermissionType().contains("foodCommon")){
            SupervisionEnCommon supervisionEnCommon = enterpriseParam.getFoodCommon();
            if(supervisionEnCommon==null){
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"请输入许可内容");
            }
            ValidationResult result = validator.validate(supervisionEnCommon);
            if(result.isHasErrors()){
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
            }
            supervisionEnCommon.setEnterpriseId(supervisionEnterprise.getId());
            supervisionEnCommon.setOperateIp("124.124.124");
            supervisionEnCommon.setOperateTime(new Date());
            supervisionEnCommon.setOperator("zcc");
            supervisionEnCommonMapper.insertSelective(supervisionEnCommon);
        }
        if(supervisionEnterprise.getPermissionType().contains("foodCirculate")){
            SupervisionEnFoodCir supervisionEnFoodCir = enterpriseParam.getFoodCirculate();
            if(supervisionEnFoodCir==null){
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"请输入许可内容");
            }
            ValidationResult result = validator.validate(supervisionEnFoodCir);
            if(result.isHasErrors()){
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
            }
            supervisionEnFoodCir.setEnterpriseId(supervisionEnterprise.getId());
            supervisionEnFoodCir.setOperateIp("124.124.124");
            supervisionEnFoodCir.setOperateTime(new Date());
            supervisionEnFoodCir.setOperator("zcc");
            supervisionEnFoodCirMapper.insertSelective(supervisionEnFoodCir);
        }
        if(supervisionEnterprise.getPermissionType().contains("foodProduce")){
            EnFoodProduceParam foodProduceParam = enterpriseParam.getFoodProduce();
            if(foodProduceParam==null){
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"请输入许可内容");
            }
            ValidationResult result = validator.validate(foodProduceParam);
            if(result.isHasErrors()){
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
            }
            SupervisionEnFoodPro supervisionEnFoodPro = new SupervisionEnFoodPro();
            BeanUtils.copyProperties(foodProduceParam,supervisionEnFoodPro);
            supervisionEnFoodPro.setEnterpriseId(supervisionEnterprise.getId());
            supervisionEnFoodPro.setOperatorIp("124.124.124");
            supervisionEnFoodPro.setOperatorTime(new Date());
            supervisionEnFoodPro.setOperator("zcc");
            supervisionEnFoodProMapper.insertSelective(supervisionEnFoodPro);
            List<SupervisionEnProCategory> supervisionEnProCategoryList = foodProduceParam.getList();
            if(supervisionEnProCategoryList.size()>0){
                supervisionEnProCategoryMapper.batchInsert(supervisionEnProCategoryList.stream().map((list)->{
                    list.setOperateIp("124.124.124");
                    list.setOperateTime(new Date());
                    list.setOperator("zcc");
                    list.setParentId(supervisionEnterprise.getId());
                    return list;}).collect(Collectors.toList()));
            }
        }
        if(supervisionEnterprise.getPermissionType().contains("drugsBusiness")){
            SupervisionEnDrugsBu supervisionEnDrugsBu = enterpriseParam.getDrugsBusiness();
            if(supervisionEnDrugsBu==null){
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"请输入许可内容");
            }
            ValidationResult result = validator.validate(supervisionEnDrugsBu);
            if(result.isHasErrors()){
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
            }
            supervisionEnDrugsBu.setEnterpriseId(supervisionEnterprise.getId());
            supervisionEnDrugsBu.setOperatorIp("124.124.124");
            supervisionEnDrugsBu.setOperatorTime(new Date());
            supervisionEnDrugsBu.setOperator("zcc");
            supervisionEnDrugsBuMapper.insertSelective(supervisionEnDrugsBu);
        }
        if(supervisionEnterprise.getPermissionType().contains("medicalUse")){
            SupervisionEnMedical supervisionEnMedical = enterpriseParam.getMedicalUse();
            if(supervisionEnMedical==null){
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"请输入许可内容");
            }
            ValidationResult result = validator.validate(supervisionEnMedical);
            if(result.isHasErrors()){
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
            }
            supervisionEnMedical.setEnterpriseId(supervisionEnterprise.getId());
            supervisionEnMedical.setOperateIp("124.124.124");
            supervisionEnMedical.setOperateTime(new Date());
            supervisionEnMedical.setOperator("zcc");
            supervisionEnMedicalMapper.insertSelective(supervisionEnMedical);
        }
        if(supervisionEnterprise.getPermissionType().contains("cosmeticsUse")){
            SupervisionEnCosmetics supervisionEnCosmetics = enterpriseParam.getCosmeticsUse();
            if(supervisionEnCosmetics==null){
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"请输入许可内容");
            }
            ValidationResult result = validator.validate(supervisionEnCosmetics);
            if(result.isHasErrors()){
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
            }
            supervisionEnCosmetics.setEnterpriseId(supervisionEnterprise.getId());
            supervisionEnCosmetics.setOperateIp("124.124.124");
            supervisionEnCosmetics.setOperateTime(new Date());
            supervisionEnCosmetics.setOperator("zcc");
            supervisionEnCosmeticsMapper.insertSelective(supervisionEnCosmetics);
        }
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
    public void importExcel(MultipartFile file, Integer type) {
        List<SysDept> sysDeptList = sysDeptMapper.getAllDept();
        Map<String,Integer> deptMap = new HashMap<>();
        for (SysDept sysDept : sysDeptList){deptMap.put(sysDept.getName(),sysDept.getId());}
        List<SysArea> sysAreaList = sysAreaMapper.getAllArea();
        Map<String,Integer> areaMap = new HashMap<>();
        for(SysArea sysArea : sysAreaList){areaMap.put(sysArea.getName(),sysArea.getId());}
        List<EnterpriseListResult> allEnterpriseList = supervisionEnterpriseMapper.getAll();
        Map<String,Integer> enterpriseIdMap = new HashMap<>();
        for (EnterpriseListResult enterpriseListResult : allEnterpriseList){enterpriseIdMap.put(enterpriseListResult.getIdNumber(),enterpriseListResult.getId());}
        List<SupervisionEnterprise> supervisionEnterpriseList = new ArrayList<>();
        List<SupervisionEnFoodPro> supervisionEnFoodProList = new ArrayList<>();
        List<SupervisionEnProCategory> supervisionEnProCategoryList = new ArrayList<>();
        List<SupervisionEnMedical> supervisionEnMedicalList = new ArrayList<>();
        List<SupervisionEnFoodCir> supervisionEnFoodCirList = new ArrayList<>();
        List<SupervisionEnFoodBu> supervisionEnFoodBuList = new ArrayList<>();
        List<SupervisionEnDrugsBu> supervisionEnDrugsBuList = new ArrayList<>();
        List<SupervisionEnCommon>  supervisionEnCommonList = new ArrayList<>();
        List<SupervisionEnCosmetics> supervisionEnCosmeticsList = new ArrayList<>();
        if(type == 7){
            try {
                XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
                    XSSFSheet sheet0 = workbook.getSheetAt(0);
                    for (int j = 0; j < sheet0.getPhysicalNumberOfRows(); j++) {
                        if (j == 0) {
                            continue;//标题行
                        }
                        SupervisionEnterprise supervisionEnterprise = new SupervisionEnterprise();
                        XSSFRow row = sheet0.getRow(j);
                        supervisionEnterprise.setEnterpriseName(ExcalUtils.handleStringXSSF(row.getCell(0)));
                        supervisionEnterprise.setShopName(ExcalUtils.handleStringXSSF(row.getCell(1)));
                        supervisionEnterprise.setPostalCode(ExcalUtils.handleStringXSSF(row.getCell(2)));
                        supervisionEnterprise.setRegisteredAddress(ExcalUtils.handleStringXSSF(row.getCell(3)));
                        supervisionEnterprise.setBusinessAddress(ExcalUtils.handleStringXSSF(row.getCell(4)));
                        supervisionEnterprise.setLegalPerson(ExcalUtils.handleStringXSSF(row.getCell(5)));
                        supervisionEnterprise.setIdNumber(ExcalUtils.handleStringXSSF(row.getCell(6)));
                        supervisionEnterprise.setLicenseNumber(ExcalUtils.handleStringXSSF(row.getCell(7)));
                        supervisionEnterprise.setCantacts(ExcalUtils.handleStringXSSF(row.getCell(8)));
                        supervisionEnterprise.setCantactWay(ExcalUtils.handleStringXSSF(row.getCell(9)));
                        supervisionEnterprise.setRegulators(importCheckDept(ExcalUtils.handleStringXSSF(row.getCell(10)),deptMap,ExcalUtils.handleStringXSSF(row.getCell(6))));
                        supervisionEnterprise.setArea(importCheckArea(ExcalUtils.handleStringXSSF(row.getCell(11)),areaMap,ExcalUtils.handleStringXSSF(row.getCell(6))));
                        supervisionEnterprise.setGrid(areaMap.get(ExcalUtils.handleStringXSSF(row.getCell(12)))==null?0:areaMap.get(ExcalUtils.handleStringXSSF(row.getCell(12))));
                        supervisionEnterprise.setSupervisor(ExcalUtils.handleStringXSSF(row.getCell(13)));
                        supervisionEnterprise.setEnterpriseScale(ExcalUtils.handleStringXSSF(row.getCell(14)));
                        supervisionEnterprise.setGridPerson(ExcalUtils.handleStringXSSF(row.getCell(15)));
                        supervisionEnterprise.setDynamicGrade(ExcalUtils.handleStringXSSF(row.getCell(16)));
                        supervisionEnterprise.setYearAssessment(ExcalUtils.handleStringXSSF(row.getCell(17)));
                        supervisionEnterprise.setEmail(ExcalUtils.handleStringXSSF(row.getCell(18)));
                        supervisionEnterprise.setOfficePhone(ExcalUtils.handleStringXSSF(row.getCell(19)));
                        supervisionEnterprise.setPatrolFrequency(ExcalUtils.handleStringXSSF(row.getCell(20)));
                        supervisionEnterprise.setTransformationType(ExcalUtils.handleStringXSSF(row.getCell(21)));
                        supervisionEnterprise.setPermissionState(ExcalUtils.handleStringXSSF(row.getCell(22)));
                        supervisionEnterprise.setPermissionType(ExcalUtils.handleStringXSSF(row.getCell(23)));
                        supervisionEnterprise.setIsStop(ExcalUtils.handleIntegerXSSF(row.getCell(24)));
                        supervisionEnterprise.setIpIdNumber(ExcalUtils.handleStringXSSF(row.getCell(25)));
                        supervisionEnterprise.setIpMobilePhone(ExcalUtils.handleStringXSSF(row.getCell(26)));
                        supervisionEnterprise.setIpSexy(ExcalUtils.handleStringXSSF(row.getCell(27)));
                        supervisionEnterprise.setIpEducation(ExcalUtils.handleStringXSSF(row.getCell(28)));
                        supervisionEnterprise.setIpPoliticalOutlook(ExcalUtils.handleStringXSSF(row.getCell(29)));
                        supervisionEnterprise.setIpCurrentAddress(ExcalUtils.handleStringXSSF(row.getCell(30)));
                        supervisionEnterprise.setIpNation(ExcalUtils.handleStringXSSF(row.getCell(31)));
                        supervisionEnterprise.setIpEmail(ExcalUtils.handleStringXSSF(row.getCell(32)));
                        supervisionEnterprise.setIpPostalCode(ExcalUtils.handleStringXSSF(row.getCell(33)));
                        supervisionEnterprise.setSpName(ExcalUtils.handleStringXSSF(row.getCell(34)));
                        supervisionEnterprise.setSpidNumber(ExcalUtils.handleStringXSSF(row.getCell(35)));
                        supervisionEnterprise.setSpOfficePhone(ExcalUtils.handleStringXSSF(row.getCell(36)));
                        supervisionEnterprise.setSpMobilePhone(ExcalUtils.handleStringXSSF(row.getCell(37)));
                        supervisionEnterprise.setSpEmail(ExcalUtils.handleStringXSSF(row.getCell(38)));
                        supervisionEnterprise.setSpSexy(ExcalUtils.handleStringXSSF(row.getCell(39)));
                        supervisionEnterprise.setSpEducation(ExcalUtils.handleStringXSSF(row.getCell(40)));
                        supervisionEnterprise.setSpCurrentAddress(ExcalUtils.handleStringXSSF(row.getCell(41)));
                        supervisionEnterprise.setSpTraining(ExcalUtils.handleStringXSSF(row.getCell(42)));
                        supervisionEnterprise.setOperationMode(ExcalUtils.handleStringXSSF(row.getCell(43)));
                        supervisionEnterprise.setHousingProperty(ExcalUtils.handleStringXSSF(row.getCell(44)));
                        supervisionEnterprise.setOwner(ExcalUtils.handleStringXSSF(row.getCell(45)));
                        supervisionEnterprise.setOwnerIdNumber(ExcalUtils.handleStringXSSF(row.getCell(46)));
                        supervisionEnterprise.setOwnerMobilePhone(ExcalUtils.handleStringXSSF(row.getCell(47)));
                        supervisionEnterprise.setAgent(ExcalUtils.handleStringXSSF(row.getCell(48)));
                        supervisionEnterprise.setAgentIdNumber(ExcalUtils.handleStringXSSF(row.getCell(49)));
                        supervisionEnterprise.setAgentMobilePhone(ExcalUtils.handleStringXSSF(row.getCell(50)));
                        supervisionEnterprise.setOtherPhone(ExcalUtils.handleStringXSSF(row.getCell(51)));
                        supervisionEnterprise.setIntegrityLevel(ExcalUtils.handleStringXSSF(row.getCell(52)));
                        supervisionEnterprise.setProductionArea(ExcalUtils.handleIntegerXSSF(row.getCell(53)));
                        supervisionEnterprise.setFixedAssets(ExcalUtils.handleIntegerXSSF(row.getCell(54)));
                        supervisionEnterprise.setPractitioners(ExcalUtils.handleStringXSSF(row.getCell(55)));
                        supervisionEnterprise.setExaminationPopulation(ExcalUtils.handleIntegerXSSF(row.getCell(56)));
                        supervisionEnterprise.setWarehouse1(ExcalUtils.handleStringXSSF(row.getCell(57)));
                        supervisionEnterprise.setWarehouse2(ExcalUtils.handleStringXSSF(row.getCell(58)));
                        supervisionEnterprise.setWarehouse3(ExcalUtils.handleStringXSSF(row.getCell(59)));
                        supervisionEnterprise.setAbbreviation(ExcalUtils.handleStringXSSF(row.getCell(60)));
                        supervisionEnterprise.setIntroduction(ExcalUtils.handleStringXSSF(row.getCell(61)));
                        supervisionEnterprise.setCulture(ExcalUtils.handleStringXSSF(row.getCell(62)));
                        supervisionEnterprise.setClassification(ExcalUtils.handleStringXSSF(row.getCell(63)));
                        supervisionEnterprise.setOperator("操作人");
                        supervisionEnterprise.setOperateIp("123.123.123");
                        supervisionEnterprise.setOperateTime(new Date());
                        if(!supervisionEnterprise.getIdNumber().equals("")){
                            supervisionEnterpriseList.add(supervisionEnterprise);
                        }
                    }
                List<Integer> updateEnterpriseIds = new ArrayList<>();
                List<SysUser> sysUserList = new ArrayList<>();
                for(SupervisionEnterprise supervisionEnterprise : supervisionEnterpriseList){
                    if(enterpriseIdMap.get(supervisionEnterprise.getIdNumber())!=null){
                        int id = enterpriseIdMap.get(supervisionEnterprise.getIdNumber());
                        updateEnterpriseIds.add(id);
                        supervisionEnterprise.setId(id);
                    }else {
                        SysUser sysUser = new SysUser();
                        String encryptedPassword = MD5Util.md5("123456+");
                        sysUser.setUsername(supervisionEnterprise.getEnterpriseName());
                        sysUser.setLoginName(supervisionEnterprise.getIdNumber());
                        sysUser.setPassword(encryptedPassword);
                        sysUser.setUserType(1);
                        sysUser.setInfoName(supervisionEnterprise.getEnterpriseName());
                        sysUser.setInfoId(supervisionEnterprise.getId());
                        sysUser.setStatus(0);
                        sysUser.setOperator("操作人");
                        sysUser.setOperateIp("124.124.124");
                        sysUser.setOperateTime(new Date());
                        sysUserList.add(sysUser);
                    }
                }
                if(updateEnterpriseIds.size()>0){
                    supervisionEnterpriseMapper.batchDelete(updateEnterpriseIds);
                    supervisionEnCosmeticsMapper.deleteByEnterpriseIds(updateEnterpriseIds);
                    supervisionEnMedicalMapper.deleteByEnterpriseIds(updateEnterpriseIds);
                    supervisionEnDrugsBuMapper.deleteByEnterpriseIds(updateEnterpriseIds);
                    supervisionEnFoodCirMapper.deleteByEnterpriseIds(updateEnterpriseIds);
                    supervisionEnFoodBuMapper.deleteByEnterpriseIds(updateEnterpriseIds);
                    supervisionEnCommonMapper.deleteByEnterpriseIds(updateEnterpriseIds);
                    supervisionEnFoodProMapper.deleteByEnterpriseIds(updateEnterpriseIds);
                    supervisionEnProCategoryMapper.deleteByEnterpriseIds(updateEnterpriseIds);
                }
                if(supervisionEnterpriseList.size()>0){
                    supervisionEnterpriseMapper.batchInsert(supervisionEnterpriseList);
                }
                Map<String,Integer> enterpriseMap = new HashMap<>();
                for(SupervisionEnterprise supervisionEnterprise : supervisionEnterpriseList){enterpriseMap.put(supervisionEnterprise.getIdNumber(),supervisionEnterprise.getId());}
                if(sysUserList.size()>0){
                    for(SysUser sysUser:sysUserList){
                        sysUser.setInfoId(enterpriseMap.get(sysUser.getLoginName()));
                    }
                    sysUserMapper.batchInsert(sysUserList);
                }
                List<SysRoleUser> sysRoleUserList = new ArrayList<>();
                for(SysUser sysUser:sysUserList)
                {
                    SysRoleUser sysRoleUser =new SysRoleUser();
                    sysRoleUser.setUserId(sysUser.getId());
                    sysRoleUser.setRoleId(27);
                    sysRoleUser.setOperateTime(new Date());
                    sysRoleUser.setOperator("操作人");
                    sysRoleUser.setOperateIp("123.124.124");
                    sysRoleUserList.add(sysRoleUser);
                }
                if(sysRoleUserList.size()>0){
                    sysRoleUserMapper.batchInsert(sysRoleUserList);
                }

                //食品经营
                XSSFSheet sheet1 = workbook.getSheetAt(1);
                for (int j = 0; j < sheet1.getPhysicalNumberOfRows(); j++) {
                    if (j == 0) {
                        continue;//标题行
                    }
                    SupervisionEnFoodBu supervisionEnFoodBu = new SupervisionEnFoodBu();
                    XSSFRow row = sheet1.getRow(j);
                    supervisionEnFoodBu.setEnterpriseId(enterpriseMap.get(ExcalUtils.handleStringXSSF(row.getCell(0))));
                    supervisionEnFoodBu.setNumber(ExcalUtils.handleStringXSSF(row.getCell(1)));
                    supervisionEnFoodBu.setBusinessFormat(ExcalUtils.handleStringXSSF(row.getCell(2)));
                    supervisionEnFoodBu.setBusinessNotes(ExcalUtils.handleStringXSSF(row.getCell(3)));
                    supervisionEnFoodBu.setIsInternet(ExcalUtils.handleStringXSSF(row.getCell(4)));
                    supervisionEnFoodBu.setWebsite(ExcalUtils.handleStringXSSF(row.getCell(5)));
                    supervisionEnFoodBu.setIsReal(ExcalUtils.handleStringXSSF(row.getCell(6)));
                    supervisionEnFoodBu.setBusinessProject(ExcalUtils.handleStringXSSF(row.getCell(7)));
                    supervisionEnFoodBu.setGiveTime(ExcalUtils.handleDateXSSF(row.getCell(8)));
                    supervisionEnFoodBu.setStartTime(ExcalUtils.handleDateXSSF(row.getCell(9)));
                    supervisionEnFoodBu.setEndTime(ExcalUtils.handleDateXSSF(row.getCell(10)));
                    supervisionEnFoodBu.setValidityAge(ExcalUtils.handleFloatXSSF(row.getCell(11)));
                    supervisionEnFoodBu.setCategory(ExcalUtils.handleStringXSSF(row.getCell(12)));
                    supervisionEnFoodBu.setLicenseAuthority(ExcalUtils.handleStringXSSF(row.getCell(13)));
                    supervisionEnFoodBu.setRemark(ExcalUtils.handleStringXSSF(row.getCell(14)));
                    supervisionEnFoodBu.setOperator("操作人");
                    supervisionEnFoodBu.setOperateIp("123.123.123");
                    supervisionEnFoodBu.setOperateTime(new Date());
                    if(supervisionEnFoodBu.getEnterpriseId()!=null) {
                        supervisionEnFoodBuList.add(supervisionEnFoodBu);
                    }
                }
                if(supervisionEnFoodBuList.size()>0){
                    supervisionEnFoodBuMapper.batchInsert(supervisionEnFoodBuList);
                }
                //餐饮服务
                XSSFSheet sheet2 = workbook.getSheetAt(2);
                for (int j = 0; j < sheet2.getPhysicalNumberOfRows(); j++) {
                    if (j == 0) {
                        continue;//标题行
                    }
                    SupervisionEnCommon supervisionEnCommon = new SupervisionEnCommon();
                    XSSFRow row = sheet2.getRow(j);
                    supervisionEnCommon.setEnterpriseId(enterpriseMap.get(ExcalUtils.handleStringXSSF(row.getCell(0))));
                    supervisionEnCommon.setNumber(ExcalUtils.handleStringXSSF(row.getCell(1)));
                    supervisionEnCommon.setGiveTime(ExcalUtils.handleDateXSSF(row.getCell(2)));
                    supervisionEnCommon.setStartTime(ExcalUtils.handleDateXSSF(row.getCell(3)));
                    supervisionEnCommon.setEndTime(ExcalUtils.handleDateXSSF(row.getCell(4)));
                    supervisionEnCommon.setValidityAge(ExcalUtils.handleFloatXSSF(row.getCell(5)));
                    supervisionEnCommon.setBusinessType(ExcalUtils.handleStringXSSF(row.getCell(6)));
                    supervisionEnCommon.setLicenseAuthority(ExcalUtils.handleStringXSSF(row.getCell(7)));
                    supervisionEnCommon.setRemark(ExcalUtils.handleStringXSSF(row.getCell(8)));
                    supervisionEnCommon.setOperator("操作人");
                    supervisionEnCommon.setOperateIp("123.123.123");
                    supervisionEnCommon.setOperateTime(new Date());
                    if(supervisionEnCommon.getEnterpriseId()!=null) {
                        supervisionEnCommonList.add(supervisionEnCommon);
                    }
                }
                if(supervisionEnCommonList.size()>0){
                    supervisionEnCommonMapper.batchInsert(supervisionEnCommonList);
                }
                //食品流通
                XSSFSheet sheet3 = workbook.getSheetAt(3);
                for (int j = 0; j < sheet3.getPhysicalNumberOfRows(); j++) {
                    if (j == 0) {
                        continue;//标题行
                    }
                    SupervisionEnFoodCir supervisionEnFoodCir = new SupervisionEnFoodCir();
                    XSSFRow row = sheet3.getRow(j);
                    supervisionEnFoodCir.setEnterpriseId(enterpriseMap.get(ExcalUtils.handleStringXSSF(row.getCell(0))));
                    supervisionEnFoodCir.setNumber(ExcalUtils.handleStringXSSF(row.getCell(1)));
                    supervisionEnFoodCir.setRegisterAdress(ExcalUtils.handleStringXSSF(row.getCell(2)));
                    supervisionEnFoodCir.setBodyType(ExcalUtils.handleStringXSSF(row.getCell(3))=="个体"?1:0);
                    supervisionEnFoodCir.setProduceAddress(ExcalUtils.handleStringXSSF(row.getCell(4)));
                    supervisionEnFoodCir.setBusinessProject(ExcalUtils.handleStringXSSF(row.getCell(5)));
                    supervisionEnFoodCir.setBusinessType(ExcalUtils.handleStringXSSF(row.getCell(6)));
                    supervisionEnFoodCir.setGiveTime(ExcalUtils.handleDateXSSF(row.getCell(7)));
                    supervisionEnFoodCir.setStartTime(ExcalUtils.handleDateXSSF(row.getCell(8)));
                    supervisionEnFoodCir.setEndTime(ExcalUtils.handleDateXSSF(row.getCell(9)));
                    supervisionEnFoodCir.setValidityAge(ExcalUtils.handleFloatXSSF(row.getCell(10)));
                    supervisionEnFoodCir.setCopiesNumber(ExcalUtils.handleIntegerXSSF(row.getCell(11)));
                    supervisionEnFoodCir.setLicenseAuthority(ExcalUtils.handleStringXSSF(row.getCell(12)));
                    supervisionEnFoodCir.setLssuer(ExcalUtils.handleStringXSSF(row.getCell(13)));
                    supervisionEnFoodCir.setInspector(ExcalUtils.handleStringXSSF(row.getCell(14)));
                    supervisionEnFoodCir.setBusinessMode(ExcalUtils.handleStringXSSF(row.getCell(15)));
                    supervisionEnFoodCir.setRemark(ExcalUtils.handleStringXSSF(row.getCell(16)));
                    supervisionEnFoodCir.setOperator("操作人");
                    supervisionEnFoodCir.setOperateIp("123.123.123");
                    supervisionEnFoodCir.setOperateTime(new Date());
                    if(supervisionEnFoodCir.getEnterpriseId()!=null) {
                        supervisionEnFoodCirList.add(supervisionEnFoodCir);
                    }
                }
                if(supervisionEnFoodCirList.size()>0){
                    supervisionEnFoodCirMapper.batchInsert(supervisionEnFoodCirList);
                }
                //食品生产
                XSSFSheet sheet4 = workbook.getSheetAt(4);
                for (int j = 0; j < sheet4.getPhysicalNumberOfRows(); j++) {
                    if (j == 0) {
                        continue;//标题行
                    }
                    SupervisionEnFoodPro supervisionEnFoodPro = new SupervisionEnFoodPro();
                    XSSFRow row = sheet4.getRow(j);
                    supervisionEnFoodPro.setEnterpriseId(enterpriseMap.get(ExcalUtils.handleStringXSSF(row.getCell(0))));
                    supervisionEnFoodPro.setNumber(ExcalUtils.handleStringXSSF(row.getCell(1)));
                    supervisionEnFoodPro.setGiveTime(ExcalUtils.handleDateXSSF(row.getCell(2)));
                    supervisionEnFoodPro.setStartTime(ExcalUtils.handleDateXSSF(row.getCell(3)));
                    supervisionEnFoodPro.setEndTime(ExcalUtils.handleDateXSSF(row.getCell(4)));
                    supervisionEnFoodPro.setValidityAge(ExcalUtils.handleFloatXSSF(row.getCell(5)));
                    supervisionEnFoodPro.setLicenseAuthority(ExcalUtils.handleStringXSSF(row.getCell(6)));
                    supervisionEnFoodPro.setOthers(ExcalUtils.handleStringXSSF(row.getCell(7)));
                    supervisionEnFoodPro.setOperator("操作人");
                    supervisionEnFoodPro.setOperatorIp("123.123.123");
                    supervisionEnFoodPro.setOperatorTime(new Date());
                    if(supervisionEnFoodPro.getEnterpriseId()!=null){
                        supervisionEnFoodProList.add(supervisionEnFoodPro);
                    }
                }
                if(supervisionEnFoodProList.size()>0){
                    supervisionEnFoodProMapper.batchInsert(supervisionEnFoodProList);
                }
                XSSFSheet sheet5 = workbook.getSheetAt(5);
                for (int j = 0; j < sheet5.getPhysicalNumberOfRows(); j++) {
                    if (j == 0) {
                        continue;//标题行
                    }
                    SupervisionEnProCategory supervisionEnProCategory = new SupervisionEnProCategory();
                    XSSFRow row = sheet5.getRow(j);
                    supervisionEnProCategory.setParentId(enterpriseMap.get(ExcalUtils.handleStringXSSF(row.getCell(0))));
                    supervisionEnProCategory.setCategory(ExcalUtils.handleStringXSSF(row.getCell(1)));
                    supervisionEnProCategory.setCode(ExcalUtils.handleStringXSSF(row.getCell(2)));
                    supervisionEnProCategory.setName(ExcalUtils.handleStringXSSF(row.getCell(3)));
                    supervisionEnProCategory.setDetail(ExcalUtils.handleStringXSSF(row.getCell(4)));
                    supervisionEnProCategory.setOperator("操作人");
                    supervisionEnProCategory.setOperateIp("123.123.123");
                    supervisionEnProCategory.setOperateTime(new Date());
                    if(supervisionEnProCategory.getParentId()!=null) {
                        supervisionEnProCategoryList.add(supervisionEnProCategory);
                    }
                }
                if(supervisionEnProCategoryList.size()>0){
                    supervisionEnProCategoryMapper.batchInsert(supervisionEnProCategoryList);
                }

               //药品经营
                XSSFSheet sheet6 = workbook.getSheetAt(6);
                for (int j = 0; j < sheet6.getPhysicalNumberOfRows(); j++) {
                    if (j == 0) {
                        continue;//标题行
                    }
                    SupervisionEnDrugsBu supervisionEnDrugsBu = new SupervisionEnDrugsBu();
                    XSSFRow row = sheet6.getRow(j);
                    supervisionEnDrugsBu.setEnterpriseId(enterpriseMap.get(ExcalUtils.handleStringXSSF(row.getCell(0))));
                    supervisionEnDrugsBu.setNumber(ExcalUtils.handleStringXSSF(row.getCell(1)));
                    supervisionEnDrugsBu.setQualityPerson(ExcalUtils.handleStringXSSF(row.getCell(2)));
                    supervisionEnDrugsBu.setOperationMode(ExcalUtils.handleStringXSSF(row.getCell(3)));
                    supervisionEnDrugsBu.setWarehouseAddress(ExcalUtils.handleStringXSSF(row.getCell(4)));
                    supervisionEnDrugsBu.setGiveTime(ExcalUtils.handleDateXSSF(row.getCell(5)));
                    supervisionEnDrugsBu.setStartTime(ExcalUtils.handleDateXSSF(row.getCell(6)));
                    supervisionEnDrugsBu.setEndTime(ExcalUtils.handleDateXSSF(row.getCell(7)));
                    supervisionEnDrugsBu.setGspNumber(ExcalUtils.handleStringXSSF(row.getCell(8)));
                    supervisionEnDrugsBu.setGspStartTime(ExcalUtils.handleDateXSSF(row.getCell(9)));
                    supervisionEnDrugsBu.setGspFinishTime(ExcalUtils.handleDateXSSF(row.getCell(10)));
                    supervisionEnDrugsBu.setValidityTime(ExcalUtils.handleFloatXSSF(row.getCell(11)));
                    supervisionEnDrugsBu.setLicenseAuthority(ExcalUtils.handleStringXSSF(row.getCell(12)));
                    supervisionEnDrugsBu.setBusinessScope(ExcalUtils.handleStringXSSF(row.getCell(13)));
                    supervisionEnDrugsBu.setRemark(ExcalUtils.handleStringXSSF(row.getCell(14)));
                    supervisionEnDrugsBu.setOperator("操作人");
                    supervisionEnDrugsBu.setOperatorIp("123.123.123");
                    supervisionEnDrugsBu.setOperatorTime(new Date());
                    if(supervisionEnDrugsBu.getEnterpriseId()!=null) {
                        supervisionEnDrugsBuList.add(supervisionEnDrugsBu);
                    }
                }
                if(supervisionEnDrugsBuList.size()>0){
                    supervisionEnDrugsBuMapper.batchInsert(supervisionEnDrugsBuList);
                }

                XSSFSheet sheet7 = workbook.getSheetAt(7);
                for (int j = 0; j < sheet7.getPhysicalNumberOfRows(); j++) {
                    if (j == 0) {
                        continue;//标题行
                    }
                    SupervisionEnMedical supervisionEnMedical = new SupervisionEnMedical();
                    XSSFRow row = sheet7.getRow(j);
                    supervisionEnMedical.setEnterpriseId(enterpriseMap.get(ExcalUtils.handleStringXSSF(row.getCell(0))));
                    supervisionEnMedical.setRegisterNumber(ExcalUtils.handleStringXSSF(row.getCell(1)));
                    supervisionEnMedical.setSuperviseCategory(ExcalUtils.handleStringXSSF(row.getCell(2)));
                    supervisionEnMedical.setLssueAuthority(ExcalUtils.handleStringXSSF(row.getCell(3)));
                    supervisionEnMedical.setGiveTime(ExcalUtils.handleDateXSSF(row.getCell(4)));
                    supervisionEnMedical.setStartTime(ExcalUtils.handleDateXSSF(row.getCell(5)));
                    supervisionEnMedical.setEndTime(ExcalUtils.handleDateXSSF(row.getCell(6)));
                    supervisionEnMedical.setMedicalSubject(ExcalUtils.handleStringXSSF(row.getCell(7)));
                    supervisionEnMedical.setRemark(ExcalUtils.handleStringXSSF(row.getCell(8)));
                    supervisionEnMedical.setOperator("操作人");
                    supervisionEnMedical.setOperateIp("123.123.123");
                    supervisionEnMedical.setOperateTime(new Date());
                    if(supervisionEnMedical.getEnterpriseId()!=null) {
                        supervisionEnMedicalList.add(supervisionEnMedical);
                    }
                }
                if(supervisionEnMedicalList.size()>0){
                    supervisionEnMedicalMapper.batchInsert(supervisionEnMedicalList);
                }
                XSSFSheet sheet8 = workbook.getSheetAt(8);
                for (int j = 0; j < sheet8.getPhysicalNumberOfRows(); j++) {
                    if (j == 0) {
                        continue;//标题行
                    }
                    SupervisionEnCosmetics supervisionEnCosmetics = new SupervisionEnCosmetics();
                    XSSFRow row = sheet8.getRow(j);
                    supervisionEnCosmetics.setEnterpriseId(enterpriseMap.get(ExcalUtils.handleStringXSSF(row.getCell(0))));
                    supervisionEnCosmetics.setRegisterCode(ExcalUtils.handleStringXSSF(row.getCell(1)));
                    supervisionEnCosmetics.setValidityAge(ExcalUtils.handleFloatXSSF(row.getCell(2)));
                    supervisionEnCosmetics.setLicenseAuthority(ExcalUtils.handleStringXSSF(row.getCell(3)));
                    supervisionEnCosmetics.setWarehouse(ExcalUtils.handleStringXSSF(row.getCell(4)));
                    supervisionEnCosmetics.setGiveTime(ExcalUtils.handleDateXSSF(row.getCell(5)));
                    supervisionEnCosmetics.setStartTime(ExcalUtils.handleDateXSSF(row.getCell(6)));
                    supervisionEnCosmetics.setEndTime(ExcalUtils.handleDateXSSF(row.getCell(7)));
                    supervisionEnCosmetics.setLicenseProject(ExcalUtils.handleStringXSSF(row.getCell(8)));
                    supervisionEnCosmetics.setRemark(ExcalUtils.handleStringXSSF(row.getCell(9)));
                    supervisionEnCosmetics.setOperator("操作人");
                    supervisionEnCosmetics.setOperateIp("123.123.123");
                    supervisionEnCosmetics.setOperateTime(new Date());
                    if(supervisionEnCosmetics.getEnterpriseId()!=null) {
                        supervisionEnCosmeticsList.add(supervisionEnCosmetics);
                    }
                }
                if (supervisionEnCosmeticsList.size()>0){
                    supervisionEnCosmeticsMapper.batchInsert(supervisionEnCosmeticsList);
                }
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(type == 3){
            try {
                HSSFWorkbook workbook = new HSSFWorkbook(file.getInputStream());
                HSSFSheet sheet0 = workbook.getSheetAt(0);
                for (int j = 0; j < sheet0.getPhysicalNumberOfRows(); j++) {
                    if (j == 0) {
                        continue;//标题行
                    }
                    SupervisionEnterprise supervisionEnterprise = new SupervisionEnterprise();
                    HSSFRow row = sheet0.getRow(j);
                    supervisionEnterprise.setEnterpriseName(ExcalUtils.handleStringHSSF(row.getCell(0)));
                    supervisionEnterprise.setShopName(ExcalUtils.handleStringHSSF(row.getCell(1)));
                    supervisionEnterprise.setPostalCode(ExcalUtils.handleStringHSSF(row.getCell(2)));
                    supervisionEnterprise.setRegisteredAddress(ExcalUtils.handleStringHSSF(row.getCell(3)));
                    supervisionEnterprise.setBusinessAddress(ExcalUtils.handleStringHSSF(row.getCell(4)));
                    supervisionEnterprise.setLegalPerson(ExcalUtils.handleStringHSSF(row.getCell(5)));
                    supervisionEnterprise.setIdNumber(ExcalUtils.handleStringHSSF(row.getCell(6)));
                    supervisionEnterprise.setLicenseNumber(ExcalUtils.handleStringHSSF(row.getCell(7)));
                    supervisionEnterprise.setCantacts(ExcalUtils.handleStringHSSF(row.getCell(8)));
                    supervisionEnterprise.setCantactWay(ExcalUtils.handleStringHSSF(row.getCell(9)));
                    supervisionEnterprise.setRegulators(importCheckDept(ExcalUtils.handleStringHSSF(row.getCell(10)),deptMap,ExcalUtils.handleStringHSSF(row.getCell(6))));
                    supervisionEnterprise.setArea(importCheckArea(ExcalUtils.handleStringHSSF(row.getCell(11)),areaMap,ExcalUtils.handleStringHSSF(row.getCell(6))));
                    supervisionEnterprise.setGrid(areaMap.get(ExcalUtils.handleStringHSSF(row.getCell(12)))==null?0:areaMap.get(ExcalUtils.handleStringHSSF(row.getCell(12))));
                    supervisionEnterprise.setSupervisor(ExcalUtils.handleStringHSSF(row.getCell(13)));
                    supervisionEnterprise.setEnterpriseScale(ExcalUtils.handleStringHSSF(row.getCell(14)));
                    supervisionEnterprise.setGridPerson(ExcalUtils.handleStringHSSF(row.getCell(15)));
                    supervisionEnterprise.setDynamicGrade(ExcalUtils.handleStringHSSF(row.getCell(16)));
                    supervisionEnterprise.setYearAssessment(ExcalUtils.handleStringHSSF(row.getCell(17)));
                    supervisionEnterprise.setEmail(ExcalUtils.handleStringHSSF(row.getCell(18)));
                    supervisionEnterprise.setOfficePhone(ExcalUtils.handleStringHSSF(row.getCell(19)));
                    supervisionEnterprise.setPatrolFrequency(ExcalUtils.handleStringHSSF(row.getCell(20)));
                    supervisionEnterprise.setTransformationType(ExcalUtils.handleStringHSSF(row.getCell(21)));
                    supervisionEnterprise.setPermissionState(ExcalUtils.handleStringHSSF(row.getCell(22)));
                    supervisionEnterprise.setPermissionType(ExcalUtils.handleStringHSSF(row.getCell(23)));
                    supervisionEnterprise.setIsStop(ExcalUtils.handleIntegerHSSF(row.getCell(24)));
                    supervisionEnterprise.setIpIdNumber(ExcalUtils.handleStringHSSF(row.getCell(25)));
                    supervisionEnterprise.setIpMobilePhone(ExcalUtils.handleStringHSSF(row.getCell(26)));
                    supervisionEnterprise.setIpSexy(ExcalUtils.handleStringHSSF(row.getCell(27)));
                    supervisionEnterprise.setIpEducation(ExcalUtils.handleStringHSSF(row.getCell(28)));
                    supervisionEnterprise.setIpPoliticalOutlook(ExcalUtils.handleStringHSSF(row.getCell(29)));
                    supervisionEnterprise.setIpCurrentAddress(ExcalUtils.handleStringHSSF(row.getCell(30)));
                    supervisionEnterprise.setIpNation(ExcalUtils.handleStringHSSF(row.getCell(31)));
                    supervisionEnterprise.setIpEmail(ExcalUtils.handleStringHSSF(row.getCell(32)));
                    supervisionEnterprise.setIpPostalCode(ExcalUtils.handleStringHSSF(row.getCell(33)));
                    supervisionEnterprise.setSpName(ExcalUtils.handleStringHSSF(row.getCell(34)));
                    supervisionEnterprise.setSpidNumber(ExcalUtils.handleStringHSSF(row.getCell(35)));
                    supervisionEnterprise.setSpOfficePhone(ExcalUtils.handleStringHSSF(row.getCell(36)));
                    supervisionEnterprise.setSpMobilePhone(ExcalUtils.handleStringHSSF(row.getCell(37)));
                    supervisionEnterprise.setSpEmail(ExcalUtils.handleStringHSSF(row.getCell(38)));
                    supervisionEnterprise.setSpSexy(ExcalUtils.handleStringHSSF(row.getCell(39)));
                    supervisionEnterprise.setSpEducation(ExcalUtils.handleStringHSSF(row.getCell(40)));
                    supervisionEnterprise.setSpCurrentAddress(ExcalUtils.handleStringHSSF(row.getCell(41)));
                    supervisionEnterprise.setSpTraining(ExcalUtils.handleStringHSSF(row.getCell(42)));
                    supervisionEnterprise.setOperationMode(ExcalUtils.handleStringHSSF(row.getCell(43)));
                    supervisionEnterprise.setHousingProperty(ExcalUtils.handleStringHSSF(row.getCell(44)));
                    supervisionEnterprise.setOwner(ExcalUtils.handleStringHSSF(row.getCell(45)));
                    supervisionEnterprise.setOwnerIdNumber(ExcalUtils.handleStringHSSF(row.getCell(46)));
                    supervisionEnterprise.setOwnerMobilePhone(ExcalUtils.handleStringHSSF(row.getCell(47)));
                    supervisionEnterprise.setAgent(ExcalUtils.handleStringHSSF(row.getCell(48)));
                    supervisionEnterprise.setAgentIdNumber(ExcalUtils.handleStringHSSF(row.getCell(49)));
                    supervisionEnterprise.setAgentMobilePhone(ExcalUtils.handleStringHSSF(row.getCell(50)));
                    supervisionEnterprise.setOtherPhone(ExcalUtils.handleStringHSSF(row.getCell(51)));
                    supervisionEnterprise.setIntegrityLevel(ExcalUtils.handleStringHSSF(row.getCell(52)));
                    supervisionEnterprise.setProductionArea(ExcalUtils.handleIntegerHSSF(row.getCell(53)));
                    supervisionEnterprise.setFixedAssets(ExcalUtils.handleIntegerHSSF(row.getCell(54)));
                    supervisionEnterprise.setPractitioners(ExcalUtils.handleStringHSSF(row.getCell(55)));
                    supervisionEnterprise.setExaminationPopulation(ExcalUtils.handleIntegerHSSF(row.getCell(56)));
                    supervisionEnterprise.setWarehouse1(ExcalUtils.handleStringHSSF(row.getCell(57)));
                    supervisionEnterprise.setWarehouse2(ExcalUtils.handleStringHSSF(row.getCell(58)));
                    supervisionEnterprise.setWarehouse3(ExcalUtils.handleStringHSSF(row.getCell(59)));
                    supervisionEnterprise.setAbbreviation(ExcalUtils.handleStringHSSF(row.getCell(60)));
                    supervisionEnterprise.setIntroduction(ExcalUtils.handleStringHSSF(row.getCell(61)));
                    supervisionEnterprise.setCulture(ExcalUtils.handleStringHSSF(row.getCell(62)));
                    supervisionEnterprise.setClassification(ExcalUtils.handleStringHSSF(row.getCell(63)));
                    supervisionEnterprise.setOperator("操作人");
                    supervisionEnterprise.setOperateIp("123.123.123");
                    supervisionEnterprise.setOperateTime(new Date());
                    if(!supervisionEnterprise.getIdNumber().equals("")){
                        supervisionEnterpriseList.add(supervisionEnterprise);
                    }
                }
                List<Integer> updateEnterpriseIds = new ArrayList<>();
                List<SysUser> sysUserList = new ArrayList<>();
                for(SupervisionEnterprise supervisionEnterprise : supervisionEnterpriseList){
                    if(enterpriseIdMap.get(supervisionEnterprise.getIdNumber())!=null){
                        int id = enterpriseIdMap.get(supervisionEnterprise.getIdNumber());
                        updateEnterpriseIds.add(id);
                        supervisionEnterprise.setId(id);
                    }else {
                        SysUser sysUser = new SysUser();
                        String encryptedPassword = MD5Util.md5("123456+");
                        sysUser.setUsername(supervisionEnterprise.getEnterpriseName());
                        sysUser.setLoginName(supervisionEnterprise.getIdNumber());
                        sysUser.setPassword(encryptedPassword);
                        sysUser.setUserType(1);
                        sysUser.setInfoName(supervisionEnterprise.getEnterpriseName());
                        sysUser.setInfoId(supervisionEnterprise.getId());
                        sysUser.setStatus(0);
                        sysUser.setOperator("操作人");
                        sysUser.setOperateIp("124.124.124");
                        sysUser.setOperateTime(new Date());
                        sysUserList.add(sysUser);
                    }
                }
                if(updateEnterpriseIds.size()>0){
                    supervisionEnterpriseMapper.batchDelete(updateEnterpriseIds);
                    supervisionEnCosmeticsMapper.deleteByEnterpriseIds(updateEnterpriseIds);
                    supervisionEnMedicalMapper.deleteByEnterpriseIds(updateEnterpriseIds);
                    supervisionEnDrugsBuMapper.deleteByEnterpriseIds(updateEnterpriseIds);
                    supervisionEnFoodCirMapper.deleteByEnterpriseIds(updateEnterpriseIds);
                    supervisionEnFoodBuMapper.deleteByEnterpriseIds(updateEnterpriseIds);
                    supervisionEnCommonMapper.deleteByEnterpriseIds(updateEnterpriseIds);
                    supervisionEnFoodProMapper.deleteByEnterpriseIds(updateEnterpriseIds);
                    supervisionEnProCategoryMapper.deleteByEnterpriseIds(updateEnterpriseIds);
                }
                if(supervisionEnterpriseList.size()>0){
                    supervisionEnterpriseMapper.batchInsert(supervisionEnterpriseList);
                }
                Map<String,Integer> enterpriseMap = new HashMap<>();
                for(SupervisionEnterprise supervisionEnterprise : supervisionEnterpriseList){enterpriseMap.put(supervisionEnterprise.getIdNumber(),supervisionEnterprise.getId());}
                if(sysUserList.size()>0){
                    for(SysUser sysUser:sysUserList){
                        sysUser.setInfoId(enterpriseMap.get(sysUser.getLoginName()));
                    }
                    sysUserMapper.batchInsert(sysUserList);
                }
                List<SysRoleUser> sysRoleUserList = new ArrayList<>();
                for(SysUser sysUser:sysUserList)
                {
                    SysRoleUser sysRoleUser =new SysRoleUser();
                    sysRoleUser.setUserId(sysUser.getId());
                    sysRoleUser.setRoleId(27);
                    sysRoleUser.setOperateTime(new Date());
                    sysRoleUser.setOperator("操作人");
                    sysRoleUser.setOperateIp("123.124.124");
                    sysRoleUserList.add(sysRoleUser);
                }
                if(sysRoleUserList.size()>0){
                    sysRoleUserMapper.batchInsert(sysRoleUserList);
                }

                //食品经营
                HSSFSheet sheet1 = workbook.getSheetAt(1);
                for (int j = 0; j < sheet1.getPhysicalNumberOfRows(); j++) {
                    if (j == 0) {
                        continue;//标题行
                    }
                    SupervisionEnFoodBu supervisionEnFoodBu = new SupervisionEnFoodBu();
                    HSSFRow row = sheet1.getRow(j);
                    supervisionEnFoodBu.setEnterpriseId(enterpriseMap.get(ExcalUtils.handleStringHSSF(row.getCell(0))));
                    supervisionEnFoodBu.setNumber(ExcalUtils.handleStringHSSF(row.getCell(1)));
                    supervisionEnFoodBu.setBusinessFormat(ExcalUtils.handleStringHSSF(row.getCell(2)));
                    supervisionEnFoodBu.setBusinessNotes(ExcalUtils.handleStringHSSF(row.getCell(3)));
                    supervisionEnFoodBu.setIsInternet(ExcalUtils.handleStringHSSF(row.getCell(4)));
                    supervisionEnFoodBu.setWebsite(ExcalUtils.handleStringHSSF(row.getCell(5)));
                    supervisionEnFoodBu.setIsReal(ExcalUtils.handleStringHSSF(row.getCell(6)));
                    supervisionEnFoodBu.setBusinessProject(ExcalUtils.handleStringHSSF(row.getCell(7)));
                    supervisionEnFoodBu.setGiveTime(ExcalUtils.handleDateHSSF(row.getCell(8)));
                    supervisionEnFoodBu.setStartTime(ExcalUtils.handleDateHSSF(row.getCell(9)));
                    supervisionEnFoodBu.setEndTime(ExcalUtils.handleDateHSSF(row.getCell(10)));
                    supervisionEnFoodBu.setValidityAge(ExcalUtils.handleFloatHSSF(row.getCell(11)));
                    supervisionEnFoodBu.setCategory(ExcalUtils.handleStringHSSF(row.getCell(12)));
                    supervisionEnFoodBu.setLicenseAuthority(ExcalUtils.handleStringHSSF(row.getCell(13)));
                    supervisionEnFoodBu.setRemark(ExcalUtils.handleStringHSSF(row.getCell(14)));
                    supervisionEnFoodBu.setOperator("操作人");
                    supervisionEnFoodBu.setOperateIp("123.123.123");
                    supervisionEnFoodBu.setOperateTime(new Date());
                    if(supervisionEnFoodBu.getEnterpriseId()!=null) {
                        supervisionEnFoodBuList.add(supervisionEnFoodBu);
                    }
                }
                if(supervisionEnFoodBuList.size()>0){
                    supervisionEnFoodBuMapper.batchInsert(supervisionEnFoodBuList);
                }
                //餐饮服务
                HSSFSheet sheet2 = workbook.getSheetAt(2);
                for (int j = 0; j < sheet2.getPhysicalNumberOfRows(); j++) {
                    if (j == 0) {
                        continue;//标题行
                    }
                    SupervisionEnCommon supervisionEnCommon = new SupervisionEnCommon();
                    HSSFRow row = sheet2.getRow(j);
                    supervisionEnCommon.setEnterpriseId(enterpriseMap.get(ExcalUtils.handleStringHSSF(row.getCell(0))));
                    supervisionEnCommon.setNumber(ExcalUtils.handleStringHSSF(row.getCell(1)));
                    supervisionEnCommon.setGiveTime(ExcalUtils.handleDateHSSF(row.getCell(2)));
                    supervisionEnCommon.setStartTime(ExcalUtils.handleDateHSSF(row.getCell(3)));
                    supervisionEnCommon.setEndTime(ExcalUtils.handleDateHSSF(row.getCell(4)));
                    supervisionEnCommon.setValidityAge(ExcalUtils.handleFloatHSSF(row.getCell(5)));
                    supervisionEnCommon.setBusinessType(ExcalUtils.handleStringHSSF(row.getCell(6)));
                    supervisionEnCommon.setLicenseAuthority(ExcalUtils.handleStringHSSF(row.getCell(7)));
                    supervisionEnCommon.setRemark(ExcalUtils.handleStringHSSF(row.getCell(8)));
                    supervisionEnCommon.setOperator("操作人");
                    supervisionEnCommon.setOperateIp("123.123.123");
                    supervisionEnCommon.setOperateTime(new Date());
                    if(supervisionEnCommon.getEnterpriseId()!=null) {
                        supervisionEnCommonList.add(supervisionEnCommon);
                    }
                }
                if(supervisionEnCommonList.size()>0){
                    supervisionEnCommonMapper.batchInsert(supervisionEnCommonList);
                }
                //食品流通
                HSSFSheet sheet3 = workbook.getSheetAt(3);
                for (int j = 0; j < sheet3.getPhysicalNumberOfRows(); j++) {
                    if (j == 0) {
                        continue;//标题行
                    }
                    SupervisionEnFoodCir supervisionEnFoodCir = new SupervisionEnFoodCir();
                    HSSFRow row = sheet3.getRow(j);
                    supervisionEnFoodCir.setEnterpriseId(enterpriseMap.get(ExcalUtils.handleStringHSSF(row.getCell(0))));
                    supervisionEnFoodCir.setNumber(ExcalUtils.handleStringHSSF(row.getCell(1)));
                    supervisionEnFoodCir.setRegisterAdress(ExcalUtils.handleStringHSSF(row.getCell(2)));
                    supervisionEnFoodCir.setBodyType(ExcalUtils.handleStringHSSF(row.getCell(3))=="个体"?1:0);
                    supervisionEnFoodCir.setProduceAddress(ExcalUtils.handleStringHSSF(row.getCell(4)));
                    supervisionEnFoodCir.setBusinessProject(ExcalUtils.handleStringHSSF(row.getCell(5)));
                    supervisionEnFoodCir.setBusinessType(ExcalUtils.handleStringHSSF(row.getCell(6)));
                    supervisionEnFoodCir.setGiveTime(ExcalUtils.handleDateHSSF(row.getCell(7)));
                    supervisionEnFoodCir.setStartTime(ExcalUtils.handleDateHSSF(row.getCell(8)));
                    supervisionEnFoodCir.setEndTime(ExcalUtils.handleDateHSSF(row.getCell(9)));
                    supervisionEnFoodCir.setValidityAge(ExcalUtils.handleFloatHSSF(row.getCell(10)));
                    supervisionEnFoodCir.setCopiesNumber(ExcalUtils.handleIntegerHSSF(row.getCell(11)));
                    supervisionEnFoodCir.setLicenseAuthority(ExcalUtils.handleStringHSSF(row.getCell(12)));
                    supervisionEnFoodCir.setLssuer(ExcalUtils.handleStringHSSF(row.getCell(13)));
                    supervisionEnFoodCir.setInspector(ExcalUtils.handleStringHSSF(row.getCell(14)));
                    supervisionEnFoodCir.setBusinessMode(ExcalUtils.handleStringHSSF(row.getCell(15)));
                    supervisionEnFoodCir.setRemark(ExcalUtils.handleStringHSSF(row.getCell(16)));
                    supervisionEnFoodCir.setOperator("操作人");
                    supervisionEnFoodCir.setOperateIp("123.123.123");
                    supervisionEnFoodCir.setOperateTime(new Date());
                    if(supervisionEnFoodCir.getEnterpriseId()!=null) {
                        supervisionEnFoodCirList.add(supervisionEnFoodCir);
                    }
                }
                if(supervisionEnFoodCirList.size()>0){
                    supervisionEnFoodCirMapper.batchInsert(supervisionEnFoodCirList);
                }
                //食品生产
                HSSFSheet sheet4 = workbook.getSheetAt(4);
                for (int j = 0; j < sheet4.getPhysicalNumberOfRows(); j++) {
                    if (j == 0) {
                        continue;//标题行
                    }
                    SupervisionEnFoodPro supervisionEnFoodPro = new SupervisionEnFoodPro();
                    HSSFRow row = sheet4.getRow(j);
                    supervisionEnFoodPro.setEnterpriseId(enterpriseMap.get(ExcalUtils.handleStringHSSF(row.getCell(0))));
                    supervisionEnFoodPro.setNumber(ExcalUtils.handleStringHSSF(row.getCell(1)));
                    supervisionEnFoodPro.setGiveTime(ExcalUtils.handleDateHSSF(row.getCell(2)));
                    supervisionEnFoodPro.setStartTime(ExcalUtils.handleDateHSSF(row.getCell(3)));
                    supervisionEnFoodPro.setEndTime(ExcalUtils.handleDateHSSF(row.getCell(4)));
                    supervisionEnFoodPro.setValidityAge(ExcalUtils.handleFloatHSSF(row.getCell(5)));
                    supervisionEnFoodPro.setLicenseAuthority(ExcalUtils.handleStringHSSF(row.getCell(6)));
                    supervisionEnFoodPro.setOthers(ExcalUtils.handleStringHSSF(row.getCell(7)));
                    supervisionEnFoodPro.setOperator("操作人");
                    supervisionEnFoodPro.setOperatorIp("123.123.123");
                    supervisionEnFoodPro.setOperatorTime(new Date());
                    if(supervisionEnFoodPro.getEnterpriseId()!=null){
                        supervisionEnFoodProList.add(supervisionEnFoodPro);
                    }
                }
                if(supervisionEnFoodProList.size()>0){
                    supervisionEnFoodProMapper.batchInsert(supervisionEnFoodProList);
                }
                HSSFSheet sheet5 = workbook.getSheetAt(5);
                for (int j = 0; j < sheet5.getPhysicalNumberOfRows(); j++) {
                    if (j == 0) {
                        continue;//标题行
                    }
                    SupervisionEnProCategory supervisionEnProCategory = new SupervisionEnProCategory();
                    HSSFRow row = sheet5.getRow(j);
                    supervisionEnProCategory.setParentId(enterpriseMap.get(ExcalUtils.handleStringHSSF(row.getCell(0))));
                    supervisionEnProCategory.setCategory(ExcalUtils.handleStringHSSF(row.getCell(1)));
                    supervisionEnProCategory.setCode(ExcalUtils.handleStringHSSF(row.getCell(2)));
                    supervisionEnProCategory.setName(ExcalUtils.handleStringHSSF(row.getCell(3)));
                    supervisionEnProCategory.setDetail(ExcalUtils.handleStringHSSF(row.getCell(4)));
                    supervisionEnProCategory.setOperator("操作人");
                    supervisionEnProCategory.setOperateIp("123.123.123");
                    supervisionEnProCategory.setOperateTime(new Date());
                    if(supervisionEnProCategory.getParentId()!=null) {
                        supervisionEnProCategoryList.add(supervisionEnProCategory);
                    }
                }
                if(supervisionEnProCategoryList.size()>0){
                    supervisionEnProCategoryMapper.batchInsert(supervisionEnProCategoryList);
                }

                //药品经营
                HSSFSheet sheet6 = workbook.getSheetAt(6);
                for (int j = 0; j < sheet6.getPhysicalNumberOfRows(); j++) {
                    if (j == 0) {
                        continue;//标题行
                    }
                    SupervisionEnDrugsBu supervisionEnDrugsBu = new SupervisionEnDrugsBu();
                    HSSFRow row = sheet6.getRow(j);
                    supervisionEnDrugsBu.setEnterpriseId(enterpriseMap.get(ExcalUtils.handleStringHSSF(row.getCell(0))));
                    supervisionEnDrugsBu.setNumber(ExcalUtils.handleStringHSSF(row.getCell(1)));
                    supervisionEnDrugsBu.setQualityPerson(ExcalUtils.handleStringHSSF(row.getCell(2)));
                    supervisionEnDrugsBu.setOperationMode(ExcalUtils.handleStringHSSF(row.getCell(3)));
                    supervisionEnDrugsBu.setWarehouseAddress(ExcalUtils.handleStringHSSF(row.getCell(4)));
                    supervisionEnDrugsBu.setGiveTime(ExcalUtils.handleDateHSSF(row.getCell(5)));
                    supervisionEnDrugsBu.setStartTime(ExcalUtils.handleDateHSSF(row.getCell(6)));
                    supervisionEnDrugsBu.setEndTime(ExcalUtils.handleDateHSSF(row.getCell(7)));
                    supervisionEnDrugsBu.setGspNumber(ExcalUtils.handleStringHSSF(row.getCell(8)));
                    supervisionEnDrugsBu.setGspStartTime(ExcalUtils.handleDateHSSF(row.getCell(9)));
                    supervisionEnDrugsBu.setGspFinishTime(ExcalUtils.handleDateHSSF(row.getCell(10)));
                    supervisionEnDrugsBu.setValidityTime(ExcalUtils.handleFloatHSSF(row.getCell(11)));
                    supervisionEnDrugsBu.setLicenseAuthority(ExcalUtils.handleStringHSSF(row.getCell(12)));
                    supervisionEnDrugsBu.setBusinessScope(ExcalUtils.handleStringHSSF(row.getCell(13)));
                    supervisionEnDrugsBu.setRemark(ExcalUtils.handleStringHSSF(row.getCell(14)));
                    supervisionEnDrugsBu.setOperator("操作人");
                    supervisionEnDrugsBu.setOperatorIp("123.123.123");
                    supervisionEnDrugsBu.setOperatorTime(new Date());
                    if(supervisionEnDrugsBu.getEnterpriseId()!=null) {
                        supervisionEnDrugsBuList.add(supervisionEnDrugsBu);
                    }
                }
                if(supervisionEnDrugsBuList.size()>0){
                    supervisionEnDrugsBuMapper.batchInsert(supervisionEnDrugsBuList);
                }

                HSSFSheet sheet7 = workbook.getSheetAt(7);
                for (int j = 0; j < sheet7.getPhysicalNumberOfRows(); j++) {
                    if (j == 0) {
                        continue;//标题行
                    }
                    SupervisionEnMedical supervisionEnMedical = new SupervisionEnMedical();
                    HSSFRow row = sheet7.getRow(j);
                    supervisionEnMedical.setEnterpriseId(enterpriseMap.get(ExcalUtils.handleStringHSSF(row.getCell(0))));
                    supervisionEnMedical.setRegisterNumber(ExcalUtils.handleStringHSSF(row.getCell(1)));
                    supervisionEnMedical.setSuperviseCategory(ExcalUtils.handleStringHSSF(row.getCell(2)));
                    supervisionEnMedical.setLssueAuthority(ExcalUtils.handleStringHSSF(row.getCell(3)));
                    supervisionEnMedical.setGiveTime(ExcalUtils.handleDateHSSF(row.getCell(4)));
                    supervisionEnMedical.setStartTime(ExcalUtils.handleDateHSSF(row.getCell(5)));
                    supervisionEnMedical.setEndTime(ExcalUtils.handleDateHSSF(row.getCell(6)));
                    supervisionEnMedical.setMedicalSubject(ExcalUtils.handleStringHSSF(row.getCell(7)));
                    supervisionEnMedical.setRemark(ExcalUtils.handleStringHSSF(row.getCell(8)));
                    supervisionEnMedical.setOperator("操作人");
                    supervisionEnMedical.setOperateIp("123.123.123");
                    supervisionEnMedical.setOperateTime(new Date());
                    if(supervisionEnMedical.getEnterpriseId()!=null) {
                        supervisionEnMedicalList.add(supervisionEnMedical);
                    }
                }
                if(supervisionEnMedicalList.size()>0){
                    supervisionEnMedicalMapper.batchInsert(supervisionEnMedicalList);
                }
                HSSFSheet sheet8 = workbook.getSheetAt(8);
                for (int j = 0; j < sheet8.getPhysicalNumberOfRows(); j++) {
                    if (j == 0) {
                        continue;//标题行
                    }
                    SupervisionEnCosmetics supervisionEnCosmetics = new SupervisionEnCosmetics();
                    HSSFRow row = sheet8.getRow(j);
                    supervisionEnCosmetics.setEnterpriseId(enterpriseMap.get(ExcalUtils.handleStringHSSF(row.getCell(0))));
                    supervisionEnCosmetics.setRegisterCode(ExcalUtils.handleStringHSSF(row.getCell(1)));
                    supervisionEnCosmetics.setValidityAge(ExcalUtils.handleFloatHSSF(row.getCell(2)));
                    supervisionEnCosmetics.setLicenseAuthority(ExcalUtils.handleStringHSSF(row.getCell(3)));
                    supervisionEnCosmetics.setWarehouse(ExcalUtils.handleStringHSSF(row.getCell(4)));
                    supervisionEnCosmetics.setGiveTime(ExcalUtils.handleDateHSSF(row.getCell(5)));
                    supervisionEnCosmetics.setStartTime(ExcalUtils.handleDateHSSF(row.getCell(6)));
                    supervisionEnCosmetics.setEndTime(ExcalUtils.handleDateHSSF(row.getCell(7)));
                    supervisionEnCosmetics.setLicenseProject(ExcalUtils.handleStringHSSF(row.getCell(8)));
                    supervisionEnCosmetics.setRemark(ExcalUtils.handleStringHSSF(row.getCell(9)));
                    supervisionEnCosmetics.setOperator("操作人");
                    supervisionEnCosmetics.setOperateIp("123.123.123");
                    supervisionEnCosmetics.setOperateTime(new Date());
                    if(supervisionEnCosmetics.getEnterpriseId()!=null) {
                        supervisionEnCosmeticsList.add(supervisionEnCosmetics);
                    }
                }
                if (supervisionEnCosmeticsList.size()>0){
                    supervisionEnCosmeticsMapper.batchInsert(supervisionEnCosmeticsList);
                }
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"文件错误");
        }
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
