package com.mars.fw.security.authentication.provider;

import com.mars.fw.security.tool.model.CustomAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * @description:定义一个自定义的provider的虚类做认证的公共实现
 * @author: aron
 * @date: 2019-07-03 15:45
 */
public abstract class AbstractAuthenticationProvider implements AuthenticationProvider {

    private CustomAuthenticationToken customAuthenticationToken;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        customAuthenticationToken = (CustomAuthenticationToken) authentication;
        return authenticateCustom(customAuthenticationToken);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }

    /**
     * 自定义认证方法 由子类实现
     *
     * @param customAuthenticationToken 自定义参数
     * @return 返回认证的结果 传递到链路中
     */
    protected abstract Authentication authenticateCustom(CustomAuthenticationToken customAuthenticationToken);
}
