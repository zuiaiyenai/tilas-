package com.itheima.manage.mapper;

import com.itheima.manage.pojo.Emp;
import com.itheima.manage.pojo.EmpQueryParam;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

import java.util.List;

@Mapper
public interface EmpMapper {
    //@Select("select e.*,d.name deptName from emp e left join dept d on e.dept_id=d.id "+
            //"order by e.update_time desc")
    public List<Emp> list(EmpQueryParam empQueryParam);

    //新增员工基本信息
@Options(useGeneratedKeys = true,keyColumn = "id")
    @Insert("insert into emp(username,password,name,gender,phone,job,salary,image,entry_date,dept_id,create_time,update_time) " +
            "values(#{username},#{password},#{name},#{gender},#{phone},#{job},#{salary},#{image},#{entryDate},#{deptId},#{createTime},#{updateTime})")
    void insert(Emp emp);

}
