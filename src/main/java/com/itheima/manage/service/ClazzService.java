package com.itheima.manage.service;

import com.itheima.manage.pojo.Clazz;
import com.itheima.manage.pojo.ClazzQueryParam;
import com.itheima.manage.pojo.PageResult;

import java.util.List;

public interface ClazzService {
    /*
     * 分页查询
     * @param page
     * @param pageSize
     * @return
     */
    PageResult<Clazz> page(ClazzQueryParam queryParam);
//删除班级
    void delete(Integer id);
//添加班级
    void add(Clazz clazz);
//根据id查询班级
    Clazz findById(Integer id);
//修改班级
    void update(Clazz clazz);
//查询所有班级
    List<Clazz> findAll();
}
