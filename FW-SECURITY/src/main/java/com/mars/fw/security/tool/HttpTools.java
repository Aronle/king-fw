package com.mars.fw.security.tool;

import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;

/**
 * @description:
 * @author: aron
 * @date: 2019-05-27 13:48
 */
public class HttpTools {

    /**
     * 判断请求体是否为json数据
     *
     * @param request
     * @return
     */
    public static boolean isJsonBody(HttpServletRequest request) {
        return request.getContentType().equals(MediaType.APPLICATION_JSON_UTF8_VALUE)
                || request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE);
    }

}
