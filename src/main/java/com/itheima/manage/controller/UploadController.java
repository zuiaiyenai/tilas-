package com.itheima.manage.controller;

import com.itheima.manage.pojo.Result;
import com.itheima.manage.utils.AliyunOSSOperator;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
public class UploadController {
   /* @SneakyThrows
    @PostMapping("/upload")
    public Result upload(String name, Integer age, MultipartFile file) {
        log.info("name:{}, age:{}, {}", name, age, file);
        //获取原始文件名
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFileName = UUID.randomUUID().toString() + extension;
        //保存文件
        file.transferTo(new File("D:/images/" + file.getOriginalFilename()));
        return Result.success();
        */

    @Autowired
    private AliyunOSSOperator aliyunOSSOperator;
     @PostMapping("/upload")
    public Result upload(MultipartFile file) throws Exception {
         log.info("file:{}", file.getOriginalFilename());
         //将文件交给oss储存管理
          String url =aliyunOSSOperator.upload(file.getBytes(), file.getOriginalFilename());
          log.info("url:{}", url);
          return Result.success(url);
          }
     }



