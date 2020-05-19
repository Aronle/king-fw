package com.mars.fw.web.exception.advice;

import com.mars.fw.web.exception.KingException;
import com.mars.fw.web.reponse.ExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Pattern;

/**
 * 统一系统异常处理
 *
 * @Author King
 * @create 2020/4/20 15:25
 */
@Slf4j
@ControllerAdvice
public class KingExceptionHandlerAdvice {


    /**
     * 统一异常处理
     *
     * @param request
     * @param response
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object controllerExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex) {

        String message = ExceptionCode.DEFAULT_EXCEPTION.message();

        if (KingException.class.isAssignableFrom(ex.getClass())) {
            KingException kingException = (KingException) ex;
            printExceptionLog(kingException, ex);
            return kingException.transferResponse();
        }

        if (ex instanceof MissingServletRequestParameterException) {
            MissingServletRequestParameterException exception = (MissingServletRequestParameterException) ex;
            message = "必填 " + exception.getParameterType() + "类型参数 '" + exception.getParameterName() + "' 不存在";
        } else if (ex instanceof EmptyResultDataAccessException) {
            EmptyResultDataAccessException exception = (EmptyResultDataAccessException) ex;
            String pattern = "No class.*with.*exists!.*";
            boolean isMatch = Pattern.matches(pattern, ex.getMessage());
            if (isMatch) {
                message = "数据不存在，请确认";
            }
        } else {
            if (null != ex.getMessage()) {
                message = "系统异常，联系管理员";
            }
        }

        KingException kingException = new KingException(ExceptionCode.DEFAULT_EXCEPTION, message);
        printExceptionLog(kingException, ex);
        if (log.isDebugEnabled()) {
            log.debug(ex.getMessage(), ex.getStackTrace());
        }
        return kingException.transferResponse();

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
