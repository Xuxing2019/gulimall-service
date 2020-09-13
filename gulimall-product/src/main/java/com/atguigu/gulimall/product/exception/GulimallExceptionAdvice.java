package com.atguigu.gulimall.product.exception;

import com.atguigu.common.exception.GulimallExceptionEnum;
import com.atguigu.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

/**
 * @author xuxing
 * @date 2020/7/29
 */
@Slf4j
@RestControllerAdvice(basePackages = "com.atguigu.gulimall.product.controller")
public class GulimallExceptionAdvice {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R exception(MethodArgumentNotValidException e){
        log.error("数据校验出现问题:{}",  e.getMessage());
        BindingResult bindingResult = e.getBindingResult();
        HashMap<String, String> map = new HashMap<>();
        bindingResult.getFieldErrors().forEach((item)->{
            map.put(item.getField(),item.getDefaultMessage());
        });
        return R.error(GulimallExceptionEnum.PARAM_VERIFY_EXCEPTION).put("data",map);
    }

    @ExceptionHandler(value = Exception.class)
    public R exception(Exception e){
        e.printStackTrace();
        log.error("异常信息:{}",e.getMessage());
        return R.error(GulimallExceptionEnum.UNKNOWN_EXCEPTION);
    }
}
