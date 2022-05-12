package com.ex.echo.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @Author: Exception
 * @Date: 2022/5/5
 * @Description
 */
@Slf4j
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
public class GlobalExceptionHandler {

    /**
     * 异常处理方法
     *
     * @param ex .
     * @return .
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public JsonModel<String> exceptionHandler(SQLIntegrityConstraintViolationException ex) {
        log.error(ex.getMessage());
        if (ex.getMessage().contains("Duplicate entry")) {
            String[] split = ex.getMessage().split(" ");
            String msg = split[2] + "已存在";
            return JsonModel.error(msg);
        }
        return JsonModel.error("未知错误");
    }

    /**
     * 自定义异常处理方法
     *
     * @param ex .
     * @return .
     */
    @ExceptionHandler(CustomException.class)
    public JsonModel<String> exceptionHandler(CustomException ex) {
        log.error(ex.getMessage());
        return JsonModel.error(ex.getMessage());
    }
}
