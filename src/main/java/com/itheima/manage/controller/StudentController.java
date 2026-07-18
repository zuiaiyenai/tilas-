package com.itheima.manage.controller;

import com.itheima.manage.pojo.PageResult;
import com.itheima.manage.pojo.Result;
import com.itheima.manage.pojo.Student;
import com.itheima.manage.pojo.StudentQueryParam;
import com.itheima.manage.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private StudentService studentService;
//分页查询学生列表
    @GetMapping
    public Result list(StudentQueryParam queryParam) {
        log.info("查询学生列表数据");
        PageResult<Student> pageResult = studentService.page(queryParam);
        return Result.success(pageResult);
    }
//根据id查询学生信息
    @GetMapping("/{id}")
    public Result findById(@PathVariable Integer id) {
        log.info("查询学生数据:{}", id);
        Student student = studentService.findById(id);
        return Result.success(student);
    }
//增加学生
    @PostMapping
    public Result add(@RequestBody Student student) {
        log.info("增加学生数据:{}", student);
        studentService.add(student);
        return Result.success();
    }
//修改学生信息
    @PutMapping
    public Result update(@RequestBody Student student) {
        log.info("修改学生数据:{}", student);
        studentService.update(student);
        return Result.success();
    }
//批量删除学员信息
    @DeleteMapping("/{ids}")
    public Result delete(@RequestParam(required = false) List<Integer> ids) {
        log.info("批量删除学生数据:{}", ids);
        if (ids != null && !ids.isEmpty()) {
            studentService.deleteByIds(ids);
        }
        return Result.success();
    }
    //违纪处理，修改学生分数
    @PutMapping("/violation/{id}/{score}")
    public Result violation(@PathVariable Integer id, @PathVariable Integer score) {
        log.info("处理违纪学生数据, id: {}, score: {}", id, score);
        if (id == null || id <= 0) {
            log.warn("学生ID无效: {}", id);
            return Result.error("学生ID必须大于0");
        }
        if (score == null || score <= 0) {
            log.warn("扣除分数无效: {}", score);
            return Result.error("扣除分数必须大于0");
        }
        studentService.violation(id, score);
        log.info("学生违纪处理成功, id: {}", id);
        return Result.success();
    }
}
