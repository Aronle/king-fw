package com.mars.fw.security.authorization.security.sign;


import com.google.common.collect.Maps;
import com.mars.fw.common.utils.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * 签名工具类
 *
 * @author king
 */
public class SignUtils {

    /**
     * 字符编码
     */
    private final static String INPUT_CHARSET = "UTF-8";

    /**
     * 超时时间
     */
    private final static int TIME_OUT = 30 * 1000;


    /**
     * 签名算法
     * 将参数按参数名首字母a-z拍列 再MD5加密
     *
     * @param params
     * @return
     */
    public static String createSignByMd5(Map<String, Object> params, String secretKey) {
        Set<String> paramNames = params.keySet();
        Object[] keys = paramNames.toArray();
        Arrays.sort(keys);
        StringBuffer sb = new StringBuffer();
        for (Object key : keys) {
            if ("secretKey".equals(key)
                    || "appKey".equals(key)
                    || "sign".equals(key)
                    || "timestamp".equals(key)) {
                continue;
            }
            sb.append(key)
                    .append("=")
                    .append(String.valueOf(params.get(key)));
        }
        return md5(sb.toString());
    }


    /**
     * 生成参数签名
     *
     * @param params
     * @return
     */
    public static String createSign(Map<String, Object> params) {

        /**参数排序***/
        Map<String, Object> sortMap = sortParams(params);
        /**参数过滤***/
        Map<String, Object> filterParam = filterParams(sortMap);
        /**参数拼接***/
        String urlParams = createParamStr(filterParam, false);

        return md5(urlParams);
    }


    /**
     * 请求参数Map转换验证Map
     *
     * @param requestParams 请求参数Map
     * @param charset       是否要转utf8编码
     * @return
     * @throws UnsupportedEncodingException
     */
    public static Map<String, Object> toVerifyMap(Map<String, String[]> requestParams, boolean charset) {
        Map<String, Object> params = new HashMap<>();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = requestParams.get(name);
            Object valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            if (charset) {
                valueStr = getContentString((String) valueStr, INPUT_CHARSET);
            }
            params.put(name, valueStr);
        }
        return params;
    }


    /**
     * 编码转换
     *
     * @param content
     * @param charset
     * @return
     */
    private static String getContentString(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return new String(content.getBytes());
        }
        try {
            return new String(content.getBytes("ISO-8859-1"), charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }


    /**
     * 将参数按参数名首字母a-z排序
     *
     * @param params
     * @return
     */
    public static Map<String, Object> sortParams(Map<String, Object> params) {

        Set<String> paramNames = params.keySet();
        Object[] keys = paramNames.toArray();
        Arrays.sort(keys);

        Map<String, Object> sortmap = Maps.newLinkedHashMap();
        for (Object key : keys) {
            sortmap.put((String) key, params.get(key));
        }
        return sortmap;
    }


    /**
     * 除去参数为空和sign的参数
     *
     * @param params
     * @return
     */
    public static Map<String, Object> filterParams(Map<String, Object> params) {
        Map<String, Object> result = Maps.newLinkedHashMap();
        for (String key : params.keySet()) {
            if ("null".equals(params.get(key))) {
                continue;
            }
            if (null == params.get(key)) {
                continue;
            }
            String temp = params.get(key).toString();
            if (StringUtils.isBlank(temp)) {
                continue;
            }
            if ("sign".equalsIgnoreCase(key)) {
                continue;
            }
            result.put(key, temp);
        }
        return result;
    }


    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param params 需要排序并参与字符拼接的参数组
     * @param encode 是否需要UrlEncode
     * @return 拼接后字符串
     */
    public static String createParamStr(Map<String, Object> params, boolean encode) {
        List<String> keys = new ArrayList<>(params.keySet());
        Collections.sort(keys);
        String result = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = (String) params.get(key);
            if (encode) {
                value = urlEncode(value, INPUT_CHARSET);
            }
            //拼接时，不包括最后一个&字符
            if (i == keys.size() - 1) {
                result = result + key + "=" + value;
            } else {
                result = result + key + "=" + value + "&";
            }
        }
        return result;
    }


    /**
     * URL转码
     *
     * @param content
     * @param charset
     * @return
     */
    private static String urlEncode(String content, String charset) {
        try {
            return URLEncoder.encode(content, charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }


    /**
     * MD5加密
     *
     * @param content 加密内容
     * @return
     */
    public static String md5(String content) {
        return DigestUtils.md5Hex(content);
    }


    /**
     * Md5校验
     *
     * @param content
     * @param secretKey
     * @param md5
     * @return
     */
    public static boolean checkMd5(String content, String secretKey, String md5) {
        String miwen = md5(content);
        if (miwen.equals(md5)) {
            return true;
        }
        return false;
    }
}
