package com.mars.fw.encrypt.rsa;

import com.mars.fw.common.utils.RsaUtil;
import com.mars.fw.common.utils.StringUtils;
import com.mars.fw.encrypt.EncryptComponent;
import com.mars.fw.encrypt.EncryptException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * RSA 加密组件
 *
 * @Author King
 * @create 2020/4/23 14:27
 */
@Service
public class RsaEncryptComponent implements EncryptComponent {

    @Autowired
    private RsaEncryptConfigure configure;

    @Override
    public String encrypt(String source) throws EncryptException {
        if (StringUtils.isBlank(configure.getPublicKey()) || StringUtils.isBlank(configure.getPrivateKey())) {
            throw new EncryptException("Rsa 的publicKey或privateKey 没有配置");
        }
        try {
            return RsaUtil.encrypt(source, configure.getPublicKey());
        } catch (Exception e) {
            throw new EncryptException("Rsa加密失败:" + e.getMessage());
        }
    }

    @Override
    public String decrypt(String cipher) throws EncryptException {
        if (StringUtils.isBlank(configure.getPublicKey()) || StringUtils.isBlank(configure.getPrivateKey())) {
            throw new EncryptException("Rsa 的publicKey或privateKey 没有配置");
        }
        try {
            return RsaUtil.decrypt(cipher, configure.getPrivateKey());
        } catch (Exception e) {
            throw new EncryptException("Rsa解密失败:" + e.getMessage());
        }
    }
}
