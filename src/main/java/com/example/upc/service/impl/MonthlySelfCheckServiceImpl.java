package com.example.upc.service.impl;

import com.example.upc.common.BusinessException;
import com.example.upc.common.EmBusinessError;
import com.example.upc.common.ValidationResult;
import com.example.upc.common.ValidatorImpl;
import com.example.upc.controller.param.MonthlySelfCheckParam;
import com.example.upc.dao.MonthlyAdditionalAnswerMapper;
import com.example.upc.dao.MonthlySelfCheckMapper;
import com.example.upc.dao.MonthlySelfcheckOptAnswerMapper;
import com.example.upc.dataobject.MonthlyAdditionalAnswer;
import com.example.upc.dataobject.MonthlySelfCheck;
import com.example.upc.dataobject.SysUser;
import com.example.upc.service.MonthlySelfCheckService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Service
public class MonthlySelfCheckServiceImpl implements MonthlySelfCheckService {
    @Autowired
    MonthlySelfCheckMapper monthlySelfCheckMapper;
    @Autowired
    MonthlySelfcheckOptAnswerMapper monthlySelfcheckOptAnswerMapper;
    @Autowired
    MonthlyAdditionalAnswerMapper monthlyAdditionalAnswerMapper;
    @Autowired
    private ValidatorImpl validator;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertMonSelfcheck(MonthlySelfCheckParam monthlySelfCheckParam, SysUser sysUser) throws InvocationTargetException, IllegalAccessException {
        MonthlySelfCheck monthlySelfCheck = new MonthlySelfCheck();
        ValidationResult result = validator.validate(monthlySelfCheckParam);
        if (result.isHasErrors()) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, result.getErrMsg());
        }
        BeanUtils.copyProperties(monthlySelfCheckParam, monthlySelfCheck);
        monthlySelfCheck.setEnterpriseId(sysUser.getInfoId());
        //插入月度自查记录
        monthlySelfCheckMapper.insert(monthlySelfCheck);
        int selfCheckId = monthlySelfCheck.getId();
        //插入选择题答案
        monthlySelfCheckParam.getMonthlySelfCheckOptCategoryParamList().forEach(item ->
                monthlySelfcheckOptAnswerMapper.batchInsert(item.getOptList(), selfCheckId));
         // 插入附加题答案
        MonthlyAdditionalAnswer monthlyAdditionalAnswer = new MonthlyAdditionalAnswer();
        BeanUtils.copyProperties(monthlySelfCheckParam,monthlyAdditionalAnswer);
        monthlyAdditionalAnswer.setSelfCheckId(selfCheckId);
        monthlyAdditionalAnswerMapper.insert(monthlyAdditionalAnswer);
    }

    @Override
    public List<MonthlySelfCheck> selectByDate(MonthlySelfCheckParam monthlySelfCheckParam, SysUser sysUser) {
        monthlySelfCheckParam.setEnterpriseId(sysUser.getInfoId());
        return monthlySelfCheckMapper.selectByDate(monthlySelfCheckParam);
    }

    @Override
    public void deleteById(MonthlySelfCheckParam monthlySelfCheckParam) {
        if(monthlySelfCheckParam.getId()==null||monthlySelfCheckParam.getId()==0)
        {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"参数不能为空");
        }
        monthlySelfCheckMapper.deleteByPrimaryKey(monthlySelfCheckParam.getId());
        monthlySelfcheckOptAnswerMapper.deleteBySelfCheckId(monthlySelfCheckParam);
        monthlyAdditionalAnswerMapper.deleteBySelfCheckId(monthlySelfCheckParam);
    }
}
