package com.itheima.manage.service;

import com.itheima.manage.pojo.GenderOption;
import com.itheima.manage.pojo.JobOption;

public interface ReportService {
    //员工职位人数统计

    JobOption getEmpJobData();
    GenderOption getEmpGenderData();
}
