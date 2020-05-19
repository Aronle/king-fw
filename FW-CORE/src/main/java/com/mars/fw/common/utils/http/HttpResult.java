package com.mars.fw.common.utils.http;


import java.util.Map;

public class HttpResult {
    private static final long serialVersionUID = -1467362277413807359L;

    /**
     * 头部信息
     */
    private Map<String, String> headers;

    /**
     * 内容信息
     */
    private String content;

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
