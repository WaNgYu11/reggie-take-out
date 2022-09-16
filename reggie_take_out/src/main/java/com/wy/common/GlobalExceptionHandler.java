package com.wy.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @Author: wangyu
 * @Date: 2022/09/15/8:51
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {


    /**
     * 处理 SQLIntegrityConstraintViolationException 异常方法
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandle(SQLIntegrityConstraintViolationException exception) {
        log.info(exception.getMessage());
        if (exception.getMessage().contains("Duplicate entry")) {
            String[] split = exception.getMessage().split(" ");
            String msg = split[2] + "这个用户名已存在";
            return R.error(msg);
        }
        return R.error("未知错误");
    }


    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandle(CustomException exception) {
        log.error(exception.getMessage()); //报错记得打日志
        //这里拿到的message是业务类抛出的异常信息，我们把它显示到前端
        return R.error(exception.getMessage());

    }


}
