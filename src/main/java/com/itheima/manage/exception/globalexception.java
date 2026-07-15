
package com.itheima.manage.exception;

import com.itheima.manage.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.sql.SQLException;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class globalexception {

    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public Result handleMethodArgumentNotValidException(org.springframework.web.bind.MethodArgumentNotValidException e) {
        log.error("参数校验失败: {}", e.getMessage());
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        String message = "参数校验失败";
        if (!fieldErrors.isEmpty()) {
            message = fieldErrors.get(0).getDefaultMessage();
        }
        return Result.error(message);
    }

    @ExceptionHandler(BindException.class)
    public Result handleBindException(BindException e) {
        log.error("参数绑定失败: {}", e.getMessage());
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        String message = "参数绑定失败";
        if (!fieldErrors.isEmpty()) {
            message = fieldErrors.get(0).getDefaultMessage();
        }
        return Result.error(message);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("请求体解析失败", e);
        String message = e.getMessage();
        if (message != null && message.contains("String")) {
            return Result.error("数据类型错误: 期望字符串类型的数据");
        }
        if (message != null && message.contains("JSON feature")) {
            return Result.error("JSON格式错误: 请检查JSON语法");
        }
        return Result.error("请求数据格式错误,请检查JSON格式");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result handleMissingParams(MissingServletRequestParameterException e) {
        log.error("缺少必填参数: {}", e.getParameterName());
        return Result.error("缺少必填参数: " + e.getParameterName());
    }

    @ExceptionHandler(SQLException.class)
    public Result handleSQLException(SQLException e) {
        log.error("数据库异常", e);

        String message = e.getMessage();
        if (message != null && message.contains("Duplicate entry")) {
            if (message.contains("for key 'username'")) {
                return Result.error("用户名已存在,请使用其他用户名");
            } else if (message.contains("for key 'phone'")) {
                return Result.error("手机号已存在,请使用其他手机号");
            }
            return Result.error("数据重复,请检查输入");
        }

        if (message != null && message.contains("Foreign key")) {
            return Result.error("关联的数据不存在,请检查外键字段");
        }

        return Result.error("数据库操作失败,请稍后重试");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Result handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("非法参数: {}", e.getMessage());
        return Result.error(e.getMessage());
    }

    @ExceptionHandler(ArithmeticException.class)
    public Result handleArithmeticException(ArithmeticException e) {
        log.error("算术异常", e);
        return Result.error("系统计算异常");
    }

    @ExceptionHandler(IndexOutOfBoundsException.class)
    public Result handleIndexOutOfBoundsException(IndexOutOfBoundsException e) {
        log.error("索引越界异常", e);
        return Result.error("数组或集合索引越界");
    }

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        log.error("服务器未知异常", e);
        e.printStackTrace();
        return Result.error("服务器异常: " + e.getMessage());
    }

}