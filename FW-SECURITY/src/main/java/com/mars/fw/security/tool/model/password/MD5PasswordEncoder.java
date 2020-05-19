package com.mars.fw.security.tool.model.password;


import com.mars.fw.security.tool.EncryptUtil;
import com.mars.fw.security.tool.model.EncryptException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class MD5PasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        return rawPassword.toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        try {
            return encodedPassword.equals(EncryptUtil.password(rawPassword.toString()));
        } catch (EncryptException e) {
            return false;
        }

    }
}
