package com.itheima.manage.service.impl;
import com.itheima.manage.mapper.EmpLogMapper;
import com.itheima.manage.pojo.EmpLog;
import com.itheima.manage.pojo.EmpLogQueryParam;
import com.itheima.manage.pojo.PageResult;
import com.itheima.manage.service.EmpLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpLogServiceImpl implements EmpLogService {

    @Autowired
    private EmpLogMapper empLogMapper;

    @Override
    public void insertLog(EmpLog empLog) {
        empLogMapper.insert(empLog);
    }

    @Override
    public PageResult<EmpLog> page(EmpLogQueryParam empLogQueryParam) {
        int page = empLogQueryParam.getPage() == null || empLogQueryParam.getPage() < 1 ? 1 : empLogQueryParam.getPage();
        int pageSize = empLogQueryParam.getPageSize() == null || empLogQueryParam.getPageSize() < 1 ? 10 : empLogQueryParam.getPageSize();
        empLogQueryParam.setPage(page);
        empLogQueryParam.setPageSize(pageSize);
        empLogQueryParam.setOffset((page - 1) * pageSize);

        List<EmpLog> rows = empLogMapper.listPage(empLogQueryParam);
        Long total = empLogMapper.count(empLogQueryParam);

        return new PageResult<>(total, rows);
    }
}
