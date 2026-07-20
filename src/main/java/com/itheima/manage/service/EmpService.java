package com.itheima.manage.service;

import com.itheima.manage.pojo.Emp;
import com.itheima.manage.pojo.EmpQueryParam;
import com.itheima.manage.pojo.PageResult;
import com.itheima.manage.pojo.Result;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

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
    /*
     * 修改员工
     */
    void update(Emp emp);
    /*
     * 删除员工
     */
    void delete(List<Integer> ids);
    //根据id查询员工信息
    Emp findById(Integer id);
//查询所有员工信息
    List<Emp> findall();
    Result login(String username, String password);
}
