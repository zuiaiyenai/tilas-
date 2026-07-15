
package com.itheima.manage.service.impl;

import com.itheima.manage.mapper.EmpExprMapper;
import com.itheima.manage.mapper.EmpMapper;
import com.itheima.manage.pojo.*;
import com.itheima.manage.service.EmpLogService;
import com.itheima.manage.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class EmpServiceImpl implements EmpService {
    @Autowired
    private EmpMapper empMapper;
    @Autowired
    private EmpExprMapper empExprMapper;
    @Autowired
    private EmpLogService empLogService;

    @Override
    public PageResult<Emp> page(EmpQueryParam empQueryParam) {
        int page = empQueryParam.getPage() == null || empQueryParam.getPage() < 1 ? 1 : empQueryParam.getPage();
        int pageSize = empQueryParam.getPageSize() == null || empQueryParam.getPageSize() < 1 ? 10 : empQueryParam.getPageSize();
        empQueryParam.setPage(page);
        empQueryParam.setPageSize(pageSize);
        empQueryParam.setOffset((page - 1) * pageSize);
        List<Emp> rows = empMapper.listPage(empQueryParam);
        Long total = empMapper.count(empQueryParam);
        return new PageResult<>(total, rows);
    }
//新增员工
    @Transactional
    @Override
    public void save(Emp emp){
        emp.setCreateTime(LocalDateTime.now());
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.insert(emp);

        List<EmpExpr> empExprList = emp.getEmpExprList();
        if (!CollectionUtils.isEmpty(empExprList)) {
            empExprList.forEach(empExpr -> {
                empExpr.setEmpId(emp.getId());
            });
            empExprMapper.insertBatch(empExprList);
        }

        EmpLog empLog = new EmpLog(null, LocalDateTime.now(), "新增员工信息");
        empLogService.insertLog(empLog);
    }
//修改员工
    @Override
    public void update(Emp emp) {
        //1.修改员工信息
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.update(emp);
        //2.修改员工经历信息
        //2.1 删除员工经历信息‘
        empExprMapper.deleteByEmpIds(Arrays.asList(emp.getId()));
        //2.2 新增员工经历信息
        List<EmpExpr> empExprList = emp.getEmpExprList();
        if (!CollectionUtils.isEmpty(empExprList)) {
            empExprList.forEach(empExpr -> {
                empExpr.setEmpId(emp.getId());
            });
            empExprMapper.insertBatch(empExprList);
        }
    }
//删除员工
    @Transactional
    @Override
    public void delete(List<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        empExprMapper.deleteByEmpIds(ids);
        empMapper.deleteByIds(ids);
    }
    //根据id查询员工信息
@Override
    public Emp findById(Integer id) {
        return empMapper.findById(id);
    }
    //查询所有员工信息
    @Override
    public List<Emp> findall() {
        return empMapper.findall();
    }
}