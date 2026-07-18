package com.itheima.manage.controller;

import com.itheima.manage.pojo.GenderOption;
import com.itheima.manage.pojo.JobOption;
import com.itheima.manage.pojo.Result;
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
    //员工职位人数统计
    @GetMapping("/empJobData")
    public  Result empJobData() {
        log.info("生成员工岗位数据");
        JobOption jobOption = reportService.getEmpJobData();
        return Result.success(jobOption);
    }
//员工性别统计
    @GetMapping("/empGenderData")
    public  Result empGenderData() {
        log.info("生成员工性别数据");
        GenderOption genderOption = reportService.getEmpGenderData();
        return Result.success(genderOption);
    }
}
