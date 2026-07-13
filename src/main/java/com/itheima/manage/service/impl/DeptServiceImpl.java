package com.itheima.manage.service.impl;

import com.itheima.manage.mapper.DeptMapper;
import com.itheima.manage.pojo.Dept;
import com.itheima.manage.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeptServiceImpl implements DeptService {
    @Autowired
    private DeptMapper deptMapper;
    //查询所有部门数据
    @Override
    public List<Dept> findAll() {
        return deptMapper.findAll();

    }
    //删除
     @Override
     public void delete(Integer id) {
        deptMapper.deleteById(id);
    }
//增加部门数据
    @Override
    public void add(Dept dept) {
        dept.setCreateTime(LocalDateTime.now());
        dept.setUpdateTime(LocalDateTime.now());
        deptMapper.add(dept);
    }
    //查询单个部门信息
    @Override
    public Dept findById(Integer id) {
        return deptMapper.findById(id);
         }
         @Override
    public void update(Dept dept) {
        dept.setUpdateTime(LocalDateTime.now());
        deptMapper.update(dept);
    }
    }


