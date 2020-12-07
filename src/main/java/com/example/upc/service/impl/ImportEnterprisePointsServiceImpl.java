package com.example.upc.service.impl;

import com.example.upc.controller.param.ImportPoints;
import com.example.upc.dao.ImportEnterprisePointsMapper;
import com.example.upc.service.ImportEnterprisePointsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ImportEnterprisePointsServiceImpl implements ImportEnterprisePointsService {
    @Autowired
    ImportEnterprisePointsMapper importEnterprisePointsMapper;

    @Override
    public List<ImportPoints> getAll(){
        return importEnterprisePointsMapper.getAll();
    }
}
