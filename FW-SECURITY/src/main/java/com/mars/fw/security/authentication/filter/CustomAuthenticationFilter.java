package com.mars.fw.security.authentication.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mars.fw.security.authentication.exception.InvalidVerifyCodeException;
import com.mars.fw.security.authorization.security.HttpHelper;
import com.mars.fw.security.tool.HttpTools;
import com.mars.fw.security.tool.model.AuthenticationParam;
import com.mars.fw.security.tool.model.CustomAuthenticationToken;
import com.mars.fw.security.tool.model.UserTypeEnum;
import com.mars.fw.web.exception.KingException;
import com.mars.fw.web.reponse.KingCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 拦截器
 * 重新构造认证的自定义参数
 * @author king
 */
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public static final String SPRING_SECURITY_FORM_USER_TYPE_KEY = "user_type";
    public static final String SPRING_SECURITY_VERIFY_CODE_KEY_KEY = "verify_code_key";
    public static final String SPRING_SECURITY_VERIFY_CODE_KEY = "verify_code";

    private boolean postOnly = true;

    /**
     * 接收json数据
     */
    private Map<String, String> input = null;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (postOnly && !"POST".equals(request.getMethod())) {
            //不是post的请求直接过滤掉
            throw new AuthenticationServiceException("认证接口不支持的请求方法类型: " + request.getMethod());
        }

        if (HttpTools.isJsonBody(request)) {
            //判断数据类型支持 application/json 和 表单
            input = parseJsonBody(request);
        }

        AuthenticationParam param = createAuthenticationParam(request);

        if (param.getUserType() == UserTypeEnum.admin || param.getUserType() == UserTypeEnum.organ || param.getUserType() == UserTypeEnum.agent) {
            //只有运营端的运营中心 机构 代理登录需要验证码
            if (!validateCaptcha(param.getVerifyCodeKey(), param.getVerifyCode())) {
                throw new InvalidVerifyCodeException("验证码");
            }
        }
        //重新构建 spring-security 认证传递的自定义参数
        CustomAuthenticationToken authRequest = new CustomAuthenticationToken(param);
        //调用父类的方法 将请求和重写的UsernamePasswordAuthenticationToken 写入认证链路
        setDetails(request, authRequest);
        //通过AuthenticationManager 返回spring-security中Authentication认证参数传递接口
        return this.getAuthenticationManager().authenticate(authRequest);
    }


    /**
     * 构建 认证接口参数
     *
     * @param request
     * @return
     */
    private AuthenticationParam createAuthenticationParam(HttpServletRequest request) {

        String userName = obtainUsername(request);
        String password = obtainPassword(request);
//        String userType = obtainCompanyType(request);
//        String verifyCodeKey = obtainVerifyCodeKey(request);
//        String verifyCode = obtainVerifyCode(request);

//        if (!validateParams(userName, userType)) {
//            //参数校验
//            throw new AuthenticationServiceException(SicherErrorCode.PARAM_ERROR.desc());
//        }

        return new AuthenticationParam().setUsername(userName.trim())
                .setPassword(password.trim());
//                .setUserType(UserTypeEnum.getUserTypeByCode(Integer.valueOf(userType)))
//                .setVerifyCode(verifyCode)
//                .setVerifyCodeKey(verifyCodeKey);
    }


    /**
     * 处理json请求体
     *
     * @param request
     * @return
     */
    private Map<String, String> parseJsonBody(HttpServletRequest request) {
        Map<String, String> map = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            // fixed 解决中文乱码的问题 HttpHelper.getBodyString(request)
            map = mapper.readValue(HttpHelper.getBodyString(request), HashMap.class);
        } catch (IOException e) {
            throw new KingException(KingCode.DEFAULT_EXCEPTION);
        }
        return map;
    }

    @Override
    protected String obtainPassword(HttpServletRequest request) {
        if (HttpTools.isJsonBody(request)) {
            return input.get(getPasswordParameter());
        }
        return super.obtainPassword(request);
    }

    @Override
    protected String obtainUsername(HttpServletRequest request) {
        if (HttpTools.isJsonBody(request)) {
            return input.get(getUsernameParameter());
        }
        return super.obtainUsername(request);
    }

    private String obtainCompanyType(HttpServletRequest request) {
        if (HttpTools.isJsonBody(request)) {
            return String.valueOf(input.get(SPRING_SECURITY_FORM_USER_TYPE_KEY));
        }
        return request.getParameter(SPRING_SECURITY_FORM_USER_TYPE_KEY);
    }


    private String obtainVerifyCodeKey(HttpServletRequest request) {
        if (HttpTools.isJsonBody(request)) {
            return input.get(SPRING_SECURITY_VERIFY_CODE_KEY_KEY);
        }
        return request.getParameter(SPRING_SECURITY_VERIFY_CODE_KEY_KEY);

    }

    private String obtainVerifyCode(HttpServletRequest request) {
        if (HttpTools.isJsonBody(request)) {
            return input.get(SPRING_SECURITY_VERIFY_CODE_KEY);
        }
        return request.getParameter(SPRING_SECURITY_VERIFY_CODE_KEY);

    }

    /**
     * 验证码验证
     *
     * @param key
     * @param captcha
     * @return
     */
    private boolean validateCaptcha(String key, String captcha) {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(captcha)) {
            return false;
        }
        return true;
    }

    /**
     * 参数验证
     *
     * @return
     */
    private boolean validateParams(String userName, String userType) {
        return validateUsernameParam(userName) && validateCompanyTypeParam(userType);
    }

    private boolean validateUsernameParam(String username) {
        if (StringUtils.isBlank(username)) {
            return false;
        }
        return true;
    }

    private boolean validateCompanyTypeParam(String type) {
        return type != null && type.matches("\\d+");
    }

}
