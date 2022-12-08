package org.xqzp.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    public R error(Exception e){
        return R.ok().message("执行了全局异常");
    }

    //自定义异常
    @ExceptionHandler(ProxyException.class)
    @ResponseBody
    public R error(ProxyException e){
        return R.error().code(e.getCode()).message(e.getMsg());
    }
}