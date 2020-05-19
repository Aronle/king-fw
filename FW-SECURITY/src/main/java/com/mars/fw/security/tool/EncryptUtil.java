package com.mars.fw.security.tool;


import com.mars.fw.security.tool.model.EncryptException;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Random;

public class EncryptUtil {

    /**
     * 密码加密
     *
     * @param aesPassword 基于AES加密后的密码
     * @return
     */
    public static String password(String aesPassword) throws EncryptException {
        return aesPassword;
    }

    /**
     * 密码加密
     * 加密规则：
     * 1. 先使用md5进行加密
     * 2. 拼接上盐值后，再使用md5进行加密
     *
     * @param password 密码
     * @param salt     盐值
     * @return String 加密后的数据
     */
    public static String password(String password, String salt) {
        return DigestUtils.md5Hex(DigestUtils.md5Hex(password) + salt);
    }

    /**
     * 生成token
     * token规则: 用户帐号拼接上当前时间戳并进行md5加密
     *
     * @param account 用户帐号
     * @return String
     */
    public static String token(String account) {
        return DigestUtils.md5Hex(account + String.valueOf(System.currentTimeMillis() / 1000));
    }

    /**
     * 生成16位的盐值
     *
     * @return
     */
    public static String salt() {
        Random random = new Random();
        StringBuilder sBuilder = new StringBuilder(16);
        sBuilder.append(random.nextInt(99999999)).append(random.nextInt(99999999));
        int len = sBuilder.length();
        if (len < 16) {
            for (int i = 0; i < 16 - len; i++) {
                sBuilder.append("0");
            }
        }
        return sBuilder.toString();
    }

}
