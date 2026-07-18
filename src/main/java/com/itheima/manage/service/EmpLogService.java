package com.itheima.manage.service;
import com.itheima.manage.pojo.EmpLog;
import com.itheima.manage.pojo.EmpLogQueryParam;
import com.itheima.manage.pojo.PageResult;

public interface EmpLogService {

    public void insertLog(EmpLog empLog);

    PageResult<EmpLog> page(EmpLogQueryParam empLogQueryParam);

}
