package com.itheima.manage.mapper;

import com.itheima.manage.pojo.Clazz;
import com.itheima.manage.pojo.ClazzQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ClazzMapper {
    List<Clazz> listPage(ClazzQueryParam queryParam);
    Long count(ClazzQueryParam queryParam);
//删除班级
    void delete(Integer id);
//增加班级
    void add(Clazz clazz);
//根据id查询班级信息
    Clazz findById(Integer id);
//修改班级信息
    void update(Clazz clazz);
//查询所有班级信息
    List<Clazz> findAll();
}
