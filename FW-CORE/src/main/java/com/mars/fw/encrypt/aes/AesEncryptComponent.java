package com.mars.fw.encrypt.aes;

import com.mars.fw.common.utils.AesUtil;
import com.mars.fw.common.utils.StringUtils;
import com.mars.fw.encrypt.EncryptComponent;
import com.mars.fw.encrypt.EncryptException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * AES 加解密组件
 *
 * @Author King
 * @create 2020/4/23 14:05
 */
@Service
public class AesEncryptComponent implements EncryptComponent {

    @Autowired
    private AesEncryptConfigure configure;

    @Override
    public String encrypt(String source) throws EncryptException {
        if (StringUtils.isBlank(configure.getKey()) || StringUtils.isBlank(configure.getIv())) {
            throw new EncryptException("Aes 的Key或Iv 没有配置");
        }
        try {
            return AesUtil.encrypt(source, configure.getKey(), configure.getIv());
        } catch (Exception e) {
            throw new EncryptException("Aes加密失败:" + e.getMessage());
        }
    }

    @Override
    public String decrypt(String cipher) throws EncryptException {
        if (StringUtils.isBlank(configure.getKey()) || StringUtils.isBlank(configure.getIv())) {
            throw new EncryptException("Aes 的Key或Iv 没有配置");
        }
        try {
            return AesUtil.decrypt(cipher, configure.getKey(), configure.getIv());
        } catch (Exception e) {
            throw new EncryptException("Aes加密失败:" + e.getMessage());
        }
    }
}
