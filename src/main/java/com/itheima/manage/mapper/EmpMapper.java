package com.itheima.manage.mapper;

import com.itheima.manage.pojo.Emp;
import com.itheima.manage.pojo.EmpQueryParam;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface EmpMapper {
    public List<Emp> list(EmpQueryParam empQueryParam);
    public List<Emp> listPage(EmpQueryParam empQueryParam);
    public Long count(EmpQueryParam empQueryParam);

    @Options(useGeneratedKeys = true,keyProperty = "id", keyColumn = "id")
    @Insert("insert into emp(username,password,name,gender,phone,job,salary,image,entry_date,dept_id,create_time,update_time) " +
            "values(#{username},#{password},#{name},#{gender},#{phone},#{job},#{salary},#{image},#{entryDate},#{deptId},#{createTime},#{updateTime})")
    void insert(Emp emp);
    //根据id修改员工信息
    void update(Emp emp);
//批量删除
    void deleteByIds(List<Integer> ids);

//查询单个员工信息
    Emp findById(Integer id);
//查询所有员工信息
@Select("select e.*, d.name as deptName from emp e left join dept d on e.dept_id = d.id")
List<Emp> findall();
    List<Map<String, Object>> countEmpJobData();
    List<Map<String, Object>> countEmpGenderData();
}
