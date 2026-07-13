package com.itheima.manage.controller;

import com.itheima.manage.mapper.EmpMapper;
import com.itheima.manage.pojo.Emp;
import com.itheima.manage.pojo.EmpQueryParam;
import com.itheima.manage.pojo.PageResult;
import com.itheima.manage.pojo.Result;
import com.itheima.manage.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping("/emps")

public class EmpConrtoller {
    @Autowired
    private EmpService empService;
    //分页查询
    @GetMapping()
    public Result page(EmpQueryParam empQueryParam)
    {
        log.info("{}",empQueryParam);
        PageResult<Emp> pageResult = empService. page(empQueryParam);
        return Result.success(pageResult);
    }
    //新增员工
    @PostMapping()
    public Result save(@RequestBody Emp emp)
        {
            log.info("{}",emp);
            empService.save(emp);
            return Result.success();
        }



}
