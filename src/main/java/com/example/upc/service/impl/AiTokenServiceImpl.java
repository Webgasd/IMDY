package com.example.upc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.upc.dao.AiTokenMapper;
import com.example.upc.dataobject.AiToken;
import com.example.upc.service.AiTokenService;
import com.example.upc.util.FaceContrast.AiApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author 董志涵
 */

@Service
public class AiTokenServiceImpl implements AiTokenService {
    @Autowired
    private AiTokenMapper aiTokenMapper;


    @Override
    public void getNewToken(){
        List<AiToken> aiTokenList = aiTokenMapper.selectAllToken();
        for (int i = 0; i < aiTokenList.size(); i++) {
            AiToken aiToken = AiApi.getNewToken(aiTokenList.get(i));
            System.out.println(aiToken.getAccessToken());
            aiTokenMapper.updateByPrimaryKeySelective(aiToken);
        }
    }

    @Override
    public JSONObject faceContrast(String personFace1,String personFace2){
        String accessToken = new String();
        accessToken = aiTokenMapper.selectByPrimaryKey(1).getAccessToken();
        return AiApi.faceContrast(personFace1,personFace2,accessToken);
    }
}
