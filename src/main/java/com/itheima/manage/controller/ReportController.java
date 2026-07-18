package com.itheima.manage.controller;

import com.itheima.manage.pojo.*;
import com.itheima.manage.service.EmpLogService;
import com.itheima.manage.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/report")
@RestController
public class ReportController {
    @Autowired
    private ReportService reportService;

    @Autowired
    private EmpLogService empLogService;

    //员工职位人数统计
    @GetMapping("/empJobData")
    public Result empJobData() {
        log.info("生成员工岗位数据");
        JobOption jobOption = reportService.getEmpJobData();
        return Result.success(jobOption);
    }

    //员工性别统计
    @GetMapping("/empGenderData")
    public Result empGenderData() {
        log.info("生成员工性别数据");
        GenderOption genderOption = reportService.getEmpGenderData();
        return Result.success(genderOption);
    }

    //学员学历统计
    @GetMapping("/studentDegreeData")
    public Result studentDegreeDate() {
        log.info("生成学员学历数据");
        StudentDegreeOption studentDegreeOption = reportService.getStudentDegreeData();
        return Result.success(studentDegreeOption);
    }

    //班级人数统计
    @GetMapping("/studentCountData")
    public Result studentCountDate() {
        log.info("生成班级人数数据");
        ClazzCountOption clazzCountOption = reportService.getStudentCountData();
        return Result.success(clazzCountOption);
    }

}
