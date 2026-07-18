package com.itheima.manage.service;

import com.itheima.manage.pojo.ClazzCountOption;
import com.itheima.manage.pojo.GenderOption;
import com.itheima.manage.pojo.JobOption;
import com.itheima.manage.pojo.StudentDegreeOption;

public interface ReportService {
    //员工职位人数统计
    JobOption getEmpJobData();
    //员工性别统计
    GenderOption getEmpGenderData();
    //学员学历统计
    StudentDegreeOption getStudentDegreeData();
//班级人数统计
    ClazzCountOption getStudentCountData();

}
