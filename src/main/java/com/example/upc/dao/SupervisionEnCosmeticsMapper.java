package com.example.upc.dao;

import com.example.upc.dataobject.SupervisionEnCosmetics;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SupervisionEnCosmeticsMapper {
    int deleteByPrimaryKey(Integer id);
    int insert(SupervisionEnCosmetics record);
    int insertSelective(SupervisionEnCosmetics record);
    SupervisionEnCosmetics selectByPrimaryKey(Integer id);
    int updateByPrimaryKeySelective(SupervisionEnCosmetics record);
    int updateByPrimaryKey(SupervisionEnCosmetics record);

    SupervisionEnCosmetics selectByEnterpriseId(@Param("id") int id);
    void deleteByEnterpriseId(@Param("enterpriseId") int enterpriseId);
    void batchInsert(@Param("cosmeticsList") List<SupervisionEnCosmetics> cosmeticsList);
    void deleteByEnterpriseIds(@Param("enterpriseIds")List<Integer> enterpriseIds);
}