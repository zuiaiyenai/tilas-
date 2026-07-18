package com.itheima.manage.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDegreeOption {
    private List<String> degreeList;
    private List<Integer> dataList;
}
