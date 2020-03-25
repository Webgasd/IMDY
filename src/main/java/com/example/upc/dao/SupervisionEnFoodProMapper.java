package com.example.upc.dao;

import com.example.upc.dataobject.SupervisionEnFoodPro;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SupervisionEnFoodProMapper {
    int deleteByPrimaryKey(Integer id);
    int insert(SupervisionEnFoodPro record);
    int insertSelective(SupervisionEnFoodPro record);
    SupervisionEnFoodPro selectByPrimaryKey(Integer id);
    int updateByPrimaryKeySelective(SupervisionEnFoodPro record);
    int updateByPrimaryKey(SupervisionEnFoodPro record);
    SupervisionEnFoodPro selectByEnterpriseId(@Param("id") int id);
    void deleteByEnterpriseId(@Param("enterpriseId") int enterpriseId);
    void batchInsert(@Param("foodProList") List<SupervisionEnFoodPro> foodProList);
    void deleteByEnterpriseIds(@Param("enterpriseIds")List<Integer> enterpriseIds);
}