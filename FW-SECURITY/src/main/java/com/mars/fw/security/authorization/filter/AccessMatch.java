package com.mars.fw.security.authorization.filter;

import javax.servlet.http.HttpServletRequest;


public interface AccessMatch {
    /**
     * 是否允许通过
     *
     * @param request
     * @return
     */
    boolean isAccessUri(HttpServletRequest request);

    /**
     * 加签白名单
     *
     * @param request
     * @return
     */
    boolean isAccessSignUri(HttpServletRequest request);
}
