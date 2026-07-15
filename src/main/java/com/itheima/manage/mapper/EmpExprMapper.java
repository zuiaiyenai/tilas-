package com.itheima.manage.mapper;

import com.itheima.manage.pojo.EmpExpr;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper

public interface EmpExprMapper {
    //保存员工经历信息
    void insertBatch(@Param("empExprList") List<EmpExpr> empExprList);

    void deleteByEmpIds(@Param("empIds") List<Integer> empIds);
}
