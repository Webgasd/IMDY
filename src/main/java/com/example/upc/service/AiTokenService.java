package com.example.upc.service;

import com.alibaba.fastjson.JSONObject;

/**
 * @author 董志涵
 */
public interface AiTokenService {
    void getNewToken();
    JSONObject faceContrast(String personFace1, String personFace2);
}
