package com.itheima.manage.service;

import com.itheima.manage.pojo.Emp;
import com.itheima.manage.pojo.EmpQueryParam;
import com.itheima.manage.pojo.PageResult;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public interface EmpService {
    /*
     * 分页查询
     * @param page
     * @param pageSize
     * @return
     */
    PageResult<Emp> page(EmpQueryParam empQueryParam);
    /*void Emp;
    * 新增员工
     */
    void save(Emp emp);
}
