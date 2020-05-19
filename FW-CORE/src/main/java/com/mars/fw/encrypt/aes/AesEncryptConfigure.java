package com.mars.fw.encrypt.aes;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author King
 * @create 2020/4/23 14:01
 */
@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "encrypt.aes", ignoreInvalidFields = true)
public class AesEncryptConfigure {

    private String key;
    private String iv;

}
