package com.mars.gateway.filter;

import com.mars.gateway.config.KingConfigureYml;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 网关全局过滤器
 *
 * @Author King
 * @create 2020/5/8 17:34
 */
@Component
public class KingGlobalFilter implements GlobalFilter, Ordered {

    private static final String POST = "POST";
    private static final String GET = "GET";
    private static final String DH = ",";

    @Autowired
    private KingConfigureYml kingConfigureYml;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();



        ServerHttpResponse response = exchange.getResponse();
        if (POST.equals(request.getMethodValue())) {
            //todo 白名单校验
            List<String> whiteIps = kingConfigureYml.getWhiteIp();
            String ip = getIpAddress(request);
            if (!CollectionUtils.isEmpty(whiteIps) && whiteIps.contains(ip)) {
                return chain.filter(exchange);
            }
            //todo token 校验
//            String token = request.getHeaders().getFirst("token");
//            if (StringUtils.isBlank(token)) {
//                response.setStatusCode(HttpStatus.UNAUTHORIZED);
//                return response.setComplete();
//            }


            //todo 参数加密 获取body体
            //todo 对接口参数想干嘛干嘛 这边可以加签 也可以加解密
            Object object = exchange.getAttribute("cachedRequestBodyObject");
            if (null == object) {
                return chain.filter(exchange);
            }
            //todo 签名
            return chain.filter(exchange.mutate().request(request).build());
        } else if (GET.equals(request.getMethod())) {
            //获取GET方法的参数
            Map requestQueryParams = request.getQueryParams();
            //todo 对接口参数想干嘛干嘛
            return chain.filter(exchange);

        }
        return chain.filter(exchange);
    }


    /**
     * 获取请求中的Ip地址
     *
     * @param request
     * @return
     */
    private static String getIpAddress(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        String ip = headers.getFirst("x-forwarded-for");
        String unKnow = "unknown";
        if (StringUtils.isNotBlank(ip) && !unKnow.equalsIgnoreCase(ip)) {
            if (ip.contains(DH)) {
                ip = ip.split(DH)[0];
            }
        }
        if (StringUtils.isBlank(ip) || unKnow.equalsIgnoreCase(ip)) {
            ip = headers.getFirst("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || unKnow.equalsIgnoreCase(ip)) {
            ip = headers.getFirst("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || unKnow.equalsIgnoreCase(ip)) {
            ip = headers.getFirst("HTTP_CLIENT_IP");
        }
        if (StringUtils.isBlank(ip) || unKnow.equalsIgnoreCase(ip)) {
            ip = headers.getFirst("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isBlank(ip) || unKnow.equalsIgnoreCase(ip)) {
            ip = headers.getFirst("X-Real-IP");
        }
        if (StringUtils.isBlank(ip) || unKnow.equalsIgnoreCase(ip)) {
            ip = ((InetSocketAddress) Objects.requireNonNull(request.getRemoteAddress())).getAddress().getHostAddress();
        }
        return ip;
    }


    @Override
    public int getOrder() {
        return -100;
    }
}
