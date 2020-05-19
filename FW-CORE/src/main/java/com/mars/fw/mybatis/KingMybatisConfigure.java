package com.mars.fw.mybatis;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis plus的一些基础配置
 * 以bean的开启方式
 *
 * @Author King
 * @create 2020/4/21 14:09
 */
@Configuration
public class KingMybatisConfigure {

    /**
     * mybatis plus 分页器插件的注入
     *
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor interceptor = new PaginationInterceptor();
        interceptor.setDialectType("mysql");
        return interceptor;
    }


    /**
     * mybatis plus 性能分析器插件 会打印出sql和执行的时间
     *
     * @return
     */
    @Bean
    public PerformanceInterceptor performanceInterceptor() {
        PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
        //sql格式化
        performanceInterceptor.setFormat(true);
        return performanceInterceptor;
    }

}
