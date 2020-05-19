package com.mars.fw.encrypt;

/**
 * @Author King
 * @create 2020/4/23 14:04
 */
public interface EncryptComponent {


    /**
     * 加密
     *
     * @param source
     * @return
     * @throws EncryptException
     */
    String encrypt(String source) throws EncryptException;

    /**
     * 解密
     *
     * @param cipher
     * @return
     * @throws EncryptException
     */
    String decrypt(String cipher) throws EncryptException;

}
