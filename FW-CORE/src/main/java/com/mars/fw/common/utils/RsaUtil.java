package com.mars.fw.common.utils;

import com.mars.fw.encrypt.EncryptException;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @Author King
 * @create 2020/4/23 14:51
 */
public class RsaUtil {


    /**
     * 加密
     *
     * @param source
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static String encrypt(String source, String publicKey) throws Exception {
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory
                .getInstance("RSA")
                .generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = Base64.encodeBase64String(cipher.doFinal(source.getBytes("UTF-8")));
        return outStr;
    }

    /**
     * 解密
     *
     * @param cipher
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static String decrypt(String cipher, String privateKey) throws Exception {

        //64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(cipher.getBytes("UTF-8"));
        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher str = Cipher.getInstance("RSA");
        str.init(Cipher.DECRYPT_MODE, priKey);
        String outStr = new String(str.doFinal(inputByte));
        return outStr;
    }
}
