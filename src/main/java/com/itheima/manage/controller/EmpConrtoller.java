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
import java.util.List;
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
//修改员工
    @PutMapping()
    public Result update(@RequestBody Emp emp) {
        log.info("{}",emp);
        empService.update(emp);
        return Result.success();
    }
//删除员工
    @DeleteMapping()
    public Result delete(@RequestParam List<Integer> ids) {
        log.info("{}",ids);
        empService.delete(ids);
        return Result.success();
    }
//根据id查询员工信息
    @GetMapping("/{id}")
    public Result findById(@PathVariable Integer id) {
        log.info("查询员工数据:{}" , id);
        Emp emp = empService.findById(id);
        return Result.success(emp);
    }
    //查询所有员工
    @GetMapping("list")
    public Result list() {
        log.info("查询所有员工数据");
        List<Emp> empList = empService.findall();
        PageResult<Emp> pageResult = new PageResult<>(Long.valueOf(empList.size()), empList);
        return Result.success(pageResult);
    }


}
