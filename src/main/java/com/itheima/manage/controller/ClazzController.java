package com.itheima.manage.controller;

import com.itheima.manage.pojo.Clazz;
import com.itheima.manage.pojo.ClazzQueryParam;
import com.itheima.manage.pojo.PageResult;
import com.itheima.manage.pojo.Result;
import com.itheima.manage.service.ClazzService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/clazzs")
public class ClazzController {
    @Autowired
    private ClazzService clazzService;
//分页查询班级列表
    @GetMapping
    public Result page(ClazzQueryParam queryParam) {
        log.info("查询班级列表数据");
        PageResult<Clazz> pageResult = clazzService.page(queryParam);
        return Result.success(pageResult);
    }
    //删除班级
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        log.info("删除班级数据:{}", id);
        clazzService.delete(id);
        return Result.success();
    }
    //增加班级
    @PostMapping()
    public Result add(@RequestBody Clazz clazz) {
        log.info("增加班级数据:{}", clazz);
        clazzService.add(clazz);
        return Result.success();
    }
    //根据id查询班级信息
    @GetMapping("/{id}")
    public Result findById(@PathVariable Integer id) {
        log.info("查询班级数据:{}" , id);
        Clazz clazz = clazzService.findById(id);
        return Result.success(clazz);
    }
    //修改班级信息
    @PutMapping()
    public Result update(@RequestBody Clazz clazz) {
        log.info("修改班级数据:{}", clazz);
        clazzService.update(clazz);
        return Result.success();
    }
    //查询所有班级信息
    @GetMapping("list")
    public Result list() {
        log.info("查询所有班级数据");
        List<Clazz> clazzList = clazzService.findAll();
        PageResult<Clazz> pageResult = new PageResult<>(Long.valueOf(clazzList.size()), clazzList);
        return Result.success(pageResult);
    }

}
