package com.mars.fw.security.authentication.handler;


import com.mars.fw.security.authentication.exception.AuthenticationSmsException;
import com.mars.fw.security.authentication.exception.InvalidVerifyCodeException;
import com.mars.fw.security.authentication.exception.LoginLockException;
import com.mars.fw.security.tool.KingResponseUtils;
import com.mars.fw.web.reponse.King;
import com.mars.fw.web.reponse.KingCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author king
 */
@Slf4j
public class AuthFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        King result = new King();
        if (exception instanceof BadCredentialsException || exception instanceof UsernameNotFoundException) {
            result.setCode(KingCode.PASS_WRONG.code());
            result.setMsg(KingCode.PASS_WRONG.message() + "或" + KingCode.USER_NOT_FIND.message());
        } else if (exception instanceof InvalidVerifyCodeException) {
            result.setCode(KingCode.CAPTCHA_ERROR.code());
            result.setMsg(KingCode.CAPTCHA_ERROR.message());
        } else if (exception.getCause() instanceof LoginLockException) {
            result.setCode(KingCode.LOGIN_LOCK.code());
            result.setMsg(KingCode.LOGIN_LOCK.message());
        } else if (exception instanceof AuthenticationSmsException) {
            result.setCode(KingCode.SMS_EXCEPTION.code());
            result.setMsg(KingCode.SMS_EXCEPTION.message());
        } else {
            result.setCode(KingCode.DEFAULT_EXCEPTION.code());
            result.setMsg(KingCode.DEFAULT_EXCEPTION.message());
            log.error("【登录】登录异常。", exception);
        }
        KingResponseUtils.writeData(response, result);
    }
}
