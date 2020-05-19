package com.mars.fw;

import com.mars.fw.web.reponse.handler.MarsRespValueHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.DeferredResultMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author King
 * @Date 2020-04-20
 */
@Configuration
@EnableAspectJAutoProxy
@EnableAutoConfiguration
public class MarsWebMvcConfigure implements WebMvcConfigurer {

    /**
     * 实例请求映射处理适配器
     */
    @Autowired
    private RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    /**
     * 解决跨域问题
     * <p>
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                .maxAge(3600);
    }

    private CorsConfiguration addcorsConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        List<String> list = new ArrayList<>();
        list.add("*");
        corsConfiguration.setAllowedOrigins(list);

        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        return corsConfiguration;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", addcorsConfig());
        return new CorsFilter(source);
    }

    /**
     * mvc 静态资源路径访问权限配置
     * 例如:registry.addResourceHandler("/upload/**").addResourceLocations("classpath:/upload/");
     * /upload/ 路径下的静态资源可以访问
     * <p>
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //所有目录都可以访问
        registry.addResourceHandler("/static/**").
                addResourceLocations("file:/usr/share/nginx/images/");

        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }


    /**
     * @PostConstruct 非静态的void()方法  尽量少在复杂的逻辑方法上注解这个 影响启动速度
     * <p>Tomcat6.x 以上
     * 服务器加载Servlet的时候运行，并且只会被服务器执行一次
     * <p>
     * 服务器加载Servlet的时候运行 执行实例请求映射处理适配器下自定义的参数拦截统一处理 返回标准数据结构
     */
    @PostConstruct
    public void initResponseValue() {
        final List<HandlerMethodReturnValueHandler> originalHandlers = new ArrayList<>(requestMappingHandlerAdapter.getReturnValueHandlers());

        RequestResponseBodyMethodProcessor requestResponseBodyMethodProcessor = null;
        for (int i = 0; i < originalHandlers.size(); i++) {
            final HandlerMethodReturnValueHandler valueHandler = originalHandlers.get(i);
            if (RequestResponseBodyMethodProcessor.class.isAssignableFrom(valueHandler.getClass())) {
                requestResponseBodyMethodProcessor = (RequestResponseBodyMethodProcessor) valueHandler;
                break;
            }
        }
        MarsRespValueHandler marsRespValueHandler = new MarsRespValueHandler(requestResponseBodyMethodProcessor);

        final int deferredPos = obtainValueHandlerPosition(originalHandlers, DeferredResultMethodReturnValueHandler.class);
        originalHandlers.add(deferredPos + 1, marsRespValueHandler);
        requestMappingHandlerAdapter.setReturnValueHandlers(originalHandlers);
    }

    private int obtainValueHandlerPosition(final List<HandlerMethodReturnValueHandler> originalHandlers, Class<?> handlerClass) {
        for (int i = 0; i < originalHandlers.size(); i++) {
            final HandlerMethodReturnValueHandler valueHandler = originalHandlers.get(i);
            if (handlerClass.isAssignableFrom(valueHandler.getClass())) {
                return i;
            }
        }
        return -1;
    }


}
