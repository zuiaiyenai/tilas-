package com.itheima.manage.mapper;

import com.itheima.manage.pojo.Student;
import com.itheima.manage.pojo.StudentQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StudentMapper {
    List<Student> listPage(StudentQueryParam queryParam);
    Long count(StudentQueryParam queryParam);
    Student findById(Integer id);
    void add(Student student);
    void update(Student student);
    void delete(Integer id);
    void deleteByIds(List<Integer> ids);
    void violation(@Param("id") Integer id, @Param("scoreValue") Integer scoreValue);
}
