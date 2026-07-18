package com.itheima.manage.pojo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class StudentQueryParam {
    private Integer page = 1;
    private Integer pageSize = 10;
    private Integer offset = 0;
    private String name;
    private String no;
    private Integer gender;
    private Integer clazzId;
    private Integer degree;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate beginGraduationDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endGraduationDate;
}
