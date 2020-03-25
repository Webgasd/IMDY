package com.example.upc.dao;

import com.example.upc.dataobject.SupervisionEnDrugsBu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SupervisionEnDrugsBuMapper {
    int deleteByPrimaryKey(Integer id);
    int insert(SupervisionEnDrugsBu record);
    int insertSelective(SupervisionEnDrugsBu record);
    SupervisionEnDrugsBu selectByPrimaryKey(Integer id);
    int updateByPrimaryKeySelective(SupervisionEnDrugsBu record);
    int updateByPrimaryKey(SupervisionEnDrugsBu record);

    SupervisionEnDrugsBu selectByEnterpriseId(@Param("id") int id);
    void deleteByEnterpriseId(@Param("enterpriseId") int enterpriseId);
    void batchInsert(@Param("drugsBuList") List<SupervisionEnDrugsBu> drugsBuList);
    void deleteByEnterpriseIds(@Param("enterpriseIds")List<Integer> enterpriseIds);
}