package com.itheima.manage.controller;

import com.itheima.manage.pojo.Dept;
import com.itheima.manage.pojo.Result;
import com.itheima.manage.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
@Slf4j
@RestController
public class DeptController {
    @Autowired
    private DeptService deptService;
    @GetMapping("/depts")
    //查询所有部门
    public Result list() {
      log.info("查询部门数据");
        List<Dept> deptList = deptService.findAll();
        return Result.success(deptList);
    }
        //删除@RequestParam
        @DeleteMapping("/depts")
        public Result delete(Integer id) {
          log.info("删除部门数据:{}", id);
            deptService.delete(id);
            return Result.success();
        }
        //增加部门
        @PostMapping("/depts")
        public Result add(@RequestBody Dept dept) {
         log.info("增加部门数据:{}",dept);
            deptService.add(dept);
            return Result.success();
        }
            //查询部门信息
    @GetMapping("/depts/{id}")
    public Result findById(@PathVariable Integer id) {
       log.info("查询部门数据:{}" , id);
        Dept dept = deptService.findById(id);
        return Result.success(dept);
    }
    //修改部门信息

    @PutMapping("/depts")
    public Result update(@RequestBody Dept dept) {
       log.info("修改部门数据:{}",dept);
        deptService.update(dept);
        return Result.success();
    }
}






