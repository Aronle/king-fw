package com.mars.fw.common.utils.http;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class HttpClientUtil {


    private static PoolingHttpClientConnectionManager connMgr;

    private static final int TIME_OUT = 20 * 1000;

    private static RequestConfig requestConfig;


    static {
        // 设置连接池
        connMgr = new PoolingHttpClientConnectionManager();
        // 设置连接池大小
        connMgr.setMaxTotal(100);
        connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal());

        RequestConfig.Builder configBuilder = RequestConfig.custom();
        // 设置连接超时
        configBuilder.setConnectTimeout(TIME_OUT);
        // 设置读取超时
        configBuilder.setSocketTimeout(TIME_OUT);
        // 设置从连接池获取连接实例的超时
        configBuilder.setConnectionRequestTimeout(TIME_OUT);
        // 在提交请求之前 测试连接是否可用
        configBuilder.setStaleConnectionCheckEnabled(true);
        requestConfig = configBuilder.build();
    }

    /**
     * 设置头部
     *
     * @param httpRequestBase
     * @param headerMap
     */
    private static void setHeader(HttpRequestBase httpRequestBase, Map<String, String> headerMap) {
        if (null != headerMap) {
            for (Map.Entry<String, String> header : headerMap.entrySet()) {
                httpRequestBase.setHeader(header.getKey(), header.getValue());
            }
        }
    }

    /**
     * GET 请求
     *
     * @param url
     * @param paramMap
     * @return
     * @throws IOException
     */
    public static String get(String url, Map<String, Object> paramMap) throws IOException {
        HttpResponse httpResponse = getResponse(url, paramMap, null);
        return getContent(httpResponse);
    }

    /**
     * GET 请求
     *
     * @param url
     * @param paramMap
     * @param headerMap
     * @return
     * @throws IOException
     */
    public static String get(String url, Map<String, Object> paramMap, Map<String, String> headerMap) throws IOException {
        HttpResponse httpResponse = getResponse(url, paramMap, headerMap);
        return getContent(httpResponse);
    }

    /**
     * GET 请求
     *
     * @param url
     * @param paramMap
     * @return
     * @throws IOException
     */
    public static HttpResult getResult(String url, Map<String, Object> paramMap) throws IOException {
        return getResult(url, paramMap, null);
    }

    /**
     * GET 请求
     *
     * @param url
     * @param paramMap
     * @param headerMap
     * @return
     * @throws IOException
     */
    public static HttpResult getResult(String url, Map<String, Object> paramMap, Map<String, String> headerMap) throws IOException {
        HttpResponse httpResponse = getResponse(url, paramMap, headerMap);
        return getResult(httpResponse);
    }

    /**
     * POST 请求 ：application/x-www-form-urlencoded
     *
     * @param url
     * @param paramMap
     * @return
     * @throws IOException
     */
    public static String post(String url, Map<String, Object> paramMap) throws IOException {
        HttpResponse httpResponse = postResponse(url, paramMap, null);
        return getContent(httpResponse);
    }

    /**
     * POST 请求 ：application/x-www-form-urlencoded
     *
     * @param url
     * @param paramMap
     * @return
     * @throws IOException
     */
    public static String post(String url, Map<String, Object> paramMap, Map<String, String> headerMap) throws IOException {
        HttpResponse httpResponse = postResponse(url, paramMap, headerMap);
        return getContent(httpResponse);
    }

    /**
     * POST 请求 ：application/x-www-form-urlencoded
     *
     * @param url
     * @param paramMap
     * @return
     * @throws IOException
     */
    public static HttpResult postResult(String url, Map<String, Object> paramMap) throws IOException {
        return postResult(url, paramMap, null);
    }

    /**
     * POST 请求 ：application/x-www-form-urlencoded
     *
     * @param url
     * @param paramMap
     * @param headerMap
     * @return
     * @throws IOException
     */
    public static HttpResult postResult(String url, Map<String, Object> paramMap, Map<String, String> headerMap) throws IOException {
        HttpResponse httpResponse = postResponse(url, paramMap, headerMap);
        return getResult(httpResponse);
    }

    /**
     * POST 请求 ：application/json
     *
     * @param url
     * @param data
     * @return
     * @throws IOException
     */
    public static String post(String url, String data) throws IOException {
        HttpResponse httpResponse = postResponse(url, data, null, null);
        return getContent(httpResponse);
    }

    /**
     * POST 请求 ：application/json
     *
     * @param url
     * @param data
     * @param paramMap
     * @return
     * @throws IOException
     */
    public static String post(String url, String data, Map<String, Object> paramMap) throws IOException {
        HttpResponse httpResponse = postResponse(url, data, paramMap, null);
        return getContent(httpResponse);
    }

    /**
     * POST 请求 ：application/json
     *
     * @param url
     * @param data
     * @param paramMap
     * @param headerMap
     * @return
     * @throws IOException
     */
    public static String post(String url, String data, Map<String, Object> paramMap, Map<String, String> headerMap) throws IOException {
        HttpResponse httpResponse = postResponse(url, data, paramMap, headerMap);
        return getContent(httpResponse);
    }

    /**
     * POST 请求 ：application/json
     *
     * @param url
     * @param data
     * @return
     * @throws IOException
     */
    public static HttpResult postResult(String url, String data) throws IOException {
        return postResult(url, data, null, null);
    }

    /**
     * POST 请求 ：application/json
     *
     * @param url
     * @param data
     * @param paramMap
     * @return
     * @throws IOException
     */
    public static HttpResult postResult(String url, String data, Map<String, Object> paramMap) throws IOException {
        return postResult(url, data, paramMap, null);
    }

    /**
     * POST 请求 ：application/json
     *
     * @param url
     * @param data
     * @param paramMap
     * @param headerMap
     * @return
     * @throws IOException
     */
    public static HttpResult postResult(String url, String data, Map<String, Object> paramMap, Map<String, String> headerMap) throws IOException {
        HttpResponse httpResponse = postResponse(url, data, paramMap, headerMap);
        return getResult(httpResponse);
    }

    /**
     * GET 请求
     *
     * @param url
     * @param paramMap
     * @param headerMap
     * @return
     * @throws IOException
     */
    private static HttpResponse getResponse(String url, Map<String, Object> paramMap, Map<String, String> headerMap) throws IOException {
        CloseableHttpClient httpClient = getHttpClient(url);
        HttpGet httpGet = new HttpGet(url + assembleParamUrl(paramMap));
        httpGet.setConfig(requestConfig);
        setHeader(httpGet, headerMap);
        return httpClient.execute(httpGet);
    }

    /**
     * POST 请求 ：application/json
     *
     * @param url
     * @param data
     * @param headerMap
     * @return
     * @throws IOException
     */
    private static HttpResponse postResponse(String url, String data, Map<String, Object> paramMap, Map<String, String> headerMap) throws IOException {
        CloseableHttpClient closeableHttpClient = getHttpClient(url);
        StringEntity entity = new StringEntity(data, "UTF-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        HttpPost httpPost = new HttpPost(url + assembleParamUrl(paramMap));
        httpPost.setConfig(requestConfig);
        setHeader(httpPost, headerMap);
        httpPost.setEntity(entity);
        return closeableHttpClient.execute(httpPost);
    }

    /**
     * POST 请求 ：application/x-www-form-urlencoded
     *
     * @param url
     * @param paramMap
     * @return
     * @throws IOException
     */
    private static HttpResponse postResponse(String url, Map<String, Object> paramMap, Map<String, String> headerMap) throws IOException {
        CloseableHttpClient closeableHttpClient = getHttpClient(url);
        List<NameValuePair> nvps = Lists.newArrayList();
        Set<Map.Entry<String, Object>> entrySet = paramMap.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            if (null != entry.getValue()) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
            }
        }
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nvps, "UTF-8");
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        setHeader(httpPost, headerMap);
        httpPost.setEntity(entity);
        return closeableHttpClient.execute(httpPost);
    }

    /**
     * 获取 http 请求回调对象
     *
     * @param httpResponse
     * @return
     * @throws IOException
     */
    private static HttpResult getResult(HttpResponse httpResponse) throws IOException {
        HttpResult result = new HttpResult();
        result.setHeaders(getHeaders(httpResponse));
        result.setContent(getContent(httpResponse));
        return result;
    }

    /**
     * 获取头部
     *
     * @param httpResponse
     * @return
     */
    private static Map<String, String> getHeaders(HttpResponse httpResponse) {
        Map<String, String> headers = Maps.newHashMap();
        Header[] allHeaders = httpResponse.getAllHeaders();
        for (Header header : allHeaders) {
            headers.put(header.getName(), header.getValue());
        }
        return headers;
    }

    /**
     * 获取消息体
     *
     * @param httpResponse
     * @return
     * @throws IOException
     */
    private static String getContent(HttpResponse httpResponse) throws IOException {
        String msg;
        try {
            msg = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
        } catch (IOException e) {
            throw e;
        } finally {
            closeClient(httpResponse);
        }
        return msg;
    }

    /**
     * 释放资源
     *
     * @param httpResponse
     */
    private static void closeClient(HttpResponse httpResponse) {
        if (null != httpResponse && httpResponse instanceof CloseableHttpClient) {
            try {
                ((CloseableHttpClient) httpResponse).close();
            } catch (IOException e) {
                log.error("CloseableHttpClient资源释放失败！", e);
            }
        }
    }

    /**
     * 组装参数 Uri
     *
     * @param paramMap
     * @return
     */
    private static String assembleParamUrl(Map<String, Object> paramMap) {
        StringBuffer sb = new StringBuffer();
        if (null != paramMap) {
            for (Map.Entry<String, Object> param : paramMap.entrySet()) {
                if (0 == sb.length()) {
                    sb.append("?");
                } else {
                    sb.append("&");
                }
                sb.append(param.getKey()).append("=").append(param.getValue());
            }
        }
        return sb.toString();
    }

    private static CloseableHttpClient getHttpClient(String url) {
        CloseableHttpClient closeableHttpClient = null;
        if (url.indexOf("https") > 0) {
            closeableHttpClient = HttpClients.custom()
                    .setSSLSocketFactory(createSSLFactory())
                    .setConnectionManager(connMgr)
                    .setDefaultRequestConfig(requestConfig).build();
        } else {
            closeableHttpClient = HttpClients.createDefault();
        }
        return closeableHttpClient;
    }

    /**
     * 创建安全链接
     *
     * @return
     */
    private static SSLConnectionSocketFactory createSSLFactory() {
        SSLConnectionSocketFactory sslConnectionSocketFactory = null;
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, new X509HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }

                @Override
                public void verify(String host, SSLSocket ssl) throws IOException {
                }

                @Override
                public void verify(String host, X509Certificate cert) throws SSLException {
                }

                @Override
                public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
                }
            });
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return sslConnectionSocketFactory;
    }


}
