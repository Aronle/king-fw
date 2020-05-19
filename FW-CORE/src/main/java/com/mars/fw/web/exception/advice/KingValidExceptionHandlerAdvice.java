package com.mars.fw.web.exception.advice;

import com.mars.fw.web.exception.KingException;
import com.mars.fw.web.reponse.ExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一系统异常处理
 *
 * @Author King
 * @create 2020/4/20 15:25
 */
@Slf4j
@ControllerAdvice
public class KingValidExceptionHandlerAdvice {

    /**
     * 对springMvc的参数校验 Validated
     * 异常做统一的处理
     * 处理成友好的展示方式(原生展示很不友好)
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public Object handleBindException(BindException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        //fixme 数据还未写入
        KingException KingException = new KingException(ExceptionCode.DEFAULT_EXCEPTION, fieldError.getField() + fieldError.getDefaultMessage());
        printExceptionLog(KingException, ex);
        return KingException.transferResponse();
    }

    /**
     * 对springMvc的参数校验 Validated
     * 异常做统一的处理
     * 处理成友好的展示方式(原生展示很不友好)
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        KingException KingException = new KingException(ExceptionCode.DEFAULT_EXCEPTION, fieldError.getField() + fieldError.getDefaultMessage());
        printExceptionLog(KingException, ex);
        return KingException.transferResponse();
    }


    /**
     * 错误日志
     *
     * @param kingException
     */
    private void printExceptionLog(KingException kingException, Exception ex) {
        if (kingException.getCause() == null) {
            log.error("######ErrorCode: {},message: {}#######", kingException.getCode().code(), kingException.getMessage());
        } else {
            log.error("######ErrorCode: {},message: {}#######", kingException.getCode().code(), kingException.getMessage(), kingException);
        }
        log.error("######ErrorCode: {},message: {}#######", kingException.getCode().code(), kingException.getMessage(), ex);
    }

}
