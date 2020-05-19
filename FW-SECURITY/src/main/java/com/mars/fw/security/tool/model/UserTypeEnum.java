package com.mars.fw.security.tool.model;

/**
 * @description:
 * @author: aron
 * @date: 2019-07-01 16:27
 */
public enum UserTypeEnum {

    /**
     *
     */
    admin(1000, "运营管理员", "运营用户"),
    organ(1001, "机构管理员", "运营用户"),
    agent(1002, "代理管理员", "运营用户"),
    saleman(1003, "业务员", "运营用户"),
    driver(1004, "骑手", "运营用户"),
    shop(2000, "商户", "B端用户"),
    consumer(3000, "消费者", "C端用户");


    private Integer code;
    private String desc;
    private String group;

    UserTypeEnum(Integer code, String desc, String group) {
        this.code = code;
        this.desc = desc;
        this.group = group;
    }


    public static UserTypeEnum getUserTypeByCode(Integer code) {

        switch (code) {
            case 1000:
                return admin;
            case 1001:
                return organ;
            case 1002:
                return agent;
            case 1003:
                return saleman;
            case 1004:
                return driver;
            case 2000:
                return shop;
            case 3000:
                return consumer;
            default:
                return null;
        }
    }


    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public String getGroup() {
        return group;
    }


}
