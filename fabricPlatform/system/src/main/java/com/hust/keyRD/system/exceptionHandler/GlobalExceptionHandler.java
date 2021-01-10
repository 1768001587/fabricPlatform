package com.hust.keyRD.system.exceptionHandler;

import com.hust.keyRD.commons.entities.CommonResult;
import com.hust.keyRD.commons.exception.fabric.FabricException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @program: fabricTest
 * @description: 全局异常处理
 * @author: zwh
 * @create: 2021-01-08 09:54
 **/
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    @ExceptionHandler(FabricException.class)
    public CommonResult handleFabricException(FabricException e) {
        return new CommonResult<>(HttpStatus.FORBIDDEN.value(), "fabric error: " + e.getMessage());
        //return "fabric error: " + e.getMessage();
    }


}
