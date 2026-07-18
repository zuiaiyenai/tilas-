
package com.itheima.manage.pojo;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class ClazzQueryParam {
    private Integer page = 1;
    private Integer pageSize = 10;
    private Integer offset = 0;
    private String name;
    private String room;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate beginDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private Integer masterId;
    private Integer subject;
}
