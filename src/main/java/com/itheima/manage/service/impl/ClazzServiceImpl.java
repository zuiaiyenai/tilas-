package com.itheima.manage.service.impl;

import com.itheima.manage.mapper.ClazzMapper;
import com.itheima.manage.pojo.Clazz;
import com.itheima.manage.pojo.ClazzQueryParam;
import com.itheima.manage.pojo.PageResult;
import com.itheima.manage.service.ClazzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClazzServiceImpl implements ClazzService {
    @Autowired
    private ClazzMapper clazzMapper;
//分页查询班级列表数据
    @Override
    public PageResult<Clazz> page(ClazzQueryParam queryParam) {
        Integer page = queryParam.getPage() == null || queryParam.getPage() < 1 ? 1 : queryParam.getPage();
        Integer pageSize = queryParam.getPageSize() == null || queryParam.getPageSize() < 1 ? 10 : queryParam.getPageSize();
        queryParam.setPage(page);
        queryParam.setPageSize(pageSize);
        queryParam.setOffset((page - 1) * pageSize);
        List<Clazz> rows = clazzMapper.listPage(queryParam);
        Long total = clazzMapper.count(queryParam);
        return new PageResult<>(total, rows);
    }
    //删除班级
    @Override
    public void delete(Integer id) {
        clazzMapper.delete(id);
    }
    //增加班级
    @Override
    public void add(Clazz clazz) {
        clazzMapper.add(clazz);
    }
    //根据id查询班级信息
    @Override
    public Clazz findById(Integer id) {
        return clazzMapper.findById(id);
    }
    //修改班级信息
    @Override
    public void update(Clazz clazz) {
        clazzMapper.update(clazz);
    }
    //查询所有班级信息
    @Override
    public List<Clazz> findAll() {
        return clazzMapper.findAll();
    }
}
