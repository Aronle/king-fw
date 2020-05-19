package com.mars.fw.security.authentication.handler;

import com.mars.fw.cache.CacheService;
import com.mars.fw.common.utils.SpringUtil;
import com.mars.fw.security.tool.KingResponseUtils;
import com.mars.fw.web.reponse.King;
import com.mars.fw.web.reponse.KingCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: mars
 * @Description:
 * @Date: 2019/11/6 20:09
 * @Version: 1.0
 */
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String token = request.getHeader("token");
        CacheService cacheService = SpringUtil.getBean(CacheService.class);
        cacheService.remove(token);
        King result = new King(KingCode.LOGOUT_SUCCESS, "success");
        KingResponseUtils.writeData(response, result);
    }
}
