package com.mars.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

/**
 * 自定义工厂 可以自定义过滤器的工厂来指定网关中过滤器的配置在yml简单配置
 * <p>
 * <p>
 * ps:过滤器工厂的顶级接口是 GatewayFilterFactory，
 * 我们可以直接继承它的两个抽象类来简化开发 AbstractGatewayFilterFactory 和 AbstractNameValueGatewayFilterFactory，
 * 这两个抽象类的区别就是前者接收一个参数（像 StripPrefix 和我们创建的这种），后者接收两个参数（像 AddResponseHeader）。
 *
 * @Author King
 * @create 2020/5/12 15:21
 */
@Slf4j
@Component
public class PrintRequestLogGatewayFilterFactory extends AbstractGatewayFilterFactory<PrintRequestLogGatewayFilterFactory.Config> {


    /**
     * isLog key.
     */
    public static final String IS_LOG = "isLog";


    private static final String API_START_TIME = "apiStartTime";


    public PrintRequestLogGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList(IS_LOG);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            exchange.getAttributes().put(API_START_TIME, System.currentTimeMillis());
            return chain.filter(exchange).then(
                    Mono.fromRunnable(() -> {
                        Long startTime = exchange.getAttribute(API_START_TIME);
                        if (startTime != null && config.isLog()) {
                            StringBuilder sb = new StringBuilder(exchange.getRequest().getURI().getRawPath())
                                    .append(": ")
                                    .append(System.currentTimeMillis() - startTime)
                                    .append("ms");
                            log.info(sb.toString());
                        }
                    })
            );
        });
    }

    public static class Config {
        private boolean isLog;

        public boolean isLog() {
            return isLog;
        }

        public void setLog(boolean log) {
            this.isLog = log;
        }
    }

}
