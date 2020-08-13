package com.example.upc.controller.miniProgram;

import com.example.upc.controller.param.EnterpriseParam;
import com.example.upc.controller.param.UserParam;
import com.example.upc.dataobject.*;
import com.example.upc.redis.UserSessionService;
import com.example.upc.service.*;
import com.example.upc.util.miniProgram.ResultVo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 小程序专用controller
 */
@RestController
@RequestMapping("/mini")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class MiniProgramController {

    @Autowired
    private SupervisionEnterpriseService supervisionEnterpriseService;
    @Autowired
    private SysAreaService sysAreaService;
    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private UserSessionService userSessionService;
    @Autowired
    private VideoParentService videoParentService;
    @Autowired
    private FormatDisinfectionService formatDisinfectionService;
    // 用户登录（成功之后传cookie，里面存的有用户信息，可以用SysUser接收）
    @PostMapping("/userLogin")
    public ResultVo userLogin(HttpServletResponse response, UserParam userParam) {
        return new ResultVo(userSessionService.miniUserLogin(response,userParam));
    }

    // 登录之后获取企业名称和店面照片
    @GetMapping("/getPhotoAndName")
    public ResultVo getPhotoAndName(int enterpriseId){
        EnterpriseParam enterpriseParam = supervisionEnterpriseService.getById(enterpriseId);
        String enterpriseName = enterpriseParam.getEnterpriseName();  // 企业名称
        // 东营的和其他地区可能不一样，其他地区是附件
        String enterpriseIcon = JSON2ImageUrl(enterpriseParam.getPropagandaEnclosure()); //企业门头照片
        Map<String,Object> storefront = new HashMap<>();
        storefront.put("enterpriseName",enterpriseName);
        storefront.put("enterpriseIcon",enterpriseIcon);
        Map<String,Object> result = new HashMap<>();
        result.put("storefront",storefront);
        return new ResultVo(result);
    }

    // 获取企业相关信息（基本信息/监管信息）
    @RequestMapping("/getEPInfoById")
    public ResultVo getEPInfoById(int enterpriseId){
        SupervisionEnterprise supervisionEnterprise = supervisionEnterpriseService.selectById(enterpriseId);

        List<Object> companyInfo = new ArrayList<Object>(); //返回信息
        Map<String,Object> basicInfo = new LinkedHashMap<>();   // 企业基本信息
        Map<String,Object> supervisionInfo = new LinkedHashMap<>(); // 企业监管信息（监管人员检验）
        basicInfo.put( "categoryId",1 );
        basicInfo.put( "categoryName","企业基本信息" );
        supervisionInfo.put( "categoryId",2 );
        supervisionInfo.put( "categoryName","企业监管信息（监管人员检验）" );


        Map<String,Object> tempInfo = new LinkedHashMap<>();
        tempInfo.put("主体名称",supervisionEnterprise.getEnterpriseName());
        tempInfo.put("店招名称",supervisionEnterprise.getShopName());
        tempInfo.put("主体分类",supervisionEnterprise.getOperationMode());
        tempInfo.put("社会统一信用代码",supervisionEnterprise.getIdNumber());
        tempInfo.put("注册资本",supervisionEnterprise.getFixedAssets());
        tempInfo.put("注册地址",supervisionEnterprise.getRegisteredAddress());
        //住所/经营场所 web端用getRegisteredAddress()而不是getBusinessAddress()
        tempInfo.put("经营场所",supervisionEnterprise.getRegisteredAddress());
        tempInfo.put("法定代表人",supervisionEnterprise.getLegalPerson());
        tempInfo.put("证件号码",supervisionEnterprise.getIpIdNumber());
        tempInfo.put("负责人/联系人",supervisionEnterprise.getCantacts());
        tempInfo.put("联系电话",supervisionEnterprise.getCantactWay());
        tempInfo.put("营业期限自",supervisionEnterprise.getBusinessTermStart());
        tempInfo.put("营业期限至",supervisionEnterprise.getBusinessTermEnd());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String givenDate = simpleDateFormat.format(supervisionEnterprise.getGivenDate());
        tempInfo.put("发证日期",givenDate);
        tempInfo.put("登记机关",supervisionEnterprise.getGivenGov());
        tempInfo.put("经营范围",supervisionEnterprise.getBusinessScale());


        Boolean[] arrayBoolean1 = new Boolean[tempInfo.size()];
        Arrays.fill(arrayBoolean1, Boolean.FALSE);
        arrayBoolean1[0] = true;
        arrayBoolean1[2] = true;
        arrayBoolean1[3] = true;
        arrayBoolean1[5] = true;
        arrayBoolean1[6] = true;


        int count = 0;
        List<Object> categoryInfo1 = new ArrayList<Object>();
        for (Map.Entry<String, Object> entry : tempInfo.entrySet()) {
            Map<String,Object> categoryInfoItem = new LinkedHashMap<>();
            categoryInfoItem.put("itemId",count+1);
            categoryInfoItem.put("itemTitle",entry.getKey());
            categoryInfoItem.put("isMust",arrayBoolean1[count]);
            categoryInfoItem.put("itemShow",entry.getValue());
            categoryInfo1.add(categoryInfoItem);
            count++;

        }
        basicInfo.put( "categoryInfo",categoryInfo1);


        tempInfo.clear();
        tempInfo.put("所属地区",sysAreaService.getAreaById(supervisionEnterprise.getArea()).getName());
        tempInfo.put("监管机构",sysDeptService.getById(supervisionEnterprise.getRegulators()).getName());
        tempInfo.put("监管人",supervisionEnterprise.getSupervisor());
        tempInfo.put("网格","");
        for (SysArea sysArea: sysAreaService.getGridByArea(supervisionEnterprise.getArea())) {
            if (sysArea.getId()==supervisionEnterprise.getGrid()) {
                tempInfo.put("网格",sysArea.getName());
            }
        }
        tempInfo.put("网格人员",supervisionEnterprise.getGridPerson());
        tempInfo.put("信用等级",supervisionEnterprise.getIntegrityLevel());
        tempInfo.put("量化情况",supervisionEnterprise.getTransformationType());

        Boolean[] arrayBoolean2 = new Boolean[tempInfo.size()];
        Arrays.fill(arrayBoolean2, Boolean.FALSE);
        arrayBoolean2[0] = true;

        count = 0;
        List<Object> categoryInfo2 = new ArrayList<Object>();
        for (Map.Entry<String, Object> entry : tempInfo.entrySet()) {
            Map<String,Object> categoryInfoItem = new LinkedHashMap<>();
            categoryInfoItem.put("itemId",count+1);
            categoryInfoItem.put("itemTitle",entry.getKey());
            categoryInfoItem.put("isMust",arrayBoolean2[count]);
            categoryInfoItem.put("itemShow",entry.getValue());
            categoryInfo2.add(categoryInfoItem);
            count++;
        }
        supervisionInfo.put( "categoryInfo",categoryInfo2);

        companyInfo.add(basicInfo);
        companyInfo.add(supervisionInfo);

        Map<String, Object> result = new HashMap<>();
        result.put("companyInfo",companyInfo);
        return new ResultVo(result);

    }

    // 获取企业许可信息（只有食品经营）
    @RequestMapping("/getBusinessLicenseInfo")
    public ResultVo getBusinessLicenseInfo(int enterpriseId){
        Map<String,Object> result = new HashMap<>();
        result.put("foodBusinessLicenseList",supervisionEnterpriseService.getFoodBusinessLicenseById(enterpriseId));
        return new ResultVo(result);
    }

    // 获取人员健康证信息
    @RequestMapping("/getHealthInfo")
    public ResultVo getHealthInfo(SysUser sysUser){
        Map<String,Object> result = new HashMap<>();
//        result.put("storefront",storefront);
        return new ResultVo(result);
    }

    // 获取名厨亮灶页面内容
    @RequestMapping("/getBrightKitchenById")
    @ResponseBody
    public ResultVo getBrightKitchenById(int enterpriseId){
        SupervisionEnterprise supervisionEnterprise = supervisionEnterpriseService.selectById(enterpriseId);
        Map<String, Object> result = new HashMap<>();

        result.put("enterpriseIcon",JSON2ImageUrl(supervisionEnterprise.getPropagandaEnclosure())); // 门头照片
        result.put("shopName",supervisionEnterprise.getShopName());                   // 店招名称
        result.put("enterpriseName",supervisionEnterprise.getEnterpriseName());             // 企业名称
        result.put("contactWay",supervisionEnterprise.getCantactWay());                   // 联系方式
        result.put("businessAddress",supervisionEnterprise.getRegisteredAddress());     // 住所/经营场所
        result.put("videoList",videoParentService.getVideoListById(enterpriseId));  // 视频流和按钮
        return new ResultVo(result);
    }

    // 获取证照/公示的照片
    @RequestMapping("/getLicensePhotos")
    public ResultVo getLicensePhotos(int enterpriseId){
        Map<String,Object> result = new HashMap<>();
        Map<String,Object> data = supervisionEnterpriseService.getLicensePhotosById(enterpriseId);
        result.put("businessLicensePhoto",JSON2ImageUrl(data.get("businessLicensePhoto")));
        result.put("foodBusinessPhoto",JSON2ImageUrl(data.get("foodBusinessPhoto")));
        return new ResultVo(result);
    }
    // 上传照片
    @RequestMapping("/upload/picture")
    public ResultVo uploadPicture(@RequestParam("file") MultipartFile file) throws IOException {
        Map<String,Object> result = new HashMap<>();
        String pictureUrl = "http://123.234.130.3:8080/upload/picture/"+uploadFile(file,"picture");
        result.put("pictureUrl",pictureUrl);
        return new ResultVo(result);
    }

    // 保存证照更改
    @RequestMapping("/save/licensePhotos")
    public ResultVo updateLicensePhotosById(int enterpriseId,String businessLicensePhoto,String foodBusinessPhoto){
        Map<String,Object> result = new HashMap<>();
        Map<String,Object> data = supervisionEnterpriseService.updateLicensePhotosById(enterpriseId,businessLicensePhoto,foodBusinessPhoto);
        result.put("businessLicensePhoto",JSON2ImageUrl(data.get("businessLicensePhoto")));
        result.put("foodBusinessPhoto",JSON2ImageUrl(data.get("foodBusinessPhoto")));
        return new ResultVo(result);
    }

    // 查询消毒记录
    @RequestMapping("/getDisinfectionRecord")
    public ResultVo getDisinfectionRecord(int enterpriseId, String date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = null;
        try {
            startDate  = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Map<String,Object> result = new HashMap<>();
        result.put("disinfectRecord","");
        result.put("disinfectRecord",formatDisinfectionService.getDisinfectionRecord(enterpriseId,startDate));
        return new ResultVo(result);
    }




    /**
     *  上传文件
     * @param file
     * @param type
     * @return  fileName
     * @throws IOException
     */
    public String uploadFile(MultipartFile file,String type) throws IOException{
        Date nowTime = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
        String currentTime = dateFormat.format( nowTime );

        String fileName=file.getOriginalFilename();
        System.out.println("源文件名："+fileName);
        File filed=new File("upload/"+type+"/"+currentTime);
        if(!filed.exists()){
            filed.mkdirs();
        }

        String resFileName = System.currentTimeMillis()+(int)(1+Math.random()*1000)+fileName.substring(fileName.lastIndexOf("."));
        file.transferTo(new File(filed.getAbsolutePath(),resFileName));
        System.out.println(currentTime+"/"+resFileName);
        return currentTime+"/"+resFileName;
    }



    /**
     * 将上传的照片JSON格式转换为String图片地址
     * @param jsonObj
     * @return imgUrl
     */
    public String JSON2ImageUrl(Object jsonObj) {
        JSONArray jsonArray = JSONArray.fromObject(jsonObj);
        JSONObject jsonObject1 = JSONObject.fromObject(jsonArray.get(0));
        JSONObject jsonObject2 = JSONObject.fromObject(jsonObject1.get("response"));
        String host = "http://123.234.130.3:8080/upload/picture/"; // 图片存储地址记得上传的时候更改IP
        String imgUrl = host+ jsonObject2.get("data");
        return imgUrl;
    }

}
