package com.mars.fw.security.tool.model;

import lombok.Data;

/**
 * @description:
 * @author: aron
 * @date: 2019-07-03 14:06
 */
@Data
public class AuthResponseModel {

    private Long userId;
    private Integer userType;
    private String userName;
    private String password;
    private String phone;
    private String email;
    private String nickName;
    private Integer sex;
    private Integer admin;
    private Integer status;
    private Integer deleted;
    private Long createAt;
    private Long companyId;
    private Long organId;
    private Long agentId;
}
