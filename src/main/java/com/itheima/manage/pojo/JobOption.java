package com.itheima.manage.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class JobOption {
    private List  joblist;//职务列表
    private List  datalist;//数据列表
}
