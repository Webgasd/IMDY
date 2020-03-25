package com.example.upc.dao;

import com.example.upc.controller.param.GridPoints1;
import com.example.upc.controller.param.SmilePoints;
import com.example.upc.controller.param.enterpriseId;
import com.example.upc.controller.searchParam.EnterpriseSearchParam;
import com.example.upc.dataobject.GridPoints;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GridPointsMapper {
    int deleteByPrimaryKey(Integer id);
    int insert(GridPoints record);
    int insertSelective(GridPoints record);
    GridPoints selectByPrimaryKey(Integer id);
    int updateByPrimaryKeySelective(GridPoints record);
    int updateByPrimaryKey(GridPoints record);

    List<GridPoints> getAll();
    List<GridPoints1> getAll1();
    List<GridPoints> getByAreaId(@Param("id") Integer id);
    int checkPoint(@Param("id") Integer id);
    int updatePoint(GridPoints record);
    List<SmilePoints> getSmileAll(@Param("search") EnterpriseSearchParam search);

    List<enterpriseId> getEnterpriseByName(@Param("name") String name);
    int deleteByEnterpriseId(@Param("id") int id);
    GridPoints getPointByEnterpriseId(@Param("id") Integer id);
    int getVideoIdByEnterprise(@Param("id")int id);
}