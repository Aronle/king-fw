package com.mars.fw.security.authorization.filter;

import com.mars.fw.cache.CacheService;
import com.mars.fw.common.utils.SpringUtil;
import com.mars.fw.common.utils.StringUtils;
import com.mars.fw.security.tool.KingResponseUtils;
import com.mars.fw.security.tool.SecurityConstant;
import com.mars.fw.web.context.GlobalEntry;
import com.mars.fw.web.context.KingContext;
import com.mars.fw.web.context.KingContextProvider;
import com.mars.fw.web.reponse.King;
import com.mars.fw.web.reponse.KingCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 授权过滤器
 *
 * @Author King
 * @create 2020/5/6 15:28
 */
@Slf4j
public class AuthenticationFilter extends BasicAuthenticationFilter {

    private AccessMatch accessMatch;

    private KingContextProvider provider;


    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        accessMatch = SpringUtil.getBean(AccessMatchImpl.class);
        provider = SpringUtil.getBean(AuthContextProviderImpl.class);
    }

    /**
     * 拦截所有的请求 进行鉴权操作
     * <p>
     * 1.接口验签：a:如果接口没有配置白名单，全部都要走验签；否则跳过；
     * 2.token校验：a:如果没有配置白名单，需要token验证；否则跳过；
     * 3.IP黑白名单：白名单：只有配置的IP才能进行接口访问
     * 4.文件加解密：暂时不做（RSA）
     *
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        if (accessMatch.isAccessUri(request)) {
            //白名单 不需要任何验证
            chain.doFilter(request, response);
            return;
        }
        /**
         * 验签
         */
//        if (accessMatch.isAccessSignUri(request)) {
//            //token白名单，不需要token验证
//            chain.doFilter(request, response);
//            return;
//        }

//        /**
//         * token鉴权验证
//         */
//        if (!authorize(request, response)) {
//            return;
//        }
//        // todo 这边可以做接口授权
//
//        String token = request.getHeader(SecurityConstant.KEY_HEAD_TOKEN);
//        GlobalEntry entry = new GlobalEntry();
//        try {
//            entry = provider.getGlobalEntry(token);
//            KingContext.setContext(entry);
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//        }

        chain.doFilter(request, response);


    }


    /**
     * token 鉴权
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    private boolean authorize(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String token = request.getHeader(SecurityConstant.KEY_HEAD_TOKEN);
            if (StringUtils.isBlank(token)) {
                King result = new King(KingCode.TOKEN_NOT_EMPTY);
                KingResponseUtils.writeData(response, result);
                return false;
            }
            if (!checkToken(token) || !checkTokenByRequestUri(token, request)) {
                King result = new King(KingCode.TOKEN_ERROR);
                KingResponseUtils.writeData(response, result);
                return false;
            }

        } catch (Exception e) {
            King result = new King(KingCode.TOKEN_EXCEPTION);
            KingResponseUtils.writeData(response, result);
            return false;
        }
        return true;
    }


    /**
     * 校验token
     *
     * @param token
     * @return
     */
    private boolean checkToken(String token) {
        CacheService cacheService = SpringUtil.getBean(CacheService.class);
        Object userId = cacheService.get(token);
        if (null == userId) {
            logger.info(String.format("【登录】token对应的用户ID为空，token：%s", token));
            return false;
        }
        return true;
    }

    /**
     * 根据请求URI，校验token
     *
     * @param token
     * @param request
     * @return
     */
    private boolean checkTokenByRequestUri(String token, HttpServletRequest request) {
        return true;
    }
}
