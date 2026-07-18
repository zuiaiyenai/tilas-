package com.itheima.manage.controller;
import com.itheima.manage.pojo.EmpLog;
import com.itheima.manage.pojo.EmpLogQueryParam;
import com.itheima.manage.pojo.PageResult;
import com.itheima.manage.pojo.Result;
import com.itheima.manage.service.EmpLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/log")
@RestController
public class LogController {

    @Autowired
    private EmpLogService empLogService;

    @GetMapping("/page")
    public Result logPage(EmpLogQueryParam empLogQueryParam) {
        log.info("日志分页查询: {}", empLogQueryParam);
        PageResult<EmpLog> pageResult = empLogService.page(empLogQueryParam);
        return Result.success(pageResult);
    }
}
