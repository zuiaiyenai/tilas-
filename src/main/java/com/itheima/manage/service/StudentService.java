package com.itheima.manage.service;

import com.itheima.manage.pojo.PageResult;
import com.itheima.manage.pojo.Student;
import com.itheima.manage.pojo.StudentQueryParam;

import java.util.List;

public interface StudentService {
    PageResult<Student> page(StudentQueryParam queryParam);

    Student findById(Integer id);

    void add(Student student);

    void update(Student student);

    void deleteByIds(List<Integer> ids);
    void violation(Integer id, Integer score);
}
