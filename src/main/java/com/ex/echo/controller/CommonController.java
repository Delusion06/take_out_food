package com.ex.echo.controller;

import com.ex.echo.common.JsonModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @Author: Exception
 * @Date: 2022/5/5
 * @Description: 文件上传和下载管理
 */
@Slf4j
@RestController
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;

    @RequestMapping(value = "/common/upload", method = RequestMethod.POST)
    public JsonModel<String> upload(MultipartFile file) {
        // 获取文件原始名称
        String originalFilename = file.getOriginalFilename();
        // 截取.jpg
        String fileName = originalFilename.substring(originalFilename.lastIndexOf("."));
        fileName = fileName + UUID.randomUUID().toString();

        // 创建图片路径
        File path = new File(basePath);
        if (!path.exists()) {
            path.mkdirs();
        }

        // 存放图片的路径
        try {
            file.transferTo(new File(basePath + fileName));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return JsonModel.success(fileName);
    }

    /**
     * 文件下载
     *
     * @param name .
     * @param response .
     */
    @RequestMapping(value = "/common/download", method = RequestMethod.GET)
    public void download(String name, HttpServletResponse response) throws Exception {
        // 读取文件内容
        FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));
        // 写入到浏览器
        ServletOutputStream outputStream = response.getOutputStream();
        // 设置响应方式
        response.setContentType("image/jpeg");

        // 读取一行一行
        int len = 0;
        byte[] bytes = new byte[1024];
        while ((len = fileInputStream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, len);
            outputStream.flush();
        }
        // 关闭资源
        outputStream.close();
        fileInputStream.close();
    }
}
