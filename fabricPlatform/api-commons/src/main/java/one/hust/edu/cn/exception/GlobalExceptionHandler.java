package one.hust.edu.cn.exception;

import lombok.extern.slf4j.Slf4j;
import one.hust.edu.cn.entities.CommonResult;
import one.hust.edu.cn.exception.fabric.FabricException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
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
    public String handleFabricException(FabricException e) {
//        log.error(e.getMessage());
//        return new CommonResult<>(HttpStatus.FORBIDDEN.value(), "fabric error: " + e.getMessage());
        return "fabric error: " + e.getMessage();
    }


}
