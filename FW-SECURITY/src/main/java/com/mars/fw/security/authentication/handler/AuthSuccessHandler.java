package com.mars.fw.security.authentication.handler;

import com.google.common.collect.Maps;
import com.mars.fw.cache.CacheService;
import com.mars.fw.common.utils.JsonMapper;
import com.mars.fw.common.utils.SpringUtil;
import com.mars.fw.security.authentication.service.CustomUserDetails;
import com.mars.fw.security.tool.KingResponseUtils;
import com.mars.fw.security.tool.SecurityConstant;
import com.mars.fw.security.tool.TokenUtils;
import com.mars.fw.security.tool.model.AuthResponseModel;
import com.mars.fw.web.reponse.King;
import com.mars.fw.web.reponse.KingCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author king
 */
@Slf4j
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    private static final long TOKEN_EXPIRATION_TIME = 60 * 60 * 24 * 1000;

    private static final String SUCCESS_MSG = "登录成功";


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        King result = new King();
        String token = this.produceJWTToken(response, authentication);
        CacheService cacheService = SpringUtil.getBean(CacheService.class);
        try {
            AuthResponseModel model = filterAuthResponseModel(customUserDetails);
            String tmp = JsonMapper.getDefault().toJson(model);
            cacheService.set(token, tmp, TOKEN_EXPIRATION_TIME);
            Map<String, Object> data = Maps.newHashMap();
            data.put(SecurityConstant.KEY_HEAD_TOKEN, token);
            data.put("User", customUserDetails);
            result.setData(data);
            result.setMsg(SUCCESS_MSG);
            KingResponseUtils.writeData(response, result);
        } catch (Exception e) {
            log.error("【登录】登录成功后，执行成功函数，抛出异常。", e);
            result.setCode(KingCode.SUCCESS.code());
            result.setMsg(KingCode.SUCCESS.message());
            cacheService.remove(token);
            result.setData(null);
            KingResponseUtils.writeData(response, result);
        }
    }

    /**
     * 用户类型过滤器
     *
     * @param customUserDetails
     * @return
     */
    private AuthResponseModel filterAuthResponseModel(CustomUserDetails customUserDetails) {
        AuthResponseModel model = new AuthResponseModel();
        BeanUtils.copyProperties(customUserDetails, model);
        return model;
    }


    /**
     * 生成token
     *
     * @param response
     * @param authentication
     * @return
     */
    private String produceJWTToken(HttpServletResponse response, Authentication authentication) {
        String token = TokenUtils.createToken(((CustomUserDetails) authentication.getPrincipal()).getUserName(), TOKEN_EXPIRATION_TIME);
        response.addHeader(SecurityConstant.KEY_HEAD_TOKEN, token);
        return token;
    }


}
