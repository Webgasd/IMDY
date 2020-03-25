package com.example.upc.controller;

import com.alibaba.fastjson.JSON;
import com.example.upc.common.CommonReturnType;
import com.example.upc.controller.param.PageQuery;
import com.example.upc.dataobject.SysNotice;
import com.example.upc.service.SysNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author zcc
 * @date 2019/3/28 13:19
 */
@Controller
@RequestMapping("/sys/notice")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class SysNoticeController {
    @Autowired
    private SysNoticeService sysNoticeService;

    @RequestMapping("/getPage")
    @ResponseBody
    public CommonReturnType getPage(PageQuery pageQuery){
        return CommonReturnType.create(sysNoticeService.getPage(pageQuery));
    }

    @RequestMapping("/getById")
    @ResponseBody
    public CommonReturnType getById(@RequestParam("id")int id){
        return CommonReturnType.create(sysNoticeService.getById(id));
    }

    @RequestMapping("/insert")
    @ResponseBody
    public CommonReturnType insert(@RequestBody String json){
        SysNotice sysNotice = JSON.parseObject(json,SysNotice.class);
        sysNoticeService.insert(sysNotice);
        return CommonReturnType.create(null);
    }

    @RequestMapping("/update")
    @ResponseBody
    public CommonReturnType update(@RequestBody String json){
        SysNotice sysNotice = JSON.parseObject(json,SysNotice.class);
        sysNoticeService.update(sysNotice);
        return CommonReturnType.create(null);
    }

    @RequestMapping("/delete")
    @ResponseBody
    public CommonReturnType delete(@RequestParam("id")int id){
        sysNoticeService.delete(id);
        return CommonReturnType.create(null);
    }

    @RequestMapping("/check")
    @ResponseBody
    public CommonReturnType check(@RequestParam("id")int id){
        sysNoticeService.check(id);
        return CommonReturnType.create(null);
    }
}
