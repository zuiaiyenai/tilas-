package com.itheima.manage.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
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
        PageHelper.startPage(empQueryParam.getPage(), empQueryParam.getPageSize());
        List<Emp> rows = empMapper.list(empQueryParam);
        Page<Emp> empPage = (Page<Emp>) rows;
        return new PageResult<Emp>(empPage.getTotal(), empPage.getResult());
    }

    @Transactional
    @Override
    public void save(Emp emp){
        try {
            emp.setCreateTime(LocalDateTime.now());
            emp.setUpdateTime(LocalDateTime.now());
            empMapper.insert(emp);
            //2.保存员工工作经历信息
            List<EmpExpr> empExprList = emp.getEmpExprList();
            if (!CollectionUtils.isEmpty(empExprList)) {
                empExprList.forEach(empExpr -> {
                    empExpr.setEmpId(emp.getId());
                });
                empExprMapper.insertBatch(empExprList);
            }
        }finally {
            EmpLog empLog = new EmpLog(null, LocalDateTime.now(), "新增员工信息");
            empLogService.insertLog(empLog);
        }

    }
}
