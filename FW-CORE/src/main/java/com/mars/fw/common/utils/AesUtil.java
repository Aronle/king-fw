package com.mars.fw.common.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * Created by bootvue@gmail.com
 * Date 2019-04-16 14:24
 * <p>
 * AES加密 解密
 * CBC模式, 构造方法要提供16位的key与iv
 */
public class AesUtil {
    /**
     * 加密
     */
    public static String encrypt(String origin, String key, String iv) throws Exception {
        return aes(origin, key, iv, Cipher.ENCRYPT_MODE);
    }

    /**
     * 解密
     *
     * @param cipher
     * @param key
     * @param iv
     * @return
     * @throws Exception
     */
    public static String decrypt(String cipher, String key, String iv) throws Exception {
        return aes(cipher, key, iv, Cipher.DECRYPT_MODE);
    }

    /**
     * 加解密执行逻辑
     *
     * @param content
     * @param key
     * @param iv
     * @param type
     * @return
     * @throws Exception
     */
    private static String aes(String content, String key, String iv, int type) throws Exception {
        byte[] raw = key.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        /**
         * 使用CBC模式，需要一个向量iv，可增加加密算法的强度
         */
        IvParameterSpec iv_ = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(type, skeySpec, iv_);
        if (Cipher.ENCRYPT_MODE == type) {
            byte[] encrypted = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
            /**
             * 此处使用BASE64做转码。
             */
            return new String(Base64.encodeBase64(encrypted));
        }
        byte[] original = cipher.doFinal(Base64.decodeBase64(content));
        return new String(original, StandardCharsets.UTF_8);
    }
}
