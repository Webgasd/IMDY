package com.example.upc.service.impl;

import com.example.upc.controller.param.MonthlySelfCheckOptCategoryParam;
import com.example.upc.controller.param.MonthlySelfCheckParam;
import com.example.upc.dao.MonthlyAdditionalAnswerMapper;
import com.example.upc.dao.MonthlySelfCheckMapper;
import com.example.upc.dao.MonthlySelfcheckOptCategoryMapper;
import com.example.upc.dao.MonthlySelfcheckOptMapper;
import com.example.upc.dataobject.MonthlyAdditionalAnswer;
import com.example.upc.dataobject.MonthlySelfCheck;
import com.example.upc.dataobject.SysUser;
import com.example.upc.service.MonthlySelfcheckOptCategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class MonthlySelfcheckOptCategoryServiceImpl implements MonthlySelfcheckOptCategoryService {
    @Autowired
    MonthlySelfcheckOptCategoryMapper monthlySelfcheckOptCategoryMapper;
    @Autowired
    MonthlyAdditionalAnswerMapper monthlyAdditionalAnswerMapper;
    @Autowired
    MonthlySelfCheckMapper monthlySelfCheckMapper;

    @Override
    public MonthlySelfCheckParam selectAllOpt(MonthlySelfCheck monthlySelfCheck, SysUser sysUser){
        //返回值
        MonthlySelfCheckParam monthlySelfCheckParam = new MonthlySelfCheckParam();
        monthlySelfCheckParam.setMonthlySelfCheckOptCategoryParamList(monthlySelfcheckOptCategoryMapper.selectAllOpt(monthlySelfCheck));
        //获取当前时间
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //当前时间前去一个月，即一个月前的时间
        calendar.add(Calendar.MONTH, -1);
        date = calendar.getTime();
        if(monthlySelfCheck.getId()==null||monthlySelfCheck.getId()==0) {
           if(monthlySelfCheckMapper.selectByMonth(date,sysUser.getInfoId())!=null)
           {
              int selfCheckId = monthlySelfCheckMapper.selectByMonth(date,sysUser.getInfoId()).getId();
              if(monthlyAdditionalAnswerMapper.selectBySelfCheckId(selfCheckId)!=null) {
                  monthlySelfCheckParam.setLastRecifySituation(monthlyAdditionalAnswerMapper.selectBySelfCheckId(selfCheckId).getLastRecifySituation());
              }
           }
        }
        else{
            if(monthlyAdditionalAnswerMapper.selectBySelfCheckId(monthlySelfCheck.getId())!=null) {
                BeanUtils.copyProperties(monthlyAdditionalAnswerMapper.selectBySelfCheckId(monthlySelfCheck.getId()), monthlySelfCheckParam);
            }
        }
        return monthlySelfCheckParam;
    }
}
