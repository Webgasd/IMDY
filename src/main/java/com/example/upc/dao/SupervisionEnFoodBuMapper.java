package com.example.upc.dao;

import com.example.upc.dataobject.SupervisionEnFoodBu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SupervisionEnFoodBuMapper {
    int deleteByPrimaryKey(Integer id);
    int insert(SupervisionEnFoodBu record);
    int insertSelective(SupervisionEnFoodBu record);
    SupervisionEnFoodBu selectByPrimaryKey(Integer id);
    int updateByPrimaryKeySelective(SupervisionEnFoodBu record);
    int updateByPrimaryKey(SupervisionEnFoodBu record);
    SupervisionEnFoodBu selectByEnterpriseId(@Param("id") int id);
    void deleteByEnterpriseId(@Param("enterpriseId") int enterpriseId);
    void batchInsert(@Param("foodBuList") List<SupervisionEnFoodBu> foodBuList);
    void deleteByEnterpriseIds(@Param("enterpriseIds")List<Integer> enterpriseIds);
}