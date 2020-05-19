package com.mars.fw.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author King
 * @create 2020/4/22 16:19
 */
@Data
@Component
@ConfigurationProperties(prefix = "king")
public class KingConfigure {

    private boolean log;


}
