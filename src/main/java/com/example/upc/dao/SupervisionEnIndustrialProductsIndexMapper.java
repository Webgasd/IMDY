package com.example.upc.dao;

import com.example.upc.dataobject.SupervisionEnIndustrialProductsIndex;
import com.example.upc.dataobject.SupervisionEnMedicalBuIndex;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SupervisionEnIndustrialProductsIndexMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table supervision_en_industrial_products_index
     *
     * @mbg.generated Fri Apr 24 17:34:42 CST 2020
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table supervision_en_industrial_products_index
     *
     * @mbg.generated Fri Apr 24 17:34:42 CST 2020
     */
    int insert(SupervisionEnIndustrialProductsIndex record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table supervision_en_industrial_products_index
     *
     * @mbg.generated Fri Apr 24 17:34:42 CST 2020
     */
    int insertSelective(SupervisionEnIndustrialProductsIndex record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table supervision_en_industrial_products_index
     *
     * @mbg.generated Fri Apr 24 17:34:42 CST 2020
     */
    SupervisionEnIndustrialProductsIndex selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table supervision_en_industrial_products_index
     *
     * @mbg.generated Fri Apr 24 17:34:42 CST 2020
     */
    int updateByPrimaryKeySelective(SupervisionEnIndustrialProductsIndex record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table supervision_en_industrial_products_index
     *
     * @mbg.generated Fri Apr 24 17:34:42 CST 2020
     */
    int updateByPrimaryKey(SupervisionEnIndustrialProductsIndex record);

    SupervisionEnIndustrialProductsIndex selectByEnterpriseId(@Param("eid") Integer eid);

    List<SupervisionEnIndustrialProductsIndex> getAll();
}