package com.itheima.manage.mapper;
import com.itheima.manage.pojo.EmpLogQueryParam;

public class EmpLogSqlProvider {

    public String listPage(EmpLogQueryParam queryParam) {
        StringBuilder sql = new StringBuilder("SELECT id, operate_time, info FROM emp_log WHERE 1=1");

        if (queryParam.getBeginTime() != null) {
            sql.append(" AND operate_time >= #{beginTime}");
        }

        if (queryParam.getEndTime() != null) {
            sql.append(" AND operate_time <= #{endTime}");
        }

        sql.append(" ORDER BY operate_time DESC LIMIT #{offset}, #{pageSize}");
        return sql.toString();
    }

    public String count(EmpLogQueryParam queryParam) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM emp_log WHERE 1=1");

        if (queryParam.getBeginTime() != null) {
            sql.append(" AND operate_time >= #{beginTime}");
        }

        if (queryParam.getEndTime() != null) {
            sql.append(" AND operate_time <= #{endTime}");
        }

        return sql.toString();
    }
}
