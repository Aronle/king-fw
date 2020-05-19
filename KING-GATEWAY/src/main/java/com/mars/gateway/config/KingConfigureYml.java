package com.mars.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author King
 * @create 2020/4/22 16:19
 */
@Data
@Component
@ConfigurationProperties(prefix = "king")
public class KingConfigureYml {

    private List<String> whiteIp;


}
