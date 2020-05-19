package com.mars.fw.security.tool.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @description:
 * @author: aron
 * @date: 2019-05-21 10:38
 */
@Setter
@Getter
@Accessors(chain = true)
public class AuthenticationParam {

    private String username;
    private String password;
    private UserTypeEnum userType;
    private Integer platform;
    private String verifyCodeKey;
    private String verifyCode;
}
