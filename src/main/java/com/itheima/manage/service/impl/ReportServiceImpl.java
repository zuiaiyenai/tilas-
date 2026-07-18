package com.itheima.manage.service.impl;

import com.itheima.manage.mapper.EmpMapper;
import com.itheima.manage.pojo.ClazzCountOption;
import com.itheima.manage.pojo.GenderOption;
import com.itheima.manage.pojo.JobOption;
import com.itheima.manage.pojo.StudentDegreeOption;
import com.itheima.manage.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private EmpMapper empMapper;

    //员工职位人数统计
    @Override
    public JobOption getEmpJobData() {
        List<Map<String, Object>> list = empMapper.countEmpJobData();

        List<String> jobList = list.stream()
                .map(dataMap -> (String) dataMap.get("pos"))
                .collect(Collectors.toList());
        List<Integer> dataList = list.stream()
                .map(dataMap -> ((Number) dataMap.get("num")).intValue())
                .collect(Collectors.toList());
        return new JobOption(jobList, dataList);
    }

    //员工性别统计
    @Override
    public GenderOption getEmpGenderData() {
        List<Map<String, Object>> list = empMapper.countEmpGenderData();
        List<String> genderList = list.stream()
                .map(dataMap -> (String) dataMap.get("pos"))
                .collect(Collectors.toList());
        List<Integer> dataList = list.stream()
                .map(dataMap -> ((Number) dataMap.get("num")).intValue())
                .collect(Collectors.toList());
        return new GenderOption(genderList, dataList);
    }
    //学生学历统计
    @Override
    public StudentDegreeOption getStudentDegreeData() {
        List<Map<String, Object>> list = empMapper.countStudentDegreeData();
        List<String> degreeList = list.stream()
                .map(dataMap -> (String) dataMap.get("pos"))
                .collect(Collectors.toList());
        List<Integer> dataList = list.stream()
                .map(dataMap -> ((Number) dataMap.get("num")).intValue())
                .collect(Collectors.toList());
        return new StudentDegreeOption(degreeList, dataList);
    }
//班级人数统计
    @Override
    public ClazzCountOption getStudentCountData() {
        List<Map<String, Object>> list = empMapper.countClazzCountData();
        List<String> clazzList = list.stream()
                .map(dataMap -> (String) dataMap.get("pos"))
                .collect(Collectors.toList());
        List<Integer> dataList = list.stream()
                .map(dataMap -> ((Number) dataMap.get("num")).intValue())
                .collect(Collectors.toList());
        return new ClazzCountOption(clazzList, dataList);

    }

}
