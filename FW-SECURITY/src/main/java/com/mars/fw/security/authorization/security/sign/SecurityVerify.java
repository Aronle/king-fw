package com.mars.fw.security.authorization.security.sign;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.mars.fw.security.authorization.security.HttpHelper;
import com.mars.fw.security.tool.HttpTools;
import com.mars.fw.security.tool.KingResponseUtils;
import com.mars.fw.web.reponse.King;
import com.mars.fw.web.reponse.KingCode;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * @author king
 */
public class SecurityVerify {

    /*****平台接口暂时未对外开放，密钥暂时只要设定一个所有的前端用这个密钥*****/
    public static String app_key = "yd_pay_app_key";

    /*****平台接口暂时未对外开放，密钥暂时只要设定一个所有的前端用这个密钥****/
    public static String secret_key = "7CF86A8786AE3BD74FE3E80286351C4F";

    /**
     * 签名验证
     * <p>
     * 1：请求身份 ---- 采用appKey开放的公钥  为每个开发者分配的公钥   secretKey 密钥  每个开发者能拿到密钥，密钥不参与传输，用来本地做签名算法
     * <p>
     * 2：防止篡改 ---- 参数签名
     * 2.1：按照请求参数名的字母升序排列非空请求参数（包含AccessKey），使用URL键值对的格式（即key1=value1&key2=value2…）拼接成字符串stringA
     * 2.2：在stringA最后拼接上Secretkey得到字符串stringSignTemp
     * 2.3：对stringSignTemp进行MD5运算，并将得到的字符串所有字符转换为大写，得到sign值。
     * <p>
     * 3：防止重复攻击 ----为了防止盗取一个正常的请求连接反复请求
     * 3.1：用随机数标记请求，作为请求的唯一身份标示，并存储，如果请求重复请求，则在存储中能拿到随机串，从而拒绝请求
     * 3.2：采用时间戳验证 服务端当前时间戳-timeOut>携带的时间戳
     * <p>
     * 4：身份验证：---- 采用token的无状态登录，为防止token被劫持，上述 1 2 3可以解决
     * 5：token刷新： ---- 用户无感知刷新token access_token  refresh_token 这个后续扩展
     *
     * @return
     */
    public static boolean checkSign(ServletRequest request, HttpServletRequest request2, HttpServletResponse response) throws IOException {

        Map<String, Object> paramMap = null;
        try {
            /************将参数转成 Map<String, Object>***************************/
            if (HttpTools.isJsonBody((HttpServletRequest) request)) {
                //ContentType 为 application/json
                //paramMap = JsonMapper.getDefault().fromJson(HttpHelper.getBodyString((HttpServletRequest) request), HashMap.class);
                String json = HttpHelper.getBodyString((HttpServletRequest) request);
                ObjectMapper mapper = new ObjectMapper();
                // 读取Json
                JsonNode rootNode = mapper.readTree(json);
                paramMap = dealJackSon(rootNode);
            } else {
                //表单提交
                Map<String, String[]> tempMap = request2.getParameterMap();
                paramMap = SignUtils.toVerifyMap(tempMap, false);
            }
            //获取验参的参数
            String requestSign = paramMap.get("sign").toString();
            String appKey = paramMap.get("app_key").toString();
            Long timestamp = Long.valueOf(paramMap.get("timestamp").toString());
            /************根据时间戳 判断url是否过期*********************************/
            Long timeOut = 60 * 1000L;
            if (System.currentTimeMillis() - timeOut > Long.valueOf(timestamp)) {
                King result = new King(KingCode.URL_EXPIRED);
                KingResponseUtils.writeData(response, result);
                return false;
            }
            /************判断签名*********************************/
            if (StringUtils.isBlank(requestSign) || StringUtils.isBlank(appKey)) {
                King result = new King(KingCode.DEFAULT_EXCEPTION.code(), "签名参数或appKey参数为空！", null);
                KingResponseUtils.writeData(response, result);
                return false;
            }

            String secretKey = secret_key;
            paramMap.put("secret_key", secretKey);
            /************根据参数和签名算法获得签名*********************************/
            String sign = SignUtils.createSign(paramMap);

            /************判断签名和接入者所传的签名是否一致*********************************/
            if (StringUtils.isNotBlank(sign) && sign.equalsIgnoreCase(requestSign)) {
                return true;
            }
            return false;
        } catch (Exception e) {
            //签名校验异常
            King result = new King(KingCode.DEFAULT_EXCEPTION.code(), "签名sign校验异常！", null);
            KingResponseUtils.writeData(response, result);
            return false;
        }

    }


    private static Map<String, Object> dealJackSon(JsonNode rootNode) throws IOException {
        if (null == rootNode) {
            return Maps.newHashMap();
        }
        Map<String, Object> map = Maps.newHashMap();
        if (rootNode.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> it = rootNode.fields();
            while (it.hasNext()) {
                Map.Entry<String, JsonNode> entry = it.next();
                if (entry.getValue().isArray()) {
                    map.put(entry.getKey(), entry.getValue().toString());
                } else {
                    map.put(entry.getKey(), entry.getValue().asText());
                }
            }
        }
        return map;
    }
}
