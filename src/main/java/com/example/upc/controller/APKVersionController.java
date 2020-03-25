package com.example.upc.controller;

import com.example.upc.common.CommonReturnType;
import com.example.upc.dataobject.APKVersion;
import com.example.upc.service.APKVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/APKVersion")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class APKVersionController {
    @Autowired
    private APKVersionService apkVersionService;

    @RequestMapping("/selectTopOne")
    @ResponseBody
    public CommonReturnType selectTopOne(){
        return CommonReturnType.create(apkVersionService.selectTopOne());
    }

    @RequestMapping("/insert")
    @ResponseBody

    public CommonReturnType insert(APKVersion apkVersion){

        apkVersionService.insert(apkVersion);
        return CommonReturnType.create(null);
    }

}
