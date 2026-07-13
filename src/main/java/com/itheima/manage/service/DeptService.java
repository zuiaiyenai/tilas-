package com.itheima.manage.service;

import com.itheima.manage.pojo.Dept;

import java.util.List;

public interface DeptService {
    List<Dept> findAll();
    void delete(Integer id);
    void add(Dept dept);
    Dept findById(Integer id);
    void update(Dept dept);
}
