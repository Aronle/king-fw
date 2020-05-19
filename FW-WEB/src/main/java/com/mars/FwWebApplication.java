package com.mars;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.net.URL;
import java.util.Map;

/**
 * @Author King
 * @create 2020/4/20 12:22
 */
@Slf4j
@SpringCloudApplication
public class FwWebApplication {

    public static void main(String[] args) {
        String name = "King-gateway";
        try {
            Yaml yaml = new Yaml();
            URL bootstrap = FwWebApplication.class.getClassLoader().getResource("bootstrap.yml");
            if (bootstrap != null) {
                InputStream resourceAsStream = FwWebApplication.class.getClassLoader().getResourceAsStream("bootstrap.yml");
                Map map = (Map) yaml.load(resourceAsStream);
                Map spring = (Map) map.get("spring");
                Map application = (Map) spring.get("application");
                name = (String) application.get("name");
            }
            log.error("服务以{}启动", name);
            SpringApplication.run(FwWebApplication.class, args);
        } catch (Exception e) {
            log.error("[" + name + "]启动异常", e);
            System.exit(1);
        }
    }

}
