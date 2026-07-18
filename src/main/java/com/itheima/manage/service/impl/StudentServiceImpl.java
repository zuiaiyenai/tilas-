package com.itheima.manage.service.impl;

import com.itheima.manage.mapper.StudentMapper;
import com.itheima.manage.pojo.PageResult;
import com.itheima.manage.pojo.Student;
import com.itheima.manage.pojo.StudentQueryParam;
import com.itheima.manage.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
@Slf4j
@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentMapper studentMapper;

    @Override
    public PageResult<Student> page(StudentQueryParam queryParam) {
        Integer page = queryParam.getPage() == null || queryParam.getPage() < 1 ? 1 : queryParam.getPage();
        Integer pageSize = queryParam.getPageSize() == null || queryParam.getPageSize() < 1 ? 10 : queryParam.getPageSize();
        queryParam.setPage(page);
        queryParam.setPageSize(pageSize);
        queryParam.setOffset((page - 1) * pageSize);

        List<Student> rows = studentMapper.listPage(queryParam);
        Long total = studentMapper.count(queryParam);
        return new PageResult<>(total, rows);
    }

    //根据id查询
    @Override
    public Student findById(Integer id) {
        return studentMapper.findById(id);
    }

    //增加学生信息
    @Override
    public void add(Student student) {
        studentMapper.add(student);
    }

    //修改学生信息
    @Override
    public void update(Student student) {
        studentMapper.update(student);
    }

    //批量删除学生
    @Override
    public void deleteByIds(List<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        studentMapper.deleteByIds(ids);
    }

    //违纪处理
    @Override
    public void violation(Integer id, Integer score) {
        log.info("开始处理学生违纪数据, id: {}, score: {}", id, score);
        studentMapper.violation(id, score);
        log.info("学生违纪处理完成, id: {}", id);
    }
}
