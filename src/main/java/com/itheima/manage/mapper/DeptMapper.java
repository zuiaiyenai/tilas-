package com.itheima.manage.mapper;

import com.itheima.manage.pojo.Dept;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DeptMapper {
    //查询所有部门信息
    @Select("SELECT id,name,create_time,update_time from dept ORDER BY update_time desc;")
    List<Dept> findAll();
    //删除
    @Delete("DELETE FROM dept WHERE id=#{id}")
    void deleteById(Integer id);
    @Insert("INSERT INTO dept(name,create_time,update_time) VALUES(#{name},#{createTime},#{updateTime})")
    void add(Dept dept);
    @Select("SELECT id,name,create_time,update_time from dept WHERE id=#{id}")
    Dept findById(Integer id);
    @Update("UPDATE dept SET name=#{name},update_time=#{updateTime} WHERE id=#{id}")
    void update(Dept dept);
}