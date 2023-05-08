package com.gaoap.demo.micro.demos.nacosconfig.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {



    /**
     * 业务异常，统一处理
     * @param e 异常对象
     * @return ResponseResult 全局异常响应
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseResult<String> businessException(BusinessException e) {
        log.info("code={}, message={}", e.getCode(), e.getMessage());
        return ResponseResult.errorResult(e.getCode(), e.getMessage());
    }


}

