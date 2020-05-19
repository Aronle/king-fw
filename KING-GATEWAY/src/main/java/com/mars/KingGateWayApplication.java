package com.mars;

import com.mars.gateway.filter.PrintRequestLogGatewayFilterFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Mono;

/**
 * @author king
 */
@Slf4j
@SpringCloudApplication
@EnableDiscoveryClient
public class KingGateWayApplication {

    public static void main(String[] args) {
        SpringApplication.run(KingGateWayApplication.class, args);
    }

    @Bean
    public KeyResolver ipAddressKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getHostName());
    }

    @Bean
    public KeyResolver userKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getQueryParams().getFirst("userId"));
    }

    /**
     * 路由配置
     *
     * @param builder
     * @return
     */
    @Bean
    public RouteLocator activityRouter(RouteLocatorBuilder builder) {
        RouteLocatorBuilder.Builder routes = builder.routes();
        RouteLocatorBuilder.Builder serviceProvider = routes
                /**
                 * 路由配置
                 */
                .route("king-gate",
                        r -> r.readBody(Object.class, requestBody -> {
                            log.info("requestBody is {}", requestBody);
                            // 这里 r.readBody做了一个前置语言，这样就可以在filter中通过exchange.getAttribute("cachedRequestBodyObject"); 获取body体
                            return true;
                        }).and().path("/king/**").filters(x -> x.hystrix(y -> y.setFallbackUri("forward:/fallback/web")).stripPrefix(1)).uri("lb://king-dev"))

                .route("king-other",
                        r -> r.readBody(Object.class, requestBody -> {
                            return true;
                        }).and().path("/kingapp/**").filters(x -> x.hystrix(y -> y.setFallbackUri("forward:/fallback/app")).stripPrefix(1)).uri("lb://king-app"))


                /**
                 * burstCapacity：令牌桶总容量。
                 * replenishRate：令牌桶每秒填充平均速率。
                 * key-resolver：用于限流的键的解析器的 Bean 对象的名字。它使用 SpEL 表达式根据#{@beanName}从 Spring 容器中获取 Bean 对象。
                 */
                .route("king-limiter",
                        r -> r.readBody(Object.class, requestBody -> {
                            return true;
                        }).and().path("/kinglimiter/**")
                                .filters(x -> x
                                        .hystrix(y -> y.setFallbackUri("forward:/fallback/app"))
                                        .stripPrefix(1)
                                        .requestRateLimiter(k -> {
                                            k.setKeyResolver(ipAddressKeyResolver()).setRateLimiter(new RedisRateLimiter(1, 3));
                                            k.setKeyResolver(userKeyResolver()).setRateLimiter(new RedisRateLimiter(1, 3));
                                        }))
                                .uri("lb://king-dev"));

        RouteLocator routeLocator = serviceProvider.build();
        log.info("ActivityServiceRouter is loading ... {}", routeLocator);
        return routeLocator;
    }

    @Bean
    public PrintRequestLogGatewayFilterFactory printRequestLogGatewayFilterFactory() {
        return new PrintRequestLogGatewayFilterFactory();
    }


}
