package com.itheima.manage.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenderOption {
    private List<String> genderList;
    private List<Integer> dataList;
}
