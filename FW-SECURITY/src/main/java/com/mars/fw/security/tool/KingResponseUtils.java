package com.mars.fw.security.tool;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * http工具类
 *
 * @author king
 * @date 2020-04-19
 */
public class KingResponseUtils {

    public static final String X_REQUESTED_WITH = "X-Requested-With";

    public static final String XML_HTTP_REQUEST = "XMLHttpRequest";

    /**
     * 判断是否ajax请求
     *
     * @param request
     * @return
     */
    public static boolean isXMLHttpRequest(HttpServletRequest request) {
        if (XML_HTTP_REQUEST.equals(request.getHeader(X_REQUESTED_WITH))) {
            return true;
        }
        return false;
    }

    /**
     * response响应数据写入
     *
     * @param response
     * @param resultObj
     * @throws IOException
     */
    public static void writeData(HttpServletResponse response, Object resultObj) throws IOException {
        // 允许跨域
        response.setHeader("Access-Control-Allow-Origin", "*");
        // 允许自定义请求头token(允许head跨域)
        response.setHeader("Access-Control-Allow-Headers", "Authorization_token, Accept, Origin, X-Requested-With, Content-Type, Last-Modified");
        // 设置编码
        response.setCharacterEncoding("UTF-8");
        // 设置数据类型
        response.setContentType("text/json");
        String result = new ObjectMapper().writeValueAsString(resultObj);
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.write(result);
            out.flush();
        } catch (IOException e) {
            throw e;
        } finally {
            if (null != out) {
                out.close();
            }
        }
    }
}
