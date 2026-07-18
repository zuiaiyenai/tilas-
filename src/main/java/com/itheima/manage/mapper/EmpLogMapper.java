package com.itheima.manage.mapper;

import com.itheima.manage.pojo.EmpLog;
import com.itheima.manage.pojo.EmpLogQueryParam;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface EmpLogMapper {

    @Insert("insert into emp_log (operate_time, info) values (#{operateTime}, #{info})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insert(EmpLog empLog);

    @SelectProvider(type = EmpLogSqlProvider.class, method = "listPage")
    List<EmpLog> listPage(EmpLogQueryParam empLogQueryParam);

    @SelectProvider(type = EmpLogSqlProvider.class, method = "count")
    Long count(EmpLogQueryParam empLogQueryParam);

}
